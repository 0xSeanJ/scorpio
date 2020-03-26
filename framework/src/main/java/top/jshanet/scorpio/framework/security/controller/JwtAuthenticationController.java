package top.jshanet.scorpio.framework.security.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.jshanet.scorpio.framework.security.domain.JwtAuthenticationToken;
import top.jshanet.scorpio.framework.security.domain.UserCredentials;
import top.jshanet.scorpio.framework.security.service.JwtAuthenticationService;

@RestController
@RequestMapping(value = "/api/auth")
@ConditionalOnProperty(prefix = "scorpio.security.jwt", name = "authentication", matchIfMissing = true)
public class JwtAuthenticationController {

    private final JwtAuthenticationService authenticationService;

    @Autowired
    public JwtAuthenticationController(JwtAuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping("/authenticate")
    public JwtAuthenticationToken authenticate(
            @RequestBody UserCredentials userCredentials) {
        return authenticationService.authenticate(userCredentials);
    }
}
