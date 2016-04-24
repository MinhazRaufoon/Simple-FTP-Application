/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package myftp.lib.Server;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import myftp.UI.CommonUIparts.MessageLog;
import myftp.UI.Utility.Utility;
import myftp.lib.ClientFacade;
import myftp.lib.FileTransfer.AsyncFileTransferThread;
import myftp.lib.FileTransfer.FileReceivingThread;
import myftp.lib.FileTransfer.FileSendingThread;
import myftp.lib.ServerFacade;

/**
 *
 * @author RAUF00N
 */

/*
    this class has following function-
    - wait for connected client's requests like- list, stop or pause any download, disconnect
*/
public class ClientRequestHandler extends Thread{
    /*
    Request formats--
    disconnect: "DISCONNECT"
    list: "LS [path]"
    pause download: "PAUSE [port]"
    resume download: "RESUME [port]"
    register: "REG [username] [password]"
    */
    
    /*
    Response formats--
    disconnect: "DISCONNECTED"
    list: send File[]
    pause download: "PAUSED [port]"
    resume download: "RESUME [port]"
    register: "REGISTRATION DONE" or "REGISTRATION FAILED"
    */
    
    public Socket clientSocket;
    FTP_Server myServer;
    
    /*
    io streams
    */
    DataInputStream in;
    DataOutputStream out;
    
    // this flag only alters when DISCONNECT request came
    boolean runflag = true;
    
    // like C://FTP SERVER//HOME//HOME//
    public String current_dir_prefix = new String();
    public String current_dir_relative = new String();
    
    ClientRequestHandler(FTP_Server myServer,Socket me)
    {
        this.current_dir_prefix = myServer.server_settings.home_directory+"";
        this.current_dir_relative="\\";
        
        //System.out.println(""+myServer.server_settings.home_directory);
        
        this.myServer = myServer;
        this.clientSocket = me;
        try {
            this.in = new DataInputStream(me.getInputStream());
            this.out = new DataOutputStream(me.getOutputStream());
        } catch (IOException ex) {
//            Logger.getLogger(ClientRequestHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public void run()
    {
        sendHomeToClient();
        waitForShortRequests();        
    }
    void waitForShortRequests()
    {
        while(this.myServer.SERVER_STATE == FTP_Server.SERVER_LOGGED_IN && runflag)
        {
            /*
                read requests on input stream, make request object, implement the request
                immidiately in new thread
            */
            try {
                // read a request
                String request = in.readUTF();
                request.trim();
                
                // do nothing if server paused
                if(this.myServer.SERVER_RUNNING_STATE == FTP_Server.SERVER_PAUSED) 
                {
                    // send server paused info
                    try {
                        out.writeUTF("SERVER PAUSED");
                    } catch (IOException ex) {
                    }
                    continue;
                }
                else
                {
                    processRequest(request);
                }
            } catch (IOException ex) { }
        }
        ServerFacade.getInstance().writeOnMessageLog("Client "+this.clientSocket.getInetAddress()+" has been disconnected", MessageLog.ACTION_CLIENT_DISCONNECTED);
    }
    void processRequest(String request)
    {
        if(request.equals("DISCONNECT"))
        {
            disconnectClient();
        }
        else if(request.equals("REF"))
        {
            //System.out.println("refresh");
            refresh();
        }
        else if(request.equals("LS"))
        {
            try {
                String path = in.readUTF();
                
                sendList(path);
            } catch (IOException ex) {
                //Logger.getLogger(ClientRequestHandler.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        else if(request.equals("REG"))
        {
            // do later
        }
        
        else if(request.equals("PAUSE"))
        {
            try {
                final int port = Integer.parseInt(in.readUTF());
                
                new Thread()
                {
                    public void run()
                    {
                        pauseTransfer(port);
                    }
                }.start();
                
            } catch (IOException ex) {
                //Logger.getLogger(ClientRequestHandler.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        else if(request.equals("RESUME"))
        {
            try {
                final int port = Integer.parseInt(in.readUTF());
                
                new Thread()
                {
                    public void run()
                    {
                        resumeTransfer(port);
                    }
                }.start();
                
            } catch (IOException ex) {
                //Logger.getLogger(ClientRequestHandler.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        else if(request.equals("CLS"))
        {
            try {
                final int port = Integer.parseInt(in.readUTF());
                
                new Thread()
                {
                    public void run()
                    {
                        cancelTransfer(port);
                    }
                }.start();
                
            } catch (IOException ex) {
                //Logger.getLogger(ClientRequestHandler.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    void pauseTransfer(int port)
    {
        for(FileReceivingThread frt: this.myServer.uploadThreads)
        {
            if(frt.socket.getPort()==port)
            {
               frt.Pause();
               return;
            }
        }
        for(FileSendingThread frt: this.myServer.downloadThreads)
        {
            if(frt.socket.getPort()==port)
            {
               frt.Pause();
               return;
            }
        }
    }
    void resumeTransfer(int port)
    {
        for(FileReceivingThread frt: this.myServer.uploadThreads)
        {
            if(frt.socket.getPort()==port)
            {
               frt.Resume();
               return;
            }
        }
        for(FileSendingThread frt: this.myServer.downloadThreads)
        {
            if(frt.socket.getPort()==port)
            {
               frt.Resume();
               return;
            }
        }
    }
    void cancelTransfer(int port)
    {
        System.out.println("xxxxxx");
        //System.out.println(this.myServer.uploadThreads.size());
        for(FileReceivingThread frt: this.myServer.uploadThreads)
        {
            
            if(frt.socket.getPort()==port)
            {
               frt.Terminate();
               return;
            }
        }
        for(FileSendingThread frt: this.myServer.downloadThreads)
        {
            if(frt.socket.getPort()==port)
            {
               frt.Terminate();
               return;
            }
        }
    }
    private void disconnectClient() {
        try {
            /*
            delete all ongoing down/uploads of this client from server,
            stop all threads which are dedicated for this client.
            notify client and close this socket.
            */
            out.writeUTF("BYE");
            
            for(FileSendingThread fst: this.myServer.downloadThreads)
            {
                if(fst.socket.getInetAddress()==this.clientSocket.getInetAddress())
                {
                    fst.Terminate();
                    this.myServer.downloadThreads.remove(fst);
                }
            }
            for(FileReceivingThread fst: this.myServer.uploadThreads)
            {
                if(fst.socket.getInetAddress()==this.clientSocket.getInetAddress())
                {
                    fst.Terminate();
                    this.myServer.uploadThreads.remove(fst);
                }
            }
            this.runflag = false;
            this.clientSocket.close();
            
        } catch (IOException ex) {
            Logger.getLogger(ClientRequestHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void sendList(String path) {
        /*
        make File[] from the path, send it to client through outputstream
        in byte mode
        */
        this.current_dir_relative = path+"";
        
        ServerFacade.getInstance().writeOnMessageLog("Client "+this.clientSocket.getInetAddress()+""
                + " requested list at "+path, 100);
        
        File[] files = Utility.getFileList(this.current_dir_prefix+this.current_dir_relative);
        
        
        try{
            out.writeUTF("DIRS");
            ObjectOutputStream oos = new ObjectOutputStream(out);
            oos.writeObject(files);
        }
        catch (Exception e)
        {
            
        }
    }


    private void sendHomeToClient() {
        sendList("\\");
    }
    public void refresh()
    {
        File[] files = Utility.getFileList(this.current_dir_prefix+this.current_dir_relative);
        try{
            out.writeUTF("DIRS");
            ObjectOutputStream oos = new ObjectOutputStream(out);
            oos.writeObject(files);
        }
        catch (Exception e)
        {
            
        }
        
    }
}
