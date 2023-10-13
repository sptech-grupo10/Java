import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.jdbc.core.JdbcTemplate;

public class Conexao {
    private JdbcTemplate conexaoDoBanco;

    public Conexao() {
        BasicDataSource dataSource = new BasicDataSource();
        /*
             Exemplo de driverClassName:
                 <- EXEMPLO PARA MYSQL
                com.microsoft.sqlserver.jdbc.SQLServerDriver <- EXEMPLO PARA SQL SERVER
        */
        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        /*
             Exemplo de string de conexões:
                jdbc:mysql://localhost:3306/mydb <- EXEMPLO PARA MYSQL
                jdbc:sqlserver://localhost:1433;database=mydb <- EXEMPLO PARA SQL SERVER
        */

        /*-Caso precise fazer alteração nessa parte Não apagem apenas comentem-*/

        dataSource.setUrl("jdbc:mysql://127.0.0.1:3306/ByteGuard");
        dataSource.setUsername("aluno");
        dataSource.setPassword("aluno");



        conexaoDoBanco = new JdbcTemplate(dataSource);

    }
    public JdbcTemplate getConexaoDoBanco(){return conexaoDoBanco;};

}
