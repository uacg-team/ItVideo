package com.itvideo.model.exceptions.user;

public class UserNotFoundException extends Exception {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final String USER_NOT_FOUND = "User not found";
	
	public UserNotFoundException(String message) {
		super(message);
	}
}
