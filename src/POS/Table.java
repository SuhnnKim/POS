/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package POS;



/**
 *
 * @author Suhn
 */
public class Table {
    private int tableNumber;
    private String customerInfo;
    private boolean occupied = false;
    private long sittingTime;
    private Order order;
    
    public Table(){
        this.occupied = true;
        this.sittingTime = System.currentTimeMillis();
    }
    
    public void setTblNum(int tn){
        this.tableNumber=tn;
    }
    
    public void setCusInfo(String cus){
        this.customerInfo = cus;
    }
    
    public long getSittingTime(){
        long diff = System.currentTimeMillis() - this.sittingTime;
        return diff;
    }
}
