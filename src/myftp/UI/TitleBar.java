/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package myftp.UI;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.*;

/**
 *
 * @author RAUF00N
 */
public class TitleBar extends JPanel
{
    int size;
    
    String image_directory ="C:\\Users\\RAUF00N\\Documents\\NetBeansProjects\\myFTP\\src\\myftp\\UI\\res\\";
    ImageIcon cls=new ImageIcon(getClass().getResource("cls.png"));
    ImageIcon mx=new ImageIcon(getClass().getResource("mx.png"));
    ImageIcon mn=new ImageIcon(getClass().getResource("mn.png"));
    
    JPanel logo=new JPanel();
    JLabel serverLabel=new JLabel("",SwingConstants.CENTER);
    JPanel buttons=new JPanel();
    
    TButton btn_cls,btn_min,btn_max;
    JFrame frame;
    public TitleBar(final JFrame frame)
    {
        this.frame = frame;
        this.setBackground(Color.white);
        this.setLayout(new GridLayout(1,3));
        this.add(logo);
        this.add(serverLabel);
        this.add(buttons);
        
        logo.setBackground(Color.white);
        
        buttons.setBackground(Color.white);
        serverLabel.setForeground(Color.decode("#a1797c"));
        buttons.setLayout(new FlowLayout(FlowLayout.RIGHT));
        
        btn_cls = new TButton();
        btn_max = new TButton();
        btn_min = new TButton();
        
        buttons.add(btn_min);
        buttons.add(btn_max);
        buttons.add(btn_cls);
        
        btn_cls.addMouseListener(new MouseListener(){
            @Override
            public void mouseClicked(MouseEvent e) {
                System.exit(0);
            }

            @Override
            public void mousePressed(MouseEvent e) {
            }

            @Override
            public void mouseReleased(MouseEvent e) {
            }

            @Override
            public void mouseEntered(MouseEvent e) {
            }

            @Override
            public void mouseExited(MouseEvent e) {
            }
        
        });
        btn_min.addMouseListener(new MouseListener(){
            @Override
            public void mouseClicked(MouseEvent e) {
                frame.setState ( Frame.ICONIFIED );
            }

            @Override
            public void mousePressed(MouseEvent e) {
            //    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public void mouseReleased(MouseEvent e) {
              //  throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public void mouseEntered(MouseEvent e) {
               // throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public void mouseExited(MouseEvent e) {
                //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }
            
        });
    }
    public void setButtonSize(int size)
    {
        this.size=size;
        btn_min.setSize(size, size);
        btn_max.setSize(size, size);
        btn_cls.setSize(size, size);
    }
    public void setTitleFontSize(int size)
    {
        serverLabel.setFont(new Font("Century Gothic",Font.PLAIN,size));
        
    }
    public void init()
    {
        Image img = mx.getImage() ;  
        Image newimg = img.getScaledInstance( size,size,  java.awt.Image.SCALE_SMOOTH ) ;  
        mx = new ImageIcon( newimg );
        btn_max.setIcon(mx);
        
        img = mn.getImage() ;  
        newimg = img.getScaledInstance( size,size,  java.awt.Image.SCALE_SMOOTH ) ;  
        mn = new ImageIcon( newimg );
        btn_min.setIcon(mn);
        
        img = cls.getImage() ;  
        newimg = img.getScaledInstance( size,size,  java.awt.Image.SCALE_SMOOTH ) ;  
        cls = new ImageIcon( newimg );
        btn_cls.setIcon(cls);
    }
    public void setConnectedServerTitle(String S)
    {
        serverLabel.setText(S);
    }
}

class TButton extends JButton
{
    public TButton()
    {
        setOpaque(false);
        setBackground(Color.white);
        setForeground(Color.red);
        setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        
    }
}