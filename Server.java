// CMPT 371 - Group 25 - Banner Orcs - Server.java

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

// This is the main server thread that runs alongside the host client thread
public class Server extends Thread{

    // This class/thread is used to send keep alive messages to the connected client that need one so they don't time out
    // - if player hasn't recieved message in 5 sec, sends message containing "08"
    // - checks players every 100 miliseconds
    class CheckConnections extends Thread {
        public synchronized void run() {
            System.out.println("connection thread running");
            String message = "08"; // keep alive message to be transmitted
            while (gp.finished != true) {
                // to slow down thread
                try {
                    wait(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } 

                int cp = currentPlayers; // gets current number of players from the main server thread
                System.out.println(cp);
                for (int i = 0; i < cp; i++) {
                    // Send player a server lives message if hasn't recieved message in 5 sec
                    if ((players[i].lastTimeUpdated + (5 * gp.oneSec)) < System.nanoTime()) {
                        System.out.println(players[i].lastTimeUpdated + " vs " + System.nanoTime());
                        System.out.println("sending 08");
                        sendData(message.getBytes(), players[i].ip, players[i].port); // send message to players IP and port 
                        players[i].lastTimeUpdated = System.nanoTime(); // reset keep alive message timer
                    }
                    // If hasn't recieved message from client in 30 sec, assume connection lost
                }

                // Create countdown check to set all players ready
                // - is currently set to a 2 min counter
                if (gp.started == false && currentPlayers == 4 && readyPlayers > 0 && allPlayersConnectedTime == -1) {
                    allPlayersConnectedTime = System.nanoTime();
                }
                if (gp.started == false && currentPlayers == 4 && readyPlayers > 0 && System.nanoTime() >= (allPlayersConnectedTime + (120 * gp.oneSec))) {
                    // sends "01 start" if all 4 players ready
                    message = "01 start";
                    for (int i = 0; i < currentPlayers; i++) {
                        System.out.println("sending start to player " + i + " in timeout");
                        sendData(message.getBytes(), players[i].ip, players[i].port);
                        players[i].lastTimeUpdated = System.nanoTime();
                    }
                }
            }
            System.out.println("Closing CheckConnections thread");
        }
    }

    // Server variables
    private DatagramSocket socket; // socket used for communication
    private GamePanel gp; // GamePanel to have access to game variables
    Entity[] players = new Entity[4]; // To hold players in the server
    int currentPlayers = 0; // current player count in the server
    int readyPlayers = 0; // current number of players ready
    Flag flag;
    int port; // port to be used by server

    // If a player quits before pressing ready to starting the game, players will get stuck on start screen
    // - following variables are used to create 2 min countdown until setting all players to ready  
    Long allPlayersConnectedTime = -1L;

    // Server constructor
    public Server(GamePanel gp, int port) {
        this.gp = gp;
        this.port = port;
        flag = new Flag(gp);
        // Initialize socket
        try {
            this.socket = new DatagramSocket(port);
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }

    // Main server function
    public synchronized void run() {
        int id; // id to be read from incomming packets

        // Initialize a CheckConnections thread and start it
        CheckConnections playerConnections = new CheckConnections();
        playerConnections.start();

        // Loop while GamePanel is running
        // - Stops when host client is finished playing or quits
        while (gp.finished != true) { 

            // Wait for an input from a client
            // - Create byte array to store input data
            // - initialize a Datagram packet
            byte[] data = new byte[1024];
            DatagramPacket packet = new DatagramPacket(data, data.length);
            // Wait for input data
            try {
                socket.receive(packet);
            } catch (IOException e) {
                e.printStackTrace();
            }
            // Handle the input data
            // - Get the string
            // - Parse the string
            //   - String values are space seperated
            //   - First value tells you its type
            //   - All types:
            //     - "00" = connection message
            //     - "01" = player ready message
            //     - "02" = player position update message
            //     - "03" = flag possesion update message
            //     - "04" = player scored message
            //     - "05" = throw message
            //     - "06" = total players connected message
            //     - "07" = total players ready message 
            //     - "08" = keep alive message  
            // - When sending messages to clients, server updates players accordingly for keep alive messages         
            String message = new String(packet.getData());
            System.out.println("Client > " + message + " Port: " + packet.getPort() + " ip: " + packet.getAddress().getHostAddress());
            String[] parseMessage = message.split(" ");
            switch (parseMessage[0].trim()) {
                // connection message
                // - expects "00" from client
                // - returns "00 playerID" to player that connected
                // - forwards updated connected player count via "06 numPlayersConnected" to all players
                case "00":
                    if (currentPlayers < players.length) {
                        int ID = currentPlayers + 1;
                        message = "00 " + ID;
                        players[currentPlayers] = new Player(gp, null, ID, packet.getAddress(), packet.getPort());
                        sendData(message.getBytes(), packet.getAddress(), packet.getPort());
                        currentPlayers++;
                        message = "06 " + currentPlayers;
                        for (int i = 0; i < currentPlayers; i++) {
                            sendData(message.getBytes(), players[i].ip, players[i].port);
                            players[i].lastTimeUpdated = System.nanoTime();
                        }
                    }
                    break;
                // ready message
                // - expects "01 readyStatus playerID"
                // - returns "01 start" if all 4 players ready
                // - else sends "07 numPlayersReady" to all players
                case "01":
                    id = Integer.parseInt(parseMessage[2].trim());
                    if (parseMessage[1].trim().equalsIgnoreCase("1")) {
                        players[id - 1].ready = true;
                        readyPlayers++;
                    }
                    else {
                        players[id - 1].ready = false;
                        readyPlayers--;
                    }
                    message = "07 " + readyPlayers;
                    for (int i = 0; i < currentPlayers; i++) {
                        sendData(message.getBytes(), players[i].ip, players[i].port);
                        players[i].lastTimeUpdated = System.nanoTime();
                    }
                    if (currentPlayers == players.length) {
                        if (allReady(players) == true) {
                            message = "01 start";
                            for (int i = 0; i < players.length; i++) {
                                players[i].lastTimeUpdated = System.nanoTime();
                                sendData(message.getBytes(), players[i].ip, players[i].port);                                
                            }
                        }                     
                    }
                    break;
                // Player position update message
                // - expects "02 playerID x y"
                // - returns "02 playerID x y" to all other players 
                case "02":
                    id = Integer.parseInt(parseMessage[1].trim());
                    int x = Integer.parseInt(parseMessage[2].trim());
                    int y = Integer.parseInt(parseMessage[3].trim());
                    players[id - 1].x = x;
                    players[id - 1].y = y;
                    for (int i = 0; i < currentPlayers; i++) {
                        if (id != players[i].ID) {
                            sendData(packet.getData(), players[i].ip, players[i].port);
                            players[i].lastTimeUpdated = System.nanoTime();
                        }
                    }
                    break;
                // Player flag possession update message
                // - expects "03 playerID [0=drop,1=pickup]"
                // - forwards message to other players
                case "03":
                    id = Integer.parseInt(parseMessage[1].trim());
                    int possessed = Integer.parseInt(parseMessage[2].trim());
                    if (possessed > 0) {
                        players[id - 1].hasFlag = true;
                        flag.possessed = id;
                    }
                    else {
                        players[id - 1].hasFlag = false;
                        flag.possessed = 0;
                    }
                    for (int i = 0; i < currentPlayers; i++) {
                        if (id != players[i].ID) {
                            sendData(packet.getData(), players[i].ip, players[i].port);
                            players[i].lastTimeUpdated = System.nanoTime();
                        }
                    }
                    break;
                // Player scored message
                // - expects "04 playerID"
                // - returns "04 playerID [true=finished | false=notFinished] newFlagX newFlagY"
                case "04":
                    String fin = "false";
                    id = Integer.parseInt(parseMessage[1].trim());
                    flag.sendRandomSpot();
                    players[id - 1].score++;
                    if (players[id - 1].score >= 5) {
                        fin = "true";
                    }
                    message = "04 " + id + " " + fin + " " + flag.x + " " + flag.y;
                    System.out.println(message);
                    for (int i = 0; i < currentPlayers; i++) {
                        sendData(message.getBytes(), players[i].ip, players[i].port);
                        players[i].lastTimeUpdated = System.nanoTime();
                    }
                    break;
                // Player throw message
                // - expects "05 playerID"
                // - sends "05 playerID" to player associated with ID
                case "05":
                    id = Integer.parseInt(parseMessage[1].trim());
                    sendData(packet.getData(), players[id - 1].ip, players[id - 1].port);
                    players[id - 1].lastTimeUpdated = System.nanoTime();
                    break;
                default:
                    break;
            }
        }
        System.out.println("closing server socket");
        // Close the socket
        socket.close();
    }

    // Supporting function to send the actual data
    public void sendData(byte[] data, InetAddress ip, int port) {
        DatagramPacket packet = new DatagramPacket(data, data.length, ip, port);
        try {
            socket.send(packet);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Supporting function for game start state comparison
    public boolean allReady(Entity[] players) {
        for (int i = 0; i < players.length; i++) {
            if (players[i].ready == false) {
                return false;
            }
        }
        return true;
    }
}

// ZMMD