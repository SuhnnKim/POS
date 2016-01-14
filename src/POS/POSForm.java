package POS;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


/**
 *
 * @author suhnmikim
 */
public final class POSForm extends JFrame{
    private static POSForm theInstance;
    
    public POSForm(){
        this.itemMap = new HashMap<>();
        itemMap = deserializeMap();
      
        createContent();
        //when start, display the main control
        pnlMainControl.setVisible(true);
        pnlItemControl.setVisible(false);
        pnlCusTable.setVisible(false);            
        displayTableButtons();
        displayItems();
        
        th = TableHandler.getInstance();
    }
    
    public static POSForm getInstance(){
        if(theInstance==null){
            theInstance = new POSForm();            
        }
        return theInstance;
    }
        
    /**
     * Display Customer table, Item control, New Table panels upon corresponding buttons clicked
     * Customer table panel hasn't been inserted yet!!!
     */              
    private class PanelHandler
        implements ActionListener
    {        
        
        @Override
        public void actionPerformed(ActionEvent e) {
                    
        String cmd = e.getActionCommand();
                                        
        if(cmd.equals(ITEM_CONTROL)){
            pnlItemControl.setVisible(true);
            pnlMainControl.setVisible(false);
            pnlCusTable.setVisible(false);
            //Display existing food items
            //displayItems();
        }else if(cmd.equals(MAIN_CONTROL)){
            pnlMainControl.setVisible(true);
            pnlItemControl.setVisible(false);
            pnlCusTable.setVisible(false);            
            displayTableButtons();
            displayItems();
            
        }else if(cmd.equals(CUS_TABLE)){
            pnlCusTable.setVisible(true);
            pnlMainControl.setVisible(false);
            pnlItemControl.setVisible(false);
            showOrders();
            }
        }
    }
            
    /* Item management
     * Load food items when opening
     * Add new item, edit item properties
     */
    private class ItemHandler
        implements ActionListener        
    {                                          
        @Override
        public void actionPerformed(ActionEvent evt)
        {                        
            //Add or edit food items
            String nm = txtName.getText();
            String pr = txtPrice.getText();
            double price = Double.parseDouble(pr);
            String ct = txtCat.getText();
            
            String itemDetail = "메뉴명: " + nm + "\n"
                    + "가격: " + pr + "\n"
                    + "분류: " + ct;
                                        
            Object options[] = {"취소", "저장"};
            int n = JOptionPane.showOptionDialog(pnlItemControl,
                    "아래 메뉴를 저장하시겠습니까? \n" + itemDetail,
                    "확인",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.PLAIN_MESSAGE,
                    null,
                    options,
                    options[1]);
            if(n==0){
                JOptionPane.showMessageDialog(pnlItemControl,
                        "취소");               
            }else{
                JOptionPane.showMessageDialog(pnlItemControl,
                        "저장");
                                               
                Item item = new Item(nm, price, ct);
                saveItems(item);
                
                //displayItems(); // <<<< doesn't display newly added item immediately
            }
            
        }
    }
    
    /**
     * Place a new order or cancel
     */
    
    private class NewOrderHandler
        implements ActionListener
    {
        @Override
        public void actionPerformed(ActionEvent e) {
            String cmd = e.getActionCommand();
            if(cmd.equals(REMOVE_ITEM)){
                int index = list.getSelectedIndex();
                selItem.removeElementAt(index);
            }else if(cmd.equals(PROCEED_ORDER)){
                Object options[] = {"Cancel", "OK"};
                String orderDetail = "Table Number: " + lblSelNum.getText() + "\n" +
                        selItem.toString();
                int n = JOptionPane.showOptionDialog(pnlNewOrder,
                        "Ordering below \n" + orderDetail,
                        "Confirm Order",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.PLAIN_MESSAGE,
                        null,
                        options,
                        options[1]);
                if(n==1){                
                    JOptionPane.showMessageDialog(pnlNewOrder,
                            "Order is placed");
                    int tblNum = Integer.parseInt(lblSelNum.getText());

                    List<Item> ordered = new ArrayList<>();

                    for(int k=0; k<selItem.size(); k++){
                        String it = selItem.getElementAt(k).toString();
                        Item item = itemMap.get(it);
                        ordered.add(item);
                        System.out.println(it + "has been added");
                    }
                    Order newOrder = new Order(tblNum, ordered);
                    orderList.add(newOrder);
                    
                    System.out.println("A new order has been placed successfully");
                    selItem.removeAllElements();
                    lblSelNum.setText("");
                }
                
            }else if(cmd.equals(CLEAR_SELECTED)){
                selItem.removeAllElements();
                lblSelNum.setText("");
            }
        }
        
    }
    
    /**
     * When a table is selected,
     * display the table number 
     */
    private class TblNumHandler
            implements ActionListener
    {                     
        @Override
        public void actionPerformed(ActionEvent e) {
            lblSelNum.setText(null);
            String tblNum = e.getActionCommand();
            int tn = Integer.parseInt(tblNum);            
            lblSelNum.setText(tblNum);
        }        
    }
    
    /**
     * When a table is selected,
     * 
     */
    private class TblButtonHandler
        implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e) {
            String tblNum = e.getActionCommand();
            // check if the table is already occupied
            if(!th.isOccupied(Integer.parseInt(tblNum))){
                setCusInfo(Integer.parseInt(tblNum));
            }
        }   
    }
      
    /**
     * Display the selected items in the list
     * If the table has no customer it will show a dialog
     */
    private class OrderItemHandler
        implements ActionListener
    {
        @Override
        public void actionPerformed(ActionEvent e) {
            String tblNum = lblSelNum.getText();            
            if(!tblNum.equals("")){
                String item = e.getActionCommand();                              
                selItem.addElement(item);
            }else{
                JOptionPane.showMessageDialog(pnlMainControl,
                        "Select a table first");
            }
        }        
    }
    
    /**
     * Add new item to itemList, serialize the list
     */    
    public void saveItems(Item it){
        itemMap.put(it.getName(), it);
        serializeMap(itemMap);
    }
    
    public void serializeMap(HashMap<String, Item> map){
        try{
            FileOutputStream fout = new FileOutputStream("itemMap.ser");
            ObjectOutputStream oos = new ObjectOutputStream(fout);
            oos.writeObject(map);
            oos.close();
            fout.close();
            System.out.println("Serialized Hashmap data is saved succesfully");
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }
    
    /**
     * Deserialize saved itemlist
     * Display item details
     * Set action listener on each item button
     */    
    public void displayItems(){
        pnlItemButtons.removeAll();
        itemMap = deserializeMap();
        
        int itemNum = itemMap.size();
        int i = 0;
        JButton[] itemButtons = new JButton[itemNum];
        ActionListener picItem = new OrderItemHandler();
        while(iterator.hasNext()){
            String key = iterator.next();
            Item it = itemMap.get(key);
            String itemDetails = key + "\n" + it.getPrice();
            itemButtons[i] = new JButton(itemDetails);
            pnlItemButtons.add(itemButtons[i]);
            
            itemButtons[i].addActionListener(picItem);
            itemButtons[i].setActionCommand(key);
            i++;
        }             
    }
    
    /**
     * When an empty table is selected,
     * a dialog to get customer info will pop up
     * If 'cancel' is clicked, table selection will be canceled.
     * No customer info input will be accepted.
     * Implemented only when the table is not already occupied
     */
    public void setCusInfo(int tblNum){
        String info = (String)JOptionPane.showInputDialog(
                        pnlMainControl, "Enter customer information");
                CUSTOMER_INFO = info;  
                
                String okMsg = info + " is sitting at table " + tblNum;
                String cancelMsg = "Table selection is cancelled";

                if(info==null){
                    JOptionPane.showMessageDialog(pnlMainControl, cancelMsg);
                }else{
                    JOptionPane.showMessageDialog(pnlMainControl, okMsg);
                    th.occupyTable((tblNum), info);
                }
    }
    
    public HashMap<String, Item> deserializeMap(){
        try{
            FileInputStream fin = new FileInputStream("itemMap.ser");
            ObjectInputStream ois = new ObjectInputStream(fin);
            itemMap = (HashMap) ois.readObject();
            ois.close();
            fin.close();
            System.out.println("HashMap is deserialized succesfully");            
            
            iterator = itemMap.keySet().iterator();
            
            return itemMap;
            
        }catch(Exception ex){
            ex.printStackTrace();
            return null;
        }
    }
                        
    /**
     * Display table buttons with numbers
     * When a table is selected, it will show the table number
     * Show taken tables disabled
     */
    public void displayTableButtons(){
        pnlPicTable.removeAll();        
        tblButtons = new JButton[numOfTables];
        // showing table number when button is selected
        ActionListener picTblNum = new TblNumHandler();
        // getting customer info
        ActionListener tblHandler = new TblButtonHandler();
        
        // labelling each button with table number
        for(int i=0; i<numOfTables; i++){
            int realNum = i + 1;
            String tblNum = Integer.toString(realNum);            
            tblButtons[i] = new JButton(tblNum);
            pnlPicTable.add(tblButtons[i]);
            
            // add Action Listeners
            tblButtons[i].addActionListener(picTblNum);
            tblButtons[i].addActionListener(tblHandler);
            tblButtons[i].setActionCommand(tblNum);            
        }
    }
    
    /**
     * Show a customer table occupied
     */
    public void displayOccupied(int tn){
        tblButtons[tn-1].setOpaque(true);
        tblButtons[tn-1].setBackground(Color.red);                
    }                
    
    /**
     * Show current orders on Customer panel
     */
    public void showOrders(){
        pnlCusShow.removeAll();
        int cusNum = orderList.size();
        JButton[] cusButtons = new JButton[cusNum];
        for(int i=0; i<cusNum; i++){
            String orderDetail = orderList.get(i).toString();
            cusButtons[i] = new JButton(orderDetail);
            cusButtons[i].setPreferredSize(new Dimension(100,100));
            pnlCusShow.add(cusButtons[i]);
        }
    }
    
            
    /**
     * Declare components
     */  
    
    //Main menu components
    JPanel pnlOperation;
    JPanel pnlSubOperation;
    JPanel pnlControl;
    JPanel pnlItemControl;      
    JPanel pnlCusTable;        
    JPanel pnlMainControl;    
    
    JLayeredPane layeredPane;
            
    JButton butMainControl;    
    JButton butItemControl;
    JButton butCusTable;
    JButton[] tblButtons;
            
    JScrollPane scrPane;
    
    //Item Control Panel components
    JPanel pnlItem1;
    JPanel pnlItem2;
    JPanel pnlItem3;
    JPanel pnlItemButtons;  
    JLabel lblName;
    JLabel lblPrice;
    JLabel lblCat;    
    JTextField txtName;
    JTextField txtPrice;
    JTextField txtCat;
    JButton butSaveItem;
    
    //New Customer Panel components
    JPanel pnlCusInfo;
    JPanel pnlNewOrder;
    JPanel pnlSubNewOrder;
    JPanel pnlSubNewOrder2;
    JPanel pnlSubNewOrder3;
    JPanel pnlSubNewOrder4;
    JPanel pnlPicTable;
    JScrollPane jsp;
    JLabel lblTblNum;
    JLabel lblSelNum;
    JLabel lblOrder;
    JButton butRemove;
    JButton butConfirmOrder;
    JButton butCancelOrder;
    
    //Customer Table components
    JPanel pnlCusShow;
    JPanel pnlPayment;
    JPanel pnlPayCash;
    JPanel pnlPayCash1;
    JPanel pnlPayCash2;
    JPanel pnlPayDetail;
    JPanel pnlSubDetail1;
    JPanel pnlSubDetail2;
    JPanel pnlSubDetail3;
    JButton butHalfK;
    JButton butK;
    JButton but5K;
    JButton but10K;    
    JButton butExit;
    JLabel lblOrderInfo;
    JLabel lblorderedItems;
    JLabel lblQty;
    JLabel lblPrc;
    
    /**
     * Action commands
     */    
    private static final String MAIN_CONTROL = "mainCtrl";
    private static final String ITEM_CONTROL = "itemctrl";
    private static final String CUS_TABLE = "custable";
    private static final String SALE_CLOSE = "cl";
    
    private static final String REMOVE_ITEM = "rmItem";
    private static final String CLEAR_SELECTED = "delSel";
    private static final String PROCEED_ORDER = "orderIN";
    private static final int numOfTables = 20;
    private static String CUSTOMER_INFO = "";
    TableHandler th;
    
    /**
     * Lists
     */
    //JList for new customer item selection    
    DefaultListModel selItem = new DefaultListModel();
    JList list = new JList(selItem);
    HashMap<String, Item> itemMap;
    Iterator<String> iterator;
    
    //Order list
    static List<Order> orderList = new ArrayList<>();
        
    /**
     * Create contents when starting the form
     */    
    void createContent(){        
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);        
        setTitle("딸내미가 프로그래머 POS");
        
        //Frame layout
        this.setLayout(new BorderLayout());
        
        //Add Layered pane, Operation pane,
        layeredPane = new JLayeredPane();
        add(layeredPane, BorderLayout.CENTER);
        layeredPane.setBorder(new EtchedBorder());
        
        //운영(Operation pane)
        pnlOperation = new JPanel();      
        add(pnlOperation, BorderLayout.EAST);
        pnlOperation.setBorder(new TitledBorder(new EtchedBorder(), "운영"));
        pnlOperation.setLayout(new GridLayout(2,1));
        
        //관리모드 pane inside Operation pane <--- 이거 뭔지 까머금-_-
        pnlControl = new JPanel(new GridLayout(0,1));        
        pnlOperation.add(pnlControl);
        pnlControl.setBorder(new TitledBorder(new EtchedBorder(), "관리모드"));        
        //새테이블 버튼
        butMainControl = new JButton("Main Control");
        pnlControl.add(butMainControl);        
        butMainControl.setActionCommand(MAIN_CONTROL);
        //식당메뉴관리 버튼 
        butItemControl = new JButton("메뉴 관리");
        pnlControl.add(butItemControl);                
        butItemControl.setActionCommand(ITEM_CONTROL);
        //손님테이블 버튼
        butCusTable = new JButton("손님 테이블");
        pnlControl.add(butCusTable);
        butCusTable.setActionCommand(CUS_TABLE);                
        
        pnlSubOperation = new JPanel();
        pnlOperation.add(pnlSubOperation);
        butExit = new JButton("장사 종료");
        butExit.setActionCommand(SALE_CLOSE);
        
        ActionListener paneAction = new PanelHandler();
        butExit.addActionListener(paneAction);
        pnlSubOperation.add(butExit);        
        butMainControl.addActionListener(paneAction);
        butItemControl.addActionListener(paneAction);
        butCusTable.addActionListener(paneAction);
        
        
        //Add food item maintenance panel
        pnlItemControl = new JPanel(new GridLayout(2,1));
        pnlItemControl.setSize(800, 500);
        pnlItemControl.setBorder(new TitledBorder("메뉴 관리"));
        pnlItemControl.setLayout(new FlowLayout());
        //New item entry
        lblName = new JLabel("메뉴명");
        txtName = new JTextField(10);
        pnlItem1 = new JPanel();
        lblPrice = new JLabel("가격");
        txtPrice = new JTextField(10);
        pnlItem2 = new JPanel();
        lblCat = new JLabel("분류");
        txtCat = new JTextField(10);
        pnlItem3 = new JPanel();
        butSaveItem = new JButton("저장");
        butSaveItem.addActionListener(new ItemHandler());                        
        pnlItem1.add(lblName); pnlItem1.add(txtName);
        pnlItem2.add(lblPrice); pnlItem2.add(txtPrice);
        pnlItem3.add(lblCat); pnlItem3.add(txtCat);
        pnlItemControl.add(pnlItem1);
        pnlItemControl.add(pnlItem2);
        pnlItemControl.add(pnlItem3);
        pnlItemControl.add(butSaveItem);        
        //pnlItemControl.add(pnlItemButtons);        
                        
        layeredPane.add(pnlItemControl);
        pnlItemControl.setVisible(false);                
        
        //Add customer table panel
        pnlCusTable = new JPanel(new GridLayout(2,1));
        pnlCusTable.setSize(900, 700);
        pnlCusTable.setBorder(new TitledBorder("손님 테이블"));
        layeredPane.add(pnlCusTable);
        pnlCusTable.setVisible(false);
        
        pnlCusShow = new JPanel();
        pnlCusTable.add(pnlCusShow);        
        pnlPayment = new JPanel(new GridLayout(1,2));
        pnlCusTable.add(pnlPayment);
        
        pnlPayCash = new JPanel(new GridLayout(2,1));
        pnlPayment.add(pnlPayCash);
        pnlPayCash1 = new JPanel(new GridLayout(3,3));
        JButton[] cashBtns = new JButton[9];
        for(int i=0; i<9; i++){
            String bn = Integer.toString(i+1);
            cashBtns[i] = new JButton(bn);
            pnlPayCash1.add(cashBtns[i]);
        }
        pnlPayCash.add(pnlPayCash1);
        
        pnlPayCash2 = new JPanel();
        butHalfK = new JButton("500");
        butK = new JButton("1000");
        but5K = new JButton("5000");
        but10K = new JButton("10000");
        pnlPayCash2.add(butHalfK);
        pnlPayCash2.add(butK);
        pnlPayCash2.add(but5K);
        pnlPayCash2.add(but10K);
        pnlPayCash.add(pnlPayCash2);
        
        pnlPayDetail = new JPanel(new GridLayout(3,1));        
        pnlPayment.add(pnlPayDetail);
        pnlSubDetail1 = new JPanel(new GridLayout(0,3));
        pnlPayDetail.add(pnlSubDetail1);
        pnlSubDetail2 = new JPanel();
        pnlPayDetail.add(pnlSubDetail2);
        pnlSubDetail3 = new JPanel(new GridLayout(1,2));
        pnlPayDetail.add(pnlSubDetail3);
              
        //Add new table panel
        pnlMainControl = new JPanel(new GridLayout(2,1));
        pnlMainControl.setSize(900, 700);
        pnlMainControl.setBorder(new TitledBorder(new EtchedBorder(), "테이블 추가"));
        layeredPane.add(pnlMainControl);
        pnlMainControl.setVisible(false);        
        
        pnlCusInfo = new JPanel(new GridLayout(1,2));
        pnlNewOrder = new JPanel(new BorderLayout());
        pnlSubNewOrder = new JPanel(new GridLayout(2,2));
        
        lblTblNum = new JLabel("테이블 번호: ");
        lblSelNum = new JLabel();
        lblOrder = new JLabel("주문 음식: ");
        pnlSubNewOrder.add(lblTblNum);
        pnlSubNewOrder.add(lblSelNum);
        pnlSubNewOrder.add(lblOrder);
                
        scrPane = new JScrollPane(list);
        
        pnlSubNewOrder3 = new JPanel();
        butRemove = new JButton("Remove Item");
        pnlSubNewOrder3.add(butRemove);                
        
        butConfirmOrder = new JButton("Order");
        butCancelOrder = new JButton("Cancel");
        pnlSubNewOrder2 = new JPanel();
        pnlSubNewOrder2.add(butConfirmOrder);
        pnlSubNewOrder2.add(butCancelOrder);        
        pnlSubNewOrder4 = new JPanel();
        
        pnlNewOrder.add(pnlSubNewOrder,BorderLayout.NORTH);               
        pnlNewOrder.add(scrPane, BorderLayout.CENTER);
        pnlNewOrder.add(pnlSubNewOrder3, BorderLayout.EAST);
        pnlNewOrder.add(pnlSubNewOrder2, BorderLayout.SOUTH);
        pnlNewOrder.add(pnlSubNewOrder4, BorderLayout.WEST);
        
        pnlPicTable = new JPanel(new GridLayout(0,5));
        pnlPicTable.setBorder(new TitledBorder(new EtchedBorder(), "테이블 번호"));        
        pnlItemButtons = new JPanel(new GridLayout(0,5));
        int v = ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED;
        int h = ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER;
        jsp = new JScrollPane(pnlItemButtons, v, h);
        pnlCusInfo.add(pnlNewOrder);
        pnlCusInfo.add(pnlPicTable); 
        
        pnlMainControl.add(pnlCusInfo);        
        pnlMainControl.add(pnlItemButtons);
        
        ActionListener newOrder = new NewOrderHandler();
        butRemove.addActionListener(newOrder);
        butRemove.setActionCommand(REMOVE_ITEM);
        butConfirmOrder.addActionListener(newOrder);
        butConfirmOrder.setActionCommand(PROCEED_ORDER);
        butCancelOrder.addActionListener(newOrder);
        butCancelOrder.setActionCommand(CLEAR_SELECTED);
        
    }//end of createContent
        
}
