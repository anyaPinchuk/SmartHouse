package beans.controllers;

import beans.services.AuthService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.security.GeneralSecurityException;

@RestController
@RequestMapping("/api/auth")
public class AuthRESTController {
    private static final Logger LOG = LoggerFactory.getLogger(AuthRESTController.class);
    private AuthService authService;

    @PostMapping("/get")
    public ResponseEntity authenticateWithGoogle(@RequestBody String idtoken) throws GeneralSecurityException, IOException {
        LOG.info("handle post request by url /api/auth/get");
        return ResponseEntity.ok().body(authService.authenticateGoogleUser(idtoken));
    }

    @Autowired
    public void setAuthService(AuthService authService) {
        this.authService = authService;
    }
}
