package com.odp.main.Repositorys;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.odp.main.Models.Publisher;

/**
 * Publisher nesnelerine yönelik veritabanı erişim işlemlerini sağlayan
 * repository sınıfı.
 */
@Repository
public interface PublisherRepository extends JpaRepository<Publisher, Long> {

	/**
	 * Belirli bir kullanıcı adına sahip yayıncıyı döndürür.
	 * 
	 * @param username Aranacak yayıncının kullanıcı adı
	 * @return İstenen kullanıcı adına sahip yayıncı nesnesi, eğer mevcutsa
	 */
	Publisher findByUsername(String username);
}
