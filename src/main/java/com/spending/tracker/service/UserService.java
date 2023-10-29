package com.spending.tracker.service;

import com.spending.tracker.model.User;

public interface UserService {
	User addUser(User user);
	User getUserById(String id);
	void deleteUserById(String id);

}
