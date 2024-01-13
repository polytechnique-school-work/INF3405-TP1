import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class AccountHandler {
	private File file = new File("accounts.csv");
	AccountHandler() {
		if(!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}	
	}
	
	private void write(String text) {		
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
	
	
	// Si trouvé renvoie le password
	// Sinon renvoie ""
	private String fetchUser(String name) {
		try {
			BufferedReader reader = new BufferedReader(new FileReader(file));
			String line;
			while((line = reader.readLine()) != null) {
				String[] splitted = line.split(",");
				String username = splitted[0];
				String password = splitted[1];
				if(username == name) return password;
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";
	}
	
	// Renvoie true si le compte a été créé
	// Renvoie false si c'est impossible de le créer
	public boolean createAccount(String username, String password) {
		if(hasAccount(username)) return false;
		this.write(username + "," + password);
		return true;
	}
	
	// Renvoie true si l'utilisateur a un compte,
	// Renvoie false sinon.
	public boolean hasAccount(String username) {
		return this.fetchUser(username) != "";
	}
	
	// Renvoie true si c'est les bons identifiants
	// Renvoie false sinon.
	public boolean login(String username, String password) {
		return this.fetchUser(username) == password;
	}
}
