
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

// import ScoreZone.ScoreZone;
// import entity.Player;
// import flag.Flag;
// import tile.TileManager;
// import ui.ScoreBoard;
// import ui.StartButton;

public class GamePanel extends JPanel implements Runnable{
	// Screen settings
	final int originalTileSize = 16; // 16x16 tile
	final int scale = 3; // tile scale factor
	public final int tileSize = originalTileSize * scale; // final tile size (48x48)
	public final int maxScreenCol = 16;
	final int maxScreenRow = 16;
	public final int screenWidth = tileSize * maxScreenCol;
	final int screenHeight = tileSize * maxScreenRow;
	
	public boolean ready = false;
	public boolean prevReady = false;
	public boolean started = false;
	public boolean finished = false;
	public boolean playersSorted = false;

	public boolean running = false;
	
	// FPS
	int FPS = 60;
	
	TileManager tileM = new TileManager(this);
	// instantiate keyhandler 
	KeyHandler keyH = new KeyHandler();
	// Create the game thread
	public Thread gameThread; // auto calls run()
	// collision handler
	public CollisionChecker cCheck = new CollisionChecker(this);
	// initiate score zones
	public ScoreZone zones[] = new ScoreZone[4];
	//initiate flag
	public Flag flag = new Flag(this);
	// instantiate player
	public Player players[] = new Player[4];
	// Start button
	StartButton startB;
	ScoreBoard scoreB;
	
	// client server initialization
	public Client client;
	private Server server;

	public int playerControl = 0;

		
	public GamePanel () {
		this.setPreferredSize(new Dimension(screenWidth, screenHeight));
		this.setBackground(Color.black);
		this.setDoubleBuffered(true);
		this.addKeyListener(keyH);
		this.setFocusable(true);

		InetAddress dummyAddress = null;
		// try {
		// 	dummyAddress = InetAddress.getByName("localhost");
		// } catch (UnknownHostException e) {
		// 	e.printStackTrace();
		// }
		
		startB = new StartButton(this, keyH);
		for (int i = 0; i < this.players.length; i++) {
			this.players[i] = new Player(this, keyH, i+1, dummyAddress, -1);
			this.zones[i] = new ScoreZone(this, i+1);
		}
		scoreB = new ScoreBoard(this);
	}

	public synchronized void startGameThread() {
		

		if (JOptionPane.showConfirmDialog(this, "Do you want to run the server") == 0) {
			server = new Server(this);
			server.start();
		}
		client = new Client(this, "localhost");
		client.start();

		running = true;
		gameThread = new Thread(this);
		gameThread.start();
	}

	public synchronized void stop() {
		running = false;
	}
	
	// Game loop goes here
	
	// Delta method for FPS
	@Override
	public void run() {
		double drawInterval = 1000000000/FPS;
		double delta = 0;
		long lastTime = System.nanoTime();
		long currentTime;
		long timer = 0;
		int drawCount = 0;

		client.sendConnectPacket();
		
		while(gameThread != null) {
			currentTime = System.nanoTime();
			delta += (currentTime - lastTime) / drawInterval;
			timer += (currentTime - lastTime);
			lastTime = currentTime;
			
			if (delta >= 1) {
				update();
				repaint();
				delta--;
				drawCount++;
			}
			
			if (timer >= 1000000000) {
				//System.out.println("FPS: " + drawCount);
				drawCount = 0;
				timer = 0;
				
				
			}
		}
	}
	
	public void update() {
		if (started == false && finished == false) {
			startB.update(client);

		}
		else if (started == true && finished == false) {
			for (int i = 0; i < players.length; i++) {
				players[i].update();
				zones[i].update();
			}
		}
		else if (started == true && finished == true) {
			
		}
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		Graphics2D g2 = (Graphics2D)g;
		// Background layer
		tileM.draw(g2);
		
		if (started == false && finished == false) {
			startB.draw(g2);
		}
		else if (started == true && finished == false) {
		
			for (int i = 0; i < zones.length; i++){
				zones[i].draw(g2);
			}
			// Flag layer
			flag.draw(g2);
			// Player layer
			for (int i = 0; i < players.length; i++) {
				players[i].draw(g2);
			}	
		}
		else if (started == true && finished == true) {
			scoreB.draw(g2);
		}
		
		g2.dispose();
	}
}