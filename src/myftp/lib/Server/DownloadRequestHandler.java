/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package myftp.lib.Server;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import myftp.UI.CommonUIparts.MessageLog;
import myftp.lib.FileTransfer.FileSendingThread;
import myftp.lib.ServerFacade;

/**
 *
 * @author RAUF00N
 */
public class DownloadRequestHandler extends Thread{
    
    // my server's instance
    FTP_Server myServer; 
    
    // my serversocket where i look for download request
    ServerSocket downloadRequestSocket;
    
    public DownloadRequestHandler(FTP_Server myServer, ServerSocket downloadRequestSocket)
    {
        this.downloadRequestSocket = downloadRequestSocket;
        this.myServer = myServer;
    }
    public void run()
    {
        waitForDownloadRequest();        
    }
    void waitForDownloadRequest()
    {
        ServerFacade.getInstance().writeOnMessageLog("Clients' download request handler thread started", 100);
        while(this.myServer.SERVER_STATE == FTP_Server.SERVER_LOGGED_IN)
        {
            if(this.myServer.SERVER_RUNNING_STATE == FTP_Server.SERVER_PAUSED) continue;
            
            try {
                // get the socket
                Socket client_download_socket = this.downloadRequestSocket.accept();
                
                FileSendingThread downloadThread = new FileSendingThread(client_download_socket,this.myServer);
                
                this.myServer.downloadThreads.add(downloadThread);
                
                downloadThread.start();
                
            } catch (IOException ex) {
               // Logger.getLogger(DownloadRequestHandler.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        }
        ServerFacade.getInstance().writeOnMessageLog("Clients' download request handler thread stopped", 100);
    }
}
