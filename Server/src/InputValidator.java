import java.util.Scanner;

public class InputValidator {
	
	private Scanner scanner;
	InputValidator() {
		this.scanner = new Scanner(System.in);
	}
	
	private String read(String question) {
		System.out.println(question);
		return scanner.nextLine();
	}
	
	public String validate(String question, String regex) {
		String answer = read(question);
		if(!answer.matches(regex)) {
			System.out.println("Vous n'avez pas correctement répondu, recommencez.");
			return validate(question, regex);
		}
		return answer;
	}
	
	public void closeScanner() {
		if(scanner != null) this.scanner.close();
	}
}
