package com.itvideo.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.itvideo.model.exceptions.user.UserException;
import com.itvideo.model.exceptions.user.UserNotFoundException;
import com.itvideo.model.interfaces.Searchable;
import com.itvideo.model.utils.DBConnection;
import com.itvideo.model.utils.DateTimeConvertor;


@Component
public class UserDao {

	@Autowired
	CommentDao cd;
	
	@Autowired
	VideoDao vd;
	
	@Autowired
	PlaylistDao pd;
	
	private Connection con;
	
	@Autowired
    private void initField() {
		 con = DBConnection.USERS.getConnection();
    }
	
	public void activateUser(long userId) throws SQLException {
		String sql = "UPDATE users SET activated = 1 WHERE user_id = ?;";
		try (PreparedStatement ps = con.prepareStatement(sql);) {
			ps.setLong(1, userId);
			ps.executeUpdate();
		}
	}

	public void createUser(User u) throws SQLException, UserException {
		String sql = "INSERT INTO users (username, password, email, date_creation, avatar_url, register_token) VALUES (?, ?, ?, ?, ?, ?)";
		try (PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);) {
			ps.setString(1, u.getUsername());
			ps.setString(2, u.getPassword());
			ps.setString(3, u.getEmail());
			ps.setString(4, DateTimeConvertor.ldtToSql(u.getDateCreation()));
			ps.setString(5, u.getAvatarUrl());
			ps.setString(6, u.getActivationToken());
			ps.executeUpdate();
			try (ResultSet rs = ps.getGeneratedKeys();) {
				rs.next();
				u.setUserId(rs.getLong(1));
			}
		}
	}

	public void delete(long userId) throws SQLException {
		synchronized (con) {
			con.setAutoCommit(false);
			try {
				// delete followers
				deleteFollowers(userId);

				// delete following
				deleteFollowings(userId);

				// delete comment likes
				// delete comments
				cd.deleteAllCommentsAndLikesForUser(userId,con);

				// delete videos
				vd.deleteUserVideos(userId, con);
				// delete video likes
				vd.deleteAllVideoLikes(userId);
				
				// delete playlists
				pd.deletePlaylistsForUser(userId,con);
				
				//delete the user, or whatever is left of it
				String del = "DELETE FROM users WHERE user_id = ?;";
				try (PreparedStatement ps = con.prepareStatement(del);) {
					ps.setLong(1,userId);
					ps.executeUpdate();
				}
				
				con.commit();
			} catch (SQLException e) {
				e.printStackTrace();
				con.rollback();
				throw e;
			} finally {
				con.setAutoCommit(true);
			}
		}
	}

	private void deleteFollowers(long userId) throws SQLException {
		String del = "DELETE FROM users_follow_users WHERE user_id = ?;";
		try (PreparedStatement ps = con.prepareStatement(del);) {
			ps.setLong(1,userId);
			ps.executeUpdate();
		}
	}

	private void deleteFollowings(long userId) throws SQLException {
		String del = "DELETE FROM users_follow_users WHERE follower_id = ?;";
		try (PreparedStatement ps = con.prepareStatement(del);) {
			ps.setLong(1,userId);
			ps.executeUpdate();
		}
	}
	
	public boolean existsEmail(String email) throws SQLException {
		String sql = "SELECT * FROM users WHERE email = ?;";
		try (PreparedStatement ps = con.prepareStatement(sql);) {
			ps.setString(1, email);
			try (ResultSet rs = ps.executeQuery();) {
				if (rs.next()) {
					return true;
				}
				return false;
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
	
	public void followUser(long user_id, long follower_id) throws SQLException {
		String follow = "INSERT INTO users_follow_users (user_id,follower_id) VALUES(?,?);";
		try (PreparedStatement ps = con.prepareStatement(follow);) {
			ps.setLong(1, user_id);
			ps.setLong(2, follower_id);
			ps.executeUpdate();
		}
	}

	public String getAvatarUrl(Long userId) throws SQLException, UserNotFoundException {
		String sql = "SELECT avatar_url FROM users WHERE user_id = ?;";
		try (PreparedStatement ps = con.prepareStatement(sql);) {
			ps.setLong(1, userId);
			try (ResultSet rs = ps.executeQuery();) {
				if (rs.next()) {
					return rs.getString("avatar_url");
				}
				throw new UserNotFoundException(UserNotFoundException.USER_NOT_FOUND);
			}
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
									rs.getString("gender"),
									rs.getString("register_token"),
									rs.getBoolean("activated")));
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
									rs.getString("gender"),
									rs.getString("register_token"),
									rs.getBoolean("activated")));
				}
				return following;
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
							rs.getString("gender"),
							rs.getString("register_token"),
							rs.getBoolean("activated"));
				} else {
					throw new UserNotFoundException(UserNotFoundException.USER_NOT_FOUND);
				}
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
							rs.getString("gender"),
							rs.getString("register_token"),
							rs.getBoolean("activated"));
				} else {
					throw new UserNotFoundException(UserNotFoundException.USER_NOT_FOUND);
				}
			}
		}
	}

	public User getUserByEmail(String email) throws SQLException, UserException, UserNotFoundException {
		String sql = "SELECT * FROM users WHERE email = ?;";
		try (PreparedStatement ps = con.prepareStatement(sql);) {
			ps.setString(1, email);
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
							rs.getString("gender"),
							rs.getString("register_token"),
							rs.getBoolean("activated"));
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

	public List<Searchable> searchUser(String username) throws SQLException, UserException {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT * FROM users");
		
		String[] words = username.split("\\s+");
		
		for (int i = 0; i < words.length; i++) {
			if (i == 0) {
				sql.append(" WHERE");
			}
			if (i == words.length - 1) {
				sql.append(" username LIKE ?;");
			} else {
				sql.append(" username LIKE ? AND");
			}
		}
		
		try (PreparedStatement ps = con.prepareStatement(sql.toString());) {
			for (int i = 0; i < words.length; i++) {
				ps.setString(i+1, "%" + words[i] + "%");
			}
			
			try (ResultSet rs = ps.executeQuery();) {
				List<Searchable> users = new ArrayList<>();
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
									rs.getString("gender"),
									rs.getString("register_token"),
									rs.getBoolean("activated")));
				}
				return users;
			}
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

	public void updateUser(User u) throws SQLException, UserNotFoundException {
		synchronized (con) {
			con.setAutoCommit(false);
			String sql = "UPDATE users SET username = ?, password = ?, facebook = ?, password = ?, email = ?, first_name = ?, last_name = ?, avatar_url = ?, gender = ? WHERE user_id = ? ;";
			try (PreparedStatement ps = con.prepareStatement(sql);) {
				ps.setString(1, u.getUsername());
				ps.setString(2, u.getPassword());
				ps.setString(3, u.getFacebook());
				ps.setString(4, u.getPassword());
				ps.setString(5, u.getEmail());
				ps.setString(6, u.getFirstName());
				ps.setString(7, u.getLastName());
				ps.setString(8, u.getAvatarUrl());
				ps.setString(9, u.getGender());
				ps.setLong(10, u.getUserId());
				int affectedRows = ps.executeUpdate();
				if (affectedRows == 0) {
					throw new UserNotFoundException(UserNotFoundException.USER_NOT_FOUND);
				}
				con.commit();
			} catch (SQLException e) {
				con.rollback();
				throw e;
			} finally {
				con.setAutoCommit(true);
			}
		}
	}
}