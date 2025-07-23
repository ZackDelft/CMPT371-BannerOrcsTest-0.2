import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;


public class Flag extends FlagSuper{
	
	public int possessed = 0;
	public Rectangle hitbox = new Rectangle(0,0,48,48);
	
	public Flag(GamePanel gp) {
		this.gp = gp;
		name = "flag";
		try {
			image = ImageIO.read(getClass().getResourceAsStream("/res/flag/flag2.png"));
		} catch(IOException e) {
			e.printStackTrace();
		}
		setDefault();
	}
	
	public void setDefault() {
		x = 9 * gp.tileSize - (gp.tileSize/2);
		y = 9 * gp.tileSize - (gp.tileSize/2);
	}
	
	public void setHolder(Entity entity) {
		this.possessed = entity.ID;
		x = entity.x;
		y = entity.y - 33;
	}
	
	public void sendRandomSpot() {
		possessed = 0;
		x = (int) ((Math.random() * ((gp.maxScreenCol - 5) * gp.tileSize)) + 2*gp.tileSize); // Double check later
		y = (int) ((Math.random() * ((gp.maxScreenCol - 5) * gp.tileSize)) + 2*gp.tileSize);
		System.out.println("x: " + x + " y: " + y);
	}
	
	public void draw(Graphics2D g2) {
		BufferedImage bImage = null;
		bImage = image;
		g2.drawImage(bImage, x, y, gp.tileSize, gp.tileSize, null);
	}
}
