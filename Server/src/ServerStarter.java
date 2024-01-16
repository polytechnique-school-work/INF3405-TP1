import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;

public class ServerStarter {
	public ServerSocket connect(InputValidator inputValidator, ServerSocket listener) {
		
		String checkIPRegex = "^((25[0-5]|(2[0-4]|1\\d|[1-9]|)\\d)\\.?\\b){4}$";
		String checkPort = "^50[0-4][0-9]|5050$";
		
		String serverAddress = inputValidator.validate("Vous devez entrer l'adresse IP du poste (ipv4):", checkIPRegex);
		String strServerPort = inputValidator.validate("Vous devez entrer le port ([5000,5050]):", checkPort);
		
		System.out.println("Démarrage du serveur...");
		
		int serverPort = Integer.parseInt(strServerPort);
		
		try {
			InetAddress serverIP = InetAddress.getByName(serverAddress);
			listener.bind(new InetSocketAddress(serverIP, serverPort));
			System.out.format("Le serveur est démarré sur %s:%d%n", serverAddress, serverPort);
		}
		catch(Exception e){
			System.out.println("Il y a une erreur dans la création du serveur, veuillez recommencer");
			connect(inputValidator, listener);
		}

		return listener;
	}
}
