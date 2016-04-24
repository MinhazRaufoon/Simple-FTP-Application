/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package myftp.lib.FileTransfer;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import myftp.UI.ServerSpecialUI.TransferObject;
import myftp.lib.Server.FTP_Server;

/**
 *
 * @author RAUF00N
 */
public abstract class AsyncFileTransferThread extends Thread{
    public FTP_Server server;
    /*
    percentage of completion of transfer
    */
    int PROGRESS = 0;
    
    /*
    states of the transfer
    */
    public static int PAUSED_STATE = 0;
    public static int RESUMED_STATE = 1;
    public static int FINISHED_STATE = 2;
    public static int INIT_STATE = 3;
    public static int TERMINATED_STATE = 4;
    public static int INACTIVE_STATE = 5;
    /*
    current state
    */
    public int STATE = INIT_STATE;
    
    /*
    socket at which file is being transfered
    */
    public Socket socket;
    DataInputStream in;
    DataOutputStream out;
    OutputStream os;
    /*
    the File reference of the file being transfered
    */
    File file;
    
    TransferObject UI;
    
    public AsyncFileTransferThread(Socket socket,FTP_Server server)
    {
        this.server = server;
        this.socket = socket;
        try {
            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());
            os = socket.getOutputStream();
        } catch (IOException ex) {
            Logger.getLogger(AsyncFileTransferThread.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    public void Pause()
    {
        this.STATE = PAUSED_STATE;
    }
    public void Resume()
    {
        this.STATE = RESUMED_STATE;
    }
    public void Finish()
    {
        this.STATE = FINISHED_STATE;
    }
    public void Terminate()
    {
        this.STATE = TERMINATED_STATE;
    }
    
    @Override
    public void start()
    {
        Resume();
        super.start();
    }
}
