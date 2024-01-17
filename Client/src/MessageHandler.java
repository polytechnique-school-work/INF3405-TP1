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
		String userInfo = inputValidator.read("Veuillez entrer un nom d'utilisateur puis un mot de passe sous le format suivant {username,password}: ");
		if(!userInfo.contains(",")) {
			System.out.println("Veuillez utiliser le format demandé");
			authentification(inputValidator);
		}
		out.writeUTF(userInfo);
		
		String réponse = in.readUTF();
		System.out.println(réponse);
		
		if(réponse.charAt(1) == 'O' && réponse.charAt(2) == 'K') {
			return;
		}
		authentification(inputValidator);
	}
	
	public void sendMessage(String message) throws Exception{
		out.writeUTF(message);
	}
}
