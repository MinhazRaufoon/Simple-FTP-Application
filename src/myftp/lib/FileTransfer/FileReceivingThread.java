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
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import myftp.UI.ClientSpecialUI.ClientDataTransferObject;
import myftp.UI.CommonUIparts.MessageLog;
import myftp.UI.ServerSpecialUI.TransferObject;
import static myftp.lib.Client.FTP_Client.DOWNLOAD_REQUEST_PORT;
import myftp.lib.ClientFacade;
import static myftp.lib.FileTransfer.AsyncFileTransferThread.FINISHED_STATE;
import static myftp.lib.FileTransfer.AsyncFileTransferThread.PAUSED_STATE;
import static myftp.lib.FileTransfer.AsyncFileTransferThread.RESUMED_STATE;
import static myftp.lib.FileTransfer.AsyncFileTransferThread.TERMINATED_STATE;
import myftp.lib.Server.FTP_Server;
import myftp.lib.ServerFacade;

/**
 *
 * @author RAUF00N
 */
public class FileReceivingThread extends AsyncFileTransferThread{
    
    public FileReceivingThread(Socket socket,FTP_Server server) {
        super(socket,server);
    }
    @Override
    public void run()
    {
        
        try {
            out.writeUTF(socket.getPort()+"");
            String relative_path = this.in.readUTF();
            String path = ServerFacade.getInstance().serverframe.server.server_settings.home_directory+relative_path;
            String size = in.readUTF();
            long filesize = Long.parseLong(size);
            final InputStream socketStream= socket.getInputStream();
            File f= new File(path);
            final OutputStream fileStream=new FileOutputStream(f);
            
            ServerFacade.getInstance().writeOnMessageLog("Client "+socket.getInetAddress()+"Uploading to "+path, MessageLog.ACTION_UPLOAD);
            
            byte[] buffer = new byte[512];
            int number=0;
            long progress=0;
            long received=0;
            long starttime = System.currentTimeMillis();
            long time;
            TransferObject to = ServerFacade.getInstance().addUpload(socket.getInetAddress().toString(), path);
            
            while(STATE != TERMINATED_STATE && STATE != FINISHED_STATE)
            {
                //System.out.println(STATE);
                if(STATE==PAUSED_STATE) continue;
                number = socketStream.read(buffer);
                if(number==-1) break;
                fileStream.write(buffer,0,number);
                received+=number;
                progress = (long)(((double)received/(double)filesize)*100);
                time = System.currentTimeMillis();
                try{
                long speed = received/(time-starttime);
                speed*=1024;
                ServerFacade.getInstance().setTransferRate(to, speed+"");
                }catch(Exception e){}
                ServerFacade.getInstance().setTransferProgress(to, (int)progress);
                if(number<512) break;
            }
            //System.out.println("yyyy");
            STATE = FINISHED_STATE;
            
            
            ServerFacade.getInstance().removeFromTransferList(to);
            
            String rel = relative_path.substring(0, relative_path.lastIndexOf("\\"));
            out.writeUTF(rel);
            
            
            
            try{fileStream.close();}catch(Exception e){}
            try{socketStream.close();}catch(Exception e){}
            try{in.close();}catch(Exception e){}
            try{out.close();}catch(Exception e){}
            try{socket.close();}catch(Exception e){}
            
        } catch (IOException ex) {
            Logger.getLogger(FileReceivingThread.class.getName()).log(Level.SEVERE, null, ex);
        }
        this.server.uploadThreads.remove(this);
    }
}
