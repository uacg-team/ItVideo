package com.itvideo.model.exceptions.user;

import sun.security.util.Password;

public class UserException extends Exception {

	public static final String INVALID_USERNAME_LENGTH = "Invalid username length";
	public static final String INVALID_USERNAME = "Invalid username";
	public static final String INVALID_PASSWORD_LENGTH = "Invalid password length";
	public static final String INVALID_PASSWORD = "Invalid password";
	public static final String PASSWORD_NOT_STRONG = "Password must contain least once digit, lower letter,upper letter, special character, must be more than 8 symbols and no white spaces";
	public static final String INVALID_NAME = "Invalid name";
	public static final String INVALID_NAME_LENGTH = "Invalid name length";
	public static final String INVALID_FACEBOOK = "Invalid facebook";
	public static final String INVALID_EMAIL = "Invalid email";
	public static final String INVALID_ID = "Invalid user id";
	public static final String MORE_THAN_ONE_USER_AFFECTED = "Mode than one user affected";
	public static final String USERNAME_EXIST = "This username already exist";
	public static final String EMAIL_EXIST = "This email already exist";

	public UserException(String message) {
		super(message);
	}
}
