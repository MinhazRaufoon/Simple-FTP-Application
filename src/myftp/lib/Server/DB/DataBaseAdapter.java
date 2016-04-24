/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package myftp.lib.Server.DB;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DataBaseAdapter {
    String host="jdbc:derby://localhost:1527/myFTPdatabase";
    String user= "myFTP";
    String pass = "myFTP";
    
    Connection con;
    
    static DataBaseAdapter obj = null;
    
    private DataBaseAdapter()
    {
        try {
            con = DriverManager.getConnection(host,user,pass);
            
            Statement st = con.createStatement();
            String createtable = "CREATE TABLE REGISTERED_USER ("
                    + "USERNAME VARCHAR(50) PRIMARY KEY ,"
                    + "PASSWORD VARCHAR(100),"
                    + "EMAIL VARCHAR(50)"
                    + ")";
            try {st.execute(createtable);}catch(Exception e){}
            
            
        } catch (SQLException ex) {
            //Logger.getLogger(DataBaseAdapter.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public boolean verifyUser(String user,String pass)
    {
        try {
            Statement st = con.createStatement();
            String search = "SELECT USERNAME FROM REGISTERED_USER "
                    + "WHERE USERNAME = '"+user+"' AND PASSWORD = '"+pass+"'";
            ResultSet rs = st.executeQuery(search);
            
            if(!rs.next()) return false;
            else return true;
            
        } catch (SQLException ex) {
            Logger.getLogger(DataBaseAdapter.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
    public ArrayList getAll()
    {
        try {
            Statement st = con.createStatement();
            String search = "SELECT * FROM REGISTERED_USER";
            ResultSet rs = st.executeQuery(search);
            
            ArrayList strings=new ArrayList();
            while(rs.next())
            {
                strings.add(rs.getString(1)+" "+rs.getString(2)+" "+rs.getString(3)) ;
            }
            return strings;
        } catch (SQLException ex) {
            Logger.getLogger(DataBaseAdapter.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    public void addRow(String user,String pass,String eml)
    {
        try {
            Statement st = con.createStatement();
            String search = "INSERT INTO REGISTERED_USER "
                    + "VALUES ( '"+user+"' , '"+pass+"' , '"+eml+"' )";
            st.execute(search);
            
        } catch (SQLException ex) {
            Logger.getLogger(DataBaseAdapter.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public static DataBaseAdapter getInstance()
    {
        if(obj==null) obj = new DataBaseAdapter();
        return obj;
    }
}
