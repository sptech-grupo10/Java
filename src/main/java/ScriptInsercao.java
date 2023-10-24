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

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.*;

public class ScriptInsercao {
    static Looca looca = new Looca();

    static Conexao conexao = new Conexao();

    static JdbcTemplate con = conexao.getConexaoDoBanco();

    static AcessoJDBC acesso = new AcessoJDBC();

    public Integer cadastroMaquina(Integer idLanhouse) {
        return acesso.cadastrarMaquina(looca.getRede().getParametros().getHostName(), idLanhouse);
    }

    public List<Integer> cadastrarComponentes(Integer idMaquina) {
        List<Integer> idsComponentes = new ArrayList<>();
        idsComponentes.add(acesso.cadastrarComponente(idMaquina, acesso.buscarIdTipoComponente("Processador"), acesso.buscarOuCadastrarMetricaComponente(0.0, 90.0, "%"), acesso.cadastrarEspecComponente(looca.getProcessador().getNome())));
        idsComponentes.add(acesso.cadastrarComponente(idMaquina, acesso.buscarIdTipoComponente("Ram"), acesso.buscarOuCadastrarMetricaComponente(10.0, 90.0, "%"), acesso.cadastrarEspecComponente("Memória RAM")));
        idsComponentes.add(acesso.cadastrarComponente(idMaquina, acesso.buscarIdTipoComponente("Disco"), acesso.buscarOuCadastrarMetricaComponente(10.0, 90.0, "%"), acesso.cadastrarEspecComponente(looca.getGrupoDeDiscos().getDiscos().get(1).getModelo())));
        return idsComponentes;
    }

    public void inserirDados(Integer idProcessador, Integer idRam, Integer idDisco) {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            List<Double> metricaProcessador = acesso.obterMetricaComponente(idProcessador);
            List<Double> metricaRam = acesso.obterMetricaComponente(idRam);
            List<Double> metricaDisco = acesso.obterMetricaComponente(idDisco);

            @Override
            public void run() {
                Object dataHora = LocalDateTime.now();
                String textLog;
                Integer statusLog;

                // Processador
                Double utilizacaoProcessador = looca.getProcessador().getUso();
                if (utilizacaoProcessador < metricaProcessador.get(0) || utilizacaoProcessador > metricaProcessador.get(1)) {
                    textLog = "Processador sobrecarregado";
                    statusLog = 2;
                } else {
                    textLog = "Processador em uso normal";
                    statusLog = 1;
                }
                acesso.insercaoDados(textLog, utilizacaoProcessador, dataHora, statusLog, idProcessador);
                System.out.printf("\n\nUtilização do processador: %d%%", utilizacaoProcessador.shortValue());

                // Memória RAM
                Double memoriaTotal = Double.valueOf(looca.getMemoria().getTotal());
                Double memoriaEmUso = Double.valueOf(looca.getMemoria().getEmUso());
                Double porcMemoria = Double.valueOf((memoriaEmUso * 100) / memoriaTotal);

                if (porcMemoria < metricaRam.get(0) || porcMemoria > metricaRam.get(1)) {
                    textLog = "Memória RAM sobrecarregado";
                    statusLog = 2;
                } else {
                    textLog = "Memória RAM em uso normal";
                    statusLog = 1;
                }
                acesso.insercaoDados(textLog, porcMemoria, dataHora, statusLog, idRam);
                System.out.printf("\nUtilização da memória RAM: %d%%", porcMemoria.shortValue());

                // Disco
                Double valorDisponivelDisco = Double.valueOf(looca.getGrupoDeDiscos().getVolumes().get(0).getDisponivel() / 8e+9);
                Double valorTotalDisco = Double.valueOf(looca.getGrupoDeDiscos().getVolumes().get(0).getTotal() / 8e+9);

                BigDecimal valorDisponivelBigDecimal = new BigDecimal(valorDisponivelDisco);
                valorDisponivelBigDecimal = valorDisponivelBigDecimal.setScale(2, RoundingMode.HALF_UP);
                Double valorDisponivelArredondado = valorDisponivelBigDecimal.doubleValue();

                BigDecimal valorTotalBigDecimal = new BigDecimal(valorTotalDisco);
                valorTotalBigDecimal = valorTotalBigDecimal.setScale(2, RoundingMode.HALF_UP);
                Double valorTotalArredondado = valorTotalBigDecimal.doubleValue();

                Double discoOcupado = (valorTotalArredondado - valorDisponivelArredondado);
                Double porcentagemDiscoOcupado = ((discoOcupado * 100) / valorTotalArredondado);

                if (porcentagemDiscoOcupado < metricaDisco.get(0) || porcentagemDiscoOcupado > metricaDisco.get(1)) {
                    textLog = "Disco sobrecarregado";
                    statusLog = 2;
                } else {
                    textLog = "Disco em uso normal";
                    statusLog = 1;
                }
                acesso.insercaoDados(textLog, porcentagemDiscoOcupado, dataHora, statusLog, idDisco);
                System.out.printf("\nUtilização de Disco: %d%%", porcentagemDiscoOcupado.shortValue());
            }
        }, 0, 1000);
    }
}
