package demo.assignment.tree.statementsvc.filter;

import demo.assignment.tree.statementsvc.model.JwtTokenUtilConfiguration;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
@Slf4j
public class JwtTokenVerifierFilter extends OncePerRequestFilter {
    private final JwtTokenUtilConfiguration jwtConfig ;

    public JwtTokenVerifierFilter(JwtTokenUtilConfiguration jwtConfig) {
        super();
        this.jwtConfig = jwtConfig;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String authHeader = request.getHeader(org.springframework.http.HttpHeaders.AUTHORIZATION);
        String tokenPrefix = jwtConfig.getTokenPrefix();



        if(authHeader == null || authHeader.isEmpty() || !authHeader.startsWith(tokenPrefix)) {
            filterChain.doFilter(request, response);
            return ;
        }

        String token = authHeader.substring(tokenPrefix.length(), authHeader.length());

        try {

            Jws<Claims> jwtClaims = Jwts.parserBuilder()
                    .setSigningKey(jwtConfig.getSecretKey())
                    .build()
                    .parseClaimsJws(token);

            String username = jwtClaims.getBody().getSubject();
            if(!jwtClaims.getBody().getAudience().equals(jwtConfig.getAudiance()) || !jwtClaims.getBody().getIssuer().equals(jwtConfig.getIssuer()))
                throw new IllegalStateException("Token cannot be trusted ");

            var grantedAuths = (List<Map<String , String>>)jwtClaims.getBody().get("authorities");

            Set<SimpleGrantedAuthority> authoritiesSet = grantedAuths.stream()
                    .map(m -> new SimpleGrantedAuthority(m.get("authority")))
                    .collect(Collectors.toSet());

            // we have to map the authorities to the set of SimpleGrantedAuthorities
            Authentication auth = new UsernamePasswordAuthenticationToken(
                    username,
                    null ,
                    authoritiesSet);


            SecurityContextHolder.getContext().setAuthentication(auth);

        }
        catch(JwtException e) {
            log.error("JwtException: Token cannot be trusted" , e);
            throw new IllegalStateException("Token cannot be trusted ");
        }
        catch(Exception e) {
            log.error("Exception: Token cannot be trusted" , e);
            throw new IllegalStateException("Token cannot be trusted ");
        }

        filterChain.doFilter(request, response);

    }
}
