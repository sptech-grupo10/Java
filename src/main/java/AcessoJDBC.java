import org.springframework.jdbc.core.JdbcTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AcessoJDBC {
    Conexao conexao = new Conexao();
    JdbcTemplate con = conexao.getConexaoDoBanco();

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
        con.update("INSERT INTO Log VALUES (null, ?, ?, ?, ?, ?)", textLog, valor.shortValue(), timer, statusLog, idComponente);
    }
}
