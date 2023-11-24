import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.jdbc.core.JdbcTemplate;

public class ConexaoSQL {
    private JdbcTemplate connection;

    public ConexaoSQL() {
        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setDriverClassName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        dataSource.setUrl("jdbc:sqlserver://54.159.238.176:1433;" +
                "database=ByteGuard;" +
                "user=sa;" +
                "password=sqlwindols;" +
                "trustServerCertificate=true;");
        dataSource.setUsername("sa");
        dataSource.setPassword("sqlwindols");
        this.connection = new JdbcTemplate(dataSource);
    }

    public JdbcTemplate getConnection() {
        return this.connection;
    }
}