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
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {

                Object dataHora = LocalDateTime.now();
                String texto;

                Double utilizacaoProcessador = looca.getProcessador().getUso();
                Integer compProcessador = acesso.selecionarComponente(maquina, 2);
                if (utilizacaoProcessador > 20) {
                    texto = "Processamento estourando";
                } else {
                    texto = "Normal";
                }

                acesso.capturaDeDados(texto, utilizacaoProcessador, dataHora, compProcessador);
                System.out.printf("\n\nUtilizacao de processador: %.2f",  utilizacaoProcessador);


                Double memoriaOcupada = Double.valueOf(looca.getMemoria().getEmUso());
                Double memoriaTotal = Double.valueOf(looca.getMemoria().getTotal());
                Double memoriaOcupadaPorcentagem = ((memoriaOcupada * 100) / memoriaTotal);
                Integer compMemoria = acesso.selecionarComponente(maquina, 1);
                if (utilizacaoProcessador > 90) {
                    texto = "Memoria estourando";
                } else {
                    texto = "Normal";
                }

                acesso.capturaDeDados(texto, memoriaOcupadaPorcentagem, dataHora, compMemoria);
                System.out.printf("\n\nMemória ocupada: %.2f%%",  memoriaOcupadaPorcentagem);


                Double valorDisponivel = Double.valueOf(looca.getGrupoDeDiscos().getVolumes().get(0).getDisponivel() / 8e+9);
                Double valorTotal = Double.valueOf(looca.getGrupoDeDiscos().getVolumes().get(0).getTotal() / 8e+9);

                BigDecimal valorDisponivelBigDecimal = new BigDecimal(valorDisponivel);
                valorDisponivelBigDecimal = valorDisponivelBigDecimal.setScale(2, RoundingMode.HALF_UP);
                Double valorDisponivelArredondado = valorDisponivelBigDecimal.doubleValue();
                System.out.printf("\n\nValor de disco disponível: %.2f%%",  valorDisponivelArredondado);

                BigDecimal valorTotalBigDecimal = new BigDecimal(valorTotal);
                valorTotalBigDecimal = valorTotalBigDecimal.setScale(2, RoundingMode.HALF_UP);
                Double valorTotalArredondado = valorTotalBigDecimal.doubleValue();
                System.out.printf("\n\nValor total do disco: %.2f%%",  valorTotalArredondado);

                Double discoOcupado = (valorTotalArredondado - valorDisponivelArredondado);
                Double porcentagemOcupado = ((discoOcupado * 100) / valorTotalArredondado);

                Integer compDisco = acesso.selecionarComponente(maquina, 3);
                if (utilizacaoProcessador > 90) {
                    texto = "Armazenamento estourando";
                } else {
                    texto = "Normal";
                }

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
