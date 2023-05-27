package demo.assignment.tree.statementsvc.model;

import io.jsonwebtoken.security.Keys;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.crypto.SecretKey;

@Configuration
@ConfigurationProperties(prefix = "application.security.jwt")
@Data
public class JwtTokenUtilConfiguration {


    private String issuer ;
    private String audiance ;
    private int expirationDays ;
    private String key ;
    private String tokenPrefix ;


    @Bean
    public SecretKey getSecretKey() {
        return Keys.hmacShaKeyFor(getKey().getBytes());
    }
}
