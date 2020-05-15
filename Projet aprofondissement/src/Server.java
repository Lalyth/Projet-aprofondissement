/* Serveur pour Projet Approfondissement
 * Auteur : Philippe Lamarche & Alexandre Blouin
 * Date : 14 Mai 2020 */

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;
import java.util.concurrent.Executors;

// Tout commentaire entre parenthèse provient des tooltips d'Eclipse

public class Server {
	static int total = 0;

	private static Set<PrintWriter> writers = new HashSet<>();

	public static void main(String[] args) throws Exception {
		try (var listener = new ServerSocket(59090)) {
			System.out.println("Elserveurrironne..");
			var pool = Executors.newFixedThreadPool(20); // Nombre de socket disponible dans le pool

			while (true) {
				pool.execute(new NomDeLaClasseDuProgrammeDansLeServeur(listener.accept())); // "Listens for a connection to be made to this socket and accepts it. The method blocks until a connection is made."
			}

		}
	}

	private static class NomDeLaClasseDuProgrammeDansLeServeur implements Runnable {
		private Socket socket; // "A socket is an endpoint for communication between two machines."
		private PrintWriter out;
		
		NomDeLaClasseDuProgrammeDansLeServeur(Socket socket) { // Constructor qui va etre appelé pour assigner un socket a une connexion (listener)
			this.socket = socket; 
		}

		public void run() {
			System.out.println("Connection avec: " + socket);

			try {
				var in = new Scanner(socket.getInputStream()); // Crée l'objet in qui recoit les messages du client
				out = new PrintWriter(socket.getOutputStream(), true); // Crée l'objet out qui envoie les messages au client

				writers.add(out); // Ajout du out dans une liste pour pouvoir envoyer des messages broadcast
				
				// Action du programme
				while (in.hasNextLine()) {
					// while (true) {
					String messageRecu = in.nextLine(); // Recoit la ligne de texte 
					System.out.println(messageRecu);
					
					if (messageRecu.startsWith("ADD")) {
						int intA = Integer.parseInt(messageRecu.substring(4));
						total = total + intA;
						System.out.println("Total est: " + total);

						for (PrintWriter writer : writers) {
							writer.println("Nouveau total est: " + total);
						}

					} else {
						out.println(messageRecu); // Rien pour le recevoir
					}

				}
			} catch (IOException e) { // InputOutput Stream Exeception
				System.out.println("Erreur socket:" + socket);
				e.printStackTrace();
			} finally {
				try {
					socket.close();
				} catch (IOException e) { 
				}
				System.out.println("Fermeture du socket: " + socket);
			}
		}
	}
}
