package com.itvideo.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.itvideo.model.exceptions.user.UserException;

public class User {
	private static final String VALID_EMAIL_PATTERN = "(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])";
	private static final String STRONG_PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$";
	private static final int MIN_USERNAME_LENGTH = 3;

	private long userId;

	private String username;
	private String password;
	private String facebook;
	private String email;
	private LocalDateTime dateCreation;
	private String firstName;
	private String lastName;
	private String avatarUrl;
	private String gender;
	private String activationToken;
	private boolean activated;

	private List<User> followers = new ArrayList<>();

	private List<User> following = new ArrayList<>();

	User(long userId, String username, String password, String facebook, String email, LocalDateTime dateCreation,
			String firstName, String lastName, String avatarUrl, String gender, String activationToken ,boolean activated) throws UserException {
		this.userId = userId;
		this.username = username;
		this.password = password;
		this.facebook = facebook;
		this.email = email;
		this.dateCreation = dateCreation;
		this.firstName = firstName;
		this.lastName = lastName;
		this.avatarUrl = avatarUrl;
		this.gender = gender;
		this.activationToken = activationToken;
		this.activated = activated;
	}
	public User(String username, String password, String email) throws UserException {
		setUsername(username);
		setPassword(password);
		setEmail(email);
		this.dateCreation = LocalDateTime.now();
	}

	public void addFollower(User u) {
		this.followers.add(u);
	}
	public void addFollowing(User u) {
		this.following.add(u);
	}

	public String getActivationToken() {
		return activationToken;
	}

	public String getAvatarUrl() {
		return avatarUrl;
	}
	

	public LocalDateTime getDateCreation() {
		return dateCreation;
	}

	public String getEmail() {
		return email;
	}
	
	public String getFacebook() {
		return facebook;
	}

	public String getFirstName() {
		return firstName;
	}

	public List<User> getFollowers(){
		return this.followers;
	}

	public List<User> getFollowing(){
		return this.following;
	}

	public String getGender() {
		return gender;
	}

	public String getLastName() {
		return lastName;
	}

	public String getPassword() {
		return password;
	}

	public long getUserId() {
		return userId;
	}

	public String getUsername() {
		return username;
	}

	public boolean isActivated() {
		return activated;
	}

	private boolean passwordIsStrong(String password) {
		if (password.matches(STRONG_PASSWORD_PATTERN)) {
			return true;
		}
		return false;
	}

	public void removeFollower(User u) {
		this.followers.remove(u);
	}

	public void removeFollowing(User u) {
		this.following.remove(u);
	}

	public void setActivated(boolean activated) {
		this.activated = activated;
	}

	public void setActivationToken(String activationToken) {
		this.activationToken = activationToken;
	}

	public void setAvatarUrl(String avatarUrl) {
		this.avatarUrl = avatarUrl;
	}

	public void setEmail(String email) throws UserException {
		if (email.matches(VALID_EMAIL_PATTERN)) {
			this.email = email;
			return;
		}
		throw new UserException(UserException.INVALID_EMAIL);
	}

	public void setFacebook(String facebook) {
		this.facebook = facebook;
	}

	public void setFirstName(String firstName) throws UserException {
		this.firstName = firstName;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public void setLastName(String lastName) throws UserException {
		this.lastName = lastName;
	}

	public void setPassword(String password) throws UserException {
		if (passwordIsStrong(password)) {
			this.password = password;
		} else {
			throw new UserException(UserException.PASSWORD_NOT_STRONG);
		}
	}

	public void setPasswordNoValidation(String password) {
		this.password = password;
	}

	public void setUserId(long userId) throws UserException {
		if (userId < 1) {
			throw new UserException(UserException.INVALID_ID);
		}
		this.userId = userId;
	}

	public void setUsername(String username) throws UserException {
		if (username == null || username.isEmpty()) {
			throw new UserException(UserException.INVALID_USERNAME);
		}
		if (username.length() < MIN_USERNAME_LENGTH) {
			throw new UserException(UserException.INVALID_USERNAME_LENGTH);
		}
		this.username = username;
	}
}