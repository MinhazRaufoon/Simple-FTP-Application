/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package myftp.UI.ClientSpecialUI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.filechooser.FileSystemView;
import myftp.lib.ClientFacade;

/**
 *
 * @author RAUF00N
 */
public class $Directory extends JPanel
{
    HddBrowser parent;
    JLabel icon,title;
    File file;
    ImageIcon imageicn;
    
    public $Directory(File file,final HddBrowser parent)
    {
        this.parent = parent;
        this.setPreferredSize(new Dimension(65,60));
        this.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.blue));       
        this.setLayout(new BorderLayout());
        
        this.file = file;
        
        // init icon and title
        
        imageicn = new ImageIcon(getClass().getResource("directory.png"));
        Image img = imageicn.getImage() ;  
        Image newimg = img.getScaledInstance( 45,45,  java.awt.Image.SCALE_SMOOTH ) ;  
        imageicn = new ImageIcon( newimg );
        icon = new JLabel(imageicn,JLabel.CENTER);
        
        if(file.getName().length()==0) title = new JLabel(file.toString(),JLabel.CENTER);
        else title = new JLabel(file.getName(),JLabel.CENTER);
        title.setFont(new Font("Calibri",Font.PLAIN,12));
        title.setForeground(Color.black);
        addComponents();
        
        this.setBorder(BorderFactory.createEmptyBorder(1,1,1,1));
        this.setBackground(Color.WHITE);
        
        addMouseListener(
            new MouseListener(){
                @Override
                public void mouseClicked(MouseEvent e) {
                    new Thread()
                    {
                        public void run()
                        {
                            parent.onClick$Dir($Directory.this);
                        }
                    }.start();
                    
                    //parent
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
                
    }
    protected void addComponents()
    {
        this.add(icon,BorderLayout.CENTER);
        this.add(title,BorderLayout.SOUTH);
    }
}
