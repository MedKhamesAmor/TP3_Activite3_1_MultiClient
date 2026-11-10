package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientProcess implements Runnable {
    private final Socket socket;
    private final int clientNumber;

    public ClientProcess(Socket socket, int clientNumber) {
        this.socket = socket;
        this.clientNumber = clientNumber;
    }

    @Override
    public void run() {
        try (
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter writer = new PrintWriter(socket.getOutputStream(), true)
        ) {
            // Envoyer à chaque client son numéro d'ordre de communication
            writer.println("Bienvenue ! Vous êtes le client n°" + clientNumber);
            writer.println("Tapez vos messages (ou 'quit'/'exit' pour quitter) :");

            String message;
            while ((message = reader.readLine()) != null) {
                if (message.equalsIgnoreCase("quit") || message.equalsIgnoreCase("exit") || message.equalsIgnoreCase("bye")) {
                    writer.println("Déconnexion du serveur. Au revoir !");
                    break;
                }
                System.out.println("[Client n°" + clientNumber + " - " + socket.getRemoteSocketAddress() + "] : " + message);
                // Réponse en echo
                writer.println("Serveur a reçu votre message : " + message);
            }
        } catch (IOException e) {
            System.err.println("Communication interrompue avec le client n°" + clientNumber + " : " + e.getMessage());
        } finally {
            try {
                socket.close();
                System.out.println("[Déconnexion] Client n°" + clientNumber + " a quitté.");
            } catch (IOException e) {
                System.err.println("Erreur lors de la fermeture de la socket client n°" + clientNumber);
            }
        }
    }
}
