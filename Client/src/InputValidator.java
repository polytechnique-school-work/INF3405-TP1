import java.util.Scanner;

public class InputValidator {
	
	private Scanner scanner;
	InputValidator() {
		this.scanner = new Scanner(System.in);
	}
	
	/*
	 * Permet de lire la saisie au clavier
	 * @param question Question à envoyer au client
	 * @return
	 * */
	private String read(String question) {
		System.out.println(question);
		return scanner.nextLine();
	}
	
	/*
	 * Permet de redemander jusqu'à ce que la réponse respecte le regex.
	 * @param question Question à demander
	 * @param regex Regex à respecter
	 * @return la réponse à la question qui respecte le regex
	 * */
	public String validate(String question, String regex) {
		String answer = read(question);
		if(!answer.matches(regex)) {
			System.out.println("Vous n'avez pas correctement répondu, recommencez.");
			return validate(question, regex);
		}
		return answer;
	}
	
	/*
	 * Permet de fermer le scanner
	 * @return
	 * */
	public void closeScanner() {
		if(scanner != null) this.scanner.close();
	}
}
