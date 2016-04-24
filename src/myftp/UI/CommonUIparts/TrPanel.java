/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package myftp.UI.CommonUIparts;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.LayoutManager;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.plaf.basic.BasicScrollBarUI;

/**
 *
 * @author RAUF00N
 */
class TrPanel extends JPanel
{
    String[] details;
    JPanel label_panel,transfer_list,transfers;
    JScrollPane scrollpane;
    public TrPanel(String[] details,int font_size)
    {
        this.details=details;
        this.setLayout(new BorderLayout());
        label_panel = new JPanel();
        label_panel.setLayout(new GridLayout());
        label_panel.setBackground(Color.white);
        
        transfer_list = new JPanel();
        transfer_list.setLayout(new FlowLayout(FlowLayout.LEFT));
        
        for(int i=0;i<details.length;i++)
        {
            label_panel.add(new TrLabel(details[i],font_size));
        }
        /*
        label_panel.add(new TrLabel("SL"));
        label_panel.add(new TrLabel("Client IP"));
        label_panel.add(new TrLabel("Type"));
        label_panel.add(new TrLabel("Speed"));
        label_panel.add(new TrLabel("Progress"));
        */
        label_panel.setBorder(BorderFactory.createMatteBorder(1, 0, 1, 0, Color.decode("#c1bbb3")));
        
        transfers=new JPanel();
        transfer_list.add(transfers);
        transfer_list.setBackground(Color.white);
        
        transfers.setLayout(new GridBagLayout());
        
        scrollpane = new JScrollPane(transfer_list);
        scrollpane.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        
        add(label_panel,BorderLayout.PAGE_START);
        add(scrollpane,BorderLayout.CENTER);
       
        scrollpane.getVerticalScrollBar().setUI(new BasicScrollBarUI()
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
}