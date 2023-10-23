import java.util.Scanner;


public class JarKM {
    static Scanner usuarioString = new Scanner(System.in);
    Scanner UsuarioInteger = new Scanner(System.in);
    static AcessoGeralJDBC acesso = new AcessoGeralJDBC();

    static ScriptDeInsersaoLM ins = new ScriptDeInsersaoLM();


    public static void main(String[] args) {
        System.out.println("====================================================");
        System.out.println("=====================ByteGuard======================");
        System.out.println("=====================Faça login=====================");
        System.out.println("Insira o código de acesso de sua lanhouse: ");
        String codigoAcesso = usuarioString.nextLine();
        System.out.println("Nome do Usuario");
        String nomeUsuario = usuarioString.nextLine();
        System.out.println("Senha");
        String senha = usuarioString.nextLine();

        Integer idEmpresa;
        Integer idLanHouse;
        Integer idUsuario;

        idLanHouse = acesso.DescobrirIdLanHousePorCodigo(codigoAcesso);
        idUsuario = acesso.DescobrirIdUsiario(nomeUsuario, senha);

        Integer idMaquina = ins.incercaoMaquina(idLanHouse);

        ins.incercaoComponentes(idMaquina);

        ins.incercaoDadosComponentes(idMaquina);
    }

}
