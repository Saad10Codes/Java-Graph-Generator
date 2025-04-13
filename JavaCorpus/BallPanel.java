/*
 * Författare: Simon Metsi & Mathias Andreasen
 * BallPanel.java
 */
package lab1.uppg5;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.Timer;


public class BallPanel extends JPanel {
	
	private final int WIDTH = 640, HEIGHT = 480;
	private final int DELAY = 20;
	
	private JButton bollKnapp;
	
	private int ballSpeedX, ballSpeedY;
	private int ballX, ballY;
	
	private Random rnd = new Random();
	private Timer timer;
	
	private ArrayList<Ball> balls = new ArrayList<Ball>();
	
	
	// Ställer in panelen, inkluderar timern för animation
	public BallPanel () {
		
		timer = new Timer(DELAY, new BallListener());
		
		Ball boll = new Ball();
		balls.add(boll);
		
		bollKnapp = new JButton("Ny Boll");
		bollKnapp.addActionListener(new buttonListener());
		bollKnapp.setBounds(2, 2, 80, 20);
		
		setPreferredSize(new Dimension(WIDTH, HEIGHT));
		setBackground(Color.white);
		setLayout(null);
		add(bollKnapp);
		add(boll);
		
		timer.start();
	}
	
	private class BallListener implements ActionListener {
		
		// Uppdaterar positionen av bilden och riktning när bollen når kanten, vid varje timer-intervall
		public void actionPerformed (ActionEvent event) {
			
			for (Ball b : balls) {
				
				b.setX(b.getX() + b.getspeedX());
				b.setY(b.getY() + b.getspeedY());
				
				
				if (b.getX() - b.getRadius() < 0) {
					b.setspeedX(-b.getspeedX());
		            b.setX(b.getRadius());
		        } else if (b.getX() + b.getRadius() > WIDTH) {
		        	b.setspeedX(-b.getspeedX());
		            b.setX(WIDTH - b.getRadius());
		        }
				
				if (b.getY() - b.getRadius() < 0) {
					b.setspeedY(-b.getspeedY());
		            b.setY(b.getRadius());
	             } else if (b.getY() + b.getRadius() > (HEIGHT)) {
	            	 b.setspeedY(-b.getspeedY());
			         b.setY((HEIGHT) - b.getRadius());
	             }
				
				b.paintComponents(getGraphics());
				
			}
			
		}
	}
		
	private class buttonListener implements ActionListener {
		
		// Skapar en ny boll för varje tryckning
		public void actionPerformed (ActionEvent event) {
			Ball boll = new Ball();
			balls.add(boll);
		}
	}
}
