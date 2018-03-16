package com.example.dao;

import java.util.List;

import com.example.model.Goods;
import com.example.model.GoodsBasket;
import com.example.model.Groupsgoods;
import com.example.model.Order;
import com.example.model.Price;
import com.example.model.Roles;
import com.example.model.StateOrder;
import com.example.model.User;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;



@Repository
public class OrderDAO {
	
	@Autowired
	private SessionFactory sessionFactory;

	public OrderDAO() {

	}

	public OrderDAO(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}


	@Transactional
	public List<StateOrder> list_states() {

		List<StateOrder> list = (List<StateOrder>) sessionFactory.getCurrentSession().createCriteria(StateOrder.class).list();

		return list;
	}
	
	
	@Transactional
	public Order getOrder(int id) {
		return (Order) sessionFactory.getCurrentSession().get(Order.class, id);

	}
	
	
	@Transactional
	public Goods GetGood(int id) {

		return (Goods) sessionFactory.getCurrentSession().get(Goods.class, id);
	}
	
	@Transactional
	public List<Order> list_orders() {

		List<Order> list = (List<Order>) sessionFactory.getCurrentSession().createCriteria(Order.class).list();

		return list;
	}
	
	
	@Transactional
	public void SaveOrder(Order g) {
		if (null!=g)	sessionFactory.getCurrentSession().saveOrUpdate(g);
		}
	
	@Transactional
	public void DeleteOrder(Order g) {

		if (null != g) 	sessionFactory.getCurrentSession().delete(g);
		}
	
	@Transactional
	public void deleteGoodsBasket(GoodsBasket g) {

		if (null != g) 	sessionFactory.getCurrentSession().delete(g);
		}
	
	@Transactional
	public GoodsBasket GetGoodsBasket(int id) {

		return (GoodsBasket) sessionFactory.getCurrentSession().get(GoodsBasket.class, id);
	}
	
}