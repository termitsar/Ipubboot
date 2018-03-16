package com.example.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

import com.example.config.CustomGenericException;
import com.example.model.Basket;
import com.example.model.Goods;
import com.example.model.GoodsBasket;
import com.example.model.Groupsgoods;
import com.example.model.Price;
import com.example.model.User;
import com.example.service.ForOtherSrv;

@Controller
public class MainController {

	@Autowired
	ForOtherSrv srv;
	
	@Autowired
	private User CurrentUser;
	
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String start(Model model){
    	
    	model.addAttribute("currentuser", CurrentUser);
    	List<Groupsgoods> groups = srv.GetGroups();
    	model.addAttribute("groups", groups);
    	List<Goods> goods = srv.GetGoods();
    	model.addAttribute("goods", goods);
    	
        return "Goods";
    }
    

    //-------------------Retrieve Goods for Group--------------------------------------------------------
    @RequestMapping(value = "/goods/{id}", method = RequestMethod.GET)
    public String listAllGoodsbyGroup(@PathVariable("id") int id, Model model) {

    	model.addAttribute("currentuser", CurrentUser);
    	List<Groupsgoods> groups = srv.GetGroups();
    	model.addAttribute("groups", groups);
    	List<Goods> goods = srv.GetGoodsbyGroup(id);
    	model.addAttribute("goods", goods);

    	return "GoodsInGroup";
      } 
    
  //-------------------Retrieve Product by ID--------------------------------------------------------
    @RequestMapping(value = "/product/{id}", method = RequestMethod.GET)
    public String GetProduct(@PathVariable("id") int id, Model model)  throws Exception{
    	
    // ЭТО ДЛЯ ПРОВЕРКИ ИСКЛЮЧЕНИЙ
    //	if (0==0) //throw new CustomGenericException("E888", "This is custom message");
	//		throw new Exception();

    	model.addAttribute("currentuser", CurrentUser);
    	List<Groupsgoods> groups = srv.GetGroups();
    	model.addAttribute("groups", groups);
    	Goods good = srv.GetProduct(id);
    	model.addAttribute("product", good);
    	
    	return "Product";
      } 
    
    
    // �������� ����� � �������
    @RequestMapping(value = "/put_in_basket",method = RequestMethod.PUT)
	public @ResponseBody int putMyData(@RequestBody GoodsBasket md) {
    	srv.AddinBasket(md);

		return 1;
	}
    

    // �������� ��� ������ �������
    @RequestMapping(value = "/get_basket",method = RequestMethod.GET)
   	public @ResponseBody Basket putMyData() {
       	Basket b = srv.GetBasket();
       	Basket g = new Basket();
      	g.setGoodsBasket(b.getgoodsBasket());
        g.setTotal_quantity(b.getTotal_quantity());
        g.setTotal_price(b.getTotal_price());
        
   		return g;
   	}
    
    // �������� ������ �� ������� �� id
    @RequestMapping(value= "/removefrombasket/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    public int deleteMyData(@PathVariable int id) {
    	srv.DeleteFromBasket(id);
    	
        return 1;
    }
    
    
    // ��������! ���������� ������
    @RequestMapping(value = "i_buy_it",method = RequestMethod.GET)
	public  String i_buy() {
    	srv.SaveBasketFirst();
    	
		return "Contacts";
  	}
    
    
    @RequestMapping(value = "/endorder", method = RequestMethod.GET)
    public String endorder(Model model){
    	model.addAttribute("currentuser", CurrentUser);
   	
        return "EndOrder";
    }
    
    // ---------ЭТО ДЛЯ ПРОВЕРКИ ИСКЛЮЧЕНИЙ
//	@ExceptionHandler(Exception.class)
//	public String handleAllException(Exception ex) {
//
//		System.out.println("fffsdfsf");
//
//		return "model";
//
//	}
	
//	@ExceptionHandler(EmployeeNotFoundException.class)
//	public String handleEmployeeNotFoundException(EmployeeNotFoundException ex) {
//
//		System.out.println("fffsdfsf");
//
//		return "model";
//
//	}
//    
//    
//    public class EmployeeNotFoundException extends Exception {
//
//    	private static final long serialVersionUID = -3332292346834265371L;
//
//    	public EmployeeNotFoundException(int id){
//    		super("EmployeeNotFoundException with id="+id);
//    	}
//    }
    
}
