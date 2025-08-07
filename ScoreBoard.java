// CMPT 371 - Group 25 - Banner Orcs - ScoreBoard.java

import java.util.Arrays;
import java.util.Comparator;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;

// Class to control the scoreboard at the end of a game
public class ScoreBoard {

	// Needed display variable
	GamePanel gp;
	BufferedImage[] crowns;
	BufferedImage pi, pii, piii, piv;
	int firstCrownX, firstCrownY;
	Font font = new Font("Ariel", Font.CENTER_BASELINE, 20);
	
	// Constructor
	public ScoreBoard(GamePanel gp) {
		this.gp = gp;
		firstCrownX = ((gp.maxScreenCol / 2) - 1) * gp.tileSize;
		firstCrownY = ((gp.maxScreenCol / 2) - 2) * gp.tileSize;
		loadImages();
	}
	
	// Function to load the images for crowns and players
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

	// Update method used at end of game 
	// - whatches for players to press 'space' to close the game
	public void update() {
		if (gp.keyH.enterPressed == true) {
			gp.running = false;
		}
	}
	
	// Method used by gamePanel to display scoreboard at the end of a game
	public void draw(Graphics2D g2) {

		// Sort the array of players by score if needed
		if (gp.playersSorted == false) {
			Arrays.sort(gp.players, Comparator.comparing(Player::getScore).reversed());
			gp.playersSorted = true;
		}
		// Draw images and strings to needed locations
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

// ZMMD