package me.mikholsky.services;

import jakarta.transaction.Transactional;
import me.mikholsky.models.User;
import me.mikholsky.repositories.UserDetailsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class UserDetailsServiceImpl implements UserDetailsService {
	private UserDetailsRepository userDetailsRepository;

	@Autowired
	public void setUserDetailsRepository(UserDetailsRepository userDetailsRepository) {
		this.userDetailsRepository = userDetailsRepository;
	}

	@Override
	public User loadUserByUsername(String username) {
		return userDetailsRepository.findByUsername(username);
	}

	public User save(User userDetails) {
		return userDetailsRepository.save(userDetails);
	}
}
