/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package myftp.UI;

import myftp.UI.Utility.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.*;
import myftp.UI.ClientSpecialUI.*;
import myftp.UI.CommonUIparts.*;
import myftp.lib.Client.FTP_Client;

/**
 *
 * @author RAUF00N
 */
public class ClientFrame {
    public FTP_Client this_client;
    
    JFrame frame;
    Container cp;
    
    TitleBar titleBar;
    OptionsBar optionsBar;
    BodyArea body;
    
    public DataTransferPanel dataTransferPanel;
    public MessageLog messageLog;
    public ClientHddBrowser clientHddBrowser;
    public ServerStorageBrowser serverStorageBrowser;
    public ClientHddFiles clientHddFiles;
    public ServerStorageFiles serverStorageFiles;
    
    public $Button load_btn;
    public $Button disconnect_btn;
    public $Button refresh_btn;
    public $Button add_bookmark_btn;
    public $Button bookmarks_btn;
    public $Button transfer_settings_btn;
    public $Button connection_settings_btn;
    public $Button other_settings_btn;
    
    int BTN_SIZE=65;
    int BTN_FNT_SIZE=12;
    public int BTN_IMG_SIZE=25;
    
    void erasecode()
    {
        
    }
    
    public void show()
    {
        init();
        addComponents();
        addButtons();
        setComponentsSize();
        decorate();
        erasecode();
        frame.setVisible(true);
    }
    void init()
    {
        initFrame();
        titleBar = new TitleBar(frame);
        optionsBar = new OptionsBar();
        body = new BodyArea();
        
        String[] details={"SL","Server","Type","Speed","Progress"};
        dataTransferPanel = new DataTransferPanel(details,12);
        messageLog = new MessageLog();
        clientHddFiles = new ClientHddFiles(this);
        clientHddBrowser = new ClientHddBrowser(clientHddFiles,this);
        serverStorageFiles = new ServerStorageFiles(this);
        serverStorageBrowser = new ServerStorageBrowser(serverStorageFiles,this);
        
    }
    void initFrame()
    {
        frame=new JFrame();
        cp = frame.getContentPane();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        cp.setLayout(new BoxLayout(cp, BoxLayout.Y_AXIS));
        //cp.setLayout(new GridBagLayout());
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setSize(dim.width*95/100, dim.height*95/100);
        cp.setBackground(Color.white);        
        frame.getRootPane().setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.decode("#c1bbb3")));
        frame.setUndecorated(true);
        setFrameMovable();
        
    }
    public String toString()
    {
        return "ClientFrame";
    }
    void addComponents()
    {
        cp.add(titleBar);
        cp.add(optionsBar);
        cp.add(body);
        
        body.setLayout(new GridLayout(3,2));
        
        body.add(messageLog);
        body.add(dataTransferPanel);
        body.add(clientHddBrowser);
        body.add(serverStorageBrowser);
        body.add(clientHddFiles);
        body.add(serverStorageFiles);
    }
    void addButtons()
    {
        this.load_btn= new $Button($Button.BUTTON_ACTIVE,Utility.strings("Load Server","Reload Server"));
        optionsBar.add(load_btn);
        load_btn.setFontSize(BTN_FNT_SIZE);
        load_btn.setButtonSize(BTN_SIZE);
        load_btn.init();
        load_btn.setIconSize(BTN_IMG_SIZE);
        load_btn.setHorizontalAlignment(SwingConstants.CENTER);
        load_btn.setHorizontalTextPosition(SwingConstants.RIGHT);
        load_btn.setVerticalTextPosition(SwingConstants.CENTER);
        load_btn.addMouseListener(new $ClickListenerClient(this,load_btn));
        
        this.disconnect_btn= new $Button($Button.BUTTON_DISABLED,Utility.strings("Disconnect",""));
        optionsBar.add(disconnect_btn);
        disconnect_btn.setFontSize(BTN_FNT_SIZE);
        disconnect_btn.setButtonSize(BTN_SIZE);
        disconnect_btn.init();
        disconnect_btn.setIconSize(BTN_IMG_SIZE);
        disconnect_btn.setHorizontalAlignment(SwingConstants.CENTER);
        disconnect_btn.setHorizontalTextPosition(SwingConstants.RIGHT);
        disconnect_btn.setVerticalTextPosition(SwingConstants.CENTER);
        disconnect_btn.addMouseListener(new $ClickListenerClient(this,disconnect_btn));
        
        this.refresh_btn= new $Button($Button.BUTTON_ACTIVE,Utility.strings("Refresh",""));
        optionsBar.add(refresh_btn);
        refresh_btn.setFontSize(BTN_FNT_SIZE);
        refresh_btn.setButtonSize(BTN_SIZE);
        refresh_btn.init();
        refresh_btn.setIconSize(BTN_IMG_SIZE);
        refresh_btn.setHorizontalAlignment(SwingConstants.CENTER);
        refresh_btn.setHorizontalTextPosition(SwingConstants.RIGHT);
        refresh_btn.setVerticalTextPosition(SwingConstants.CENTER);
        refresh_btn.addMouseListener(new $ClickListenerClient(this,refresh_btn));
        
        this.add_bookmark_btn= new $Button($Button.BUTTON_DISABLED,Utility.strings("Add Bookmark",""));
        optionsBar.add(add_bookmark_btn);
        add_bookmark_btn.setFontSize(BTN_FNT_SIZE);
        add_bookmark_btn.setButtonSize(BTN_SIZE);
        add_bookmark_btn.init();
        add_bookmark_btn.setIconSize(BTN_IMG_SIZE);
        add_bookmark_btn.setHorizontalAlignment(SwingConstants.CENTER);
        add_bookmark_btn.setHorizontalTextPosition(SwingConstants.RIGHT);
        add_bookmark_btn.setVerticalTextPosition(SwingConstants.CENTER);
        add_bookmark_btn.addMouseListener(new $ClickListenerClient(this,add_bookmark_btn));
        
        this.bookmarks_btn= new $Button($Button.BUTTON_ACTIVE,Utility.strings("Bookmarks",""));
        optionsBar.add(bookmarks_btn);
        bookmarks_btn.setFontSize(BTN_FNT_SIZE);
        bookmarks_btn.setButtonSize(BTN_SIZE);
        bookmarks_btn.init();
        bookmarks_btn.setIconSize(BTN_IMG_SIZE);
        bookmarks_btn.setHorizontalAlignment(SwingConstants.CENTER);
        bookmarks_btn.setHorizontalTextPosition(SwingConstants.RIGHT);
        bookmarks_btn.setVerticalTextPosition(SwingConstants.CENTER);
        bookmarks_btn.addMouseListener(new $ClickListenerClient(this,bookmarks_btn));
        
        this.connection_settings_btn= new $Button($Button.BUTTON_ACTIVE,Utility.strings("Connection Settings",""));
        optionsBar.add(connection_settings_btn);
        connection_settings_btn.setFontSize(BTN_FNT_SIZE);
        connection_settings_btn.setButtonSize(BTN_SIZE);
        connection_settings_btn.init();
        connection_settings_btn.setIconSize(BTN_IMG_SIZE);
        connection_settings_btn.setHorizontalAlignment(SwingConstants.CENTER);
        connection_settings_btn.setHorizontalTextPosition(SwingConstants.RIGHT);
        connection_settings_btn.setVerticalTextPosition(SwingConstants.CENTER);
        connection_settings_btn.addMouseListener(new $ClickListenerClient(this,connection_settings_btn));
        
        this.transfer_settings_btn= new $Button($Button.BUTTON_ACTIVE,Utility.strings("Transfer Settings",""));
        optionsBar.add(transfer_settings_btn);
        transfer_settings_btn.setFontSize(BTN_FNT_SIZE);
        transfer_settings_btn.setButtonSize(BTN_SIZE);
        transfer_settings_btn.init();
        transfer_settings_btn.setIconSize(BTN_IMG_SIZE);
        transfer_settings_btn.setHorizontalAlignment(SwingConstants.CENTER);
        transfer_settings_btn.setHorizontalTextPosition(SwingConstants.RIGHT);
        transfer_settings_btn.setVerticalTextPosition(SwingConstants.CENTER);
        transfer_settings_btn.addMouseListener(new $ClickListenerClient(this,transfer_settings_btn));
        
        this.other_settings_btn= new $Button($Button.BUTTON_ACTIVE,Utility.strings("Other Settings",""));
        optionsBar.add(other_settings_btn);
        other_settings_btn.setFontSize(BTN_FNT_SIZE);
        other_settings_btn.setButtonSize(BTN_SIZE);
        other_settings_btn.init();
        other_settings_btn.setIconSize(BTN_IMG_SIZE);
        other_settings_btn.setHorizontalAlignment(SwingConstants.CENTER);
        other_settings_btn.setHorizontalTextPosition(SwingConstants.RIGHT);
        other_settings_btn.setVerticalTextPosition(SwingConstants.CENTER);
        other_settings_btn.addMouseListener(new $ClickListenerClient(this,other_settings_btn));
    }
    void setComponentsSize()
    {
        int h= frame.getSize().height,w=frame.getSize().width;
        titleBar.setMaximumSize(new Dimension(w,25));
        titleBar.setPreferredSize(new Dimension(w,25));

        optionsBar.setMaximumSize(new Dimension(w,40));
        optionsBar.setPreferredSize(new Dimension(w,40));
        
        

        body.setMaximumSize(new Dimension(w,h-65));
        body.setPreferredSize(new Dimension(w,h-65));
    }
    void decorate()
    {
        titleBar.setButtonSize(17);
        titleBar.setTitleFontSize(10);
        titleBar.init();
        body.setBorder(BorderFactory.createMatteBorder(5, 5, 5, 5, Color.WHITE));
        
        messageLog.setTextPaneFontSize(13);
        messageLog.setBorder(BorderFactory.createMatteBorder(0, 0, 5, 0, Color.WHITE));
        messageLog.setLabelFontSize(15);
        messageLog.setLabelSize(20);
        messageLog.setBorder(BorderFactory.createEmptyBorder(3,0,3,0)); 
        messageLog.label.setBorder(BorderFactory.createEmptyBorder(0, 3, 3, 0));
        messageLog.jsp.setBorder(BorderFactory.createEmptyBorder(0, 3, 3, 3));
        
        dataTransferPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 5, 0, Color.WHITE));
        dataTransferPanel.setLabelFontSize(15);
        dataTransferPanel.setLabelSize(20);
        dataTransferPanel.setBorder(BorderFactory.createEmptyBorder(3,0,3,0)); 
        dataTransferPanel.label.setBorder(BorderFactory.createEmptyBorder(0, 3, 3, 0));
        
        
        // remove
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

