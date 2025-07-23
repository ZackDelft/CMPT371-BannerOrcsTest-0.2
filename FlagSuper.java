import java.awt.Rectangle;
import java.awt.image.BufferedImage;


public class FlagSuper {
	public BufferedImage image;
	public String name;
	public boolean collision = false;
	public int x, y;
	public GamePanel gp;
	public Rectangle hitbox = new Rectangle(0,0,48,48);
	public int solidAreaDefaultX = hitbox.x;
	public int solidAreaDefaultY = hitbox.y;
}
