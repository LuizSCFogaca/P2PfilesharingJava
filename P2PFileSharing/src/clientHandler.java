import java.io.*;
import java.net.*;

public class clientHandler implements Runnable {
    private Socket socket;

    public clientHandler(Socket socket){
        this.socket = socket;
    };

    public void run(){
        try(BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true)){
            String message;
            while((message = in.readLine()) != null){
                System.out.println("Received: " + message);
                out.println(message);
            }
        }catch(IOException e){
            e.printStackTrace();
        }finally{
            try{
                socket.close();
            }catch(IOException e){
                e.printStackTrace();
            }
        }
    }
}