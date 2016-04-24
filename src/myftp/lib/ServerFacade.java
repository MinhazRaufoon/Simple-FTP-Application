/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package myftp.lib;

import myftp.UI.ServerFrame;
import myftp.UI.ClientSpecialUI.ClientDataTransferObject;
import myftp.UI.ServerSpecialUI.TransferObject;

/**
 *
 * @author RAUF00N
 */
public class ServerFacade {
    public static ServerFacade serverfacade = null;
    public ServerFrame serverframe;
    private ServerFacade()
    {
    }
    public static ServerFacade getInstance()
    {
        if(serverfacade == null)
        {
            serverfacade = new ServerFacade();
        }
        return serverfacade;
    }
    public void setUI(ServerFrame serverframe)
    {
        this.serverframe = serverframe;
    }
    public void writeOnMessageLog(String msg,int action)
    {
        serverframe.messageLog.writeMsgLog(msg, action);
    }
    public TransferObject addDownload(String address,String path)
    {
        String[] values = new String[10];
        synchronized(this){
        values[0]=""+(serverframe.dataTransferPanel.transferObjects.size()+1);
        }
        values[1]=address+"";
        values[2]="Download";
        values[3]="0mbps";
        values[4]="0%";
        TransferObject tr=new TransferObject(values,path,"DOWNLOAD");
        synchronized(this){
        serverframe.dataTransferPanel.addTransferObject(tr);
        }
        return tr;
    }
    public TransferObject addUpload(String address,String path)
    {
        String[] values = new String[10];
        synchronized(this){
            values[0]=""+(serverframe.dataTransferPanel.transferObjects.size()+1);
        }
        values[1]=address+"";
        values[2]="Upload";
        values[3]="0mbps";
        values[4]="0%";
        TransferObject tr=new TransferObject(values,path,"UPLOAD");
        synchronized(this){
            serverframe.dataTransferPanel.addTransferObject(tr);
        }
        return tr;
    }
    public void removeFromTransferList(TransferObject o)
    {
        synchronized(this){
            int i = serverframe.dataTransferPanel.transferObjects.indexOf(o);
            (serverframe.dataTransferPanel.transferObjects.get(i)).setVisible(false);
            serverframe.dataTransferPanel.transferObjects.remove(o);
            
                for(;i<serverframe.dataTransferPanel.transferObjects.size();i++)
                {
                    String S =""+serverframe.dataTransferPanel.transferObjects.get(i).SL.getText();
                    serverframe.dataTransferPanel.transferObjects.get(i).SL.setText((Integer.parseInt(S)-1)+"");
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
    public void setConnectedServerTitle(String title)
    {
        serverframe.titleBar.setConnectedServerTitle(title);
    }
}
