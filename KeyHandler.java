import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyHandler implements KeyListener{
	
	public boolean upPressed, downPressed, leftPressed, rightPressed, spacePressed, enterPressed;
	public int playerControl = 1;

	// UNUSED
	@Override
	public void keyTyped(KeyEvent e) {
		// Unused here			
	}

	// keydown event handler
	@Override
	public void keyPressed(KeyEvent e) {
		// movement
		int code = e.getKeyCode();
		// up
		if (code == KeyEvent.VK_W ) {
			upPressed = true;
		}
		// down
		if (code == KeyEvent.VK_S) {
			downPressed = true;
		}
		// left
		if (code == KeyEvent.VK_A) {
			leftPressed = true;
		}
		// right
		if (code == KeyEvent.VK_D) {
			rightPressed = true;
		}
		// up-left
		if (code == KeyEvent.VK_W) {
			upPressed = true;
		}
		// change player
		if (code == KeyEvent.VK_1) {
			playerControl = 1;
		}
		if (code == KeyEvent.VK_2) {
			playerControl = 2;
		}
		if (code == KeyEvent.VK_3) {
			playerControl = 3;
		}
		if (code == KeyEvent.VK_4) {
			playerControl = 4;
		}
		// throw in-range players
		if (code == KeyEvent.VK_SPACE) {
			spacePressed = true;
		}
		
		// Start game at start screen
		if (code == KeyEvent.VK_ENTER) {
			enterPressed = true;
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		int code = e.getKeyCode();
		// up
		if (code == KeyEvent.VK_W ) {
			upPressed = false;
		}
		// down
		if (code == KeyEvent.VK_S) {
			downPressed = false;
		}
		// left
		if (code == KeyEvent.VK_A) {
			leftPressed = false;
		}
		// right
		if (code == KeyEvent.VK_D) {
			rightPressed = false;
		}
		
		// end attack
		if (code == KeyEvent.VK_SPACE) {
			spacePressed = false;
		}
		
		// end enter press
		// Start game at start screen
		if (code == KeyEvent.VK_ENTER) {
			enterPressed = false;
		}
	}

}
