package com.odp.main.Models;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Collections;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import jakarta.persistence.*;

/**
 * Yönetici kullanıcılarını temsil eden entity sınıfı.
 */
@Entity
@Table(name = "admins")
public class Admin implements UserDetails {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username; // Yöneticinin kullanıcı adı

    @Column(nullable = false)
    private String password; // Yöneticinin şifresi

    @Column(nullable = false)
    private BigDecimal balance; // Yöneticinin bakiyesi

    // Varsayılan yapıcı
    public Admin() {
        this.balance = BigDecimal.ZERO;
    }

    // Parametreli yapıcı
    public Admin(String username, String password) {
        this.username = username;
        this.password = password;
        this.balance = BigDecimal.ZERO; // Başlangıç bakiyesi
    }

    // Getter ve setter metotları
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    /**
     * Yöneticiye atanmış yetkileri döndürür.
     * ROLE_ADMIN yetkisi olan bir koleksiyon döndürür.
     *
     * @return GrantedAuthority koleksiyonu
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singleton(new SimpleGrantedAuthority("ROLE_ADMIN"));
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
