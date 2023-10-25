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

        String Identificacao = String.valueOf(acesso.DefinindoMaquina(maquina));
        System.out.println(Identificacao);

        System.out.println("| Componente | Dado | Uni.Medida |");

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {

                Object dataHora = LocalDateTime.now();
                String texto;
                Integer metricaMax;


                Double utilizacaoProcessador = looca.getProcessador().getUso();
                Integer compProcessador = acesso.selecionarComponente(maquina, 2);
                metricaMax = acesso.SelecionarMetricaMax(compProcessador);
                String UniMedida = acesso.DefinindoMedida(2);

                if (utilizacaoProcessador > metricaMax) {
                    texto = "Processamento estourando";
                } else {
                    texto = "Normal";
                }

                System.out.println("|CPU |" + utilizacaoProcessador + " | " + UniMedida + " |");

                acesso.capturaDeDados(texto, utilizacaoProcessador, dataHora, compProcessador);


                Double memoriaOcupada = Double.valueOf(looca.getMemoria().getEmUso());
                Double memoriaTotal = Double.valueOf(looca.getMemoria().getTotal());
                Double memoriaOcupadaPorcentagem = ((memoriaOcupada * 100) / memoriaTotal);
                Integer compMemoria = acesso.selecionarComponente(maquina, 1);
                metricaMax = acesso.SelecionarMetricaMax(compMemoria);
                UniMedida = acesso.DefinindoMedida(1);
                if (memoriaOcupadaPorcentagem > metricaMax) {
                    texto = "Memoria estourando";
                } else {
                    texto = "Normal";
                }

                System.out.println("|CPU |" + memoriaOcupadaPorcentagem + " | " + UniMedida + " |");

                acesso.capturaDeDados(texto, memoriaOcupadaPorcentagem, dataHora, compMemoria);


                Double valorDisponivel = Double.valueOf(looca.getGrupoDeDiscos().getVolumes().get(0).getDisponivel() / 8e+9);
                Double valorTotal = Double.valueOf(looca.getGrupoDeDiscos().getVolumes().get(0).getTotal() / 8e+9);

                BigDecimal valorDisponivelBigDecimal = new BigDecimal(valorDisponivel);
                valorDisponivelBigDecimal = valorDisponivelBigDecimal.setScale(2, RoundingMode.HALF_UP);
                Double valorDisponivelArredondado = valorDisponivelBigDecimal.doubleValue();


                BigDecimal valorTotalBigDecimal = new BigDecimal(valorTotal);
                valorTotalBigDecimal = valorTotalBigDecimal.setScale(2, RoundingMode.HALF_UP);
                Double valorTotalArredondado = valorTotalBigDecimal.doubleValue();


                Double discoOcupado = (valorTotalArredondado - valorDisponivelArredondado);
                Double porcentagemOcupado = ((discoOcupado * 100) / valorTotalArredondado);

                Integer compDisco = acesso.selecionarComponente(maquina, 3);
                metricaMax = acesso.SelecionarMetricaMax(compDisco);
                UniMedida = acesso.DefinindoMedida(3);
                if (porcentagemOcupado > metricaMax) {
                    texto = "Armazenamento estourando";
                } else {
                    texto = "Normal";
                }

                System.out.println("|CPU |" + porcentagemOcupado + " | " + UniMedida + " |");


                acesso.capturaDeDados(texto, porcentagemOcupado, dataHora, compDisco);


                // velocidadeRedeDownload
                // velocidadeRedeUpload
                //quadrosPlaca = INOVAÇÃO!!!
                //temperaturaPlaca = INOVAÇÃO!!!
                //entergiaConsumidaPlaca = INOVAÇÃO!!!


            }

        }, 0, 1000);

    }

}
