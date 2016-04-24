/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package myftp.UI.CommonUIparts;

import java.awt.Color;
import java.awt.Font;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

/**
 *
 * @author RAUF00N
 */
class TrLabel extends JLabel
{
    public TrLabel(String S,int font_size)
    {
        super(S,SwingConstants.CENTER);
        this.setForeground(Color.decode("#655643"));
        setFont(new Font("Calibri", Font.PLAIN, font_size));
        setBorder(BorderFactory.createEmptyBorder((int)(3.0*((double)font_size/15.0)), 0, (int)(3.0*((double)font_size/15.0)), 0));
    }
}