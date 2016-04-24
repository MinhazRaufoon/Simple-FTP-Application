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
import myftp.lib.Client.FTP_Client;
import myftp.lib.ClientFacade;

public class ClientMain extends Thread{

    /**
     * @param args the command line arguments
     */
    public void run()
    {
        ClientFrame clientFrame = new ClientFrame();
        clientFrame.show();
               
        final ClientFacade clientFacade = ClientFacade.getInstance();
        clientFacade.setUI(clientFrame);
        
        FTP_Client this_client = new FTP_Client(clientFrame);
        clientFrame.this_client = this_client;
        
    }
    
    public static void main(String[] args) {
        
        try{
        ClientMain cmain = new ClientMain();
        cmain.start();
        }catch(Exception e){}
    }
}
