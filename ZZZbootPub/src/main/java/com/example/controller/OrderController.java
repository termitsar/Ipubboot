package com.example.controller;


import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.example.model.Order;
import com.example.model.User;
import com.example.service.ForOtherSrv;
import com.example.service.OrderService;


@Controller
@RequestMapping("order")
public class OrderController {
	
	@Autowired
	ForOtherSrv srv;
    @Autowired
    private User CurrentUser;
    @Autowired
    OrderService ordersrv;
    
 // ������� ������ �������
 	@PreAuthorize("hasAuthority('ADMIN')")
 	@RequestMapping("/list")
 	public String listorders(Model model) throws Exception {
      	model.addAttribute("currentuser", CurrentUser);
      	
 		List<Order> listOrders = ordersrv.list_orders();
 		model.addAttribute("listorders", listOrders);
 		return "Admin/OrderList"; 
 	}
 	
 	
 	 // ������� ���� ����� ������� ������� � ������
 	@PreAuthorize("hasAuthority('ADMIN')")
 	@RequestMapping(value = "/getOrder/{id}", method = RequestMethod.GET)
 	public String getorder(@PathVariable("id") int id, Model model) throws Exception {
      	model.addAttribute("currentuser", CurrentUser);
      	
 		Order order = ordersrv.getOrder(id);
 		model.addAttribute("order", order);
 		return "Admin/OrderDetails"; 
 	}
   
 // ���������� ��������� � ������
  	@PreAuthorize("hasAuthority('ADMIN')")
  	@RequestMapping(value = "/SaveOrder", method = RequestMethod.POST)
  	public String saveorder( @ModelAttribute Order order, Model model) throws Exception {
       	model.addAttribute("currentuser", CurrentUser);
      
       	
       	ordersrv.SaveOrder(order);

  		return "redirect:/order/list"; 
  	}
  	
 // ������� ����� �������
   	@PreAuthorize("hasAuthority('ADMIN')")
   	@RequestMapping(value = "/deleteOrder/{id}", method = RequestMethod.GET)
   	public String deleteorder(@PathVariable("id") int id) throws Exception {
        	
      ordersrv.DeleteOrder(id);

   		return "redirect:/order/list"; 
   	}
 	
 // ������� ����� �� ������
   	@PreAuthorize("hasAuthority('ADMIN')")
   	@RequestMapping(value = "/deletegood/{id}", method = RequestMethod.GET)
   	public String deletegood(@PathVariable("id") int id,  HttpServletRequest request) throws Exception {
   		String referer = request.getHeader("Referer"); 	
      
   		ordersrv.DeleteGoodsBasket(id);

     return "redirect:"+referer; 
   	}
   	
}
