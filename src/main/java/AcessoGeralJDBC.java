import org.springframework.jdbc.core.JdbcTemplate;

import java.util.ArrayList;
import java.util.List;

public class AcessoGeralJDBC {
    Conexao conexao = new Conexao();
    JdbcTemplate con = conexao.getConexaoDoBanco();

    static ScriptDeInsersaoLM ins = new ScriptDeInsersaoLM();

    /*LOGIN*/

    public Integer DescobrirIdEmpresa(String nome) {
        List<Integer> idEmpresas = con.queryForList("SELECT idEmpresa FROM Empresa WHERE nomeFantasia = ?", Integer.class, nome);

        Integer idEmpresa = idEmpresas.get(0);

        if (idEmpresa == null) {
            System.out.println(
                    "empresa não existe"
            );
        }
        return idEmpresa;

    }

    ;

    public Integer DescobrirIdLanHouse(String nome) {
        List<Integer> idLanHouses = con.queryForList("SELECT idLanHouse FROM LanHouse WHERE unidade = ?", Integer.class, nome);
        Integer idLanHouse = idLanHouses.get(0);

        if (idLanHouse == null) {
            System.out.println(
                    "LanHouse não existe"
            );
        }
        return idLanHouse;
    }

    ;

    public Integer DescobrirIdUsiario(String nome, String senha) {
        List<Integer> idUsuarios = con.queryForList("SELECT idUsuario FROM Usuario WHERE nome = ? AND senha = ?", Integer.class, nome, senha);
        Integer idUsuario = idUsuarios.get(0);

        if (idUsuario == null) {
            System.out.println(
                    "Usuario não existe \n" +
                            "ou Senha incorreta"

            );

        }
        return idUsuario;
    }

    ;

    public Integer inserirMaquina(String nome, Integer fkLanHouse) {
        List<Integer> idMaquinas = new ArrayList<>();
        Integer idMaquina = null;

        try {
            idMaquinas = con.queryForList("SELECT idMaquina FROM Maquina WHERE nomeMaquina = ? AND fkLanHouse = ?", Integer.class, nome, fkLanHouse);
            idMaquina = idMaquinas.get(0);
            return idMaquina;
        } catch (IndexOutOfBoundsException err) {
            System.out.println("Maquina Sendo Cadastrada");
            err.printStackTrace();
        } finally {
            if (idMaquina == null) {
                con.update("INSERT INTO Maquina VALUES (null,?,?)", nome, fkLanHouse);
                idMaquinas = con.queryForList("SELECT idMaquina FROM Maquina WHERE nomeMaquina = ? AND fkLanHouse = ?", Integer.class, nome, fkLanHouse);
                idMaquina = idMaquinas.get(0);
            }
        }

        return idMaquina;

    }

    ;

    /*--------------------------------------------------------------Especificação de componentes--------------------------------------------------------------*/
    public Integer inserirNomeDoComponente(String nome) {
        List<Integer> idEspecificacoesComponentes = new ArrayList<>();
        Integer idEspecificacoesComponente = null;
        try {
            idEspecificacoesComponentes = con.queryForList("SELECT idEspecificacoesComponente FROM EspecificacoesComponente WHERE especificacao = ?", Integer.class, nome);
            idEspecificacoesComponente = idEspecificacoesComponentes.get(0);
        } catch (IndexOutOfBoundsException err) {
            System.out.println("Especificando novo componente");
        } finally {
            if (idEspecificacoesComponente == null) {
                con.update("INSERT INTO EspecificacoesComponente VALUES (null,?,null)", nome);
                idEspecificacoesComponentes = con.queryForList("SELECT idEspecificacoesComponente FROM EspecificacoesComponente WHERE especificacao = ?", Integer.class, nome);
                idEspecificacoesComponente = idEspecificacoesComponentes.get(0);
            }
        }

        return idEspecificacoesComponente;
    }

    ;


    /*--------------------------------------------------------------componentes--------------------------------------------------------------*/

    public void compilarComponente(Integer maquina, Integer tipo, Integer metrica, Integer especificacao) {
        List<Integer> Componentes = new ArrayList<>();
        Integer Componente = null;
        try {
            Componentes = con.queryForList("SELECT idComponente FROM Componente WHERE fkMaquina = ? AND fkTipoComponente =? AND fkMetricaComponente = ? AND fkEspecificacoesComponente =?", Integer.class, maquina, tipo, metrica, especificacao);
            Componente = Componentes.get(0);
        } catch (IndexOutOfBoundsException err) {
            System.out.println("concatenação de componente");
        } finally {
            if (Componente == null) {
                con.update("INSERT INTO Componente VALUES (null,?,?,?,?)", maquina, tipo, metrica, especificacao);
                System.out.println("maquina e componentes acabam de ser cadastrados");
            } else {
                System.out.println("Maquina e componentes verificados e autenticados");
            }
        }
    }

    ;

    /*-----------------------------------------------------------------------------------------------------------------------------------------*/

    Integer selecionarComponente(Integer maquina, Integer tipo) {
        List<Integer> Componentes = con.queryForList("SELECT idComponente FROM Componente WHERE fkMaquina = ? AND fkTipoComponente = ? ", Integer.class, maquina, tipo);
        Integer Componente = Componentes.get(0);
        return Componente;
    }

    public void capturaDeDados(String texto, Double valor, Object timer, Integer componente) {
        con.update("INSERT INTO Log VALUES (null,?,?,?,null,?)", texto, valor, timer, componente);
    }

    ;


    public Integer SelecionarMetricaMin(Integer Componente) {
        List<Integer> MetricasMin = con.queryForList("SELECT minMetrica FROM Componente JOIN MetricaComponente ON fkMetricaComponente = idMetricaComponente WHERE idComponente = ?;", Integer.class);
        Integer MetricaMin = MetricasMin.get(0);
        return MetricaMin;
    }

    public Integer SelecionarMetricaMax(Integer Componente) {
        List<Integer> MetricasMax = con.queryForList("SELECT maxMetrica FROM Componente JOIN MetricaComponente ON fkMetricaComponente = idMetricaComponente WHERE idComponente = ?;", Integer.class,Componente);
        Integer MetricaMax = MetricasMax.get(0);
        return MetricaMax;
    }

    public String DefinindoMaquina(Integer idMaquina) {
        List<String> IdentificacoesNome = con.queryForList("SELECT nomeMaquina FROM maquina WHERE idMaquina = ?;", String.class, idMaquina);
        String IdentificacaoNome = IdentificacoesNome.get(0);


        List<String> IdentificacoesUni = con.queryForList("SELECT unidade FROM maquina join lanhouse on fkLanhouse=idLanhouse where idMaquina =?;", String.class, idMaquina);
        String IdentificacaoUni = IdentificacoesUni.get(0);

        String identificacao = "Nome da Maquina: " + IdentificacaoNome + " Nome da Lanhouse: " + IdentificacaoUni;
        return identificacao;
    }

    ;/*SELECT unidadeMedida from metricaComponente WHERE idMetrica = 1;*/
    public String DefinindoMedida(Integer id){
        List<String> unidadeMedidas = con.queryForList("SELECT unidadeMedida from metricaComponente WHERE idMetricaComponente = ?;", String.class, id);
        String unidadeMedida = unidadeMedidas.get(0);

        return unidadeMedida;
    }
};
