/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package POS;

import java.awt.Dimension;
import java.util.HashMap;
import java.util.List;
import javax.swing.JButton;

/**
 *
 * @author Suhn
 */
public class OrderHandler {
    private static OrderHandler theInstance;
    HashMap<Integer, Order> orderList = new HashMap();
    private int currentOrderNumber = 0;
    
    public static OrderHandler getInstance(){
        if(theInstance==null){
            theInstance = new OrderHandler();
        }
        return theInstance;
    }
    
    public void addOrder(int tb, List<Item> foodList){
        Order newOrder = new Order(foodList);
        orderList.put(tb, newOrder);
        currentOrderNumber = orderList.size();
        newOrder.setOrderNum(currentOrderNumber);
    }
    
    public String getDetails(int tn){
        String orderDetail = orderList.get(tn).getDetails();
        return orderDetail;
    }
    
    public double getTotalPrice(int tn){
        return orderList.get(tn).getTotal();
    }
    /**
     * Show current orders on Customer panel
     */
//    public void showOrders(){
//        pnlCusShow.removeAll();
//        int cusNum = orderList.size();
//        JButton[] cusButtons = new JButton[cusNum];
//        for(int i=0; i<cusNum; i++){
//            String orderDetail = orderList.get(i).toString();
//            cusButtons[i] = new JButton(orderDetail);
//            cusButtons[i].setPreferredSize(new Dimension(100,100));
//            pnlCusShow.add(cusButtons[i]);
//        }
//    }
    
//    public String toString(){
//        String orderDetail = "테이블 " + this.tableNum + "\n" +
//                this.orderedItems.toString() + "\n" +
//                this.getTotal();
//        return orderDetail;
//    }
    
}
