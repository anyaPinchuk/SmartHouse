package beans.config.security;

import exception.WrongCredentialsException;
import repository.UserRepository;
import entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {

    private UserRepository userRepository;

    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        String email = authentication.getName();
        String password = (String) authentication.getCredentials();

        User user = userRepository.findByLoginAndPassword(email, password);

        if (user == null) {
            throw new WrongCredentialsException("User not found.");
        }

        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        final String roleName = user.getRole();

        grantedAuthorities.add(new SimpleGrantedAuthority(roleName));
        return new UsernamePasswordAuthenticationToken(user, password, grantedAuthorities);

    }


    @Override
    public boolean supports(Class<?> aClass) {
        return true;
    }

}
