import java.io.DataInputStream;
import java.io.DataOutputStream;

public class MessageHandler {
	private DataInputStream in; 
	private DataOutputStream out;
	
	MessageHandler(DataInputStream in, DataOutputStream out){
		this.in = in;
		this.out = out;
	}
	
	public void authentification(InputValidator inputValidator) throws Exception {
		// Accepte tous les chiffres/lettres (au moins 1 caractère)
		String checkUsername = "^[A-Za-z0-9]+$";
		
		// Accepte tous les chiffres/lettres avec certains caractères spéciaux (au moins 1 caractère)
		String checkPassword = "^[A-Za-z0-9\\p{Punct}]+$";
		String username = inputValidator.validate("Veuillez entrer votre nom d'utilisateur", checkUsername);
		String password = inputValidator.validate("Veuillez entrer votre mot de passe", checkPassword);
		String ACCEPT_CONNECTION = "<OK>";
		
		
		// Récupérer la réponse qui nous concerne
		this.sendMessage(username + ',' + password);
		
		String answer = this.in.readUTF();
		System.out.println(answer);
		
		if(answer.startsWith(ACCEPT_CONNECTION)) return;
		authentification(inputValidator);
	}
	
	/*
	 * Envoyer un message au serveur
	 * @param message Message à envoyer
	 * @return
	 * */
	public void sendMessage(String message) throws Exception {
		out.writeUTF(message);
	}
}
