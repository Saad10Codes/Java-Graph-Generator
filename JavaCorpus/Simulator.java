import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

import javax.swing.Box.Filler;
import javax.swing.JFrame;

public class Simulator extends JFrame{
	
	public Simulator(Interface iface)
	{
		add(new Board(iface));
		setTitle("Simulatie");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
	    setSize(726, 505);
	    setLocationRelativeTo(null);
	    setBackground(Color.green);
	    setVisible(true);
	    setResizable(false);
	}
	
	public static void main(String[] args)
	{
		new Simulator(new Interface());
	}


	

}
