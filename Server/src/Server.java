import java.net.ServerSocket;
import java.io.IOException;

public class Server {

	private static ServerSocket Listener;
	public static void main(String[] args) {

		
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
			try {
				Listener.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		
	}
}
