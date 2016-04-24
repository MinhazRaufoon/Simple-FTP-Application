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
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import myftp.UI.CommonUIparts.DataTransferPanel;
import myftp.UI.ServerSpecialUI.TransferObject;
import myftp.UI.Utility.Utility;
import myftp.lib.ClientFacade;

/**
 *
 * @author RAUF00N
 */
public class ClientDataTransferObject extends TransferObject{
    public int port=0;
    
    SmallClsButton a=new SmallClsButton(this);
    SmallPPButton b=new SmallPPButton(this);
    JPanel backpanel=new JPanel();
    JPanel btn_panel = new JPanel();
    
    public boolean running = true;
    public boolean paused = false;
    
    public ClientDataTransferObject(String values[],String path,String type)
    {
        super(values,path,type);
        height=40;
        setBorder(BorderFactory.createMatteBorder(3, 0, 3, 0, Color.decode("#ffffff")));
        panel.setBorder(BorderFactory.createMatteBorder(1, 0, 1, 1, Color.decode("#899d78")));
        addComponents();
    }
    void addComponents()
    {
        backpanel.setLayout(new BoxLayout(backpanel,BoxLayout.X_AXIS));
        add(backpanel,BorderLayout.CENTER);
        
        
        
        
        
        panel.add(upPanel);
        panel.add(progbar);
        
        
        
        font_size=12;
        
        SL.setFont(new Font("Calibri",Font.PLAIN,font_size));
        upPanel.add(SL);
        ADDR.setFont(new Font("Calibri",Font.PLAIN,font_size));
        upPanel.add(ADDR);
        TYPE.setFont(new Font("Calibri",Font.PLAIN,font_size));
        upPanel.add(TYPE);
        RATE.setFont(new Font("Calibri",Font.PLAIN,font_size));
        upPanel.add(RATE);
        PRG.setFont(new Font("Calibri",Font.PLAIN,font_size));
        upPanel.add(PRG);
        
        setBorder(BorderFactory.createMatteBorder(3, 0, 3, 0, Color.decode("#ffffff")));
        progbar.setFont(new Font("Calibri",Font.PLAIN,font_size-2));
        
        Dimension dim = panel.getPreferredSize();
        
        
        btn_panel.setLayout(new GridLayout(2,1));
        btn_panel.add(a);
        btn_panel.add(b);
        
        btn_panel.setMaximumSize(new Dimension(
                10,
                dim.height
        ));
        
        panel.setPreferredSize(new Dimension(
                dim.width-10,
                dim.height
        ));
        
        a.setPreferredSize(new Dimension(20,dim.height/2));
        b.setPreferredSize(new Dimension(20,dim.height/2));
        a.setMaximumSize(new Dimension(20,dim.height/2));
        b.setMaximumSize(new Dimension(20,dim.height/2));
        a.setMinimumSize(new Dimension(20,dim.height/2));
        b.setMinimumSize(new Dimension(20,dim.height/2));
        
        
        backpanel.add(btn_panel);
        backpanel.add(panel);
        backpanel.setPreferredSize(dim);
        
    }
    SmallClsButton getClsButton()
    {
        return this.a;
    }
}

class SmallClsButton extends JButton{
    ClientDataTransferObject obj;
    public SmallClsButton(ClientDataTransferObject o)
    {
        this.obj=o;
        String image_directory ="C:\\Users\\RAUF00N\\Documents\\NetBeansProjects\\myFTP\\src\\myftp\\UI\\res\\";
        this.setBackground(Color.decode("#d11141"));
        setFocusPainted(false);
        String imagesrc = "small_close.png";
        ImageIcon icon = new ImageIcon(getClass().getResource(imagesrc));
        Image img = icon.getImage() ;  
        Image newimg = img.getScaledInstance( 10,10,  java.awt.Image.SCALE_SMOOTH ) ;  
        icon = new ImageIcon( newimg );
        this.setIcon(icon);
        this.setBorder(BorderFactory.createMatteBorder(0,0,0,0,Color.decode("#ffffff")));
        
        
        this.addMouseListener(new MouseListener(){
            @Override
            public void mouseClicked(MouseEvent e) {
                new Thread()
                {
                    public void run()
                    {
                        onClickClose();
                    }
                }.start();
                
            }
            void onClickClose()
            {
                obj.running = false;
                //System.out.println(obj.running);
                obj.setVisible(false);
                DataTransferPanel dp = (DataTransferPanel)obj.getParent().getParent().getParent().getParent().getParent().getParent();
                int i=dp.transferObjects.indexOf(obj);
                dp.transferObjects.remove(obj);
                synchronized(this){
                    for(;i<dp.transferObjects.size();i++)
                    {
                        String S =""+dp.transferObjects.get(i).SL.getText();
                        dp.transferObjects.get(i).SL.setText((Integer.parseInt(S)-1)+"");
                    }
                }
                try {
                    //.....
                    DataOutputStream out =new DataOutputStream(ClientFacade.getInstance().clientframe.this_client.myServerSocket.getOutputStream());
                    out.writeUTF("CLS");
                    out.writeUTF(obj.port+"");
                } catch (IOException ex) {
                    Logger.getLogger(SmallClsButton.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            @Override
            public void mousePressed(MouseEvent e) {
            }

            @Override
            public void mouseReleased(MouseEvent e) {
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                setBackground(Color.decode("#fd5244"));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                setBackground(Color.decode("#d11141"));
            }
        
        });
    }
    
}

class SmallPPButton extends JButton{
    ClientDataTransferObject obj;
    String imagename="small_pause.png";
    String image_directory ="C:\\Users\\RAUF00N\\Documents\\NetBeansProjects\\myFTP\\src\\myftp\\UI\\res\\";
    ImageIcon icon;
    public SmallPPButton(ClientDataTransferObject o)
    {
        this.obj=o;
        this.setBackground(Color.white);
        setFocusPainted(false);
        String imagesrc = imagename;
        icon = new ImageIcon(getClass().getResource(imagename));
        Image img = icon.getImage() ;  
        Image newimg = img.getScaledInstance( 15,15,  java.awt.Image.SCALE_SMOOTH ) ;  
        icon = new ImageIcon( newimg );
        this.setIcon(icon);
        setBorder(BorderFactory.createMatteBorder(0, 1, 1, 0, Color.decode("#899d78")));
        
        this.addMouseListener(new MouseListener(){
            @Override
            public void mouseClicked(MouseEvent e) {
                new Thread()
                {
                    public void run()
                    {
                        onClickPause();
                    }
                }.start();
                
            }
            void onClickPause()
            {
                DataOutputStream out=null;
                try {
                    //.....
                    out =new DataOutputStream(ClientFacade.getInstance().clientframe.this_client.myServerSocket.getOutputStream());
                    
                } catch (IOException ex) {
                    Logger.getLogger(SmallClsButton.class.getName()).log(Level.SEVERE, null, ex);
                }
                
                if(imagename.equals("small_pause.png"))
                {
                    imagename = "small_play.png";
                    obj.paused = true;
                    try {
                        out.writeUTF("PAUSE");
                        out.writeUTF(obj.port+"");
                    } catch (IOException ex) {
                        Logger.getLogger(SmallPPButton.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    
                }
                else {
                    imagename = "small_pause.png";
                    obj.paused = false;
                    try {
                        out.writeUTF("RESUME");
                        out.writeUTF(obj.port+"");
                    } catch (IOException ex) {
                        Logger.getLogger(SmallPPButton.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    
                }
                String imagesrc = imagename;
                icon = new ImageIcon(getClass().getResource(imagename));
                Image img = icon.getImage() ;  
                Image newimg = img.getScaledInstance( 15,15,  java.awt.Image.SCALE_SMOOTH ) ;  
                icon = new ImageIcon( newimg );
                setIcon(icon);
                //..........................
                
            }

            @Override
            public void mousePressed(MouseEvent e) {
                
            }

            @Override
            public void mouseReleased(MouseEvent e) {
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                setBorder(BorderFactory.createMatteBorder(1,1,1,1,Color.decode("#002849")));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                setBorder(BorderFactory.createMatteBorder(0, 1, 1, 0, Color.decode("#899d78")));
            }
        
        });
    }
}

