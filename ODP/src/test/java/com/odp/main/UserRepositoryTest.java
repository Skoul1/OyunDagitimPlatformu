package com.odp.main;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Date;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;

import com.odp.main.Models.Publisher;
import com.odp.main.Models.User;
import com.odp.main.Repositorys.PublisherRepository;
import com.odp.main.Repositorys.UserRepository;

import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class UserRepositoryTest {

	@Autowired
	private UserRepository repo;

	@Autowired
	private TestEntityManager entityManager;

	@Test
	public void testCreateUser() {
		User newUser = new User();
		newUser.setUsername("asdasasdasddasd");
		newUser.setPassword("passwoasdaasd23");
		newUser.setFirstName("Joas123"); // firstName alanını doldurmayı unutmayın
		newUser.setLastName("123dsa");
		newUser.setEmail("123hrgs@example.com");

		repo.save(newUser); // User'ı kaydedin
	}


	@Autowired
	private PublisherRepository publisherRepository;

	@Test
	public void testCreatePublisher() {
		Publisher publisher = new Publisher();
		publisher.setUsername("testPublisher");
		publisher.setPassword("testPassword"); // Normalde şifreleme işlemi burada yapılmaz, test amaçlı direkt
												// veriyorum
		publisher.setCompanyName("Test Company");
		publisher.setContactEmail("test@example.com");
		publisher.setWebsite("http://www.testcompany.com");
		publisher.setPhoneNumber("1234567890");
		publisher.setAddress("Test Address");
		publisher.setDescription("This is a test description");
		publisher.setBirthDate(new Date());

		Publisher savedPublisher = publisherRepository.save(publisher);

		Publisher existPublisher = entityManager.find(Publisher.class, savedPublisher.getId());
		assertThat(existPublisher.getUsername()).isEqualTo(publisher.getUsername());
	}
}
