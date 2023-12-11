import java.util.List;
import java.util.Scanner;

public class Jar {
    static Scanner inLine = new Scanner(System.in);
    static Scanner inInt = new Scanner(System.in);
    static AcessoJDBC acesso = new AcessoJDBC();
    static ScriptInsercao insercao = new ScriptInsercao();

    public static void main(String[] args) {
        System.out.println("""
                || ByteGuard ||
                || Entre com suas credenciais ||
                Insira o código de acesso de sua lanhouse:
                """
        );
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
                |3- Obter últimos dados de um componente específico               |
                |4- Sair                                                          |
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
                System.out.println("""
                            ______________________________________
                            | Você pode ver os últimos dados de: |
                            ------------------~-------------------
                            | 1 - RAM                            |
                            | 2 - Processador                    |
                            | 3 - Disco                          |
                            | 3 - Rede                           |
                            | 5 - GPU                            |
                            ------------------~-------------------
                            """);
                idMaquina = insercao.cadastroMaquina(idLanhouse);
                Integer escolha = inInt.nextInt();
                acesso.buscarUltimosDados(idMaquina, escolha);
                acesso.imprimirUltimosDados(idMaquina, escolha);
                break;
            case 4:
                System.exit(0);
        }
    }
}
