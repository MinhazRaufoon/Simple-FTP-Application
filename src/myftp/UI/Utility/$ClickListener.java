/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package myftp.UI.Utility;

import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import myftp.UI.CommonUIparts.$Button;

/**
 *
 * @author RAUF00N
 */
public class $ClickListener implements MouseListener{

    $Button component;
    
    public $ClickListener($Button component)
    {
        this.component=component;
    }
    @Override
    public void mouseClicked(MouseEvent e) {
        if(component.getCurrentState()==$Button.BUTTON_DISABLED) return;
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if(component.getCurrentState()==$Button.BUTTON_DISABLED) return;
        component.setBackground(Color.decode("#f6d87e"));
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if(component.getCurrentState()==$Button.BUTTON_DISABLED) return;
        component.setBackground(Color.decode("#fcf2d4"));
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        if(component.getCurrentState()==$Button.BUTTON_DISABLED) return;
        component.setBackground(Color.decode("#fcf2d4"));
    }

    @Override
    public void mouseExited(MouseEvent e) {
        component.setBackground(Color.white);
    }

}
