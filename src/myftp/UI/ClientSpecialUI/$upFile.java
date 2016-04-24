/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package myftp.UI.ClientSpecialUI;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.filechooser.FileSystemView;
import myftp.UI.ClientFrame;

/**
 *
 * @author RAUF00N
 */
public class $upFile extends JPanel{
    JLabel icon,title,size;
    JButton data_transfer_btn;
    File file;
    ImageIcon imageicn;
    JPanel temp_pnl = new JPanel();
    ClientFrame clientframe;
    
    public $upFile(File file,ClientFrame clientframe)
    {
        this.clientframe = clientframe;
        this.file = file;
        this.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.white));       
        this.setPreferredSize( new Dimension(100,30) );
        this.setLayout(new GridLayout(1,4));
        
        Icon ico = FileSystemView.getFileSystemView().getSystemIcon(file);
        imageicn = (ImageIcon)ico;
        Image img = imageicn.getImage() ;  
        Image newimg = img.getScaledInstance( 20,20,  java.awt.Image.SCALE_SMOOTH ) ;  
        imageicn = new ImageIcon( newimg );
        icon = new JLabel(imageicn,JLabel.CENTER);
        icon.setBorder(BorderFactory.createMatteBorder(2, 2,2, 2, Color.white));
        
        title = new JLabel(file.getName(),JLabel.LEFT);
        title.setFont(new Font("Calibri",Font.PLAIN,13));
        title.setForeground(Color.black);
        title.setBorder(BorderFactory.createMatteBorder(2, 2,2, 2, Color.white));
        
        long len = file.length();
        String sizestr="";
        if(len<1000) sizestr = len+" Bytes";
        else if(len<1000000) sizestr = ((double)len/1000.0)+" KB";
        else if(len<1000000000) sizestr = ((double)len/1000000.0)+" MB";
        else sizestr = ((double)len/1000000000.0)+" GB";
        size = new JLabel(sizestr,JLabel.RIGHT);
        size.setFont(new Font("Calibri",Font.PLAIN,13));
        size.setForeground(Color.black);
        size.setBorder(BorderFactory.createMatteBorder(2, 2,2, 2, Color.white));
        
        data_transfer_btn = new JButton("Upload");
        data_transfer_btn.setPreferredSize(new Dimension(20,20));
        data_transfer_btn.setBorder(BorderFactory.createMatteBorder(2, 2,2, 2, Color.white));
        
        this.setBorder(BorderFactory.createEmptyBorder(1,1,1,1));
        this.setBackground(Color.WHITE);
        
        temp_pnl.setBackground(Color.white);
        temp_pnl.setLayout(new BoxLayout(temp_pnl,BoxLayout.X_AXIS));
        
        temp_pnl.add(icon);
        temp_pnl.add(title);
        this.add(temp_pnl);
        this.add(size);
        this.add(data_transfer_btn);
        
        addMouseListener(
            new MouseListener(){
                @Override
                public void mouseClicked(MouseEvent e) {
                    
                }

                @Override
                public void mousePressed(MouseEvent e) {
                }

                @Override
                public void mouseReleased(MouseEvent e) {
                }

                @Override
                public void mouseEntered(MouseEvent e) {
                    setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.decode("#589ad5")));
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.decode("#ffffff")));
                }
                }
        );
        data_transfer_btn.setBackground(Color.white);
        data_transfer_btn.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        data_transfer_btn.setFont(new Font("Calibri",Font.PLAIN,13));
        
        
        ImageIcon ic=new ImageIcon(getClass().getResource("upload.gif"));
        Image image = ic.getImage() ;  
        Image newimage = image.getScaledInstance( 20,20,  java.awt.Image.SCALE_SMOOTH ) ;  
        ic = new ImageIcon( newimage );
        data_transfer_btn.setIcon(ic);
        data_transfer_btn.setFocusPainted(false);
        
        addDownload_UploadMouseListener();
    }
    protected void addDownload_UploadMouseListener()
    {
        data_transfer_btn.addMouseListener(new MouseListener(){
            
            @Override
            public void mouseClicked(MouseEvent e) {
                uploadFileToServer();
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
    private void uploadFileToServer()
    {
        this.clientframe.this_client.uploadFile(this.file.getAbsolutePath());
    }
}
