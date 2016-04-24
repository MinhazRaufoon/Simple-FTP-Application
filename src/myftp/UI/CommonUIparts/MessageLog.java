/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package myftp.UI.CommonUIparts;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import javax.swing.*;
import javax.swing.plaf.basic.BasicScrollBarUI;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
/**
 *
 * @author RAUF00N
 */
public class MessageLog extends JPanel
{
    public static int ACTION_SERVER_START=0;
    public static int ACTION_SERVER_STOP=1;
    public static int ACTION_CLIENT_CONNECTED=2;
    public static int ACTION_CLIENT_DISCONNECTED=3;
    public static int ACTION_DOWNLOAD=4;
    public static int ACTION_UPLOAD=5;
    
    public JLabel label;
    public JScrollPane jsp ;
    JTextPane textPane;
    int msg_font_size;
    public MessageLog()
    {
        LayoutManager layout = new BorderLayout();
        this.setLayout(layout);
        this.setBackground(Color.white);
        
        label = new JLabel();
        textPane = new JTextPane();
        jsp = new JScrollPane(textPane);
        add(label,BorderLayout.PAGE_START);
        add(jsp,BorderLayout.CENTER);
        
        
        label.setText("Message Log");
        label.setForeground(Color.decode("#80af71"));
        label.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        textPane.setEditable(false);
        textPane.setBackground(Color.decode("#ffffff"));
        jsp.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        jsp.setBackground(Color.white);
//        jsp.setBackground(Color.decode(TOOL_TIP_TEXT_KEY));
        
        // custom scrollbar
        jsp.getVerticalScrollBar().setUI(new BasicScrollBarUI()
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
    public void setLabelSize(int size)
    {
        label.setPreferredSize(new Dimension(size,size));
    }
    public void setLabelFontSize(int size)
    {
        label.setFont(new Font("Calibri", Font.PLAIN, size));        
    }
    public void setTextPaneFontSize(int size)
    {
        this.msg_font_size=size;
    }
    public void writeMsgLog(String msg,int action)
    {
        Color color;
        if(action==MessageLog.ACTION_SERVER_START) color = Color.decode("#004000");
        else if(action==MessageLog.ACTION_SERVER_STOP) color = Color.decode("#4c0000");
        else if(action==MessageLog.ACTION_CLIENT_CONNECTED) color = Color.decode("#283300");
        else if(action==MessageLog.ACTION_CLIENT_DISCONNECTED) color = Color.decode("#4c1e1e");
        else if(action==MessageLog.ACTION_DOWNLOAD) color = Color.decode("#0d152d");
        else if(action==MessageLog.ACTION_UPLOAD) color = Color.decode("#30191b");
        else color = Color.decode("#404040");
        
        
        String write = "";
        
        StyledDocument doc = textPane.getStyledDocument();
        Style style = textPane.addStyle("I'm a Style", null);
        StyleConstants.setForeground(style, color);
        StyleConstants.setFontSize(style, this.msg_font_size);
        StyleConstants.setFontFamily(style, "Century Gothic");
        
        synchronized(this){
            String timeStamp = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(Calendar.getInstance().getTime());
            write+="  ["+timeStamp+"]  "+msg+"\n";
            try { doc.insertString(doc.getLength(), write,style); }
            catch (Exception e){}
            textPane.setCaretPosition(textPane.getDocument().getLength());
        }
    }
}
