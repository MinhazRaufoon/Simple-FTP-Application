/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package myftp.lib.Client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import myftp.UI.CommonUIparts.MessageLog;
import myftp.UI.Utility.Utility;
import static myftp.lib.Client.FTP_Client.CLIENT_NOT_LOGGED_IN;
import myftp.lib.ClientFacade;

/**
 *
 * @author RAUF00N
 */
public class ServerListenerThread extends Thread{
    
    public String cur_relative_dir="\\";
    
    FTP_Client this_client;
    DataInputStream in;
    DataOutputStream out;
    
    public ServerListenerThread(FTP_Client this_client)
    {
        try {
            this.this_client = this_client;
            in = new DataInputStream(this_client.myServerSocket.getInputStream());
            out = new DataOutputStream(this_client.myServerSocket.getOutputStream());
        } catch (IOException ex) {
            //Logger.getLogger(ServerListenerThread.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void run()
    {
        ClientFacade.getInstance().writeOnMessageLog("Listening to server on thread "+this.getId(),100);
        waitForServerResponse();
//        ClientFacade.getInstance().writeOnMessageLog("Disconnected from server.",MessageLog.ACTION_CLIENT_DISCONNECTED);
    }
    void waitForServerResponse()
    {
        /*
        responses are:
        LOCKED= server locked
        UNLOCKED= server unlocked
        PAUSED= server paused
        RESUMED = server resumed
        STOPPED = server stopped
        */
        while(this.this_client.CLIENT_STATE == FTP_Client.CLIENT_LOGGED_IN)
        {
            try {
                String response = in.readUTF();
                
                if(response.equals("PAUSED"))
                {
                    new Thread(){
                        public void run()
                        {
                            onServerPause();
                        }
                    }.start();
                    
                }
                else if(response.equals("RESUMED"))
                {                    
                    new Thread(){
                        public void run()
                        {
                            onServerResume();
                        }
                    }.start();
                }
                else if(response.equals("LOCKED"))
                {
                    new Thread(){
                        public void run()
                        {
                            onServerLocked();
                        }
                    }.start();
                    
                }
                else if(response.equals("UNLOCKED"))
                {
                    new Thread(){
                        public void run()
                        {
                            onServerUnlocked();
                        }
                    }.start();
                    
                }
                else if(response.equals("STOPPED"))
                {
                    new Thread(){
                        public void run()
                        {
                            onUnexpectedStopServer();
                        }
                    }.start();
                    
                }
                
                else if(response.equals("DIRS"))
                {
                    ObjectInputStream ois = new ObjectInputStream(in);
                    Object o=null;
                    try {
                        o = ois.readObject();
                    } catch (ClassNotFoundException ex) {
                        Logger.getLogger(ServerListenerThread.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    final File[] files;
                    files = (File[])o;
                    new Thread(){
                        public void run()
                        {
                            receiveDIRS(files);
                        }
                    }.start();
                }
            } catch (IOException ex) {
                System.out.println(ex);
            }
        }
    }
    void receiveDIRS(File[] files)
    {
        try{
            
            ClientFacade.getInstance().writeOnMessageLog("received list from server's "+this.cur_relative_dir, 100);
            ClientFacade.getInstance().loadServerDirectory(files);
            ClientFacade.getInstance().loadServerFiles(files);
        }catch(Exception e){System.out.println(e);}
    }
    void onUnexpectedStopServer()
    {
        // also delete all download and upload currently
        
        // ugly code. does the logout process

        this.this_client.clientFrame.load_btn.toggle();
        
        this.this_client.clientFrame.serverStorageBrowser.scrollpanel.removeAll();
        this.this_client.clientFrame.serverStorageBrowser.scrollpanel.revalidate();
        this.this_client.clientFrame.serverStorageBrowser.scrollpanel.repaint();
        this.this_client.clientFrame.serverStorageBrowser.filesPanel.scrollpanel.removeAll();
        this.this_client.clientFrame.serverStorageBrowser.filesPanel.scrollpanel.revalidate();
        this.this_client.clientFrame.serverStorageBrowser.filesPanel.scrollpanel.repaint();
        
        this.this_client.clientFrame.load_btn.setIconSize(this.this_client.clientFrame.BTN_IMG_SIZE);
        this.this_client.clientFrame.disconnect_btn.setDisabled();
        this.this_client.clientFrame.disconnect_btn.setIconSize(this.this_client.clientFrame.BTN_IMG_SIZE);
        this.this_client.clientFrame.add_bookmark_btn.setDisabled();
        this.this_client.clientFrame.add_bookmark_btn.setIconSize(this.this_client.clientFrame.BTN_IMG_SIZE);
        
        this.this_client.CLIENT_STATE = CLIENT_NOT_LOGGED_IN;
        ClientFacade.getInstance().writeOnMessageLog("Server terminated itself", MessageLog.ACTION_CLIENT_DISCONNECTED);
            try{this.this_client.myServerSocket.close(); } catch (Exception e){}
//            try{this.this_client.downloadRequestSocket.close(); } catch (Exception e){}
//            try{this.this_client.uploadRequestSocket.close(); } catch (Exception e){}
            
        // update UI
        this.this_client.clientFrame.serverStorageBrowser.scrollpanel.removeAll();
        this.this_client.clientFrame.serverStorageBrowser.scrollpanel.revalidate();
        this.this_client.clientFrame.serverStorageBrowser.scrollpanel.repaint();
    }

    private void onServerPause() {
        this.this_client.SERVER_STATE = FTP_Client.SERVER_PAUSED;
        ClientFacade.getInstance().writeOnMessageLog("Server is paused", MessageLog.ACTION_CLIENT_CONNECTED);
    }

    private void onServerResume() {
        this.this_client.SERVER_STATE = FTP_Client.SERVER_RUNNING;
        ClientFacade.getInstance().writeOnMessageLog("Server is resumed", 100);
    }

    private void onServerLocked() {
        this.this_client.SERVER_LOCK_STATE = FTP_Client.SERVER_LOCKED;
        ClientFacade.getInstance().writeOnMessageLog("Server is locked", MessageLog.ACTION_CLIENT_CONNECTED);
    }

    private void onServerUnlocked() {
        this.this_client.SERVER_LOCK_STATE = FTP_Client.SERVER_UNLOCKED;
        ClientFacade.getInstance().writeOnMessageLog("Server is unlocked", 100);
    }
}
