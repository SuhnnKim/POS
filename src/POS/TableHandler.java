/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package POS;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

/**
 *
 * @author Suhn
 */
public class TableHandler {    
    private static TableHandler theInstance;
    HashMap<Integer, Table> cusTblList = new HashMap();    
    Set tableNumbers = cusTblList.keySet();
    
    private boolean isOccupied = false;
    private boolean hasOrdered = false;
    private boolean foodDelivered = false;
    private boolean hasPaid = false;
    
    public static TableHandler getInstance(){
        if(theInstance==null){
            theInstance = new TableHandler();            
        }
        return theInstance;
    }
    
    public boolean isOccupied(int tb){
        if(tableNumbers.contains(tb)){
            isOccupied = true;
        }else{
            isOccupied = false;
        }      
        return isOccupied;
    }
    
    public void addTable(int t, Table table){
        cusTblList.put(t, table);
    }
    
    public void occupyTable(int tblNum, String cusInfo){
        Table tbl = new Table();
        tbl.setTblNum(tblNum);
        tbl.setCusInfo(cusInfo);
        addTable(tblNum, tbl);
        
        POSForm pf = POSForm.getInstance();
        pf.displayOccupied(tblNum);
    }
    
    public String getCusInfo(int tblNum){
        String cusInfo = "";
        Table t = cusTblList.get(tblNum);
        cusInfo = t.getCusInfo();
        return cusInfo;
    }
}
