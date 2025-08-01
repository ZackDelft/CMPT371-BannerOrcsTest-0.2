import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.InetAddress;

import javax.imageio.ImageIO;

public class Player extends Entity{
	GamePanel gp;
	KeyHandler keyH;
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
	public void setDefaultValues(int ID) {
		this.ID = ID;
		direction = "";
		score = 0;
		nextThrowTime = System.nanoTime();
		stopThrowingAt = System.nanoTime();
		noLongerThrownAt = System.nanoTime();
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
 	public void update(Client client) {
		int prevX = x;
		int prevY = y;
 		if (isThrown == true) {
 			if (noLongerThrownAt <= System.nanoTime()) {
 				isThrown = false;
 			}
 		}
 		else if (throwing == true) {
 			if (stopThrowingAt <= System.nanoTime()) {
 				throwing = false;
 			}
 		}
 		else if (keyH.spacePressed == true && ID == gp.playerControl && isThrown == false && throwing == false) {
 			if (System.nanoTime() >= nextThrowTime) {
 				gp.cCheck.checkThrowRange(this, client);
 			}
 		}
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
 		
		// check collision
		collisionOn = false;
		if (isThrown == true) {
			if (thrownY > 0) {
				direction = "down";
			}
			else if (thrownX < 0){
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
		else {			
			if (gp.playerControl == this.ID) {
				gp.cCheck.flagChecker(this, client);
				gp.cCheck.checkZone(this, client);
			}
			gp.cCheck.checkTile(this);
			gp.cCheck.checkPlayers(this);
		}
		if (collisionOn == false) {
			
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
				if (thrownX > 0) {
					x += speed;
				}
				else if (thrownX < 0){
					x -= speed;
				}
				if (thrownY > 0) {
					y += speed;
				}
				else if (thrownY < 0){
					y -= speed;
				}
			}
		}
		if (prevX != x || prevY != y) {
			// Send update to server
			client.sendPositionUpdate(x, y);
		}	
	}
	public void draw(Graphics2D g2) {
		/*
		 * g2.setColor(Color.white); g2.fillRect(x, y, gp.tileSize, gp.tileSize);
		 */
		
		BufferedImage image = null;
		image = characterImage;
		g2.drawImage(image, x, y, gp.tileSize, gp.tileSize, null);
	}
	
	// to handle throwing players
	public void throwPlayer() {
		if (hasFlag) {
			gp.flag.possessed = 0;
			hasFlag = false;
			gp.client.sendFlagPossesion();
		}
		// send sendFlagPossesion()
		

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
	
	public int getScore() {
		return score;
	}
}
