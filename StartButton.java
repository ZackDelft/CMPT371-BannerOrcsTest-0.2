import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

public class StartButton {
	Client client;
	GamePanel gp;
	BufferedImage startImage;
	KeyHandler keyH;
	int x, y;
	
	public StartButton(GamePanel gp, KeyHandler keyH) {
		this.gp = gp;
		this.keyH = keyH;
		this.client = gp.client;
		this.x = ((gp.maxScreenCol / 2) - 1) * gp.tileSize;
		this.y = (((gp.maxScreenCol / 2)) * gp.tileSize) - (gp.tileSize/2);
		try {
			startImage = ImageIO.read(getClass().getResourceAsStream("/res/ui/startButton.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void update(Client client) {
		if (keyH.enterPressed == true) {
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
		image = startImage;
		g2.drawImage(image, x, y, gp.tileSize*2, gp.tileSize, null);
	}
}
