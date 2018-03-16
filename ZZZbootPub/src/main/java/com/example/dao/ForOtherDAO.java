package com.example.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;

import com.example.model.Goods;
import com.example.model.GoodsBasket;
import com.example.model.Groupsgoods;
import com.example.model.Order;
import com.example.model.Price;
import com.example.model.StateOrder;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;



@Repository
public class ForOtherDAO {
	@SuppressWarnings("unchecked")

	@Autowired
	private SessionFactory sessionFactory;
	

	public ForOtherDAO() {

	}

	public ForOtherDAO(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	@Transactional
	public List<Groupsgoods> GetGroups() {

		Session ff = sessionFactory.getCurrentSession();

		List<Groupsgoods> list = (List<Groupsgoods>) sessionFactory.getCurrentSession()
				.createQuery("from Groupsgoods where parent=null").list();

		return list;
	}

	@Transactional
	public void addGroup(Groupsgoods group) {
		sessionFactory.getCurrentSession().merge(group);
	}
	
	// ��� ������ ����� ��� ������� �� �������� �����
	@Transactional
	public List<Groupsgoods> GetGroupsAll() {

		Session ff = sessionFactory.getCurrentSession();

		List<Groupsgoods> list = (List<Groupsgoods>) sessionFactory.getCurrentSession()
				.createQuery("from Groupsgoods").list();

		return list;
	}
	
	@Transactional
	public Groupsgoods GetGroup(int id) {

		return (Groupsgoods) sessionFactory.getCurrentSession().get(Groupsgoods.class, id);
	}
	
	@Transactional
	public void deleteGroup(Groupsgoods g) {
		Session ff = sessionFactory.getCurrentSession();
		ff.flush();
		if (null != g) 	ff.delete(g);
		
		
		}
	
	@Transactional
	public List<Goods> GetGoods() {

		Session ff = sessionFactory.getCurrentSession();
		List<Goods> list = (List<Goods>) sessionFactory.getCurrentSession().createQuery("from Goods ").list();

		return list;
	}
	
	@CacheEvict(value="goods", key="#good.id") 
	@Transactional
	public void addGood(Goods good) {
		sessionFactory.getCurrentSession().saveOrUpdate(good);
	}
	
	@CacheEvict(value="goods", key="#g.id") 
	@Transactional
	public void deleteGood(Goods g) {

		if (null != g) 	sessionFactory.getCurrentSession().delete(g);
		}
	
	@Transactional
	public void addPrice(Price price) {
		sessionFactory.getCurrentSession().saveOrUpdate(price);
	}
	
	@Transactional
	public void deletePrice(Price price) {

		if (null != price) 	sessionFactory.getCurrentSession().delete(price);
		}

	@Transactional
	public List<Goods> GetGoodsbyGroup(int id) {

		Session ff = sessionFactory.getCurrentSession();

		List<Goods> list = (List<Goods>) sessionFactory.getCurrentSession()
				.createQuery("from Goods where group_id=:id ").setInteger("id", id).list();

		return list;
	}

	@Cacheable(value="goods", key="#id") 
	@Transactional
	public Goods GetProduct(int id) {

		return (Goods) sessionFactory.getCurrentSession().get(Goods.class, id);
	}

	@Transactional
	public List<Price> GetPrice(int id_type, int id_good) {
		List<Price> list;
		
		if (id_type>0) 
		{	
			list = (List<Price>) sessionFactory.getCurrentSession()
					.createQuery("from Price where id_type=:id_t and id_good=:id_g ")
					.setInteger("id_t", id_type)
					.setInteger("id_g", id_good).list();
		}
		else
		{
			list = (List<Price>) sessionFactory.getCurrentSession()
					.createQuery("from Price where id_good=:id_g ")
					.setInteger("id_g", id_good).list();
		}	
		
		return list;
	}


	
	
	@Transactional
	public List<GoodsBasket> GetBasketbyType(int id_user, String type) {
		Session ff = sessionFactory.getCurrentSession();
		
		List<GoodsBasket> list = (List<GoodsBasket>) sessionFactory.getCurrentSession()
				.createQuery("from GoodsBasket where id_user=:id_user and type=:type ")
				.setInteger("id_user", id_user)
				.setString("type", type).list();

		return list;
	}
	
	
	@Transactional
	public void addBasket(GoodsBasket g) {
		g.setId(0);
		if (null!=g)	sessionFactory.getCurrentSession().saveOrUpdate(g);
		}

	@Transactional
	public void deleteBasketbyType(GoodsBasket g) {

		if (null != g) 	sessionFactory.getCurrentSession().delete(g);
		}
	
	
	@Transactional
	public int SaveOrder(Order g) {
		int id = -1;
		g.setId(0);
		if (null!=g)	
		{	
			 id = (Integer) sessionFactory.getCurrentSession().save(g);
		}
		return id;
	}
	
	
	@Transactional
	public StateOrder GetStateOrder(int id) {

		return (StateOrder) sessionFactory.getCurrentSession().get(StateOrder.class, id);
	}
	
}