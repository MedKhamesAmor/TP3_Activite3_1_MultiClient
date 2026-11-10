package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class MultiThreadServer {
    public static final int PORT = 1234;

    public static void main(String[] args) {
        int clientCount = 0;
        System.out.println("====================================================");
        System.out.println(" Démarrage du Serveur Multi-thread TCP (Port " + PORT + ")");
        System.out.println("====================================================");

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Serveur en attente de connexions clientes...");

            while (true) {
                // Attente d'une nouvelle connexion cliente
                Socket socket = serverSocket.accept();
                clientCount++;

                // Affichage côté serveur des informations du client connecté
                System.out.println("\n[Connexion acceptée]");
                System.out.println(" -> Client n°          : " + clientCount);
                System.out.println(" -> Adresse IP & Port  : " + socket.getRemoteSocketAddress());

                // Lancement d'un thread dédié pour traiter ce client
                ClientProcess process = new ClientProcess(socket, clientCount);
                Thread thread = new Thread(process);
                thread.start();
            }
        } catch (IOException e) {
            System.err.println("Erreur au niveau du serveur : " + e.getMessage());
        }
    }
}
