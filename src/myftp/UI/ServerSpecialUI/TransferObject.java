/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package myftp.UI.ServerSpecialUI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.ArrayList;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.SwingConstants;
import javax.swing.plaf.basic.BasicProgressBarUI;

/**
 *
 * @author RAUF00N
 */
public class TransferObject extends JPanel{
    public int height=60;
    String type;
    
    protected JPanel panel=new JPanel();
    protected JPanel upPanel=new JPanel();
    protected JProgressBar progbar=new JProgressBar();
        
    public JLabel SL,ADDR,TYPE,RATE,PRG;
    protected int font_size;
    private void addComponents()
    {
        add(panel,BorderLayout.CENTER);
        panel.add(upPanel);
        panel.add(progbar);
        
        font_size=15;
        
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
        
        setBorder(BorderFactory.createMatteBorder(5, 0, 5, 0, Color.decode("#ffffff")));
        progbar.setFont(new Font("Calibri",Font.PLAIN,font_size-2));
        progbar.setUI(new BasicProgressBarUI() {
            protected Color getSelectionBackground() { 
                if(type.equals("DOWNLOAD"))return Color.DARK_GRAY; 
                else return Color.decode("#7d372a");
            }
            protected Color getSelectionForeground() { 
                if(type.equals("DOWNLOAD"))return Color.white; 
                else return Color.decode("#8b3e2f");
            }
          });
        
    }
    public TransferObject(String values[],String path,String type)
    {
        this.type=type;
        
        SL=new JLabel(values[0],SwingConstants.CENTER);
        ADDR=new JLabel(values[1],SwingConstants.CENTER);
        TYPE=new JLabel(values[2],SwingConstants.CENTER);
        RATE=new JLabel(values[3],SwingConstants.CENTER);
        PRG=new JLabel(values[4],SwingConstants.CENTER);
        
        this.setLayout(new BorderLayout());
        
        
        panel.setBackground(Color.white);
        panel.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.decode("#899d78")));
        
        panel.setLayout(new GridLayout(2,1));
        upPanel.setLayout(new GridLayout(1,5));
        upPanel.setBackground(Color.white);
        
        
                
        progbar.setBorderPainted(false);
        progbar.setValue(0);
        if(type.equals("DOWNLOAD")){
            progbar.setBackground(Color.decode("#b2dac7"));
            progbar.setForeground(Color.decode("#008547"));
        }
        else{
            progbar.setBackground(Color.decode("#fbe7bf"));
            progbar.setForeground(Color.decode("#f7c410"));
        }
        progbar.setStringPainted(true);
        progbar.setString(path);
        addComponents();
    }
    public void setProgress(int p)
    {
        this.progbar.setValue(p);
    }
    public void setValues(String values[])
    {
        SL=new JLabel(values[0],SwingConstants.CENTER);
        ADDR=new JLabel(values[1],SwingConstants.CENTER);
        TYPE=new JLabel(values[2],SwingConstants.CENTER);
        RATE=new JLabel(values[3],SwingConstants.CENTER);
        PRG=new JLabel(values[4],SwingConstants.CENTER);
    }
}
