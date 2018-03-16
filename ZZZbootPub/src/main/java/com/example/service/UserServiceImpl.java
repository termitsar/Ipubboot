package com.example.service;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.encoding.PasswordEncoder;
import org.springframework.security.authentication.encoding.ShaPasswordEncoder;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.dao.UserDAO;

import com.example.model.Roles;
import com.example.model.User;

@Service(value="userServiceImpl")
public class UserServiceImpl implements UserService {

	
	@Autowired
	private UserDAO userDao;

	@Autowired
	private ShaPasswordEncoder passwordEncoder;
	
    static final Logger rootLogger = LogManager.getRootLogger();
    static final Logger userLogger = LogManager.getLogger("myinfo");
	
	@Override
	public Boolean isOwner(int id) {
		return true;
	}

	@Override
	public User getUserByLogin(String login) {

		User user = userDao.getUserByLogin(login);
		if (user==null) return new User();
			
		List<Roles> roles = user.getRole_list();
		user.setRole_list_full(userDao.list_roles());
		for (Roles r : user.getRole_list_full()) {
			if (roles.contains(r))
			{
				r.setYes(true);
			}
		}

		
		return user;
	}

	@Override
	public User getUser(int id) {
	User user = userDao.getUser(id);
	
	List<Roles> roles = user.getRole_list();
	user.setRole_list_full(userDao.list_roles());
	for (Roles r : user.getRole_list_full()) {
		if (roles.contains(r))
		{
			r.setYes(true);
		}
	}
	user.getRole_list_full().sort(Comparator.comparing(Roles::getId));
	
	
	return user;
	}
	

	public User getNewUser() {
	User user = new User();

	user.setRole_list_full(userDao.list_roles());
	user.getRole_list_full().sort(Comparator.comparing(Roles::getId));
	user.setCr_date(java.util.Calendar.getInstance().getTime());
	
	return user;
	}
	
	
	@Override
	public void setUser(User user) {
		String type_user = "new";
		if (user.getId()>0) 
		{
			type_user="old";
			user.setPassword(userDao.getUser(user.getId()).getPassword());
		}
		
		String encodedPassword="";
		if (!user.getNewpassword().equalsIgnoreCase(""))
        {
			encodedPassword = passwordEncoder.encodePassword(user.getNewpassword(), null);
			user.setPassword(encodedPassword);
        }	
		else	
		{
			if (type_user.equals("new"))
			{
			    encodedPassword = passwordEncoder.encodePassword("", null);	
			    user.setPassword(encodedPassword);
			}
		}
			
		
        if (user.getRole_list_full()!=null) 
        {	
	        user.getRole_list_full().removeIf(d->d.isYes()==false);
	        user.setRole_list(user.getRole_list_full());
        }
        
        System.out.print(user.getId());
		userDao.saveOrUpdate(user);
		// User user = userDao.get(login);

	}

}
