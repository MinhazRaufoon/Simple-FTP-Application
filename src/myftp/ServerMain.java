/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package myftp;

/**
 *
 * @author RAUF00N
 */
import myftp.UI.*;
import myftp.lib.Server.DB.DataBaseAdapter;
import myftp.lib.Server.FTP_Server;
import myftp.lib.ServerFacade;

public class ServerMain extends Thread
{
    public void run()
    {
        ServerFrame serverFrame = new ServerFrame();
        serverFrame.show();
        
        ServerFacade serverFacade;
        serverFacade = ServerFacade.getInstance();
        serverFacade.setUI(serverFrame);
        
        
        FTP_Server server = new FTP_Server();
        serverFrame.server = server;
        
        
    }
    public static void main(String[] args)
    {
        try{
        ServerMain smain = new ServerMain();
        smain.start();
        }catch(Exception e){
        	System.out.println(e.getStackTrace());
        }
    }
}
