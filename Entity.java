
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.net.InetAddress;

public class Entity {
	public int ID;
	public int x, y;
	public int speed;
	public int score;

	// Server variables
	public boolean ready = false; // for server communication
	public InetAddress ip; // used by server 
	public int port; // used by server
	public long lastTimeUpdated = -1; // used by server to control client server connected message so they don't time out
	
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
