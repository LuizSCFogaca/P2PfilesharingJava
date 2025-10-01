import java.io.*;
import java.util.*;

public class Main {
         public static void main(String[] args) {

        try {
            int port = Integer.parseInt(args[0]);
            String peersFile = args[1];
            String directory = args[2];

            // Carrega os endereços dos outros peers
            Set<String> knownPeers = new HashSet<>();
            try (BufferedReader br = new BufferedReader(new FileReader(peersFile))) {
                String line;
                while ((line = br.readLine()) != null) {
                    if (!line.trim().isEmpty()) {
                        knownPeers.add(line.trim());
                    }
                }
            }

            // Inicializa o nó da rede
            Peer selfNode = new Peer(port, directory, knownPeers);
            selfNode.startListening();

            // Interface de comando para o usuário
            System.out.println("--------------------------------------------------");
            System.out.println("FileSync App iniciado. Digite 'share <arquivo>' ou 'delete <arquivo>'.");
            Scanner commandScanner = new Scanner(System.in);
            while (true) {
                String input = commandScanner.nextLine();
                String[] parts = input.split(" ", 2);
                String command = parts[0];

                if (parts.length < 2) {
                    System.out.println("Comando inválido. Use o formato: <comando> <nome_do_arquivo>");
                    continue;
                }
                String fileName = parts[1];

                if ("share".equalsIgnoreCase(command)) {
                    selfNode.shareFileWithPeers(fileName);
                } else if ("delete".equalsIgnoreCase(command)) {
                    selfNode.requestFileDeletion(fileName);
                } else {
                    System.out.println("Comando desconhecido: " + command);
                }
            }

        } catch (Exception e) {
            System.err.println("Ocorreu um erro ao iniciar a aplicação: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
