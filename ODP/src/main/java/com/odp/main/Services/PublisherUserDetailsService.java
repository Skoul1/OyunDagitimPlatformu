package com.odp.main.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.odp.main.CustomUserDetails;
import com.odp.main.Models.Publisher;
import com.odp.main.Repositorys.PublisherRepository;

// Yayıncılar için kullanıcı detayları servisi.
@Service
public class PublisherUserDetailsService implements UserDetailsService {
    // PublisherRepository bağımlılığını enjekte ediyoruz.
    @Autowired
    private PublisherRepository repo;

    // Verilen kullanıcı adına göre kullanıcı detaylarını yükler.
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Username'e göre yayıncıyı veritabanından arar.
        Publisher publisher = repo.findByUsername(username);
        // Eğer yayıncı bulunamazsa bir hata fırlatır.
        if (publisher == null) {
            throw new UsernameNotFoundException("Yayıncı bulunamadı");
        }
        // Bulunan yayıncı için bir CustomUserDetails nesnesi oluşturur ve döndürür.
        return new CustomUserDetails(publisher);
    }
}
