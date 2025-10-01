import java.io.*;
import java.net.*;
import java.lang.Thread;
import java.util.*;
import java.nio.file.*;

public class Peer {
    private int port;
    private DatagramSocket datagramsocket;
    private String sharedDirectory;
    private Set<String> knownPeersAddresses;

    public Peer(int port, String sharedDirectory, Set<String> knownPeersAddresses) throws IOException{
        this.port = port;
        this.datagramsocket = new DatagramSocket(port);
        this.sharedDirectory = sharedDirectory;
        this.knownPeersAddresses = knownPeersAddresses;
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

    public void startListening() {
        Thread listenerThread = new Thread(this::listenForMessages);
        listenerThread.start();
    };

    private void listenForMessages() {
        System.out.println("Nó iniciado e ouvindo na porta: " + port);
        byte[] buffer = new byte[65507]; // Tamanho máximo de um pacote UDP

        while (true) {
            try {
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                datagramsocket.receive(packet); // Bloqueia até receber um pacote
                processIncomingPacket(packet);
            } catch (IOException e) {
                System.err.println("Erro de I/O no listener: " + e.getMessage());
            }
        }
    };
    
    private void processIncomingPacket(DatagramPacket packet) {
        String message = new String(packet.getData(), 0, packet.getLength());

        if (message.startsWith("SHARE_FILE:")) {
            handleFileSharing(message);
        } else if (message.startsWith("DELETE_FILE:")) {
            handleFileDeletion(message);
        }
    };

    private void handleFileSharing(String message) {
        try {
            String[] parts = message.split(":", 3);
            if (parts.length == 3) {
                String fileName = parts[1];
                byte[] fileContent = parts[2].getBytes();

                Path filePath = Paths.get(sharedDirectory, fileName);
                Files.write(filePath, fileContent);
                System.out.println("Arquivo recebido e salvo: " + fileName);
            }
        } catch (IOException e) {
            System.err.println("Falha ao salvar arquivo recebido: " + e.getMessage());
        }
    };

    public void handleFileDeletion(String message) {
        try {
            String[] parts = message.split(":", 2);
            if (parts.length == 2) {
                String fileName = parts[1];
                Path filePath = Paths.get(sharedDirectory, fileName);
                Files.deleteIfExists(filePath);
                System.out.println("Arquivo deletado: " + fileName);
            }
        } catch (IOException e) {
            System.err.println("Falha ao deletar arquivo: " + e.getMessage());
        }
    };

    public void shareFileWithPeers(String fileName) {
        try {
            Path filePath = Paths.get(sharedDirectory, fileName);
            if (!Files.exists(filePath)) {
                System.err.println("Arquivo não encontrado para envio: " + fileName);
                return;
            }
            byte[] content = Files.readAllBytes(filePath);
            String header = "SHARE_FILE:" + fileName + ":";
            byte[] message = (header + new String(content)).getBytes();
            broadcastMessage(message);
            System.out.println("Arquivo '" + fileName + "' compartilhado com a rede.");
        } catch (IOException e) {
            System.err.println("Não foi possível ler ou enviar o arquivo: " + e.getMessage());
        }
    };

    public void requestFileDeletion(String fileName) {
        String message = "DELETE_FILE:" + fileName;
        broadcastMessage(message.getBytes());
        System.out.println("Solicitação de deleção para '" + fileName + "' enviada à rede.");
    };


    private void broadcastMessage(byte[] message) {
        for (String peerAddress : knownPeersAddresses) {
            try {
                String[] parts = peerAddress.split(":");
                InetAddress address = InetAddress.getByName(parts[0]);
                int targetPort = Integer.parseInt(parts[1]);

                DatagramPacket packet = new DatagramPacket(message, message.length, address, targetPort);
                datagramsocket.send(packet);

            } catch (Exception e) {
                System.err.println("Falha ao enviar mensagem para " + peerAddress + ": " + e.getMessage());
            }
        }
    };
    
}
