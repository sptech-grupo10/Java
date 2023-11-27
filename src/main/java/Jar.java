import java.util.List;
import java.util.Scanner;

public class Jar {
    static Scanner inLine = new Scanner(System.in);
    static Scanner inInt = new Scanner(System.in);
    static AcessoJDBC acesso = new AcessoJDBC();
    static ScriptInsercao insercao = new ScriptInsercao();

    public static void main(String[] args) {
        System.out.println("// ByteGuard //");
        System.out.println("// Faça login //");
        System.out.println("Insira o código de acesso de sua lanhouse: ");
        Integer idLanhouse = acesso.obterIdLanhousePorCodigo(inLine.nextLine());
        while (idLanhouse.equals(0)) {
            System.out.println("Lanhouse não encontrada, insira o código novamente");
            idLanhouse = acesso.obterIdLanhousePorCodigo(inLine.nextLine());
        }

        System.out.println("Insira o e-mail");
        String email = inLine.nextLine();
        System.out.println("Insira a sua senha");
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
