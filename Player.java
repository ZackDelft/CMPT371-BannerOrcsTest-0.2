// CMPT 371 - Group 25 - Banner Orcs - Player.java

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.InetAddress;
import javax.imageio.ImageIO;

// Main player class
// - inherits Entity class
public class Player extends Entity{

	// Needed game elements
	GamePanel gp;
	KeyHandler keyH;

	// Player constructor
	public Player(GamePanel gp, KeyHandler keyH, int ID, InetAddress ip, int port) {
		this.gp = gp;
		this.keyH = keyH;
		this.ip = ip;
		this.port = port;
		
		// hit-box
		hitbox = new Rectangle(6, 15, 36, 33);
		solidAreaDefaultX = hitbox.x;
		solidAreaDefaultY = hitbox.y;
		
		setDefaultValues(ID);
		getPlayerImage();
	}

	// Function to set default values for each player
	// - Players are set to start in their designated areas when the game starts
	public void setDefaultValues(int ID) {
		this.ID = ID;
		direction = "";
		score = 0;

		// for client server timeout
		lastTimeUpdated = System.nanoTime(); 

		nextThrowTime = System.nanoTime();
		stopThrowingAt = System.nanoTime();
		noLongerThrownAt = System.nanoTime();
		// Determine start location based off ID
		if (ID == 1) {
			x = (gp.screenWidth / 2) - (gp.tileSize / 2);
			y = (gp.tileSize / 2);
		}
		else if (ID == 2) {
			x = (gp.screenWidth / 2) - (gp.tileSize / 2);
			y = gp.screenWidth - (gp.tileSize) - (gp.tileSize / 2);
		}
		else if (ID == 3) {
			x = (gp.tileSize / 2);
			y = (gp.screenWidth / 2) - (gp.tileSize / 2);
		}
		else {
			x = gp.screenWidth - (gp.tileSize) - (gp.tileSize / 2);
			y = (gp.screenWidth / 2) - (gp.tileSize / 2);
		}
		
		speed = 4;
	}

	// Function to load player images
	// - image corrisponds to ID
	public void getPlayerImage() {
		try {
			if (ID == 1) {
				characterImage = ImageIO.read(getClass().getResourceAsStream("/res/player/blueOrc.png"));
			}
			else if (ID == 2) {
				characterImage = ImageIO.read(getClass().getResourceAsStream("/res/player/redOrc.png"));
			}
			else if (ID == 3) {
				characterImage = ImageIO.read(getClass().getResourceAsStream("/res/player/purpleOrc.png"));
			}
			else {
				characterImage = ImageIO.read(getClass().getResourceAsStream("/res/player/yellowOrc.png"));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// Main function used to update the player each frame
 	public void update(Client client) {

		// Previous x and y positions for comparison with values at end of update
		// - if different "02 playerID x y" is sent to server
		int prevX = x;
		int prevY = y;

		// Player being thrown
		// - sets player isThrown to false if enough time has expired
 		if (isThrown == true) {
 			if (noLongerThrownAt <= System.nanoTime()) {
 				isThrown = false;
 			}
 		}
		// player throwing player
		// - sets player throwing to false if enough time has expired
 		else if (throwing == true) {
 			if (stopThrowingAt <= System.nanoTime()) {
 				throwing = false;
 			}
 		}
		// Player hit the space key during gameplay
		// - checks to see if players are within range by calling checkThrowRang() within collisionChecker
 		else if (keyH.spacePressed == true && ID == gp.playerControl && isThrown == false && throwing == false) {
 			if (System.nanoTime() >= nextThrowTime) {
 				gp.cCheck.checkThrowRange(this, client);
 			}
 		}
		// Player movement key detection
 		else if (isThrown == false && throwing == false) {
			if (keyH.upPressed == true && ID == gp.playerControl) {
				direction = "up";
			}
			else if (keyH.downPressed == true && ID == gp.playerControl) {
				direction = "down";
			}
			else if (keyH.leftPressed == true && ID == gp.playerControl) {
				direction = "left";
			}
			else if (keyH.rightPressed == true && ID == gp.playerControl) {
				direction = "right";
			}
			else {
				direction = "";
			}
 		}
 		
		// reset collisionOn
		collisionOn = false;

		// Check for collisions if being thrown
		// - needs to check multiple directions
		if (isThrown == true) {
			if (thrownY > 0) {
				direction = "down";
			}
			else if (thrownY < 0){
				direction = "up";
			}
			else {
				direction = "";
			}
			gp.cCheck.checkTile(this);
			if (thrownX > 0) {
				direction = "right";
			}
			else if (thrownX < 0){
				direction = "left";
			}
			gp.cCheck.checkTile(this);
		}
		// If not thrown
		else {	
			// Check for collision with flag and if player has flag check if scored		
			if (gp.playerControl == this.ID) {
				gp.cCheck.flagChecker(this, client);
				gp.cCheck.checkZone(this, client);
			}
			// collosion with walls, arena edge and other players
			gp.cCheck.checkTile(this);
			gp.cCheck.checkPlayers(this);
		}
		// If player is not colliding with anything
		if (collisionOn == false) {
			// movement update through wasd input
			if (isThrown == false && throwing == false) {
				switch (direction) {
				case "up":
					y -= speed;
					break;
				case "down":
					y += speed;
					break;
				case "left":
					x -= speed;
					break;
				case "right":
					x += speed;
					break;
				}
				if (hasFlag) {
					gp.flag.setHolder(this);
				}
			}
			// being thrown
			else if (isThrown == true){

				x += speed * thrownX;
				y += speed * thrownY;
			}
		}
		// Check to see if update is needed to be sent to server
		if ((prevX != x || prevY != y) && gp.playerControl == ID) {
			// Send update to server
			client.sendPositionUpdate(x, y); // sends "02 playerID x y" message to server to be forwarded
			if (hasFlag == true) {
				client.sendFlagPossesion(); // sends "03 playerControl" message to server to be forwarded
			}
		}	
	}

	// Function used by GamePanel to draw players
	public void draw(Graphics2D g2) {
		BufferedImage image = null;
		image = characterImage;
		g2.drawImage(image, x, y, gp.tileSize, gp.tileSize, null);
	}
	
	// to handle throwing player
	// - Currently players only get thrown in vertical, horizontal and 45 degree angles
	// - possible for player not to move at all (thrown to ground)
	public void throwPlayer() {
		System.out.println("THROWN--------------------");
		if (hasFlag) {
			gp.flag.possessed = 0;
			hasFlag = false;
			gp.client.sendFlagPossesion(); // sends "03 0" flag possession message to server to be forwarded
		}

		isThrown = true;
		// throw in random direction
		int directionX = (int)(Math.random() * 3) - 1;
		//int randX = (int) Math.pow(-1, directionX);
		int directionY = (int)(Math.random() * 3) - 1;
		//int randY = (int) (Math.pow(-1, directionY));
		thrownX = directionX;
		thrownY = directionY;
		noLongerThrownAt = System.nanoTime() + 500000000L;
	}
	
	// Helper function to get score 
	public int getScore() {
		return score;
	}
}

// ZMMD