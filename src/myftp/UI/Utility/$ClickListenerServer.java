/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package myftp.UI.Utility;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.TextArea;
import java.awt.TextField;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import myftp.UI.CommonUIparts.$Button;
import myftp.UI.ServerFrame;
import myftp.lib.Server.DB.DataBaseAdapter;
import myftp.lib.Server.FTP_Server;

/**
 *
 * @author RAUF00N
 */
public class $ClickListenerServer implements MouseListener{

    ServerFrame serverframe;
    $Button component;
    
    public $ClickListenerServer(ServerFrame serverframe,$Button component)
    {
        this.component=component;
        this.serverframe=serverframe;
    }
    @Override
    public void mouseClicked(MouseEvent e) {
        if(component.getCurrentState()==$Button.BUTTON_DISABLED) return;
        
        doWork();
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
        if(component.getCurrentState()==$Button.BUTTON_DISABLED) return;
        component.setBackground(Color.white);
    }
    void onPressConnectButton()
    {
        JPanel panel=new JPanel();
        panel.setPreferredSize(new Dimension(400,200));
        
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.add(new JLabel("Server Address",JLabel.LEFT));
        
        TextField tf_server_address = new TextField();
        panel.add(tf_server_address);
        
        panel.add(new JLabel("Password",JLabel.LEFT));
        
        TextField tf_server_password = new TextField();
        panel.add(tf_server_password);
        
        panel.add(new JLabel("Port",JLabel.LEFT));
        
        TextField tf_server_port = new TextField();
        panel.add(tf_server_port);
        tf_server_port.setText(FTP_Server.DEFAULT_CONNECTION_PORT+"");
        tf_server_port.setEditable(false);
        
        ImageIcon icon = new ImageIcon(getClass().getResource("ConnectServer_active.png"));
        Image img = icon.getImage() ;  
        Image newimg = img.getScaledInstance( 50,50,  java.awt.Image.SCALE_SMOOTH ) ;  
        icon = new ImageIcon( newimg );
        
        int result = JOptionPane.showConfirmDialog(null, panel, "Connection Settings",
                        JOptionPane.OK_CANCEL_OPTION,
                        JOptionPane.QUESTION_MESSAGE,
                        icon);
        
        if(result == JOptionPane.YES_OPTION)
        {
            serverframe.server.login(tf_server_address.getText(), tf_server_password.getText(), Integer.parseInt(tf_server_port.getText()));
        }
        else
        {
            return;
        }
        // at first establish a connection
        serverframe.connect_button.setDisabled();
        serverframe.disconnect_button.setEnabled();
        serverframe.lock_button.setEnabled();
        serverframe.online_button.setEnabled();
    }
    void onPressChangeServerButton()
    {
    }
    void onPressDisconnectButton()
    {
        
        // at first do work
        serverframe.disconnect_button.setDisabled();
        serverframe.server.logOut();
        serverframe.connect_button.setEnabled();
        serverframe.lock_button.setDisabled();
        serverframe.online_button.setDisabled();
        
    }
    void onPressPauseButton()
    {
        this.serverframe.server.PauseServer();       
        serverframe.online_button.toggle();
        
    }
    void onPressLockButton()
    {
        this.serverframe.server.LockServer();
        serverframe.lock_button.toggle();                
    }
    void onPressUnlockButton()
    {
        this.serverframe.server.UnlockServer();
        serverframe.lock_button.toggle();
    }
    void onPressResumeButton()
    {
        // at first do work
        this.serverframe.server.ResumeServer();
        serverframe.online_button.toggle();
        
    }
    void onPressClientSettingsButton()
    {
        JPanel panel=new JPanel();
        panel.setPreferredSize(new Dimension(400,400));
        
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.add(new JLabel("Max Clients",JLabel.LEFT));
        TextField tb_max_clients = new TextField();
        panel.add(tb_max_clients);
        panel.add(new JLabel("Banned List",JLabel.LEFT));
        TextArea ta_banned = new TextArea();
        panel.add(ta_banned);
        
        ImageIcon icon = new ImageIcon(getClass().getResource("ClientSettings_active.png"));
        Image img = icon.getImage() ;  
        Image newimg = img.getScaledInstance( 50,50,  java.awt.Image.SCALE_SMOOTH ) ;  
        icon = new ImageIcon( newimg );
        
        tb_max_clients.setText(serverframe.server.server_settings.MAX_CLIENTS+"");
        ta_banned.setEditable(false);
        for(Object o: serverframe.server.server_settings.bannedIps)
        {
            ta_banned.append((String)o);
            ta_banned.append("\n");
        }
        ta_banned.setPreferredSize(new Dimension(400,400));
        ta_banned.setBackground(Color.white);
        int result = JOptionPane.showConfirmDialog(null, panel, "Client Settings",
                        JOptionPane.OK_CANCEL_OPTION,
                        JOptionPane.QUESTION_MESSAGE,
                        icon);
        
        if(result == JOptionPane.YES_OPTION)
        {
            serverframe.server.server_settings.MAX_CLIENTS = Integer.parseInt(tb_max_clients.getText());
            serverframe.server.server_settings.saveCurrent();
        }
    }
    void onPressConnectionSettingsButton()
    {
        JPanel panel=new JPanel();
        panel.setPreferredSize(new Dimension(400,400));
        
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.add(new JLabel("Max Download Threads",JLabel.LEFT));
        
        TextField max_dwn_sp = new TextField();
        panel.add(max_dwn_sp);
        max_dwn_sp.setText(serverframe.server.server_settings.MAX_DOWNLOAD_THREADS+"");
        
        panel.add(new JLabel("Max Download Threads",JLabel.LEFT));
        
        TextField max_up_sp = new TextField();
        panel.add(max_up_sp);
        max_up_sp.setText(serverframe.server.server_settings.MAX_UPLOAD_THREADS+"");
        
        ImageIcon icon = new ImageIcon(getClass().getResource("ConnectionSettings_active.png"));
        Image img = icon.getImage() ;  
        Image newimg = img.getScaledInstance( 50,50,  java.awt.Image.SCALE_SMOOTH ) ;  
        icon = new ImageIcon( newimg );
        
        int result = JOptionPane.showConfirmDialog(null, panel, "Connection Settings",
                        JOptionPane.OK_CANCEL_OPTION,
                        JOptionPane.QUESTION_MESSAGE,
                        icon);
        
        if(result == JOptionPane.YES_OPTION)
        {
            serverframe.server.server_settings.MAX_DOWNLOAD_THREADS = Integer.parseInt(max_dwn_sp.getText());
            serverframe.server.server_settings.MAX_UPLOAD_THREADS = Integer.parseInt(max_up_sp.getText());
            serverframe.server.server_settings.saveCurrent();
        }
    }
    void onPressAdminSettingsButton()
    {
        JPanel panel=new JPanel();
        panel.setPreferredSize(new Dimension(400,400));
        
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.add(new JLabel("Registered Users",JLabel.LEFT));
        
        TextArea ta_wm = new TextArea();
        panel.add(ta_wm);
        ta_wm.setText("");
        ta_wm.setEditable(false);
        
        panel.add(new JLabel("Add new user",JLabel.LEFT));
        
        JPanel panelin = new JPanel();
        panelin.setLayout(new GridLayout(3,3));
        panelin.add(new JLabel("Username"));
        TextField tf_u = new TextField();
        panelin.add(tf_u);
        
        panelin.add(new JLabel("Password"));
        TextField tf_p = new TextField();
        panelin.add(tf_p);
        
        panelin.add(new JLabel("Email"));
        TextField tf_e = new TextField();
        panelin.add(tf_e);
        panel.add(panelin);
        
        
        
        ImageIcon icon = new ImageIcon(getClass().getResource("RegisteredUsers_active.png"));
        Image img = icon.getImage() ;  
        Image newimg = img.getScaledInstance( 50,50,  java.awt.Image.SCALE_SMOOTH ) ;  
        icon = new ImageIcon( newimg );
        
        ArrayList s = DataBaseAdapter.getInstance().getAll();
        
        for(Object str: s)
        {
            if(s==null) break;
            ta_wm.append(str+"\n");
        }
        
        int result = JOptionPane.showConfirmDialog(null, panel, "Other Settings",
                        JOptionPane.OK_CANCEL_OPTION,
                        JOptionPane.QUESTION_MESSAGE,
                        icon);
        
        if(result == JOptionPane.OK_OPTION)
        {
            if(tf_u.getText().length()==0) return;
            DataBaseAdapter.getInstance().addRow(tf_u.getText(), tf_p.getText(), tf_e.getText());
        }
    }
    void onPressOtherSettingsButton()
    {
        JPanel panel=new JPanel();
        panel.setPreferredSize(new Dimension(400,400));
        
        panel.setLayout(new GridLayout(6,1));
        panel.add(new JLabel("Welcome message",JLabel.LEFT));
        
        TextArea ta_wm = new TextArea();
        panel.add(ta_wm);
        ta_wm.setText(serverframe.server.server_settings.welcomeMsg);
        
        panel.add(new JLabel("Server's home directory",JLabel.LEFT));
        
        TextField s_home = new TextField();
        panel.add(s_home);
        s_home.setText(serverframe.server.server_settings.home_directory);
        
        panel.add(new JLabel("Server's home directory for registered users"));
        
        TextField s_home_reg = new TextField();
        panel.add(s_home_reg);
        s_home.setText(serverframe.server.server_settings.home_directory);
        
        ImageIcon icon = new ImageIcon(getClass().getResource("OtherSettings_active.png"));
        Image img = icon.getImage() ;  
        Image newimg = img.getScaledInstance( 50,50,  java.awt.Image.SCALE_SMOOTH ) ;  
        icon = new ImageIcon( newimg );
        
        int result = JOptionPane.showConfirmDialog(null, panel, "Other Settings",
                        JOptionPane.OK_CANCEL_OPTION,
                        JOptionPane.QUESTION_MESSAGE,
                        icon);
        
        if(result == JOptionPane.YES_OPTION)
        {
            serverframe.server.server_settings.home_directory = s_home.getText()+"";
            serverframe.server.server_settings.welcomeMsg = ta_wm.getText();
            serverframe.server.server_settings.saveCurrent();
            serverframe.server.server_settings.saveWelcomeMsg();
        }
    }
    
    public void doWork()
    {
        switch (component.getCurrentTitle()) {
            case "Connect Server":
                onPressConnectButton();
                break;
            case "Log out":
                onPressDisconnectButton();
                break;
            case "Change Server":
                onPressChangeServerButton();
                break;
            case "Lock Server":
                onPressLockButton();
                break;
            case "Unlock Server":
                onPressUnlockButton();
                break;
            case "Pause Server":
                onPressPauseButton();
                break;
            case "Resume Server":
                onPressResumeButton();
                break;
            case "Client Settings":
                onPressClientSettingsButton();
                break;
            case "Connection Settings":
                onPressConnectionSettingsButton();
                break;
            case "Registered Users":
                onPressAdminSettingsButton();
                break;
            case "Other Settings":
                onPressOtherSettingsButton();
                break;
            default:
                break;
        }
    }

}
