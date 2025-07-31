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

    public Server(GamePanel gp) {
        this.gp = gp;
        try {
            this.socket = new DatagramSocket(53333);
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }

    public void run() {
        while (gp.finished != true) {
            byte[] data = new byte[1024];
            DatagramPacket packet = new DatagramPacket(data, data.length);
            try {
                socket.receive(packet);
            } catch (IOException e) {
                e.printStackTrace();
            }
            String message = new String(packet.getData());
            System.out.println("Client > " + message + " Port: " + packet.getPort() + " ip: " + packet.getAddress().getHostAddress());
            // if (message.trim().equalsIgnoreCase("00")) {
            //     int ID = currentPlayers + 1;
            //     message = "00 " + ID;
            //     players[currentPlayers] = new Player(gp, null, ID);
            //     sendData(message.getBytes(), packet.getAddress(), packet.getPort());
            //     currentPlayers++;
            // }
            String[] parseMessage = message.split(" ");
            switch (parseMessage[0].trim()) {
                // connection message
                case "00":
                    if (currentPlayers < players.length) {
                        int ID = currentPlayers + 1;
                        message = "00 " + ID;
                        players[currentPlayers] = new Player(gp, null, ID, packet.getAddress(), packet.getPort());
                        sendData(message.getBytes(), packet.getAddress(), packet.getPort());
                        currentPlayers++;
                    }
                    break;
                // ready message
                case "01":
                    int player = Integer.parseInt(parseMessage[2].trim());
                    if (parseMessage[1].trim().equalsIgnoreCase("1")) {
                        players[player - 1].ready = true;
                    }
                    else {
                        players[player - 1].ready = false;
                    }
                    if (currentPlayers == players.length) {
                        if (allReady(players) == true) {
                            message = "01 start";
                            for (int i = 0; i < players.length; i++) {
                                sendData(message.getBytes(), players[i].ip, players[i].port);                                
                            }
                        }                       
                    }
                    break;
                default:
                    break;
            }
            
        }
    }

    public void sendData(byte[] data, InetAddress ip, int port) {
        DatagramPacket packet = new DatagramPacket(data, data.length, ip, port);
        try {
            socket.send(packet);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean allReady(Entity[] players) {
        for (int i = 0; i < players.length; i++) {
            if (players[i].ready == false) {
                return false;
            }
        }
        return true;
    }
}
