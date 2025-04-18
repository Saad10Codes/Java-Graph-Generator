import java.awt.event.*;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.util.*;
import javax.swing.JPanel;
import javax.swing.Timer;
import javax.swing.text.html.HTMLDocument.Iterator;

public class Board extends JPanel implements MouseListener, MouseMotionListener, MouseWheelListener{
	
	private int delay = 100;
	private int dragX;
	private int dragY;
	private boolean canDrag = false;
	private Building buildingSelection;
	private Obstacle obstacleSelection;
	private Path pathSelection;
	private LinkedList<Building> buildings = new LinkedList<Building>();
	private LinkedList<Path> paths = new LinkedList<Path>();
	private LinkedList<Visitor> people = new LinkedList<Visitor>();
	private LinkedList<Boolean> stagesB = new LinkedList<Boolean>();
	private LinkedList<Obstacle> obstacles = new LinkedList<Obstacle>();
	private Legenda legenda;
	private int destinationX;
	private int destinationY;
	private Interface iface;
	private Map bitmap = new Map();
	private Timer timer;
	private boolean paused = false;
	private boolean river;
	private boolean start;
	private ArrayList<Point> rivers = new ArrayList<Point>();
	private ArrayList<String> stages = new ArrayList<String>();
	private GregorianCalendar time = new GregorianCalendar();
	private ArrayList<Act> allActs = new ArrayList<Act>();
	
	//Constructor
	public Board(Interface iface)
	{
		this.iface = iface;
		stages = iface.getAllStages();
		for (int i = 0; i<stages.size(); i++)
		{
			ArrayList<Integer> tempInt = iface.getStage(i).getAllActs();
			for(int j : tempInt)
			{
				ArrayList<String> tempString = iface.getStage(i).getAct(j).getArtistNames();
				for(int k = 0; k < tempString.size(); k++)
				{
					for(int l = 0; iface.findArtist(tempString.get(k)).getRating() > l; l++)
						{
							allActs.add((iface.getStage(i).getAct(j)));
						}
				}
			}
		}	
		for(int i = 0;i<stages.size();i++)
		{
			stagesB.add(new Boolean(false));
		}
		initSimulator();
		setDoubleBuffered(true);
		addMouseListener(this);
		addMouseMotionListener(this);
		addMouseWheelListener(this);
		legenda = new Legenda();
		time.set(Calendar.MONTH, 1);
		this.timer = new Timer(delay, new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				time.add(Calendar.SECOND, 1);
				
				run();
			}
		});
		this.timer.start();
	}
	
	
	public void speedUp()
	{
		delay -= 20;
		if (delay <= 0)
		{
			delay = 1;
		}
		timer.setDelay(delay);
	}
	
	public void slowDown()
	{
		delay += 20;
		timer.setDelay(delay);
	}
	
	public void pause()
	{
		timer.stop();
		paused = true;
	}
	
	public void go()
	{
		timer.start();
		paused = false;
	}
	
	//Initialize method
	public void initSimulator()
	{
		addEHBO(100, 100);
		addSnackBar(200, 200);
		addWC(300, 300);
		
		Random r = new Random();
		for (String s : stages)
		{
			addStage(r.nextInt(24*30), r.nextInt(24*20));
		}
	}
	
	public void createPeople()
	{
		for(int i = 0; i <= 100; i+=4)
		{
			for(int t = 0; t <=100; t+=4)
			{
		    addVisitor(t, i);
			}
		}
	    for(Person person: people)
	    {
			Random random = new Random();
			if(random.nextInt()%2 == 0)
			{
				person.setAppearance(2);
			}
			else
				person.setAppearance(3);
	    }
	}
	
	
	// List adds
	public void addForest(int x, int y)
	{
		obstacles.add(new Forest(x, y));
	}
	
	public void addRoad(int x, int y)
	{
		paths.add(new Road(x, y));
	}
	
	public void addVisitor(int x, int y)
	{
		people.add(new Visitor(x, y));
	}
	
	public void addEHBO(int x, int y)
	{
		buildings.add(new EHBO(x, y));
	}
	
	public void addSnackBar(int x, int y)
	{
		buildings.add(new Snackbar(x, y));
	}
	
	public void addStage(int x, int y)
	{
		buildings.add(new StagePicture(x, y));
	}
	
	public void addWC(int x, int y)
	{
		buildings.add(new WC(x, y));
	}
	
	public void addBridge(int x, int y)
	{
		paths.add(new Bridge(x, y));
	}
	
	
	//List removes
	public void removeRoad(int x, int y)
	{
		paths.remove(new Road(x, y));
	}
	
	public void removeVisitor(int x, int y)
	{
		people.remove(new Visitor(x, y));
	}
	
	public void removeEHBO(int x, int y)
	{
		buildings.remove(new EHBO(x, y));
	}
	
	public void removeSnackBar(int x, int y)
	{
		buildings.remove(new Snackbar(x, y));
	}
	
	public void removeStage(int x, int y)
	{
		buildings.remove(new StagePicture(x, y));
	}
	
	
	//Paint method
	public void paint(Graphics g)
	{
		super.paint(g);
		Graphics2D g2 = (Graphics2D) g;
		checkSelection();
		g2.setColor(Color.green);
		g2.fillRect(0, 0, 720, 480);
		g2.setColor(Color.black);
		drawRaster(g2);
		drawRiver(g2);
		for(Path path: paths)
		{
			drawPath(path, g2);
		}
		for(Person person: people)
		{
			drawPerson(person, g2);
		}
		for(Building building: buildings)
		{
			drawBuilding(building, g2);
		}
		for(Obstacle obstacle: obstacles)
		{
			drawObstacle(obstacle, g2);
		}
		g2.scale(2, 2);
		g2.setColor(Color.RED);
		g2.drawString("Date: " + time.get(Calendar.YEAR) + "-" + (time.get(Calendar.MONTH)+1) + "-" + time.get(Calendar.DAY_OF_MONTH), 5, 15);
		g2.drawString("Time: " + time.get(Calendar.HOUR_OF_DAY) + ":" + time.get(Calendar.MINUTE) + ":" + time.get(Calendar.SECOND), 5, 25);
		if(river)
		{
			g2.setColor(Color.RED);
			g2.drawString("DRAW MODE ON", 260, 10);
		}	
		g2.dispose();
	}
	
	
	//Draw methods
	public void drawRaster(Graphics2D g2)
	{
		int x = 0;
	    int y = 0;
	    for(int i = 0; i < 20; i++)
	    {
	    	 y = i;
	    	 Shape s = new Rectangle(x * 24, y * 24, 24, 24);
	    	 g2.draw(s);
	     
	    for(int t = 0; t < 30; t++)
	    {
	    	 x = t;
	    	 Shape z = new Rectangle(x * 24, y * 24, 24, 24);
	    	 g2.draw(z);
	    }
	    }
	}
	
	public void drawRiver(Graphics2D g2)
	{
		if(!rivers.isEmpty())
		{
			g2.setColor(new Color(0, 125, 255));
			//Random r = new Random();
			//g2.setColor(new Color(r.nextInt(255), r.nextInt(255), r.nextInt(255)));
			for(Point p: rivers)
			{
			g2.fillRect((int)p.getX(), (int)p.getY(), 4, 4);
			}
		}
	}
	
	public void drawPath(Path path, Graphics2D g2)
	{
		g2.drawImage(path.getImageIcon().getImage(), path.getX(), path.getY(), this);
	}
	
	public void drawBuilding(Building building, Graphics2D g2)
	{
		g2.drawImage(building.getImageIcon().getImage(), building.getX(), building.getY(), this);
	}
	
	public void drawObstacle(Obstacle obstacle, Graphics2D g2)
	{
		g2.drawImage(obstacle.getImageIcon().getImage(), obstacle.getX(), obstacle.getY(), this);
	}
	
	public void drawPerson(Person person, Graphics2D g2)
	{
		if(person.getAppearance() == 2)
		{
			g2.setColor(Color.blue);
		}
		else
			g2.setColor(Color.pink);
		
		g2.fillRect(person.getX(), person.getY(), 4, 4);
		g2.setColor(null);
	}
	
	
	//Run method
	public void run() {			
		for(Visitor visitor: people)
		{
			movePerson(visitor);
		}
		if (time.get(Calendar.MINUTE) % 5 == 0)
		{
			changeDestinationPeople();
		}
		
		repaint();		
	}
	
	//Object checks
	public void checkSelection()
	{
		if(legenda.RightClicked())
		{
		if(legenda.getSelection() == "Selection: EHBO")
		{
			addEHBO(0, 0);
		}
		else if(legenda.getSelection() == "Selection: SnackBar")
		{
			addSnackBar(0, 0);
		}
		else if(legenda.getSelection() == "Selection: Stage")
		{
			addStage(0, 0);
		}
		else if(legenda.getSelection() == "Selection: River")
		{
			river = true;
		}
		else if(legenda.getSelection() == "Selection: Forest")
		{
			addForest(0, 0);
		}
		else if(legenda.getSelection() == "Selection: WC")
		{
			addWC(0, 0);
		}
		else if(legenda.getSelection() == "Selection: Bridge")
		{
			addBridge(720-24, 480-24);
		}
		else if(legenda.getSelection() == "Selection: Run" && !start)
		{
			createPeople();
			start = true;
		}
		}
	}
	
	public void checkDragBuilding(Building building, int x, int y)
	{
	       if (x >= building.getX() && x <= (building.getX() + building.getWidth())
	                && y >= building.getY() && y <= (building.getY() + building.getHeight())) {
	            canDrag = true;
	            dragX = x - building.getX();  
	            dragY = y - building.getY(); 
	            buildingSelection = building;
	            if(building instanceof StagePicture)
	            {
	    			int stageX = building.getX()/24*24;
					int stageY = building.getY()/24*24;
					for(int w = 0; w < building.getWidth()/4; w++)
					{
						for(int h = 0; h < building.getHeight()/4; h++)
						{
							bitmap.free(stageX+4*w, stageY+4*h);
						}
					}
	            }
	        }
	}
	
	public void checkDragPath(Path path, int x, int y)
	{
	       if (x >= path.getX() && x <= (path.getX() + path.getWidth())
	                && y >= path.getY() && y <= (path.getY() + path.getHeight())) {
	            canDrag = true;
	            dragX = x - path.getX();  
	            dragY = y - path.getY(); 
	            pathSelection = path;
	            reclaimRiver();
	       }
	}
	
	public void checkDragObstacle(Obstacle obstacle, int x, int y)
	{
	       if (x >= obstacle.getX() && x <= (obstacle.getX() + obstacle.getWidth())
	                && y >= obstacle.getY() && y <= (obstacle.getY() + obstacle.getHeight())) {
	            canDrag = true;
	            dragX = x - obstacle.getX();  
	            dragY = y - obstacle.getY(); 
	            obstacleSelection = obstacle;
				int obstacleX = obstacle.getX()/24*24;
				int obstacleY = obstacle.getY()/24*24;
				for(int w = 0; w < obstacle.getWidth()/4; w++)
				{
					for(int h = 0; h < obstacle.getHeight()/4; h++)
					{
						bitmap.free(obstacleX+4*w, obstacleY+4*h);
					}
				}
	        }
	}
	
	public boolean checkOccupationBuilding(Building building)
	{
		Rectangle2D r2 = new Rectangle2D.Double(building.getX(), building.getY(), building.getWidth(), building.getHeight());
		int i = 0;
		for(Visitor visitor: people)
		{
			Point p = new Point(visitor.getX(), visitor.getY());
			if(r2.contains(p))
			{
				i++;
			}
			if(i == 36)
			{
				break;
			}
		}
		if(i == 36)
		{
			return true;
		}
		else
			return false;
	}
	
	//Movement methods
	public void moveDragBuilding(Building building, int x, int y)
	{
		building.setX(x - dragX);
		building.setY(y - dragY);
	}
	
	public void moveDragObstacle(Obstacle obstacle, int x, int y)
	{
		obstacle.setX(x - dragX);
		obstacle.setY(y - dragY);
	}
	
	public void moveDragPath(Path path, int x, int y)
	{
		path.setX(x - dragX);
		path.setY(y - dragY);
	}
	
	public void movePerson(Visitor visitor)
	{
		Point p = searchNearestDestination(visitor.getDestination(), visitor);
		Point space = getAvailableSpace((int)(p.getX()), (int)(p.getY()));
		int x = (int) (space.getX()/4 * 4);
		int y = (int) (space.getY()/4 * 4);
		if(x == 0 && y == 0)
		{
			x = 360;
			y = 240;
		}
		visitor.setDestinationPoint(new Point(x,y));
		if(visitor.getTimesTried() == 5 && visitor.getStatus() !="WayPointMade")
		{
			Random r = new Random();
			int i = r.nextInt(4);
			if( i == 0)
			{
			x = visitor.getX() + 24;
			y = visitor.getY() + 24;
			}
			else if ( i == 1)
			{
				x = visitor.getX() - 24;
				y = visitor.getY() - 24;
			}
			else if ( i == 2)
			{
				x = visitor.getX() + 24;
				y = visitor.getY() - 24;
			}
			else if ( i == 3)
			{
				x = visitor.getX() - 24;
				y = visitor.getY() + 24;
			}
			visitor.setStatus("WayPointMade");
		}
		if (visitor.getStatus()!="DestinationReached")
		{
			if (x < visitor.getX() && y < visitor.getY() && bitmap.claim((visitor.getX())-4, (visitor.getY())-4))
			{
				bitmap.free(visitor.getX(), visitor.getY());
				visitor.act("LEFT", 4);
				visitor.act("UP", 4);
			}
			else if (x > visitor.getX() && y < visitor.getY() && bitmap.claim((visitor.getX())+4, (visitor.getY())-4))
			{
				bitmap.free(visitor.getX(), visitor.getY());
				visitor.act("RIGHT", 4);
				visitor.act("UP", 4);
			}
			else if (x < visitor.getX() && y > visitor.getY() && bitmap.claim((visitor.getX())-4, (visitor.getY())+4))
			{
				bitmap.free(visitor.getX(), visitor.getY());
				visitor.act("LEFT", 4);
				visitor.act("DOWN", 4);
			}
			else if (x > visitor.getX() && y > visitor.getY() && bitmap.claim((visitor.getX())+4, (visitor.getY())+4))
			{
				bitmap.free(visitor.getX(), visitor.getY());
				visitor.act("RIGHT", 4);
				visitor.act("DOWN", 4);
			}
			else if (x < visitor.getX() && bitmap.claim((visitor.getX())-4, visitor.getY()))
			{
				bitmap.free(visitor.getX(), visitor.getY());
				visitor.act("LEFT", 4);
			}
			else if (x > visitor.getX() && bitmap.claim(visitor.getX()+4, visitor.getY()))
			{
				bitmap.free(visitor.getX(), visitor.getY());
				visitor.act("RIGHT", 4);
			}
		
			else if (y < visitor.getY() && bitmap.claim(visitor.getX(), visitor.getY()-4))
			{
				bitmap.free(visitor.getX(), visitor.getY());
				visitor.act("UP", 4);
			}
			else if (y>visitor.getY() && bitmap.claim(visitor.getX(), visitor.getY()+4))
			{
				bitmap.free(visitor.getX(), visitor.getY());
				visitor.act("DOWN", 4);
			}
			else if(visitor.getX() == x && visitor.getY() == y)
			{
				visitor.setStatus("DestinationReached");
				visitor.resetTimesTried();
			}

			else
			{
				visitor.increaseTimesTried();
			}
		}
	}
	
	//Methods for behavior
	public void destinationChange(Visitor visitor)
	{
		Random random = new Random();
		int r = random.nextInt(1000);
		
		if(r<=100)
			visitor.setDestination("EHBO");
		if(r>100 && r <=200)
			visitor.setDestination("SnackBar");
		if(r>200 && r <=300)
			visitor.setDestination("WC");
		if(r>300 && r <=500)
			visitor.setDestination("Stage");
		
				
	}
	
	public void changeDestinationPeople()
	{
		for(int i = 0; i<stagesB.size(); i++)
		{
		System.out.println(stagesB.get(i)+ "\n");
			if(iface.getStage(i).actBusy(time, (int)time.getTimeInMillis()*60*60))
			{
				boolean b = stagesB.get(i);
				stagesB.remove(i);
				b = true;
				stagesB.add(i,b);
			}
			else if(iface.getStage(i).actPast((int)time.getTimeInMillis()*60*60))
			{
				
			}
		}
		for(Visitor visitor: people)
		{
			destinationChange(visitor);
			visitor.setStatus(null);
		}
	}
	
	public Point searchNearestDestination(String destination, Visitor visitor)
	{
		int x = 1000;
		int y = 1000;
		int difference;
		int difference2;
		int visitorXY = visitor.getX()+visitor.getY();
		for(Building building: buildings)
		{
			if(!checkOccupationBuilding(building))
			{
			int buildingXY = building.getX()+building.getY();
			//Calculate differences
			if(visitorXY > buildingXY)
			{
				difference = visitorXY - buildingXY;
			}
			else
			{
				difference = buildingXY - visitorXY;
			}
			if(visitorXY > x+y)
			{
				difference2 = visitorXY - x+y;
			}
			else
			{
				difference2 = x+y - visitorXY;
			}
			//Destination chooser
			if(destination == "EHBO" && building instanceof EHBO)
			{
				if(difference < difference2)
				{
					x = building.getX();
					y = building.getY();
				}
			}
			if(destination == "SnackBar" && building instanceof Snackbar)
			{
				if(difference < difference2)
				{
					x = building.getX();
					y = building.getY();
				}
			}
			if(destination == "Stage" && building instanceof StagePicture)
			{
				if(difference < difference2)
				{
					x = building.getX();
					y = building.getY()+48;
				}
			}
			if(destination == "WC" && building instanceof WC)
			{
				if(difference < difference2)
				{
					x = building.getX();
					y = building.getY();
				}
			}
			}
		}
		return new Point(x,y);
	}
	
	public Point getAvailableSpace(int x, int y)
	{
		Point space = new Point();
		boolean breaker = false;
		for(int i = x; i <= x+24; i+=4)
		{
			for(int t = y; t <= y+24; t+=4)
			{
				if(bitmap.check(i, t))
				{
					space.setLocation(i, t);
					breaker = true;
					break;
				}
			}
			if(breaker)
			{
				break;
			}
		}
		return space;
	}
	
	//Reclaim
	public void reclaimRiver()
	{
		for(Point p: rivers)
		{
			bitmap.claim((int)p.getX(), (int)p.getY());
		}
	}

	
	//mouseDrag method
	public void mouseDragged(MouseEvent e) {
	
		if(river)
		{
			for(int i = 0; i < 3; i++)
			{
				for(int z = 0; z < 3; z++)
				{
					if(bitmap.claim(e.getX()/4*4+i*4, e.getY()/4*4+z*4))
					{
						rivers.add(new Point(e.getX()/4*4+i*4, e.getY()/4*4+z*4));
					}
				}
			}
		}
		else if (canDrag) {
		String selection = "";
		for (Building building : buildings)
		{
			if (buildingSelection == building)
			{
				moveDragBuilding(building, e.getX(), e.getY());
				selection = "Building";
				break;
			}
		}
		if(selection == "")
		{
			for (Obstacle obstacle : obstacles)
			{
				if (obstacleSelection == obstacle)
				{
					moveDragObstacle(obstacle, e.getX(), e.getY());
					selection = "Obstacle";
					break;
				}
			}
		}
		if(selection == "")
		{
			for (Path path : paths)
			{
				if (pathSelection == path)
				{
					moveDragPath(path, e.getX(), e.getY());
					break;
				}
			}
		}
		}
		}
	
	
	//mousePress method
	public void mousePressed(MouseEvent e) {
	       for(Building building: buildings)
	       {
			checkDragBuilding(building, e.getX(), e.getY());
	       }
	       for(Obstacle obstacle: obstacles)
	       {
			checkDragObstacle(obstacle, e.getX(), e.getY());
	       }
	       for(Path path: paths)
	       {
			checkDragPath(path, e.getX(), e.getY());
	       }
		}
	
	
	//mouseRelease method
	public void mouseReleased(MouseEvent e) {
		canDrag = false;
		for(Building building: buildings)
		{
			building.setX(building.getX()/24*24);
			building.setY(building.getY()/24*24);
			buildingSelection = null;
			if(building instanceof StagePicture)
			{
				int stageX = building.getX()/24*24;
				int stageY = building.getY()/24*24;
				for(int w = 0; w < building.getWidth()/4; w++)
				{
					for(int h = 0; h < building.getHeight()/4; h++)
					{
						bitmap.claim(stageX+4*w, stageY+4*h);
					}
				}
			}
		}
		for(Obstacle obstacle: obstacles)
		{
			int obstacleX = obstacle.getX()/24*24;
			int obstacleY = obstacle.getY()/24*24;
			obstacle.setX(obstacleX);
			obstacle.setY(obstacleY);
			for(int w = 0; w < obstacle.getWidth()/4; w++)
			{
				for(int h = 0; h < obstacle.getHeight()/4; h++)
				{
					bitmap.claim(obstacleX+4*w, obstacleY+4*h);
				}
			}
			obstacleSelection = null;
		}
		for(Path path: paths)
		{
			int pathX = path.getX()/24*24;
			int pathY = path.getY()/24*24;
			path.setX(pathX);
			path.setY(pathY);
			for(int w = 0; w < path.getWidth()/4; w++)
			{
				for(int h = 0; h < path.getHeight()/4; h++)
				{
					bitmap.free(pathX+4*w, pathY+4*h);
				}
			}
			pathSelection = null;
		}
		repaint();
	}

	
	//mouseClick method
	public void mouseClicked(MouseEvent e) 
	{
		if(e.getButton() == MouseEvent.BUTTON2)
		{
			if(!paused)
			{
				pause();
			}
			else {
				go();
			}
		}
		if(e.getButton() == MouseEvent.BUTTON3)
		{
			for(Building building: buildings)
			{
				if(building.getX() == e.getX()/24*24 && building.getY() == e.getY()/24*24)
				{
					destinationX = building.getX();
					destinationY = building.getY();
					break;
				}
			}
			river = false;
			legenda.resetSelection();
		}
		if(e.getButton() == MouseEvent.BUTTON1)	
		{
			changeDestinationPeople();
		}
	}
	
	 //Not used
	public void mouseMoved(MouseEvent e) {}
	public void mouseEntered(MouseEvent e) {}
	public void mouseExited(MouseEvent e) {}


	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		if(e.getWheelRotation() < 0)
		{
			if(!paused)
			slowDown();
		}
		else if(e.getWheelRotation() > 0)
		{
			if(!paused)
			speedUp();
		}
	}
}
