package com.odp.main.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.odp.main.CustomUserDetails;
import com.odp.main.Models.Admin;
import com.odp.main.Repositorys.AdminRepository;

// Spring Security'nin kullanıcı detay servisini özelleştirmek için kullanılan servis.
@Service
public class AdminDetailsService implements UserDetailsService {

    @Autowired
    private AdminRepository adminRepository; // AdminRepository enjekte ediliyor.

    // Kullanıcı adına göre kullanıcı bilgilerini yüklemek için override edilen metod.
    @Override
    public CustomUserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Kullanıcı adını kullanarak admin araması yapılıyor.
        Admin admin = adminRepository.findByUsername(username);
        if (admin == null) {
            // Eğer admin bulunamazsa UsernameNotFoundException fırlatılıyor.
            throw new UsernameNotFoundException("Admin bulunamadı");
        }
        // Bulunan admin için CustomUserDetails objesi oluşturuluyor ve döndürülüyor.
        return new CustomUserDetails(admin);
    }
}
