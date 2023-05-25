package demo.assignment.tree.statementsvc.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;
import java.io.File;

@Configuration
public class SpringJdbcConfig {

    @Bean
    public DataSource mysqlDataSource() {
        ClassLoader classLoader = getClass().getClassLoader();
        final File dbFileName = new
                File(classLoader.getResource("accountsdb.accdb").getFile());

        final String jdbcUrl = "jdbc:ucanaccess://" + dbFileName +  ";openExclusive=false;ignoreCase=true";

        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("net.ucanaccess.jdbc.UcanaccessDriver");
        dataSource.setUrl(jdbcUrl);
        //dataSource.setUsername("guest_user");
        //dataSource.setPassword("guest_password");

        return dataSource;
    }
}
