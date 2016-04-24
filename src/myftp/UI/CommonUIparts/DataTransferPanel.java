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
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridLayout;
import java.awt.LayoutManager;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.plaf.basic.BasicScrollBarUI;
import myftp.UI.ServerSpecialUI.TransferObject;
/**
 *
 * @author RAUF00N
 */
public class DataTransferPanel extends JPanel{
    public JLabel label;
    public TrPanel panel;
    public ArrayList<TransferObject> transferObjects = new ArrayList<TransferObject>();
    
    public DataTransferPanel(String[] details,int font_size)
    {
        LayoutManager layout = new BorderLayout();
        this.setLayout(layout);
        this.setBackground(Color.white);
        setBorder(BorderFactory.createEmptyBorder(10,20,0,10)); 
        
        label = new JLabel();
        panel = new TrPanel(details,font_size);
            
        
        add(label,BorderLayout.PAGE_START);
        add(panel,BorderLayout.CENTER);
               
        label.setText("Transfer List");
        label.setForeground(Color.decode("#80af71"));
        label.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        
        panel.setBackground(Color.red);
        
    }
    public void setLabelSize(int size)
    {
        label.setPreferredSize(new Dimension(size,size));
    }
    public void setLabelFontSize(int size)
    {
        label.setFont(new Font("Calibri", Font.PLAIN, size));        
    }
    @Override
    public String toString()
    {
        return "Data Transfer Panel";
    }
    public void addTransferObject(TransferObject transferObject)
    {
        transferObjects.add(transferObject);
        GridBagConstraints constraint = new GridBagConstraints();
        constraint.anchor = GridBagConstraints.CENTER;
        constraint.fill = GridBagConstraints.HORIZONTAL;
        constraint.gridx = 0;
        constraint.gridy = GridBagConstraints.RELATIVE;
        constraint.weightx = 1.0f;
        constraint.weighty=1.0f;
        transferObject.setPreferredSize(new Dimension(panel.transfer_list.getSize().width - 30,transferObject.height));
        
        synchronized(this){
            this.panel.transfers.add(transferObject,constraint);
        }
        JScrollBar vertical = this.panel.scrollpane.getVerticalScrollBar();
        vertical.setValue( vertical.getMaximum() );
        this.panel.transfers.revalidate();
        this.panel.transfers.repaint();   
    }
}


