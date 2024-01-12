import java.net.ServerSocket;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;

public class Server {

	private static ServerSocket Listener;
	public static void main(String[] args) {
		int clientNumber = 0;
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
		
		
	}
}
