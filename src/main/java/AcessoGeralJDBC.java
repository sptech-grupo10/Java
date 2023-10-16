import org.springframework.jdbc.core.JdbcTemplate;

import java.time.format.DateTimeFormatter;
import java.util.List;

public class AcessoGeralJDBC {
    Conexao conexao = new Conexao();
    JdbcTemplate con = conexao.getConexaoDoBanco();

    /*LOGIN*/

    public Integer DescobrirIdEmpresa(String nome) {
        List<Integer> idEmpresas = con.queryForList("SELECT idEmpresa FROM Empresa WHERE nome = ?", Integer.class, nome);

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
        List<Integer> idLanHouses = con.queryForList("SELECT idLanHouse FROM LanHouse WHERE nome = ?", Integer.class, nome);
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
        List<Integer> idUsuarios = con.queryForList("SELECT idUsuario FROM Usuarios WHERE nome = ? AND senha = ?", Integer.class, nome, senha);
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
        List<Integer> idMaquinas = con.queryForList("SELECT idMaquina FROM Maquina WHERE nomeMaquina = ? AND fkLanHouse = ?", Integer.class, nome, fkLanHouse);
        Integer idMaquina = idMaquinas.get(0);

        if (idMaquina == null) {
            con.update("INSERT INTO Maquina VALUES (null,?,?)", nome, fkLanHouse);
            inserirMaquina(nome, fkLanHouse);
        }
        return idMaquina;
    }

    ;

    /*--------------------------------------------------------------Especificação de componentes--------------------------------------------------------------*/
    public Integer inserirNomeDoComponente(String nome) {
        List<Integer> idEspecificacoesComponentes = con.queryForList("SELECT id FROM EspecificacoesComponente WHERE tipoComponente = ?", Integer.class, nome);
        Integer idEspecificacoesComponente = idEspecificacoesComponentes.get(0);
        if (idEspecificacoesComponente == null) {
            con.update("INSERT INTO tipoComponente VALUES (null,?)", nome);
            inserirNomeDoComponente(nome);
        }
        return idEspecificacoesComponente;
    }

    ;


    /*--------------------------------------------------------------componentes--------------------------------------------------------------*/

    public void compilarComponente(Integer maquina, Integer tipo, Integer metrica, Integer especificacao) {
        List<Integer> Componentes = con.queryForList("SELECT id FROM Componente WHERE fkMaquina = ? AND fkTipoComponente =? AND fkMetrica = ? AND fkEspecificacoesComponentes =?", Integer.class, maquina, tipo, metrica, especificacao);
        Integer Componente = Componentes.get(0);
        if (Componentes == null) {
            con.update("INSERT INTO Componente VALUES (null,?,?,?,?)", maquina, tipo, metrica, especificacao);
            System.out.println("maquina e componentes acabam de ser cadastrados");
        } else {
            System.out.println("Maquina e componentes verificados e autenticados");
        }

    };

    Integer selecionarComponente(Integer maquina,Integer tipo){
        List<Integer> Componentes = con.queryForList("SELECT id FROM Componente WHERE fkMaquina = ? ", Integer.class, maquina, tipo );
        Integer Componente = Componentes.get(0);
        return Componente;
    }

    public void capturaDeDados(String texto, Double valor, Object timer, Integer componente){
        con.update("INSERT INTO Log VALUES (null,?,?,?,?)", texto, valor, timer,componente);
    };

};