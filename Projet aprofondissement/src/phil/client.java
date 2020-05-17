package phil;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/* Client pour Projet Approfondissement
 * Auteur : Philippe Lamarche & Alexandre Blouin
 * Date : 14 Mai 2020 */

public class client {
	static Scanner scan;
	static Scanner in;
	static PrintWriter out;
	static String messageRecu = "";

	static final String NEW = "Bonjour, quel est votre nom ?";
	static String nom = "";

	static final String MENU = "Appuyer sur 1 pour attaquer, 2 pour notCodedYet, 3 pour notCodedYet";
	static final String ATT = "Appuyer sur 1 pour faire 5 de dégats, 2 pour crash le programme, 3 pour s'auto DDOS";

	public static void main(String[] args) throws Exception {
		if (args.length != 1) {
			System.err.println("IP comme argument");
			return;
		}

		try (var socket = new Socket(args[0], 59090)) {
			System.out.println("- - Connexion établie avec le serveur - -");
			scan = new Scanner(System.in);
			in = new Scanner (socket.getInputStream());
			out = new PrintWriter(socket.getOutputStream(), true);

			// Ce que le programme fait
			while (in.hasNextLine()) {
				messageRecu = in.nextLine();

				if (messageRecu.startsWith("NEW")) {
					System.out.println(NEW);
					nom = scan.nextLine();			
					out.println("NOM" + nom);
				}

				if (messageRecu.startsWith("MENU")) {
					menu();
				}
			}
		}
	}

	private static void menu() {
		System.out.println(MENU);

		int choix = scan.nextInt();
		
		switch(choix) {
			case 1:
				attaque();
				break;
			case 2:
				equipe();
				break;
			case 3:
				item();
			default:
				System.out.println("Choix valide svp");
		}
		
	}

	private static void attaque() {
		System.out.println(ATT);

		int choix = scan.nextInt();

		switch (choix) {
			case 1 :
// enlever 5 dmg a l'autre
				break;
			case 2 :

				break;
			case 3 :

				break;
			default : 
				System.out.println("");
		}
	}

	private static void equipe() {

	}

	private static void item() {

	}




} 


