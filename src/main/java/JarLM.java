import java.util.Scanner;


public class JarLM {
    static Scanner usuarioString = new Scanner(System.in);
    Scanner UsuarioInteger = new Scanner(System.in);
    static AcessoGeralJDBC acesso = new AcessoGeralJDBC();

    static ScriptDeInsersaoLM ins = new ScriptDeInsersaoLM();


    public static void main(String[] args) {
        System.out.println("Nome da Empresa");
        String nomeEmpresa = usuarioString.nextLine();
        System.out.println("Nome da LanHouse");
        String nomeLanHouse = usuarioString.nextLine();
        System.out.println("Nome do Usuario");
        String nomeUsuario = usuarioString.nextLine();
        System.out.println("Senha");
        String senha = usuarioString.nextLine();


        Integer idEmpresa;
        Integer idLanHouse;
        Integer idUsuario;

        idEmpresa = acesso.DescobrirIdEmpresa(nomeEmpresa);
        idLanHouse = acesso.DescobrirIdLanHouse(nomeLanHouse);
        idUsuario = acesso.DescobrirIdUsiario(nomeUsuario, senha);

        Integer idMaquina = ins.incercaoMaquina(idLanHouse);

        ins.incercaoComponentes(idMaquina);

        ins.incercaoDadosComponentes(idMaquina);
    }

}
