package com.example.controller;

import java.util.List;
import java.util.logging.Logger;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import com.example.dao.UserDAO;
import com.example.model.Goods;
import com.example.model.GoodsBasket;
import com.example.model.Groupsgoods;
import com.example.model.GroupsgoodsEdit;
import com.example.model.Order;
import com.example.model.User;
import com.example.model.UserValidator;
import com.example.service.OrderService;
import com.example.service.SendMail;
import com.example.service.UserService;

import org.hibernate.annotations.common.util.impl.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

/**
 * Handles requests for the application home page.
 */
@Controller
public class HomeController {
		
    @Autowired
    private UserService userService;
    
    @Autowired
    private OrderService orderService;
	
	@Autowired
	private UserDAO userDao;
	
	@Autowired
	private UserValidator uservalidator;
	
    @Autowired
    private User CurrentUser;
    
    @Autowired
    private SendMail sendMail;
	
	
	@InitBinder("user")
	protected void initBinderFileGoods(WebDataBinder binder) {
	   binder.setValidator(uservalidator);
	}
	
	// ������� ������ �������������
	@PreAuthorize("hasAuthority('ADMIN')")
	@RequestMapping("/listusers")
	public String listusers(Model model) throws Exception {

     	model.addAttribute("currentuser", CurrentUser);
		List<User> listUsers = userDao.list_user();
		model.addAttribute("userlist", listUsers);
		return "Admin/UserList"; 
	}
	
	
	
	// �������� ������������ �� id  � �� ��������������
	@PreAuthorize("hasAuthority('ADMIN') or #id==0") 
	@RequestMapping(value = "/userform/{id}", method = RequestMethod.GET)
	public String EditUser( @PathVariable("id") int id, Model model,  HttpServletRequest request) {
		String referer = request.getHeader("Referer");
		CurrentUser.setPrevious_url(referer);
		
		model.addAttribute("currentuser", CurrentUser);
		
		// ���� id=0 ����� ����������� ������������ ������������
		if (CurrentUser.getId()==0) return "redirect:/";
		if (id==0) id = CurrentUser.getId();
		
    	User u = userService.getUser(id);
    	model.addAttribute("user", u);
   	
    	return "Admin/UserForm";
      } 
	
	// ��������� ������������
		@RequestMapping(value = "/usersave", method = RequestMethod.POST)
	    public String SaveUser(@Valid  User user, BindingResult result, Model model) {

			if (result.hasErrors()) {
				System.out.println("validation errors");
				return "Admin/UserForm";
			}
	   	
			userService.setUser(user);
			
			String previous_url = CurrentUser.getPrevious_url();
			return "redirect:"+previous_url;
	    	//return "redirect:/listusers";
	      } 
	
		// ����� ������������
		@RequestMapping(value = "/usernew", method = RequestMethod.GET)
		public String newUser( Model model, HttpServletRequest request) {
			String referer = request.getHeader("Referer");
			CurrentUser.setPrevious_url(referer);
			
			model.addAttribute("currentuser", CurrentUser);
			User u = userService.getNewUser();
	    	model.addAttribute("user", u);
			
			return "Admin/UserForm";		
		}	
		
		// ������� ������������
		@PreAuthorize("hasAuthority('ADMIN')")
		@RequestMapping(value = "/userdelete/{id}", method = RequestMethod.GET)
		public String deleteUser(@PathVariable("id") int id, Model model) {
	
			userDao.delete(id);
	
			return "redirect:/listusers";
		}
	

		// �������� ���� �� ������������
		@RequestMapping(value = "/sendemail", method = RequestMethod.GET)
		public String sendemail(Model model) {
			
			sendMail.sendEmailCustomer(20);
			
			model.addAttribute("currentuser", CurrentUser);
			Order order = orderService.getOrder(20);	
			model.addAttribute("order", order);
			List<GoodsBasket> goods = order.getGoodsBasket();
			model.addAttribute("goodsBasket", goods);
		return "MailTemplate";
		}		
		

	
}
