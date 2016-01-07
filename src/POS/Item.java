/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package POS;

import java.io.Serializable;

/**
 *
 * @author suhnmikim
 */
public class Item implements Serializable{
    String name;
    double price;
    String category;
    String memo;
    //boolean isServed;
    
    public Item(){}
    
    public Item(String nm){
        this.name = nm;
    }
    
    public Item(String nm, double pr, String ct){
        this.name = nm;
        this.price = pr;
        this.category = ct;
    }
    
    //setters
    public void setPrice(double pr){
        this.price = pr;
    }
    
    public void setName(String nm){
        this.name = nm;
    }
    
    public void setCat(String ct){
        this.category = ct;
    }
    
    //getters
    public String getName(){
        return this.name;
    }
    
    public double getPrice(){
        return this.price;
    }
    
    public String toString(){
        return new StringBuffer()
                .append(this.name)
                .append("\n")
                .append(this.price).toString();                                
    }
    
}
