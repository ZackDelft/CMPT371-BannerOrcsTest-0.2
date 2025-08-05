import java.util.Arrays;
import java.util.Comparator;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

public class ScoreBoard {
	GamePanel gp;
	BufferedImage[] crowns;
	BufferedImage pi, pii, piii, piv;
	int firstCrownX, firstCrownY;
	Font font = new Font("Ariel", Font.CENTER_BASELINE, 20);
	
	public ScoreBoard(GamePanel gp) {
		this.gp = gp;
		firstCrownX = ((gp.maxScreenCol / 2) - 1) * gp.tileSize;
		firstCrownY = ((gp.maxScreenCol / 2) - 2) * gp.tileSize;
		loadImages();
	}
	
	void loadImages() {
		try {
			crowns = new BufferedImage[4];
			crowns[0] = ImageIO.read(getClass().getResourceAsStream("/res/ui/1st.png"));
			crowns[1] = ImageIO.read(getClass().getResourceAsStream("/res/ui/2nd.png"));
			crowns[2] = ImageIO.read(getClass().getResourceAsStream("/res/ui/3rd.png"));
			crowns[3] = ImageIO.read(getClass().getResourceAsStream("/res/ui/4th.png"));
			pi = ImageIO.read(getClass().getResourceAsStream("/res/ui/p1.png"));
			pii = ImageIO.read(getClass().getResourceAsStream("/res/ui/p2.png"));
			piii = ImageIO.read(getClass().getResourceAsStream("/res/ui/p3.png"));
			piv = ImageIO.read(getClass().getResourceAsStream("/res/ui/p4.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void update(JFrame window) {
		if (gp.keyH.enterPressed == true) {
			System.out.println("enter-pressed");
			gp.running = false;
			//System.exit(0);
		}
	}
	
	public void draw(Graphics2D g2) {
		if (gp.playersSorted == false) {
			Arrays.sort(gp.players, Comparator.comparing(Player::getScore).reversed());
			gp.playersSorted = true;
		}
		for (int i = 0; i < gp.players.length; i++) {
			g2.drawImage(crowns[i], firstCrownX, firstCrownY + (i * gp.tileSize), gp.tileSize, gp.tileSize, null);
			switch (gp.players[i].ID) {
			case 1:
				g2.drawImage(pi, firstCrownX + gp.tileSize, firstCrownY + (i * gp.tileSize), gp.tileSize, gp.tileSize, null);
				break;
			case 2:
				g2.drawImage(pii, firstCrownX + gp.tileSize, firstCrownY + (i * gp.tileSize), gp.tileSize, gp.tileSize, null);
				break;
			case 3:
				g2.drawImage(piii, firstCrownX + gp.tileSize, firstCrownY + (i * gp.tileSize), gp.tileSize, gp.tileSize, null);
				break;
			case 4:
				g2.drawImage(piv, firstCrownX + gp.tileSize, firstCrownY + (i * gp.tileSize), gp.tileSize, gp.tileSize, null);
				break;
			}
		}
		g2.setColor(Color.WHITE);
		g2.setFont(font);
		g2.drawString("Press 'enter' to quit", ((gp.maxScreenCol / 2) - 2) * gp.tileSize, firstCrownY + (5 * gp.tileSize));
	}
}
