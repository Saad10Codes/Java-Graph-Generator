/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package finalproject;

import java.util.Calendar;

/**
 *
 * @author Jacob
 */
public class LogItem implements Comparable<LogItem> {
    public long timestamp;
    public String details;
    
    LogItem(String d, long t) {
        this.details = d;
        this.timestamp = t;
    }
    
    @Override
    public String toString() {
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(this.timestamp*1000); //we multiply, since we have it in seconds
        
        //zero-based month names
        String[] months = {"January", "February", "March", "April", "May", "June", 
            "July", "August", "September", "October", "November", "December"};
        
        //Build date string
        String dt = months[c.get(Calendar.MONTH)] + " " + c.get(Calendar.DAY_OF_MONTH) + 
                ", " + c.get(Calendar.YEAR) + " @ " + (c.get(Calendar.HOUR)+1) + 
                ":" + c.get(Calendar.MINUTE);
        
        //return the value for the JList
        return "[" + dt + "] " + this.details;
    }

    @Override
    public int compareTo(LogItem o) {
        return -((Long)this.timestamp).compareTo((Long)o.timestamp); //high to low
    }
}
