import java.net.Socket;

public class ServerConnector {
	public Socket connectToServer(InputValidator inputValidator) {
		
		// Différents regex récupérés un peu partout sur internet, par exemple : 
		// https://stackoverflow.com/questions/5284147/validating-ipv4-addresses-with-regexp
		String checkIPRegex = "^((25[0-5]|(2[0-4]|1\\d|[1-9]|)\\d)\\.?\\b){4}$";
		String checkPort = "^50[0-4][0-9]|5050$";
		

		// Obtention des paramètres d'authentification du client 
		String serverAddress = inputValidator.validate("Entrez l'adresse IPv4 du poste auquel vous désirez vous connecter: ", checkIPRegex);
		String port = inputValidator.validate("Veuillez entrer le port entre 5000 et 5050 auquel vous voulez vous connecter: ", checkPort);
			
		// Essai d'une nouvelle connexion avec le serveur
		Socket socket;
		try{
			socket = new Socket(serverAddress, Integer.parseInt(port));
		}
		catch(Exception e){
			System.out.println("La connexion au serveur est erronée");
			return connectToServer(inputValidator);
		}

		inputValidator.closeScanner();
		System.out.format("Serveur lancé sur [%s:%s]\n", serverAddress, port);
		return socket;
	}
}
