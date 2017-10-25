package com.itvideo.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.itvideo.model.exceptions.user.UserException;
import com.itvideo.model.exceptions.user.UserNotFoundException;
import com.itvideo.model.utils.DateTimeConvertor;


/**
 * User Data Access Object
 * 
 * @author HP
 *
 */
public class UserDao {

	private static UserDao instance;
	private static final Connection con = DBConnection.CON1.getConnection();
	
	static {
		instance = new UserDao();
	}

	private UserDao() {
	}
	
	public static UserDao getInstance() {
		return instance;
	}

	public void createUser(User u) throws SQLException, UserException {
		String sql = "INSERT INTO users (username, password, email, date_creation, avatar_url) VALUES (?, ?, ?, ?, ?)";
		try (PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);) {
			ps.setString(1, u.getUsername());
			ps.setString(2, u.getPassword());
			ps.setString(3, u.getEmail());
			ps.setString(4, DateTimeConvertor.ldtToSql(u.getDateCreation()));
			ps.setString(5, u.getAvatarUrl());
			ps.executeUpdate();
			try (ResultSet rs = ps.getGeneratedKeys();) {
				rs.next();
				u.setUserId(rs.getLong(1));
			}
		}
	}

	public List<User> searchUser(String username) throws SQLException, UserException {
		String sql = "SELECT * FROM users WHERE username LIKE ?";
		try (PreparedStatement ps = con.prepareStatement(sql);) {
			ps.setString(1, "%" + username + "%");
			try (ResultSet rs = ps.executeQuery();) {
				List<User> users = new ArrayList<>();
				while (rs.next()) {
					users.add(
							new User(
									rs.getLong("user_id"), 
									rs.getString("username"), 
									rs.getString("password"),
									rs.getString("facebook"), 
									rs.getString("email"),
									DateTimeConvertor.sqlToLdt(rs.getString("date_creation")), 
									rs.getString("first_name"),
									rs.getString("last_name"),
									rs.getString("avatar_url"),
									rs.getString("gender")));
				}
				return users;
			}
		}
	}

	public void updateUser(User u) throws SQLException, UserException, UserNotFoundException {
		String sql = "UPDATE users SET facebook = ?, password = ?, email = ?, first_name = ?, last_name = ?, avatar_url = ?, gender = ? WHERE user_id = ? ;";
		try (PreparedStatement ps = con.prepareStatement(sql);) {
			ps.setString(1, u.getFacebook());
			ps.setString(2, u.getPassword());
			ps.setString(3, u.getEmail());
			ps.setString(4, u.getFirstName());
			ps.setString(5, u.getLastName());
			ps.setString(6, u.getAvatarUrl());
			ps.setString(7, u.getGender());
			ps.setLong(8, u.getUserId());
			int affectedRows = ps.executeUpdate();
			if (affectedRows == 0) {
				throw new UserNotFoundException(UserNotFoundException.USER_NOT_FOUND);
			}
		}
	}

	public boolean existsUser(String username) throws SQLException {
		String sql = "SELECT * FROM users WHERE username = ?;";
		try (PreparedStatement ps = con.prepareStatement(sql);) {
			ps.setString(1, username);
			try (ResultSet rs = ps.executeQuery();) {
				if (rs.next()) {
					return true;
				}
				return false;
			}
		}
	}
	
	public User getUser(String username) throws SQLException, UserNotFoundException, UserException {
		String sql = "SELECT * FROM users WHERE username = ?;";
		try (PreparedStatement ps = con.prepareStatement(sql);) {
			ps.setString(1, username);
			try (ResultSet rs = ps.executeQuery();) {
				if (rs.next()) {
					return new User(
							rs.getLong("user_id"), 
							rs.getString("username"), 
							rs.getString("password"),
							rs.getString("facebook"), 
							rs.getString("email"),
							DateTimeConvertor.sqlToLdt(rs.getString("date_creation")), 
							rs.getString("first_name"),
							rs.getString("last_name"),
							rs.getString("avatar_url"),
							rs.getString("gender"));
				} else {
					throw new UserNotFoundException(UserNotFoundException.USER_NOT_FOUND);
				}
			}
		}
	}
	
	public User getUser(long userId) throws SQLException, UserNotFoundException, UserException {
		String sql = "SELECT * FROM users WHERE user_id = ?;";
		try (PreparedStatement ps = con.prepareStatement(sql);) {
			ps.setLong(1, userId);
			try (ResultSet rs = ps.executeQuery();) {
				if (rs.next()) {
					return new User(
							rs.getLong("user_id"), 
							rs.getString("username"), 
							rs.getString("password"),
							rs.getString("facebook"), 
							rs.getString("email"),
							DateTimeConvertor.sqlToLdt(rs.getString("date_creation")), 
							rs.getString("first_name"),
							rs.getString("last_name"),
							rs.getString("avatar_url"),
							rs.getString("gender"));
				} else {
					throw new UserNotFoundException(UserNotFoundException.USER_NOT_FOUND);
				}
			}
		}
	}

	public boolean isFollowing(long user_id, long following_id) throws SQLException {
		String sql = "SELECT * FROM users_follow_users WHERE user_id = ? AND follower_id = ?";
		try (PreparedStatement ps = con.prepareStatement(sql);) {
			ps.setLong(1, user_id);
			ps.setLong(2, following_id);
			try (ResultSet rs = ps.executeQuery();) {
				if (rs.next()) {
					return true;
				}
				return false;
			}
		}
	}

	public void followUser(long user_id, long follower_id) throws SQLException {
		String follow = "INSERT INTO users_follow_users (user_id,follower_id) VALUES(?,?);";
		try (PreparedStatement ps = con.prepareStatement(follow);) {
			ps.setLong(1, user_id);
			ps.setLong(2, follower_id);
			ps.executeUpdate();
		}
	}

	public void unfollowUser(long user_id, long following_id) throws SQLException {
		String unfollow = "DELETE FROM users_follow_users WHERE user_id = ? AND follower_id = ?;";
		try (PreparedStatement ps = con.prepareStatement(unfollow);) {
			ps.setLong(1, user_id);
			ps.setLong(2, following_id);
			ps.executeUpdate();
		}
	}

	public List<User> getFollowers(long user_id) throws SQLException, UserNotFoundException, UserException {
		String unfollow = "SELECT users.* FROM users_follow_users AS follower JOIN users ON (follower.follower_id = users.user_id) WHERE follower.user_id = ?;";
		try (PreparedStatement ps = con.prepareStatement(unfollow);) {
			ps.setLong(1, user_id);
			try (ResultSet rs = ps.executeQuery();) {
				List<User> followers = new ArrayList<>();
				while (rs.next()) {
					followers.add(
							new User(
									rs.getLong("user_id"), 
									rs.getString("username"), 
									rs.getString("password"),
									rs.getString("facebook"), 
									rs.getString("email"),
									DateTimeConvertor.sqlToLdt(rs.getString("date_creation")), 
									rs.getString("first_name"),
									rs.getString("last_name"),
									rs.getString("avatar_url"),
									rs.getString("gender")));
				}
				return followers;
			}
		}
	}

	public List<User> getFollowing(long user_id) throws SQLException, UserNotFoundException, UserException {
		String unfollow = "SELECT users.* FROM users_follow_users AS follower JOIN users ON (follower.user_id = users.user_id) WHERE follower.follower_id = ?;";
		try (PreparedStatement ps = con.prepareStatement(unfollow);) {
			ps.setLong(1, user_id);
			try (ResultSet rs = ps.executeQuery();) {
				List<User> following = new ArrayList<>();
				while (rs.next()) {
					following.add(
							new User(
									rs.getLong("user_id"), 
									rs.getString("username"),
									rs.getString("password"),
									rs.getString("facebook"), 
									rs.getString("email"),
									DateTimeConvertor.sqlToLdt(rs.getString("date_creation")), 
									rs.getString("first_name"),
									rs.getString("last_name"),
									rs.getString("avatar_url"),
									rs.getString("gender")));
				}
				return following;
			}
		}
	}

	public void delete(long userId) {
		//TODO implement
		//delete followers
		//delete following
		//delete comment likes
		//delete comments
		//delete videos
		//delete video likes
		//delete playlists
	}
}