/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package myftp.lib.Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import myftp.UI.CommonUIparts.MessageLog;
import myftp.lib.ServerFacade;

/**
 * 
 * @author RAUF00N
 */

/*
    This class has the following job
    - wait for client's connection request
    - verifies it
    - if verified, wrap it with a ClientRequestHandler object and send it to server object
*/

public class Connector extends Thread{
    /*
    In this socket, the thread waits for client's connection request
    */
    FTP_Server myServer;
    ServerSocket connection_socket;
       
    public Connector(FTP_Server myServer,ServerSocket connection_socket)
    {
        
        this.myServer = myServer;
        this.connection_socket = connection_socket;
    }
    public void run()
    {
        waitAndConnect();
    }
    public void waitAndConnect()
    {
        ServerFacade.getInstance().writeOnMessageLog("Client connector thread started", 100);
        while(this.myServer.SERVER_STATE == FTP_Server.SERVER_LOGGED_IN)
        {
            // if server is paused or locked, wait
            if(this.myServer.SERVER_RUNNING_STATE == FTP_Server.SERVER_PAUSED
                    ||
                    this.myServer.SERVER_LOCK_STATE == FTP_Server.SERVER_LOCKED)
            {
                continue;
            }
            Socket clientConnSocket = null;
            /*
            Wait for connection requests and accepts it
            */
            try {
                clientConnSocket = this.connection_socket.accept();
                ServerFacade.getInstance().writeOnMessageLog("New connection request came from "+ clientConnSocket.getInetAddress()+" at port "+clientConnSocket.getPort(), 100);
                
                ClientVerifierThread verifier = new ClientVerifierThread(this.myServer,clientConnSocket);
                verifier.start();
                
                
                
            } catch (IOException ex) {
                //Logger.getLogger(Connector.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        }
        ServerFacade.getInstance().writeOnMessageLog("Client connector thread stopped", 100);
    }
}

