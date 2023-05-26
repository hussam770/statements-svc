package demo.assignment.tree.statementsvc.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;
import java.io.File;
import java.util.Objects;

@Configuration
public class SpringJdbcConfig {

    @Bean
    public DataSource mysqlDataSource() {
        ClassLoader classLoader = getClass().getClassLoader();
        final File dbFileName = new
                File(Objects.requireNonNull(classLoader.getResource("accountsdb.accdb")).getFile());

        final String jdbcUrl = "jdbc:ucanaccess://" + dbFileName +  ";openExclusive=false;ignoreCase=true";

        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("net.ucanaccess.jdbc.UcanaccessDriver");
        dataSource.setUrl(jdbcUrl);

        return dataSource;
    }
}
