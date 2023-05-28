package demo.assignment.tree.statementsvc.controller;

import demo.assignment.tree.statementsvc.model.JwtTokenUtilConfiguration;
import demo.assignment.tree.statementsvc.service.UserManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping(value = "v1/statements")
public class UserSessionController {

    private final UserManagementService userManagementService ;


    private final JwtTokenUtilConfiguration jwtConfig ;
    @Autowired
    public UserSessionController(UserManagementService userManagementService, JwtTokenUtilConfiguration jwtConfig) {
        this.userManagementService = userManagementService;
        this.jwtConfig = jwtConfig;
    }

    @PostMapping("logout")
    public ResponseEntity<String> logout(HttpServletRequest request){
        String authHeader = request.getHeader(org.springframework.http.HttpHeaders.AUTHORIZATION);
        String tokenPrefix = jwtConfig.getTokenPrefix();
        String token = authHeader.substring(tokenPrefix.length());
        userManagementService.logoutUser(token);

        return ResponseEntity.ok("Logged out successfully ..");
    }
}
