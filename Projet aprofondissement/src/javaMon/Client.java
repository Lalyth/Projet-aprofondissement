package javaMon;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

/* Client pour Projet Approfondissement
 * Auteur : Philippe Lamarche & Alexandre Blouin
 * Date : 14 Mai 2020 */

public class Client {
	static Scanner scan;
	static DataInputStream in;
	static DataOutputStream out;
	static String monId;
	static int pv;

	static String messageRecu = "";

	static String nom = "";
	private static boolean vivant;

	static final String MENU = "Appuyer sur [1] pour attaquer";
	static final String ATT = "Appuyer sur [1] pour faire 1 de dégats, [2] pour faire 3 de dégats, [3] pour faire 5 de dégats";


	public static void main(String[] args) throws Exception {
		if (args.length != 1) {
			System.err.println("IP comme argument");
			return;
		}

		try (var socket = new Socket(args[0], 59090)) { // Initiate connection
			scan = new Scanner(System.in);
			in = new DataInputStream(socket.getInputStream());
			out = new DataOutputStream(socket.getOutputStream());
			monId = in.readUTF();
			pv = in.readInt();
			System.out.println("- - Connexion établie avec le serveur - - Vous êtes joueur #" + monId);
			System.out.println("PV : " + pv);
			System.out.println("Bonjour, quel sera votre nom ? ");

			nom = scan.nextLine();			
			out.writeUTF(nom);

			vivant = true;

			while (vivant) {			
				messageRecu = in.readUTF(); 
				if (messageRecu.startsWith(monId + " MENU")) {
					menu();
				}
				if (messageRecu.startsWith(monId + " PV")) {
					pv = Integer.parseInt(messageRecu.substring(5));
				}
			}

		}
	}

	private static void menu() {
		System.out.println(MENU);
		System.out.println("PV : " + pv);

		String choix = scan.nextLine();

		switch(choix) {
			case "1" :
				attaque();
				break;
			default:
				System.out.println("Choix valide svp");
		}

	}

	private static void attaque() {
		System.out.println(ATT);
		try {
			String choix = scan.nextLine();

			switch (choix) {
				case "1" :
					out.writeUTF("ATT " + monId + " 0");
					break;
				case "2" :
					out.writeUTF("ATT " + monId + " 1");
					break;
				case "3" :
					out.writeUTF("ATT " + monId + " 2");
					break;
				default : 
					System.out.println("");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}