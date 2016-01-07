/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package POS;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Suhn
 */
public class Order {
    int orderNum;
    int tableNum;
    double totalPrice;
    List<Item> orderedItems;
    //date and time;
    
    public Order(){}
    
    public Order(int i,
                List<Item> list){
        this.tableNum = i;
        this.orderedItems = list;
    }
    
    //setters
    public void setOrderNum(int i){
        this.orderNum = i;
    }
    
    //getters
    public int getOrderNum(){
        int i = this.orderNum;
        return i;
    }
    
    public double getTotal(){
        double total=0;
        for(Item item : orderedItems){
            total += item.price;
        }
        
        return total;
    }
    
    public String toString(){
        String orderDetail = "테이블 " + this.tableNum + "\n" +
                this.orderedItems.toString() + "\n" +
                this.getTotal();
        return orderDetail;
    }
    
}
