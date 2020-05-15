import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class TicTacToeClientTuto {

	private JFrame frame = new JFrame("Tic Tac Toe");
	private JLabel messageLabel = new JLabel("...");

	private Square[] board = new Square[9];
	private Square currentSquare;

	private Socket socket;
	private Scanner in;
	private PrintWriter out;

	public TicTacToeClientTuto(String serverAddress) throws Exception {

		socket = new Socket(serverAddress, 59090);
		in = new Scanner(socket.getInputStream());
		out = new PrintWriter(socket.getOutputStream(), true);

		messageLabel.setBackground(Color.lightGray);
		frame.getContentPane().add(messageLabel, BorderLayout.SOUTH);

		var boardPanel = new JPanel();
		boardPanel.setBackground(Color.black);
		boardPanel.setLayout(new GridLayout(3, 3, 2, 2));

		for (var i = 0; i < board.length; i++) {
			final int j = i;
			board[i] = new Square();
			board[i].addMouseListener(new MouseAdapter() {
				public void mousePressed(MouseEvent e) {
					currentSquare = board[j];
					out.println("MOVE " + j);
				}
			});
			boardPanel.add(board[i]);
		}
		frame.getContentPane().add(boardPanel, BorderLayout.CENTER);
	}

	public void play() throws Exception {
		try {
			var response = in.nextLine();
			var mark = response.charAt(8);
			var opponentMark = mark == 'X' ? 'O' :'X';
			frame.setTitle("Tic Tac Toe: Player " + mark);

			while (in.hasNextLine()) {
				response = in.nextLine();

				if (response.startsWith("VALID_MOVE")) {
					currentSquare.setText(mark);
					currentSquare.repaint();
				} else if (response.startsWith("OPPONENT_MOVED")) {
					var loc = Integer.parseInt(response.substring(15));
					board[loc].setText(opponentMark);
					board[loc].repaint();
					messageLabel.setText("L'adversaire a joué. À votre tour.");
				} else if (response.startsWith("MESSAGE")) {
					messageLabel.setText(response.substring(8));
				} else if (response.startsWith("VICTORY")) {
					JOptionPane.showMessageDialog(frame, "Winner Winner");
					break;
				} else if (response.startsWith("DEFEAT")) {
					JOptionPane.showMessageDialog(frame, "Sorry you lost.");
					break;
				} else if (response.startsWith("TIE")) {
					JOptionPane.showMessageDialog(frame, "Tie");
					break;
				} else if (response.startsWith("OTHER_PLAYER_LEFT")) {
					JOptionPane.showMessageDialog(frame, "L'adversaire a quitté.");
					break;
				}
			}
			out.println("QUIT");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			socket.close();
			frame.dispose();
		}
	}

	static class Square extends JPanel {
		
		private static final long serialVersionUID = 1L;
		JLabel label = new JLabel();

		public Square() {
			setBackground(Color.white);
			setLayout(new GridBagLayout());
			label.setFont(new Font("Arial", Font.BOLD, 40));
			add(label);
		}

		public void setText(char text) {
			label.setForeground(text == 'X' ? Color.BLUE : Color.RED);
			label.setText(text + "");
		}
	}

	public static void main(String[] args) throws Exception {
		
		if (args.length != 1) {
			System.err.println("Ip comme argument");
			return;
		}
		TicTacToeClientTuto client = new TicTacToeClientTuto(args[0]);
		client.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		client.frame.setSize(320, 320);
		client.frame.setVisible(true);
		client.frame.setResizable(false);
		client.play();
	}
}
