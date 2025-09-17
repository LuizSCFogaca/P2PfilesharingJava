import java.io.*;
import java.net.*;
import java.lang.Thread;

public class Peer {
    private int port;
    private DatagramSocket datagramsocket;
    
    public Peer(int port) throws IOException{
        this.port = port;
        this.datagramsocket = new DatagramSocket(port);
    };

    public int getPort(){
        return port;
    };

    public void setPort(int port){
        this.port = port;
    };

    public DatagramSocket getdatagramsocket(){
        return datagramsocket;
    };

    public void setdatagramsocket(DatagramSocket datagramsocket){
        this.datagramsocket = datagramsocket;
    };

    public void connectToPeer(String host, int port, String message) {
        try {
            byte[] buffer = message.getBytes();
            InetAddress address = InetAddress.getByName(host);
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length, address, port);
            datagramsocket.send(packet);
            System.out.println("Sent to " + host + ":" + port + " -> " + message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

private boolean replied = false;
    public void start() {
        System.out.println("Peer started on port: " + port);

        byte[] buffer = new byte[1024];
        while (true) {
            try {
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                datagramsocket.receive(packet);
                String msg = new String(packet.getData(), 0, packet.getLength());
                System.out.println("Received from " + packet.getAddress() + ":" + packet.getPort() + " -> " + msg);

                // Send response back
                if(!replied){ 
                    String response = "Hello back from peer on port " + this.port;
                    byte[] responseData = response.getBytes();
                    DatagramPacket responsePacket = new DatagramPacket(responseData, responseData.length, packet.getAddress(), packet.getPort());
                    datagramsocket.send(responsePacket);
                    replied = true;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

        public static void main(String[] args)throws IOException{
        if (args.length != 1) {
            System.out.println("Usage: java Peer <port>");
            return;
        }
        int port = Integer.parseInt(args[0]);
        try {
            Peer peer = new Peer(port);

            // Start listening in a new thread
            new Thread(peer::start).start();

            // Try to connect to other peers
            if (port == 5000) {
                peer.connectToPeer("localhost", 5001, "Hello from peer on port 5000");
                peer.connectToPeer("localhost", 5002, "Hello from peer on port 5000");
            }
            else if (port == 5001) {
                peer.connectToPeer("localhost", 5002, "Hello from peer on port 5001");
            }
            
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }
}
