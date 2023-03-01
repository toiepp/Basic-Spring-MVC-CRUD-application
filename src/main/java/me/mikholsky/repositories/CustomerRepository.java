package me.mikholsky.repositories;

import me.mikholsky.models.Customer;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CustomerRepository {
	private SessionFactory sessionFactory;

	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	public Customer save(Customer customer) {
		sessionFactory.getCurrentSession().persist(customer);
		return customer;
	}

	public void delete(Integer id) {
		Session session = sessionFactory.getCurrentSession();

		Customer toDelete = session.find(Customer.class, id);

		session.remove(toDelete);
	}

	public void delete(Customer customer) {
		sessionFactory.getCurrentSession().remove(customer);
	}

	public Customer update(Customer customer) {
		return sessionFactory.getCurrentSession().merge(customer);
	}

	public Customer findById(Integer id) {
		return sessionFactory.getCurrentSession().find(Customer.class, id);
	}

	public List<Customer> getAll() {
		Session session = sessionFactory.getCurrentSession();

		return session.createQuery("from Customer", Customer.class).getResultList();
	}
}
