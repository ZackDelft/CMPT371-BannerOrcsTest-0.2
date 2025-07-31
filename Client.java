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
                case "00":
                    gp.playerControl = Integer.parseInt(parsedMessage[1].trim());
                    break;
                case "01":
                    if (parsedMessage[1].trim().equalsIgnoreCase("start")) {
                        gp.started = true;
                    }
                    break;            
                default:
                    break;
            }
        }
    }

    public void sendConnectPacket() {
        byte[] data = "00".getBytes();
        DatagramPacket packet = new DatagramPacket(data, data.length, ip, 53333);
        try {
            socket.send(packet);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

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
        DatagramPacket packet = new DatagramPacket(data, data.length, ip, 53333);
        try {
            socket.send(packet);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
