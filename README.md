# CMPT 371 Group 25 - Banner Orcs

## Members
* Zachariah Delft
* 
* 
* 
  
## Game Description

This is a client-server java game meant for four players/orcs. Wild orcs battle over a banner for your amusement, with the hopes of bringing the flag back to their corresponding zone and bring glory to their clans. Doing so scores a point and if a orc reaches a score of 5, they win the game and are declared Warboss.

### Video like of gameplay
* https://www.youtube.com/watch?v=KqpkDEARK7E
* Original upload:
  * https://www.youtube.com/watch?v=T1_Dr04uMuk
  * Edited by Arshdeep Mann
    * https://github.com/devarshmann

## How to run and play the game

* Open the main folder in the terminal/command prompt
* Run the command "javac Main.java"
* Run the command "java Main.java"
* First player to run Main.java should choose yes for starting the server
  * This player will enter the port on which they would like the game to run
  * This player's client thread will automatically use the previously entered port number and localhost as the IP address for the server
* Subsequent players should click the option no to starting the server, if they want to connect to the game without issues forming
  * You will then need to enter the IP and port number of the host server
* You will then be presented with a start sreen
  * Game will start when all player are ready or atleast one player is ready and the 2 minute countdown timer elapses
* Enjoy?

## Controls

* Press 'ENTER' on start screen to set ready status
  * Green indicates ready, grey indicates unready
* 'WASD' to move
  * 'W' = up
  * 'A' = left
  * 'S' = down
  * 'D' = right
* When in range of another orc, press 'SPACE' to throw them
  * There is a cool down associated and you cannot move when throwing
  * Players are able to throw more than one orc at a time
  * Being throw causes an orc to drop the flag
  * An orc can be thrown to the ground, indicated by not being able to move during throw time allotment
* When presented, pressing 'enter' exits the program
  * On scoreboard screen and server connection lost timeout screens

## References used during development

1. Course materials and resourses provided by CMPT 371 - Data Communications and Networking - Simon Fraser University
2. How to make a 2D game in Java
   * https://www.youtube.com/playlist?list=PL_QPQmz5C6WUF-pOQDsbsKbaBZqXj4qSq
   * Reference was made to Java game development basics and lead to certain design decisions, such as the use of the Delta method for FPS, as well as the tile management system for more flexible arena construction
3. Multiplayer Support: Basic UDP Client/Server Ping Pong
   * https://www.youtube.com/watch?v=l1p21JWa_8s
   * Reference made to JOptionPane use and inspired client hosting the server thread

##
