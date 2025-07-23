import javax.swing.JFrame;

public class Main {
	public static void main(String[] args) {
		JFrame window = new JFrame(); // create window
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Allows window to be closed properly
		window.setAutoRequestFocus(false); // Cannot be resized
		window.setTitle("Game Test 1");
		
		GamePanel gamePanel = new GamePanel();
		window.add(gamePanel);
		
		window.pack();
		
		window.setLocationRelativeTo(null); // set window to middle of the screen
		window.setVisible(true);
		
		gamePanel.startGameThread();
	}
}