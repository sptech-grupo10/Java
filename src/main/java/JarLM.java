import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;


public class JarLM {
    static Scanner usuarioString = new Scanner(System.in);
    Scanner UsuarioInteger = new Scanner(System.in);
    static AcessoGeralJDBC acesso = new AcessoGeralJDBC();

    static ScriptDeInsersaoLM ins = new ScriptDeInsersaoLM();

    public static void main(String[] args) {

        System.out.println("""
                                           @@@@
                                         @@@@
                                       @@@@     @@@@
                                    @@@@     @@@@  @@@@
                                 @@@@     @@@@      @@@@
                               @@@@    @@@@    @@@@@     @@@@
                            @@@@     @@@@   @@@@       @@@@   \s
                          @@@@    @@@@   @@@@       @@@@@    @@@@\s
                        @@@@    @@@@   @@@@      @@@@@    @@@@   @@@@
                      @@@@   @@@@    @@@@      @@@@@   @@@@       @@@@
                    @@@@    @@@@   @@@@     @@@@@   @@@@          @@@@
                  @@@@    @@@@    @@@@    @@@@   @@@@             @@@@
                  @@@@    @@@@    @@@@    @@@@   @@@@             @@@@
                  @@@@    @@@@    @@@@    @@@@   @@@@             @@@@
                  @@@@    @@@@    @@@@    @@@@   @@@@             @@@@
                  @@@@    @@@@    @@@@    @@@@   @@@@             @@@@
                  @@@@    @@@@    @@@@    @@@@   @@@@             @@@@
                  @@@@    @@@@    @@@@    @@@@   @@@@        @@@@ @@@@
                  @@@@    @@@@    @@@@    @@@@   @@@@       @@@@  @@@@
                  @@@@    @@@@    @@@@    @@@@   @@@@    @@@@   @@@@
                  @@@@    @@@@    @@@@    @@@@   @@@@  @@@@    @@@@
                   @@@@   @@@@    @@@@    @@@@   @@@@@@@@@   @@@@
                     @@@@ @@@@    @@@@    @@@@     @@@@    @@@@
                      @@@@@@@@    @@@@    @@@@          @@@@
                          @@@@    @@@@    @@@@      @@@@
                           @@@    @@@@    @@@@    @@@@
                                  @@@@    @@@@@@@@@
                                   @@@    @@@@@@
                                          @@@@
                {ByteGuard.Lm}
                                
                                
                                
                                
                                
                                
                                
                """);
        System.out.println("|Nome da Empresa|");
        String nomeEmpresa = usuarioString.nextLine();
        System.out.println("|Nome da LanHouse|");
        String nomeLanHouse = usuarioString.nextLine();
        System.out.println("|Nome do Usuario|");
        String nomeUsuario = usuarioString.nextLine();
        System.out.println("|Senha|");
        String senha = usuarioString.nextLine();


        Integer idEmpresa;
        Integer idLanHouse;
        Integer idUsuario;

        idEmpresa = acesso.DescobrirIdEmpresa(nomeEmpresa);
        idLanHouse = acesso.DescobrirIdLanHouse(nomeLanHouse);
        idUsuario = acesso.DescobrirIdUsiario(nomeUsuario, senha);

        Integer idMaquina = ins.incercaoMaquina(idLanHouse);
        System.out.println("id" + idMaquina);
        ins.incercaoComponentes(idMaquina);
        final Integer[] cont = {0};

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                System.out.println("x");
        ins.incercaoDadosComponentes(idMaquina);
                cont[0]++;

                if(cont[0] >0){
                    timer.cancel();
                }
            }


        }, 10000, 1000);
    }

}
