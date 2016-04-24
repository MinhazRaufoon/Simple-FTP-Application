/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package myftp.UI.Utility;

import java.awt.*;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.Socket;
import javax.swing.*;


public class Utility {

    public static void setComponentSize(JComponent component,double perc_width, double perc_height)
    {
        Dimension screenSize=component.getParent().getMaximumSize();
        int height,width;
        width = (int) Math.round(perc_width * screenSize.width);
        height = (int) Math.round(perc_height * screenSize.height);
        component.setMaximumSize(new Dimension(width, height));
        component.setPreferredSize(new Dimension(width, height));
    }
    public static String[] strings(String x,String y)
    {
        String[] out= new String[2];
        out[0] = x;
        out[1] = y;
        return out;
    }
    public static String excludeSpace(String in)
    {
        String  out="";
        for(int i=0;i<in.length();i++)
        {
            if(in.charAt(i)!=' ') out+=in.charAt(i)+"";
        }
        return out;
    }
    public static File[] getLocalDrives()
    {
        return File.listRoots();        
    }
    public static File[] getFileList(String path)
    {
        File folder = new File(path);
        File[] listOfFiles = folder.listFiles();
        
        return listOfFiles;
    }
    public static void SendMessage(Socket receiver, String msg) throws IOException
    {
        (new DataOutputStream( receiver.getOutputStream() )).writeUTF(msg);
    }
}

