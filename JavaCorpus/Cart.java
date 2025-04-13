/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package finalproject;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;



/**
 *
 * @authors Eli, Griffen and Nicholas
 */
public class Cart {
    
    //initialize variables
    ArrayList <Item> items = new ArrayList();
    
    //constructor
    Cart(){};    
    
    
    // add item
    public void add (Item product) {
        items.add(product);
        FinalProject.updateCartGUI();
    }
    
    
    // remove item
    public void remove (int i) {
         items.remove(i);
         FinalProject.updateCartGUI();
    }
     
    
    //checkout (update item stock, add to db)
    public void checkout(){
        String timestamp = ((long)System.currentTimeMillis()/1000)+"";
        String itemlist = "";
        for(int i=0; i<this.items.size(); i++) {
            if (i == this.items.size()-1) {
                //no comma at end
                itemlist += "(" + this.items.get(i).id + ")";
            } else {
                itemlist += "(" + this.items.get(i).id + "),";
            }
        }
        String sql = "INSERT INTO carts (items, timestamp) VALUES('"+itemlist+"', "+timestamp+")";
        //System.out.println(sql);
        FinalProject.db.executeQuery(sql);
        
        //reduce the stock
        String[] sqls = new String[this.items.size()];
        for (int i=0; i<this.items.size(); i++) {
            sqls[i] = "UPDATE item SET stock=(stock-1) WHERE id="+this.items.get(i).id;
        }
        FinalProject.db.executeMultipleUpdates(sqls);
        
        FinalProject.loadItems();
    }    
    
    
    //total price before tax 
    public BigDecimal subTotal(){
        BigDecimal total = new BigDecimal(0);
        for(int i=0;i<items.size();i++){
            total = total.add(items.get(i).getPrice());
        }
        total.setScale(2, RoundingMode.HALF_EVEN);
        return total;
    }
    
    //total tax
    public BigDecimal tax(){
        BigDecimal t = this.subTotal().multiply(new BigDecimal(0.13));
        t.setScale(2, RoundingMode.HALF_EVEN);
        return t;
    }
    
    //total price including tax
    public BigDecimal total() {
        BigDecimal tot = this.subTotal().add(this.tax());
        tot.setScale(2, RoundingMode.HALF_EVEN);
        return tot;
    }
}
