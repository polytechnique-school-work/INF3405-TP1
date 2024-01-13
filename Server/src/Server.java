import java.net.ServerSocket;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;

public class Server {

	private static ServerSocket Listener;
	public static void main(String[] args) {
		
		// Différents regex récupérés un peu partout sur internet, par exemple : 
		// https://stackoverflow.com/questions/5284147/validating-ipv4-addresses-with-regexp
		String checkIPRegex = "^((25[0-5]|(2[0-4]|1\\d|[1-9]|)\\d)\\.?\\b){4}$";
		String checkPort = "^50[0-4][0-9]|5050$";
		String checkUsername = "^[A-Za-z0-9]+$";
		String checkPassword = "^[A-Za-z0-9\\p{Punct}]+$";
		
		InputValidator inputValidator = new InputValidator();
		
		String ipAddress = inputValidator.validate("Vous devez entrer l'adresse IP du poste (ipv4):", checkIPRegex);
		String port = inputValidator.validate("Vous devez entrer le port ([5000,5050]):", checkPort);
		String username = inputValidator.validate("Vous devez entrer un nom d'utilisateur (Seulement des chiffres ou nombres):", checkUsername);
		String password = inputValidator.validate("Vous devez entrer un mot de passe (Chiffre, nombre et caractères spéciaux, sans espace):", checkPassword);
		
		
		
	
		
		/*int clientNumber = 0;
		String serverAddress = "127.0.0.1";
		int serverPort = 5000;
		
		try {
			Listener = new ServerSocket();
			Listener.setReuseAddress(true);
			InetAddress serverIP = InetAddress.getByName(serverAddress);
			Listener.bind(new InetSocketAddress(serverIP, serverPort));
			System.out.format("The server is running on %s:%d%n", serverAddress, serverPort);
			
			while(true) {
				new ClientHandler(Listener.accept(), clientNumber++).start();
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				Listener.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		*/
	}
}
