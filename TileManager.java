import java.awt.Graphics2D;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.imageio.ImageIO;

public class TileManager {
	GamePanel gp;
	public Tile[] tile;
	int windowSize;
	
	public int mapTileNum[][];
	
	public TileManager(GamePanel gp) {
		this.gp = gp;
		
		tile = new Tile[10];
		mapTileNum = new int[gp.maxScreenCol][gp.maxScreenCol];
		
		getTileImage();
		loadMap("/res/maps/arena.txt");
	}
	public void getTileImage() {
		try {
			tile[0] = new Tile();
			tile[0].image = ImageIO.read(getClass().getResourceAsStream("/res/tiles/mainFloor.png"));
			tile[1] = new Tile();
			tile[1].image = ImageIO.read(getClass().getResourceAsStream("/res/tiles/wall.png"));
			tile[1].collision = true;
			tile[2] = new Tile();
			tile[2].image = ImageIO.read(getClass().getResourceAsStream("/res/tiles/blueFloor.png"));
			tile[3] = new Tile();
			tile[3].image = ImageIO.read(getClass().getResourceAsStream("/res/tiles/purpleFloor.png"));
			tile[4] = new Tile();
			tile[4].image = ImageIO.read(getClass().getResourceAsStream("/res/tiles/redFloor.png"));
			tile[5] = new Tile();
			tile[5].image = ImageIO.read(getClass().getResourceAsStream("/res/tiles/yellowFloor.png"));
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	public void loadMap(String arena) {
		try {
			InputStream is = getClass().getResourceAsStream(arena);
			BufferedReader br = new BufferedReader(new InputStreamReader(is));
			
			int col = 0;
			int row = 0;
			
			while (col < gp.maxScreenCol && row < gp.maxScreenCol) {
				String line = br.readLine();
				while(col < gp.maxScreenCol) {
					String numbers[] = line.split(" ");
					int num = Integer.parseInt(numbers[col]);
					mapTileNum[col][row] = num;
					col++;
				}
				if (col == gp.maxScreenCol) {
					col = 0;
					row++;
				}
			}
			br.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public void draw(Graphics2D g2) {
		
		/*
		 * for (int i = 0; (i * gp.tileSize) < gp.screenWidth; i++) { for (int j = 0; (j
		 * * gp.tileSize) < gp.screenWidth; j++) { if (((i*gp.tileSize) ==
		 * (gp.screenWidth / 2) || (i*gp.tileSize) == ((gp.screenWidth / 2) -
		 * gp.tileSize)) && (j < 2)) { g2.drawImage(tile[2].image, i*gp.tileSize,
		 * j*gp.tileSize, gp.tileSize, gp.tileSize, null); } else if (((i*gp.tileSize)
		 * == (gp.screenWidth / 2) || (i*gp.tileSize) == ((gp.screenWidth / 2) -
		 * gp.tileSize)) && (j >= gp.maxScreenCol - 2)) { g2.drawImage(tile[4].image,
		 * i*gp.tileSize, j*gp.tileSize, gp.tileSize, gp.tileSize, null); } else if
		 * (((j*gp.tileSize) == (gp.screenWidth / 2) || (j*gp.tileSize) ==
		 * ((gp.screenWidth / 2) - gp.tileSize)) && (i >= gp.maxScreenCol - 2)) {
		 * g2.drawImage(tile[5].image, i*gp.tileSize, j*gp.tileSize, gp.tileSize,
		 * gp.tileSize, null); } else if (((j*gp.tileSize) == (gp.screenWidth / 2) ||
		 * (j*gp.tileSize) == ((gp.screenWidth / 2) - gp.tileSize)) && (i < 2)) {
		 * g2.drawImage(tile[3].image, i*gp.tileSize, j*gp.tileSize, gp.tileSize,
		 * gp.tileSize, null); } else if ((i < 2) || (i >= gp.maxScreenCol - 2) || (j <
		 * 2) || (j >= gp.maxScreenCol - 2)) { g2.drawImage(tile[1].image,
		 * i*gp.tileSize, j*gp.tileSize, gp.tileSize, gp.tileSize, null); } else {
		 * g2.drawImage(tile[0].image, i*gp.tileSize, j*gp.tileSize, gp.tileSize,
		 * gp.tileSize, null); } } }
		 */
		
		int col = 0;
		int row = 0;
		int x = 0;
		int y = 0;
		
		while (col < gp.maxScreenCol && row < gp.maxScreenCol) {
			int tileNum = mapTileNum[col][row];
			g2.drawImage(tile[tileNum].image, x, y, gp.tileSize, gp.tileSize, null);
			col++;
			x += gp.tileSize;
			if (col == gp.maxScreenCol) {
				col = 0;
				x = 0;
				row++;
				y += gp.tileSize;
			}
		}
	}
}
