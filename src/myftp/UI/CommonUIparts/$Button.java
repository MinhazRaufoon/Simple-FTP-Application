/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package myftp.UI.CommonUIparts;

import myftp.UI.Utility.$ClickListener;
import com.sun.awt.AWTUtilities;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import myftp.UI.Utility.Utility;
import myftp.UI.*;
/**
 *
 * @author RAUF00N
 */
public class $Button extends JButton
{
    String image_directory ="C:\\Users\\RAUF00N\\Documents\\NetBeansProjects\\myFTP\\src\\myftp\\UI\\res\\";
    static Color active_text_color=Color.decode("#486982");
    static Color disabled_text_color=Color.LIGHT_GRAY;
    
    private String titles[];
    private  int current_function;
    private String current_title;
    private int current_state;
    ImageIcon icon;
    
    public static int BUTTON_FUNC_0 = 0;
    public static int BUTTON_FUNC_1 = 1;
    public static int BUTTON_DISABLED = 100;
    public static int BUTTON_ACTIVE = 200;  
    
    public $Button(int state,String[] titles)
    {   
        this.titles = titles;
        
        current_state=state;
        current_function = BUTTON_FUNC_0;
        current_title = titles[current_function];
    }
    public void setFontSize(int size)
    {
        setFont(new Font("Calibri", Font.PLAIN, size));
    }
    public void setButtonSize(int size)
    {
        this.setPreferredSize(new Dimension(size,size));
        this.setMinimumSize(this.getPreferredSize());
    }
    public void setIconSize(int size)
    {
        Image img = icon.getImage() ;  
        Image newimg = img.getScaledInstance( size,size,  java.awt.Image.SCALE_SMOOTH ) ;  
        icon = new ImageIcon( newimg );
        this.setIcon(icon);
    }
    public void init()
    {
        setFocusPainted(false);
        this.setBackground(Color.white);
        setVerticalTextPosition(SwingConstants.BOTTOM);
        setHorizontalTextPosition(SwingConstants.CENTER);
        updateVisual(current_state,current_title);
        setBorder(BorderFactory.createMatteBorder(1, 0, 1, 0, Color.decode("#c1bbb3")));
        addMouseListener(new $ClickListener(this));
    }

    public void updateVisual(int state,String title)
    {
        String imagesrc = Utility.excludeSpace(current_title);
        // set the state
        if(state==$Button.BUTTON_ACTIVE) {
            this.setForeground(active_text_color);
            imagesrc+="_active.png";
        }
        else if(state==$Button.BUTTON_DISABLED) {
            this.setForeground(disabled_text_color);
            imagesrc+="_disabled.png";
        } 
        // setting the image
        icon = new ImageIcon(getClass().getResource(imagesrc));
        Image img = icon.getImage() ;  
        Image newimg = img.getScaledInstance( 500,500,  java.awt.Image.SCALE_SMOOTH ) ;  
        icon = new ImageIcon( newimg );
        this.setIcon(icon);
        
        // set the text
        this.setText(this.current_title);
    }
    public void toggle()
    {
        if(this.current_state == $Button.BUTTON_DISABLED) return;
        
        if(this.current_function == $Button.BUTTON_FUNC_0 ) this.current_function = $Button.BUTTON_FUNC_1;
        else this.current_function = $Button.BUTTON_FUNC_0;
        
        this.current_title=titles[this.current_function];
        this.setText(current_title);
        
        String imagesrc = Utility.excludeSpace(current_title)+"_active.png";
        icon = new ImageIcon(getClass().getResource(imagesrc));
        Image img = icon.getImage() ;  
        Image newimg = img.getScaledInstance( 70,70,  java.awt.Image.SCALE_SMOOTH ) ;  
        icon = new ImageIcon( newimg );
        this.setIcon(icon);
        
        //revalidate();;
        
    }
    
    public void setEnabled()
    {
        this.current_function = $Button.BUTTON_FUNC_0;
        this.current_title = titles[$Button.BUTTON_FUNC_0];
        
        this.current_state = $Button.BUTTON_ACTIVE;
        this.setForeground(active_text_color);
        this.setText(current_title);
        
        String imagesrc = Utility.excludeSpace(current_title)+"_active.png";
        icon = new ImageIcon(getClass().getResource(imagesrc));
        Image img = icon.getImage() ;  
        Image newimg = img.getScaledInstance( 70,70,  java.awt.Image.SCALE_SMOOTH ) ;  
        icon = new ImageIcon( newimg );
        this.setIcon(icon);
    }
    
    public void setDisabled()
    {
        this.current_function = $Button.BUTTON_FUNC_0;
        this.current_title = titles[$Button.BUTTON_FUNC_0];
        this.setText(current_title);
        
        this.current_state = $Button.BUTTON_DISABLED;
        this.setForeground(disabled_text_color);
        
        String imagesrc = Utility.excludeSpace(current_title)+"_disabled.png";
        icon = new ImageIcon(getClass().getResource(imagesrc));
        Image img = icon.getImage() ;  
        Image newimg = img.getScaledInstance( 70,70,  java.awt.Image.SCALE_SMOOTH ) ;  
        icon = new ImageIcon( newimg );
        this.setIcon(icon);
    }
    public int getCurrentState()
    {
        return this.current_state;
    }
    public String getCurrentTitle()
    {
        return this.current_title;
    }
}

