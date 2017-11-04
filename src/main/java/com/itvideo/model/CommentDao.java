package com.itvideo.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.itvideo.model.exceptions.comments.CommentException;
import com.itvideo.model.exceptions.comments.CommentNotFoundException;
import com.itvideo.model.exceptions.user.UserException;
import com.itvideo.model.exceptions.video.VideoException;
import com.itvideo.model.utils.DBConnection;
import com.itvideo.model.utils.DateTimeConvertor;
import com.mysql.jdbc.Statement;

@Component
public class CommentDao {
	public static final Comparator<Comment> ASC_BY_DATE = (c1, c2) -> c1.getDate().compareTo(c2.getDate());
	
	public static final Comparator<Comment> DESC_BY_DATE = (c1, c2) -> c2.getDate().compareTo(c1.getDate());
	
	public static final Comparator<Comment> DESC_BY_LIKES = (c1, c2) -> {
		if(c1.getLikes() == c2.getLikes()) {
			if(c2.getDislikes() > c1.getDislikes()) {
				return -1;
			}else {
				return 1;
			}
		}
		return (c2.getLikes()-c1.getLikes()) > 0 ? 1 : -1;
	};
	
	public static final Comparator<Comment> DESC_BY_DISLIKES = (c1, c2) -> {
		if(c2.getDislikes() == c1.getDislikes()) {
			if(c2.getLikes()>c1.getLikes()) {
				return -1;
			}else {
				return 1;
			}
		}
		return (c2.getDislikes()-c1.getDislikes()) > 0 ? 1 : -1;
	};

	private Connection con;
	
	private CommentDao() {
	}

	@Autowired
	private void initCon() {
		con = DBConnection.COMMENTS.getConnection();
	}

	/**
	 * @param comment -new comment
	 * @throws SQLException
	 */
	public void createComment(Comment comment) throws SQLException{
		String sql = "INSERT INTO comments (text, date,video_id, user_id, reply_id) VALUES(?,?,?,?,?)";
		try (PreparedStatement ps = con.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS)) {
			ps.setString(1, comment.getText());
			ps.setString(2, DateTimeConvertor.ldtToSql(comment.getDate()));
			ps.setLong(3, comment.getVideoId());
			ps.setLong(4, comment.getUserId());
			if (comment.getReplyId() != 0) {
				ps.setLong(5, comment.getReplyId());
			} else {
				ps.setString(5, null);
			}
			ps.executeUpdate();
			try (ResultSet rs = ps.getGeneratedKeys()) {
				rs.next();
				long id = rs.getLong(1);
				comment.setId(id);
			}
		}
	}
	
	/**
	 * delete all votes for comments with author user,all votes by user,all comments and replies with author user
	 * @param userId
	 * @throws SQLException
	 */
	public void deleteAllCommentsAndLikesForUser(long userId) throws SQLException {
		//delete votes for replies in comments by user with id:
		String sql1 = "DELETE cl.* FROM comments_likes AS cl INNER JOIN comments AS r ON(cl.comment_id=r.comment_id) "
				+ "INNER JOIN comments AS c ON(r.reply_id=c.comment_id) WHERE c.user_id=?;";
		//delete likes/dislikes for comments by user_id;
		String sql2 = "DELETE FROM comments_likes WHERE user_id=?";
		//delete all replies to comments from user_id;
		String sql3 =  "DELETE FROM comments WHERE comment_id IN "
				+ "(SELECT * FROM (SELECT r.comment_id FROM comments AS c "
				+ "INNER JOIN comments AS r ON (r.reply_id = c.comment_id) WHERE c.user_id=?) AS d);";
		//delete all comments to user_id;
		String sql4 = "DELETE FROM comments WHERE user_id=?";
		synchronized (con) {
			try {
				con.setAutoCommit(false);
				try (PreparedStatement ps = con.prepareStatement(sql1)) {
					ps.setLong(1, userId);
					ps.executeUpdate();
				}
				try (PreparedStatement ps = con.prepareStatement(sql2)) {
					ps.setLong(1, userId);
					ps.executeUpdate();
				}
				try (PreparedStatement ps = con.prepareStatement(sql3)) {
					ps.setLong(1, userId);
					ps.executeUpdate();
				}
				try (PreparedStatement ps = con.prepareStatement(sql4)) {
					ps.setLong(1, userId);
					ps.executeUpdate();
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

	/**
	 * delete all votes for comment replies and for comment, delete all replies for comment,delete the comment;
	 * @param commentId
	 * @throws SQLException
	 */
	public void deleteComment(long commentId) throws SQLException {
		//delete votes for replies to this comment
		String sql1 = "DELETE FROM comments_likes WHERE comment_id IN "
				+ "(SELECT * FROM (SELECT r.comment_id FROM comments AS c "
				+ "INNER JOIN comments AS r ON (r.reply_id = c.comment_id) WHERE c.comment_id=?) AS d);";
		// delete votes for comment
		String sql2 = "DELETE FROM comments_likes WHERE comment_id=?";
		// delete all replies for comment
		String sql3 = "DELETE FROM comments WHERE comment_id IN "
				+ "(SELECT * FROM (SELECT r.comment_id FROM comments AS c "
				+ "INNER JOIN comments AS r ON (r.reply_id = c.comment_id) WHERE c.comment_id=?) AS d);";
		//delete comment
		String sql4 = "DELETE FROM comments WHERE comment_id=?";
		synchronized (con) {
			try {
				con.setAutoCommit(false);
				try (PreparedStatement ps = con.prepareStatement(sql1)) {
					ps.setLong(1, commentId);
					ps.executeUpdate();
				}
				try (PreparedStatement ps = con.prepareStatement(sql2)) {
					ps.setLong(1, commentId);
					ps.executeUpdate();
				}
				try (PreparedStatement ps = con.prepareStatement(sql3)) {
					ps.setLong(1, commentId);
					ps.executeUpdate();
				}
				try (PreparedStatement ps = con.prepareStatement(sql4)) {
					ps.setLong(1, commentId);
					ps.executeUpdate();
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

	/**
	 * @param videoId
	 * @return deleted comments
	 * @throws SQLException
	 */
	public void deleteAllCommentsForVideo(long videoId) throws SQLException {
		String sql1 = "DELETE cl . * FROM comments_likes AS cl INNER JOIN comments AS r ON (cl.comment_id = r.comment_id) "
				+ "INNER JOIN comments AS c ON (r.reply_id = c.comment_id) WHERE c.video_id = ?;";
		String sql2 = "DELETE FROM comments WHERE video_id = ? AND reply_id IS NOT null;";
		String sql3 = "DELETE FROM comments WHERE video_id = ?";
		synchronized (con) {
			try {
				con.setAutoCommit(false);
				try (PreparedStatement ps = con.prepareStatement(sql1)) {
					ps.setLong(1, videoId);
					ps.executeUpdate();
				}
				try (PreparedStatement ps = con.prepareStatement(sql2)) {
					ps.setLong(1, videoId);
					ps.executeUpdate();
				}
				try (PreparedStatement ps = con.prepareStatement(sql3)) {
					ps.setLong(1, videoId);
					ps.executeUpdate();
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

	/**
	 * @param commentId
	 * @param userId
	 * @throws SQLException
	 * @throws CommentException
	 * @throws UserException
	 */
	public void dislikeComment(long commentId, long userId) throws SQLException, CommentException, UserException {
		String sql = "select isLike from comments_likes where user_id=? and comment_id=?;";
		try (PreparedStatement ps = con.prepareStatement(sql)) {
			ps.setLong(1, userId);
			ps.setLong(2, commentId);
			try (ResultSet rs = ps.executeQuery()) {
				if (!rs.next()) {
					sql = "insert into comments_likes (user_id,comment_id,isLike) values (?,?,0)";
				} else {
					if (!rs.getBoolean(1)) {
						sql = "delete from comments_likes where user_id=? and comment_id=?";
					} else {
						sql = "update comments_likes set isLike=0 where user_id=? and comment_id=?";
					}
				}
			}
		}
		try (PreparedStatement ps = con.prepareStatement(sql)) {
			ps.setLong(1, userId);
			ps.setLong(2, commentId);
			ps.executeUpdate();
		}
	}
	
	public long getNumberOfCommentsAndRepliesForVideo(long videoId) throws SQLException{
		String sql = "SELECT COUNT(*) FROM comments WHERE video_id=?;";
		long count=0;
		try (PreparedStatement ps = con.prepareStatement(sql)) {
			ps.setLong(1, videoId);
			try (ResultSet rs = ps.executeQuery()) {
				rs.next();
				count = rs.getLong(1);
			}
		}
		return count;
	}

	/**
	 * @param videoId
	 * @param withReplies
	 *            - if true comments and replies sorted by date,else only
	 *            comments ordered by date
	 * @return List<Comments>
	 * @throws VideoException
	 *             -for invalid id = 0
	 * @throws SQLException
	 */
	public List<Comment> getAllComments(long videoId, boolean withReplies) throws VideoException, SQLException {
		String addition = "";
		if (!withReplies) {
			addition = "and reply_id is null ";
		}
		String sql = "select * from comments where video_id=? " + addition + "order by date;";
		try (PreparedStatement ps = con.prepareStatement(sql)) {
			ps.setLong(1, videoId);
			try (ResultSet rs = ps.executeQuery()) {
				List<Comment> comments = new ArrayList<>();
				while (rs.next()) {
					Long id = rs.getLong("comment_id");
					String text = rs.getString("text");
					LocalDateTime date = DateTimeConvertor.sqlToLdt(rs.getString("date"));
					Long userId = rs.getLong("user_id");
					Long replyId = rs.getLong("reply_id");
					Comment reply = new Comment(id, text, date, userId, videoId, replyId);
					comments.add(reply);
				}
				return comments;
			}
		}
	}

	/**
	 * @param userId
	 * @param withReplies
	 *            -if true comments and replies sorted by date,else only
	 *            comments ordered by date
	 * @return
	 * @throws SQLException
	 * @throws UserException
	 *             -for invalid id = 0
	 */
	public List<Comment> getAllCommentsByUser(long userId, boolean withReplies) throws SQLException, UserException {
		String addition = "";
		if (!withReplies) {
			addition = "and reply_id is null ";
		}
		String sql = "select * from comments where user_id=? " + addition + "order by date;";
		try (PreparedStatement ps = con.prepareStatement(sql)) {
			ps.setLong(1, userId);
			try (ResultSet rs = ps.executeQuery()) {
				List<Comment> comments = new ArrayList<>();
				while (rs.next()) {
					Long id = rs.getLong("comment_id");
					String text = rs.getString("text");
					LocalDateTime date = DateTimeConvertor.sqlToLdt(rs.getString("date"));
					Long videoId = rs.getLong("video_id");
					Long replyId = rs.getLong("reply_id");
					Comment reply = new Comment(id, text, date, userId, videoId, replyId);
					comments.add(reply);
				}
				return comments;
			}
		}
	}
	/**
	 * @param videoId
	 * @param myUserId
	 * @param comparator
	 * @return all comments for video without replies,sorted by comparator
	 * @throws SQLException
	 */
	//TODO make it with part and number from DB and set number of replies
	public List<Comment> getAllCommentWithVotesByVideoWithoutReplies(long videoId,long myUserId,Comparator<Comment> comparator) throws SQLException {
		//get all comments without replies with vote
		String sql="select c.*,sum(if(l.isLike = 1, 1, 0)) as likes,"
				+ "sum(if(l.isLike = 0, 1, 0)) as dislikes,"
				+ "sum(if(l.user_id=?,if(l.isLike=1,1,-1),0)) as my_vote "
				+ "from comments as c left join comments_likes as l "
				+ "on(c.comment_id=l.comment_id) "
				+ "where c.video_id=? and c.reply_id is null group by (c.comment_id);";
		String repNumber="select count(*) from comments where reply_id=?;";
		List<Comment> comments = new ArrayList<>();
		try (PreparedStatement ps = con.prepareStatement(sql)) {
			ps.setLong(1, myUserId);
			ps.setLong(2, videoId);
			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					Long id = rs.getLong("comment_id");
					String text = rs.getString("text");
					LocalDateTime date = DateTimeConvertor.sqlToLdt(rs.getString("date"));
					Long userId = rs.getLong("user_id");
					Comment comment = new Comment(id, text, date, userId, videoId, (long)0);
					comment.setLikes(rs.getLong("likes"));
					comment.setDislikes(rs.getLong("dislikes"));
					comment.setVote(rs.getInt("my_vote"));
					loadUserInfo(comment);
					comments.add(comment);
				}
			}
		}
		//TODO replace this code with one querry
		for(Comment c:comments) {
			try (PreparedStatement ps = con.prepareStatement(repNumber)) {
				ps.setLong(1, c.getCommentId());
				try(ResultSet rs =ps.executeQuery()) {
					rs.next();
					c.setNumberReplies(rs.getLong(1));
				}
			}
		}
		//sort by comparator
		Collections.sort(comments, comparator);
		return comments;
	}


	/**
	 * @param commentId
	 * @return -empty ArrayList list if no comments, or ordered list replies by
	 *         date
	 * @throws SQLException
	 * @throws CommentException
	 *             -if comment have no id
	 */
	public List<Comment> getAllReplies(long commentId) throws SQLException, CommentException {
		String sql = "select * from comments where reply_id=? order by date;";
		try (PreparedStatement ps = con.prepareStatement(sql)) {
			ps.setLong(1, commentId);
			try (ResultSet rs = ps.executeQuery()) {
				List<Comment> replies = new ArrayList<>();
				while (rs.next()) {
					Long id = rs.getLong("comment_id");
					String text = rs.getString("text");
					LocalDateTime date = DateTimeConvertor.sqlToLdt(rs.getString("date"));
					Long userId = rs.getLong("user_id");
					Long videoId = rs.getLong("video_id");
					// because this is reply there is no reply to this
					// comment!
					Long replyId = (long) 0;
					Comment reply = new Comment(id, text, date, userId, videoId, replyId);
					replies.add(reply);
				}
				return replies;
			}
		}
	}
	/**
	 * 
	 * @param commentId
	 * @param myUserId
	 * @param comparator
	 * @return all replies for comment with comparator
	 * @throws SQLException
	 */
	public List<Comment> getAllRepliesWithVotesForComment (long commentId,long myUserId,Comparator<Comment> comparator) throws SQLException{
		String sql="select c.*,sum(if(l.isLike = 1, 1, 0)) as likes,"
				+ "sum(if(l.isLike = 0, 1, 0)) as dislikes,"
				+ "sum(if(l.user_id=?,if(l.isLike=1,1,-1),0)) as my_vote "
				+ "from comments as c left join comments_likes as l "
				+ "on(c.comment_id=l.comment_id) where c.reply_id=? "
				+ "group by (c.comment_id);";
		List<Comment> replies = new ArrayList<>();
		try (PreparedStatement ps = con.prepareStatement(sql)) {
			ps.setLong(1, myUserId);
			ps.setLong(2, commentId);
			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					Long id = rs.getLong("comment_id");
					String text = rs.getString("text");
					LocalDateTime date = DateTimeConvertor.sqlToLdt(rs.getString("date"));
					Long userId = rs.getLong("user_id");
					Long videoId = rs.getLong("video_id");
					Long replyId = rs.getLong("reply_id");
					Comment reply = new Comment(id, text, date, userId, videoId, replyId);
					reply.setLikes(rs.getLong("likes"));
					reply.setDislikes(rs.getLong("dislikes"));
					reply.setVote(rs.getInt("my_vote"));
					loadUserInfo(reply);
					replies.add(reply);
				}
			}
		}
		Collections.sort(replies, comparator);
		return replies;
	}
	/**
	 * @param commentId
	 * @return integer
	 * @throws SQLException
	 */
	public int getDislikes(long commentId) throws SQLException {
		String sql = "select count(*) from comments_likes where comment_id=? and isLike = 0;";
		try (PreparedStatement ps = con.prepareStatement(sql)) {
			ps.setLong(1, commentId);
			try (ResultSet rs = ps.executeQuery()) {
				// there is always information
				rs.next();
				int dislikes = rs.getInt(1);
				return dislikes;
			}
		}
	}
	
	/**
	 * @param commentId
	 * @return integer
	 * @throws SQLException
	 */
	public int getLikes(long commentId) throws SQLException {
		String sql = "select count(*) from comments_likes where comment_id=? and isLike = 1;";
		try (PreparedStatement ps = con.prepareStatement(sql)) {
			ps.setLong(1, commentId);
			try (ResultSet rs = ps.executeQuery()) {
				// there is always information
				rs.next();
				int likes = rs.getInt(1);
				return likes;
			}
		}
	}

	/**
	 * @param commentId
	 *            must have comment_id
	 * @param userId
	 *            - must have id
	 * @throws SQLException
	 * @throws CommentException
	 * @throws UserException
	 */
	public void likeComment(long commentId, long userId) throws SQLException, CommentException, UserException {
		String sql = "select isLike from comments_likes where user_id=? and comment_id=?;";
		try (PreparedStatement ps = con.prepareStatement(sql)) {
			ps.setLong(1, userId);
			ps.setLong(2, commentId);
			try (ResultSet rs = ps.executeQuery()) {
				if (!rs.next()) {
					sql = "insert into comments_likes (user_id,comment_id,isLike) values (?,?,1)";
				} else {
					if (rs.getBoolean(1)) {
						sql = "delete from comments_likes where user_id=? and comment_id=?";
					} else {
						sql = "update comments_likes set isLike=1 where user_id=? and comment_id=?";
					}
				}
			}
		}
		try (PreparedStatement ps = con.prepareStatement(sql)) {
			ps.setLong(1, userId);
			ps.setLong(2, commentId);
			ps.executeUpdate();
		}
	}
	/**
	 * add in comment users info-name and img
	 * @param comment
	 * @throws SQLException
	 */
	public void loadUserInfo(Comment comment) throws SQLException {
		String sql = "select u.username, u.avatar_url as url from users as u "
				+ "inner join comments as c on(c.user_id=u.user_id) where comment_id=?;";
		try (PreparedStatement ps = con.prepareStatement(sql)) {
			ps.setLong(1, comment.getCommentId());
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					comment.setUsername(rs.getString("username"));
					comment.setUrl(rs.getString("url"));
				}
			}
		}
	}
	/**
	 * @param comment-comment
	 *            with changes
	 * @throws SQLException
	 * @throws CommentNotFoundException
	 *             - if cant find comment
	 * @throws CommentException
	 *             -if cant update in db
	 */
	public void updateComment(Comment comment) throws SQLException, CommentException {
		// if comment was deleted from another user before load page
		String foundComment = "select comment_id from comments where comment_id=?;";
		try (PreparedStatement ps = con.prepareStatement(foundComment)) {
			ps.setLong(1, comment.getCommentId());
			try (ResultSet rs = ps.executeQuery()) {
				if (!rs.next()) {
					ps.close();
					rs.close();
					throw new CommentNotFoundException();
				}
			}
		}
		String sql = "UPDATE comments SET text=?, date=STR_TO_DATE(?,'%Y-%m-%d %H:%i:%s.%f'),video_id=?, user_id=?, reply_id=? WHERE comment_id="
				+ comment.getCommentId();
		try (PreparedStatement ps = con.prepareStatement(sql)) {
			ps.setString(1, comment.getText());
			ps.setString(2, DateTimeConvertor.ldtToSql(comment.getDate()));
			ps.setLong(3, comment.getVideoId());
			ps.setLong(4, comment.getUserId());
			if (comment.getReplyId() != 0) {
				ps.setLong(5, comment.getUserId());
			} else {
				ps.setString(5, null);
			}
			int i = ps.executeUpdate();
			if (i == 0) {
				throw new CommentException(CommentException.CANT_UPDATE);
			}
		}
	}
}
