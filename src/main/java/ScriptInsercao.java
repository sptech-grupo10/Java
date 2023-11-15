import com.github.britooo.looca.api.core.Looca;
import com.github.britooo.looca.api.group.discos.DiscoGrupo;
import com.github.britooo.looca.api.group.discos.Volume;
import com.github.britooo.looca.api.group.dispositivos.DispositivosUsbGrupo;
import com.github.britooo.looca.api.group.janelas.JanelaGrupo;
import com.github.britooo.looca.api.group.memoria.Memoria;
import com.github.britooo.looca.api.group.processador.Processador;
import com.github.britooo.looca.api.group.processos.ProcessoGrupo;
import com.github.britooo.looca.api.group.rede.Rede;
import com.github.britooo.looca.api.group.rede.RedeInterface;
import com.github.britooo.looca.api.group.servicos.ServicoGrupo;
import com.github.britooo.looca.api.group.sistema.Sistema;
import com.github.britooo.looca.api.group.temperatura.Temperatura;
import com.github.britooo.looca.api.util.Conversor;
import com.google.errorprone.annotations.Immutable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.apache.commons.dbcp2.BasicDataSource;
import oshi.SystemInfo;
import oshi.hardware.HardwareAbstractionLayer;


import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.*;


public class ScriptInsercao {
    static Looca looca = new Looca();

    static Conexao conexao = new Conexao();

    static JdbcTemplate con = conexao.getConexaoDoBanco();

    static AcessoJDBC acesso = new AcessoJDBC();

    SystemInfo systemInfo = new SystemInfo();
    HardwareAbstractionLayer hardware = systemInfo.getHardware();


    public Integer cadastroMaquina(Integer idLanhouse) {
        return acesso.cadastrarMaquina(looca.getRede().getParametros().getHostName(), idLanhouse);
    }

    public List<Integer> cadastrarComponentes(Integer idMaquina) {
        List<Integer> idsComponentes = new ArrayList<>();
        idsComponentes.add(acesso.cadastrarComponente(idMaquina, 1, acesso.buscarIdTipoComponente("Processador"), acesso.buscarOuCadastrarMetricaComponente(0.0, 90.0, "%")));
        acesso.cadastrarEspecsComponente("Numero CPU lógicas", looca.getProcessador().getNumeroCpusLogicas().toString(), idsComponentes.get(0));
        acesso.cadastrarEspecsComponente("Modelo", looca.getProcessador().getNome(), idsComponentes.get(0));
        acesso.cadastrarEspecsComponente("Fabricante", looca.getProcessador().getFabricante(), idsComponentes.get(0));
        acesso.cadastrarEspecsComponente("Identificador", looca.getProcessador().getIdentificador(), idsComponentes.get(0));

        idsComponentes.add(acesso.cadastrarComponente(idMaquina, 1, acesso.buscarIdTipoComponente("Ram"), acesso.buscarOuCadastrarMetricaComponente(10.0, 90.0, "%")));
        acesso.cadastrarEspecsComponente("Capacidade máxima (GB)", looca.getMemoria().getTotal().toString(), idsComponentes.get(1));

        idsComponentes.add(acesso.cadastrarComponente(idMaquina, 1, acesso.buscarIdTipoComponente("Disco"), acesso.buscarOuCadastrarMetricaComponente(10.0, 90.0, "%")));
        acesso.cadastrarEspecsComponente("Modelo", looca.getGrupoDeDiscos().getDiscos().get(0).getModelo(), idsComponentes.get(2));
        acesso.cadastrarEspecsComponente("UUID", looca.getGrupoDeDiscos().getVolumes().get(0).getUUID(), idsComponentes.get(2));
        acesso.cadastrarEspecsComponente("Tipo de disco", looca.getGrupoDeDiscos().getVolumes().get(0).getTipo(), idsComponentes.get(2));
        acesso.cadastrarEspecsComponente("Unidade", looca.getGrupoDeDiscos().getVolumes().get(0).getPontoDeMontagem(), idsComponentes.get(2));

        for (RedeInterface rede : looca.getRede().getGrupoDeInterfaces().getInterfaces()) {
            if (!rede.getEnderecoIpv4().isEmpty()) {
                idsComponentes.add(acesso.cadastrarComponente(idMaquina, 1, acesso.buscarIdTipoComponente("Rede"), acesso.buscarOuCadastrarMetricaComponente(40.0, 90.0, "%")));
                acesso.cadastrarEspecsComponente("Endereço IPv4", rede.getEnderecoIpv4().get(0), idsComponentes.get(3));
                acesso.cadastrarEspecsComponente("Endereço IPv6", rede.getEnderecoIpv6().get(0), idsComponentes.get(3));
                acesso.cadastrarEspecsComponente("Nome da rede", rede.getNome(), idsComponentes.get(3));
                acesso.cadastrarEspecsComponente("Nome de exibição", rede.getNomeExibicao(), idsComponentes.get(3));
                break;
            }
        }

        //cadastrando gpu - inovacao

        idsComponentes.add(acesso.cadastrarComponente(idMaquina, 1, acesso.buscarIdTipoComponente("GPU"), acesso.buscarOuCadastrarMetricaComponente(0.0, 90.0, "%")));
        acesso.cadastrarEspecsComponente("Nome da placa Gráfica", hardware.getGraphicsCards().get(0).getName(), idsComponentes.get(4));
        acesso.cadastrarEspecsComponente("Marca placa gráfica", hardware.getGraphicsCards().get(0).getVendor(), idsComponentes.get(4));
        acesso.cadastrarEspecsComponente("Versão do driver", hardware.getGraphicsCards().get(0).getVersionInfo(), idsComponentes.get(4));

        return idsComponentes;
    }


    public void inserirDados(Integer idProcessador, Integer idRam, Integer idDisco, Integer idRede, Integer idGpu, Integer idMaquina, Integer idLanHouse) {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            List<Double> metricaProcessador = acesso.obterMetricaComponente(idProcessador);
            List<Double> metricaRam = acesso.obterMetricaComponente(idRam);
            List<Double> metricaDisco = acesso.obterMetricaComponente(idDisco);
            List<Double> metricaRede = acesso.obterMetricaComponente(idRede);


            @Override
            public void run() {
                Object dataHora = LocalDateTime.now();
                String textLog;
                Integer statusLog;

                // Processador
                Double utilizacaoProcessador = looca.getProcessador().getUso();
                if (utilizacaoProcessador < metricaProcessador.get(0) || utilizacaoProcessador > metricaProcessador.get(1)) {
                    textLog = "Processador sobrecarregado";
                    statusLog = 3;
                    acesso.enviarAlerta(idMaquina, idLanHouse, 2, 3);
                }
                else if (utilizacaoProcessador < (metricaProcessador.get(0) * 0.85) ||
                        utilizacaoProcessador > (metricaProcessador.get(1) * 0.85)) {
                    textLog = "Processador quase sobrecarregado";
                    statusLog = 2;
                    acesso.enviarAlerta(idMaquina, idLanHouse, 2, 2);
//                    acesso.construirLog(idMaquina, utilizacaoProcessador, );
                }
                else {
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
                    statusLog = 1;
                    acesso.enviarAlerta(idMaquina, idLanHouse, 1, 3);
                }
                else if (porcMemoria < (metricaRam.get(0) * 0.85) || porcMemoria > (metricaRam.get(1) * 0.85)) {
                    textLog = "Memória RAM quase sobrecarregado";
                    statusLog = 2;
                    acesso.enviarAlerta(idMaquina, idLanHouse, 1, 2);
                }
                else {
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
                    statusLog = 3;
                    acesso.enviarAlerta(idMaquina, idLanHouse, 3, 3);
                }
                else if (porcentagemDiscoOcupado < (metricaDisco.get(0) * 0.85)
                        || porcentagemDiscoOcupado > (metricaDisco.get(1) * 0.85)) {
                    textLog = "Disco quase sobrecarregado";
                    statusLog = 2;
                    acesso.enviarAlerta(idMaquina, idLanHouse, 3, 2);
                }
                else {
                    textLog = "Disco em uso normal";
                    statusLog = 1;
                }
                acesso.insercaoDados(textLog, porcentagemDiscoOcupado, dataHora, statusLog, idDisco);
                System.out.printf("\nUtilização de Disco: %d%%", porcentagemDiscoOcupado.shortValue());


                //Rede - Download
                Double velocidadeDownload = 0.0;
                List<RedeInterface> lista = looca.getRede().getGrupoDeInterfaces().getInterfaces();
                for (int i = 0; lista.size() > i; i++) {
                    if (!lista.get(i).getEnderecoIpv4().isEmpty()) {
                        velocidadeDownload = looca.getRede().getGrupoDeInterfaces().getInterfaces().get(i).getBytesRecebidos().doubleValue();
                        break;
                    }
                }

                Double porcentagemVelocidadeDowload = (velocidadeDownload * 100) / 150.0;

                if (porcentagemVelocidadeDowload < metricaRede.get(0)) {
                    textLog = "Download fora do ideal";
                    statusLog = 2;
                    acesso.enviarAlerta(idMaquina, idLanHouse, 4, 3);
                }
                else if (porcentagemVelocidadeDowload < (metricaRede.get(0) * 0.85)) {
                    textLog = "Download quase fora do ideal";
                    statusLog = 2;
                    acesso.enviarAlerta(idMaquina, idLanHouse, 4, 2);
                }
                else {
                    textLog = "Download ideal";
                    statusLog = 1;
                }
                acesso.insercaoDados(textLog, velocidadeDownload, dataHora, statusLog, idRede);
                System.out.println("\nVelocidade de download:" + Conversor.formatarBytes(velocidadeDownload.longValue()));

                //Rede - Upload
                Double velocidadeUpload = 0.0;

                for (int i = 0; lista.size() > i; i++) {
                    if (!lista.get(i).getEnderecoIpv4().isEmpty()) {
                        velocidadeUpload = looca.getRede().getGrupoDeInterfaces().getInterfaces().get(i).getBytesEnviados().doubleValue();
                        break;
                    }
                }

                Double porcentagemVelocidadeUpload = (velocidadeUpload * 100) / 1.0;

                if (porcentagemVelocidadeUpload < metricaRede.get(0)) {
                    textLog = "Upload fora do ideal";
                    statusLog = 3;
                    acesso.enviarAlerta(idMaquina, idLanHouse, 4, 3);
                }
                else if (porcentagemVelocidadeDowload < (metricaRede.get(0) * 0.85)) {
                    textLog = "Upload quase fora do ideal";
                    statusLog = 2;
                    acesso.enviarAlerta(idMaquina, idLanHouse, 4, 2);
                }
                else {
                    textLog = "Upload ideal";
                    statusLog = 1;
                }
                acesso.insercaoDados(textLog, velocidadeUpload, dataHora, statusLog, idRede);
                System.out.println("Velocidade de upload: " + Conversor.formatarBytes(velocidadeUpload.longValue()));
            }
        }, 0, 1000);
    }
}
