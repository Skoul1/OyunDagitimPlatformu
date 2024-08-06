package com.odp.main;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GameRepository extends JpaRepository<Game, Long> {
    List<Game> findByTitleContaining(String title);
    List<Game> findByDescriptionContaining(String description);
    List<Game> findByTitleContainingAndDescriptionContaining(String title, String description);

    // Başlık uzunluğuna göre oyunları sıralama için özel sorgu
    @Query("SELECT g FROM Game g WHERE LENGTH(g.title) = :length")
    List<Game> findByTitleLength(@Param("length") int length);
}
