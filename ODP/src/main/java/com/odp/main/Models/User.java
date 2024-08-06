package com.odp.main.Models;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.persistence.*;

/**
 * Kullanıcıları temsil eden entity sınıfı.
 */
@Entity
@Table(name = "users")
public class User implements UserDetails {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long userId; // Kullanıcının benzersiz tanımlayıcısı

    @Column(name = "username", nullable = false, unique = true, length = 20)
    private String username; // Kullanıcı adı

    @Column(name = "password", nullable = false, length = 64)
    private String password; // Kullanıcının şifresi

    @Column(name = "first_name", nullable = false, length = 20)
    private String firstName; // Kullanıcının adı

    @Column(name = "last_name", nullable = false, length = 20)
    private String lastName; // Kullanıcının soyadı

    @Column(name = "email", nullable = false, unique = true, length = 45)
    private String email; // Kullanıcının e-posta adresi

    @ManyToMany
    @JoinTable(name = "user_games", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "game_id"))
    private List<Game> library; // Kullanıcının oyun kütüphanesi

    // Getter ve setter metotları

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
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

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<Game> getLibrary() {
        return library;
    }

    public void setLibrary(List<Game> library) {
        this.library = library;
    }

    /**
     * Kullanıcının yetkilerini döndürür. ROLE_USER yetkisine sahip bir koleksiyon döndürür.
     * @return Yetkilerin bir koleksiyonu
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singleton(new SimpleGrantedAuthority("ROLE_USER"));
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
