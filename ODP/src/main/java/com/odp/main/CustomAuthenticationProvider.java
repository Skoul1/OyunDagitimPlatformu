package com.odp.main;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.odp.main.Models.Publisher;
import com.odp.main.Models.User;
import com.odp.main.Repositorys.PublisherRepository;
import com.odp.main.Repositorys.UserRepository;

import java.util.Arrays;

// CustomAuthenticationProvider, özel bir kimlik doğrulama yöntemi sağlar.
@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {

    @Autowired
    private UserRepository userRepository; // Kullanıcı verilerine erişim sağlar.
    
    @Autowired
    private PublisherRepository publisherRepository; // Yayıncı verilerine erişim sağlar.

    @Autowired
    private PasswordEncoder passwordEncoder; // Şifreleri kodlamak ve doğrulamak için kullanılır.

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName(); // Kimlik doğrulama nesnesinden kullanıcı adını alır.
        String password = authentication.getCredentials().toString(); // Kimlik doğrulama nesnesinden şifreyi alır.

        // Yayıncı olarak kimlik doğrulama kontrolü
        Publisher publisher = publisherRepository.findByUsername(username);
        if (publisher != null && passwordEncoder.matches(password, publisher.getPassword())) {
            return new UsernamePasswordAuthenticationToken(username, password, Arrays.asList(new SimpleGrantedAuthority("ROLE_PUBLISHER")));
        }

        // Kullanıcı olarak kimlik doğrulama kontrolü
        User user = userRepository.findByUsername(username);
        if (user != null && passwordEncoder.matches(password, user.getPassword())) {
            return new UsernamePasswordAuthenticationToken(username, password, Arrays.asList(new SimpleGrantedAuthority("ROLE_USER")));
        }

        return null; // Kimlik doğrulama başarısızsa null döner.
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class); // Desteklenen kimlik doğrulama türünü belirler.
    }
}
