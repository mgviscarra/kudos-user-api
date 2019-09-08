package com.mgvr.kudos.user.api.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import com.mgvr.kudos.user.api.model.User;

@Repository
@Transactional
public class UserDao {
	@Autowired
	private SessionFactory factory;

	public void saveUser(User user) {
		getSession().save(user);
	}
	

	public List<User> getAllUsers() {
		return getSession().createCriteria(User.class).list();
	}
	
	public void updateUser(User user) {
		getSession().update(user);
	}
	
	public void deleteUser(User user) {
		getSession().delete(user);
	}
	
	private Session getSession() {
		Session session = factory.getCurrentSession();
		if (session == null) {
			session = factory.openSession();
		}
		return session;
	}


}
