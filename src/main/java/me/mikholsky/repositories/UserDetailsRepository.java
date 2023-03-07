package me.mikholsky.repositories;

import me.mikholsky.models.User;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;

@Repository
public class UserDetailsRepository {
	private SessionFactory sessionFactory;

	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	public User save(UserDetails userDetails) {
		sessionFactory.getCurrentSession().persist(userDetails);
		return ((User) userDetails);
	}

	public User findByUsername(String username) {
		return sessionFactory.getCurrentSession()
							 .createQuery("from User where username=:username", User.class)
							 .setParameter("username", username)
							 .getSingleResult();
	}
}
