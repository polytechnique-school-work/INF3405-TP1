import java.io.DataInputStream;
import java.net.Socket;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Client {
	private static Socket socket;
	
	public static void main (String[] args) throws Exception {
		//Instantiation de la lecture des entrées
		Scanner inputReader = new Scanner(System.in);
		
		// Adresse IP
		boolean isFormat = false;
		String serverAddress = "";
		while(!isFormat) {
			System.out.println("Veuillez entrer votre adresse IP :");
			serverAddress = inputReader.nextLine();
			
			Pattern ipv4 = Pattern.compile("^(\\d{1,3}\\.){3}\\d{1,3}$");
			Matcher ipMatcher = ipv4.matcher(serverAddress);
			isFormat = ipMatcher.matches();
			
			String[] octets = serverAddress.split("\\.");
			
			for(String octet : octets) {
				Integer number = Integer.parseInt(octet);
				if(number > 255) {
					isFormat = false;
				}
			}
			
			if(!isFormat) {
				System.out.println("Vous devez entrer une adresse IPv4 valide!");
			}
		}
		
		// Port
		
		boolean isValid = false;
		int port = 0;
		
		while(!isValid) {
			System.out.println("Veuillez entrer le port auquel vous voulez accéder :");
			port = inputReader.nextInt();
			
			if(port >= 5000 && port <= 5050) {
				isValid = true;
			}
			else {
				System.out.println("Le port doit être entre 5000 et 5050");
			}
		}
		
		
		// Création d'une nouvelle connexion aves le serveur
		socket = new Socket(serverAddress, port);
		System.out.format("Serveur lancé sur [%s:%d]", serverAddress, port);
		
		// Création d'un canal entrant pour recevoir les messages envoyés, par le serveur
		DataInputStream in = new DataInputStream(socket.getInputStream());
		
		// Attente de la réception d'un message envoyé par le, server sur le canal
		String helloMessageFromServer = in.readUTF();
		System.out.println(helloMessageFromServer);
		
		// fermeture de La connexion avec le serveur
		socket.close();
	}
}
