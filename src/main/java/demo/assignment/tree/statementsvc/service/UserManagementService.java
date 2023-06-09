package demo.assignment.tree.statementsvc.service;

import demo.assignment.tree.statementsvc.model.JwtTokenUtilConfiguration;
import demo.assignment.tree.statementsvc.model.User;
import demo.assignment.tree.statementsvc.repo.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
public class UserManagementService {

    private final JwtTokenUtilConfiguration jwtConfig ;
    private final UserRepository userRepository ;

    @Autowired
    public UserManagementService(JwtTokenUtilConfiguration jwtConfig, UserRepository userRepository) {
        super();
        this.jwtConfig = jwtConfig;
        this.userRepository = userRepository;
    }
    public void saveUserToken(String token){

        Jws<Claims> jwtClaims = Jwts.parserBuilder()
                .setSigningKey(jwtConfig.getSecretKey())
                .build()
                .parseClaimsJws(token);

        String username = jwtClaims.getBody().getSubject();

        final var optionalUser = userRepository.findById(username);
        if(optionalUser.isPresent()){
            throw new IllegalStateException("user is already logged in ..");
        }


        User user = User.builder()
                .setUsername(username)
                .setToken(token)
                .build();
        userRepository.save(user);
    }

    public void logoutUser(String token){
        // remove from redis cache

        Jws<Claims> jwtClaims = Jwts.parserBuilder()
                .setSigningKey(jwtConfig.getSecretKey())
                .build()
                .parseClaimsJws(token);

        if(!validateToken(token)){
            throw new IllegalStateException("session already expired .. ");
        }

        String username = jwtClaims.getBody().getSubject();
        userRepository.deleteById(username);

    }


    public boolean validateToken(String token){

        try {

            Jws<Claims> jwtClaims = Jwts.parserBuilder()
                    .setSigningKey(jwtConfig.getSecretKey())
                    .build()
                    .parseClaimsJws(token);

            String username = jwtClaims.getBody().getSubject();
            final var dbUser = userRepository.findById(username)
                    .orElseThrow(() ->  new IllegalStateException("user is not logged in before .."));

            final var dbUserToken = dbUser.getToken();

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

        return true ;

    }



}
