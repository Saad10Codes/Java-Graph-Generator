/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package finalproject;

import ca.jacobparry.DialogItem;
import ca.jacobparry.ModularDialog;
import ca.jacobparry.SpinnerItem;

/**
 *
 * @author Jacob
 */
public class RestockDialog extends ModularDialog {
    private int item = -1;
    RestockDialog(int itemid) {
        super(null, "Restock", ModularDialog.OK_CANCEL, 300, new DialogItem[] {
            new SpinnerItem("Quantity", 1, 1, Double.POSITIVE_INFINITY, 1)
        });
        this.item = itemid;
    }
    
    @Override
    public int inputVerifier(DialogItem[] dis) {
        //try it
        SpinnerItem spn = (SpinnerItem) dis[0];
        if (spn.getValue() > 0) {
            FinalProject.db.restockItem(this.item, (int)spn.getValue());
            FinalProject.loadItems();
            FinalProject.loadItem(this.item);
            return 1;
        } else {
            return 0;
        }
    }
    
}
