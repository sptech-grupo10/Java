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


    public static <Memeoria> void main(String[] args) {

//----------------------------------MAQUINA--------------------------------------------------------//
        String nomeDaMaquina = looca.getRede().getParametros().getHostName();
//----------------------------------------COMPONENTES---------------------------------------------------------//
        String processadorAlocado = looca.getProcessador().getNome();
        // Memoria memoriaAlocada = looca.getMemoria();
        String discoAlocado = looca.getGrupoDeDiscos().getDiscos().get(1).getNome();
        Long capacidadeDisco = looca.getGrupoDeDiscos().getDiscos().get(1).getTamanho();
        String tipoDisco = looca.getGrupoDeDiscos().getDiscos().get(1).getModelo();
        // redeDownload
        // redeUpload
        // PLACA DE VIDEO = INOVAÇÃO!!!

//----------------------------------------DADOS DOS COMPONENTES----------------------------------------------//
        Object dataHora = LocalDateTime.now();
        Double utilizacaoProcessador = looca.getProcessador().getUso();
        Integer processos = looca.getGrupoDeProcessos().getTotalProcessos();
        Integer threadsProcessador = looca.getGrupoDeProcessos().getTotalThreads();
        Integer ThreadsProcessadorByte = (int) looca.getGrupoDeProcessos().getTotalThreads().byteValue();
        Long memoriaOcupada = looca.getMemoria().getEmUso();

        String texto = looca.getGrupoDeDiscos().getVolumes().toString();
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

        long discoOcupado = valorTotalLong - valorDisponivelLong;
        // velocidadeRedeDownload
        // velocidadeRedeUpload
        //quadrosPlaca = INOVAÇÃO!!!
        //temperaturaPlaca = INOVAÇÃO!!!
        //entergiaConsumidaPlaca = INOVAÇÃO!!!




    }
}











