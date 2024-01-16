import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Client {
	private static Socket socket;
	
	public static void main (String[] args) throws Exception {
		
		InputValidator inputValidator = new InputValidator();
		ServerConnector serverConnector = new ServerConnector();
		
		// Se connecte au serveur
		Socket socket = serverConnector.connectToServer(inputValidator);
				
		// Création d'un canal entrant pour recevoir les messages envoyés, par le serveur
		DataInputStream in = new DataInputStream(socket.getInputStream());
		DataOutputStream out = new DataOutputStream(socket.getOutputStream());
		
		
		// Attente de la réception d'un message envoyé par le, server sur le canal
		//while(true) {
		//	String helloMessageFromServer = in.readUTF();
		//	System.out.println(helloMessageFromServer);
		//}
		
		// fermeture de La connexion avec le serveur
		//socket.close();
	}
	
}
