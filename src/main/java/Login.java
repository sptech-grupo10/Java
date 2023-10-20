import com.github.britooo.looca.api.core.Looca;
import com.github.britooo.looca.api.group.processador.Processador;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Scanner;

public class Login {
    static String username;
static Looca looca= new Looca();
    public static void main(String[] args) {

        System.out.println(looca.getGrupoDeDiscos().getVolumes().get(0).getDisponivel()/8e+9);
        System.out.println(looca.getRede().getGrupoDeInterfaces().getInterfaces().get(3).getBytesEnviados()/1e+9);


Double x = Double.valueOf(looca.getMemoria().getEmUso());
Double y= Double.valueOf(looca.getMemoria().getDisponivel());
Double z= Double.valueOf(looca.getMemoria().getTotal());
        System.out.println(x+"\n"+y+"\n"+z);


        System.out.println();
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
