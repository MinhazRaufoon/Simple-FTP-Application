/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package myftp.lib.Server;

import static java.lang.Math.min;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import myftp.lib.FileTransfer.AsyncFileTransferThread;

public class TransferSchedulerThread extends Thread{
    FTP_Server myServer;
    String type;
    
    int first, last;
    
    public TransferSchedulerThread(FTP_Server myServer,String type)
    {
        this.myServer = myServer;
        this.type = type+"";
    }
    
    
    public void run()
    {
        init();
        while(this.myServer.SERVER_STATE == FTP_Server.SERVER_LOGGED_IN)
        {
            if(this.myServer.SERVER_RUNNING_STATE == FTP_Server.SERVER_PAUSED) continue;
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) { }
            reschedule();
        }
    }
    void init()
    {
        first = 0;
        
        if(type.equals("D"))
        {
            last = min(myServer.downloadThreads.size(),myServer.server_settings.MAX_DOWNLOAD_THREADS)-1;
            this.startDownloadThreads();
        }
        else
        {
            last = min(myServer.uploadThreads.size(),myServer.server_settings.MAX_UPLOAD_THREADS)-1;
            this.startUploadThreads();
        }
    }
    void reschedule()
    {
        if(type.equals("D"))
        {
            if(!myServer.downloadThreads.isEmpty()) rescheduleDownload();
        }
        else
        {
            if(!myServer.uploadThreads.isEmpty()) rescheduleUpload();
        }
    }
    void startDownloadThreads()
    {
        for(int i=first;i<=last;i++)
        {
            this.myServer.downloadThreads.get(i).start();
        }
    }
    void startUploadThreads()
    {
        for(int i=first;i<=last;i++)
        {
            this.myServer.uploadThreads.get(i).start();    
        }
    }
    void rescheduleUpload()
    {
        for(int i=first;i<=last;i++)
        {
            // if terminated or finished, remove it from array
            if(this.myServer.uploadThreads.get(i).STATE == AsyncFileTransferThread.FINISHED_STATE
                    ||
                this.myServer.uploadThreads.get(i).STATE == AsyncFileTransferThread.TERMINATED_STATE    
                    )
            {
                myServer.uploadThreads.remove(i);
                last--;
            }
            
            // if paused, do nothing
            else if(this.myServer.uploadThreads.get(i).STATE == AsyncFileTransferThread.PAUSED_STATE)
            {
                continue;
            }
            
            // if it's resumed, make it inactive
            else if(this.myServer.uploadThreads.get(i).STATE == AsyncFileTransferThread.RESUMED_STATE)
            {
                this.myServer.uploadThreads.get(i).STATE = AsyncFileTransferThread.INACTIVE_STATE;
            }
        }
        
        // change 'first' to next index of last
        first = (last+1)% myServer.uploadThreads.size();
        
        // change 'last' by first+N
        if(myServer.uploadThreads.size() <= myServer.server_settings.MAX_UPLOAD_THREADS)
        {
            last = first - 1;
            if(last==-1) last = myServer.uploadThreads.size()-1;
        }
        else
        {
            last = (first + myServer.server_settings.MAX_UPLOAD_THREADS)%myServer.uploadThreads.size();
        }
        
        // resume next N threads
        for(int i=first;i<=last;i++)
        {
            // if first>last
            if(i==myServer.uploadThreads.size() &&i!=last+1)
            {
                i = 0;
            }
            // if paused, do nothing
            if(myServer.uploadThreads.get(i).STATE == AsyncFileTransferThread.PAUSED_STATE) continue;            
            // if inactive thread, resume it
            else if(myServer.uploadThreads.get(i).STATE == AsyncFileTransferThread.INACTIVE_STATE)
            {
                myServer.uploadThreads.get(i).STATE = AsyncFileTransferThread.RESUMED_STATE;
            }
        }
    }
    void rescheduleDownload()
    {
        for(int i=first;i<=last;i++)
        {
            // if terminated or finished, remove it from array
            if(this.myServer.downloadThreads.get(i).STATE == AsyncFileTransferThread.FINISHED_STATE
                    ||
                this.myServer.downloadThreads.get(i).STATE == AsyncFileTransferThread.TERMINATED_STATE    
                    )
            {
                myServer.downloadThreads.remove(i);
                last--;
            }
            
            // if paused, do nothing
            else if(this.myServer.downloadThreads.get(i).STATE == AsyncFileTransferThread.PAUSED_STATE)
            {
                continue;
            }
            
            // if it's resumed, make it inactive
            else if(this.myServer.downloadThreads.get(i).STATE == AsyncFileTransferThread.RESUMED_STATE)
            {
                this.myServer.downloadThreads.get(i).STATE = AsyncFileTransferThread.INACTIVE_STATE;
            }
        }
        
        // change 'first' to next index of last
        first = (last+1)% myServer.downloadThreads.size();
        
        // change 'last' by first+N
        if(myServer.downloadThreads.size() <= myServer.server_settings.MAX_DOWNLOAD_THREADS)
        {
            last = first - 1;
            if(last==-1) last = myServer.downloadThreads.size()-1;
        }
        else
        {
            last = (first + myServer.server_settings.MAX_DOWNLOAD_THREADS)%myServer.downloadThreads.size();
        }
        
        // resume next N threads
        for(int i=first;i<=last;i++)
        {
            // if first>last
            if(i==myServer.downloadThreads.size() &&i!=last+1)
            {
                i = 0;
            }
            // if paused, do nothing
            if(myServer.downloadThreads.get(i).STATE == AsyncFileTransferThread.PAUSED_STATE) continue;            
            // if inactive thread, resume it
            else if(myServer.downloadThreads.get(i).STATE == AsyncFileTransferThread.INACTIVE_STATE)
            {
                myServer.downloadThreads.get(i).STATE = AsyncFileTransferThread.RESUMED_STATE;
            }
        }
    }
}
