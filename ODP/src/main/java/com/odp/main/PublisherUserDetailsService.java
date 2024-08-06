package com.odp.main;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class PublisherUserDetailsService implements UserDetailsService {
    @Autowired
    private PublisherRepository repo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Publisher publisher = repo.findByUsername(username);
        if (publisher == null) {
            throw new UsernameNotFoundException("Publisher not found");
        }
        return new CustomUserDetails(publisher);
    }
}
