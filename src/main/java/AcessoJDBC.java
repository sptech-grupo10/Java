import com.github.seratch.jslack.Slack;
import com.github.seratch.jslack.api.webhook.Payload;
import com.github.seratch.jslack.api.webhook.WebhookResponse;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.ArrayList;
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

    public void enviarAlerta(Integer idMaquina, Integer idLanHouse, Integer fkComponente) {

        String sql = "SELECT" +
                " COUNT(Log.idLog) AS quantidadeDeLogs," +
                " TipoComponente.tipoComponente AS tipoDoComponente," +
                " Maquina.nomeMaquina AS nomeDaMaquina," +
                " LanHouse.unidade AS unidadeDaLanHouse" +
                " FROM Log" +
                " JOIN Componente ON Log.fkComponente = Componente.idComponente" +
                " JOIN TipoComponente ON Componente.fkTipoComponente = TipoComponente.idTipoComponente" +
                " JOIN Maquina ON Componente.fkMaquina = Maquina.idMaquina" +
                " JOIN LanHouse ON Maquina.fkLanhouse = LanHouse.idLanHouse" +
                " WHERE LanHouse.idLanHouse = ?" +
                " AND Maquina.idMaquina = ?" +
                " AND Componente.fkTipoComponente = ?" +
                " AND Log.statuslog = 2" +
                " AND Log.dataLog >= DATE_SUB(NOW(), INTERVAL 1 HOUR)" +
                " GROUP BY" +
                " tipoDoComponente, nomeDaMaquina, unidadeDaLanHouse";

        RetornoSelect resultado = con.queryForObject(sql, new Object[]{idLanHouse, idMaquina, fkComponente}, RetornoSelect.class);

        // Agora você pode usar os valores do DTO conforme necessário
        Integer quantidadeDeLogs = resultado.getQuantidadeDeLogs();
        String tipoDoComponente = resultado.getTipoDoComponente();
        String nomeDaMaquina = resultado.getNomeDaMaquina();
        String unidadeDaLanHouse = resultado.getUnidadeDaLanHouse();

        if (quantidadeDeLogs % 10 == 0 ){
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


    public static void enviarAlerta(String mensagem) {
        try {
            StringBuilder msgBuilder = new StringBuilder();
            msgBuilder.append(mensagem);

            Payload payload = Payload.builder().channel(canalSlack).text(msgBuilder.toString()).build();
            WebhookResponse wbResp = Slack.getInstance().send(webHooksUrl, payload);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //Para conseguir realizar o select e conseguir alocar os valores em variaves (sem usar map) usei uma classe DTO
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
}