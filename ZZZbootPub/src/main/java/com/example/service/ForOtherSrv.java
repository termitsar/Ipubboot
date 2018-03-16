package com.example.service;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.example.dao.ForOtherDAO;
import com.example.model.Basket;
import com.example.model.Goods;
import com.example.model.GoodsBasket;
import com.example.model.Groupsgoods;
import com.example.model.Order;
import com.example.model.Picturesgoods;
import com.example.model.Price;

@Service
public class ForOtherSrv {
	
	@Value("${path.pictures.short}")
	private  String IMAGE_LOCATION;
	
	@Autowired
	ForOtherDAO dao;
	Picturesgoods no_photo;
	
	@Autowired
	Basket basket;
	
	// ��� ��� ���� ������������! ������ �� ���� ������� �� �������������� � ����� ���� ���
	int id_type = 1;
	int id_user = 1;
	
	public ForOtherSrv() {
		super();
		no_photo = new Picturesgoods();
		no_photo.setUrl("/none.png");
	}

	public Groupsgoods GetGroup(int id) {
		return  dao.GetGroup(id);
	}
	
	public void SaveGroup(Groupsgoods group) {
		dao.addGroup(group);
	}
	
	
	public List<Groupsgoods> GetGroups() {
		List<Groupsgoods> l = dao.GetGroups();
		return l;
	}

	
	public List<Goods> GetGoods() {
		List<Goods> l = dao.GetGoods();
		l.forEach(h->SetPrimaryPicture(h));
		l.forEach(h->h.setPrice(GetPrice(h.getId())));
		
		return l;
	}
	
	public boolean ValidatieDeleteGroup(int id_group) {
		
		List<Goods> goods = dao.GetGoodsbyGroup(id_group);
		if (!goods.isEmpty()) return false;
		
		Groupsgoods group = dao.GetGroup(id_group);
		if (!group.getSubgroup().isEmpty()) return false;
		
		return true;
	}
	
	public void deleteGroup(int id_group) {
		Groupsgoods group = dao.GetGroup(id_group);
		dao.deleteGroup(group);
		}
	
	
	public void AddOrUpdateGood(Goods g) {
		
		dao.addGood(g);
	}
	
	public void DeleteGood(int id_good) {
		
		Goods good = dao.GetProduct(id_good);
		
		// ������� ���������� ���
		List<Price> list = dao.GetPrice(0, id_good);
		for (Price p : list)
		{
			dao.deletePrice(p);
		}
		
		// ������� ����� ���� � �����
		File dir = new File(IMAGE_LOCATION + good.getId());
		if (dir.exists()) {
            String[] children = dir.list();
            for (int i=0; i<children.length; i++) {
                File f = new File(dir, children[i]);
                f.delete();
            }
            dir.delete();
         } 
		
		// � ������� ��� ����� �� ������� ����� � ������
		dao.deleteGood(good);
		
	}

	
	public List<Goods> GetGoodsbyGroup(int id) {
		List<Goods> l = dao.GetGoodsbyGroup(id);
		l.forEach(h->SetPrimaryPicture(h));
		l.forEach(h->h.setPrice(GetPrice(h.getId())));
		
		return l;
	}
	
	public Goods GetProduct(int id) {
		Goods l = dao.GetProduct(id);
		l.setPrice(GetPrice(l.getId()));
		SetPrimaryPicture(l);
		
		return l;
	}
	
	public Goods SetPrimaryPicture(Goods g) {
		
		Picturesgoods prim_pic=null;

		
		for (Picturesgoods p:  g.getPictures())
		{
			if (p.isPrimary()) prim_pic = p;
		}
		
		if (prim_pic==null) prim_pic=no_photo;		

		g.setPrimPicture(prim_pic);
		g.setId_primPicture(prim_pic.getId());
		return g;
	}
	
	public void AddinBasket(GoodsBasket md) {
		if (md.getQuantity()==0) return;
		
		Goods g = GetProduct(md.getId_good());
	//	md.setId_user(id_user);
		md.setGoods(g);
		if (basket.getgoodsBasket().contains(md))
		{
			for (GoodsBasket n : basket.getgoodsBasket()) 
			{
				if (n.equals(md)) 
			   {
				   n.setQuantity(n.getQuantity()+md.getQuantity());
			   }
			}
		}	
		else
		{
	    	basket.getgoodsBasket().add(md);			
		}

		List<GoodsBasket> f = basket.getgoodsBasket();
		basket.SetTotalValues();
		
	
	}
	
	public void DeleteFromBasket(int id) {
			basket.getgoodsBasket().removeIf(l->l.getId_good()==id);
	}
	
	public Basket GetBasket() {
		basket.SetTotalValues();
		
		return basket;
	}
	
public double GetPrice(int id_good) {

	double price = 0;
	
	List<Price> l = dao.GetPrice(id_type, id_good);
	if (l.size()>0) price = l.get(0).getPrice();
		
		return price;
	}	
	
public void SetPrice(int id_good,  double price) {
	
	List<Price> l = dao.GetPrice(id_type, id_good);
	if (l.size()>0) 
	{
		l.get(0).setPrice(price);
		dao.addPrice(l.get(0));
	}	
	else
	{
		Price p = new Price();
		p.setId_good(id_good);
		p.setId_type(id_type);
		p.setPrice(price);
		dao.addPrice(p);
	}	
	
	}

public void SaveBasketFirst() {
	// �������� ��� ������ �������
	List<GoodsBasket> l = dao.GetBasketbyType(id_user,"begin");
	//������� 
	//dao.deleteBasketbyType(l.get(0));
	l.forEach(d->dao.deleteBasketbyType(d));
	// ������ � ����� ������ �� ��� ��� � ������ ����������
	basket.getgoodsBasket().forEach(b->b.setType("begin"));
	//���������� �� � ����
	basket.getgoodsBasket().forEach(b->dao.addBasket(b));
	

}




	
}
