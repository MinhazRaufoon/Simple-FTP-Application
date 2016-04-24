/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package myftp.UI.Utility;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.TextArea;
import java.awt.TextField;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import myftp.UI.ClientFrame;
import myftp.UI.CommonUIparts.$Button;
import myftp.UI.ServerFrame;
import myftp.lib.ClientFacade;
import myftp.lib.Server.FTP_Server;
import myftp.UI.ClientSpecialUI.$Directory;
/**
 *
 * @author RAUF00N
 */
public class $ClickListenerClient implements MouseListener{

    ClientFrame clientframe;
    $Button component;
    
    public $ClickListenerClient(ClientFrame clientframe,$Button component)
    {
        this.component=component;
        this.clientframe=clientframe;
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
    void onPressLoadButton()
    {
        JPanel panel=new JPanel();
        panel.setPreferredSize(new Dimension(400,200));
        
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.add(new JLabel("Server Address",JLabel.LEFT));
        
        TextField tf_server_address = new TextField();
        tf_server_address.setText("localhost");
        panel.add(tf_server_address);
        
        
        panel.add(new JLabel("Username",JLabel.LEFT));
        
        TextField tf_username = new TextField();
        tf_username.setText("anonymous");
        panel.add(tf_username);
        
        panel.add(new JLabel("Password",JLabel.LEFT));
        
        TextField tf_server_password = new TextField();
        tf_server_password.setText("anonymous");
        panel.add(tf_server_password);
        
        
        panel.add(new JLabel("Port",JLabel.LEFT));
        
        TextField tf_server_port = new TextField();
        panel.add(tf_server_port);
        tf_server_port.setText(FTP_Server.DEFAULT_CONNECTION_PORT+"");
        tf_server_port.setEditable(false);
        
        ImageIcon icon = new ImageIcon(getClass().getResource("LoadServer_active.png"));
        Image img = icon.getImage() ;  
        Image newimg = img.getScaledInstance( 50,50,  java.awt.Image.SCALE_SMOOTH ) ;  
        icon = new ImageIcon( newimg );
        
        int result = JOptionPane.showConfirmDialog(null, panel, "Connection Settings",
                        JOptionPane.OK_CANCEL_OPTION,
                        JOptionPane.QUESTION_MESSAGE,
                        icon);
        
        if(result == JOptionPane.YES_OPTION)
        {
            int res = this.clientframe.this_client.login(tf_server_address.getText(),tf_username.getText(), 
                    tf_server_password.getText(), Integer.parseInt(tf_server_port.getText()));
            if(res!=1) return;
            clientframe.load_btn.toggle();
            clientframe.load_btn.setIconSize(clientframe.BTN_IMG_SIZE);
            clientframe.disconnect_btn.setEnabled();
            clientframe.disconnect_btn.setIconSize(clientframe.BTN_IMG_SIZE);
            clientframe.add_bookmark_btn.setEnabled();
            clientframe.add_bookmark_btn.setIconSize(clientframe.BTN_IMG_SIZE);
        }
        else
        {
            return;
        }
        //.......
        
    }
    void onPressReloadButton()
    {
        //String S = this.clientframe.this_client.serverListener.cur_relative_dir;
        try {
            DataOutputStream out = new DataOutputStream(this.clientframe.this_client.myServerSocket.getOutputStream());
            out.writeUTF("REF");
        } catch (IOException ex) {
            Logger.getLogger($ClickListenerClient.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    void onPressDisconnectButton()
    {
        clientframe.load_btn.toggle();
        clientframe.load_btn.setIconSize(clientframe.BTN_IMG_SIZE);
        clientframe.disconnect_btn.setDisabled();
        clientframe.disconnect_btn.setIconSize(clientframe.BTN_IMG_SIZE);
        clientframe.add_bookmark_btn.setDisabled();
        clientframe.add_bookmark_btn.setIconSize(clientframe.BTN_IMG_SIZE);
        this.clientframe.this_client.logout();
        
        
    }
    void onPressRefreshButton()
    {
         ClientFacade.getInstance().clientframe.clientHddBrowser.ListDirs(
                new $Directory(ClientFacade.getInstance().clientframe.clientHddBrowser.current_dir
                        ,ClientFacade.getInstance().clientframe.clientHddBrowser
                )
         );
         ClientFacade.getInstance().clientframe.clientHddBrowser.ListFiles(
                 new $Directory(ClientFacade.getInstance().clientframe.clientHddBrowser.current_dir
                 ,ClientFacade.getInstance().clientframe.clientHddBrowser)
                 
         );
    }
    void onPressAddBookmarkButton()
    {
                
    }
    void onPressBookmarksButton()
    {
                
    }
    void onPressConnectionSettingsButton()
    {
                
    }
    void onPressTransferSettingsButton()
    {
                
    }
    void onPressOtherSettingsButton()
    {
        JPanel panel=new JPanel();
        panel.setPreferredSize(new Dimension(400,400));
        
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.add(new JLabel("Download location",JLabel.LEFT));
        
        TextArea ta_wm = new TextArea();
        panel.add(ta_wm);
        ta_wm.setText(clientframe.this_client.settings.save_dir);
        
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
            clientframe.this_client.settings.save_dir = ta_wm.getText()+"";
            
            clientframe.this_client.settings.saveCurrent();
        }
    }

    public void doWork()
    {
        switch (component.getCurrentTitle()) {
            case "Load Server":
                new Thread(){
                    public void run()
                    {
                        onPressLoadButton();
                    }
                }.start();
                
                break;
            case "Reload Server":
                
                new Thread(){
                    public void run()
                    {
                        onPressReloadButton();
                    }
                }.start();
                
                break;
            case "Disconnect":
                new Thread(){
                    public void run()
                    {
                        onPressDisconnectButton();
                    }
                }.start();
                
                break;    
            case "Refresh":
                new Thread(){
                    public void run()
                    {
                        onPressRefreshButton();
                    }
                }.start();
                
                break;    
            case "Add Bookmark":
                new Thread(){
                    public void run()
                    {
                        onPressAddBookmarkButton();
                    }
                }.start();
                
                break;    
            case "Bookmarks":
                
                new Thread(){
                    public void run()
                    {
                        onPressBookmarksButton();
                    }
                }.start();
                break;
            case "Connection Settings":
                onPressConnectionSettingsButton();
                break;    
            case "Transfer Settings":
                onPressTransferSettingsButton();
                break;    
            case "Other Settings":
                onPressOtherSettingsButton();
                break;    
            default:
                break;
        }
    }

}

