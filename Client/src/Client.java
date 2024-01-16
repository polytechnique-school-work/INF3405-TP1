import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;

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
		
		String connexionMessage = in.readUTF();
		System.out.println(connexionMessage);
		
		MessageHandler messageHandler = new MessageHandler(in, out);
		
		messageHandler.authentification(inputValidator);
				
		
		// fermeture de La connexion avec le serveur
		//socket.close();
	}
	
}
