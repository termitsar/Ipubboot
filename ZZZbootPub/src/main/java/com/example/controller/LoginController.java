package com.example.controller;


import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import com.example.model.User;


@Controller
//@RequestMapping("/login")
public class LoginController {
	
    @Autowired
    private User CurrentUser;
    static final Logger userLogger = LogManager.getLogger("myinfo");
    
//    @RequestMapping(method = RequestMethod.GET)
//    public ModelAndView loginPage( Model model, @RequestParam(value = "error", required = false) String error){
//    	
//    	ModelAndView model1 = new ModelAndView();
//    	if (error != null) {
//			model1.addObject("error", "����� ��� ������ �� �����!");
//		}
//    	
//    	model1.setViewName("login");
//        return model1;
//    }

    @RequestMapping("/login")
    public String login() {
      return "login";
    }

    // Login form with error
    @RequestMapping("/login-error")
    public String loginError(Model model) {
      model.addAttribute("loginError", true);
      return "login";
    }
    
    @RequestMapping("/logouturl")
    public String logout() {
    	userLogger.info("Выход: Пользователь "+CurrentUser.getUsername()+ " вышел");
    	
        CurrentUser.setId(0);;
        
      return "redirect:/";
    }
    
}
