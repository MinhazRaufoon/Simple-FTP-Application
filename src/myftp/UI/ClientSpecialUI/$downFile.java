/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package myftp.UI.ClientSpecialUI;

import java.awt.Color;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import javax.swing.ImageIcon;
import myftp.UI.ClientFrame;

/**
 *
 * @author RAUF00N
 */
public class $downFile extends $upFile{
    
    public $downFile(File file,ClientFrame clientframe) {
        super(file,clientframe);
        data_transfer_btn.setText("Download");
        ImageIcon ic=new ImageIcon(getClass().getResource("download.png"));
        Image image = ic.getImage() ;  
        Image newimage = image.getScaledInstance( 20,20,  java.awt.Image.SCALE_SMOOTH ) ;  
        ic = new ImageIcon( newimage );
        data_transfer_btn.setIcon(ic);
    }
    protected void addDownload_UploadMouseListener()
    {
        data_transfer_btn.addMouseListener(new MouseListener(){
            
            @Override
            public void mouseClicked(MouseEvent e) {
                downloadFile();
            }

            @Override
            public void mousePressed(MouseEvent e) {
            
            }

            @Override
            public void mouseReleased(MouseEvent e) {
            
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                data_transfer_btn.setBackground(Color.decode("#fff568"));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                data_transfer_btn.setBackground(Color.decode("#ffffff"));
            }
            
        });
    }
    private void downloadFile()
    {
        this.clientframe.this_client.downloadFile(this.file.getAbsolutePath());
    }
}
