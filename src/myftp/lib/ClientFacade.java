/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package myftp.lib;

import java.awt.Color;
import java.awt.GridLayout;
import java.io.File;
import javax.swing.BorderFactory;
import myftp.UI.ClientFrame;
import myftp.UI.ClientSpecialUI.*;
import myftp.UI.ClientSpecialUI.ClientDataTransferObject;
import myftp.UI.ServerFrame;
import myftp.UI.ServerSpecialUI.TransferObject;

/**
 *
 * @author RAUF00N
 */
public class ClientFacade {
    public ClientFrame clientframe;
    public static ClientFacade clientfacade = null;
    private ClientFacade(){
        
    }
    public static ClientFacade getInstance()
    {
        if(clientfacade==null) clientfacade = new ClientFacade();
        return clientfacade;
    }
    public void setUI(ClientFrame clientframe)
    {
        this.clientframe = clientframe;
    }
    public void writeOnMessageLog(String msg,int action)
    {
        clientframe.messageLog.writeMsgLog(msg, action);
    }
    public TransferObject addDownload(String address,String path)
    {
        String[] values = new String[10];
        synchronized(this){
        values[0]=""+(clientframe.dataTransferPanel.transferObjects.size()+1);
        }
        values[1]=address+"";
        values[2]="Download";
        values[3]="0mbps";
        values[4]="0%";
        TransferObject tr=new ClientDataTransferObject(values,path,"DOWNLOAD");
        synchronized(this){
            clientframe.dataTransferPanel.addTransferObject(tr);
        }
        return tr;
    }
    public TransferObject addUpload(String address,String path)
    {
        String[] values = new String[10];
        synchronized(this){
            values[0]=""+(clientframe.dataTransferPanel.transferObjects.size()+1);
        }
        values[1]=address+"";
        values[2]="Upload";
        values[3]="0mbps";
        values[4]="0%";
        TransferObject tr=new ClientDataTransferObject(values,path,"UPLOAD");
        synchronized(this){
            clientframe.dataTransferPanel.addTransferObject(tr);
        }
        return tr;
    }
    public void removeFromTransferList(TransferObject o)
    {
        synchronized(this){
            clientframe.dataTransferPanel.transferObjects.remove(o);
            int i=clientframe.dataTransferPanel.transferObjects.indexOf(o);
            
                for(;i<clientframe.dataTransferPanel.transferObjects.size();i++)
                {
                    String S =""+clientframe.dataTransferPanel.transferObjects.get(i).SL.getText();
                    clientframe.dataTransferPanel.transferObjects.get(i).SL.setText((Integer.parseInt(S)-1)+"");
                }
            }
    }
    
    public void setTransferProgress(TransferObject o,int prog) // in thread
    {
        o.setProgress(prog);
        o.PRG.setText(""+prog+"%");
    }
    public void setTransferRate(TransferObject o,String rate) // in thread
    {
        o.RATE.setText(""+rate+"bps");
    }
    public void loadServerDirectory(File[] files)
    {     
        synchronized(this){
            clientframe.serverStorageBrowser.scrollpanel.removeAll();
            int R=6,C=6,cnt = 35;
            try{
                R=files.length/6 +1;
                C=6;
                if(R<5) R=5;
                cnt=R*C;
            }
            catch(Exception e)
            {
            }
            clientframe.serverStorageBrowser.scrollpanel.setLayout(new GridLayout(R,C));
            //System.out.println(clientframe.serverStorageBrowser.scrollpanel.getLayout());
            if(files==null)
            {
                File file = new File("HOME");
                // prepare home
                clientframe.serverStorageBrowser.scrollpanel.add(new $Directory(file,clientframe.serverStorageBrowser));
                cnt--;
                while(cnt>0)
                {
                    clientframe.serverStorageBrowser.scrollpanel.add(new empty_panel());
                    cnt--;
                }
                return;
            }
            for(File f: files)
            {   
                if(!f.isDirectory()) continue;
                clientframe.serverStorageBrowser.scrollpanel.add(new $Directory(f,clientframe.serverStorageBrowser));
                cnt--;
            }
            while(cnt>0)
            {
                clientframe.serverStorageBrowser.scrollpanel.add(new empty_panel());
                cnt--;
            }
            clientframe.serverStorageBrowser.scrollpanel.revalidate();
            clientframe.serverStorageBrowser.scrollpanel.repaint();
        }
    }
    
    //..........................................................
    //.........................................................
    public void loadServerFiles(File[] files)
    {
        if(files==null) return;
        synchronized(this){
            clientframe.serverStorageBrowser.filesPanel.scrollpanel.removeAll();
            for(File f: files)
            {
                if(!f.isDirectory())
                {
                    clientframe.serverStorageBrowser.filesPanel.scrollpanel.add(new $downFile(f,clientframe));
                }
            }
            clientframe.serverStorageBrowser.filesPanel.scrollpanel.revalidate();
            clientframe.serverStorageBrowser.filesPanel.scrollpanel.repaint();
        }   
    }
}
