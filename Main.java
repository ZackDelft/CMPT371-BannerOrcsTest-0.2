// CMPT 371 - Group 25 - Banner Orcs - Main.java

import javax.swing.JFrame;

// References used during development:
// - How to make a 2D game in Java
//   - https://www.youtube.com/playlist?list=PL_QPQmz5C6WUF-pOQDsbsKbaBZqXj4qSq
//   - Reference was made to game machanics handling, such as Delta method for FPS
// - Multiplayer Support: Basic UDP Client/Server Ping Pong
//   - https://www.youtube.com/watch?v=l1p21JWa_8s
//   - Inspired a client being the host of the game server
// - Materials and resourses provided by CMPT 371 - Simon Fraser University

// Entry point for program
// - Creates JFrame window and sets initial values
// - Creates a new GamePanel and adds it to the window
// - Starts the GamePanel game thread
public class Main {
	public static void main(String[] args) {
		JFrame window = new JFrame(); // create window
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Allows window to be closed properly
		window.setAutoRequestFocus(false); // Cannot be resized
		window.setTitle("CMPT 371 - Group 25 - Banner Orcs");
		
		GamePanel gamePanel = new GamePanel();
		window.add(gamePanel);
		
		window.pack();
		
		window.setLocationRelativeTo(null); // set window to middle of the screen
		window.setVisible(true);
		
		gamePanel.startGameThread();
		
	}
}

// ZMMD