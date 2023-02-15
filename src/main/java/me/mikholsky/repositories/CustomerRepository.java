package me.mikholsky.repository;

import me.mikholsky.models.Customer;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
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

	public void save(Customer customer) {
		sessionFactory.getCurrentSession().persist(customer);
	}

	public void delete(int id) {
		Session session = sessionFactory.getCurrentSession();

		Query<Customer> query = session.createQuery("from Customer where id=:id", Customer.class);

		query.setParameter("id", id);

		Customer toDelete = query.getSingleResult();

		session.remove(toDelete);
	}

	public void delete(Customer customer) {
		sessionFactory.getCurrentSession().remove(customer);
	}

	public List<Customer> getAll() {
		return sessionFactory.getCurrentSession()
							 .createQuery("from Customer", Customer.class)
							 .getResultList();
	}
}
