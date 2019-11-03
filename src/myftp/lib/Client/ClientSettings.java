/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package myftp.lib.Client;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.logging.Level;
import java.util.logging.Logger;
import myftp.lib.Server.Settings;
import myftp.lib.Utility;

/**
 *
 * @author RAUF00N
 */
public class ClientSettings {
	String root = System.getProperty("user.home")+ "/";
    String settings_dir = root + "myFTP saved files/Client/";
    public String save_dir = root + "/myFTP downloads/";
    
    public ClientSettings()
    {
        new File(root + "myFTP downloads/").mkdir();
        new File(root + "myFTP saved files/").mkdir();
        new File(root + "myFTP saved files/Client/").mkdir();
        load();
    }
    void load() 
    {
        Utility.createFile(settings_dir+"settings.txt");
        
        if(Utility.readFile(settings_dir+"settings.txt").length()==0)
        {
            saveCurrent();
            return;
        }
        BufferedReader br = null;
        
        String line1 = null,line2 = null;
        try {
            br = new BufferedReader(new FileReader(this.settings_dir+"settings.txt"));
        } catch (FileNotFoundException ex) {
        }

        do{
            try {
                line1 = br.readLine();
                if(line1==null) continue;
                line2 = br.readLine();
                if(line2==null) continue;
                //System.out.println(line1+" "+line2);
                if(line1.equals("SAVE_DIR")) this.save_dir = line2;
            } catch (IOException ex) {  }
        }
        while(line1!=null);
    }
    public void saveCurrent()
    {
        PrintWriter writer = null;
            try {
                writer = new PrintWriter(settings_dir+"settings.txt", "UTF-8");
                writer.println("SAVE_DIR");
                writer.println(this.save_dir);
                writer.close();
            } catch (Exception ex) {
                Logger.getLogger(Settings.class.getName()).log(Level.SEVERE, null, ex);
            } 
            finally {
                writer.close();
            }
    }
}
