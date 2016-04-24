/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package myftp.lib.FileTransfer;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import myftp.UI.ClientSpecialUI.ClientDataTransferObject;
import myftp.UI.CommonUIparts.MessageLog;
import myftp.UI.ServerSpecialUI.TransferObject;
import myftp.lib.ClientFacade;
import static myftp.lib.FileTransfer.AsyncFileTransferThread.FINISHED_STATE;
import static myftp.lib.FileTransfer.AsyncFileTransferThread.TERMINATED_STATE;
import myftp.lib.Server.FTP_Server;
import myftp.lib.ServerFacade;

/**
 *
 * @author RAUF00N
 */
public class FileSendingThread extends AsyncFileTransferThread{
    
    public FileSendingThread(Socket socket,FTP_Server server) {
        super(socket,server);
        
    }
    
    @Override
    public void run()
    {
        String file_src=null;
        try {
            out.writeUTF(socket.getPort()+"");
            // read filesrc from receiver
            file_src = in.readUTF();
            
            // make file
            file = new File(file_src);
            
            // send file size to receiver
            out.writeUTF(file.length()+"");
            
            // update UI by adding transferObject
            ServerFacade.getInstance().writeOnMessageLog("Client "+socket.getInetAddress()+"Downloading "+file_src, MessageLog.ACTION_DOWNLOAD);
            
            this.UI = ServerFacade.getInstance().addDownload(socket.getInetAddress().toString(), file.getAbsolutePath());           
            
            byte[] buffer = new byte[512];
            int number;
            long size = file.length();
            long received = 0;
            long progress;
            long starttime = System.currentTimeMillis();
            long time;
            FileInputStream fis = new FileInputStream(file);
            
            while ( (STATE != TERMINATED_STATE && STATE != FINISHED_STATE)) {
                if(STATE==PAUSED_STATE) continue;
                number = fis.read(buffer);
                if(number==-1) break;
                try{os.write(buffer, 0, number);}catch(Exception e){}
                received += number;
                progress = (long)(((double)received / (double)size)*100);
                time = System.currentTimeMillis();
                try{
                long speed = received/(time-starttime);
                speed*=1024;
                ServerFacade.getInstance().setTransferRate(UI, speed+"");
                }catch(Exception e){}
                ServerFacade.getInstance().setTransferProgress(UI, (int)progress);
                
                
                if(number<512) break;
            }
            STATE = FINISHED_STATE;
            ServerFacade.getInstance().removeFromTransferList(UI);
            //System.out.println(this.file.getName()+" finished");
            
            try{out.close();}catch(Exception e){}
            try{in.close();}catch(Exception e){}
            try{os.close();}catch(Exception e){}
            try{fis.close();}catch(Exception e){}
            try{socket.close();}catch(Exception e){}
            
            this.server.downloadThreads.remove(this);
        } catch (IOException ex) {
            System.out.println(ex);
        }
        
        
    }
}
