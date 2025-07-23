
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

public class Entity {
	public int ID;
	public int x, y;
	public int speed;
	public int score;
	
	// for image
	public BufferedImage characterImage;
	public String direction;
	// for hit-box
	public Rectangle hitbox;
	public int solidAreaDefaultX, solidAreaDefaultY;
	public boolean collisionOn = false;
	public boolean hasFlag = false;
	public boolean throwing = false;
	public long nextThrowTime;
	public long stopThrowingAt;
	public boolean isThrown = false;
	public long noLongerThrownAt;
	int thrownX, thrownY;
}
