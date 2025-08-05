import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

public class StartButton {
	Client client;
	GamePanel gp;
	BufferedImage startImage1; // coloured
	BufferedImage startImage2; // grey scale image
	KeyHandler keyH;
	int x, y;
	
	
	public StartButton(GamePanel gp, KeyHandler keyH) {
		this.gp = gp;
		this.keyH = keyH;
		this.client = gp.client;
		this.x = ((gp.maxScreenCol / 2) - 1) * gp.tileSize;
		this.y = (((gp.maxScreenCol / 2)) * gp.tileSize) - (gp.tileSize/2);
		try {
			startImage1 = ImageIO.read(getClass().getResourceAsStream("/res/ui/startButton.png"));
			startImage2 = ImageIO.read(getClass().getResourceAsStream("/res/ui/startButton2.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void update(Client client) {
		if (keyH.enterPressed == true && gp.timedout == false) {
			if (gp.ready == false) {
				gp.ready = true;
			}
			else {
				gp.ready = false;
			}
			client.sendReadyPacket(gp.ready);
			keyH.enterPressed = false;
		}
	}
	
	public void draw(Graphics2D g2) {
		BufferedImage image = null;
		if (gp.ready == false) {
			image = startImage2;
		}
		else {
			image = startImage1;
		}
		g2.drawImage(image, x, y, gp.tileSize*2, gp.tileSize, null);
		g2.setColor(Color.WHITE);
		g2.setFont(gp.font);
		g2.drawString("Connected Players: " + gp.connectedPlayers, gp.tileSize/2, gp.tileSize/2);
		g2.drawString("Ready Players: " + gp.readyPlayers, gp.tileSize/2, gp.tileSize/2 + 25);
	}
}
