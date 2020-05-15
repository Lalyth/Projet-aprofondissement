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
	static String messageBroad = "";

	static Executor pool = Executors.newFixedThreadPool(5);

	public static void main(String[] args) throws Exception {
		if (args.length != 1) {
			System.err.println("IP comme argument");
			return;
		}

		try (var socket = new Socket(args[0], 59090)) {
			System.out.println("Connexion établie avec le serveur");
			scan = new Scanner(System.in);
			in = new Scanner (socket.getInputStream());
			out = new PrintWriter(socket.getOutputStream(), true);

			// Ce que le programme fait
			pool.execute(new ecouteBroadcast());

			while (in.hasNextLine()) {	
				messageRecu = in.nextLine();
				
				if (messageRecu.startsWith("MESSAGE")) {
					messageBroad = messageRecu.substring(8);
				}

				if (messageRecu.startsWith("NEW")) {
					messageRecu = messageRecu.substring(4);
					System.out.println(messageRecu);
					out.println(scan.nextLine());
				}

				if (messageRecu.startsWith("PROMPT")) {
					messageRecu = messageRecu.substring(7);
					System.out.println(messageRecu);
					out.println(scan.nextLine());
				}

			}

			scan.close();
		}
	}

	private static class ecouteBroadcast implements Runnable {

		@Override
		public void run() {

			while (true) {	
				if (!client.messageBroad.isEmpty()) {
					System.out.println(client.messageBroad);
					client.messageBroad = "";
				}
			}
		}
	} 


}