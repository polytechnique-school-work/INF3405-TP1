import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicReference;

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
		
		AtomicReference<Boolean> isActive = new AtomicReference<Boolean>(true);
		
		// Ok donc le but est de lire le scanner et de vérifier le readUTF en même temps.
		
		// Lecture du scanner
		CompletableFuture.runAsync(() -> {
			Scanner scanner = new Scanner(System.in);
			while(isActive.get()) {
				while(isActive.get() && scanner.hasNext()) {
					String input = scanner.nextLine();
					if (input.equals("/disconnect")) {
						try {
							messageHandler.sendMessage("%disconnect%");
							socket.close();
							isActive.set(true);
							System.out.println("Vous vous êtes déconnecté avec succès!");
							break;
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
					try {
						if(input.length() > 200) {
							System.out.println("Veuilez entrer un message ayant moins de 200 caractères: ");
						}
						else {
							messageHandler.sendMessage(input);
						}
					} catch (Exception e) {
						scanner.close();
						System.exit(0);
						break;
					}
				}
			}
		});
		
		// Lecture des envoies serveur
		CompletableFuture.runAsync(() -> {
			while(isActive.get()) {
				try {
					if(in.available() > 0) {
						String message = in.readUTF();
						if(message == "<Server> Fermeture du serveur.") {
							isActive.set(false);
						}
						System.out.println(message);
					} else {
						Thread.sleep(100);
					}
				} catch (EOFException e) {
					isActive.set(false);
				} catch (InterruptedException e) {
					Thread.currentThread().interrupt();
					isActive.set(false);
				} catch (IOException e) {
					isActive.set(false);
				}
			}
		});
		
		// Bloquer le thread principal
		while(isActive.get()) {}

	}
	
}
