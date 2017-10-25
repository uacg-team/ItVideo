package com.itvideo.model.exceptions.user;

public class UserNotFoundException extends Exception {
	
	public static final String USER_NOT_FOUND = "User not found";
	
	public UserNotFoundException(String message) {
		super(message);
	}
}
