package com.odp.main.Repositorys;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.odp.main.Models.Game;
import java.util.List;

/**
 * Game nesnelerine yönelik veritabanı erişim işlemlerini sağlayan repository
 * sınıfı.
 */
@Repository
public interface GameRepository extends JpaRepository<Game, Long> {

	/**
	 * Belirli bir yayıncıya ait oyunları döndürür.
	 * 
	 * @param publisherId Aranacak yayıncının kimlik numarası
	 * @return Belirtilen yayıncı kimliğine sahip oyunların listesi
	 */
	List<Game> findByPublisherId(Long publisherId);

	/**
	 * Oyunların durumuna göre listelenmesini sağlar.
	 * 
	 * @param status Aranacak oyun durumu ('Pending', 'Approved', 'Rejected' gibi)
	 * @return Belirtilen duruma sahip oyunların listesi
	 */
	List<Game> findByStatus(String status);
}
