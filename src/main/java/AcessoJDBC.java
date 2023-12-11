import Classes.EspecificacoesComponente;
import Classes.Maquina;
import Classes.MetricaComponente;
import Classes.TipoComponente;
import com.github.seratch.jslack.Slack;
import com.github.seratch.jslack.api.webhook.Payload;
import com.github.seratch.jslack.api.webhook.WebhookResponse;
import org.springframework.jdbc.core.JdbcTemplate;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.PosixFilePermission;
import java.nio.file.attribute.PosixFilePermissions;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class AcessoJDBC {
    ConexaoSQL conexao = new ConexaoSQL();
    JdbcTemplate con = conexao.getConnection();
    
    Conexao conexaoMysql = new Conexao();
    JdbcTemplate conexaoMysqlConexaoDoBanco = conexaoMysql.getConexaoDoBanco();
    private static String parte1url = "https://hooks.slack.com/services/T064";
    private static String webHooksUrl = parte1url + "ABT4TFU/B066XCEMKQX/vPXL1QNC68BczzFcaxN1Gdhc";
    private static String canalSlack = "alertas";

    public Integer obterIdLanhousePorCodigo(String codigoAcesso) {
        List<Integer> idLanhouse = con.queryForList("SELECT idLanhouse FROM Lanhouse WHERE codigoAcesso = ?", Integer.class, codigoAcesso);
        if (idLanhouse.isEmpty()) {
            return 0;
        }
        return idLanhouse.get(0);
    }

    public Boolean verificarLogin(String email, String senha) {
        List<Integer> idUsuario = con.queryForList("SELECT idUsuario FROM Usuario WHERE email = ? AND senha = ?", Integer.class, email, senha);
        return !idUsuario.isEmpty();
    }

    public String buscarUsuario(String email, String senha) {
        List<String> usuario = con.queryForList("SELECT nome FROM Usuario WHERE email = ? AND senha = ?", String.class, email, senha);
        return usuario.get(0);
    }

    public Integer cadastrarMaquina(Maquina maquina) {
        List<Integer> idMaquinas = new ArrayList<>();

        idMaquinas = con.queryForList("SELECT idMaquina FROM Maquina WHERE nomeMaquina = ? AND fkLanhouse = ?", Integer.class, maquina.getNomeMaquina(), maquina.getFkLanHouse());

        if (idMaquinas.isEmpty()) {
            //nuvem
            con.update("INSERT INTO Maquina(nomeMaquina, fkLanhouse) VALUES (?, ?)", maquina.getNomeMaquina(), maquina.getFkLanHouse());
            System.out.println("Máquina " + maquina.getNomeMaquina() + " cadastrada");
            //local
            conexaoMysqlConexaoDoBanco.update("INSERT INTO Maquina(nomeMaquina, fkLanhouse) VALUES (?, ?)", maquina.getNomeMaquina(), maquina.getFkLanHouse());
            System.out.println("Máquina " + maquina.getNomeMaquina() + " cadastrada");
            idMaquinas = con.queryForList("SELECT idMaquina FROM Maquina WHERE nomeMaquina = ? AND fkLanhouse = ?", Integer.class, maquina.getNomeMaquina(), maquina.getFkLanHouse());
        }
        System.out.println("Máquina " + maquina.getNomeMaquina() + " localizada");

        return idMaquinas.get(0);
    }

    public void cadastrarEspecsComponente(EspecificacoesComponente especificacaoComponente) {
        List<Integer> especComponenteSel = new ArrayList<>();
        especComponenteSel = con.queryForList("SELECT idEspecificacaoComponente FROM EspecificacaoComponente WHERE especificacao = ? AND valorEspecificacao = ? AND fkComponente = ?", Integer.class, especificacaoComponente.getEspecificacao(), especificacaoComponente.getValor(), especificacaoComponente.getFkComponente());

        if (especComponenteSel.isEmpty()) {
            //nuvem
            con.update("INSERT INTO EspecificacaoComponente(especificacao, valorEspecificacao, fkComponente) VALUES (?, ?, ?)", especificacaoComponente.getEspecificacao(), especificacaoComponente.getValor(), especificacaoComponente.getFkComponente());
            //local
            conexaoMysqlConexaoDoBanco.update("INSERT INTO EspecificacaoComponente(especificacao, valorEspecificacao, fkComponente) VALUES (?, ?, ?)", especificacaoComponente.getEspecificacao(), especificacaoComponente.getValor(), especificacaoComponente.getFkComponente());
        }
    }

    public Integer buscarIdTipoComponente(TipoComponente tipoComponente) {
        List<Integer> idComponentes = con.queryForList("SELECT idTipoComponente FROM TipoComponente WHERE tipoComponente = ?", Integer.class, tipoComponente.getTipoComponente());
        return idComponentes.get(0);
    }

    public Integer buscarOuCadastrarMetricaComponente(MetricaComponente metricaComponente) {
        List<Integer> metricaComponenteSel = new ArrayList<>();
        metricaComponenteSel = con.queryForList("SELECT idMetricaComponente FROM MetricaComponente WHERE minMetrica = ? AND maxMetrica = ? AND unidadeMedida = ?", Integer.class, metricaComponente.getMin(), metricaComponente.getMax(), metricaComponente.getUnidadeMedida());

        if (metricaComponenteSel.isEmpty()) {
            //nuvem
            con.update("INSERT INTO MetricaComponente(minMetrica, maxMetrica, unidadeMedida) VALUES (?, ?, ?)", metricaComponente.getMin(), metricaComponente.getMax(), metricaComponente.getUnidadeMedida());
            //local
            conexaoMysqlConexaoDoBanco.update("INSERT INTO MetricaComponente(minMetrica, maxMetrica, unidadeMedida) VALUES (?, ?, ?)", metricaComponente.getMin(), metricaComponente.getMax(), metricaComponente.getUnidadeMedida());
            metricaComponenteSel = con.queryForList("SELECT idMetricaComponente FROM MetricaComponente WHERE minMetrica = ? AND maxMetrica = ? AND unidadeMedida = ?", Integer.class, metricaComponente.getMin(), metricaComponente.getMax(), metricaComponente.getUnidadeMedida());
        }

        return metricaComponenteSel.get(0);
    }

    public Integer cadastrarComponente(Integer idMaquina, Integer valorTotal, Integer idTipoComponente, Integer idMetrica) {
        List<Integer> componenteSel = new ArrayList<>();

        componenteSel = con.queryForList("SELECT idComponente FROM Componente WHERE fkMaquina = ? AND fkTipoComponente = ? AND fkMetricaComponente = ?", Integer.class, idMaquina, idTipoComponente, idMetrica);

        if (componenteSel.isEmpty()) {
            //nuvem
            con.update("INSERT INTO Componente(fkMaquina, valorTotal, fkTipoComponente, fkMetricaComponente) VALUES(?, ?, ?, ?)", idMaquina, valorTotal, idTipoComponente, idMetrica);
            //local
            conexaoMysqlConexaoDoBanco.update("INSERT INTO Componente(fkMaquina, valorTotal, fkTipoComponente, fkMetricaComponente) VALUES(?, ?, ?, ?)", idMaquina, valorTotal, idTipoComponente, idMetrica);
            componenteSel = con.queryForList("SELECT idComponente FROM Componente WHERE fkMaquina = ? AND fkTipoComponente = ? AND fkMetricaComponente = ?", Integer.class, idMaquina, idTipoComponente, idMetrica);
        }

        Integer idComponente = componenteSel.get(0);

        System.out.println("Componente de id " + idComponente + " foi localizado");
        return idComponente;
    }

    public List<Double> obterMetricaComponente(Integer idComponente) {
        List<Double> metricaComponente = new ArrayList<>();
        metricaComponente.add(con.queryForList("SELECT minMetrica FROM MetricaComponente WHERE idMetricaComponente = (SELECT fkMetricaComponente FROM Componente WHERE idComponente = ?)", Double.class, idComponente).get(0));
        metricaComponente.add(con.queryForList("SELECT maxMetrica FROM MetricaComponente WHERE idMetricaComponente = (SELECT fkMetricaComponente FROM Componente WHERE idComponente = ?)", Double.class, idComponente).get(0));
        return metricaComponente;
    }

    public void insercaoDados(String textLog, Double valor, Object timer, Integer statusLog, Integer idComponente) {
        if (textLog.contains("Download") || textLog.contains("Upload")) {
            //nuvem
            con.update("INSERT INTO Log(textLog, valor, dataLog, statusLog, fkComponente) VALUES (?, ?, ?, ?, ?)", textLog, valor.longValue(), timer, statusLog, idComponente);
            //local
            conexaoMysqlConexaoDoBanco.update("INSERT INTO Log(textLog, valor, dataLog, statusLog, fkComponente) VALUES (?, ?, ?, ?, ?)", textLog, valor.longValue(), timer, statusLog, idComponente);
        } else {
            //nuvem
            con.update("INSERT INTO Log(textLog, valor, dataLog, statusLog, fkComponente) VALUES (?, ?, ?, ?, ?)", textLog, valor.shortValue(), timer, statusLog, idComponente);
            //local
            conexaoMysqlConexaoDoBanco.update("INSERT INTO Log(textLog, valor, dataLog, statusLog, fkComponente) VALUES (?, ?, ?, ?, ?)", textLog, valor.shortValue(), timer, statusLog, idComponente);
        }
    }

    public void enviarAlerta(Integer idMaquina, Integer idLanHouse, Integer fkComponente, Integer idMensagem) {
        //System.out.println("entrou no enviar");
        String sql = "SELECT COUNT(*) AS quantidadeDeLogs " +
                "FROM Log " +
                "JOIN Componente ON Log.fkComponente = Componente.idComponente " +
                "JOIN TipoComponente ON Componente.fkTipoComponente = TipoComponente.idTipoComponente " +
                "JOIN Maquina ON Componente.fkMaquina = Maquina.idMaquina " +
                "JOIN LanHouse ON Maquina.fkLanhouse = LanHouse.idLanHouse " +
                "WHERE LanHouse.idLanHouse = ? " +
                "AND Maquina.idMaquina = ? " +
                "AND Componente.fkTipoComponente = ? " +
                "AND Log.statuslog = ? " +
                "AND Log.dataLog >= DATEADD(MINUTE, -200, GETDATE()) " +
                "GROUP BY " +
                "TipoComponente.tipoComponente, " +
                "Maquina.nomeMaquina, " +
                "LanHouse.unidade";


        List<Integer> qtdRegistros = con.queryForList(sql, Integer.class, idLanHouse, idMaquina, fkComponente
                , idMensagem
        );

        //System.out.println(qtdRegistros);
        if (!qtdRegistros.isEmpty()) {
           // System.out.println("viu se é senfjnefe");
            System.out.println(qtdRegistros.get(0));
            if (qtdRegistros.get(0).equals(10)) {
            //if (qtdRegistros.get(0) > 10) {


                String sqlTipoComponente = """
                            select tipoComponente from TipoComponente where idTipoComponente = ?;
                        """;


                List<String> infoTipoComponente = con.queryForList(sqlTipoComponente, String.class,
                        fkComponente);


                String sqlNomeMaquina = """
                            SELECT nomeMaquina from Maquina where idMaquina = ?;
                        """;

                List<String> infoNomeMaquina = con.queryForList(sqlNomeMaquina, String.class,
                        idMaquina);

                String sqlUnidadeLanHouse = """
                            select unidade from LanHouse where idLanHouse = ?;
                        """;

                List<String> infoUnidade = con.queryForList(sqlUnidadeLanHouse, String.class,
                        idLanHouse);

                try {
                    String mensagem = String.format("""
                            Notamos que a máquina %s da Lan House %s está apresentando problemas de %s. Verifique o quantos antes.
                            """, infoNomeMaquina.get(0), infoUnidade.get(0), infoTipoComponente.get(0));
                    StringBuilder msgBuilder = new StringBuilder();
                    msgBuilder.append(mensagem);

                    Payload payload = Payload.builder().channel(canalSlack).text(msgBuilder.toString()).build();
                    WebhookResponse wbResp = Slack.getInstance().send(webHooksUrl, payload);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                //System.out.println("fez tudo");
            }
        }
    }


    public void construirLog(Integer idMaquina, Double uProcessador, Double uRam, Double uDisco, String uDown, String uUp, Double uGpu, Integer idComponente) {

        String caminhoArquivo = System.getProperty("java.io.tmpdir") + "/";
       // String caminhoArquivo = "C:\\Users\\SAMSUNG\\Desktop\\SP Tech\\2º sem\\Repositórios\\Java";

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyyMMdd");
        String dataHoje = LocalDateTime.now().format(dtf);

        Date dataHoraAtual = new Date();
        String data = new SimpleDateFormat("dd/MM/yyyy").format(dataHoraAtual);
        String hora = new SimpleDateFormat("HH:mm:ss").format(dataHoraAtual);

        String caminho = caminhoArquivo + dataHoje + "_log.txt";
        String componente = "";

        if (Files.exists(Path.of(caminho))) {
            try {
                BufferedWriter buffer = new BufferedWriter(new FileWriter(caminho, true));
                if (idComponente.equals(1)){
                    componente = "CPU";
                } else if (idComponente.equals(2)) {
                    componente = "RAM";
                } else if (idComponente.equals(3)) {
                    componente = "Disco";
                } else if (idComponente.equals(4)) {
                    componente = "Rede";
                } else if (idComponente.equals(5)) {
                    componente = "GPU";
                }

                List<String> nomeMaquina = con.queryForList("SELECT nomeMaquina FROM maquina WHERE idMaquina = ?", String.class, idMaquina);

                String conteudo = "\nA Máquina " + nomeMaquina.get(0) + " precisa de atenção em " + componente + "." +
                        "\nId Maquina: " + idMaquina +
                        "\nUtilização RAM: " + uRam + "%" +
                        "\nUtilização CPU: " + uProcessador + "%"+
                        "\nUtilização Disco: " + uDisco + "%"+
                        "\nUtilização Download: " + uDown +
                        "\nUtilização Upload: " + uUp +
                        "\nUtilização GPU: " + uGpu + "%" +
                        "\nData: " + data + " Hora: " + hora +
                        "\nArquivo salvo em: " + caminho +
                        "\n"
                        ;

                buffer.write(conteudo);
                buffer.newLine();
                buffer.close();

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            //vendo o SO
            boolean win = System.getProperty("os.name").toLowerCase().contains("win");

            try {
                if (win) {
                    Files.createFile(Path.of(caminho));
                } else {
                    Set<PosixFilePermission> perms = EnumSet.of(PosixFilePermission.OWNER_READ,
                            PosixFilePermission.OWNER_EXECUTE, PosixFilePermission.OWNER_WRITE);
                    Files.createFile(Path.of(caminho), PosixFilePermissions.asFileAttribute(perms));
                }

                try {
                    BufferedWriter writer = Files.newBufferedWriter(Path.of(caminho));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public List<Map<String, Object>> buscarUltimosDados(Integer idMaquina, Integer componente) {
        List<Map<String, Object>> retornoSelect = new ArrayList<>();

        String sql = "SELECT l.dataLog, l.textLog, tc.tipoComponente " +
                "FROM Log AS l " +
                "INNER JOIN Componente AS c ON l.fkComponente = c.idComponente " +
                "INNER JOIN Maquina AS m ON c.fkMaquina = m.idMaquina " +
                "INNER JOIN TipoComponente AS tc ON c.fkTipoComponente = tc.idTipoComponente " +
                "WHERE m.idMaquina = ? AND c.fkTipoComponente = ? " +
                "ORDER BY l.dataLog DESC LIMIT 5";

        List<Map<String, Object>> result = con.queryForList(sql, idMaquina, componente);

        for (Map<String, Object> row : result) {
            retornoSelect.add(row);
        }

        return retornoSelect;
    }

    public void imprimirUltimosDados(Integer idMaquina, Integer componente) {
        List<Map<String, Object>> dados = buscarUltimosDados(idMaquina, componente);

        for (Map<String, Object> linha : dados) {
            System.out.println("Tipo: " + linha.get("tipoComponente"));
            System.out.println("Data: " + linha.get("dataLog"));
            System.out.println("Condição: " + linha.get("textLog"));
            System.out.println(); // Adiciona uma linha em branco entre os elementos
        }
    }

}
