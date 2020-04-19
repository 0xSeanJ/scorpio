package top.jshanet.scorpio.framework.security.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;
import top.jshanet.scorpio.framework.common.dto.ScorpioRestMessage;
import top.jshanet.scorpio.framework.security.autoconfig.properties.JwtSecurityProperties;
import top.jshanet.scorpio.framework.security.domain.JwtAuthenticationToken;
import top.jshanet.scorpio.framework.security.domain.UserCredentials;
import top.jshanet.scorpio.framework.security.service.JwtAuthenticationService;
import top.jshanet.scorpio.framework.web.controller.ScorpioBaseController;

@RestController
@ConditionalOnProperty(prefix = "scorpio.security.jwt", name = "", matchIfMissing = true)
public class JwtAuthenticationController extends ScorpioBaseController {

    private final JwtAuthenticationService authenticationService;

    private final JwtSecurityProperties properties;

    @Autowired
    public JwtAuthenticationController(JwtAuthenticationService authenticationService, JwtSecurityProperties properties) {
        this.authenticationService = authenticationService;
        this.properties = properties;
    }

    @PostMapping("/users/auth")
    public DeferredResult<ScorpioRestMessage<JwtAuthenticationToken>> authenticate(
            @RequestBody UserCredentials userCredentials) {
        return execute(userCredentials, request -> toRestMessage(authenticationService.authenticate(request)));
    }
}
