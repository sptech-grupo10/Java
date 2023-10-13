import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

public class AcessoGeralJDBC {
    Conexao conexao = new Conexao();
    JdbcTemplate con = conexao.getConexaoDoBanco();

    /*LOGIN*/

    public Integer DescobrirIdEmpresa (String nome){
        List<Integer> idEmpresas = con.queryForList("SELECT idEmpresa FROM Empresa WHERE nome = ?", Integer.class, nome);

    Integer idEmpresa = idEmpresas.get(0);
        return idEmpresa;
    };

}
