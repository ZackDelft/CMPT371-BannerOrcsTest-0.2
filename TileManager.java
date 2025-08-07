// CMPT 371 - Group 25 - Banner Orcs - TileManager.java

import java.awt.Graphics2D;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.imageio.ImageIO;

// Tile management class
// - Uses arena.txt to build the play area
//   - numbers corrispond to images and collision/solid status
// - See Main.java for references used
public class TileManager {

	// Needed variables
	GamePanel gp;
	int windowSize;
	public Tile[] tile; // array to hold the different tiles that can be used
	public int mapTileNum[][]; // The actual arena
	
	// constructor
	public TileManager(GamePanel gp) {
		this.gp = gp;
		
		tile = new Tile[6];
		mapTileNum = new int[gp.maxScreenCol][gp.maxScreenCol];
		
		getTileImage();
		loadMap("/res/maps/arena.txt");
	}

	// Function to initialize tile array indexes and load corrisponding images
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

	// Function to build the arena
	// - goes row by row through arena.txt
	//   - integer values (columns) are space seperated and correspond to the tile to be used in the draw method
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

	// Function used by GamePanel to draw the arena
	// - uses 2d array of integers to get index of image to use
	// - fills screen row by row, column by column
	public void draw(Graphics2D g2) {
		
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

// ZMMD