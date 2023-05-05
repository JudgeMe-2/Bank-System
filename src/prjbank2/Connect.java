/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package prjbank2;
import java.sql.*;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;


/**
 *
 * @author tulin
 */
public class Connect {
    Connection conn=null;
    public Connect(){
        try {
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/bankSample","root","");
            //JOptionPane.showMessageDialog(null, "Connected");
        } catch (SQLException ex) {
            Logger.getLogger(Connect.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public boolean registerUser(User user){
        Statement stmt;
        String sql=null;
        ResultSet rs=null;
        try {
            stmt = conn.createStatement();
            sql="select * from user where username='"+user.getUsername()+"'";
            rs =stmt.executeQuery(sql);
            if(rs.next()==false){
                sql="insert into user values('"+user.getUsername()+"','"+user.getPassword()+"','"+user.getFirstname()+"','"+user.getLastname()+"',0)";
                stmt.executeUpdate(sql);
                return true;
            }
        } catch (SQLException ex) {
            Logger.getLogger(Connect.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            try {
                conn.close();
            } catch (SQLException ex) {
                Logger.getLogger(Connect.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return false;
    }
    
    public int login(String username, String password){
        Statement stmt;
        String sql;
        ResultSet rs;
        try {
            stmt=conn.createStatement();
            sql ="select * from user where username='"+username+"' and password='"+password+"'";
            rs = stmt.executeQuery(sql);
            if (rs.next()==true) {
                if(Integer.parseInt(rs.getString("userType")) == 1) {
                    return 2;
                }
                return 1;
            } else {
                return 0;
            }  
        } catch (SQLException ex) {
            Logger.getLogger(Connect.class.getName()).log(Level.SEVERE, null, ex);
        }
        return -1;
    }
    
    public ArrayList<Account> displayAccount(String username){
        ArrayList<Account> acc = new ArrayList<Account>();
        String sql ="select * from account where username='"+username+"'";
        Statement stmt;
        ResultSet rs;
        
        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
            while(rs.next()){
              Account a = new Account(rs.getString(1),rs.getDouble(2)) ;
              acc.add(a);
            }
            
            
        } catch (SQLException ex) {
            Logger.getLogger(Connect.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return acc;
    }
    
    public boolean updateBalance(Account account){
        Statement stmt;
        String sql=null;
        ResultSet rs=null;
        try {
            stmt = conn.createStatement();
            sql="select * from account where accountnumber='"+account.getAccountNumber()+"'";
            rs =stmt.executeQuery(sql);
            if(rs.next()){
                /*if(rs.getDouble(2) - account.getBalance() < 1000) {
                    JOptionPane.showMessageDialog(null, "Account must have a 1000 maintaining balance");
                    return false;
                }*/
                sql="insert into verification values('"+rs.getString(3)+"','"+account.getAccountNumber()+"','"+account.getBalance()+"', 'Update', 0)";
                stmt.executeUpdate(sql);
                JOptionPane.showMessageDialog(null, "Update will be proccess");
                return true;
            } else {
                JOptionPane.showMessageDialog(null, "Failed to process");
            }
        } catch (SQLException ex) {
            Logger.getLogger(Connect.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
    
    public void deleteAccount(Account account){
        Statement stmt;
        String sql=null;
        ResultSet rs=null;
        
        try {
            stmt = conn.createStatement();
            sql="select * from account where accountnumber='"+account.getAccountNumber()+"'";
            rs =stmt.executeQuery(sql);
            if(rs.next()){
                sql="insert into verification values('"+rs.getString(3)+"','"+account.getAccountNumber()+"','"+account.getBalance()+"', 'Delete', 0)";
                stmt.executeUpdate(sql);
                JOptionPane.showMessageDialog(null, "Deletion will be process");
                return;
            } else {
                JOptionPane.showMessageDialog(null, "Failed to process");
            }
        } catch (SQLException ex) {
            Logger.getLogger(Connect.class.getName()).log(Level.SEVERE, null, ex);
        }
        return;
    }
    
    public  ArrayList<Verification> displayVerification() {
        ArrayList<Verification> acc = new ArrayList<Verification>();
        String sql ="select * from verification where status<>1";
        Statement stmt;
        ResultSet rs;
        
        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
            //System.out.print(rs.getString(1) + rs.getString(2) + rs.getDouble(3) + rs.getString(4));
            while(rs.next()){
              Verification v = new Verification(rs.getString(1), rs.getString(2), rs.getDouble(3), rs.getString(4));
              acc.add(v);
            }
            
            
        } catch (SQLException ex) {
            Logger.getLogger(Connect.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return acc;
    }
    
    public boolean delete(Verification ver, boolean approval) {
        Statement stmt;
        String sql=null;
        ResultSet rs=null;
        if(approval) {
            
            try {
                stmt = conn.createStatement();
                sql="select * from account where accountnumber='"+ver.getAccnum()+"'";
                rs =stmt.executeQuery(sql);
                if(rs.next()){
                    if(!statuscheck(ver)) {
                        JOptionPane.showMessageDialog(null, "The request has already been approved.");
                        return false;
                    }
                    sql="delete from account where accountnumber='"+ver.getAccnum()+"' ";
                    stmt.executeUpdate(sql);
                    sql="update verification set status=1 where status=0 and accountNumber='"+ver.getAccnum()+"' and customerUsername='"+ver.getUname()+"' and amount="+ver.getAmnt()+" and typeoftransaction='Delete'";
                    stmt.executeUpdate(sql);
                    JOptionPane.showMessageDialog(null, "Account successfully deleted");
                    return true;
                } else {
                    JOptionPane.showMessageDialog(null, "Account does not exist");
                }
            } catch (SQLException ex) {
                Logger.getLogger(Connect.class.getName()).log(Level.SEVERE, null, ex);
            }
            return false;
        } else {
            try {
                stmt = conn.createStatement();
                sql="update verification set status=1 where status=0 and accountNumber='"+ver.getAccnum()+"' and customerUsername='"+ver.getUname()+"' and amount="+ver.getAmnt()+" and typeoftransaction='Delete'";
                stmt.executeUpdate(sql);
                return false;
            } catch (SQLException ex) {
                Logger.getLogger(Connect.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return false;
    }   
    
    public boolean update(Verification ver, boolean approval) {
        Statement stmt;
        String sql=null;
        ResultSet rs=null;
        if(approval) {
            try {
                stmt = conn.createStatement();
                sql="select * from account where accountnumber='"+ver.getAccnum()+"'";
                rs =stmt.executeQuery(sql);
                if(rs.next()){
                    if(!statuscheck(ver)) {
                        JOptionPane.showMessageDialog(null, "The request has already been approved.");
                        return false;
                    }
                    sql="update account set balance="+ver.getAmnt()+" where accountnumber='"+ver.getAccnum()+"' ";
                    stmt.executeUpdate(sql);
                    sql="update verification set status=1 where status=0 and accountNumber='"+ver.getAccnum()+"' and customerUsername='"+ver.getUname()+"' and amount="+ver.getAmnt()+" and typeoftransaction='Update'";
                    stmt.executeUpdate(sql);
                    JOptionPane.showMessageDialog(null, "Account successfully Updated");
                    return true;
                } else {
                    JOptionPane.showMessageDialog(null, "Failed update account");
                }
            } catch (SQLException ex) {
                Logger.getLogger(Connect.class.getName()).log(Level.SEVERE, null, ex);
            }
            return false;
        } else {
            try {
                stmt = conn.createStatement();
                sql="update verification set status=1 where status=0 and accountNumber='"+ver.getAccnum()+"' and customerUsername='"+ver.getUname()+"' and amount="+ver.getAmnt()+" and typeoftransaction='Delete'";
                stmt.executeUpdate(sql);
                return false;
            } catch (SQLException ex) {
                Logger.getLogger(Connect.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return false;
    }
    
    public boolean statuscheck(Verification ver) {
        Statement stmt;
        String sql=null;
        ResultSet rs=null;
        try {
            stmt = conn.createStatement();
            sql="select * from verification where accountNumber='"+ver.getAccnum()+"' and customerUsername='"+ver.getUname()+"' and amount="+ver.getAmnt()+" and status=0";
            rs =stmt.executeQuery(sql);
            if(rs.next()){
                
                return true;
            }
        } catch (SQLException ex) {
            Logger.getLogger(Connect.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
    public boolean addAccount(Account acc) {
        Statement stmt;
        String sql=null;
        ResultSet rs=null;
        try {
            stmt = conn.createStatement();
            sql="select * from account where accountnumber='"+acc.getAccountNumber()+"'";
            rs =stmt.executeQuery(sql);
            if(!rs.next()){
                sql = "insert into account values('"+acc.getAccountNumber()+"', "+acc.getBalance()+", '"+acc.getUsername()+"')";
                int n = stmt.executeUpdate(sql);
                if(n > 0) {
                    JOptionPane.showMessageDialog(null, "Account successfully added");
                } else {
                    JOptionPane.showMessageDialog(null, "Failed to add account");
                }
                return true;
            }
        } catch (SQLException ex) {
            Logger.getLogger(Connect.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
    
    public boolean instantUpdate(Account acc) {
        Statement stmt;
        String sql=null;
        ResultSet rs=null, rs2 = null;
        try {
            stmt = conn.createStatement();
            sql="select * from account where accountnumber='"+acc.getAccountNumber()+"'";
            rs =stmt.executeQuery(sql);
            if(rs.next()){
                sql="update account set balance=" + acc.getBalance() + " where accountnumber=" + acc.getAccountNumber();
                stmt.executeUpdate(sql);
                return true;
            }
        } catch (SQLException ex) {
            Logger.getLogger(Connect.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
    
    public Account getAccount(String accnum) {
        Statement stmt;
        String sql = "select * from account where accountnumber=" + accnum;
        ResultSet rs;
        
        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
            
            if(rs.next()) {
                return new Account(rs.getString(1), rs.getDouble(2));
            }
        } catch (SQLException ex) {
            Logger.getLogger(Connect.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
}
