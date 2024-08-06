	package com.odp.main;
	
	import org.springframework.data.jpa.repository.JpaRepository;
	import org.springframework.stereotype.Repository;

	
	@Repository
	public interface PublisherRepository extends JpaRepository<Publisher, Long> {
		Publisher findByUsername(String username);
	}
