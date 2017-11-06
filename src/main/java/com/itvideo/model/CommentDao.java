package com.itvideo.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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
	public void deleteAllCommentsAndLikesForUser(long userId,Connection con) throws SQLException {
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
			} catch (SQLException e) {
				e.printStackTrace();
				throw e;
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
	 * delete all comments and replies and their votes for video with id,<b> use in transaction!</b>
	 * @param videoId
	 * @throws SQLException
	 */
	public void deleteAllCommentsForVideo(long videoId,Connection con) throws SQLException {
		String sql1 = "DELETE cl . * FROM comments_likes AS cl INNER JOIN comments AS r ON (cl.comment_id = r.comment_id) "
				+ "INNER JOIN comments AS c ON (r.reply_id = c.comment_id) WHERE c.video_id = ?;";
		String sql2 = "DELETE FROM comments WHERE video_id = ? AND reply_id IS NOT null;";
		String sql3 ="DELETE cl . * FROM comments_likes AS cl INNER JOIN comments AS c ON(cl.comment_id=c.comment_id) WHERE c.video_id=?";
		String sql4 = "DELETE FROM comments WHERE video_id = ?";
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
		try (PreparedStatement ps = con.prepareStatement(sql4)) {
			ps.setLong(1, videoId);
			ps.executeUpdate();
		}
	}

	/**
	 * @param commentId
	 * @param userId
	 * @param vote 1 for like,0 for dislike
	 * @throws SQLException
	 */
	public void likeComment(long commentId,long userId,int vote) throws SQLException {
		String sql = "SELECT isLike FROM comments_likes WHERE user_id=? AND comment_id=?;";
		try (PreparedStatement ps = con.prepareStatement(sql)) {
			ps.setLong(1, userId);
			ps.setLong(2, commentId);
			try (ResultSet rs = ps.executeQuery()) {
				if (!rs.next()) {
					sql = "INSERT INTO comments_likes (user_id,comment_id,isLike) VALUES (?,?,"+vote+")";
				} else {
					if(vote==1) {
						//like
						if (rs.getBoolean(1)) {
							sql = "DELETE FROM comments_likes WHERE user_id=? AND comment_id=?";
						} else {
							sql = "UPDATE comments_likes SET isLike=1 WHERE user_id=? AND comment_id=?";
						}
					}else {
						//dislike
						if (!rs.getBoolean(1)) {
							sql = "DELETE FROM comments_likes WHERE user_id=? AND comment_id=?";
						} else {
							sql = "UPDATE comments_likes SET isLike=0 WHERE user_id=? AND comment_id=?";
						}
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

	public long getNumberOfCommentsForVideo(long videoId) throws SQLException{
		String sql = "SELECT COUNT(*) FROM comments WHERE video_id=? and reply_id IS NULL;";
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
	 * @param commentId
	 * @param myUserId
	 * @param comparator
	 * @return all replies for comment with comparator
	 * @throws SQLException
	 */
	public List<Comment> getAllRepliesWithVotesForComment (long commentId,long myUserId,String  comparator) throws SQLException{
		comparator=createComparator(comparator);
		String sql="SELECT c.*,SUM(if(l.isLike = 1, 1, 0)) AS likes,"
				+ "SUM(IF(l.isLike = 0, 1, 0)) AS dislikes,"
				+ "SUM(IF(l.user_id = ?, IF(l.isLike=1,1,-1),0)) AS my_vote "
				+ "FROM comments AS c LEFT JOIN comments_likes as l "
				+ "ON(c.comment_id=l.comment_id) WHERE c.reply_id=? "
				+ "GROUP BY (c.comment_id)  ORDER BY "+comparator+";";
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
		return replies;
	}

	/**
	 * add in comment users info-name and img url
	 * @param comment
	 * @throws SQLException
	 */
	public void loadUserInfo(Comment comment) throws SQLException {
		String sql = "SELECT u.username, u.avatar_url AS url FROM users AS u "
				+ "INNER JOIN comments AS c on(c.user_id=u.user_id) WHERE comment_id=?;";
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
	
	private String createComparator(String comparator) {
		if(comparator.equals("likes desc")||comparator.equals("dislikes desc")) {
			return comparator+",c.date desc";
		}else {
			return comparator;
		}
	}
	//SHOWME: comment query
	public List<Comment> getAllCommentsWithVotesByVideoWithoutReplies(int pageid,long videoId,long myUserId,String comparator,String dateFromRequest) throws SQLException {
		int count=10;
		comparator=createComparator(comparator);
		//get all comments without replies with vote
		String sql="SELECT c.*, SUM( IF( l.isLike = 1, 1, 0)) AS likes,"
				+ "SUM( IF( l.isLike = 0, 1, 0)) AS dislikes,"
				+ "SUM( IF( l.user_id = ?, IF( l.isLike = 1, 1, -1),0)) AS my_vote "
				+ "FROM comments AS c LEFT JOIN comments_likes AS l "
				+ "ON( c.comment_id = l.comment_id) "
				+ "WHERE c.video_id=? AND c.date <= STR_TO_DATE(?,'%Y-%m-%d %H:%i:%s.%f') "
				+ "AND c.reply_id IS NULL GROUP BY (c.comment_id) ORDER BY "+comparator
				+ " LIMIT "+count+" OFFSET "+count*pageid;
		List<Comment> comments = new ArrayList<>();
		try (PreparedStatement ps = con.prepareStatement(sql)) {
			ps.setLong(1, myUserId);
			ps.setLong(2, videoId);
			LocalDateTime dateRequest=LocalDateTime.parse(dateFromRequest);
			ps.setString(3, DateTimeConvertor.ldtToSql(dateRequest));
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
		String repNumber="SELECT COUNT(*) FROM comments WHERE reply_id=?;";
		for(Comment c:comments) {
			try (PreparedStatement ps = con.prepareStatement(repNumber)) {
				ps.setLong(1, c.getCommentId());
				try(ResultSet rs =ps.executeQuery()) {
					rs.next();
					c.setNumberReplies(rs.getLong(1));
				}
			}
		}
		return comments;
	}
}
