package demo.assignment.tree.statementsvc.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "db")
@Data
public class DBUsersProperties {

    private String userUsername ;
    private String userPassword ;
    private String adminUsername;
    private String adminPassword;
}
