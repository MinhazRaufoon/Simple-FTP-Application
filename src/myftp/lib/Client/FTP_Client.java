/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package myftp.lib.Client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import myftp.UI.ClientFrame;
import myftp.UI.ClientSpecialUI.$Directory;
import myftp.UI.ClientSpecialUI.ClientDataTransferObject;
import myftp.UI.CommonUIparts.MessageLog;
import myftp.lib.ClientFacade;
import myftp.lib.ServerFacade;

/**
 *
 * @author RAUF00N
 */
public class FTP_Client {
    public ClientSettings settings;
    /*
    states of the client
    */
    public static int CLIENT_LOGGED_IN = 0;
    public static int CLIENT_NOT_LOGGED_IN = 1;
    int CLIENT_STATE = CLIENT_NOT_LOGGED_IN;
    
    public static int SERVER_RUNNING = 2;
    public static int SERVER_PAUSED = 3;
    int SERVER_STATE = SERVER_PAUSED;
    
    public static int SERVER_LOCKED = 4;
    public static int SERVER_UNLOCKED = 5;
    int SERVER_LOCK_STATE = SERVER_UNLOCKED;
    
    /*
    socket at which it's connected to server.
    in this socket, client will send short requests
    */
    public Socket myServerSocket;
    
    
    // ports
    public static int DEFAULT_CONNECTION_PORT=1024;
    public static int DOWNLOAD_REQUEST_PORT=1025;
    public static int UPLOAD_REQUEST_PORT=1026;
    int CONNECTION_PORT = DEFAULT_CONNECTION_PORT;
    
    // i o streams for myserver socket
    DataOutputStream out;
    DataInputStream in;
    
    // server listener thread
    public ServerListenerThread serverListener;
    
    ClientFrame clientFrame;
    
    String current_server_address = null;
    
    public FTP_Client(ClientFrame clientFrame)
    {
        this.clientFrame = clientFrame;
        this.settings = new ClientSettings();
    }
    
    public int login(String server_address,String username, String password, int port)
    {
        try {
            this.current_server_address = server_address;
            // initialize the connection socket
            
            this.myServerSocket = new Socket(server_address,port);
            ClientFacade.getInstance().writeOnMessageLog("Connection established with server", port);
            
            // initialize the streams
            out = new DataOutputStream(this.myServerSocket.getOutputStream());
            in = new DataInputStream(this.myServerSocket.getInputStream());
            
            // send login info to server
            out.writeUTF(username);
            out.writeUTF(password);
            
            // read server's response
            String response = in.readUTF();
            
            if(response.equals("OK"))
            {
                /*
                successfull login.
                */
                ClientFacade.getInstance().writeOnMessageLog("Signed in to server "+this.myServerSocket.getInetAddress(), MessageLog.ACTION_CLIENT_CONNECTED);
                
                //&***** this codes may not work
                // initialize the down/uploadsockets
               // this.downloadRequestSocket = new Socket(server_address,DOWNLOAD_REQUEST_PORT);
               // this.uploadRequestSocket = new Socket(server_address,UPLOAD_REQUEST_PORT);
                
                // initialize server listener
                this.serverListener = new ServerListenerThread(this);
                
                // change state to logged in
                this.CLIENT_STATE = CLIENT_LOGGED_IN;
                this.SERVER_STATE = SERVER_RUNNING;
                this.SERVER_LOCK_STATE = SERVER_UNLOCKED;
                
                //start listener
                this.serverListener.start();
                
                
                return 1;
            }
            else if(response.equals("FAILED"))
            {
                /*
                unsuccessfull login
                */
                myServerSocket.close();
                ClientFacade.getInstance().writeOnMessageLog("Sign in procedure failed, client dumped", MessageLog.ACTION_CLIENT_DISCONNECTED);
                return 0;
            }
            //this.downloadRequestSocket = new Socket(server_address,port);
        } catch (Exception ex) {
            ClientFacade.getInstance().writeOnMessageLog("Server not found", MessageLog.ACTION_CLIENT_DISCONNECTED);
            return -1;
        }
        return -1;
    }
    public void logout()
    {
        this.current_server_address = null;
        // update UI
        this.clientFrame.serverStorageBrowser.scrollpanel.removeAll();
        this.clientFrame.serverStorageBrowser.scrollpanel.revalidate();
        this.clientFrame.serverStorageBrowser.scrollpanel.repaint();
        this.clientFrame.serverStorageBrowser.filesPanel.scrollpanel.removeAll();
        this.clientFrame.serverStorageBrowser.filesPanel.scrollpanel.revalidate();
        this.clientFrame.serverStorageBrowser.filesPanel.scrollpanel.repaint();
        //.......
        this.CLIENT_STATE = CLIENT_NOT_LOGGED_IN;
        
        try{ out.writeUTF("DISCONNECT"); } catch (Exception e){}
        
        try{ String S = in.readUTF(); 
            ClientFacade.getInstance().writeOnMessageLog("Server said "+S, 100);
        } catch (Exception e){}
            
        
        
        ClientFacade.getInstance().writeOnMessageLog("Disconnected from server", MessageLog.ACTION_CLIENT_DISCONNECTED);
            try{this.myServerSocket.close(); } catch (Exception e){}
//            try{this.downloadRequestSocket.close(); } catch (Exception e){}
//            try{this.uploadRequestSocket.close(); } catch (Exception e){}
            
            
    }
    
    
    public void downloadFile(String src_path)
    {
        if(this.current_server_address == null) return;
        
        privateDownloadThread dt = new privateDownloadThread(src_path,this);
        dt.start();
        
    }
    public void uploadFile(String src_path)
    {
        if(this.current_server_address == null) return;
        privateUploadThread dt = new privateUploadThread(src_path,this);
        dt.start();
    }
    class privateDownloadThread extends Thread
    {
        String src;
        FTP_Client this_client;
        
        privateDownloadThread(String src,FTP_Client this_client)
        {
            this.src = src + "";
            this.this_client = this_client;
        }
        public void run()
        {
            // no download in paused server
            if(this_client.SERVER_STATE == SERVER_PAUSED) 
            {
                ClientFacade.getInstance().writeOnMessageLog("Server is paused, no data transfer is allowed now", MessageLog.ACTION_CLIENT_DISCONNECTED);
                return;
            }

            // get the destination
            String dest_path = settings.save_dir;
            
            //System.out.println("download to "+settings.save_dir);
            
            Socket downloadSocket;

            try {
                downloadSocket = new Socket(this_client.current_server_address,DOWNLOAD_REQUEST_PORT);
                DataInputStream ds_in = new DataInputStream(downloadSocket.getInputStream());
                DataOutputStream ds_out = new DataOutputStream(downloadSocket.getOutputStream());
                InputStream socketStream= downloadSocket.getInputStream();
                
                // get the port
                int PORT = Integer.parseInt(ds_in.readUTF());
                // send the path of the file to be downloaded
                ds_out.writeUTF(src);
                
                long filesize;
                // get size
                // get the filesize
                filesize = Long.parseLong(ds_in.readUTF());
                //...................
                byte[] buffer = new byte[512];
                String name=settings.save_dir + src.substring(src.lastIndexOf("/")+1);
 //               System.out.println("to be downloaded: "+name);
                File f = new File(name);
                
                OutputStream fileStream=new FileOutputStream(f);
                //.......UI.............
                ClientDataTransferObject dt_ui=(ClientDataTransferObject)ClientFacade.getInstance().addDownload(downloadSocket.getInetAddress().toString(), name);
                
                //.....progress calculation.................
                int number;
                long prog = 0;
                long received =0;
                dt_ui.port = PORT;
                long starttime = System.currentTimeMillis();
                long time;
                while (dt_ui.running) {
                    
                    if(dt_ui.paused)continue;
                    
                    number = socketStream.read(buffer);
                    if(number==-1) break;
                    fileStream.write(buffer,0,number);
                    received+=number;
                    prog = (long)(((double)received/(double)filesize)*100);
                    time = System.currentTimeMillis();
                    //System.out.println(time+" "+starttime);
                    try{
                    long speed = received/(time-starttime);
                    speed*=1024;
                    ClientFacade.getInstance().setTransferRate(dt_ui, speed+"");
                    }catch(Exception e){}
                    
                    ClientFacade.getInstance().setTransferProgress(dt_ui, (int)prog);
                    if(number<512) break;
                }
                out.writeUTF("REF");
                try{fileStream.close();}catch(Exception e){}
                try{ds_in.close();}catch(Exception e){}
                try{ds_out.close();}catch(Exception e){}
                try{socketStream.close();}catch(Exception e){}
                try{downloadSocket.close();}catch(Exception e){}
                try{
                ClientFacade.getInstance().clientframe.clientHddBrowser.ListFiles(
                        new $Directory(ClientFacade.getInstance().clientframe.clientHddBrowser.current_dir
                        ,ClientFacade.getInstance().clientframe.clientHddBrowser)
                );
                }catch (Exception e){}
            } catch (IOException ex) {
                System.out.println(ex);
            }
        }
    }
    class privateUploadThread extends Thread{
        String src;
        FTP_Client this_client;
        privateUploadThread(String src,FTP_Client this_client)
        {
            this.src = src;
            this.this_client = this_client;
        }
        public void run()
        {
            // no upload in paused server
            if(this_client.SERVER_STATE == SERVER_PAUSED || this.this_client.current_server_address==null) 
            {
                ClientFacade.getInstance().writeOnMessageLog("Server is paused, no data transfer is allowed now", MessageLog.ACTION_CLIENT_DISCONNECTED);
                return;
            }
            
            
            
            Socket uploadSocket=null;
            DataOutputStream us_out=null;
            DataInputStream us_in=null;
            try {
                uploadSocket = new Socket(this.this_client.current_server_address,UPLOAD_REQUEST_PORT);
                us_out = new DataOutputStream(uploadSocket.getOutputStream());
                us_in = new DataInputStream(uploadSocket.getInputStream());
            } catch (IOException ex) {
                //Logger.getLogger(FTP_Client.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            
//            System.out.println(src);
            File file = new File(src);
            try {
                int PORT = Integer.parseInt(us_in.readUTF());
                
                us_out.writeUTF(this.this_client.serverListener.cur_relative_dir+"/"+file.getName());
                us_out.writeUTF(file.length()+"");
                
                //System.out.println(this.this_client.serverListener.cur_relative_dir+file.getName()+" "+file.length());
                
                byte[] buffer = new byte[512];
                int number=0;
                long size = file.length();
                long received = 0;
                long progress=0;
                
                FileInputStream fis = new FileInputStream(file);
                OutputStream os = uploadSocket.getOutputStream();
                
                ClientDataTransferObject UI = (ClientDataTransferObject)ClientFacade.getInstance().addUpload(this_client.current_server_address, src);
                
                UI.port = PORT;
                long starttime = System.currentTimeMillis();
                long time;
                time = System.currentTimeMillis();
                
                while(UI.running)
                {
                    if(UI.paused)continue;
                    number = fis.read(buffer);
                    if(number==-1) break;
                    os.write(buffer, 0, number);
                    received += number;
                    progress = (long)(((double)received / (double)size)*100);

                    ClientFacade.getInstance().setTransferProgress(UI, (int)progress);
                    time = System.currentTimeMillis();
                    try{
                    long speed = received/(time-starttime);
                    speed*=1024;
//                    System.out.println(speed);
                    ClientFacade.getInstance().setTransferRate(UI, speed+"");
                    }catch(Exception e){}
                    
                    if(number<512) break;
                }
               
                String path =us_in.readUTF();
                out.writeUTF("LS");
                
                out.writeUTF(path);
            try{ fis.close(); }catch(Exception e){}
            try{ us_out.close(); }catch(Exception e){}
            try{ os.close(); }catch(Exception e){}
            try{ uploadSocket.close(); }catch(Exception e){}
            
            } catch (IOException ex) {
                //Logger.getLogger(FTP_Client.class.getName()).log(Level.SEVERE, null, ex);
            }
             
        }
    }
}
