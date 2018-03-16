package com.example.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.dao.ForOtherDAO;

import com.example.model.Basket;
import com.example.model.Goods;
import com.example.model.GoodsBasket;
import com.example.model.Groupsgoods;
import com.example.model.Order;
import com.example.model.Picturesgoods;
import com.example.model.Price;
import com.example.model.User;

@Service("orderSrvImpl")
public class OrderSrvImpl implements OrderSrv {

	@Autowired
	Basket basket;
	@Autowired
	ForOtherDAO dao;
	@Autowired
	private User CurrentUser;
	@Autowired
	private SendMail sendMail;
	
	// ��� ��� ���� ������������! ������ �� ���� ������� �� �������������� � ����� ���� ���
	int id_type = 1;
	int id_user = 1;
	
public User getCurrentUser()
{
	return CurrentUser;
}	
	/////////////////////////���������� ������/////////////////////////

public Order CreateOrder() {
	
	Order order = new Order();
	List<GoodsBasket> bb = basket.getgoodsBasket();
	bb.forEach(s->s.setOrder(order));	
	order.setGoodsBasket(bb);
	
	order.setTotal_quantity(basket.getTotal_quantity());
	order.setTotal_price(basket.getTotal_price());
	order.setId_user(id_user);
	
	return order;

}

public void SaveOrder(Order g) {
	g.setState_order(dao.GetStateOrder(1));
	int id = dao.SaveOrder(g);
	
	if (id!=-1) sendMail.sendEmailCustomer(id);
	
	basket.getgoodsBasket().clear();
	}

	
}
