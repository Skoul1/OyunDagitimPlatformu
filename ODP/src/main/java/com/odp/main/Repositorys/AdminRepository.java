package com.odp.main.Repositorys;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.odp.main.Models.Admin;

/**
 * Admin nesnelerine yönelik veritabanı erişim işlemlerini sağlar.
 */
@Repository
public interface AdminRepository extends JpaRepository<Admin, Long> {

	/**
	 * Verilen kullanıcı adına göre Admin nesnesini veritabanından bulup döndürür.
	 * 
	 * @param username Aranacak kullanıcı adı
	 * @return Eşleşen Admin nesnesi, eğer bulunamazsa null döner
	 */
	Admin findByUsername(String username);
}
