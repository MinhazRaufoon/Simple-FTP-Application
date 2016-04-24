/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package myftp.lib.Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import myftp.UI.CommonUIparts.MessageLog;
import myftp.lib.FileTransfer.FileReceivingThread;
import myftp.lib.FileTransfer.FileSendingThread;
import myftp.lib.ServerFacade;

/**
 *
 * @author RAUF00N
 */
public class UploadRequestHandler extends Thread{

    // my server's instance
    FTP_Server myServer; 
    
    // my serversocket where i look for upload request
    ServerSocket uploadRequestSocket;
    
    public UploadRequestHandler(FTP_Server myServer, ServerSocket upload_server_socket) {
        this.myServer = myServer;
        this.uploadRequestSocket = upload_server_socket;
    }
    
    public void run()
    {
        ServerFacade.getInstance().writeOnMessageLog("Clients' upload request handler thread started", 100);
        waitForUploadRequest();
        ServerFacade.getInstance().writeOnMessageLog("Clients' download request handler thread stopped", 100);
    }
    void waitForUploadRequest()
    {
        while(this.myServer.SERVER_STATE == FTP_Server.SERVER_LOGGED_IN)
        {
            if(this.myServer.SERVER_RUNNING_STATE == FTP_Server.SERVER_PAUSED) continue;
            try {
                // get the socket
                Socket client_upload_socket = this.uploadRequestSocket.accept();
                
                //System.out.println("DownloadRequestHandler: 60 - _port "+client_download_socket.getPort());
                
                FileReceivingThread uploadThread = new FileReceivingThread(client_upload_socket,this.myServer);
                
                this.myServer.uploadThreads.add(uploadThread);
                
                uploadThread.start();
                
            } catch (IOException ex) {
               // Logger.getLogger(DownloadRequestHandler.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
