package demo.assignment.tree.statementsvc.filter;

import demo.assignment.tree.statementsvc.model.JwtTokenUtilConfiguration;
import demo.assignment.tree.statementsvc.service.UserManagementService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@Component
public class JwtTokenVerifierFilter extends OncePerRequestFilter {
    private final JwtTokenUtilConfiguration jwtConfig ;
    private final UserManagementService userManagementService ;

    public JwtTokenVerifierFilter(JwtTokenUtilConfiguration jwtConfig, UserManagementService userManagementService) {
        super();
        this.jwtConfig = jwtConfig;
        this.userManagementService = userManagementService;
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

        String token = authHeader.substring(tokenPrefix.length());
        userManagementService.validateToken(token);
        filterChain.doFilter(request, response);

    }
}
