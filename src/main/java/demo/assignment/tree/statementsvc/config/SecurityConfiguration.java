package demo.assignment.tree.statementsvc.config;

import demo.assignment.tree.statementsvc.filter.JwtAuthenticationFilter;
import demo.assignment.tree.statementsvc.filter.JwtTokenVerifierFilter;
import demo.assignment.tree.statementsvc.model.JwtTokenUtilConfiguration;
import demo.assignment.tree.statementsvc.service.UserManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    private final JwtTokenUtilConfiguration jwtConfig ;
    @Autowired
    private final UserManagementService userManagementService ;
    @Autowired final DBUsersProperties dbUsersProperties ;

    public SecurityConfiguration(JwtTokenUtilConfiguration jwtConfig, UserManagementService userManagementService, DBUsersProperties dbUsersProperties) {
        this.jwtConfig = jwtConfig;
        this.userManagementService = userManagementService;
        this.dbUsersProperties = dbUsersProperties;
    }


    @Override
    @Bean
    protected UserDetailsService userDetailsService() {
        UserDetails user = User.withUsername(dbUsersProperties.getUserUsername())
                .password(getPasswordEncoder().encode(dbUsersProperties.getUserPassword()))
                .roles("USER")
                .build();

        UserDetails admin = User.withUsername(dbUsersProperties.getAdminUsername())
                .password(getPasswordEncoder().encode(dbUsersProperties.getAdminPassword()))
                .roles("USER", "ADMIN")
                .build();

        return new InMemoryUserDetailsManager(
                user, admin);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.headers().frameOptions().disable();
        http.csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .addFilter(new JwtAuthenticationFilter(authenticationManager() , jwtConfig))
                .addFilterAfter(new JwtTokenVerifierFilter(jwtConfig, userManagementService) ,JwtAuthenticationFilter.class )
                .authorizeRequests()
                .anyRequest()
                .authenticated();
    }

    @Bean
    public PasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder() ;
    }


}