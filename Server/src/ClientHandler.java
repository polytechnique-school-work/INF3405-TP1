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
	private int port;
	public ClientHandler(Socket socket, int port, int clientNumber, LoggerHandler logger) {
		this.port = port;
		this.socket = socket;
		this.clientNumber = clientNumber;
		this.accountHandler = new AccountHandler();
		this.logger = logger;
		ClientHandler.clients.put(clientNumber, this);
		System.out.println("Nouvelle connection avec le client #" + clientNumber + " sur " + socket);
	}
	
	public void run() {
		try {
			
			// Envoie des données d'annonce de connection au nouveau client
			out = new DataOutputStream(socket.getOutputStream());
			out.writeUTF("Connection au serveur effectuée, vous êtes le client #" + clientNumber);
			
			// Permet de recevoir des données des clients
			DataInputStream in = new DataInputStream(socket.getInputStream());
			
			// Boucle pour qu'à chaque fois qu'on reçoit un nouveau message,
			// qu'il soit traité, en fonction de si le client est connecté ou non.
			while(true) {
				
				String message = in.readUTF();
			
				// Dans le cas où l'utiliseur n'est pas encore login, on vérifie
				// si ce qu'il envoie est conforme à la demande.
				if(!isLogged) {
					String[] splittedAccount = message.split(",");
					String username = splittedAccount[0];
					String password = splittedAccount[1];
					
					// S'il n'a pas encore de compte, on en crée un nouveau
					// avec les informations donnés
					if(!this.accountHandler.hasAccount(username)) {	
						out.writeUTF("<OK> Compte créé, bienvenue dans le système!");
						this.accountHandler.createAccount(username, password);
					}
					
					// On vérifie dans tous les cas si c'est possible de se login
					isLogged = this.accountHandler.login(username, password);
					
					if(!isLogged) {
						// Impossible de se login (mauvais mot de passe)
						out.writeUTF("<Error> Erreur dans la saisie du mot de passe."); 
						continue;
					} else {
						// Login réussit, on set son nom d'utilisateur
						// On lui envoie les 15 derniers messages

						this.username = username;
						out.writeUTF("<OK> Bonjour, " + username);
						
						List<String> messages = this.logger.read(15);
						messages.forEach(t -> this.send(t));
						
						this.send("Bienvenue dans le serveur de clavardage. Pour terminer la connexion au serveur, écrivez \"/disconnect\"");
					}
					continue;
				}
				
				// L'utilisateur est logged, tout ce qu'il envoie est donc des messages
				// On peut donc formatter son message et l'envoyer à tous les autres
				// utilisateurs qui sont connectés.
				
				if(message == "%disconnect%") {
					this.disconnect();
					return;
				}
				
				String formattedMessage = logger.formatMessage(username, port, socket.getInetAddress().toString(), message);
				this.logger.write(formattedMessage);
				ClientHandler.sendToAll(formattedMessage);
			}
			
		} catch (IOException e) {
			System.out.println("Error handling client #" + clientNumber + ": " + e);
		} finally {
			// Déconnection du client, on le retire de la liste des clients.
			this.disconnect();
			// Déconnection effectuée avec succès.
			System.out.println("Connection with client #" + clientNumber + " closed.");
		}
	}
	
	/*
	 * Permet d'envoyer un message à l'utilisateur
	 * @param message Message à envoyer
	 * @return
	 * */
	public void send(String message) {
		try {
			out.writeUTF(message);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/*
	 * Permet d'envoyer un message à l'ensemble des utilisateurs
	 * @param message Message à envoyer
	 * @return
	 * */
	public static void sendToAll(String message) {
		ClientHandler.clients.forEach((clientNumber, clientHandler) -> {
			clientHandler.send(message);
		});
	}
	
	/*
	 * Permet de déconnecter un utilisateur comme il faut.
	 * @return
	 * */
	private void disconnect() {
		String disconnectMessage = "<Server> " + username + " disconnected.";
		this.logger.write(disconnectMessage);
		ClientHandler.sendToAll(disconnectMessage);
		try {
			this.socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		ClientHandler.clients.remove(clientNumber);
	}

}
