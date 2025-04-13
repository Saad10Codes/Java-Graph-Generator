package pkg360;

import java.util.Timer;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * @author Caleb Morris
 */
public class Transfer {
    private static Transfer instance = null;
    public Hint current;
    
    public JList horizontalContain;
    public JList verticalContain;
    public JTextField scoreContain;
    public JTextField timerContain;
    
    public JLabel[][] num;
    public JLabel[][] let;
    public JPanel[][] contain;
    
    public loginFrame tLF;
    public puzzleSelectorFrame psf;
    
    Timer timer;
    
    public JLabel scoreExtraContain;
    public JLabel lp2l;
    public JLabel lp1l;
    public JTextField textScorep1;
    
    public boolean isPaused;
    public boolean isSaved;
    public int turn;
    
    public JLabel lTurn;
    
    protected Transfer() {
      // Exists only to defeat instantiation.
    }
    public static Transfer getInstance() {
        if(instance == null) {
            instance = new Transfer();
        }
        return instance;
    }
}
