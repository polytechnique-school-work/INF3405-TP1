import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.CompletableFuture;

public class Client {
	
	public static void main (String[] args) throws Exception {
		
		InputValidator inputValidator = new InputValidator();
		ServerConnector serverConnector = new ServerConnector();
		
		Socket socket = serverConnector.connectToServer(inputValidator);
				
		DataInputStream in = new DataInputStream(socket.getInputStream());
		DataOutputStream out = new DataOutputStream(socket.getOutputStream());
		
		String connexionMessage = in.readUTF();
		System.out.println(connexionMessage);
		
		MessageHandler messageHandler = new MessageHandler(in, out);
		
		messageHandler.authentification(inputValidator);
		
		// Ok donc le but est de lire le scanner et de vérifier le readUTF en même temps.
		
		
		// Lecture du scanner
		CompletableFuture<Void> scannerFuture = CompletableFuture.runAsync(() -> {
			Scanner scanner = new Scanner(System.in);
			while(true) {
				while(scanner.hasNext()) {
					String input = scanner.nextLine();
					try {
						messageHandler.sendMessage(input);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						scanner.close();
						break;
					}
				}
			}
		});
		
		// Lecture des envoies serveur
		CompletableFuture<Void> serverFuture = CompletableFuture.runAsync(() -> {
			boolean isActive = true;
			
			while(isActive) {
				try {
					if(in.available() > 0) {
						String message = in.readUTF();
						System.out.println(message);
					} else {
						Thread.sleep(100);
					}
				} catch (EOFException e) {
					isActive = false;
				} catch (InterruptedException e) {
					Thread.currentThread().interrupt();
					isActive = false;
				} catch (IOException e) {
					isActive = false;
				}
			}
		});
		
		// Bloquer le thread principal
		while(true) {}
	
	}
	
}
