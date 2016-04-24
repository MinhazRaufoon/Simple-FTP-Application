/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package myftp.lib.Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import myftp.UI.CommonUIparts.MessageLog;
import myftp.lib.FileTransfer.FileReceivingThread;
import myftp.lib.FileTransfer.FileSendingThread;
import myftp.lib.ServerFacade;

/**
 *
 * @author RAUF00N
 */
public class FTP_Server {
    
    /*
    states of server
    */
    
    public int SERVER_STATE;
    public static int SERVER_LOGGED_IN = 5;
    public static int SERVER_LOGGED_OUT = 6;
    
    public int SERVER_RUNNING_STATE;
    public static int SERVER_PAUSED = 3;
    public static int SERVER_RESUMED = 4;
    
    public int SERVER_LOCK_STATE;
    public static int SERVER_LOCKED = 1;
    public static int SERVER_UNLOCKED = 2;
    /*
        Default port numbers
    */
    public static int DEFAULT_CONNECTION_PORT = 1024;
    public static int DEFAULT_DOWNLOAD_PORT = 1025;
    public static int DEFAULT_UPLOAD_PORT = 1026;
    
    /*
        Port numbers
    */
    private int CONNECTION_PORT = DEFAULT_CONNECTION_PORT;
    private int DOWNLOAD_PORT = DEFAULT_DOWNLOAD_PORT;
    private int UPLOAD_PORT = DEFAULT_UPLOAD_PORT;
    
    /*
        Server's IP or web address
    */
    private String server_address = null;
    
    /*
        Settings object, contains necessary informations
    */
    public Settings server_settings;
    
    /*
    Sockets: for establishing connections, downloading and uploading
    */
    ServerSocket conn_server_socket;
    ServerSocket download_server_socket;
    ServerSocket upload_server_socket;
    
    /*
    Threads
    */
    Connector clientConnector;
    DownloadRequestHandler downloadRequestHandler;
    UploadRequestHandler uploadRequestHandler;
    
    ArrayList<ClientRequestHandler>ClientRequestHandlers
            = new ArrayList<>();
    public ArrayList<FileSendingThread>downloadThreads 
            = new ArrayList<>();
    public ArrayList<FileReceivingThread>uploadThreads 
            = new ArrayList<>();
    TransferSchedulerThread downloadSchedulerThread;
    TransferSchedulerThread uploadSchedulerThread;
    
    /*
    serversockets for download and upload
    you use only 1027 - 64999
    */
    public ServerSocket file_transfer_ports[] = new ServerSocket[65000];
    
    
    /*
    the following flags are for checking if serversocket[i] is available or not
    */
    public boolean flie_transfer_port_available[] = new boolean[65000];
    public ArrayList free_ports_queue = new ArrayList();
    
    
    
    public FTP_Server()
    {
        /*
        Instantiate the setting object
        */
        server_settings = new Settings();
        ServerFacade.getInstance().writeOnMessageLog("Server created", 100);
    }
    class LoginThread extends Thread
    {
        String myServerAddress;
        String password;
        int port;
        LoginThread(String myServerAddress /*the name of my server*/, String password, int port)
        {
            this.myServerAddress = myServerAddress;
            this.password = password;
            this.port =port;
        }
        public void run()
        {
            try {
            // here put some password verification
            /*
            Instantiate the sockets
            */
            conn_server_socket = new ServerSocket(DEFAULT_CONNECTION_PORT);
            server_address = myServerAddress;
            CONNECTION_PORT = port;
            
            download_server_socket = new ServerSocket(DOWNLOAD_PORT);
            upload_server_socket = new ServerSocket(UPLOAD_PORT);
            
            // Set server to loggedin
            SERVER_STATE = SERVER_LOGGED_IN;
            
            ServerFacade.getInstance().writeOnMessageLog("Logged in to server "+
                    conn_server_socket.getInetAddress(), 100);
            
            initServer();
            
            } catch (IOException ex) {
                ServerFacade.getInstance().writeOnMessageLog("Cannot log in to server "+
                        conn_server_socket.getInetAddress(), MessageLog.ACTION_CLIENT_DISCONNECTED);
            }
        }
        void initServer()
        {
            //init_transfer_ports();
            /*
            instantiate the clientConnector which connects new client
            */
            clientConnector = new Connector(FTP_Server.this,conn_server_socket);
            /*
            instantiate a thread which looks for new download requests
            */
            downloadRequestHandler = new DownloadRequestHandler(FTP_Server.this,download_server_socket);
            /*
            instantiate a thread which looks for new upload requests
            */
            uploadRequestHandler = new UploadRequestHandler(FTP_Server.this,upload_server_socket);

            /*
            instantiate transfer scheduler who schedules the list of transfers
            */
            downloadSchedulerThread = new TransferSchedulerThread(FTP_Server.this,"D");
            uploadSchedulerThread = new TransferSchedulerThread(FTP_Server.this,"U");

            startServer();
        }
        void startServer()
        {

            // set server to resumed and unlocked
            SERVER_RUNNING_STATE = SERVER_RESUMED;
            SERVER_LOCK_STATE = SERVER_UNLOCKED;
            /*
            server starts as resumed and unlocked
            */
            SERVER_RUNNING_STATE = SERVER_RESUMED;
            SERVER_LOCK_STATE = SERVER_UNLOCKED;

            ServerFacade.getInstance().writeOnMessageLog("Server started", MessageLog.ACTION_SERVER_START);

            /*
            start the thread and look for new connections
            */
            clientConnector.start();
            /*
            start the thread and look for downloads
            */
            downloadRequestHandler.start();
            /*
            start the thread and look for uploads
            */
            uploadRequestHandler.start();

            // start the schedulers
            //this.downloadSchedulerThread.start();
            //this.uploadSchedulerThread.start();


        }
    }
    public void login(String myServerAddress /*the name of my server*/, String password, int port)
    {
        (new LoginThread(myServerAddress,password,port)).start();
        
    }
    public void logOut()
    {
        Thread t =(new ResponseSenderThread("STOPPED",this));
        t.start();
        try {
            t.join();
            (new LogOutThread()).start();
        } catch (InterruptedException ex) {
            Logger.getLogger(FTP_Server.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
    }
    class LogOutThread extends Thread{

        public void run()
        {
            SERVER_STATE = SERVER_LOGGED_OUT;

            ClientRequestHandlers.removeAll(ClientRequestHandlers);
            ServerFacade.getInstance().writeOnMessageLog("Logged out from server "+conn_server_socket.getInetAddress(), MessageLog.ACTION_SERVER_STOP);

            // clear arrays
            ClientRequestHandlers.removeAll(ClientRequestHandlers);
            downloadThreads.removeAll(downloadThreads);
            uploadThreads.removeAll(uploadThreads);

            try { conn_server_socket.close();} catch(Exception e){}
            try { download_server_socket.close();} catch(Exception e){}
            try { upload_server_socket.close();} catch(Exception e){}
        }
    }
    public void PauseServer()
    {
        (new ResponseSenderThread("PAUSED",this)).start();
        this.SERVER_RUNNING_STATE = SERVER_PAUSED;
        // send "PAUSED" to all
        
        ServerFacade.getInstance().writeOnMessageLog("Server paused", MessageLog.ACTION_CLIENT_DISCONNECTED);
    }
    public void ResumeServer()
    {
        (new ResponseSenderThread("RESUMED",this)).start();
        this.SERVER_RUNNING_STATE = SERVER_RESUMED;
        // send everyone "RESUMED" message
        
        ServerFacade.getInstance().writeOnMessageLog("Server resumed", 100);
    }
    public void LockServer()
    {
        (new ResponseSenderThread("LOCKED",this)).start();
        this.SERVER_LOCK_STATE = SERVER_LOCKED;
        ServerFacade.getInstance().writeOnMessageLog("Server locked", MessageLog.ACTION_CLIENT_DISCONNECTED);        
    }
    public void UnlockServer()
    {
        (new ResponseSenderThread("UNLOCKED",this)).start();
        this.SERVER_LOCK_STATE = SERVER_UNLOCKED;
        ServerFacade.getInstance().writeOnMessageLog("Server unlocked", 100);
    }
    
    
}
