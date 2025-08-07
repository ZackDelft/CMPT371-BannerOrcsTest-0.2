// CMPT 371 - Group 25 - Banner Orcs - Flag.java

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;

// Class controls all flag attributes within the game
// - inherits FlagSuper
public class Flag extends FlagSuper{
	GamePanel gp;
	public int possessed = 0; // holds the player id of the player holding flag or 0 if not held
	public Rectangle hitbox = new Rectangle(0,0,48,48);
	
	// Flag constructor
	public Flag(GamePanel gp) {
		this.gp = gp;
		try {
			image = ImageIO.read(getClass().getResourceAsStream("/res/flag/flag2.png"));
		} catch(IOException e) {
			e.printStackTrace();
		}
		setDefault();
	}
	
	// Helper function to set default values more easily in testing
	public void setDefault() {
		x = (gp.maxScreenCol/2) * gp.tileSize - (gp.tileSize/2);
		y = (gp.maxScreenCol/2) * gp.tileSize - (gp.tileSize/2);
	}
	
	// Sets the current flag holder
	public void setHolder(Entity entity) {
		this.possessed = entity.ID;
		x = entity.x;
		y = entity.y - 33;
	}
	
	// Sends the flag to a random spot
	// - used after a player scores
	public void sendRandomSpot() {
		possessed = 0;
		x = (int) ((Math.random() * ((gp.maxScreenCol - 5) * gp.tileSize)) + 2*gp.tileSize);
		y = (int) ((Math.random() * ((gp.maxScreenCol - 5) * gp.tileSize)) + 2*gp.tileSize);
		System.out.println("x: " + x + " y: " + y);
	}
	
	// Used by gamePanel to draw the flag
	public void draw(Graphics2D g2) {
		BufferedImage bImage = null;
		bImage = image;
		g2.drawImage(bImage, x, y, gp.tileSize, gp.tileSize, null);
	}
}

// ZMMD