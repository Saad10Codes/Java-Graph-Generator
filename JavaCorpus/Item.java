
//imports
package finalproject;
import java.math.BigDecimal;
import javax.swing.ImageIcon;
/**
 *
 * @author Eli Zeeb, Jacob Parry
 */
public class Item {
    
    BigDecimal price;   //the sale price for the consumer of the item
    BigDecimal cost;    //the cost to the company of buying the item
    int id;             //the items id number
    int stock;          //how much of the item the company has
    String name;        //the name of the item
    String desc = "";   //a short description of the item
    ImageIcon pic;      //a small picture of the item
    QRCode qrCode;      //the qr code of the item
    
    //constructor for all variables known
    Item(String n, String d, BigDecimal p, BigDecimal c, int num, int s, ImageIcon pict){
        name = n;
        price = p;
        cost = c;
        stock = s;
        desc = d;
        pic = pict;
        id = num;
        this.qrCode = new QRCode(String.valueOf(this.id));
    }
    //constructor for no picture
    Item(String n, int num, String d, BigDecimal p, BigDecimal c, int s){
        name = n;
        price = p;
        cost = c;
        stock = s;
        desc = d;
        id = num;
        this.qrCode = new QRCode(String.valueOf(this.id));
    }
    //constructor for no desctiption
    Item(String n, int num, BigDecimal p, BigDecimal c, int s, ImageIcon pict){
        name = n;
        price = p;
        id = num;
        cost = c;
        stock = s;
        desc = "";
        pic = pict;
        this.qrCode = new QRCode(String.valueOf(this.id));
    }
    //constructor for no picture or description
    Item(String n, int num, BigDecimal p, BigDecimal c, int s){
        name = n;
        price = p;
        cost = c;
        stock = s;
        desc = "";
        id = num;
        this.qrCode = new QRCode(String.valueOf(this.id));
    }
    //constructor for id, name, desc, price, stock, cost; no image, no qr code
    Item(int num, String n, String d, BigDecimal p, int s, BigDecimal c){
        id = num;
        name = n;
        desc = d;
        price = p;
        stock = s;
        cost = c;
        this.qrCode = new QRCode(String.valueOf(this.id));
    }
    
    //constructor for creating a new item
    Item(String n, String d, BigDecimal p, int s, BigDecimal c) {
        id = -1;
        name = n;
        desc = d;
        price = p;
        stock = s;
        cost = c;
        this.qrCode = new QRCode(String.valueOf(this.id));
    }
    
    //toString
    @Override
    public String toString(){
        //return ("[Name: "+name+", ID: "+id+", Price: "+price+", Cost: "+cost+", Description: "+desc+", Stock: "+stock+"]");
        return "["+this.id+"] "+this.name;
    }
    
    //GET functions for id
    int getID(){
        return id;
    }
    
    //SET and GET description
    void setDesc(String d){
        String sql = "UPDATE item SET desc='"+Database.escape(d)+"' WHERE id="+this.id;
        if (FinalProject.db.executeQuery(sql, true) != -1) {
            desc = d;
        } else {
            System.out.println("DB Error: could not update desc");
        }
    }
    String getDesc(){
        return desc;
    }
    
    //GET and SET picture
    void setPic(ImageIcon p){
        pic = p;
    }
    ImageIcon getPic(){
        return pic;
    }
    
    //GET and SET ptice
    void setPrice(BigDecimal p){
        String sql = "UPDATE item SET price='"+Database.escape(p.toString())+"' WHERE id="+this.id;
        if (FinalProject.db.executeQuery(sql, true) != -1) {
            price = p;
        } else {
            System.out.println("DB Error: could not update price");
        }
    }
    BigDecimal getPrice(){
        return price;
    }
    
    //GET and SET cost
    void setCost(BigDecimal c){
        String sql = "UPDATE item SET cost='"+Database.escape(c.toString())+"' WHERE id="+this.id;
        if (FinalProject.db.executeQuery(sql, true) != -1) {
            cost = c;
        } else {
            System.out.println("DB Error: could not update cost");
        }
    }
    BigDecimal getCost(){
        return cost;
    }
    
    //GET qr code
    QRCode getQR(){
        return qrCode;
    }
    
    //GET and SET stock
    void setStock(int s){
        String sql = "UPDATE item SET stock='"+Database.escape(String.valueOf(s))+"' WHERE id="+this.id;
        if (FinalProject.db.executeQuery(sql, true) != -1) {
            stock = s;
        } else {
            System.out.println("DB Error: could not update stock");
        }
    }
    int getStock(){
        return stock;
    }
    
    //make the computer set the price for the item
    BigDecimal autoPrice(){
        BigDecimal base = new BigDecimal(1.33);
        BigDecimal x = new BigDecimal(10);
        BigDecimal s = new BigDecimal(stock);
        return cost.multiply(base.add(x.divide(s)));
    }
    
    //restock an item
    BigDecimal restock(int num){
        BigDecimal n = new BigDecimal (num);
        stock += num;
        return cost.multiply(n);
    }
    
    //compare two items
    boolean equals(Item x){
        if(x.getID() == id){
            return true;
        }
        else{
            return false;
        }
    }
}