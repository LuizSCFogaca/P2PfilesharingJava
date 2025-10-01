## Getting Started

Welcome to the VS Code Java world. Here is a guideline to help you get started to write Java code in Visual Studio Code.

## Folder Structure

The workspace contains two folders by default, where:

- `src`: the folder to maintain sources
- `lib`: the folder to maintain dependencies

Meanwhile, the compiled output files will be generated in the `bin` folder by default.

## Para rodar o código é necessário:

========= Preparação (Apenas uma vez) =========

    - Crie as pastas para os peers: tmp/peer1, tmp/peer2, tmp/peer3.

    - Crie a pasta Peers e, dentro dela, os arquivos peer1.txt, peer2.txt e peer3.txt com os endereços dos outros peers (ex: 127.0.0.1:5001).

========= Compilação (Apenas se alterar o código) =========

    - Navegue no terminal até à pasta P2PFileSharing.

    - Execute: javac -d bin src/Main.java src/Peer.java

    - Execução (Toda vez que quiser usar):

    - Abra três terminais separados, um para cada peer.

    - Em cada terminal, navegue até a pasta P2PFileSharing.

========= Execute os seguintes comandos(um em cada terminal) =========

    - Terminal 1: java -cp bin Main 5000 Peers/peer1.txt tmp/peer1

    - Terminal 2: java -cp bin Main 5001 Peers/peer2.txt tmp/peer2

    - Terminal 3: java -cp bin Main 5002 Peers/peer3.txt tmp/peer3

Após a execução, os três peers estarão online e prontos para sincronizar arquivos.

========= Para testar com arquivos =========
    
    - Abra um quarto terminal para criar um .txt

    - Utilize o código: echo "Ficheiro de teste P2P" > C:\ ... \P2PFileSharing\tmp\peer1\oi.txt

    - No lugar dos 3 pontos coloque o caminho correto do seu computador

    - Utilize o comando share oi.txt no terminal 1 para compartilhar com os outros e o comando delete oi.txt para deletar
