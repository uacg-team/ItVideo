package  com.itvideo.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;


import com.itvideo.model.exceptions.comments.CommentException;
import com.itvideo.model.exceptions.comments.CommentNotFoundException;
import com.itvideo.model.exceptions.user.UserException;
import com.itvideo.model.exceptions.video.VideoException;
import com.itvideo.model.utils.DBConnection;
import com.itvideo.model.utils.DateTimeConvertor;


public class CommentDao {
	private final Connection con;
	private static CommentDao instance;
	public static final Comparator<Comment> ASC_BY_DATE = (o1,o2)->o1.getDate().compareTo(o2.getDate());
	public static final Comparator<Comment> DESC_BY_DATE = (o1,o2)->o2.getDate().compareTo(o1.getDate());
	private CommentDao() {
		con = DBConnection.CON1.getConnection();
	}

	public static CommentDao getInstance() {
		if (instance == null) {
			instance = new CommentDao();
		}
		return instance;
	}

	/**
	 * <b>!Warning! set replyId only to comment,not for reply!</b>
	 * 
	 * @param comment
	 *            must have text,date,videoId,userId,no need of commentId;
	 * @throws SQLException
	 * @throws CommentException
	 */
	public void createComment(Comment comment) throws SQLException, CommentException {
		String sql = "insert into comments (text, date,video_id, user_id, reply_id) values(?,?,?,?,?)";
		try (PreparedStatement ps = con.prepareStatement(sql)) {
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
	 * @param comment-comment
	 *            with changes
	 * @throws SQLException
	 * @throws CommentNotFoundException
	 *             - if cant find comment
	 * @throws CommentException
	 *             -if cant update in db
	 */
	public void updateComment(Comment comment) throws SQLException, CommentException {
		//if comment was deleted from another user before load page
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

	/**
	 * 
	 * @param c
	 * @return comments deleted
	 * @throws CommentException
	 * @throws SQLException
	 */
	public int deleteComment(long commentId) throws CommentException, SQLException {
		int count=0;
		synchronized (con) {
			try {
				con.setAutoCommit(false);
				
				deleteAllLikesForCommentAndReplies(commentId);
				
				count = deleteAllRepliesToComment(commentId);
				
				String sql = "delete from comments where comment_id=?";
				try (PreparedStatement ps = con.prepareStatement(sql)) {
					ps.setLong(1, commentId);
					count += ps.executeUpdate();
				}
				con.commit();
			}catch(SQLException e) {
				con.rollback();
				throw new SQLException(e);
			}finally {
				con.setAutoCommit(true);
			}
		}
		return count;
	}

	/**
	 * @param commentId
	 * @return count deleted comments
	 * @throws SQLException
	 * @throws CommentException
	 */
	private int deleteAllRepliesToComment(long commentId) throws SQLException {
		String sql = "delete from comments where comment_id in "
				+ "(select * from (select r.comment_id from comments as c "
				+ "inner join comments as r on (r.reply_id = c.comment_id) where c.comment_id=?) as d);";
		int count=0;
		try (PreparedStatement ps = con.prepareStatement(sql)) {
			ps.setLong(1, commentId);
			count = ps.executeUpdate();
		}
		return count;
	}
	//use in transaction!
	private int deleteAllLikesForCommentAndReplies(long commentId) throws SQLException {
		//delete all likes for replies
		String sql1 = "delete from comments_likes where comment_id in "
				+ "(select * from (select r.comment_id from comments as c "
				+ "inner join comments as r on (r.reply_id = c.comment_id) where c.comment_id=?) as d);";
		//delete all likes for comment
		String sql2 = "delete from comments_likes where comment_id=?";
		int count =0;
		try (PreparedStatement ps = con.prepareStatement(sql1)) {
			ps.setLong(1, commentId);
			count = ps.executeUpdate();
		}
		try (PreparedStatement ps = con.prepareStatement(sql2)) {
			ps.setLong(1, commentId);
			count += ps.executeUpdate();
		}
		return count;
	}

	/**
	 * 
	 * @param videoId
	 * @return deleted comments
	 * @throws SQLException
	 */
	public int deleteComments(long videoId) throws SQLException {
		int count = 0;
		synchronized (con) {
			try {
				con.setAutoCommit(false);
				String deleteLikes = "delete from comments_likes where comment_id in "
						+ "(select * from (select c.comment_id from comments as c "
						+ "inner join videos as v on (v.video_id = c.video_id) " 
						+ "where c.video_id=?) as co);";
				String deleteReplies = "delete from comments where video_id = ? and reply_id is not null;";
				String deleteComments = "delete from comments where video_id = ?";
				try (PreparedStatement ps = con.prepareStatement(deleteLikes)) {
					ps.setLong(1, videoId);
				}
				try (PreparedStatement ps = con.prepareStatement(deleteReplies)) {
					ps.setLong(1, videoId);
					count += ps.executeUpdate();
				}
				try (PreparedStatement ps = con.prepareStatement(deleteComments)) {
					ps.setLong(1, videoId);
					count += ps.executeUpdate();
				}
				con.commit();
			} catch(SQLException e){
				con.rollback();
				throw new SQLException(e);
			}finally {
				con.setAutoCommit(true);
			}
		}
		return count;
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
	public void loadUserInfo(Comment comment) throws SQLException {
		String sql="select u.username, u.avatar_url as url from users as u "
				+ "inner join comments as c on(c.user_id=u.user_id) where comment_id=?;";
		try (PreparedStatement ps = con.prepareStatement(sql)) {
			ps.setLong(1, comment.getCommentId());
			try (ResultSet rs = ps.executeQuery()) {
				if(rs.next()) {
					comment.setUsername(rs.getString("username"));
					comment.setUrl(rs.getString("url"));
				}
			}
		}
	}

	public static void main(String[] args) throws SQLException, CommentException {
		// generating comments
		for (int i = 0; i < 15; i++) {
			CommentDao.getInstance().createComment(new Comment("comment" + i, LocalDateTime.now(), 1, 1, 0));
		}
		// replies
		for (int i = 0; i < 15; i++) {
			CommentDao.getInstance()
					.createComment(new Comment("reply" + i, LocalDateTime.now(), 1, 1, 1 + new Random().nextInt(15)));
		}
	}
}
