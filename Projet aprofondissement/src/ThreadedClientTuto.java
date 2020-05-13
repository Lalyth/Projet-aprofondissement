import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class ThreadedClientTuto {

	public static void main(String[] args) throws Exception {
		if (args.length != 1) {
			System.err.println("IP comme argument");
			return;
		}
		try (var socket = new Socket(args[0], 59090)) {
			System.out.println("Entrer du texte. CTRL + D ou CTRL + C pour quitter.");
			var scanner = new Scanner(System.in);
			var in = new Scanner (socket.getInputStream());
			var out = new PrintWriter(socket.getOutputStream(), true);

			while (scanner.hasNextLine()) {
				out.println(scanner.nextLine());
				System.out.println(in.nextLine());
			}
			scanner.close();
		}

	}


}
