package com.odp.main.Models;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * Yorumları temsil eden entity sınıfı.
 */
@Entity
@Table(name = "comments") // Veritabanında 'comments' tablosuna karşılık gelir.
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // Yorumun benzersiz tanımlayıcısı

    @Column(nullable = false, length = 500)
    private String content; // Yorum içeriği

    @Column(nullable = false)
    private LocalDateTime timestamp; // Yorumun yapıldığı zaman

    @ManyToOne
    @JoinColumn(name = "game_id", nullable = false)
    private Game game; // Yoruma ait oyun

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user; // Yorumu yapan kullanıcı

    // Getter ve Setter metodları

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
