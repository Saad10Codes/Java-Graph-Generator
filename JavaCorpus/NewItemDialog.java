/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package finalproject;

import ca.jacobparry.*;
import java.math.BigDecimal;
import javax.swing.JOptionPane;

/**
 *
 * @author Jacob
 */
public class NewItemDialog extends ModularDialog {
    
    NewItemDialog() {
        super(null, "New Item", new String[] {"Add", "Cancel"}, 300, new DialogItem[] {
            new StringItem("Name"),
            new StringItem("Price"),
            new StringItem("Wholesale Cost"),
            new SpinnerItem("Stock", 1, 0, Double.POSITIVE_INFINITY, 1),
            new TextAreaItem("Description")
        });
    }
    
    @Override
    public int inputVerifier(DialogItem[] dis) {
        try {
            StringItem nm = (StringItem)dis[0];
            StringItem price = (StringItem)dis[1];
            StringItem cost = (StringItem)dis[2];
            SpinnerItem stock = (SpinnerItem)dis[3];
            TextAreaItem desc = (TextAreaItem)dis[4];

            String n = nm.getText(); //get the name
            String d = desc.getText(); //and the description

            String pStr = price.getText();
            String cStr = cost.getText();

            pStr = pStr.replaceAll("[^0-9.]+", ""); //we only want numbers and periods
            cStr = cStr.replaceAll("[^0-9.]+", ""); //same here

            if (pStr.trim().equals("")) { //stop BigDecimal from complaining
                pStr = "0.00";
            }
            if (cStr.trim().equals("")) {
                cStr = "0.00";
            }

            BigDecimal p = new BigDecimal(pStr); //create the price and cost in a currency friendly format
            BigDecimal c = new BigDecimal(cStr);

            int s = (int)stock.getValue(); //get the stock

            if (!n.trim().equals("") && s >= 0) {
                Item i = new Item(n, d, p, s, c); //create the new item

                //actually add the item
                FinalProject.db.addItem(i);
                
                FinalProject.loadItems(); //we added it, now reload them
                
                return 1;
            } else {
                JOptionPane.showMessageDialog(null, "You must fill in a name!", "Error", JOptionPane.ERROR_MESSAGE);
                return -1;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }
    
}
