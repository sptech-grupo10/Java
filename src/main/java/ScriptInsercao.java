import Classes.EspecificacoesComponente;
import Classes.Maquina;
import Classes.MetricaComponente;
import Classes.TipoComponente;
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
        Maquina maquina = new Maquina(looca.getRede().getParametros().getHostName(), idLanhouse);
        return acesso.cadastrarMaquina(maquina);
    }

    public List<Integer> cadastrarComponentes(Integer idMaquina) {
        List<Integer> idsComponentes = new ArrayList<>();

        TipoComponente tipoComponenteCPU = new TipoComponente("Processador");
        MetricaComponente metricaComponenteCPU = new MetricaComponente(0.0, 90.0, "%");
        idsComponentes.add(acesso.cadastrarComponente(idMaquina, 1, acesso.buscarIdTipoComponente(tipoComponenteCPU), acesso.buscarOuCadastrarMetricaComponente(metricaComponenteCPU)));

        EspecificacoesComponente especificacaoCPU1 = new EspecificacoesComponente("Numero CPU lógicas", looca.getProcessador().getNumeroCpusLogicas().toString(), idsComponentes.get(0));
        EspecificacoesComponente especificacaoCPU2 = new EspecificacoesComponente("Modelo", looca.getProcessador().getNome(), idsComponentes.get(0));
        EspecificacoesComponente especificacaoCPU3 = new EspecificacoesComponente("Fabricante", looca.getProcessador().getFabricante(), idsComponentes.get(0));
        EspecificacoesComponente especificacaoCPU4 = new EspecificacoesComponente("Identificador", looca.getProcessador().getIdentificador(), idsComponentes.get(0));

        acesso.cadastrarEspecsComponente(especificacaoCPU1);
        acesso.cadastrarEspecsComponente(especificacaoCPU2);
        acesso.cadastrarEspecsComponente(especificacaoCPU3);
        acesso.cadastrarEspecsComponente(especificacaoCPU4);

        System.out.println("\nCPU localizada: " + especificacaoCPU2.getValor());

        TipoComponente tipoComponenteRAM = new TipoComponente("Ram");
        MetricaComponente metricaComponenteRAM = new MetricaComponente(0.0, 90.0, "%");
        idsComponentes.add(acesso.cadastrarComponente(idMaquina, 1, acesso.buscarIdTipoComponente(tipoComponenteRAM), acesso.buscarOuCadastrarMetricaComponente(metricaComponenteRAM)));

        EspecificacoesComponente especificacaoRAM1 = new EspecificacoesComponente("Capacidade máxima", looca.getMemoria().getTotal().toString(), idsComponentes.get(1));
        acesso.cadastrarEspecsComponente(especificacaoRAM1);

        System.out.println("\nRAM de capacidade máxima: " + especificacaoRAM1.getValor() + " localizada");

        TipoComponente tipoComponenteDisco = new TipoComponente("Disco");
        MetricaComponente metricaComponenteDisco = new MetricaComponente(10.0, 90.0, "%");
        idsComponentes.add(acesso.cadastrarComponente(idMaquina, 1, acesso.buscarIdTipoComponente(tipoComponenteDisco), acesso.buscarOuCadastrarMetricaComponente(metricaComponenteDisco)));

        EspecificacoesComponente especificacaoDisco1 = new EspecificacoesComponente("Modelo", looca.getGrupoDeDiscos().getDiscos().get(0).getModelo(), idsComponentes.get(2));
        EspecificacoesComponente especificacaoDisco2 = new EspecificacoesComponente("UUID", looca.getGrupoDeDiscos().getVolumes().get(0).getUUID(), idsComponentes.get(2));
        EspecificacoesComponente especificacaoDisco3 = new EspecificacoesComponente("Tipo de disco", looca.getGrupoDeDiscos().getVolumes().get(0).getTipo(), idsComponentes.get(2));
        EspecificacoesComponente especificacaoDisco4 = new EspecificacoesComponente("Unidade", looca.getGrupoDeDiscos().getVolumes().get(0).getPontoDeMontagem(), idsComponentes.get(2));
        acesso.cadastrarEspecsComponente(especificacaoDisco1);
        acesso.cadastrarEspecsComponente(especificacaoDisco2);
        acesso.cadastrarEspecsComponente(especificacaoDisco3);
        acesso.cadastrarEspecsComponente(especificacaoDisco4);

        System.out.println("\nDisco localizado: " + especificacaoDisco1.getValor());

        for (RedeInterface rede : looca.getRede().getGrupoDeInterfaces().getInterfaces()) {
            if (!rede.getEnderecoIpv4().isEmpty()) {
                TipoComponente tipoComponenteRede = new TipoComponente("Rede");
                MetricaComponente metricaComponenteRede = new MetricaComponente(40.0, 90.0, "%");
                idsComponentes.add(acesso.cadastrarComponente(idMaquina, 1, acesso.buscarIdTipoComponente(tipoComponenteRede), acesso.buscarOuCadastrarMetricaComponente(metricaComponenteRede)));

                EspecificacoesComponente especificacaoRede1 = new EspecificacoesComponente("Endereço IPv4", rede.getEnderecoIpv4().get(0), idsComponentes.get(3));
                EspecificacoesComponente especificacaoRede2 = new EspecificacoesComponente("Endereço IPv6", rede.getEnderecoIpv6().get(0), idsComponentes.get(3));
                EspecificacoesComponente especificacaoRede3 = new EspecificacoesComponente("Nome da rede", rede.getNome(), idsComponentes.get(3));
                EspecificacoesComponente especificacaoRede4 = new EspecificacoesComponente("Nome de exibição", rede.getNomeExibicao(), idsComponentes.get(3));
                acesso.cadastrarEspecsComponente(especificacaoRede1);
                acesso.cadastrarEspecsComponente(especificacaoRede2);
                acesso.cadastrarEspecsComponente(especificacaoRede3);
                acesso.cadastrarEspecsComponente(especificacaoRede4);

                System.out.println("\nRede localizada: " + especificacaoRede3.getValor());
                break;
            }
        }

        TipoComponente tipoComponenteGPU = new TipoComponente("GPU");
        MetricaComponente metricaComponenteGPU = new MetricaComponente(0.0, 95.0, "%");
        idsComponentes.add(acesso.cadastrarComponente(idMaquina, 1, acesso.buscarIdTipoComponente(tipoComponenteGPU), acesso.buscarOuCadastrarMetricaComponente(metricaComponenteGPU)));

        EspecificacoesComponente especificacaoGPU1 = new EspecificacoesComponente("Nome da placa Gráfica", hardware.getGraphicsCards().get(0).getName(), idsComponentes.get(4));
        EspecificacoesComponente especificacaoGPU2 = new EspecificacoesComponente("Marca placa gráfica", hardware.getGraphicsCards().get(0).getVendor(), idsComponentes.get(4));
        EspecificacoesComponente especificacaoGPU3 = new EspecificacoesComponente("Versão do driver", hardware.getGraphicsCards().get(0).getVersionInfo(), idsComponentes.get(4));
        acesso.cadastrarEspecsComponente(especificacaoGPU1);
        acesso.cadastrarEspecsComponente(especificacaoGPU2);
        acesso.cadastrarEspecsComponente(especificacaoGPU3);

        System.out.println("\nPlaca gráfica localizada: " + especificacaoGPU1.getValor());

        return idsComponentes;
    }

    public void inserirDados(Integer idProcessador, Integer idRam, Integer idDisco, Integer idRede, Integer idGpu, Integer idMaquina, Integer idLanHouse) {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            List<Double> metricaProcessador = acesso.obterMetricaComponente(idProcessador);
            List<Double> metricaRam = acesso.obterMetricaComponente(idRam);
            List<Double> metricaDisco = acesso.obterMetricaComponente(idDisco);
            List<Double> metricaRede = acesso.obterMetricaComponente(idRede);
            List<Double> metricaGpu = acesso.obterMetricaComponente(idGpu);


            @Override
            public void run() {
                Object dataHora = LocalDateTime.now();
                String textLog;
                Integer statusLog;
                //foi preciso mudar a posição das utilizações para usar no método de log

                // Processador
                Double utilizacaoProcessador = looca.getProcessador().getUso();

                // Memória RAM
                Double memoriaTotal = Double.valueOf(looca.getMemoria().getTotal());
                Double memoriaEmUso = Double.valueOf(looca.getMemoria().getEmUso());
                Double porcMemoria = Double.valueOf((memoriaEmUso * 100) / memoriaTotal);

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

                //Rede - Upload
                Double velocidadeUpload = 0.0;

                for (int i = 0; lista.size() > i; i++) {
                    if (!lista.get(i).getEnderecoIpv4().isEmpty()) {
                        velocidadeUpload = looca.getRede().getGrupoDeInterfaces().getInterfaces().get(i).getBytesEnviados().doubleValue();
                        break;
                    }
                }

                Double porcentagemVelocidadeUpload = (velocidadeUpload * 100) / 1.0;

                // GPU - Inovação
                Double bytesGpu = Double.valueOf(hardware.getGraphicsCards().get(0).getVRam());
                Double totalGpu = 5981045185.00;
                Double porcGpu = (bytesGpu / totalGpu) * 100;


//              Processador
                Boolean enviarAlerta = false;
                if (utilizacaoProcessador < metricaProcessador.get(0) || utilizacaoProcessador > metricaProcessador.get(1)) {
                    textLog = "Processador sobrecarregado";
                    statusLog = 3;
                    enviarAlerta = true;
                } else if (utilizacaoProcessador < (metricaProcessador.get(0) * 0.85) ||
                        utilizacaoProcessador > (metricaProcessador.get(1) * 0.85)) {
                    textLog = "Processador quase sobrecarregado";
                    statusLog = 2;
                    enviarAlerta = true;
                } else {
                    textLog = "Processador em uso normal";
                    statusLog = 1;

                }
                acesso.insercaoDados(textLog, utilizacaoProcessador, dataHora, statusLog, idProcessador);

                if (enviarAlerta && statusLog.equals(2)){
                    acesso.enviarAlerta(idMaquina, idLanHouse, 1, 2);
                } else if (enviarAlerta && statusLog.equals(3)) {
                    acesso.enviarAlerta(idMaquina, idLanHouse, 1, 3);
                    acesso.construirLog(idMaquina, utilizacaoProcessador, porcMemoria, porcentagemDiscoOcupado, porcentagemVelocidadeDowload, porcentagemVelocidadeUpload, porcGpu, 1);
                }
                enviarAlerta = false;

                System.out.print("==============================================================");
                System.out.printf("\n|Utilização do processador: %d%%|", utilizacaoProcessador.shortValue());

                // Memória RAM

                if (porcMemoria < metricaRam.get(0) || porcMemoria > metricaRam.get(1)) {
                    textLog = "Memória RAM sobrecarregado";
                    statusLog = 1;
                    enviarAlerta = true;
                } else if (porcMemoria < (metricaRam.get(0) * 0.85) || porcMemoria > (metricaRam.get(1) * 0.85)) {
                    textLog = "Memória RAM quase sobrecarregado";
                    statusLog = 2;
                    enviarAlerta = true;
                } else {
                    textLog = "Memória RAM em uso normal";
                    statusLog = 1;
                }
                acesso.insercaoDados(textLog, porcMemoria, dataHora, statusLog, idRam);

                if (enviarAlerta && statusLog.equals(2)){
                    acesso.enviarAlerta(idMaquina, idLanHouse, 2, 2);
                } else if (enviarAlerta && statusLog.equals(3)) {
                    acesso.enviarAlerta(idMaquina, idLanHouse, 2, 3);
                    acesso.construirLog(idMaquina, utilizacaoProcessador, porcMemoria, porcentagemDiscoOcupado, porcentagemVelocidadeDowload, porcentagemVelocidadeUpload, porcGpu, 2);
                }
                enviarAlerta = false;

                System.out.printf("\nUtilização da memória RAM: %d%%", porcMemoria.shortValue());

                // Disco

                if (porcentagemDiscoOcupado < metricaDisco.get(0) || porcentagemDiscoOcupado > metricaDisco.get(1)) {
                    textLog = "Disco sobrecarregado";
                    statusLog = 3;
                    enviarAlerta = true;
                } else if (porcentagemDiscoOcupado < (metricaDisco.get(0) * 0.85)
                        || porcentagemDiscoOcupado > (metricaDisco.get(1) * 0.85)) {
                    textLog = "Disco quase sobrecarregado";
                    statusLog = 2;
                    enviarAlerta = true;
                } else {
                    textLog = "Disco em uso normal";
                    statusLog = 1;
                }
                acesso.insercaoDados(textLog, porcentagemDiscoOcupado, dataHora, statusLog, idDisco);

                if (enviarAlerta && statusLog.equals(2)){
                    acesso.enviarAlerta(idMaquina, idLanHouse, 3, 2);
                } else if (enviarAlerta && statusLog.equals(3)) {
                    acesso.enviarAlerta(idMaquina, idLanHouse, 3, 3);
                    acesso.construirLog(idMaquina, utilizacaoProcessador, porcMemoria, porcentagemDiscoOcupado, porcentagemVelocidadeDowload, porcentagemVelocidadeUpload, porcGpu, 3);
                }
                enviarAlerta = false;

                System.out.printf("\nUtilização de Disco: %d%%", porcentagemDiscoOcupado.shortValue());


                //Rede - Download

                if (porcentagemVelocidadeDowload < metricaRede.get(0)) {
                    textLog = "Download fora do ideal";
                    statusLog = 2;
                    enviarAlerta = true;
                } else if (porcentagemVelocidadeDowload < (metricaRede.get(0) * 0.85)) {
                    textLog = "Download quase fora do ideal";
                    statusLog = 2;
                    enviarAlerta = true;
                } else {
                    textLog = "Download ideal";
                    statusLog = 1;
                }
                acesso.insercaoDados(textLog, velocidadeDownload, dataHora, statusLog, idRede);

                if (enviarAlerta && statusLog.equals(2)){
                    acesso.enviarAlerta(idMaquina, idLanHouse, 4, 2);
                } else if (enviarAlerta && statusLog.equals(3)) {
                    acesso.enviarAlerta(idMaquina, idLanHouse, 4, 3);
                    acesso.construirLog(idMaquina, utilizacaoProcessador, porcMemoria, porcentagemDiscoOcupado, porcentagemVelocidadeDowload, porcentagemVelocidadeUpload, porcGpu, 4);
                }
                enviarAlerta = false;

                System.out.println("\nVelocidade de download:" + Conversor.formatarBytes(velocidadeDownload.longValue()));

                //Rede - Upload

                if (porcentagemVelocidadeUpload < metricaRede.get(0)) {
                    textLog = "Upload fora do ideal";
                    statusLog = 3;
                    enviarAlerta = true;
                } else if (porcentagemVelocidadeDowload < (metricaRede.get(0) * 0.85)) {
                    textLog = "Upload quase fora do ideal";
                    statusLog = 2;
                    enviarAlerta = true;
                } else {
                    textLog = "Upload ideal";
                    statusLog = 1;
                }
                acesso.insercaoDados(textLog, velocidadeUpload, dataHora, statusLog, idRede);

                if (enviarAlerta && statusLog.equals(2)){
                    acesso.enviarAlerta(idMaquina, idLanHouse, 4, 2);
                } else if (enviarAlerta && statusLog.equals(3)) {
                    acesso.enviarAlerta(idMaquina, idLanHouse, 4, 3);
                    acesso.construirLog(idMaquina, utilizacaoProcessador, porcMemoria, porcentagemDiscoOcupado, porcentagemVelocidadeDowload, porcentagemVelocidadeUpload, porcGpu, 4);
                }
                enviarAlerta = false;

                System.out.println("Velocidade de upload: " + Conversor.formatarBytes(velocidadeUpload.longValue()));

                if (porcGpu < metricaGpu.get(0)) {
                    textLog = "Placa gráfica sobrecarregando";
                    statusLog = 3;
                    enviarAlerta = true;
                } else if (porcGpu < (metricaGpu.get(0) * 0.85)) {
                    textLog = "Placa gráfica quase sobrecarregando";
                    statusLog = 2;
                    enviarAlerta = true;
                } else {
                    textLog = "Placa gráfica ideal";
                    statusLog = 1;
                    enviarAlerta = true;
                }
                acesso.insercaoDados(textLog, porcGpu, dataHora, statusLog, idGpu);

                if (enviarAlerta && statusLog.equals(2)){
                    acesso.enviarAlerta(idMaquina, idLanHouse, 5, 2);
                } else if (enviarAlerta && statusLog.equals(3)) {
                    acesso.enviarAlerta(idMaquina, idLanHouse, 5, 3);
                    acesso.construirLog(idMaquina, utilizacaoProcessador, porcMemoria, porcentagemDiscoOcupado, porcentagemVelocidadeDowload, porcentagemVelocidadeUpload, porcGpu, 5);
                }
              enviarAlerta = false;

                System.out.println("Utilização placa gráfica: " + porcGpu + "%");
            }
        }, 0, 1000);
    }
}
