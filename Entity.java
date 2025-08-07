// CMPT 371 - Group 25 - Banner Orcs - Entity.java

import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.net.InetAddress;

// Parent class for players and other entities added at a later time
public class Entity {
	public int ID; // The player number associated with client
	public int x, y; // current x and y position from images top left corner
	public int speed; // players speed inn pixels per frame
	public int score; // players current score

	// Server variables
	public boolean ready = false; // for server communication
	public InetAddress ip; // used by server, dummy address in client 
	public int port; // used by server to send communications back, dummy port in client
	public long lastTimeUpdated = -1; // used by server to control when to send a keep alive message
	
	// for image
	public BufferedImage characterImage;
	public String direction;
	// for hit-box
	public Rectangle hitbox;
	public int solidAreaDefaultX, solidAreaDefaultY;
	public boolean collisionOn = false; // used by collisionChecker
	public boolean hasFlag = false;
	public boolean throwing = false;
	public long nextThrowTime; // next time player can throw someone
	public long stopThrowingAt; // time player stops throwing player
	public boolean isThrown = false;
	public long noLongerThrownAt; // time player is no longer moving due to being thrown
	int thrownX, thrownY;
}

// ZMMD