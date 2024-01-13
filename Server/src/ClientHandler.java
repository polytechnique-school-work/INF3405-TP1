import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClientHandler extends Thread {
	private Socket socket;
	private int clientNumber;
	private boolean isLogged;
	private AccountHandler accountHandler;
	private static Map<Integer, ClientHandler> clients = new HashMap<Integer, ClientHandler>();
	private DataOutputStream out;
	private LoggerHandler logger;
	private String username;
	public ClientHandler(Socket socket, int clientNumber, LoggerHandler logger) {
		this.socket = socket;
		this.clientNumber = clientNumber;
		this.accountHandler = new AccountHandler();
		this.logger = logger;
		ClientHandler.clients.put(clientNumber, this);
		System.out.println("Nouvelle connection avec le client #" + clientNumber + " sur " + socket);
	}
	
	public void run() {
		try {
			
			// Envoyer des données au client
			out = new DataOutputStream(socket.getOutputStream());
			out.writeUTF("Connection au serveur effectuée, vous êtes le client #" + clientNumber);
			out.writeUTF("Afin de vous authentifier, envoyez votre utilisateur et votre mot de passe");
			out.writeUTF("sous le format suivant et ce sans espace: username,password");
			
		
			
			// Recevoir des données du client
			DataInputStream in = new DataInputStream(socket.getInputStream());
			
			while(true) {
				
				String message = in.readUTF();
			
				if(!isLogged) {
					String[] splittedAccount = message.split(",");
					String username = splittedAccount[0];
					String password = splittedAccount[1];
					if(!this.accountHandler.hasAccount(username)) {	
						out.writeUTF("<OK> Bienvenue dans le système!");
						this.accountHandler.createAccount(username, password);
					}
					
					isLogged = this.accountHandler.login(username, password);
					this.username = username;
					
					if(!isLogged) {
						out.writeUTF("<Error> Vous avez tapé un mauvais mot de passe."); 
					}
					else {
						// Envoie des 15 messages à l'utilisateur
						List<String> messages = this.logger.read(15);
						messages.forEach(t -> this.send(t));
					}
					return; // Est-ce que je peux faire ce return ou ça risque de faire des erreurs? À voir.
				}
				
				// Maintenant on gère la réception de message (formattage et envoie à tous).
				String formattedMessage = logger.formatMessage(username, socket.getInetAddress().toString(), message);
				this.logger.write(formattedMessage);
				ClientHandler.sendToAll(formattedMessage);
			}
			
		} catch (IOException e) {
			// TODO: handle exception
			System.out.println("Error handling client #" + clientNumber + ": " + e);
		} finally {
			try {
				socket.close();
				ClientHandler.clients.remove(clientNumber);
			} catch (IOException e) {
				// TODO: handle exception
				System.out.println("Cloudn't close a socket");
			}
			
			System.out.println("Connection with client #" + clientNumber + " closed.");
		}
	}
	
	public void send(String message) {
		try {
			out.writeUTF(message);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void sendToAll(String message) {
		ClientHandler.clients.forEach((clientNumber, clientHandler) -> {
			clientHandler.send(message);
		});
	}

}
