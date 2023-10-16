import com.github.britooo.looca.api.core.Looca;
import com.github.britooo.looca.api.group.discos.DiscoGrupo;
import com.github.britooo.looca.api.group.discos.Volume;
import com.github.britooo.looca.api.group.dispositivos.DispositivosUsbGrupo;
import com.github.britooo.looca.api.group.janelas.JanelaGrupo;
import com.github.britooo.looca.api.group.memoria.Memoria;
import com.github.britooo.looca.api.group.processador.Processador;
import com.github.britooo.looca.api.group.processos.ProcessoGrupo;
import com.github.britooo.looca.api.group.rede.Rede;
import com.github.britooo.looca.api.group.servicos.ServicoGrupo;
import com.github.britooo.looca.api.group.sistema.Sistema;
import com.github.britooo.looca.api.group.temperatura.Temperatura;
import org.springframework.jdbc.core.JdbcTemplate;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class ScriptDeInsersaoLM {

    static Looca looca = new Looca();

    static Conexao conexao = new Conexao();

    static JdbcTemplate con = conexao.getConexaoDoBanco();

    static AcessoGeralJDBC acesso = new AcessoGeralJDBC();


    public Integer incercaoMaquina(Integer lanhouse) {

//-----------------------------------------MAQUINA--------------------------------------------------------//
        String nomeDaMaquina = looca.getRede().getParametros().getHostName();
        Integer IdMaquina = acesso.inserirMaquina(nomeDaMaquina, lanhouse);

        return IdMaquina;
    }

    public void incercaoComponentes(Integer maquina) {
//----------------------------------------COMPONENTES-------------------------------------------------------//
        String processadorAlocado = looca.getProcessador().getNome();
        Integer idProcessador = acesso.inserirNomeDoComponente(processadorAlocado);
        acesso.compilarComponente(maquina, 2, 2, idProcessador);

        // Memoria memoriaAlocada = looca.getMemoria();
        Integer idRan = acesso.inserirNomeDoComponente("memRan");
        acesso.compilarComponente(maquina, 1, 1, idRan);

        String discoAlocado = looca.getGrupoDeDiscos().getDiscos().get(1).getModelo();
        Integer idDisco = acesso.inserirNomeDoComponente(discoAlocado);
        acesso.compilarComponente(maquina, 3, 3, idDisco);


        // redeDownload
        // redeUpload
        // PLACA DE VIDEO = INOVAÇÃO!!!
    }


    //----------------------------------------DADOS DOS COMPONENTES----------------------------------------------//
    public void incercaoDadosComponentes(Integer maquina) {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {

        Object dataHora = LocalDateTime.now();
        String texto;

        Double utilizacaoProcessador = looca.getProcessador().getUso();
        Integer compProcessador = acesso.selecionarComponente(maquina, 2);
        if (utilizacaoProcessador>80){
             texto = "Processamento estourando";
        } else { texto = "Normal";}

        acesso.capturaDeDados(texto, utilizacaoProcessador, dataHora, compProcessador);



     /*   Integer processos = looca.getGrupoDeProcessos().getTotalProcessos();
        Integer threadsProcessador = looca.getGrupoDeProcessos().getTotalThreads();
        Integer ThreadsProcessadorByte = (int) looca.getGrupoDeProcessos().getTotalThreads().byteValue();
*/


        Double memoriaOcupada = Double.valueOf(looca.getMemoria().getEmUso());
        Integer compMemoria = acesso.selecionarComponente(maquina, 1);
        if (utilizacaoProcessador>90){
            texto = "Memoria estourando";
        } else { texto = "Normal";}

        acesso.capturaDeDados(texto, memoriaOcupada, dataHora, compMemoria);



        Long capacidadeDisco = looca.getGrupoDeDiscos().getDiscos().get(1).getTamanho();
        String x = looca.getGrupoDeDiscos().getVolumes().toString();
        java.util.regex.Pattern pattern = java.util.regex.Pattern.compile("disponível: (\\d+)");
        java.util.regex.Matcher matcher = pattern.matcher(texto);
        long valorDisponivelLong = 0;
        if (matcher.find()) {
            String valorDisponivel = matcher.group(1);
            valorDisponivelLong = Long.parseLong(valorDisponivel);
        }
        String texto2 = looca.getGrupoDeDiscos().getVolumes().toString();
        java.util.regex.Pattern pattern2 = java.util.regex.Pattern.compile("total: (\\d+)");
        java.util.regex.Matcher matcher2 = pattern.matcher(texto2);
        long valorTotalLong = 0;
        if (matcher.find()) {
            String valorTotal = matcher.group(1);
            valorTotalLong = Long.parseLong(valorTotal);
        }

        Double discoOcupado = (double) (valorTotalLong - valorDisponivelLong);

        Integer compDisco = acesso.selecionarComponente(maquina, 3);
        if (utilizacaoProcessador>90){
            texto = "Armazenamento estourando";
        } else { texto = "Normal";}

        acesso.capturaDeDados(texto, discoOcupado, dataHora, compDisco);







        // velocidadeRedeDownload
        // velocidadeRedeUpload
        //quadrosPlaca = INOVAÇÃO!!!
        //temperaturaPlaca = INOVAÇÃO!!!
        //entergiaConsumidaPlaca = INOVAÇÃO!!!





            }

        }, 1000, 1000);

    }

}











