import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class LoggerHandler {
	private File file = new File("messages.txt");
	LoggerHandler(){
		if(!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}	
	}
	
	/*
	 * Permet d'écrire dans le fichier messages.txt
	 * @param text Message qui sera écrit dans le fichier.
	 * @return
	 * */
	public void write(String text) {		
		try {
			PrintWriter printer = new PrintWriter(new FileWriter(file, true));
			printer.append(text + "\n");
			printer.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			
	}
	
	/*
	 * Permet de lire les nLines derniers messages
	 * @param nLines Nombre de lignes à lire
	 * @return List<String> contenant les lignes lues
	 * */
	public List<String> read(int nLines) {
		List<String> lines = new ArrayList<String>();
		
		try {
			BufferedReader reader = new BufferedReader(new FileReader(file));
			String line;
			while((line = reader.readLine()) != null) {
				lines.add(line);
				if(lines.size() > nLines) lines.remove(0);
			}
			reader.close();	
			return lines;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return lines;
	}
	
	/*
	 * Permet de formater un message sous le format demandé
	 * @param username Nom d'utilisateur
	 * @param userAddress Addresse et port de l'utilisateur
	 * @param message Message à envoyer
	 * @return le message formaté
	 * */
	public String formatMessage(String username, int port, String userAddress, String message) {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'@'HH:mm:ss");
        String date = simpleDateFormat.format(new Date());
		return String.format("[%s - %s - %s]: %s", username, userAddress+":"+port, date, message);
	}
}
