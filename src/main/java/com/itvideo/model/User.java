package com.itvideo.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

import com.itvideo.model.exceptions.user.UserException;
import com.itvideo.model.utils.Hash;




public class User {

	private static final String DEFAULT_AVATAR_JPG = "defaultAvatar.png";
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

	private List<User> followers = new ArrayList<>();
	private List<User> following = new ArrayList<>();

	User(long userId, String username, String password, String facebook, String email, LocalDateTime dateCreation,
			String firstName, String lastName, String avatarUrl, String gender) throws UserException {
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
	}

	public User(String username, String password, String email) throws UserException {
		setUsername(username);
		setPassword(password);
		setEmail(email);
		this.avatarUrl = DEFAULT_AVATAR_JPG;
		this.dateCreation = LocalDateTime.now();
	}
	

	public void addFollower(User u) {
		this.followers.add(u);
	}

	public void addFollowing(User u) {
		this.following.add(u);
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

	private boolean passwordIsStrong(String password) {
		// https://stackoverflow.com/questions/3802192/regexp-java-for-password-validation
		String pattern = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$";
		if (password.matches(pattern)) {
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

	public void setAvatarUrl(String avatarUrl) {
		this.avatarUrl = avatarUrl;
	}

	public void setEmail(String email) throws UserException {
		try {
			InternetAddress emailAddr = new InternetAddress(email);
			emailAddr.validate();
		} catch (AddressException ex) {
			throw new UserException(UserException.INVALID_EMAIL);
		}
		this.email = email;
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

	private void setPassword(String password) throws UserException {
		if (passwordIsStrong(password)) {
			this.password = Hash.getHashPass(password);
		} else {
			throw new UserException(UserException.PASSWORD_NOT_STRONG);
		}
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