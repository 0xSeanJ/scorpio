package top.jshanet.scorpio.framework.security.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.jshanet.scorpio.framework.security.autoconfig.properties.JwtSecurityProperties;
import top.jshanet.scorpio.framework.security.domain.JwtAuthenticationToken;
import top.jshanet.scorpio.framework.security.domain.UserCredentials;
import top.jshanet.scorpio.framework.security.service.JwtAuthenticationService;

@RestController
@ConditionalOnProperty(prefix = "scorpio.security.jwt", name = "", matchIfMissing = true)
public class JwtAuthenticationController {

    private final JwtAuthenticationService authenticationService;

    private final JwtSecurityProperties properties;

    @Autowired
    public JwtAuthenticationController(JwtAuthenticationService authenticationService, JwtSecurityProperties properties) {
        this.authenticationService = authenticationService;
        this.properties = properties;
    }

    @PostMapping("/user/auth")
    public JwtAuthenticationToken authenticate(
            @RequestBody UserCredentials userCredentials) {
        return authenticationService.authenticate(userCredentials);
    }
}
