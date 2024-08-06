package com.odp.main;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.odp.main.Models.Admin;
import com.odp.main.Models.Publisher;
import com.odp.main.Models.User;

import java.util.Collection;

// CustomUserDetails, Spring Security tarafından kullanıcı kimlik doğrulaması için gerekli kullanıcı detaylarını sağlar.
public class CustomUserDetails implements UserDetails {
    private String username;
    private String password;
    private Collection<? extends GrantedAuthority> authorities;
    private Publisher publisher;  // Yayıncı nesnesi için eklenen özel alan
    private Admin admin; // Admin nesnesi için eklenen özel alan

    // User türü için constructor
    public CustomUserDetails(User user) {
        this.username = user.getUsername();
        this.password = user.getPassword();
        this.authorities = user.getAuthorities();
    }

    // Publisher türü için constructor
    public CustomUserDetails(Publisher publisher) {
        this.username = publisher.getUsername();
        this.password = publisher.getPassword();
        this.authorities = publisher.getAuthorities();
        this.publisher = publisher;  // Publisher nesnesini bu nesneye kaydet
    }

 // Admin türü için constructor
    public CustomUserDetails(Admin admin) {
        this.username = admin.getUsername();
        this.password = admin.getPassword();
        this.authorities = admin.getAuthorities();
        this.admin = admin; // Admin nesnesini bu nesneye kaydet
    }
    public Admin getAdmin() {
        return this.admin;
    }
    // Kullanıcının yetkilerini döndüren metod
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    // Kullanıcının şifresini döndüren metod
    @Override
    public String getPassword() {
        return password;
    }

    // Kullanıcının adını döndüren metod
    @Override
    public String getUsername() {
        return username;
    }

    // Hesabın süresinin dolup dolmadığını belirten metod
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    // Hesabın kilitli olup olmadığını belirten metod
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    // Kullanıcının kimlik bilgilerinin süresinin dolup dolmadığını belirten metod
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    // Kullanıcının etkin olup olmadığını belirten metod
    @Override
    public boolean isEnabled() {
        return true;
    }

    // Publisher nesnesine erişimi sağlayan getter metodu
    public Publisher getPublisher() {
        return this.publisher;
    }
}
