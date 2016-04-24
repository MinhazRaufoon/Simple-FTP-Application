/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package myftp.lib.Server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import myftp.UI.CommonUIparts.MessageLog;
import myftp.lib.Server.DB.DataBaseAdapter;
import myftp.lib.ServerFacade;

/**
 *
 * @author RAUF00N
 */
public class ClientVerifierThread extends Thread{
    Socket socket;
    FTP_Server myServer;
    
    public ClientVerifierThread(FTP_Server myServer,Socket socket)
    {
        this.socket = socket;
        this.myServer = myServer;
    }
    @Override
    public void run()
    {
        if(verify())
        {
            /*
            wrap the socket object with ClientRequestHandler object and add it to
            server. this will create new thread and wait for client's short length requests
            */
            ServerFacade.getInstance().writeOnMessageLog("Client connected with IP "+socket.getInetAddress()
                    +" at port "+ socket.getPort(), MessageLog.ACTION_CLIENT_CONNECTED);
            
            ClientRequestHandler clientRequestHandler = new ClientRequestHandler(this.myServer,socket);
            this.myServer.ClientRequestHandlers.add(clientRequestHandler);
            clientRequestHandler.start();
        }
        else
        {
            try {
                /*
                close the connection because of invalid login info
                */
                socket.close();
                ServerFacade.getInstance().writeOnMessageLog("Client's login from "+socket.getInetAddress()
                    +" at port "+ socket.getPort()+" failed due to wrong input", MessageLog.ACTION_CLIENT_DISCONNECTED);
            } catch (IOException ex) {
               // Logger.getLogger(ClientVerifierThread.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    /*
    takes username and password from client and verifies it
    */
    boolean verify()
    {
        try {
            DataInputStream in = new DataInputStream(socket.getInputStream());
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());
            
            String username = in.readUTF();
            String password = in.readUTF();
            
            if(username.equals("anonymous"))
            {
                if(password.equals("anonymous"))
                {
                    out.writeUTF("OK");
                    
                    return true;
                }
                else
                {
                    out.writeUTF("FAILED");
                    return false;
                }
            }
            else
            {
                try{
                boolean f =DataBaseAdapter.getInstance().verifyUser(username, password);
                if(f) 
                {
                    out.writeUTF("OK");
                    return true;
                }
                }catch(Exception e){}
                out.writeUTF("FAILED");
            }
            /*
            write database checking code here
            send "OK" if connection valid, else send "CONNECTION FAILED"
            return true or false
            */
        } catch (IOException ex) {
            //Logger.getLogger(ClientVerifierThread.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
}
