
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Rectangle;


public class ScoreZone {
	GamePanel gp;
	int zoneNum;
	public Rectangle hitbox;
	public int x, y;
	public String score;
	Font zoneFont = new Font("Ariel", Font.CENTER_BASELINE, 25);
	
	public ScoreZone(GamePanel gp, int zoneNum) {
		this.gp = gp;
		this.zoneNum = zoneNum;
		this.hitbox = new Rectangle(0, 0, gp.tileSize, gp.tileSize);
		this.score = "0";
		setPosition();
	}
	
	public void setPosition() {
		switch (zoneNum) {
		case 1:
			x = (gp.screenWidth / 2) - (gp.tileSize / 2);
			y = gp.tileSize / 2;
			break;
		case 2:
			x = (gp.screenWidth / 2) - (gp.tileSize / 2);
			y = gp.screenWidth - (gp.tileSize / 2) - gp.tileSize;
			break;
		case 3:
			x = gp.tileSize / 2;
			y = (gp.screenWidth / 2) - (gp.tileSize / 2);
			break;
		case 4:
			x = gp.screenWidth - (gp.tileSize / 2) - gp.tileSize;
			y = (gp.screenWidth / 2) - (gp.tileSize / 2);
			break;
		}
	}
	
	public void update() {
		score = String.valueOf(gp.players[zoneNum - 1].score);
	}
	
	public void draw(Graphics2D g2) {
		
		g2.setColor(Color.WHITE); 
		g2.setFont(zoneFont); 
		g2.drawString(score, x, y);
		 
		
		/*
		 * g2.setColor(Color.white); g2.fillRect(x, y, gp.tileSize, gp.tileSize);
		 */
	}
}