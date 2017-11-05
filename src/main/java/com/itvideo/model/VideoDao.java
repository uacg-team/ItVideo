package com.itvideo.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.swing.tree.RowMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.itvideo.model.exceptions.tags.TagNotFoundException;
import com.itvideo.model.exceptions.user.UserNotFoundException;
import com.itvideo.model.exceptions.video.VideoException;
import com.itvideo.model.exceptions.video.VideoNotFoundException;
import com.itvideo.model.utils.DBConnection;
import com.itvideo.model.utils.DateTimeConvertor;


@Component
public class VideoDao {
	private Connection con;
	
	@Autowired
    private void initField() {
		 con = DBConnection.VIDEOS.getConnection();
    }

	@Autowired
	CommentDao cd;
	
	@Autowired
	TagDao td;
	
	/**
	 * Insert new video in database
	 * @param v - video object
	 * @throws SQLException
	 * @throws TagNotFoundException
	 */
	public void createVideo(Video v) throws SQLException, TagNotFoundException {
		con.setAutoCommit(false);
		String sql = "INSERT INTO videos (name, views , date, location_url, user_id, thumbnail_url, description, privacy_id) VALUES(?,?,?,?,?,?,?,?);";
		try (PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);) {
			ps.setString(1, v.getName());
			ps.setInt(2, v.getViews());
			String date = DateTimeConvertor.ldtToSql(v.getDate());
			ps.setString(3, date);
			ps.setString(4, v.getLocationUrl());
			ps.setLong(5, v.getUserId());
			ps.setString(6, v.getThumbnailUrl());
			ps.setString(7, v.getDescription());
			ps.setLong(8, v.getPrivacyId());
			ps.executeUpdate();

			try (ResultSet rs = ps.getGeneratedKeys();) {
				rs.next();
				v.setVideoId(rs.getLong(1));
			}

			td.insertVideoTags(v);
			con.commit();
		} catch (SQLException e) {
			con.rollback();
			throw e;
		} finally {
			con.setAutoCommit(true);
		}
	}

	/**
	 * Delete all the likes of the videos that user has made
	 * @param userId
	 * @throws SQLException
	 */
	public void deleteAllVideoLikes(long userId) throws SQLException {
		String sql = "DELETE FROM video_likes WHERE user_id = ?;";
		try (PreparedStatement ps = con.prepareStatement(sql);) {
			ps.setLong(1, userId);
			ps.executeUpdate();
		}
	}
	
	/**
	 * Delete single video from database
	 * @param videoId
	 * @throws SQLException
	 */
	public void deleteVideo(long videoId) throws SQLException {
		con.setAutoCommit(false);
		try {
			deleteVideoLikes(videoId);
			deleteVideosFromPlaylist(videoId);
			cd.deleteAllCommentsForVideo(videoId);
			deleteVideoTags(videoId);

			String sql = "DELETE FROM videos WHERE video_id = ?;";
			try (PreparedStatement ps = con.prepareStatement(sql);) {
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
		
	/**
	 * Delete video tags without transaction
	 * @param videoId
	 * @throws SQLException
	 */
	private void deleteVideoTags(long videoId) throws SQLException {
		String sql = "DELETE FROM videos_has_tags WHERE video_id = ?;";
		try (PreparedStatement ps = con.prepareStatement(sql);) {
			ps.setLong(1, videoId);
			ps.executeUpdate();
		}
	}
	
	/**
	 * Delete video likes without transaction
	 * @param videoId
	 * @throws SQLException
	 */
	private void deleteVideoLikes(long videoId) throws SQLException {
		String sql = "DELETE FROM video_likes WHERE video_id = ?;";
		try (PreparedStatement ps = con.prepareStatement(sql);) {
			ps.setLong(1, videoId);
			ps.executeUpdate();
		}
	}
	
	public void deleteVideos(long userId) throws SQLException {
		List<Video> videos = getVideos(userId);
		for (Video video : videos) {
			deleteVideo(video.getVideoId());
		}
	}
	
	/**
	 * Delete videos from playlist without transaction
	 * @param videoId
	 * @throws SQLException
	 */
	private void deleteVideosFromPlaylist(long videoId) throws SQLException {
		String sql = "DELETE FROM playlists_has_videos WHERE video_id = ?;";
		try (PreparedStatement ps = con.prepareStatement(sql);) {
			ps.setLong(1, videoId);
			ps.executeUpdate();
		}
	}

	/**
	 * Check if video exists
	 * @param video_id
	 * @return
	 * @throws SQLException
	 */
	public boolean existsVideo(long video_id) throws SQLException {
		String sql = "SELECT * FROM videos WHERE video_id = ?;";
		try (PreparedStatement ps = con.prepareStatement(sql);) {
			ps.setLong(1, video_id);
			try(ResultSet rs = ps.executeQuery();){
				if (rs.next()) {
					return true;
				}
				return false;
			}
		}
	}

	public List<Video> getAllVideoOrderByDate() throws SQLException {
		String sql = "SELECT * FROM videos WHERE privacy_id = 1 ORDER BY date DESC;";
		try (PreparedStatement ps = con.prepareStatement(sql);) {
			try(ResultSet rs = ps.executeQuery();){
				List<Video> videos = new ArrayList<>();
				while (rs.next()) {
					videos.add(
							new Video(
									rs.getLong("video_id"), 
									rs.getString("name"), 
									rs.getInt("views"),
									DateTimeConvertor.sqlToLdt(rs.getString("date")),
									rs.getString("location_url"), 
									rs.getLong("user_id"), 
									rs.getString("thumbnail_url"),
									rs.getString("description"), 
									rs.getLong("privacy_id"), 
									getTags(rs.getLong("video_id"))));
				}
				return videos;
			}
		}
	}
	
	public List<Video> getAllVideoOrderByLikes() throws SQLException {
		String sql = "SELECT v.video_id, v.name, v.views, v.date, v.location_url, v.user_id, v.thumbnail_url, v.description, v.privacy_id, SUM(video_likes.isLike) AS likes FROM videos as v LEFT JOIN video_likes USING (video_id) GROUP BY video_id ORDER BY SUM(video_likes.isLike) DESC;";
		try (PreparedStatement ps = con.prepareStatement(sql);) {
			try (ResultSet rs = ps.executeQuery();) {
				List<Video> videos = new ArrayList<>();
				while (rs.next()) {
					videos.add(
							new Video(
									rs.getLong("video_id"), 
									rs.getString("name"), 
									rs.getInt("views"),
									DateTimeConvertor.sqlToLdt(rs.getString("date")),
									rs.getString("location_url"), 
									rs.getLong("user_id"), 
									rs.getString("thumbnail_url"),
									rs.getString("description"), 
									rs.getLong("privacy_id"), 
									getTags(rs.getLong("video_id"))));
				}
				return videos;
			}
		}
	}

	public List<Video> getAllVideoOrderByViews() throws SQLException {
		String sql = "SELECT * FROM videos WHERE privacy_id = 1 ORDER BY views DESC;";
		try (PreparedStatement ps = con.prepareStatement(sql);) {
			try(ResultSet rs = ps.executeQuery();){
				List<Video> videos = new ArrayList<>();
				while (rs.next()) {
					videos.add(
							new Video(
									rs.getLong("video_id"), 
									rs.getString("name"), 
									rs.getInt("views"),
									DateTimeConvertor.sqlToLdt(rs.getString("date")),
									rs.getString("location_url"), 
									rs.getLong("user_id"), 
									rs.getString("thumbnail_url"),
									rs.getString("description"), 
									rs.getLong("privacy_id"), 
									getTags(rs.getLong("video_id"))));
				}
				return videos;
			}
		}
	}

	/**
	 * Get sum of dislikes
	 * @param videoId
	 * @return
	 * @throws SQLException
	 */
	public int getDisLikes(long videoId) throws SQLException {
		String sql = "SELECT sum(if(isLike = 1,1,0)) as likes_sum FROM video_likes WHERE video_id = ?;";
		try (PreparedStatement ps = con.prepareStatement(sql);) {
			ps.setLong(1, videoId);
			try (ResultSet rs = ps.executeQuery();) {
				if (rs.next()) {
					return rs.getInt("likes_sum");
				} else {
					return 0;
				}	
			}
		}
	}

	/**
	 * Get sum of likes
	 * @param videoId
	 * @return
	 * @throws SQLException
	 */
	public int getLikes(long videoId) throws SQLException {
		String sql = "SELECT sum(if(isLike = 0,1,0)) as dislikes_sum FROM video_likes WHERE video_id = ?;";
		try (PreparedStatement ps = con.prepareStatement(sql);) {
			ps.setLong(1, videoId);
			try (ResultSet rs = ps.executeQuery();) {
				if (rs.next()) {
					return rs.getInt("dislikes_sum");
				} else {
					return 0;
				}	
			}
		}
	}

	public String getlocationUrl(Long videoId) throws SQLException, VideoNotFoundException {
		String sql = "SELECT location_url FROM videos WHERE video_id = ?;";
		try (PreparedStatement ps = con.prepareStatement(sql);) {
			ps.setLong(1, videoId);
			try (ResultSet rs = ps.executeQuery();) {
				if (rs.next()) {
					return rs.getString("location_url");
				} 
				throw new VideoNotFoundException(VideoNotFoundException.NOT_FOUND);
			}
		}
	}

	public String getPrivacy(long privacyId) throws SQLException {
		String sql = "SELECT name FROM privacy_settings WHERE privacy_id = ?;";
		try(PreparedStatement ps = con.prepareStatement(sql);){
			ps.setLong(1, privacyId);
			try(ResultSet rs = ps.executeQuery();){
				if (rs.next()) {
					return rs.getString("name");
				}else {
					return "";
				}
			}
		}
	}
	
	public List<Video> getPublicVideos(long user_id) throws SQLException {
		String sql = "SELECT * FROM videos WHERE privacy_id = 1 AND user_id = ?;";
		List<Video> allUserVideos = new ArrayList<>();
		try (PreparedStatement ps = con.prepareStatement(sql);) {
			ps.setLong(1, user_id);
			try (ResultSet rs = ps.executeQuery();){
				while (rs.next()) {
					allUserVideos.add(
							new Video(
									rs.getLong("video_id"), 
									rs.getString("name"), 
									rs.getInt("views"),
									DateTimeConvertor.sqlToLdt(rs.getString("date")),
									rs.getString("location_url"), 
									rs.getLong("user_id"), 
									rs.getString("thumbnail_url"),
									rs.getString("description"), 
									rs.getLong("privacy_id"), 
									getTags(rs.getLong("video_id"))));
				}
			}
		}
		return allUserVideos;
	}
	
	/**
	 * Get TOP5 related videos ordered by views
	 * @param videoId
	 * @return
	 * @throws SQLException
	 * @throws VideoNotFoundException
	 */
	public Set<Video> getRelatedVideos(long videoId) throws SQLException, VideoNotFoundException {
		Set<Tag> tags = getTags(videoId);
		Set<Video> relatedVideos = new HashSet<>();
		
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT v.* FROM videos as v JOIN videos_has_tags AS vt ON(v.video_id = vt.video_id) JOIN tags AS t ON (vt.tag_id = t.tag_id) ");
		
		for (int i = 0; i < tags.size(); i++) {
			if (i == 0) {
				sql.append(" WHERE");
			}
			if (i == tags.size()-1) {
				sql.append(" t.tag = ?");
			}else {
				sql.append(" t.tag = ? OR");
			}
		}
		sql.append(" ORDER BY v.views DESC LIMIT 5;");
		
		try (PreparedStatement ps = con.prepareStatement(sql.toString());) {
			Tag[] arr = tags.toArray(new Tag[tags.size()]);
			for (int i = 0; i < arr.length; i++) {
				ps.setString(i+1, arr[i].getTag());
			}
			
			System.out.println(ps.toString());
			
			try (ResultSet rs = ps.executeQuery();) {
				while (rs.next()) {
					Video video = new Video(
							rs.getLong("video_id"), 
							rs.getString("name"), 
							rs.getInt("views"),
							DateTimeConvertor.sqlToLdt(rs.getString("date")), 
							rs.getString("location_url"),
							rs.getLong("user_id"), 
							rs.getString("thumbnail_url"), 
							rs.getString("description"),
							rs.getLong("privacy_id"), 
							getTags(rs.getLong("video_id")));
					relatedVideos.add(video);
				}
			}
		}
		return relatedVideos;
	}
	
	private Set<Tag> getTags(long videoId) throws SQLException {
		Set<Tag> tags = new HashSet<>();
		String sql = "SELECT t.tag FROM videos_has_tags as vt JOIN tags as t USING (tag_id) JOIN videos as v ON (vt.video_id = v.video_id) WHERE v.video_id = ? ;";
		try (PreparedStatement ps_tags = con.prepareStatement(sql);) {
			ps_tags.setLong(1, videoId);
			try (ResultSet rs = ps_tags.executeQuery();) {
				while (rs.next()) {
					tags.add(new Tag(rs.getString("tag")));
				}
				return tags;
			}
		}
	}

	public String getThumbnailUrl(long videoId) throws SQLException, VideoNotFoundException {
		String sql = "SELECT thumbnail_url FROM videos WHERE video_id = ?;";
		try (PreparedStatement ps = con.prepareStatement(sql);) {
			ps.setLong(1, videoId);
			try (ResultSet rs = ps.executeQuery();) {
				if (rs.next()) {
					return rs.getString("thumbnail_url");
				} 
				throw new VideoNotFoundException(VideoNotFoundException.NOT_FOUND);
			}
		}
	}

	public long getUserId(long videoId) throws SQLException, VideoNotFoundException {
		String sql = "SELECT user_id FROM videos WHERE video_id = ?;";
		try (PreparedStatement ps = con.prepareStatement(sql);) {
			ps.setLong(1, videoId);
			try (ResultSet rs = ps.executeQuery();) {
				if (rs.next()) {
					return rs.getLong("user_id");
				} 
				throw new VideoNotFoundException(VideoNotFoundException.NOT_FOUND);
			}
		}
	}

	public String getUserName(long userId) throws SQLException, UserNotFoundException {
		String sql = "SELECT username FROM youtubedb.users WHERE user_id = ?;";
		try(PreparedStatement ps = con.prepareStatement(sql);){
			ps.setLong(1, userId);
			try(ResultSet rs = ps.executeQuery();){
				if (rs.next()) {
					return rs.getString("username");
				}else {
					throw new UserNotFoundException(UserNotFoundException.USER_NOT_FOUND);
				}
			}			
		}
	}

	public Video getVideo(long videoId) throws VideoNotFoundException, SQLException {
		Video video = null;
		String sql = "SELECT * FROM videos WHERE video_id = ?;";
		try (PreparedStatement ps = con.prepareStatement(sql);) {
			ps.setLong(1, videoId);
			try (ResultSet rs = ps.executeQuery();) {
				if (rs.next()) {
					video = new Video(
							rs.getLong("video_id"), 
							rs.getString("name"), 
							rs.getInt("views"),
							DateTimeConvertor.sqlToLdt(rs.getString("date")), 
							rs.getString("location_url"),
							rs.getLong("user_id"), 
							rs.getString("thumbnail_url"), 
							rs.getString("description"),
							rs.getLong("privacy_id"), 
							getTags(videoId));
				} 
			}
			
			if (video == null) {
				throw new VideoNotFoundException(VideoException.NOT_FOUND);
			} else {
				return video;
			}
		} catch (SQLException e ) {
			throw e;
		} catch (VideoNotFoundException e) {
			throw e;
		} 
	}

	public Video getVideoForPlayer(long videoId, long userId) throws SQLException, VideoNotFoundException {
		//TODO: Cool sql query
		String sql = "SELECT v.*, SUM(IF(vl.isLike = 1, 1, 0)) AS likes, SUM(IF(vl.isLike = 0, 1, 0)) AS dislikes, SUM(IF(vl.user_id = ?, IF(vl.isLike = 1, 1, -1), 0)) AS current_user_vote, u.username AS video_owner_username FROM videos AS v LEFT JOIN video_likes AS vl ON (v.video_id = vl.video_id) JOIN users AS u ON (u.user_id = v.user_id) WHERE v.video_id = ? GROUP BY (v.video_id);";
		Video video = null;
		try (PreparedStatement ps = con.prepareStatement(sql);) {
			ps.setLong(1, userId);
			ps.setLong(2, videoId);
			try (ResultSet rs = ps.executeQuery();){
				if (rs.next()) {
					video = new Video(
							rs.getLong("video_id"), 
							rs.getString("name"), 
							rs.getInt("views"),
							DateTimeConvertor.sqlToLdt(rs.getString("date")), 
							rs.getString("location_url"),
							rs.getLong("user_id"), 
							rs.getString("thumbnail_url"), 
							rs.getString("description"),
							rs.getLong("privacy_id"), 
							getTags(rs.getLong("video_id")));
					video.setLikes(rs.getInt("likes"));
					video.setDislikes(rs.getInt("dislikes"));
					video.setUserName(rs.getString("video_owner_username"));
					video.setVote(rs.getInt("current_user_vote"));
				} else {
					throw new VideoNotFoundException(VideoNotFoundException.NOT_FOUND);
				}
			}
		}
		return video;
	}

	public List<Video> getVideos(long user_id) throws SQLException {
		String sql = "Select * FROM videos WHERE user_id = ?;";
		List<Video> allUserVideos = new ArrayList<>();
		try (PreparedStatement ps = con.prepareStatement(sql);) {
			ps.setLong(1, user_id);
			try (ResultSet rs = ps.executeQuery();) {
				while (rs.next()) {
					Set<Tag> tags = new HashSet<>();
					String getTags = "SELECT tags.tag FROM videos_has_tags JOIN tags USING (tag_id) WHERE videos_has_tags.video_id = ? ;";
					try(PreparedStatement ps_tags = con.prepareStatement(getTags);){
						ps_tags.setLong(1, rs.getLong("video_id"));
						try (ResultSet rs1 = ps_tags.executeQuery();) {
							while (rs1.next()) {
								tags.add(new Tag(rs1.getString("tag")));
							}		
						}
					}
					allUserVideos.add(
							new Video(
									rs.getLong("video_id"), 
									rs.getString("name"), 
									rs.getInt("views"),
									DateTimeConvertor.sqlToLdt(rs.getString("date")),
									rs.getString("location_url"), 
									rs.getLong("user_id"), 
									rs.getString("thumbnail_url"),
									rs.getString("description"), 
									rs.getLong("privacy_id"), 
									tags));
				}				
			}
		}
		return allUserVideos;
	}

	public List<Video> getVideos(String tag) throws SQLException {
		String sql = "SELECT v.* FROM videos AS v JOIN videos_has_tags AS vt ON (v.video_id = vt.video_id) JOIN tags AS t ON (vt.tag_id = t.tag_id) WHERE t.tag = ?;";
		try (PreparedStatement ps = con.prepareStatement(sql);) {
			ps.setString(1, tag);
			try(ResultSet rs = ps.executeQuery();){
				List<Video> videos = new ArrayList<>();
				while (rs.next()) {
					videos.add(
							new Video(
									rs.getLong("video_id"), 
									rs.getString("name"), 
									rs.getInt("views"),
									DateTimeConvertor.sqlToLdt(rs.getString("date")),
									rs.getString("location_url"), 
									rs.getLong("user_id"), 
									rs.getString("thumbnail_url"),
									rs.getString("description"), 
									rs.getLong("privacy_id"), 
									getTags(rs.getLong("video_id"))));
				}
				return videos;
			}
		}
	}

	public void increaseViews(long videoId) throws SQLException {
		String sql = "UPDATE videos SET views = views + 1 WHERE video_id = ?;";
		try (PreparedStatement ps = con.prepareStatement(sql);) {
			ps.setLong(1, videoId);
			ps.executeUpdate();
		}
	}

	public void like(long videoId, long userId, int like) throws SQLException {
		String sql = "SELECT isLike FROM video_likes WHERE video_id = ? AND user_id = ?;";
		try (PreparedStatement ps = con.prepareStatement(sql)) {
			ps.setLong(1, videoId);
			ps.setLong(2, userId);
			try (ResultSet rs = ps.executeQuery()) {
				if (!rs.next()) {
					sql = "INSERT INTO video_likes (video_id,user_id,isLike) VALUES (?,?,?)";
					try (PreparedStatement ps1 = con.prepareStatement(sql)) {
						ps1.setLong(1, videoId);
						ps1.setLong(2, userId);
						ps1.setInt(3, like);
						ps1.executeUpdate();
					}
				} else {
					if (rs.getInt(1) == like) {
						sql = "DELETE FROM video_likes WHERE video_id = ? AND user_id = ?";
						try (PreparedStatement ps1 = con.prepareStatement(sql)) {
							ps1.setLong(1, videoId);
							ps1.setLong(2, userId);
							ps1.executeUpdate();
						}
					} else {
						sql = "UPDATE video_likes SET isLike = ? WHERE video_id = ? AND user_id = ?";
						try (PreparedStatement ps1 = con.prepareStatement(sql)) {
							ps1.setInt(1, like);
							ps1.setLong(2, videoId);
							ps1.setLong(3, userId);
							ps1.executeUpdate();
						}
					}
				}
			}
		}
	}
	
	public List<Video> searchVideo(String name) throws SQLException, VideoException {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT * FROM videos WHERE privacy_id = 1");
		
		String[] words = name.split("\\s+");
		
		for (int i = 0; i < words.length; i++) {
			sql.append(" AND name LIKE ?");
		}
		
		try (PreparedStatement ps = con.prepareStatement(sql.toString());) {
			for (int i = 0; i < words.length; i++) {
				ps.setString(i+1, "%" + words[i] + "%");
			}
			
			try (ResultSet rs = ps.executeQuery();) {
				List<Video> videos = new ArrayList<>();
				while (rs.next()) {
					videos.add(
							new Video(
									rs.getLong("video_id"), 
									rs.getString("name"), 
									rs.getInt("views"),
									DateTimeConvertor.sqlToLdt(rs.getString("date")), 
									rs.getString("location_url"),
									rs.getLong("user_id"), 
									rs.getString("thumbnail_url"), 
									rs.getString("description"),
									rs.getLong("privacy_id"), 
									getTags(rs.getLong("video_id"))));
				}
				return videos;
			}
		}
	}

	public void updateVideo(Video v) throws SQLException {
		String sql = "UPDATE videos SET name = ?, description = ?, privacy_id = ? WHERE video_id = ?;";
		try (PreparedStatement ps = con.prepareStatement(sql);){
			ps.setString(1, v.getName());
			ps.setString(2, v.getDescription());
			ps.setLong(3, v.getPrivacyId());
			ps.setLong(4, v.getVideoId());
			ps.executeUpdate();
		}
	}
	

	//TODO test pagination
	public List<Video> getVideosByPage(int pageid, int total) throws SQLException {
		String sql = "SELECT * FROM videos LIMIT " + (pageid - 1) + "," + total;
		List<Video> page = new ArrayList<>();
		try (PreparedStatement ps = con.prepareStatement(sql);){
			try (ResultSet rs = ps.executeQuery();) {
				while (rs.next()) {
					page.add(
						new Video(
							rs.getLong("video_id"), 
							rs.getString("name"), 
							rs.getInt("views"),
							DateTimeConvertor.sqlToLdt(rs.getString("date")), 
							rs.getString("location_url"),
							rs.getLong("user_id"), 
							rs.getString("thumbnail_url"), 
							rs.getString("description"),
							rs.getLong("privacy_id"), 
							getTags(rs.getLong("video_id"))));
				}
			}
			
		}
		return page;
	}
	public int getAllVideos() throws SQLException {
		String sql = "SELECT count(*) as total FROM videos;";
		try (PreparedStatement ps = con.prepareStatement(sql);){
			try (ResultSet rs = ps.executeQuery();) {
				if (rs.next()) {
					return rs.getInt("total");
				}else {
					return 0;
				}
			}
			
		}
	}
}
