import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class ClientTuto {

	public static void main(String[] args) throws IOException {
		
		if (args.length != 1) {
			System.out.println("Le ip comme argument quand on le run");
			return;
		}
		
		var socket = new Socket(args[0], 59090);
		var in = new Scanner(socket.getInputStream());
		System.out.println("Server response: " + in.nextLine());
	}
}
