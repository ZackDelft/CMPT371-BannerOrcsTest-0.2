// CMPT 371 - Group 25 - Banner Orcs - CollisionChecker.java

// Used to detect all colisions within the game
public class CollisionChecker {
	
	GamePanel gp;
	
	// Constructor
	public CollisionChecker(GamePanel gp) {
		this.gp = gp;
	}
	
	// Check if player is hitting wall
	public void checkTile(Entity entity) {
		int entityLeft = entity.x + entity.hitbox.x;
		int entityRight = entity.x + entity.hitbox.x + entity.hitbox.width;
		int entityTop = entity.y + entity.hitbox.y;
		int entityBottom = entity.y + entity.hitbox.y + entity.hitbox.height;
		
		int entityLeftCol = entityLeft / gp.tileSize;
		int entityRightCol = (entityRight - 1) / gp.tileSize;
		int entityTopRow = entityTop / gp.tileSize;
		int entityBottomRow = (entityBottom - 1) / gp.tileSize;
		
		int tileNum1, tileNum2;
		
		switch (entity.direction)  {
		case "up":
			entityTopRow = (entityTop - entity.speed) / gp.tileSize;
			tileNum1 = gp.tileM.mapTileNum[entityLeftCol][entityTopRow];
			tileNum2 = gp.tileM.mapTileNum[entityRightCol][entityTopRow];
			if (gp.tileM.tile[tileNum1].collision == true || gp.tileM.tile[tileNum2].collision == true || entity.y <= 0) {
				entity.collisionOn = true;
			}
			break;
		case "down":
			entityBottomRow = Math.min(((entityBottom + entity.speed) / gp.tileSize), gp.maxScreenCol - 1);
			tileNum1 = gp.tileM.mapTileNum[entityLeftCol][entityBottomRow];
			tileNum2 = gp.tileM.mapTileNum[entityRightCol][entityBottomRow];
			if (gp.tileM.tile[tileNum1].collision == true || gp.tileM.tile[tileNum2].collision == true || entity.y >= gp.screenWidth - gp.tileSize) {
				entity.collisionOn = true;
			}
			break;
		case "right":
			entityRightCol = (entityRight + entity.speed) / gp.tileSize;
			tileNum1 = gp.tileM.mapTileNum[entityRightCol][entityTopRow];
			tileNum2 = gp.tileM.mapTileNum[entityRightCol][entityBottomRow];
			if (gp.tileM.tile[tileNum1].collision == true || gp.tileM.tile[tileNum2].collision == true || entity.x >= gp.screenWidth - gp.tileSize) {
				entity.collisionOn = true;
			}
			break;
		case "left":
			entityLeftCol = (entityLeft - entity.speed) / gp.tileSize;
			tileNum1 = gp.tileM.mapTileNum[entityLeftCol][entityTopRow];
			tileNum2 = gp.tileM.mapTileNum[entityLeftCol][entityBottomRow];
			if (gp.tileM.tile[tileNum1].collision == true || gp.tileM.tile[tileNum2].collision == true || entity.x <= 0) {
				entity.collisionOn = true;
			}
			break;
		}
	}
	
	// check for collisions with other players
	public void checkPlayers(Entity entity) {
		
		for (int i = 0; i < gp.players.length; i++) {
			if (gp.players[i].ID != entity.ID) {
				
				int othersLeft = gp.players[i].x + gp.players[i].hitbox.x;
				int othersRight = gp.players[i].x + gp.players[i].hitbox.x + gp.players[i].hitbox.width;
				int othersTop = gp.players[i].y + gp.players[i].hitbox.y;
				int othersBottom = gp.players[i].y + gp.players[i].hitbox.y + gp.players[i].hitbox.height;
				
				int entityLeft = entity.x + entity.hitbox.x;
				int entityRight = entity.x + entity.hitbox.x + entity.hitbox.width;
				int entityTop = entity.y + entity.hitbox.y;
				int entityBottom = entity.y + entity.hitbox.y + entity.hitbox.height;
				
				switch (entity.direction)  {
				case "up":
					if ((entityTop - entity.speed <= othersBottom && entityTop - entity.speed >= othersTop && entityLeft <= othersRight && entityLeft >= othersLeft) ||
							(entityTop - entity.speed <= othersBottom && entityTop - entity.speed >= othersTop && entityRight <= othersRight && entityRight >= othersLeft)) {
						entity.collisionOn = true;
					}
					break;
				case "down":
					
					if ((entityBottom + entity.speed <= othersBottom && entityBottom + entity.speed >= othersTop && entityLeft <= othersRight && entityLeft >= othersLeft) ||
							(entityBottom + entity.speed <= othersBottom && entityBottom + entity.speed >= othersTop && entityRight <= othersRight && entityRight >= othersLeft)) {
						entity.collisionOn = true;
					}
					break;
				case "right":
					
					if ((entityRight + entity.speed >= othersLeft && entityRight + entity.speed <= othersRight && entityTop <= othersBottom && entityTop >= othersTop) ||
							(entityRight + entity.speed >= othersLeft && entityRight + entity.speed <= othersRight && entityBottom <= othersBottom && entityBottom >= othersTop)) {
						entity.collisionOn = true;
					}
					break;
				case "left":
					
					if ((entityLeft - entity.speed >= othersLeft && entityLeft - entity.speed <= othersRight && entityTop <= othersBottom && entityTop >= othersTop) ||
							(entityLeft - entity.speed >= othersLeft && entityLeft - entity.speed <= othersRight && entityBottom <= othersBottom && entityBottom >= othersTop)) { 
						entity.collisionOn = true; 
					}
					break;
				}
			}
		}
	}
	
	// Check if player touched flag
	// - If a player picks up the flag a "03" message is sent to server with new holder id
	public void flagChecker(Entity entity, Client client) {
		
		int flagTop = gp.flag.y;
		int flagBottom = gp.flag.y + gp.tileSize;
		int flagLeft = gp.flag.x;
		int flagRight = gp.flag.x + gp.tileSize;
		
		int entityTop = entity.y + entity.hitbox.y;
		int entityBottom = entity.y + entity.hitbox.y + entity.hitbox.height;
		int entityLeft = entity.x + entity.hitbox.x;
		int entityRight = entity.x + entity.hitbox.x + entity.hitbox.width;
		
		switch (entity.direction) {
		case "up":
			if ((gp.flag.possessed == 0 && !entity.hasFlag && entityTop <= flagBottom && entityTop >= flagTop && entityLeft >= flagLeft && entityLeft <= flagRight) ||
					(gp.flag.possessed == 0 && !entity.hasFlag && entityTop <= flagBottom && entityTop >= flagTop && entityRight >= flagLeft && entityRight <= flagRight)) {
				if (entity.isThrown == false) {
					gp.flag.setHolder(entity);
					entity.hasFlag = true;
					client.sendFlagPossesion(); // Sends "03 flag.possessed" to server
				}
			}
			break;
		case "down":
			if ((gp.flag.possessed == 0 && !entity.hasFlag && entityBottom <= flagBottom && entityBottom >= flagTop && entityLeft >= flagLeft && entityLeft <= flagRight) ||
					(gp.flag.possessed == 0 && !entity.hasFlag && entityBottom <= flagBottom && entityBottom >= flagTop && entityRight >= flagLeft && entityRight <= flagRight)) {
				if (entity.isThrown == false) {
					gp.flag.setHolder(entity);
					entity.hasFlag = true;
					client.sendFlagPossesion(); // Sends "03 flag.possessed" to server
				}
			}
			break;
		case "right":
			if ((gp.flag.possessed == 0 && !entity.hasFlag && entityRight <= flagRight && entityRight >= flagLeft && entityTop >= flagTop && entityTop <= flagBottom) ||
					(gp.flag.possessed == 0 && !entity.hasFlag && entityRight <= flagRight && entityRight >= flagLeft && entityBottom >= flagTop && entityBottom <= flagBottom)) {
				if (entity.isThrown == false) {
					gp.flag.setHolder(entity);
					entity.hasFlag = true;
					client.sendFlagPossesion(); // Sends "03 flag.possessed" to server
				}
			}
			break;
		case "left":
			if ((gp.flag.possessed == 0 && !entity.hasFlag && entityLeft <= flagRight && entityLeft >= flagLeft && entityTop >= flagTop && entityTop <= flagBottom) ||
					(gp.flag.possessed == 0 && !entity.hasFlag && entityLeft <= flagRight && entityLeft >= flagLeft && entityBottom >= flagTop && entityBottom <= flagBottom)) {
				if (entity.isThrown == false) {
					gp.flag.setHolder(entity);
					entity.hasFlag = true;
					client.sendFlagPossesion(); // Sends "03 flag.possessed" to server
				}
			}
			break;
		}
	}
	
	// Check if player scores
	public void checkZone(Entity entity, Client client) {
		int zoneTop = gp.zones[entity.ID - 1].y;
		int zoneBottom = gp.zones[entity.ID - 1].y + gp.tileSize;
		int zoneLeft = gp.zones[entity.ID - 1].x;
		int zoneRight = gp.zones[entity.ID - 1].x + gp.tileSize;
		
		int entityTop = entity.y + entity.hitbox.y;
		int entityBottom = entity.y + entity.hitbox.y + entity.hitbox.height;
		int entityLeft = entity.x + entity.hitbox.x;
		int entityRight = entity.x + entity.hitbox.x + entity.hitbox.width;
		
		if ((entityTop < zoneBottom && entityTop > zoneTop && entityLeft >= zoneLeft && entityLeft <= zoneRight && entity.hasFlag) ||
				(entityTop < zoneBottom && entityTop > zoneTop && entityRight >= zoneLeft && entityRight <= zoneRight && entity.hasFlag) ||
				(entityBottom < zoneBottom && entityBottom > zoneTop && entityLeft >= zoneLeft && entityLeft <= zoneRight && entity.hasFlag) ||
				(entityBottom < zoneBottom && entityBottom > zoneTop && entityRight >= zoneLeft && entityRight <= zoneRight && entity.hasFlag) ||
				(entityLeft < zoneRight && entityLeft > zoneLeft && entityTop >= zoneTop && entityTop <= zoneBottom && entity.hasFlag) || 
				(entityLeft < zoneRight && entityLeft > zoneLeft && entityBottom >= zoneTop && entityBottom <= zoneBottom && entity.hasFlag) ||
				(entityRight < zoneRight && entityRight > zoneLeft && entityTop >= zoneTop && entityTop <= zoneBottom && entity.hasFlag) || 
				(entityRight < zoneRight && entityRight > zoneLeft && entityBottom >= zoneTop && entityBottom <= zoneBottom && entity.hasFlag)) {
			
			entity.hasFlag = false;
			gp.flag.possessed = 0;
			entity.direction = ""; // Seeing if this will stop an extremely rare bug 
			client.sendFlagPossesion(); // sends "03 flag.possessed" to server - test moving to server with "04" score messages
			client.sendScoredMessage(); // sends "04 playerID" to server
		}
	}
	
	// Check if within throw distance
	// - triggered by 'space' press
	// - sends "05 playerBeingThrownID" to server for all players in range
	public void checkThrowRange(Entity entity, Client client) {
		
		int entityTop = entity.y;
		int entityBottom = entity.y + gp.tileSize;
		int entityLeft = entity.x;
		int entityRight = entity.x + gp.tileSize;
		
		for (int i = 0; i < gp.players.length; i++) {
			if (gp.players[i].ID != entity.ID) {
				int othersLeft = gp.players[i].x;
				int othersRight = gp.players[i].x + gp.tileSize;
				int othersTop = gp.players[i].y;
				int othersBottom = gp.players[i].y + gp.tileSize;
				if ((entityTop < othersBottom && entityTop > othersTop && entityLeft >= othersLeft && entityLeft <= othersRight) ||
						(entityTop < othersBottom && entityTop > othersTop && entityRight >= othersLeft && entityRight <= othersRight) ||
						(entityBottom < othersBottom && entityBottom > othersTop && entityLeft >= othersLeft && entityLeft <= othersRight) ||
						(entityBottom < othersBottom && entityBottom > othersTop && entityRight >= othersLeft && entityRight <= othersRight) ||
						(entityLeft < othersRight && entityLeft > othersLeft && entityTop >= othersTop && entityTop <= othersBottom) || 
						(entityLeft < othersRight && entityLeft > othersLeft && entityBottom >= othersTop && entityBottom <= othersBottom) ||
						(entityRight < othersRight && entityRight > othersLeft && entityTop >= othersTop && entityTop <= othersBottom) || 
						(entityRight < othersRight && entityRight > othersLeft && entityBottom >= othersTop && entityBottom <= othersBottom)) {
					
					entity.nextThrowTime = System.nanoTime() + 5000000000L; // throw again in 5 seconds
					entity.stopThrowingAt = System.nanoTime() + 500000000L; // Stop throwing in 1 second
					entity.throwing = true;
					client.sendThrowMessage(gp.players[i].ID); // Sends "05 playerBeingThrownID"
				}
			}
		}
		
	}
}

// ZMMD