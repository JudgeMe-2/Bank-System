/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package prjbank2;

/**
 *
 * @author Jadge
 */
public class Verification {
    private String uname;
    private String accnum;
    private double amnt;
    private String typeoftrans;
    private int status;

    public Verification(String uname, String accnum, double amnt, String typeoftrans) {
        this.uname = uname;
        this.accnum = accnum;
        this.amnt = amnt;
        this.typeoftrans = typeoftrans;
        status = 0;
    }

    public String getUname() {
        return uname;
    }

    public void setUname(String uname) {
        this.uname = uname;
    }

    public String getAccnum() {
        return accnum;
    }

    public void setAccnum(String accnum) {
        this.accnum = accnum;
    }

    public double getAmnt() {
        return amnt;
    }

    public void setAmnt(double amnt) {
        this.amnt = amnt;
    }

    public String getTypeoftrans() {
        return typeoftrans;
    }

    public void setTypeoftrans(String typeoftrans) {
        this.typeoftrans = typeoftrans;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
    
    
    
    
}
