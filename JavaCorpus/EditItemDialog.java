/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package finalproject;

import ca.jacobparry.*;
import java.math.BigDecimal;
import javax.swing.JTextArea;
import javax.swing.JViewport;

/**
 *
 * @author Jacob
 */
public class EditItemDialog extends ModularDialog {
    private Item i;
    
    EditItemDialog(Item it) {
        super(null, "Edit Item", ModularDialog.OK_CANCEL, 300, new DialogItem[] {
            new StringItem("Name"),
            new StringItem("Price"),
            new StringItem("Wholesale Cost"),
            new SpinnerItem("Stock", 1, 0, Double.POSITIVE_INFINITY, 1),
            new TextAreaItem("Description")
        });
        
        this.i = it;
        
        StringItem name = (StringItem) this.components[0];
        StringItem price = (StringItem) this.components[1];
        StringItem cost = (StringItem) this.components[2];
        SpinnerItem stock = (SpinnerItem) this.components[3];
        TextAreaItem desc = (TextAreaItem) this.components[4];
        
        name.getComponent().setText(i.name);
        price.getComponent().setText(i.price.toString());
        cost.getComponent().setText(i.cost.toString());
        stock.getComponent().setValue(i.stock);
        ((JTextArea)((JViewport)desc.getComponent().getComponent(0)).getComponent(0)).setText(i.desc);
        
        name.getComponent().setEnabled(false);
    }
    
    @Override
    public int inputVerifier(DialogItem[] dis) {
        
        StringItem price = (StringItem) dis[1];
        StringItem cost = (StringItem) dis[2];
        SpinnerItem stock = (SpinnerItem) dis[3];
        TextAreaItem desc = (TextAreaItem) dis[4];
        
        //get the price and cost
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
        
        double s = stock.getValue();
        String d = desc.getText();
        
        this.i.setPrice(p);
        this.i.setCost(c);
        this.i.setStock((int)s);
        this.i.setDesc(d);
        
        FinalProject.loadItem(this.i.id);
        
        return 1;
        
    }
    
}
