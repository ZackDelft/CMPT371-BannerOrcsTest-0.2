// CMPT 371 - Group 25 - Banner Orcs - GamePanel.java

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.net.InetAddress;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

// Class that runs the client side of the game and displays information for user
public class GamePanel extends JPanel implements Runnable{
	// Screen settings
	final int originalTileSize = 16; // 16x16 tile
	final int scale = 3; // tile scale factor
	public final int tileSize = originalTileSize * scale; // final tile size (48x48)
	public final int maxScreenCol = 16; // max tiles horizontally 
	final int maxScreenRow = 16; // max tiles vertically
	public final int screenWidth = tileSize * maxScreenCol; // width in pixels
	final int screenHeight = tileSize * maxScreenRow; // height in pixels
	
	public boolean ready = false; // player ready status
	public boolean prevReady = false; // for comparison used to see if server message is needed
	public boolean started = false; // game started state
	public boolean finished = false; // game finished state
	public boolean playersSorted = false; // used to sort players by score once (render will call everytime otherwise)

	public boolean running; // Game running status - used for quit states
	
	// FPS
	int FPS = 60;

	// Game variables
	// instantiate tilemanager
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

	public Font font = new Font("Ariel", Font.CENTER_BASELINE, 20); // for displaying some text on screen
	
	// client server initialization
	public Client client;
	private Server server;
	public int playerControl = 0; // server tells client which player to control through this variable
	int port = -1;
	String serverIP = "localhost";
	int connectedPlayers = 0;
	int readyPlayers = 0;
	// Connection timeout variables for client
	boolean connected2server = false;
	boolean timedout = false;
	long connectionMessageSentTime; // Used for initial connection timeout
	long connectionTimeOut; // used for client timeouts
	long oneSec = 1000000000; // nanosec in 1 sec

		
	public GamePanel () {
		// Initialize display settings and add keyhandler(all black)
		this.setPreferredSize(new Dimension(screenWidth, screenHeight));
		this.setBackground(Color.black);
		this.setDoubleBuffered(true);
		this.addKeyListener(keyH);
		this.setFocusable(true);

		InetAddress dummyAddress = null; // dummy address used in the creation of players for clients (value needed only by server)
		
		// Initialize game classes
		startB = new StartButton(this, keyH);
		for (int i = 0; i < this.players.length; i++) {
			this.players[i] = new Player(this, keyH, i+1, dummyAddress, -1);
			this.zones[i] = new ScoreZone(this, i+1);
		}
		scoreB = new ScoreBoard(this);
	}

	public synchronized void startGameThread() {
		
		// Ask if the player wants to host a game with a server
		// - if yes, get a port number to run the game on
		//   - checks for valid port number
		//   - Starts the server after initializing with given IP
		//     - see Server.java for more information
		// - else get the IP and port the client wants to connect to
		//   - initializes client thread and starts it
		//   - see Client.java for more information
		if (JOptionPane.showConfirmDialog(this, "Do you want to run the server on this machine") == 0) {
			goodPort();
			server = new Server(this, port);
			server.start();
		}
		if (port == -1) {
			serverIP = JOptionPane.showInputDialog("Please enter the IP address of the game server");
			goodPort();
			
		}
		client = new Client(this, serverIP, port); // Need better way of handling IP
		client.start();

		// Start running the game
		running = true; 
		gameThread = new Thread(this);
		gameThread.start();
	}

	// Function to see if entered port is acceptable
	// - will keep asking for acceptable port until one is received
	public void goodPort() {
		boolean goodPort = false;
		while (goodPort == false) {
			try {
				port = Integer.parseInt(JOptionPane.showInputDialog("Please enter the port number to connect to on the server\n(Accepteble range = 49152 - 65535)").trim());
				if (port >= 49152 && port <= 65535) {
					goodPort = true;
				}
			} catch (Exception e) {
			}
		}
	}

	// Used to stop the program 
	public synchronized void stop() {
		running = false;
	}
	
	// Main client game loop
	// - Delta method for FPS
	@Override
	public void run() {
		double drawInterval = 1000000000/FPS;
		double delta = 0;
		long lastTime = System.nanoTime();
		long currentTime;
		long timer = 0;
		int drawCount = 0;

		client.sendConnectPacket();
		connectionMessageSentTime = System.nanoTime();
		connectionTimeOut = connectionMessageSentTime + (10 * oneSec);
		
		while(running == true) {
			currentTime = System.nanoTime();
			delta += (currentTime - lastTime) / drawInterval;
			timer += (currentTime - lastTime);
			lastTime = currentTime;
			
			if (delta >= 1) {
				// Check for connection timeout
				if (System.nanoTime() > connectionTimeOut && finished == false) {
					connected2server = false;
				}

				// Inital connection timeout
				if (connected2server == false && System.nanoTime() > connectionTimeOut) {
					timedout = true;
				}
				update();
				repaint();
				delta--;
				drawCount++;
			}
			
			if (timer >= 1000000000) {
				//System.out.println("FPS: " + drawCount); // prints fps count
				drawCount = 0;
				timer = 0;
			}
		}

		// Not running close program
		System.exit(0);
	}
	
	// Function used to update all game elements 
	public void update() {
		// Game hasn't started yet
		if (started == false && finished == false && timedout == false) {
			startB.update(client);

		}
		// Game started - playing
		else if (started == true && finished == false && timedout == false) {
			for (int i = 0; i < players.length; i++) {
				players[i].update(client);
				zones[i].update();
			}
		}
		// Game ended
		else if (started == true && finished == true && timedout == false) {
			scoreB.update();
		}
		// Timeout from server
		else if (timedout == true) {
			if (keyH.enterPressed == true) {
				running = false;
			}
		}
	}
	
	// Function used to display game updates
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		Graphics2D g2 = (Graphics2D)g;
		// Background layer
		tileM.draw(g2);
		
		// Not started
		if (started == false && finished == false && timedout == false) {
			startB.draw(g2);
		}
		// Game stared - playing
		else if (started == true && finished == false && timedout == false) {
		
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
		// Game finished
		else if (started == true && finished == true && timedout == false) {
			scoreB.draw(g2);
		}
		// Timeout from server
		else if (timedout == true) {
			g2.setColor(Color.WHITE);
			g2.setFont(font);
			g2.drawString("Lost connection to server", (((maxScreenCol / 2) - 2) * tileSize) - (tileSize/2), ((maxScreenCol / 2) * tileSize));
			g2.drawString("Press 'enter' to quit", (((maxScreenCol / 2) - 2) * tileSize) - (tileSize/2), ((maxScreenCol / 2) * tileSize) + 25);
		}
		
		g2.dispose();
	}
}

// ZMMD