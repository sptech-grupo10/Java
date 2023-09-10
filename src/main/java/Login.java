import java.util.Scanner;

public class Login {
    static String username;

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        System.out.println("Digite o email:");
        String email = in.nextLine();
        System.out.println("Digite a senha:");
        String senha = in.nextLine();
        Database db = new Database();
        if (Database.autenticarLogin(email, senha)) {
            username = db.getUsername();
            System.out.println("Bem vindo, " + username);
        } else {
            System.out.println("Login falhou. Verifique suas credenciais.");
        }
    }
}
