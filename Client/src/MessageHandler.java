import java.io.DataInputStream;
import java.io.DataOutputStream;

public class MessageHandler {
	private static DataInputStream in; 
	private static DataOutputStream out; 
	
	MessageHandler(DataInputStream in, DataOutputStream out){
		MessageHandler.in = in;
		MessageHandler.out = out;
	}
	
	public void authentification(InputValidator inputValidator) throws Exception{
		String userInfo = inputValidator.read("Veuillez entrer un nom d'utilisateur puis un mot de passe sous le format suivant {username,password}: ");
		out.writeUTF(userInfo);
		
		String réponse = in.readUTF();
		System.out.println(réponse);
		
		if(réponse.charAt(1) == 'O' && réponse.charAt(2) == 'K') {
			return;
		}
		authentification(inputValidator);
	}
}
