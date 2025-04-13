/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package finalproject;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import javax.swing.DefaultListModel;
import javax.swing.UIManager;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author jacob
 */
public class FinalProject {

    public static final String[] names = {"The Credible Hulk", "Eli", "Griffen", "Nicky", "Alexander The Great", "Laur Laur"};
    public static Database db = null;
    public static Item[] items = null;
    public static MainFrame mf = null;
    
    public static int lastLoadedId = -1;
    public static Item lastLoadedItem = null;
    
    public static Cart cart = new Cart();
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        System.setProperty("java.library.path", "./");
        run(); //run the program
    }
    
    public static void run() {
        //make it look like the OS rather than the default Java
        try { 
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch(Exception e) { System.out.println(e); }
        
        //load the db
        db = new Database("sqlite:database.db3", "", "");
        
        //load the main frame
        mf  = new MainFrame();
        
        loadItems(); //load the items and put them in the gui
        
        //display the frame
        mf.setVisible(true);
    }
    
    public static void loadItems() {
        //load the items from the db
        items = db.getItems();
        
        //add all the items to the inventory list
        javax.swing.DefaultListModel mdl = new javax.swing.DefaultListModel<>();
        javax.swing.DefaultComboBoxModel cmdl = new javax.swing.DefaultComboBoxModel<>();
        for (Item i : items) {
            mdl.addElement(i);
            cmdl.addElement(i);
        }
        mf.invDisplay.setModel(mdl);
        mf.jcbPOSItems.setModel(cmdl);
        
        //clear the form
        mf.lblInvName.setText("Product Name");
        mf.lblInvPrice.setText("Sale Price: $0.00");
        mf.lblInvCost.setText("Wholesale Cost: $0.00");
        mf.lblInvStock.setText("0 in stock");
        
        mf.lblInvDesc.setText("<html><i>No description</i></html>");
        
        mf.jliInvLog.setModel(new DefaultListModel()); //clear log
        
        clearPOSInfo();
        
        //disable buttons
        mf.btnInvEdit.setEnabled(false);
        mf.btnInvDelete.setEnabled(false);
        mf.btnInvRestock.setEnabled(false);
        
        //reset id
        lastLoadedId = -1;
        lastLoadedItem = null;
    }
    public static void clearPOSInfo() {
        mf.lblPOSName.setText("Product Name");
        mf.lblPOSPrice.setText("Sale Price: $0.00");
        mf.lblPOSDesc.setText("<html><i>No description</i></html>");
    }
    
    public static void loadItem(int id) {
        //load the one item
        Item i = null;
        for(Item it : items) {
            if (it.id == id) {
                i = it;
            }
        }
        
        //load the info info the labels
        mf.lblInvName.setText(i.name);
        mf.lblInvPrice.setText("Sale Price: $"+i.price.toString());
        mf.lblInvCost.setText("Wholesale Cost: $"+i.cost.toString());
        mf.lblInvStock.setText(i.stock+" in stock");
        
        if (i.desc != null) mf.lblInvDesc.setText("<html>"+i.desc+"</html>");
        else mf.lblInvDesc.setText("<html><i>No description</i></html>");
        
        //enable buttons
        mf.btnInvEdit.setEnabled(true);
        mf.btnInvDelete.setEnabled(true);
        mf.btnInvRestock.setEnabled(true);
        
        //populate the list
        ArrayList<LogItem> logItems = new ArrayList<>();
        
        //get instances of restocking
        String sql = "SELECT * FROM restock WHERE itemid="+i.id;
        String[][] s = db.returnRows(sql);
        for (int j=0; j<s.length; j++) {
            //add all the restocking instances to the list
            long time = Long.parseLong(s[j][3]); //get the timestamp
            logItems.add(new LogItem("Added " + s[j][2] + " items", time)); //add it
        }
        
        //get all purchases
        sql = "SELECT * FROM carts WHERE items LIKE '%("+i.id+")%'";
        s = db.returnRows(sql);
        for (int j=0; j<s.length; j++) {
            //add the 'sold' instances
            long time = Long.parseLong(s[j][2]); //get the timestamp
            int numunits = StringUtils.countMatches(s[j][1], "("+i.id+")"); //count qty sold
            logItems.add(new LogItem(numunits+" unit(s) sold", time)); //add it
        }
        
        //sort the list
        Collections.sort(logItems);
        
        //create a list model to populate the JList
        DefaultListModel mdl = new DefaultListModel();
        for (int j=0; j<logItems.size(); j++) {
            mdl.addElement(logItems.get(j)); //add the log items to the model
        }
        mf.jliInvLog.setModel(mdl); //set the model
        
        //store the ID
        lastLoadedId = i.id;
        lastLoadedItem = i;
    }
    
    public static void updateCartGUI() {
        //build the list of cart items
        DefaultListModel mdl = new DefaultListModel();
        for (int i=0; i<cart.items.size(); i++) {
            mdl.addElement(cart.items.get(i)); //add the items
        }
        mf.checkoutList.setModel(mdl);
        
        //get the totals at the bottom
        BigDecimal st = cart.subTotal();
        BigDecimal tax = cart.tax();
        BigDecimal tot = cart.total();
        
        //format as currency
        DecimalFormat twoD = new DecimalFormat("#.##");
        
        //set the labels for the totals
        mf.lblPOSSubTotal.setText("<html><b>Sub Total</b> $" + twoD.format(st) + "</html>");
        mf.lblPOSTax.setText("<html><b>Tax</b> $" + twoD.format(tax) + "</html>");
        mf.lblPOSTotal.setText("<html><b>Total</b> $" + twoD.format(tot) + "</html>");
    }
}
