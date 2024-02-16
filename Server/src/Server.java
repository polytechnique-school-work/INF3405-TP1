import java.io.IOException;
import java.net.ServerSocket;

public class Server {

	private static ServerSocket Listener;
	public static void main(String[] args) {
		
		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() {
				endProcess();
			}
		});

		
		// Initialisation de certains éléments
		InputValidator inputValidator = new InputValidator();
		LoggerHandler loggerHandler = new LoggerHandler();
		ServerStarter serverStarter = new ServerStarter();	
		
		
		int clientNumber = 0;
		
		try {
			Listener = new ServerSocket();
			Listener = serverStarter.connect(inputValidator, Listener);
			Listener.setReuseAddress(true);
			
			while(true) {
				new ClientHandler(Listener.accept(), Listener.getLocalPort(), clientNumber++, loggerHandler).start();
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			endProcess();
		}
	}
	
	public static void endProcess() {
		ClientHandler.sendToAll("<Server> Fermeture du serveur.");
		try {
			Listener.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
