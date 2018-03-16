package com.example.dao;

import java.util.List;

import com.example.model.Roles;
import com.example.model.User;

public interface UserDAO {
	public List<User> list_user();
	
	public void saveOrUpdate(User user);
	
	public void delete(int id);

	public List<Roles> list_roles();

	public User getUser(int id);

	public User getUserByLogin(String login);
}