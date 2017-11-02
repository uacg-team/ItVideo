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
	CommentDao cd;
	
	@Autowired
	TagDao td;
	
	@Autowired
    private void initField() {
		 con = DBConnection.VIDEOS.getConnection();
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
	
	public void createVideo(Video v) throws SQLException, TagNotFoundException {
		con.setAutoCommit(false);
		String sql = "INSERT into videos (name, views , date, location_url, user_id, thumbnail_url, description, privacy_id) VALUES(?,?,?,?,?,?,?,?);";
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
	
	public void deleteVideoLikes(long videoId) throws SQLException {
		String sql = "DELETE FROM video_likes WHERE video_id = ?;";
		try (PreparedStatement ps = con.prepareStatement(sql);) {
			ps.setLong(1, videoId);
			ps.executeUpdate();
		}
	}
	
	public void deleteVideosFromPlaylist(long videoId) throws SQLException {
		String sql = "DELETE FROM playlists_has_videos WHERE video_id = ?;";
		try (PreparedStatement ps = con.prepareStatement(sql);) {
			ps.setLong(1, videoId);
			ps.executeUpdate();
		}
	}

	public void deleteVideo(long videoId) throws SQLException {
		try {
			con.setAutoCommit(false);
			deleteVideoLikes(videoId);
			deleteVideosFromPlaylist(videoId);
			//TODO: da pitam veli trqbva li ?
			cd.deleteComments(videoId);
			td.delete(videoId);
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

	public void disLike(long video_id, long user_id) throws SQLException {
		String sql = "SELECT isLike FROM video_likes WHERE video_id = ? AND user_id = ?;";
		try (PreparedStatement ps = con.prepareStatement(sql)) {
			ps.setLong(1, video_id);
			ps.setLong(2, user_id);
			try (ResultSet rs = ps.executeQuery()) {
				if (!rs.next()) {
					sql = "INSERT INTO video_likes (video_id,user_id,isLike) VALUES (?,?,0)";
				} else {
					if (!rs.getBoolean(1)) {
						sql = "DELETE FROM video_likes WHERE video_id = ? AND user_id = ?";
					} else {
						sql = "UPDATE video_likes SET isLike = 0 WHERE video_id = ? AND user_id = ?";
					}
				}
			}
		}
		try (PreparedStatement ps = con.prepareStatement(sql)) {
			ps.setLong(1, video_id);
			ps.setLong(2, user_id);
			ps.executeUpdate();
		}
	}

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
		//TODO: make transaction
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

	public List<Video> getAllVideoOrderByViews() throws SQLException {
		//TODO: make transaction
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
	
	public List<Video> getAllVideoOrderByLikes() throws SQLException {
		//TODO: make transaction
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

	public Video getVideo(long videoId) throws VideoNotFoundException, SQLException {
		con.setAutoCommit(false);
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
			
			con.commit();
			if (video == null) {
				throw new VideoNotFoundException(VideoException.NOT_FOUND);
			} else {
				return video;
			}
		} catch (SQLException e ) {
			con.rollback();
			throw e;
		} catch (VideoNotFoundException e) {
			throw e;
		} 
		finally {
			con.setAutoCommit(true);
		}
	}

	public List<Video> getPublicVideos(long user_id) throws SQLException {
		//TODO: transaction
		String sql = "Select * FROM videos WHERE privacy_id = 1 AND user_id = ?;";
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
					
					
					//TODO: delete
					/*				
					Set<Tag> tags = new HashSet<>();
					String getTags = "SELECT tags.tag FROM videos_has_tags JOIN tags USING (tag_id) WHERE videos_has_tags.video_id = ? ;";
					try (PreparedStatement ps_tags = con.prepareStatement(getTags);){
						ps_tags.setLong(1, rs.getLong("video_id"));
						try (ResultSet rs1 = ps_tags.executeQuery();){
							while (rs1.next()) {
								tags.add(new Tag(rs1.getString("tag")));
							}
							
						}
					}*/
				}
			}
		}
		return allUserVideos;
	}
	
	public void deleteAllVideoLikes(long user_id) {
		//TODO: not impemented
	}
	
	public void deleteVideos(long userId) throws SQLException {
		//TODO: transaction
		List<Video> videos = getVideos(userId);
		for (Video video : videos) {
			deleteVideo(video.getVideoId());
		}
	}
	
	public List<Video> getVideos(long user_id) throws SQLException {
	//TODO: Transaction and try with resources
		String sql = "Select * FROM videos WHERE user_id = ?;";
		List<Video> allUserVideos = new ArrayList<>();
		try (PreparedStatement ps = con.prepareStatement(sql);) {
			ps.setLong(1, user_id);
			try (ResultSet rs = ps.executeQuery();) {
				while (rs.next()) {
					Set<Tag> tags = new HashSet<>();
					String getTags = "SELECT tags.tag FROM videos_has_tags JOIN tags USING (tag_id) WHERE videos_has_tags.video_id = ? ;";
					PreparedStatement ps_tags = con.prepareStatement(getTags);
					ps_tags.setLong(1, rs.getLong("video_id"));
					ResultSet rs1 = ps_tags.executeQuery();
					while (rs1.next()) {
						tags.add(new Tag(rs1.getString("tag")));
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

	public void like(long videoId, long userId) throws SQLException {
		String sql = "SELECT isLike FROM video_likes WHERE video_id = ? AND user_id = ?;";
		try (PreparedStatement ps = con.prepareStatement(sql)) {
			ps.setLong(1, videoId);
			ps.setLong(2, userId);
			try (ResultSet rs = ps.executeQuery()) {
				if (!rs.next()) {
					sql = "INSERT INTO video_likes (video_id,user_id,isLike) VALUES (?,?,1)";
				} else {
					if (rs.getBoolean(1)) {
						sql = "DELETE FROM video_likes WHERE video_id = ? AND user_id = ?";
					} else {
						sql = "UPDATE video_likes SET isLike = 1 WHERE video_id = ? AND user_id = ?";
					}
				}
			}
		}
		try (PreparedStatement ps = con.prepareStatement(sql)) {
			ps.setLong(1, videoId);
			ps.setLong(2, userId);
			ps.executeUpdate();
		}
	}

	public List<Video> searchVideo(String name) throws SQLException, VideoException {
		String sql = "SELECT * FROM videos WHERE name LIKE ? and privacy_id = 1";
		try (PreparedStatement ps = con.prepareStatement(sql);) {
			ps.setString(1, "%" + name + "%");
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

	public Set<Video> getRelatedVideos(long videoId) throws SQLException, VideoNotFoundException {
		//TODO: Make transaction
		Set<Tag> tags = getTags(videoId);
		Set<Video> relatedVideos = new HashSet<>();
		
		for (Tag tag : tags) {
			String sql = "SELECT v.location_url, v.video_id FROM videos as v "
					+ "JOIN videos_has_tags AS vt ON(v.video_id = vt.video_id) "
					+ "JOIN tags AS t ON (vt.tag_id = t.tag_id) "
					+ "WHERE t.tag = ? LIMIT 5;";
			
			try (PreparedStatement ps = con.prepareStatement(sql);) {
				ps.setString(1, tag.getTag());
				try (ResultSet rs = ps.executeQuery();) {
					while (rs.next()) {
						long relVideoId = rs.getLong("video_id");
						if (videoId == relVideoId) {
							continue;
						}
						Video video = getVideo(relVideoId);
						relatedVideos.add(video);
					}
				}
			}
		}
		return relatedVideos;
	}

	public void increaseViews(long videoId) throws SQLException {
		String sql = "UPDATE videos SET views = views + 1 WHERE video_id = ?;";
		try (PreparedStatement ps = con.prepareStatement(sql);) {
			ps.setLong(1, videoId);
			ps.executeUpdate();
		}
	}

	public int getLikes(long videoId) throws SQLException {
		//TODO: change sql query: SELECT sum(if(isLike = 0,1,0)) as dislikes_sum FROM video_likes WHERE video_id = 5;
		String sql = "SELECT isLike FROM video_likes WHERE video_id = ?;";
		int count = 0;
		try (PreparedStatement ps = con.prepareStatement(sql);) {
			ps.setLong(1, videoId);
			try (ResultSet rs = ps.executeQuery();) {
				while (rs.next()) {
					if (rs.getInt("isLike") == 1) {
						count += 1;
					}
				}				
			}
		}
		return count;
	}

	public int getDisLikes(long videoId) throws SQLException {
		//TODO: look getLikes method
		String sql = "SELECT isLike FROM video_likes WHERE video_id = ?;";
		int count = 0;
		try (PreparedStatement ps = con.prepareStatement(sql);) {
			ps.setLong(1, videoId);
			try (ResultSet rs = ps.executeQuery();) {
				while (rs.next()) {
					if (rs.getInt("isLike") == 0) {
						count += 1;
					}
				}
			}
		}
		return count;
	}

	public Video getVideoForPlayer(long videoId, long userId) throws SQLException, VideoNotFoundException {
		//TODO: qkata zaqvka
		String sql = "SELECT v.*, SUM(IF(vl.isLike = 1, 1, 0)) AS likes, SUM(IF(vl.isLike = 0, 1, 0)) AS dislikes, SUM(IF(vl.user_id = ?, IF(vl.isLike = 1, 1, - 1), 0)) AS current_user_vote, u.username AS video_owner_username FROM videos AS v LEFT JOIN video_likes AS vl ON (v.video_id = vl.video_id) JOIN users AS u ON (u.user_id = v.user_id) WHERE v.video_id = ? GROUP BY (v.video_id);";
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
}
