package POS;

import java.awt.Dimension;
import java.awt.Frame;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author suhnmikim
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        POSForm pos = POSForm.getInstance();
        
        pos.setVisible(true);
        
        //set full-screen when open
        //pos.setExtendedState(Frame.MAXIMIZED_BOTH);
        //pos.pack();
        pos.setSize(1024, 680);
        pos.setResizable(true);
        
        
    }
    
}
