package com.odp.main.Models;

import java.math.BigDecimal;
import java.util.List;
import jakarta.persistence.*;

/* Oyun bilgilerini saklamak için kullanılan entity sınıfı. */
@Entity
@Table(name = "games") // Veritabanında 'games' tablosuna karşılık gelir.
public class Game {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Otomatik artan birincil anahtar.
    private Long id;

    @Column(nullable = false)
    private String title; // Oyunun başlığı.

    @Column(nullable = false)
    private String description; // Oyunun açıklaması.

    @Column(nullable = false)
    private String category; // Oyunun kategorisi.

    @Column(nullable = false)
    private BigDecimal price; // Oyunun fiyatı.

    private String imagePath; // Oyunun resim dosyasının yolu.
    private String gameFilePath; // Oyunun dosya yolunu saklar.
    private String status; // Oyunun durumu: 'Pending', 'Approved', 'Rejected'

    @Column(name = "publisher_id", nullable = false) // Oyunu yayınlayan yayıncının ID'si.
    private Long publisherId;

    @OneToMany(mappedBy = "game", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Comment> comments;

    // Getter ve setter metotları aşağıdadır.

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getGameFilePath() {
        return gameFilePath;
    }

    public void setGameFilePath(String gameFilePath) {
        this.gameFilePath = gameFilePath;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getPublisherId() {
        return publisherId;
    }

    public void setPublisherId(Long publisherId) {
        this.publisherId = publisherId;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    /**
     * Resim dosyasının adını döndürür. Eğer resim yolu belirtilmişse, dosya adını
     * yolundan ayırarak döndürür.
     * 
     * @return Resim dosyasının adı.
     */
    public String getImageFileName() {
        if (imagePath != null) {
            int lastSeparatorIndex = Math.max(imagePath.lastIndexOf("\\"), imagePath.lastIndexOf("/"));
            if (lastSeparatorIndex >= 0) {
                return imagePath.substring(lastSeparatorIndex + 1);
            }
        }
        return imagePath;
    }
}
