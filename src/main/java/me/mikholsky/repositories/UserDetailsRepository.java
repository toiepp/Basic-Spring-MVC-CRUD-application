package me.mikholsky.repositories;

import me.mikholsky.models.User;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class UserDetailsRepository {
	private SessionFactory sessionFactory;

	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	public User save(User user) {
		sessionFactory.getCurrentSession().persist(user);
		return user;
	}

	public User findByUsername(String username) {
		return sessionFactory.getCurrentSession()
							 .createQuery("from User where username=:username", User.class)
							 .setParameter("username", username)
							 .getSingleResult();
	}
}
