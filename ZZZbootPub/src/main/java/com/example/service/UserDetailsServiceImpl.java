package com.example.service;


import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.model.Roles;
import com.example.model.User;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserService userService;
    
    @Autowired
    private User CurrentUser;
    
    static final Logger rootLogger = LogManager.getRootLogger();
    static final Logger userLogger = LogManager.getLogger("myinfo");

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        User user = userService.getUserByLogin(login);
        Set<GrantedAuthority> roles = new HashSet();
        user.getRole_list().forEach(value -> roles.add(new SimpleGrantedAuthority(value.getRolename())));
//        for(Roles role : user.getRole_list()) {     
//            roles.add(new SimpleGrantedAuthority(role.getRolename()));
//        }
        //roles.add(new SimpleGrantedAuthority(UserRoleEnum.USER.name()));
        //roles.add(new SimpleGrantedAuthority(UserRoleEnum.ADMIN.name()));

        UserDetails userDetails = new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), roles);
        
        User CurUser = userService.getUserByLogin(login);
        CurrentUser.setId(CurUser.getId());
        CurrentUser.setFam(CurUser.getFam());
        CurrentUser.setIm(CurUser.getIm());
        CurrentUser.setOt(CurUser.getOt());
        CurrentUser.setUsername(CurUser.getUsername());
        CurrentUser.setEmail(CurUser.getEmail());
         
      //  rootLogger.debug("������������ "+CurUser.getUsername()+ " �����������");
        userLogger.info("Вход: Пользователь "+CurUser.getUsername()+ " залогинился");

        return userDetails;
    }

}
