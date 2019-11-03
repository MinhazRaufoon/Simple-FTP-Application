/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package myftp.UI.ClientSpecialUI;

import java.awt.Color;
import java.awt.GridLayout;
import static java.awt.image.ImageObserver.ALLBITS;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import myftp.UI.ClientFrame;
import myftp.UI.Utility.Utility;
import myftp.lib.ClientFacade;

/**
 *
 * @author RAUF00N
 */
public class ServerStorageBrowser extends HddBrowser{
    public ServerStorageBrowser(HddFiles filesPanel,ClientFrame clientframe)
    {
        super(filesPanel,clientframe);
        init_back_btn();
        init_label_panel("Server directories");
        init_scrollpane();
        init_scrollpanel();
        scroll.setBorder(BorderFactory.createMatteBorder(0, 1, 0, 0, Color.decode("#c1bbb3")));
        cur_dir_label.setText("Server's directory");
    }

    @Override
    void enterDir(File dir) {}

    @Override
    void goBack() {
        try{
        	(new BackButtonWork(this)).start();
        } catch(Exception e) {}
    }

    @Override
    public void ListFiles($Directory file) {
        String serverPath = file.file.getPath();
    }

    @Override
    void ListDirs($Directory file) {
        // get path of that file
        (new ServerDirListThread(this,file)).start();
    }

    @Override
    public void onClick$Dir($Directory dir) {
        // get dir->file->path
        // send a request: LIST path
        ClientFacade.getInstance().writeOnMessageLog("request list at "+this.clientframe.this_client.serverListener.cur_relative_dir+"\\"+dir.file.getName(), 100);          
        ListDirs(dir);
        ListFiles(dir);
    }
}

class ServerDirListThread extends Thread
{
    ServerStorageBrowser ssb;
    $Directory file;
    ServerDirListThread(ServerStorageBrowser ssb,$Directory file)
    {
        this.ssb = ssb;
        this.file = file;
    }
    public void run()
    {
        try {
            String request;
            
            DataOutputStream out = new DataOutputStream(ssb.clientframe.this_client.myServerSocket.getOutputStream());
            if(file.file.getName().equals("HOME"))
            {
                out.writeUTF("LS");
                request = ssb.clientframe.this_client.serverListener.cur_relative_dir;
                out.writeUTF(request);
            }
            else
            {
                out.writeUTF("LS");
                request = ssb.clientframe.this_client.serverListener.cur_relative_dir
                        + "/" + file.file.getName();
                ssb.clientframe.this_client.serverListener.cur_relative_dir = request+"";
                out.writeUTF(request);
            }
        } catch (IOException ex) {
            //Logger.getLogger(ServerStorageBrowser.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}

class BackButtonWork extends Thread
{
    ServerStorageBrowser ssb;
    BackButtonWork(ServerStorageBrowser ssb)
    {
        this.ssb = ssb;
    }
    public void run()
    {
        String dir = ssb.clientframe.this_client.serverListener.cur_relative_dir+"";
        int l = dir.lastIndexOf("/");
        String res = "";
        for(int i=0;i<l;i++)
        {
            res+=dir.charAt(i)+"";
        }
        if(res.equals("")) res = "/";
        ssb.clientframe.this_client.serverListener.cur_relative_dir = res+"";        
        
        DataOutputStream out;
        try {
            out = new DataOutputStream(ssb.clientframe.this_client.myServerSocket.getOutputStream());
            out.writeUTF("LS");
            String request = ssb.clientframe.this_client.serverListener.cur_relative_dir;
            out.writeUTF(request);
        } catch (IOException ex) {
            //Logger.getLogger(ServerStorageBrowser.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}