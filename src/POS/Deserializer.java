/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package POS;

import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.io.Serializable;

/**
 *
 * @author suhnmikim
 */
public class Deserializer {
    
    public static void main (String args[]){
        Deserializer deserializer = new Deserializer();
    }
    
    public Item deserializeItem(){
        
        Item item;
        
        try{
            FileInputStream fin = new FileInputStream("item.ser");
            ObjectInputStream ois = new ObjectInputStream(fin);
            item = (Item) ois.readObject();
            ois.close();
            
            return item;
        }catch(Exception ex){
            ex.printStackTrace();
            return null;
        }
        
    }
            
}
