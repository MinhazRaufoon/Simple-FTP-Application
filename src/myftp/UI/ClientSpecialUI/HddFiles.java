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
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.plaf.basic.BasicScrollBarUI;
import myftp.UI.ClientFrame;

/**
 *
 * @author RAUF00N
 */
public abstract class HddFiles extends JPanel{
    protected JPanel label_panel = new JPanel();
    public JPanel scrollpanel = new JPanel();
    protected JPanel temp_panel = new JPanel();
    protected JScrollPane scroll = new JScrollPane(temp_panel);
    
    protected File[] currentFiles;
//    protected ArrayList<$Directory> listed$Dirs = new ArrayList<$Directory>();
    ClientFrame clientframe;
    
    public HddFiles(ClientFrame clientframe)
    {
        this.clientframe = clientframe;
        setBorder(BorderFactory.createEmptyBorder(1, 1, 1, 1));
        LayoutManager layout = new BorderLayout();
        this.setLayout(layout);
        this.setBackground(Color.white);
        
    }
    
    void init_label_panel(String title)
    {
        label_panel.setLayout(new GridLayout(1,1));
        
        JLabel label = new JLabel(title);
        label.setFont(new Font("Calibri", Font.PLAIN, 15));
        label.setForeground(Color.decode("#80af71"));
        label.setPreferredSize(new Dimension(200,20));
        label.setBorder(BorderFactory.createEmptyBorder(0, 3, 3, 0));
        
        label_panel.setBackground(Color.white);
        label_panel.add(label);
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
        temp_panel.setBackground(Color.white);
        temp_panel.setLayout(new BorderLayout());
        temp_panel.add(scrollpanel,BorderLayout.PAGE_START);
        scrollpanel.setBackground(Color.white);
        scrollpanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        
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
    abstract void listFiles($Directory dir);
}
