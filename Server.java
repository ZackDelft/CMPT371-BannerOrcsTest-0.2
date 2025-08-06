import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

public class Server extends Thread{
    private DatagramSocket socket;
    private GamePanel gp;
    Entity[] players = new Entity[4];
    int currentPlayers = 0;
    int readyPlayers = 0;
    Flag flag;
    boolean finished = false;
    int port; // = 53333;

    public Server(GamePanel gp, int port) {
        this.gp = gp;
        this.port = port;
        flag = new Flag(gp);
        try {
            this.socket = new DatagramSocket(port);
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }

    public void run() {
        int id;
        while (gp.finished != true) { // If server seperate change end condition - might not be doing anything
            byte[] data = new byte[1024];
            DatagramPacket packet = new DatagramPacket(data, data.length);
            try {
                socket.receive(packet);
            } catch (IOException e) {
                e.printStackTrace();
            }
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
                        }
                    }
                    break;
                // ready message
                // - expects "01 readyStatus playerID"
                // - returns "01 start" if all 4 players ready
                // - else sends "07 numPlayersReady" to all players
                // - should rearange order of ID and readyStatus
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
            // Send connection lives signals to players who needs it
            // - if player hasn't recieved message in 5 sec, sends message containing "08"
            // ------- Needs seperate thread
            // for (int i = 0; i < currentPlayers; i++) {
            //     if (players[i].lastTimeUpdated + (5 * gp.oneSec) < System.nanoTime()) {
            //         message = "08";
            //         sendData(message.getBytes(), players[i].ip, players[i].port);
            //         players[i].lastTimeUpdated = System.nanoTime();
            //     }
            // }   
        }
        System.out.println("closing server socket");
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
