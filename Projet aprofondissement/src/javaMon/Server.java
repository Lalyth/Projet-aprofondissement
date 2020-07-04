package javaMon;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.Executors;

/* Serveur pour Projet Approfondissement
 * Auteur : Philippe Lamarche & Alexandre Blouin
 * Date : 14 Mai 2020 */

// ** Tout commentaire entre parenthèse provient des tooltips d'Eclipse ** \\

public class Server {
	static String messageRecu;
	static int nbrJoueurs;
	static final int[] ATTAQUE = {1,3,5};

	private static ArrayList<DataOutputStream> writers = new ArrayList<DataOutputStream>();
	private static ArrayList<Joueur> joueurs = new ArrayList<Server.Joueur>();

	public static void main(String[] args) throws Exception {
		try (var listener = new ServerSocket(59090)) {
			System.out.println("Server status : En marche");
			nbrJoueurs = 0;
			var pool = Executors.newFixedThreadPool(2); // Nombre de socket disponible dans le pool
			while (true) {
				nbrJoueurs++;
				pool.execute(new Joueur(listener.accept(), nbrJoueurs, 10)); // "Listens for a connection to be made to this socket and accepts it. The method blocks until a connection is made."	
			}

		}
	}

	private static class Joueur implements Runnable {
		private Socket socket; // "A socket is an endpoint for communication between two machines."
		private int id;
		private String nom;
		private int pv;
		private boolean vivant;

		public int getPv() {
			return pv;
		}

		public void setPv(int pv) {
			this.pv = pv;
		}

		public String toString() {
			return String.format("Nom : %S PV : %d, ID : %d", nom, pv, id);
		}

		Joueur(Socket socket, int id, int pv) { // Constructor qui va etre appelé pour créer un joueur
			this.socket = socket;
			this.id = id;
			this.pv = pv;
		}

		@Override
		public void run() {
			joueurs.add(this);
			System.out.println("Joueur #" + id + " se connecte - Connection avec: " + socket);

			try {
				var in = new DataInputStream(socket.getInputStream());
				var out = new DataOutputStream(socket.getOutputStream());
				writers.add(out); // Ajout du 'out' dans une liste pour pouvoir envoyer des messages à tous les joueurs
				out.writeUTF(Integer.toString(id));
				out.flush();
				out.writeInt(pv);
				out.flush();

				messageRecu = in.readUTF();
				nom = messageRecu;
				System.out.println(nom + " s'ajoute à la partie");				
				vivant = true;

				out.writeUTF("1 MENU");

				while (vivant) {
					messageRecu = in.readUTF();
					if (messageRecu.startsWith("ATT")) {
						if (messageRecu.substring(4).startsWith("1")) {
							int atkId = Integer.parseInt(messageRecu.substring(6));
							joueurs.get(1).setPv(joueurs.get(1).getPv() - ATTAQUE[atkId]);
							
							for (DataOutputStream o : writers) {
								o.writeUTF("2 PV " + joueurs.get(1).getPv());
								o.writeUTF("2 MENU");
							}
							
						} else if (messageRecu.substring(4).startsWith("2")) {
							int atkId = Integer.parseInt(messageRecu.substring(6));
							joueurs.get(0).setPv(joueurs.get(0).getPv() - ATTAQUE[atkId]);
							
							for (DataOutputStream o : writers) {
								o.writeUTF("1 PV " + joueurs.get(0).getPv());
								o.writeUTF("1 MENU");
							}
							
						}
					}
				}

			} catch (IOException e) {
				System.out.println("Erreur socket:" + socket);
				e.printStackTrace();
			} finally {
				try {
					socket.close();
				} catch (IOException e) {
				}
				System.out.println("Fermeture du socket: " + socket);
				nbrJoueurs--;
			}
		}
	}
}