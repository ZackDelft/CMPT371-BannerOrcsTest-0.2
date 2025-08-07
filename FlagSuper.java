// CMPT 371 - Group 25 - Banner Orcs - FlagSuper.java

import java.awt.Rectangle;
import java.awt.image.BufferedImage;

// Parent class of the flag
// - If implementing more than on banner to the game, initialize array here
public class FlagSuper {
	public BufferedImage image;
	public boolean collision = false;
	public int x, y;
	public GamePanel gp;
	public Rectangle hitbox = new Rectangle(0,0,48,48);
	public int solidAreaDefaultX = hitbox.x;
	public int solidAreaDefaultY = hitbox.y;
}

// ZMMD