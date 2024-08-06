package com.odp.main;

import org.springframework.beans.factory.annotation.Autowired;

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
		User user = new User();

		user.setUsername("123333333");
		user.setPassword("12333333");
		user.setFirstName("123333333");
		user.setLastName("12333333");
		user.setEmail("123333@gmail.com");

		User savedUser = repo.save(user);

		User existUser = entityManager.find(User.class, savedUser.getUserID());
		assertThat(existUser.getEmail()).isEqualTo(user.getEmail());
	}

	@Test
	public void testFindUserByUsername() {

		String Username = "ds";

		User user = repo.findByUsername(Username);

		assertThat(user).isNotNull();
	}
}
