# CMPT 371 Group 25 - Banner Orcs

## Group 25 Team Members
* Zachariah Delft
* Raffi Macaraig
* Arshdeep Mann
* Rithik Ramamurthy
  
## Game Description

Banner Orcs is a fast paced four player client-server java game, that utilises the UDP transport-layer protocol and socket programming as the method of communication between players and the server. In this game, savage orcs battle over a contested banner for your amusement, with the hopes of bringing the banner back to their corresponding zone and bring glory to their respective clans. Doing so will score a point and if an orc reaches a score limit of 5, they win the game and are declared ‘Warboss’. 

### Video like of gameplay
* https://www.youtube.com/watch?v=KqpkDEARK7E
* Original upload:
  * https://www.youtube.com/watch?v=T1_Dr04uMuk
  * Edited by Arshdeep Mann
    * https://github.com/devarshmann

## Link to BannerOrcsTest
* https://github.com/ZackDelft/CMPT371-BannerOrcsTest
  * This was the original test version of the game without any of the client-server functionality seen in the final 0.2 version

## Building and Running the Program
* Step 1: Open up the directory containing the repository in your terminal
* Step 2: Within the directory containing Main.java, run the command ‘javac Main.java’
* Step 3: To run the application, run the command ‘java Main.java’
  *  Note: This application was developed using JDK 23
    
## Starting a Server and Joining a Game
* When first running the program, the user will be asked if they would like to host a game
on their machine.
* If the user chooses to run a host server, they will be prompted to enter the port number of which they would like the application to run on. Once entering a valid port number, their client thread will automatically connect to the server using the IP address 127.0.0.1 (localhost) and the previously entered port number.
* If the user chooses to not run a host server, they will be prompted to enter the IP
address and port number of the host computer
* Once connected, players will be presented with the start screen

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
<sub><sup>Artwork by Zachariah Delft.</sup></sub>\
<sub><sup>This game is not meant to be sold or redistributed in any way</sup></sub>
##
