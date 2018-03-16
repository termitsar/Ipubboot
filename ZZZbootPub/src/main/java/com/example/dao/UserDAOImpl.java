package com.example.dao;

import java.util.List;

import com.example.model.Groupsgoods;
import com.example.model.Roles;
import com.example.model.User;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class UserDAOImpl implements UserDAO {
	
	@Autowired
	private SessionFactory sessionFactory;

	public UserDAOImpl() {
		
	}
	
	public UserDAOImpl(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	@Override
	@Transactional
	public List<User> list_user() {

		List<User> list = (List<User>) sessionFactory.getCurrentSession().createCriteria(User.class).list();

		return list;
	}

	@Override
	@Transactional
	public List<Roles> list_roles() {
		@SuppressWarnings("unchecked")
		
		Session ff = sessionFactory.getCurrentSession();
		
		List<Roles> list = (List<Roles>) sessionFactory.getCurrentSession()
				.createCriteria(Roles.class)
				.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY).list();

		return list;
	}
	
	
	@Override
	@Transactional
	public void saveOrUpdate(User user) {
		sessionFactory.getCurrentSession().saveOrUpdate(user);
	}

	@Override
	@Transactional
	public void delete(int id) {
		User userToDelete = new User();
		userToDelete.setId(id);
		sessionFactory.getCurrentSession().delete(userToDelete);
	}

	
	@Override
	@Transactional
	public User getUser(int id) {
		return (User) sessionFactory.getCurrentSession().get(User.class, id);

	}
	
	@Override
	@Transactional
	public User getUserByLogin(String login) {
		String hql = "from User where username=:username";
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
			  query.setParameter("username", login);		

		@SuppressWarnings("unchecked")
		List<User> listUser = (List<User>) query.list();
		
		if (listUser != null && !listUser.isEmpty()) {
			return listUser.get(0);
		}
		
		return null;
	}
}