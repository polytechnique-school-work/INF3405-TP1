import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class AccountHandler {
	private File file = new File("accounts.gab");
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
	
	/*
	 * Permet d'écrire à l'intérieur du fichier accounts.gab
	 * @param text sera écrit à l'intérieur du fichier, suivit d'un saut de ligne.
	 * @return
	 * */
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
	
	
	/*
	 * Permet de rechercher un utilisateur à l'intérieur de la "base de donnée".
	 * @param name Nom de l'utilisateur à rechercher
	 * @return le mot de passe de l'utilisateur s'il est présent, null sinon.
	 * */
	private String fetchUser(String name) {
		try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
			String line;
			while((line = reader.readLine()) != null) {
				String[] splitted = line.split(",");
				String username = splitted[0];
				String password = splitted[1];
				if(username.equals(name)) return password;
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	/*
	 * Permet de créer un nouveau compte
	 * @param username Nom d'utilisateur du compte à créer
	 * @param password Mot de passe du compte à créer
	 * @return true si le compte est créer, false sinon.
	 * */
	public boolean createAccount(String username, String password) {
		if(hasAccount(username)) return false;
		this.write(username + "," + password);
		return true;
	}
	
	/*
	 * Permet de vérifier si un utilisateur a un compte.
	 * @param username Nom d'utilisateur du compte
	 * @return true si l'utilisateur existe, false sinon.
	 * */
	public boolean hasAccount(String username) {
		return this.fetchUser(username) != null;
	}
	
	/*
	 * Permet de tenter le login d'un utilisateur
	 * @param username Nom d'utilisateur du compte
	 * @param password Mot de passe de l'utilisateur
	 * @return true si la connection est réussie, false sinon.
	 * */
	public boolean login(String username, String password) {
		return this.fetchUser(username).equals(password);
	}
}
