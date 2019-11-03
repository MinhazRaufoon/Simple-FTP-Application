/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package myftp.lib.Server;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import myftp.lib.Utility;

/**
 *
 * @author RAUF00N
 */
public class Settings {
	String root = System.getProperty("user.home")+ "/";
    String settings_dir = root + "myFTP saved files/Server/";
    public String default_home;
    // other setting
    public String welcomeMsg;
    public String home_directory;
    // client settings
    public int MAX_CLIENTS=80;
    public ArrayList bannedIps = new ArrayList();
    
    // connection settings
    public int MAX_DOWNLOAD_THREADS=100;
    public int MAX_UPLOAD_THREADS=50;
    
    // admin settings
    public int ADMIN_PORT=10000;
    String ADMIN_EMAIL = null;
    String ADMIN_PASSWORD= null;
    
    
    public Settings()
    {
    	// try to create folder
        // *** these will be created even though home is changed, deal with it later
        new File(root + "myFTP saved files/").mkdir();
        new File(root + "myFTP saved files/Server/").mkdir();
        
        new File(root + "myFTP SERVER/").mkdir();
        new File(root + "myFTP SERVER/HOME/").mkdir();
        new File(root + "myFTP SERVER/HOME/HOME/").mkdir();
        
        this.default_home = root + "myFTP SERVER/HOME/HOME";
        home_directory = default_home + "";
        // load all attributes from hard drive
        loadWelcomeMsg();
        loadSettings();
        loadBannedIps();
    }
    void loadWelcomeMsg() 
    {
        Utility.createFile(this.settings_dir+"wm.txt");
        this.welcomeMsg = ""+Utility.readFile(this.settings_dir+"wm.txt");
    }
    public void saveWelcomeMsg()
    {
        PrintWriter writer = null;
        try {
            writer = new PrintWriter(this.settings_dir+"wm.txt", "UTF-8");
            writer.println(this.welcomeMsg);
            writer.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Settings.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(Settings.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            writer.close();
        }
    }
    void loadSettings()
    {
        Utility.createFile(this.settings_dir+"set.txt");
        
        BufferedReader br = null;
        
        try {
            br = new BufferedReader(new FileReader(this.settings_dir+"set.txt"));
        } catch (FileNotFoundException ex) {
        }
        
        if(Utility.readFile(this.settings_dir+"set.txt").length()==0)
        {
            this.saveCurrent();
            return;
        }
        
        String line1 = null,line2 = null;

        do{
            try {
                line1 = br.readLine();
                if(line1==null) continue;
                line2 = br.readLine();
                if(line2==null) continue;
                //System.out.println(line1+" "+line2);
                if(line1.equals("HOME_DIR")) this.home_directory = line2;
                if(line1.equals("MAX_CL")) this.MAX_CLIENTS = Integer.parseInt(line2);
                if(line1.equals("MAX_UP")) this.MAX_UPLOAD_THREADS = Integer.parseInt(line2);
                if(line1.equals("MAX_DWN")) this.MAX_DOWNLOAD_THREADS = Integer.parseInt(line2);
                if(line1.equals("ADMIN_PORT")) this.ADMIN_PORT = Integer.parseInt(line2);
            } catch (IOException ex) {  }
        }
        while(line1!=null);
            
        try {
            this.saveCurrent();
            br.close();
        } catch (IOException ex) {
        }
    }
    void loadBannedIps()
    {
        Utility.createFile(this.settings_dir+"bndip.txt");
        
        BufferedReader br = null;
        
        try {
            br = new BufferedReader(new FileReader(this.settings_dir+"bndip.txt"));
        } catch (FileNotFoundException ex) {
        }
        
        
        String line = null;

        do{
            try {
                line = br.readLine();
                if(line==null) continue;
                bannedIps.add(line);
            } catch (IOException ex) {  }
        }
        while(line!=null);
            
        try {
            br.close();
        } catch (IOException ex) {
        }
        
    }
    public void saveCurrent()
    {
        PrintWriter writer = null;
            try {
                writer = new PrintWriter(this.settings_dir+"set.txt", "UTF-8");
                writer.println("HOME_DIR");
                writer.println(this.home_directory);
                writer.println("MAX_CL");
                writer.println(this.MAX_CLIENTS);
                writer.println("MAX_UP");
                writer.println(this.MAX_UPLOAD_THREADS);
                writer.println("MAX_DWN");
                writer.println(this.MAX_DOWNLOAD_THREADS);
                writer.println("ADMIN_PORT");
                writer.println(this.ADMIN_PORT);
                writer.close();
            } catch (FileNotFoundException ex) {
                Logger.getLogger(Settings.class.getName()).log(Level.SEVERE, null, ex);
            } catch (UnsupportedEncodingException ex) {
                Logger.getLogger(Settings.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                writer.close();
            }
    }
    void addBannedIP(String IP)
    {
        try {
            this.bannedIps.add(IP);
            Files.write(Paths.get(this.settings_dir+"bndip.txt"), (IP).getBytes(), StandardOpenOption.APPEND);
            
            Files.write(Paths.get(this.settings_dir+"bndip.txt"), (System.getProperty("line.separator")).getBytes(), StandardOpenOption.APPEND);
            
        } catch (IOException ex) {
            Logger.getLogger(Settings.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
