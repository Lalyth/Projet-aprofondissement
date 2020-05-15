package alex;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

/* Client pour Projet Approfondissement
 * Auteur : Philippe Lamarche & Alexandre Blouin
 * Date : 14 Mai 2020 */

public class testclient {

	public static void main(String[] args) throws Exception {
		if (args.length != 1) {
			System.err.println("IP comme argument");
			return;
		}

		try (var socket = new Socket(args[0], 59090)) {
			System.out.println("Connexion établie avec le serveur");
			var scan = new Scanner(System.in);
			var in = new Scanner (socket.getInputStream());
			var out = new PrintWriter(socket.getOutputStream(), true);

			// Ce que le programme fait
			//System.out.println("Écrit quelque chose (ADD pour ajouter au total)");
			while (in.hasNextLine()) {	
				String messageRecu = in.nextLine();
				//out.println(scan.nextLine()); // Envoie une ligne de texte au serveur
				if (messageRecu.startsWith("NEW")) {
					messageRecu = messageRecu.substring(4);
					System.out.println(messageRecu);
					out.println(scan.nextLine());
				}
				if (messageRecu.startsWith("MESSAGE")) {
					messageRecu = messageRecu.substring(8);
					System.out.println(messageRecu);
				}
				if (messageRecu.startsWith("PROMPT")) {
					messageRecu = messageRecu.substring(7);
					System.out.println(messageRecu);
					out.println(scan.nextLine());
				}
				//System.out.println(in.nextLine());
			}
			scan.close();
		}

	}

}



