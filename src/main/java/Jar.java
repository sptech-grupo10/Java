import java.util.List;
import java.util.Scanner;

public class Jar {
    static Scanner inLine = new Scanner(System.in);
    static Scanner inInt = new Scanner(System.in);
    static AcessoJDBC acesso = new AcessoJDBC();
    static ScriptInsercao insercao = new ScriptInsercao();

    public static void main(String[] args) {
        System.out.println("""
                                           ****
                                         ****
                                       ****     ****
                                    ****     ****  ****
                                 ****     ****      ****
                               ****    ****      *****   ****
                            ****     ****      ****    ****
                          ****    ****       ****    *****    ******
                        ****    ****      ****     *****    ****  ****
                      ****   ****       ****     *****   ****      ****
                    ****    ****     ****    *****   ****           ****
                  ****    ****     ****   ****   ****                 ****
                  ****    ****     ****   ****   ****                 ****
                  ****    ****     ****   ****   ****                 ****
                  ****    ****     ****   ****   ****                 ****
                  ****    ****     ****   ****   ****      ******     ****
                  ****    ****     ****   ****   ****     ***  ****   ****
                  ****    ****     ****   ****   ****          ****   ****
                  ****    ****     ****   ****   ****       ****      ****
                  ****    ****     ****   ****   ****    ****      ****
                  ****    ****     ****   ****   ****  ****     ****
                   ****   ****     ****   ****   *********   ****
                     **** ****     ****   ****     ****    ****
                      ********     ****   ****          ****
                        ******     ****   ****       ****
                          ****     ****   ****    ****
                                   ****   *********
                                   ****   *****
                                          ****
                {ByteGuard.Lm}
                                
                                
                                
                                
                                
                                
                                
                """);
        System.out.println("********* Faça login *********");
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
        System.out.println("=================== Bem vindo(a) " + nomeUsuario + " ==================\n");

        System.out.println("===================================================================");
        System.out.println("|                       Escolha uma opção:                        |");
        System.out.println("|=================================================================|");
        System.out.println("|1- Cadastrar/Localizar componentes e começar a busca de dados    |");
        System.out.println("|2- Mudar de conta                                                |");
        System.out.println("|3- Sair                                                          |");
        System.out.println("===================================================================");

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
