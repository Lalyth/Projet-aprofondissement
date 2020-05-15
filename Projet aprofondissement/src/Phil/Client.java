package Phil;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

/* Client pour Projet Approfondissement
 * Auteur : Philippe Lamarche & Alexandre Blouin
 * Date : 14 Mai 2020 */

public class Client {

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
			System.out.println("Écrit quelque chose (ADD pour ajouter au total)");
			//while (scan.hasNextLine()) {	
			while (scan.hasNextLine()) {
				out.println(scan.nextLine()); // Envoie une ligne de texte au serveur
				//while (in.hasNextLine()) {
					System.out.println(in.nextLine());
				//}
			}
			scan.close();
		}

	}

}



