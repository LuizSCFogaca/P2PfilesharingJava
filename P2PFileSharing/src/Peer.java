import java.io.*;
import java.net.*;
import java.lang.Thread;

public class Peer {
    private int port;
    private ServerSocket serverSocket;
    
    public Peer(int port) throws IOException{
        this.port = port;
        this.serverSocket = new ServerSocket(port);
    };

    public int getPort(){
        return port;
    };

    public void setPort(int port){
        this.port = port;
    };

    public ServerSocket getServerSocket(){
        return serverSocket;
    };

    public void setServerSocket(ServerSocket serverSocket){
        this.serverSocket = serverSocket;
    };

    public void connectToPeer(String host, int port){
        try(Socket socket = new Socket(host, port);
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
        BufferedReader in = new BufferedReader(new InputStreamReader((socket.getInputStream())))){
            out.println("Hello from peer on port: " + this.port);
            String response = in.readLine();
            System.out.println("Receive from another peer: " + response);
        }catch (IOException e){
            e.printStackTrace();
        }
    };

    public void start() throws IOException {
        System.out.println("peer started on port: "+ port);
        while(true){
            Socket clientSocket = serverSocket.accept();
            new Thread(new clientHandler(clientSocket)).start();
        }
    };

        public static void main(String[] args) {
        if(args.length != 1){
            System.out.println("Usage: java Peer <port>");
            return;
        }
        int port = Integer.parseInt(args[0]);
        try{
            Peer peer = new Peer(port);

        // Start server socket in a new thread
        new Thread(() -> {
            try {
                peer.start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();

        // Wait a bit to make sure the server starts
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Try to connect to the other peer
        if(port == 5000){
            peer.connectToPeer("localhost", 5001);
        }
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
