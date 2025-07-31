import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

public class Client extends Thread {
    private InetAddress ip;
    private DatagramSocket socket;
    private GamePanel gp;
    private int port = 53333;

    public Client(GamePanel gp, String ip) {
        this.gp = gp;
        try {
            this.socket = new DatagramSocket();
            this.ip = InetAddress.getByName(ip);
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

    public void run() {
        int id;
        while (gp.finished != true) {
            byte[] data = new byte[1024];
            DatagramPacket packet = new DatagramPacket(data, data.length);
            try {
                socket.receive(packet);
            } catch (IOException e) {
                e.printStackTrace();
            }
            String message = new String(packet.getData());
            System.out.println("Server > " + message);
            String[] parsedMessage = message.split(" ");
            // if (parsedMessage[0].equalsIgnoreCase("00")) {
            //     gp.playerControl = Integer.parseInt(parsedMessage[1].trim());
            // }
            switch (parsedMessage[0].trim()) {
                // Server connection packet
                // - expects "00 playerIDtoUse"
                case "00":
                    gp.playerControl = Integer.parseInt(parsedMessage[1].trim());
                    break;
                // Server start game message
                // - expects "01 start"
                // - starts the game when recieved
                // - will change later so player knows how many players are ready
                case "01":
                    if (parsedMessage[1].trim().equalsIgnoreCase("start")) {
                        gp.started = true;
                    }
                    break;
                // Server player position update
                // - expects "02 playerID x y"
                // - update player associated with the ID
                case "02":
                    id = Integer.parseInt(parsedMessage[1].trim());
                    int x = Integer.parseInt(parsedMessage[2].trim());
                    int y = Integer.parseInt(parsedMessage[3].trim());
                    gp.players[id - 1].x = x;
                    gp.players[id - 1].y = y;
                    break;        
                default:
                    break;
            }
        }
        socket.close();
    }

    // Client conection packet
    // - contains token "00" as signifier 
    // - server should return packet with "00" token and player ID to be used 
    public void sendConnectPacket() {
        byte[] data = "00".getBytes();
        DatagramPacket packet = new DatagramPacket(data, data.length, ip, port);
        try {
            socket.send(packet);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    // Client ready packet
    // - contains token "01" as signifier, data = players ready status, player ID 
    // - server should return packet with "01" token and "start" when all 4 players are ready
    public void sendReadyPacket(boolean ready) {
        String message = "01 ";
        if (ready == true) {
            message += 1 + " ";
        }
        else {
            message += 0 + " ";
        }
        message += gp.playerControl;
        byte[] data = message.getBytes();
        DatagramPacket packet = new DatagramPacket(data, data.length, ip, port);
        try {
            socket.send(packet);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    // Client send position update
    // - contains token "02" as signifier, player ID, x value, y value
    public void sendPositionUpdate(int x, int y) {
        String message = "02 " + gp.playerControl + " " + x + " " + y;
        byte[] data = message.getBytes();
        DatagramPacket packet = new DatagramPacket(data, data.length, ip, port);
        try {
            socket.send(packet);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Client flag pickup/drop
    // - sends "03 playerID [0=drop,1=pickup]"
    public void sendFlagPossesion() {
        String message = "03 " + gp.playerControl + " " + gp.flag.possessed;
        byte[] data = message.getBytes();
        DatagramPacket packet = new DatagramPacket(data, data.length, ip, port);
        try {
            socket.send(packet);
        } catch (IOException e) {
            e.printStackTrace();
        } 
    }
}
