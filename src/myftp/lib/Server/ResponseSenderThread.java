/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package myftp.lib.Server;

import java.io.IOException;

/**
 *
 * @author RAUF00N
 */
public class ResponseSenderThread extends Thread{
    String msg;
    FTP_Server server;
    
    public ResponseSenderThread(String msg, FTP_Server server)
    {
        this.server = server;
        this.msg = msg;
    }
    
    public void run()
    {
        for(ClientRequestHandler crh: server.ClientRequestHandlers)
        {
            if(server.SERVER_STATE == FTP_Server.SERVER_LOGGED_OUT) break;
            try {
                crh.out.writeUTF(msg);
            } catch (IOException ex) {
                    //Logger.getLogger(FTP_Server.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
