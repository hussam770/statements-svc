package demo.assignment.tree.statementsvc.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import demo.assignment.tree.statementsvc.model.JwtTokenUtilConfiguration;
import demo.assignment.tree.statementsvc.model.UserNamePasswordAuthenticationRequest;
import io.jsonwebtoken.Jwts;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.UUID;

public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private final AuthenticationManager authenticationManager ;
    private final JwtTokenUtilConfiguration jwtConfig ;

    public JwtAuthenticationFilter(AuthenticationManager authenticationManager , JwtTokenUtilConfiguration jwtConfig ) {
        super();
        this.authenticationManager = authenticationManager;
        this.jwtConfig = jwtConfig ;
        setFilterProcessesUrl("/api/v1/secureLogin");
    }


    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
                                            Authentication authResult)  {

        String token = Jwts.builder()
                .setIssuer(jwtConfig.getIssuer())
                .setAudience(jwtConfig.getAudiance())
                .setSubject(authResult.getName())
                .setId(UUID.randomUUID().toString())
                .claim("authorities", authResult.getAuthorities())
                .setExpiration(new Date(System.currentTimeMillis() + 60 * 5 * 1000))
                .signWith(jwtConfig.getSecretKey())
                .compact();

        String tokenPrefix = "Bearer";

        response.setHeader("Authorization", tokenPrefix.concat(token));

    }


    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {

        try {
            UserNamePasswordAuthenticationRequest authenticationRequest = new ObjectMapper().readValue(request.getInputStream(), UserNamePasswordAuthenticationRequest.class);

            Authentication auth = new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername()
                    , authenticationRequest.getPassword());

            return authenticationManager.authenticate(auth);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
