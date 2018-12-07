package com.diloso.weblogin.aut;


public interface UserRegistry {
	
	AppUser findUser(String email, Long firmId);

	void registerUser(AppUser newUser);

	void removeUser(String email, Long firmId);
}
