package com.itvideo.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.itvideo.model.exceptions.playlists.PlaylistException;
import com.itvideo.model.exceptions.user.UserException;
import com.itvideo.model.utils.DBConnection;
import com.itvideo.model.utils.DateTimeConvertor;

@Component
public class PlaylistDao {
	private Connection con;

	@Autowired
	private void initCon() {
		con = DBConnection.PLAYLISTS.getConnection();
	}

	/**
	 * @param playlist
	 *            must contain user_id, and Playlist name
	 * @throws PlaylistException
	 *             -invalid user id or name
	 * @throws SQLException
	 * @throws UserException
	 */
	public void createPlaylist(Playlist playlist) throws PlaylistException, SQLException, UserException {
		// initial checks
		List<Playlist> userPlayslist = getPlaylistForUser(playlist.getUserId());
		for (Playlist p : userPlayslist) {
			if (p.getPlaylistName().equalsIgnoreCase(playlist.getPlaylistName())) {
				throw new PlaylistException(PlaylistException.PLAYLIST_ALREADY_EXISTS);
			}
		}

		String sql = "insert into playlists (user_id,playlist_name) values (?,?);";
		try (PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
			ps.setLong(1, playlist.getUserId());
			ps.setString(2, playlist.getPlaylistName());
			ps.executeUpdate();
			try (ResultSet rs = ps.getGeneratedKeys()) {
				if (rs.next()) {
					playlist.setId(rs.getLong(1));
				} else {
					throw new PlaylistException(PlaylistException.CANT_CREATE);
				}
			}
		}
	}

	/**
	 * Update only name for playlist
	 * 
	 * @param playlist
	 *            - must contain playlist_id
	 * @throws PlaylistException
	 *             - playlist id is default
	 * @throws SQLException
	 */
	public void updatePlaylist(Playlist playlist) throws PlaylistException, SQLException {
		if (playlist.getUserId() == 0) {
			throw new PlaylistException(PlaylistException.INVALID_ID);
		}
		// update playlist info
		String sql = "update playlists set playlist_name=?,user_id=? where playlist_id=?;";
		try (PreparedStatement ps = con.prepareStatement(sql)) {
			ps.setString(1, playlist.getPlaylistName());
			ps.setLong(2, playlist.getUserId());
			ps.setLong(3, playlist.getPlaylistId());
			ps.executeUpdate();
		}
	}

	/**
	 * @param playlistId
	 * @throws PlaylistException
	 *             - if playlist have no id
	 * @throws SQLException
	 * @throws PlaylistException
	 */
	public void deletePlaylist(long playlistId) throws PlaylistException, SQLException {
		try {
			con.setAutoCommit(false);
			// delete videos in playlist
			deleteVideosInPlaylist(playlistId);
			// delete playlist
			String sql = "delete from playlists where playlist_id=?";
			try (PreparedStatement ps = con.prepareStatement(sql)) {
				ps.setLong(1, playlistId);
				ps.executeUpdate();
			}
			con.commit();
		} catch (SQLException e) {
			con.rollback();
			throw new SQLException(e);
		} finally {
			con.setAutoCommit(true);
		}
	}

	/**
	 * @param userId
	 * @return arraylist with playlists without loaded videos in it
	 * @throws UserException
	 *             if user_id missing
	 * @throws SQLException
	 */
	public List<Playlist> getPlaylistForUser(long userId) throws UserException, SQLException {
		List<Playlist> playlists = new ArrayList<>();
		String sql = "select * from playlists where user_id=?;";
		try (PreparedStatement ps = con.prepareStatement(sql)) {
			ps.setLong(1, userId);
			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					long playlist_id = rs.getLong("playlist_id");
					String playlist_name = rs.getString("playlist_name");
					Playlist playlist = new Playlist(playlist_id, playlist_name, userId);
					playlists.add(playlist);
				}
				return playlists;
			}
		}
	}
	
	public List<Playlist> getPlaylistForUserWithStatus(long userId,long videoId) throws UserException, SQLException {
		List<Playlist> playlists = new ArrayList<>();
		String sql = "select p.*,sum(if(v.video_id = ?, 1, 0)) as video_status "
				+ "from playlists as p left join playlists_has_videos v "
				+ "on (p.playlist_id=v.playlist_id)  where user_id=? group by p.playlist_id;";
		try (PreparedStatement ps = con.prepareStatement(sql)) {
			ps.setLong(1, videoId);
			ps.setLong(2, userId);
			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					long playlist_id = rs.getLong("playlist_id");
					String playlist_name = rs.getString("playlist_name");
					Playlist playlist = new Playlist(playlist_id, playlist_name, userId);
					playlist.setVideoStatus(rs.getInt("video_status"));
					playlists.add(playlist);
				}
				return playlists;
			}
		}
	}

	/**
	 * @param user_id
	 * @param playlistName
	 *            not null and not empty;
	 * @return playlist if exist playlist for user with playlistName ignoreCase
	 *         or null;
	 * @throws UserException
	 *             - if user have no id
	 * @throws SQLException
	 * @throws PlaylistException
	 *             -if playlistName is null or empty
	 */
	public Playlist getPlaylist(long user_id, String playlistName) throws PlaylistException, UserException, SQLException{
		if (playlistName == null || playlistName.isEmpty()) {
			throw new PlaylistException(PlaylistException.INVALID_NAME);
		}
		List<Playlist> playslist = getPlaylistForUser(user_id);
		for (Playlist p : playslist) {
			if (p.getPlaylistName().equalsIgnoreCase(playlistName)) {
				return p;
			}
		}
		throw new PlaylistException(PlaylistException.PLAYLIST_NOT_FOUND);
	}

	/**
	 * @param playlist
	 *            must contain real id,load videos in this playlist
	 * @throws PlaylistException
	 *             - if playlist_id is 0
	 * @throws SQLException
	 */
	public void loadVideosInPlaylist(Playlist playlist) throws PlaylistException, SQLException {
		if (playlist.getPlaylistId() == 0) {
			throw new PlaylistException(PlaylistException.INVALID_ID);
		}
		String sql = "select * from videos as v inner join "
				+ "(select * from playlists_has_videos where playlist_id=?) as p" + " on(v.video_id=p.video_id);";
		List<Video> videos = new ArrayList<>();
		try (PreparedStatement ps = con.prepareStatement(sql)) {
			ps.setLong(1, playlist.getPlaylistId());
			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					long video_id = rs.getLong("video_id");
					String name = rs.getString("name");
					int views = rs.getInt("views");
					LocalDateTime date = DateTimeConvertor.sqlToLdt(rs.getString("date"));
					String location_url = rs.getString("location_url");
					long user_id = rs.getLong("user_id");
					String thumbnail_url = rs.getString("thumbnail_url");
					String description = rs.getString("description");
					long privacy_id = rs.getLong("privacy_id");
					// tags for video, not loaded!
					Video video = new Video(video_id, name, views, date, location_url, user_id, thumbnail_url,
							description, privacy_id, null);
					videos.add(video);
				}
				playlist.setVideos(videos);
			}
		}
	}

	/**
	 * @param playlistId
	 * @throws PlaylistException
	 * @throws SQLException
	 */
	private void deleteVideosInPlaylist(long playlistId) throws PlaylistException, SQLException {
		String sql = "delete from playlists_has_videos where playlist_id = ?;";
		try (PreparedStatement ps = con.prepareStatement(sql)) {
			ps.setLong(1, playlistId);
			ps.executeUpdate();
		}
	}
	/**
	 * add video if not in playlist or remove video if exist
	 * @param playlistId
	 * @param videoId
	 * @throws SQLException
	 */
	public void addVideo(long playlistId, long videoId) throws SQLException {
		//TODO test
		String init = "select * from playlists_has_videos where playlist_id=? and video_id=?";
		String insert = "insert into playlists_has_videos (playlist_id, video_id) values (?,?)";
		String delete = "delete from playlists_has_videos where playlist_id=? and video_id=?";
		boolean exist=false;
		try (PreparedStatement ps = con.prepareStatement(init)) {
			ps.setLong(1, playlistId);
			ps.setLong(2, videoId);
			try(ResultSet rs = ps.executeQuery()){
				if(rs.next()) {
					exist=true;
				}
			}
		}
		if(exist) {
			try (PreparedStatement ps = con.prepareStatement(delete)) {
				ps.setLong(1, playlistId);
				ps.setLong(2, videoId);
				ps.executeUpdate();
			}
		}else {
			try (PreparedStatement ps = con.prepareStatement(insert)) {
				ps.setLong(1, playlistId);
				ps.setLong(2, videoId);
				ps.executeUpdate();
			}
		}
	}

	public void removeVideo(long playlistId, long videoId) throws SQLException {
		String sql = "delete from playlists_has_videos where playlist_id=? and video_id=?";
		try (PreparedStatement ps = con.prepareStatement(sql)) {
			ps.setLong(1, playlistId);
			ps.setLong(2, videoId);
			ps.executeUpdate();
		}
	}

	public List<Playlist> getPlaylists() throws SQLException {
		List<Playlist> playslist = new ArrayList<>();
		String sql = "select * from playlists";
		try (PreparedStatement ps = con.prepareStatement(sql)) {
			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					Playlist p = new Playlist(rs.getLong("playlist_id"), rs.getString("playlist_name"),
							rs.getLong("user_id"));
					playslist.add(p);
				}
			}
		}
		return playslist;
	}

	public List<Playlist> searchPlaylist(String searchPlaylistName) throws SQLException {
		List<Playlist> playlist = new ArrayList<>();
		String sql = "select * from playlists where playlist_name like ?";
		try (PreparedStatement ps = con.prepareStatement(sql)) {
			ps.setString(1, "%" + searchPlaylistName.toLowerCase().trim() + "%");
			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					Playlist p = new Playlist(rs.getLong("playlist_id"), rs.getString("playlist_name"),
							rs.getLong("user_id"));
					playlist.add(p);
				}
			}
		}
		return playlist;
	}
	
	public void deletePlaylistsForUser(long userId) throws SQLException {
		String sql = "delete from playlists where user_id=?";
		try (PreparedStatement ps = con.prepareStatement(sql)) {
			ps.setLong(1, userId);
			ps.executeUpdate();
		}
	}
}
