import java.net.ServerSocket;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;

public class Server {

	private static ServerSocket Listener;
	public static void main(String[] args) {
		
		// Différents regex récupérés un peu partout sur internet, par exemple : 
		// https://stackoverflow.com/questions/5284147/validating-ipv4-addresses-with-regexp
		
		// String checkUsername = "^[A-Za-z0-9]+$";
		// String checkPassword = "^[A-Za-z0-9\\p{Punct}]+$";
		
		// Initialisation de certains éléments
		InputValidator inputValidator = new InputValidator();
		LoggerHandler loggerHandler = new LoggerHandler();
		ServerStarter serverStarter = new ServerStarter();
		
		// Enregistrement de l'adresse IP et du port de connection.
		
		// String username = inputValidator.validate("Vous devez entrer un nom d'utilisateur (Seulement des chiffres ou nombres):", checkUsername);
		// String password = inputValidator.validate("Vous devez entrer un mot de passe (Chiffre, nombre et caractères spéciaux, sans espace):", checkPassword);	
		
		int clientNumber = 0;
		
		try {
			Listener = new ServerSocket();
			Listener = serverStarter.connect(inputValidator, Listener);
			Listener.setReuseAddress(true);
			
			while(true) {
				new ClientHandler(Listener.accept(), clientNumber++, loggerHandler).start();
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				// Est-ce qu'on devrait envoyer à tout le monde que le serveur s'est fermé?
				Listener.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		
	}
}
