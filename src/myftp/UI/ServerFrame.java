/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package myftp.UI;

import myftp.UI.Utility.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import myftp.UI.CommonUIparts.*;
import myftp.lib.Server.FTP_Server;

/**
 *
 * @author RAUF00N
 */
public class ServerFrame 
{
    public FTP_Server server;
    
    JFrame frame;
    public Container cp;
    public TitleBar titleBar;
    public OptionsBar optionsBar;
    public BodyArea body;
    
    public MessageLog messageLog;
    public DataTransferPanel dataTransferPanel;
    
    public $Button connect_button;
    public $Button disconnect_button;
    public $Button lock_button;
    public $Button online_button;
    public $Button client_settings_button;
    public $Button admin_settings_button;
    public $Button connection_settings_button;
    public $Button other_settings_button;
    
    public void show()
    {
        init();
        addComponents();
        setComponentsSize();
        decorate();
        
        frame.setVisible(true);
    }
    void init()
    {
        initFrame();
        titleBar = new TitleBar(frame);
        optionsBar = new OptionsBar();
        body = new BodyArea();
        
        String[] details={"SL","Client IP","Type","Speed","Progress"};
        dataTransferPanel = new DataTransferPanel(details,15);
        
        messageLog = new MessageLog();
        
        titleBar.logo.setLayout(new FlowLayout(FlowLayout.LEFT));
        
    }
    void initFrame()
    {
        frame=new JFrame();
        frame.setBackground(Color.white);
        cp = frame.getContentPane();
        cp.setBackground(Color.white);
        //frame.setExtendedState(JFrame.MAXIMIZED_BOTH); 
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setSize(dim.width*90/100, dim.height*90/100);
        
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        cp.setLayout(new BoxLayout(cp, BoxLayout.Y_AXIS));       
        frame.getRootPane().setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.decode("#c1bbb3")));
        frame.setUndecorated(true);
        setFrameMovable();
    }
    void addComponents()
    {
        cp.add(titleBar);
        cp.add(optionsBar);
        cp.add(body);
        
        body.add(messageLog);
        body.add(dataTransferPanel);
        
        addButtons();
    }
    void setComponentsSize()
    {
        Utility.setComponentSize(titleBar, 1, 0.08);
        Utility.setComponentSize(optionsBar, 1, 0.05);
        Utility.setComponentSize(body, 1, 0.85);
        
        Utility.setComponentSize(messageLog, 0.5, 1);
        Utility.setComponentSize(dataTransferPanel, 0.5, 1);
        
        messageLog.setLabelSize(50);
        messageLog.setLabelFontSize(30);
        messageLog.setTextPaneFontSize(15);
        messageLog.setBorder(BorderFactory.createEmptyBorder(10,20,0,10)); 
        
        
        dataTransferPanel.setLabelSize(50);
        dataTransferPanel.setLabelFontSize(30);
        
        titleBar.setButtonSize(30);
        titleBar.init();
        titleBar.setTitleFontSize(35);
    }
    void decorate()
    {
        
    }
    void addButtons()
    {
        this.connect_button = new $Button($Button.BUTTON_ACTIVE,Utility.strings("Connect Server",""));
        optionsBar.add(connect_button);
        connect_button.init();
        connect_button.addMouseListener(new $ClickListenerServer(this,connect_button));
        connect_button.setFontSize(16);
        connect_button.setIconSize(70);
        connect_button.setButtonSize(100);
        
        this.disconnect_button= new $Button($Button.BUTTON_DISABLED,Utility.strings("Log out",""));
        optionsBar.add(disconnect_button);
        disconnect_button.init();
        disconnect_button.addMouseListener(new $ClickListenerServer(this,disconnect_button));
        disconnect_button.setFontSize(16);
        disconnect_button.setIconSize(70);
        disconnect_button.setButtonSize(100);
        
        this.lock_button = new $Button($Button.BUTTON_DISABLED,Utility.strings("Lock Server","Unlock Server"));
        optionsBar.add(lock_button);
        lock_button.init();
        lock_button.addMouseListener(new $ClickListenerServer(this,lock_button));
        lock_button.setFontSize(16);
        lock_button.setIconSize(70);
        lock_button.setButtonSize(100);
        
        this.online_button = new $Button($Button.BUTTON_DISABLED,Utility.strings("Pause Server","Resume Server"));
        optionsBar.add(online_button);
        online_button.init();
        online_button.addMouseListener(new $ClickListenerServer(this,online_button));
        online_button.setFontSize(16);
        online_button.setIconSize(70);
        online_button.setButtonSize(100);
        
        this.client_settings_button = new $Button($Button.BUTTON_ACTIVE,Utility.strings("Client Settings",""));
        optionsBar.add(client_settings_button);
        client_settings_button.init();
        client_settings_button.addMouseListener(new $ClickListenerServer(this,client_settings_button));
        client_settings_button.setFontSize(16);
        client_settings_button.setIconSize(70);
        client_settings_button.setButtonSize(100);
        
        this.connection_settings_button = new $Button($Button.BUTTON_ACTIVE,Utility.strings("Connection Settings",""));
        optionsBar.add(connection_settings_button);
        connection_settings_button.init();
        connection_settings_button.addMouseListener(new $ClickListenerServer(this,connection_settings_button));
        connection_settings_button.setFontSize(16);
        connection_settings_button.setIconSize(70);
        connection_settings_button.setButtonSize(100);
        
        this.admin_settings_button = new $Button($Button.BUTTON_ACTIVE,Utility.strings("Registered Users",""));
        optionsBar.add(admin_settings_button);
        admin_settings_button.init();
        admin_settings_button.addMouseListener(new $ClickListenerServer(this,admin_settings_button));
        admin_settings_button.setFontSize(16);
        admin_settings_button.setIconSize(70);
        admin_settings_button.setButtonSize(100);
        
        this.other_settings_button = new $Button($Button.BUTTON_ACTIVE,Utility.strings("Other Settings",""));
        optionsBar.add(other_settings_button);
        other_settings_button.init();
        other_settings_button.addMouseListener(new $ClickListenerServer(this,other_settings_button));
        other_settings_button.setFontSize(16);
        other_settings_button.setIconSize(70);
        other_settings_button.setButtonSize(100);
        
    }
    void setFrameMovable()
    {
        MouseAdapter ma = new MouseAdapter() {
            int lastX, lastY;
            @Override
            public void mousePressed(MouseEvent e) {
                lastX = e.getXOnScreen();
                lastY = e.getYOnScreen();
            }
            @Override
            public void mouseDragged(MouseEvent e) {
                int x = e.getXOnScreen();
                int y = e.getYOnScreen();
                // Move frame by the mouse delta
                frame.setLocation(frame.getLocationOnScreen().x + x - lastX,
                        frame.getLocationOnScreen().y + y - lastY);
                lastX = x;
                lastY = y;
            }
        };
        frame.addMouseListener(ma);
        frame.addMouseMotionListener(ma);
    }
}
