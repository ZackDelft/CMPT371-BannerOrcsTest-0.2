
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.net.InetAddress;

import javax.print.DocFlavor.STRING;

public class Entity {
	public int ID;
	public int x, y;
	public int speed;
	public int score;

	public boolean ready = false;
	public InetAddress ip;
	public int port;
	
	// for image
	public BufferedImage characterImage;
	public String direction;
	// for hit-box
	public Rectangle hitbox;
	public int solidAreaDefaultX, solidAreaDefaultY;
	public boolean collisionOn = false;
	public boolean hasFlag = false;
	public boolean throwing = false;
	public long nextThrowTime; // next time player can throw someone
	public long stopThrowingAt; // time player stops throwing player
	public boolean isThrown = false;
	public long noLongerThrownAt; // time player is no longer moving due to being thrown
	int thrownX, thrownY;
}
