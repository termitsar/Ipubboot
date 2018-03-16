package com.example.service;

import com.example.model.User;

public interface UserService {

    User getUser(int id);

	void setUser(User user);

	User getUserByLogin(String login);

	User getNewUser();

	Boolean isOwner(int id);

}
