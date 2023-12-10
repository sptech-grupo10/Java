import java.util.List;
import java.util.Scanner;

public class Jar {
    static Scanner inLine = new Scanner(System.in);
    static Scanner inInt = new Scanner(System.in);
    static AcessoJDBC acesso = new AcessoJDBC();
    static ScriptInsercao insercao = new ScriptInsercao();

    public static void main(String[] args) {
        System.out.print("""
                  _           _                                 _       __  _____                           _             _           \s
                 | |         | |                               | |     / / /  __ \\                         (_)           (_)          \s
                 | |__  _   _| |_ ___  __ _ _   _  __ _ _ __ __| |    / /  | /  \\/ __ _ _ __ __ _ _ __ ___  _  ___ ___    _ _ __   ___\s
                 | '_ \\| | | | __/ _ \\/ _` | | | |/ _` | '__/ _` |   / /   | |    / _` | '__/ _` | '_ ` _ \\| |/ __/ _ \\  | | '_ \\ / __|
                 | |_) | |_| | ||  __/ (_| | |_| | (_| | | | (_| |  / /    | \\__/\\ (_| | | | (_| | | | | | | | (_| (_) | | | | | | (__\s
                 |_.__/ \\__, |\\__\\___|\\__, |\\__,_|\\__,_|_|  \\__,_| /_/      \\____/\\__,_|_|  \\__,_|_| |_| |_|_|\\___\\___/  |_|_| |_|\\___|
                         __/ |         __/ |                                                                                          \s
                        |___/         |___/                                                                                           \s
                """);
        System.out.println("\nEste é a aplicação que faz a abstração do uso de seus");
        System.out.println("componentes, primeiro insira o código da sua lanhouse: ");
        Integer idLanhouse = acesso.obterIdLanhousePorCodigo(inLine.nextLine());
        while (idLanhouse.equals(0)) {
            System.out.println("\nLanhouse não encontrada, insira o código novamente");
            idLanhouse = acesso.obterIdLanhousePorCodigo(inLine.nextLine());
        }

        System.out.println("\nInsira o e-mail");
        String email = inLine.nextLine();
        System.out.println("\nInsira a sua senha");
        String senha = inLine.nextLine();
        while (!acesso.verificarLogin(email, senha)) {
            System.out.println("Usuário não encontrado, tente novamente: ");
            System.out.println("Insira o e-mail");
            email = inLine.nextLine();
            System.out.println("Insira a sua senha");
            senha = inLine.nextLine();
        }

        String nomeUsuario = acesso.buscarUsuario(email, senha);
        System.out.println(String.format("""
                =================== Bem vindo(a) " %s " ==================
                ===================================================================
                |                       Escolha uma opção:                        |
                |=================================================================|
                |1- Cadastrar/Localizar componentes e começar a busca de dados    |
                |2- Mudar de conta                                                |
                |3- Sair                                                          |
                ===================================================================
                """, nomeUsuario));

        switch (inInt.nextInt()) {
            case 1:
                Integer idMaquina = insercao.cadastroMaquina(idLanhouse);

                List<Integer> idsComponentes = insercao.cadastrarComponentes(idMaquina);

                insercao.inserirDados(idsComponentes.get(0), idsComponentes.get(1), idsComponentes.get(2), idsComponentes.get(3), idsComponentes.get(4), idMaquina, idLanhouse);
                break;
            case 2:
                main(null);
                break;
            case 3:
                System.exit(0);
        }
    }
}
