

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package myftp.UI.ClientSpecialUI;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.LayoutManager;
import java.io.File;
import java.util.ArrayList;
import java.util.Stack;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import myftp.UI.ClientFrame;
import myftp.UI.Utility.Utility;
/**
 *
 * @author RAUF00N
 */
public class ClientHddBrowser extends HddBrowser {
    
    public ClientHddBrowser(HddFiles filesPanel,ClientFrame clientframe)
    {
        super(filesPanel,clientframe);
        localDrives = Utility.getLocalDrives();
        init_back_btn();
        init_label_panel("Hard drive directories");
        init_scrollpane();
        init_scrollpanel();
        
        initDirList();
        
        
        
    }
    void initDirList()
    {
        int R,C;
        C = 6; R=(localDrives.length / 6)+1;
        cur_dir_label.setText("Root");
        scrollpanel.setLayout(new GridLayout(R,C));
        
        for(int i=0;i<localDrives.length;i++)
        {
            // if(!localDrives[i].isHidden()) continue;
            scrollpanel.add(new $Directory(localDrives[i],this));
        }
    }
    void enterDir(File dir)
    {
    }
    void goBack()
    {
        scrollpanel.removeAll();
        scrollpanel.revalidate();
        scrollpanel.repaint();
        if(count>0) count--;
        if(count>0)
        {
            current_dir = current_dir.getParentFile();
            ListDirs(new $Directory(current_dir,this));
            ListFiles(new $Directory(current_dir,this));
        }
        else 
        {
            filesPanel.scrollpanel.removeAll();
            filesPanel.scrollpanel.revalidate();
            filesPanel.scrollpanel.repaint();
            initDirList();
        }
    }
    public void ListFiles($Directory dir)
    {
        filesPanel.listFiles(dir);
    }
    public void ListDirs($Directory dir)
    {
        scrollpanel.removeAll();
        scrollpanel.revalidate();
        scrollpanel.repaint();
        
        File[] f = Utility.getFileList(dir.file.getPath());
        cur_dir_label.setText(""+dir.file.getPath());
        current_dir = dir.file;
        
        int R,C;
        C = 6; R=(f.length / 6)+1;
        scrollpanel.setLayout(new GridLayout(R,C));
        
        int cnt=0;
        for(File i: f)
        {
            if(i.isDirectory()&&!i.isHidden())
            {
                scrollpanel.add(new $Directory(i,this));
                cnt++;
            }
        }
        while(cnt<R*C)
        {
            scrollpanel.add(new empty_panel());
            cnt++;
        }
        scrollpanel.revalidate();
        scrollpanel.repaint();
    }
    public void onClick$Dir($Directory dir)
    {
        count++;
        synchronized(this)
        {
            ListDirs(dir);
            enterDir(dir.file);
            ListFiles(dir);
        }
        
    }
}
