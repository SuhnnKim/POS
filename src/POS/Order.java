/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package POS;

import java.util.List;

/**
 *
 * @author Suhn
 */
public class Order {
    int orderNum;    
    double totalPrice;
    List<Item> orderedItems;
    //date and time;
    
    public Order(){}
    
    public Order(List<Item> list){        
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
    
    
    
}
