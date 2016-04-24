/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package myftp.UI.ClientSpecialUI;

import java.awt.Color;
import java.io.File;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import myftp.UI.ClientFrame;
import myftp.UI.Utility.Utility;

/**
 *
 * @author RAUF00N
 */
public class ClientHddFiles  extends HddFiles{
    public ClientHddFiles(ClientFrame clientframe)
    {
        super(clientframe);
        init_label_panel("Files at current directory");
        init_scrollpane();
        init_scrollpanel();
        scrollpanel.setLayout(new BoxLayout(scrollpanel,BoxLayout.Y_AXIS));
    }

    @Override
    void listFiles(final $Directory dir) {
        synchronized(this){
        scrollpanel.removeAll();
        new Thread(){
            public void run()
            {
                File[] f = Utility.getFileList(dir.file.getPath());
                for(int i=0;i<f.length;i++)
                {
                    if(f[i].isDirectory()) continue;
                    $upFile file = new $upFile(f[i],clientframe);
                    scrollpanel.add(file);

                }
                scrollpanel.revalidate();
            scrollpanel.repaint();
            }
        }.start();
        }
    }
}
