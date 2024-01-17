import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.util.Scanner;

public class Client {
	
	public static void main (String[] args) throws Exception {
		
		InputValidator inputValidator = new InputValidator();
		ServerConnector serverConnector = new ServerConnector();
		
		Socket socket = serverConnector.connectToServer(inputValidator);
				
		DataInputStream in = new DataInputStream(socket.getInputStream());
		DataOutputStream out = new DataOutputStream(socket.getOutputStream());
		Scanner scanner = new Scanner(in);
		
		String connexionMessage = in.readUTF();
		System.out.println(connexionMessage);
		
		MessageHandler messageHandler = new MessageHandler(in, out);
		
		messageHandler.authentification(inputValidator);
		
		// Partie Chatroom (ne fonctionne pas)
		while(true) {
			try {

				String messageRecu = in.readUTF();
				if(messageRecu != "") {
					System.out.println(messageRecu);
				}
				String messageEnvoyé = scanner.nextLine();
				messageHandler.sendMessage(messageEnvoyé);
			}
			catch(Exception e) {
				break;
			}
		}
		
		if(scanner != null) scanner.close();
		
	}
	
}
