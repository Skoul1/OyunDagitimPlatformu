package com.odp.main.Repositorys;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.odp.main.Models.User;

/**
 * Kullanıcı nesnelerine yönelik veritabanı erişim işlemlerini sağlayan
 * repository sınıfı.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

	/**
	 * Belirli bir kullanıcı adına sahip kullanıcıyı döndürür.
	 * 
	 * @param username Aranacak kullanıcının kullanıcı adı
	 * @return İstenen kullanıcı adına sahip kullanıcı nesnesi, eğer mevcutsa
	 */
	User findByUsername(String username);
}
