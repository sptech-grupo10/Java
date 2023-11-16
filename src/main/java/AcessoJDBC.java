import com.github.seratch.jslack.Slack;
import com.github.seratch.jslack.api.webhook.Payload;
import com.github.seratch.jslack.api.webhook.WebhookResponse;
import com.google.common.io.InsecureRecursiveDeleteException;
import org.springframework.jdbc.core.JdbcTemplate;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AcessoJDBC {
    Conexao conexao = new Conexao();
    JdbcTemplate con = conexao.getConexaoDoBanco();
    private static String webHooksUrl = "https://hooks.slack.com/services/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";
    private static String oAuthToken = "xoxb-6146401163538-6147415712946-6fi0np5JSHztzuFkq5ZI8AFf";
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
        String nomeUsuario = usuario.get(0);
        return nomeUsuario;
    }

    public Integer cadastrarMaquina(String nomeMaquina, Integer fkLanhouse) {
        List<Integer> idMaquinas = new ArrayList<>();

        idMaquinas = con.queryForList("SELECT idMaquina FROM Maquina WHERE nomeMaquina = ? AND fkLanhouse = ?", Integer.class, nomeMaquina, fkLanhouse);

        if (idMaquinas.isEmpty()) {
            con.update("INSERT INTO Maquina VALUES (null, ?, ?)", nomeMaquina, fkLanhouse);
            System.out.println("Máquina " + nomeMaquina + " cadastrada");
            idMaquinas = con.queryForList("SELECT idMaquina FROM Maquina WHERE nomeMaquina = ? AND fkLanhouse = ?", Integer.class, nomeMaquina, fkLanhouse);
        }
        System.out.println("Máquina " + nomeMaquina + " localizada");

        Integer idMaquina = idMaquinas.get(0);
        return idMaquina;
    }

    public Integer cadastrarEspecsComponente(String especificacao, String valorEspec, Integer fkComponente) {
        List<Integer> especComponenteSel = new ArrayList<>();
        especComponenteSel = con.queryForList("SELECT idEspecificacaoComponente FROM EspecificacaoComponente WHERE especificacao = ? AND valorEspecificacao = ?", Integer.class, especificacao, valorEspec);

        if (especComponenteSel.isEmpty()) {
            con.update("INSERT INTO EspecificacaoComponente VALUES (null, ?, ?, ?)", especificacao, valorEspec, fkComponente);
            especComponenteSel = con.queryForList("SELECT idEspecificacaoComponente FROM EspecificacaoComponente WHERE especificacao = ? AND valorEspecificacao = ?", Integer.class, especificacao, valorEspec);
        }

        Integer especComponente = especComponenteSel.get(0);
        return especComponente;
    }

    public Integer buscarIdTipoComponente(String tipoComponente) {
        List<Integer> idComponentes = con.queryForList("SELECT idTipoComponente FROM TipoComponente WHERE tipoComponente = ?", Integer.class, tipoComponente);
        return idComponentes.get(0);
    }

    public Integer buscarOuCadastrarMetricaComponente(Double minMetrica, Double maxMetrica, String unidadeMedida) {
        List<Integer> metricaComponenteSel = new ArrayList<>();
        metricaComponenteSel = con.queryForList("SELECT idMetricaComponente FROM MetricaComponente WHERE minMetrica = ? AND maxMetrica = ? AND unidadeMedida = ?", Integer.class, minMetrica, maxMetrica, unidadeMedida);

        if (metricaComponenteSel.isEmpty()) {
            con.update("INSERT INTO MetricaComponente VALUES (null, ?, ?, ?)", minMetrica, maxMetrica, unidadeMedida);
            metricaComponenteSel = con.queryForList("SELECT idMetricaComponente FROM MetricaComponente WHERE minMetrica = ? AND maxMetrica = ? AND unidadeMedida = ?", Integer.class, minMetrica, maxMetrica, unidadeMedida);
        }

        Integer metricaComponente = metricaComponenteSel.get(0);
        return metricaComponente;
    }

    public Integer cadastrarComponente(Integer idMaquina, Integer valorTotal, Integer idTipoComponente, Integer idMetrica) {
        List<Integer> componenteSel = new ArrayList<>();

        componenteSel = con.queryForList("SELECT idComponente FROM Componente WHERE fkMaquina = ? AND fkTipoComponente = ? AND fkMetricaComponente = ?", Integer.class, idMaquina, idTipoComponente, idMetrica);

        if (componenteSel.isEmpty()) {
            con.update("INSERT INTO Componente VALUES(null, ?, ?, ?, ?)", idMaquina, valorTotal, idTipoComponente, idMetrica);
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
            con.update("INSERT INTO Log VALUES (null, ?, ?, ?, ?, ?)", textLog, valor.longValue(), timer, statusLog, idComponente);
        } else {
            con.update("INSERT INTO Log VALUES (null, ?, ?, ?, ?, ?)", textLog, valor.shortValue(), timer, statusLog, idComponente);
        }
    }

    public void enviarAlerta(Integer idMaquina, Integer idLanHouse, Integer fkComponente, Integer idMensagem) {


        String sql = "SELECT " +
                "COUNT(Log.idLog) AS quantidadeDeLogs, " +
                "TipoComponente.tipoComponente AS tipoDoComponente, " +
                "Maquina.nomeMaquina AS nomeDaMaquina, " +
                "LanHouse.unidade AS unidadeDaLanHouse " +
                "FROM Log " +
                "JOIN Componente ON Log.fkComponente = Componente.idComponente " +
                "JOIN TipoComponente ON Componente.fkTipoComponente = TipoComponente.idTipoComponente " +
                "JOIN Maquina ON Componente.fkMaquina = Maquina.idMaquina " +
                "JOIN LanHouse ON Maquina.fkLanhouse = LanHouse.idLanHouse " +
                "WHERE LanHouse.idLanHouse = ? " +
                "AND Maquina.idMaquina = ? " +
                "AND Componente.fkTipoComponente = ? " +
                "AND Log.statuslog = ? " +
                "AND Log.dataLog >= DATE_SUB(NOW(), INTERVAL 0.33 HOUR) " +
                "GROUP BY " +
                "tipoDoComponente, nomeDaMaquina, unidadeDaLanHouse, Log.dataLog " +
                "ORDER BY " +
                "Log.dataLog DESC " +
                "LIMIT 1";

        RetornoSelect resultado = con.queryForObject(sql, new Object[]{idLanHouse, idMaquina, fkComponente, idMensagem}, RetornoSelect.class);

        Integer quantidadeDeLogs = resultado.getQuantidadeDeLogs();
        String tipoDoComponente = resultado.getTipoDoComponente();
        String nomeDaMaquina = resultado.getNomeDaMaquina();
        String unidadeDaLanHouse = resultado.getUnidadeDaLanHouse();

//        String mensagemAlerta = "";
//        if (idMensagem.equals(2)){
//            mensagemAlerta = "quase";
//        } else if (idMensagem.equals(3)) {
//            mensagemAlerta = "";
//        }

        if (quantidadeDeLogs.equals(10)) {
            try {
                String mensagem = String.format("""
                        Notamos que a máquina %s da Lan House %s está apresentando problemas de %s. Verifique o quantos antes.
                        """, nomeDaMaquina, unidadeDaLanHouse, tipoDoComponente);
                StringBuilder msgBuilder = new StringBuilder();
                msgBuilder.append(mensagem);

                Payload payload = Payload.builder().channel(canalSlack).text(msgBuilder.toString()).build();
                WebhookResponse wbResp = Slack.getInstance().send(webHooksUrl, payload);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }



    public void construirLog(Integer idMaquina, Double uProcessador, Double uRam, Double uDisco, Double uDown, Double uUp, Double uGpu) {

        //mudar de acordo com a máquina
        String diretorio = "C:\\Users\\SAMSUNG\\Desktop\\SP Tech\\2º sem\\Repositórios\\Java";

        // Crie um formato de data para incorporar no nome do arquivo
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss");
        String dataFormatada = dateFormat.format(new Date());

        String nomeArquivo = "log_" + dataFormatada + ".txt";
        String caminhoCompleto = diretorio + "\\" + nomeArquivo;

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String dataHoje = LocalDateTime.now().format(dtf);

        try {
            File hist = new File(caminhoCompleto);

            if (!hist.exists()) {
                hist.createNewFile();
            }

            BufferedWriter buffer = new BufferedWriter(new FileWriter(caminhoCompleto, true));

            String conteudo = "Id Maquina: " + idMaquina +
                    "\nUtilização RAM: " + uRam +
                    "\nUtilização CPU: " + uProcessador +
                    "\nUtilização Disco: " + uDisco +
                    "\nUtilização Download: " + uDown +
                    "\nUtilização Upload: " + uUp +
                    "\nUtilização GPU: " + uGpu +
                    "\nData e hora: " + dataHoje
                    ;

            buffer.write(conteudo);
            buffer.newLine();
            buffer.close();

            System.out.println("Verifique seu log armazenado em: " + caminhoCompleto);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    //Para conseguir realizar o select e conseguir alocar os valores em variaves (sem usar map) usei classes DTO
    public static class RetornoSelect {
        private Integer quantidadeDeLogs;
        private String tipoDoComponente;
        private String nomeDaMaquina;
        private String unidadeDaLanHouse;

        // getters e setters
        // ...

        public Integer getQuantidadeDeLogs() {
            return quantidadeDeLogs;
        }

        public String getTipoDoComponente() {
            return tipoDoComponente;
        }

        public String getNomeDaMaquina() {
            return nomeDaMaquina;
        }

        public String getUnidadeDaLanHouse() {
            return unidadeDaLanHouse;
        }


}

    public static class RetornoParaLog {
        private String tipoDoComponente;
        private String nomeDaMaquina;
        private String unidadeDaLanHouse;
        private String dataHora;

        // getters e setters
        // ...


        public String getTipoDoComponente() {
            return tipoDoComponente;
        }

        public String getNomeDaMaquina() {
            return nomeDaMaquina;
        }

        public String getUnidadeDaLanHouse() {
            return unidadeDaLanHouse;
        }
        public String getDataHora(){return dataHora;}

    }
}