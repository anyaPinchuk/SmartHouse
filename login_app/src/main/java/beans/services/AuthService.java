package beans.services;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import entities.User;
import exceptions.ServiceException;
import exceptions.WrongCredentialsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.core.env.StandardEnvironment;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import repository.UserRepository;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

@Service
@PropertySource({"classpath:google_client.properties"})
public class AuthService {
    private Environment environment;
    private UserRepository userRepository;

    public String authenticateGoogleUser(String idtoken) {
        org.springframework.core.env.PropertySource<?> propertySources = ((StandardEnvironment) environment)
                .getPropertySources().get("class path resource [google_client.properties]");
        String roleName;
        try {
            HttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
            JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();
            GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(httpTransport, jsonFactory)
                    .setAudience(Collections.singletonList(((Properties) propertySources.getSource()).getProperty("client.id")))
                    .build();
            GoogleIdToken idToken = verifier.verify(idtoken);
            if (idToken != null) {
                GoogleIdToken.Payload payload = idToken.getPayload();
                String email = payload.getEmail();
                User user = userRepository.findUserByLogin(email);

                if (user == null) {
                    throw new WrongCredentialsException("User not found");
                }

                List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
                roleName = user.getRole();
                grantedAuthorities.add(new SimpleGrantedAuthority(roleName));

                SecurityContextHolder.getContext()
                        .setAuthentication(new UsernamePasswordAuthenticationToken(user, user.getPassword(), grantedAuthorities));
            } else {
                throw new ServiceException("Invalid ID token");
            }
        } catch (GeneralSecurityException | IOException e) {
            throw new ServiceException("auth with google account failed");
        }
        return roleName;
    }

    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Autowired
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }
}
