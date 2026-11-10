package client;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) {
        String host = "127.0.0.1";
        int port = 1234;

        if (args.length >= 1) {
            host = args[0];
        }
        if (args.length >= 2) {
            port = Integer.parseInt(args[1]);
        }

        System.out.println("====================================================");
        System.out.println(" Client TCP - Connexion au Serveur");
        System.out.println("====================================================");

        try {
            // Utilisation de InetAddress et InetSocketAddress comme demandé
            InetAddress serverAddress = InetAddress.getByName(host);
            InetSocketAddress socketAddress = new InetSocketAddress(serverAddress, port);

            Socket socket = new Socket();
            System.out.println("Tentative de connexion à " + host + ":" + port + "...");
            socket.connect(socketAddress, 5000); // 5 sec timeout
            System.out.println("Connecté avec succès au serveur !");

            BufferedReader serverReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter serverWriter = new PrintWriter(socket.getOutputStream(), true);
            Scanner consoleScanner = new Scanner(System.in);

            // Lecture du message d'accueil et du numéro d'ordre
            String welcome = serverReader.readLine();
            System.out.println("[Serveur] " + welcome);
            String instructions = serverReader.readLine();
            System.out.println("[Serveur] " + instructions);

            // Thread ou boucle d'envoi de messages
            while (true) {
                System.out.print("\nEntrez un message : ");
                if (!consoleScanner.hasNextLine()) break;
                String userMessage = consoleScanner.nextLine();

                serverWriter.println(userMessage);

                String response = serverReader.readLine();
                if (response == null) {
                    System.out.println("Le serveur a fermé la connexion.");
                    break;
                }
                System.out.println("[Réponse Serveur] : " + response);

                if (userMessage.equalsIgnoreCase("quit") || userMessage.equalsIgnoreCase("exit") || userMessage.equalsIgnoreCase("bye")) {
                    break;
                }
            }

            socket.close();
            consoleScanner.close();
            System.out.println("Connexion terminée.");

        } catch (Exception e) {
            System.err.println("Erreur de connexion client : " + e.getMessage());
        }
    }
}
