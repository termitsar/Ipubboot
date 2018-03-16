package com.example.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.dao.OrderDAO;

import com.example.model.GoodsBasket;
import com.example.model.Order;


@Service
public class OrderService {

@Autowired
OrderDAO orderdao;
@Autowired
private SendMail sendMail;


public List<Order> list_orders() {

		
	List<Order> list = orderdao.list_orders();
	for (Order o: list) 
	{
		Double sum = o.getGoodsBasket().stream().mapToDouble(a->a.getPrice()*a.getQuantity()).sum();
		int count =  o.getGoodsBasket().stream().mapToInt(a->a.getQuantity()).sum();
		o.setTotal_price(sum);
		o.setTotal_quantity(count);
	}	
		

	return list;
}


public Order getOrder(int id) {
	Order o = orderdao.getOrder(id);
	o.getGoodsBasket().forEach(d -> d.setGoods(orderdao.GetGood(d.getId_good())));
	
	Double sum = o.getGoodsBasket().stream().mapToDouble(a->a.getPrice()*a.getQuantity()).sum();
	int count =  o.getGoodsBasket().stream().mapToInt(a->a.getQuantity()).sum();
	o.setTotal_price(sum);
	o.setTotal_quantity(count);
	o.setListstate(orderdao.list_states());
	
	return o;

}

public void SaveOrder(Order order) {
	order.getDeliver_price();
	
	orderdao.SaveOrder(order);
    
	// ����� ������ ���������� ����� ��� ������ ����������
	sendMail.sendEmailCustomer(order.getId());
	
	}
	

public void DeleteOrder(int id) {
	Order order = orderdao.getOrder(id);
	List<GoodsBasket> gb = order.getGoodsBasket();
	for(GoodsBasket h : gb)
	{
		orderdao.deleteGoodsBasket(h);
	}	
	
	order.setGoodsBasket(null);
	orderdao.DeleteOrder(order);

	}

public void DeleteGoodsBasket(int id) {
	GoodsBasket g = orderdao.GetGoodsBasket(id);
	orderdao.deleteGoodsBasket(g);

	}


}
