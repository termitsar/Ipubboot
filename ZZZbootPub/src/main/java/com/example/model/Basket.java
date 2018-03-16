package com.example.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonManagedReference;


@Scope(value="session", proxyMode =ScopedProxyMode.TARGET_CLASS)
@Component
public class Basket implements Serializable{

	@JsonManagedReference
	List<GoodsBasket> goodsBasket = new ArrayList<GoodsBasket>();
	
	int total_quantity = goodsBasket.size();
	double total_price;
	
	
	public Basket() {	
		goodsBasket = new ArrayList<GoodsBasket>();
		
	}
	
	public void SetTotalValues() {	
		total_quantity = goodsBasket.size();
		total_price = goodsBasket.stream().mapToDouble(o -> o.getGoods().getPrice()*o.getQuantity()).sum();
	}

	public List<GoodsBasket> getgoodsBasket() {
		return goodsBasket;
	}

	public void setGoodsBasket(List<GoodsBasket> GoodsBasket) {
		this.goodsBasket = GoodsBasket;
	}

	public int getTotal_quantity() {
		return total_quantity;
	}

	public void setTotal_quantity(int total_quantity) {
		this.total_quantity = total_quantity;
	}

	public double getTotal_price() {
		return total_price;
	}

	public void setTotal_price(double total_price) {
		this.total_price = total_price;
	}
	
	
	
}
