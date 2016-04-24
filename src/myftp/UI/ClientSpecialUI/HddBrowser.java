/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package myftp.UI.ClientSpecialUI;

import java.awt.*;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.LayoutManager;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.util.ArrayList;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import static javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER;
import javax.swing.plaf.basic.BasicScrollBarUI;
import myftp.UI.ClientFrame;
import myftp.UI.Utility.Utility;

/**
 *
 * @author RAUF00N
 */
public abstract class HddBrowser extends JPanel{
    ClientFrame clientframe;
    int count=0;
    protected JLabel cur_dir_label=new JLabel("",JLabel.CENTER);
    public File current_dir;
    protected JPanel label_panel = new JPanel();
    protected JPanel temp_panel = new JPanel();
    protected JPanel temp_panel_2 = new JPanel();
    public JPanel scrollpanel = new JPanel();
    protected JScrollPane scroll = new JScrollPane(temp_panel);
    protected JButton back_btn = new JButton();
    protected JPanel btn_pnl = new JPanel();
    public HddFiles filesPanel;
    
    public static File[] localDrives;
    
    
    public HddBrowser(HddFiles filespanel,ClientFrame clientframe)
    {
        this.clientframe = clientframe;
        setBorder(BorderFactory.createEmptyBorder(1, 1, 1, 1));
        LayoutManager layout = new BorderLayout();
        this.setLayout(layout);
        this.setBackground(Color.white);
        this.filesPanel = filespanel;
        
    }
    void init_back_btn()
    {
        btn_pnl.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.decode("#c1bbb3")));
        btn_pnl.setLayout(new FlowLayout(FlowLayout.RIGHT));
        back_btn.setPreferredSize(new Dimension(23,23));
        back_btn.setBackground(Color.white);
        back_btn.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        back_btn.setFocusPainted(false);
        btn_pnl.add(back_btn);
        
        ImageIcon icon = new ImageIcon(getClass().getResource("back.png"));
        Image img = icon.getImage() ;  
        Image newimg = img.getScaledInstance( 23,23,  java.awt.Image.SCALE_SMOOTH ) ;  
        icon = new ImageIcon( newimg );
        back_btn.setIcon(icon);
        
        back_btn.addMouseListener(new MouseListener(){
            @Override
            public void mouseClicked(MouseEvent e) {
                goBack();
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
    }
    void init_label_panel(String title)
    {
        label_panel.setLayout(new GridLayout(1,2));
        
        JLabel label = new JLabel(title);
        label.setFont(new Font("Calibri", Font.PLAIN, 15));
        label.setForeground(Color.decode("#80af71"));
        label.setPreferredSize(new Dimension(200,20));
        label.setBorder(BorderFactory.createEmptyBorder(0, 3, 3, 0));
        
        label_panel.setBackground(Color.white);
        btn_pnl.setBackground(Color.white);
        label_panel.add(label);
        label_panel.add(btn_pnl);
        label.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.decode("#c1bbb3")));
        
        this.add(label_panel,BorderLayout.PAGE_START);
        this.add(scroll,BorderLayout.CENTER);
    }
    void init_scrollpane()
    {
        this.scroll.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));
        this.scroll.setBackground(Color.white);
    }
    void init_scrollpanel()
    {
        scrollpanel.setBackground(Color.white);
        //scrollpanel.setLayout(new BoxLayout(scrollpanel,BoxLayout.Y_AXIS));
        temp_panel.setBackground(Color.white);
        temp_panel_2.setBackground(Color.white);
        temp_panel.setLayout(new BorderLayout());
        temp_panel_2.setLayout(new BorderLayout());
        cur_dir_label.setMinimumSize(new Dimension(400,15));
        cur_dir_label.setFont(new Font("Calibri",Font.PLAIN,12));
        cur_dir_label.setText("ROOT");
        temp_panel.add(cur_dir_label,BorderLayout.PAGE_START);
        temp_panel.add(temp_panel_2,BorderLayout.CENTER);
        temp_panel_2.add(scrollpanel,BorderLayout.PAGE_START);
        
        scroll.getVerticalScrollBar().setUI(new BasicScrollBarUI()
        {   
            @Override
            protected JButton createDecreaseButton(int orientation) {
                return createZeroButton();
            }

            @Override    
            protected JButton createIncreaseButton(int orientation) {
                return createZeroButton();
            }

            private JButton createZeroButton() {
                JButton jbutton = new JButton();
                jbutton.setPreferredSize(new Dimension(0, 0));
                jbutton.setMinimumSize(new Dimension(0, 0));
                jbutton.setMaximumSize(new Dimension(0, 0));
                return jbutton;
            }
        });
    }
    
    abstract void enterDir(File dir);
    abstract void goBack();
    abstract void ListFiles($Directory file);
    abstract void ListDirs($Directory file);
    public abstract void onClick$Dir($Directory dir);
}
