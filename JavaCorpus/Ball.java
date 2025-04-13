/*
 * Författare: Simon Metsi & Mathias Andreasen
 * Ball.java
 */
package lab1.uppg5;

import java.awt.Color;
import java.awt.Graphics;
import java.util.Random;

import javax.swing.JPanel;

public class Ball extends JPanel {
	
	private final int RADIUS = 20; // Bollens radie
	
	private Random rnd = new Random();
	private int ballX, ballY, ballSpeedX, ballSpeedY;
	
	public Ball () {
		
		ballX = (640 / 2);
		ballY = (480 / 2);
		
		ballSpeedX = (- rnd.nextInt(10) + 5);
		ballSpeedY = (- rnd.nextInt(10) + 5);
		
	}
	
	public void updateBall () {
		paintComponent(getGraphics());
	}
	
	public void setX (int n) { ballX = n; }
	public void setY (int n) { ballY = n; }
	public void setspeedX (int n) { ballSpeedX = n; }
	public void setspeedY (int n) { ballSpeedY = n; }
	
	public int getX () { return ballX; }
	public int getY () { return ballY; }
	public int getspeedX () { return ballSpeedX; }
	public int getspeedY () { return ballSpeedY; }
	public int getRadius () { return RADIUS; }
	
	public void paintComponents (Graphics g) {
		super.paintComponents(g);
		
		g.setColor(Color.green);
		g.fillOval( (ballX - RADIUS), (ballY - RADIUS), (2 * RADIUS), (2 * RADIUS));
	}
}
