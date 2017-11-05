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

import com.itvideo.model.exceptions.playlists.PlaylistException;
import com.itvideo.model.exceptions.playlists.PlaylistNotFoundException;
import com.itvideo.model.exceptions.user.UserException;
import com.itvideo.model.utils.DBConnection;
import com.itvideo.model.utils.DateTimeConvertor;

@Component
public class PlaylistDao {
	private Connection con;
	@Autowired
	private VideoDao videoDao;
	@Autowired
	private void initCon() {
		con = DBConnection.PLAYLISTS.getConnection();
	}
	/**
	 * 
	 * @param playlist
	 * @throws PlaylistException
	 * @throws SQLException
	 * @throws UserException
	 */
	public void createPlaylist(Playlist playlist) throws PlaylistException, SQLException, UserException {
		if (playlist.getUserId() == 0) {
			throw new PlaylistException(UserException.INVALID_ID);
		}
		try {
			getPlaylist(playlist.getUserId(), playlist.getPlaylistName());
		}catch (PlaylistNotFoundException e) {
			String sql = "INSERT INTO playlists (user_id,playlist_name) VALUES (?,?);";
			try (PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
				ps.setLong(1, playlist.getUserId());
				ps.setString(2, playlist.getPlaylistName());
				ps.executeUpdate();
				try (ResultSet rs = ps.getGeneratedKeys()) {
					rs.next();
					playlist.setId(rs.getLong(1));
				}
			}
			return;
		}
		throw new PlaylistException(PlaylistException.PLAYLIST_ALREADY_EXISTS);
	}
	/**
	 * @param playlist
	 * @throws PlaylistException
	 * @throws SQLException
	 * @throws UserException
	 */
	public void updatePlaylist(Playlist playlist) throws PlaylistException, SQLException, UserException {
		if (playlist.getUserId() == 0) {
			throw new PlaylistException(UserException.INVALID_ID);
		}
		try {
			getPlaylist(playlist.getUserId(), playlist.getPlaylistName());
		}catch (PlaylistNotFoundException e) {
			String sql = "UPDATE playlists SET playlist_name=?,user_id=? WHERE playlist_id=?;";
			try (PreparedStatement ps = con.prepareStatement(sql)) {
				ps.setString(1, playlist.getPlaylistName());
				ps.setLong(2, playlist.getUserId());
				ps.setLong(3, playlist.getPlaylistId());
				ps.executeUpdate();
			}
			return;
		}
		throw new PlaylistException(PlaylistException.PLAYLIST_ALREADY_EXISTS);
	}
	/**
	 * @param playlistId
	 * @throws PlaylistException
	 * @throws SQLException
	 */
	public void deletePlaylist(long playlistId) throws PlaylistException, SQLException {
		try {
			con.setAutoCommit(false);
			// delete videos in playlist
			deleteVideosInPlaylist(playlistId);
			// delete playlist
			String sql = "DELETE FROM playlists WHERE playlist_id=?";
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
	 * @return
	 * @throws UserException
	 * @throws SQLException
	 */
	public List<Playlist> getPlaylistForUser(long userId) throws UserException, SQLException {
		List<Playlist> playlists = new ArrayList<>();
		String sql = "SELECT * FROM playlists WHERE user_id=?;";
		try (PreparedStatement ps = con.prepareStatement(sql)) {
			ps.setLong(1, userId);
			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					Playlist playlist = new Playlist(rs.getLong("playlist_id"),  rs.getString("playlist_name"), userId);
					playlists.add(playlist);
				}
				return playlists;
			}
		}
	}
	/**
	 * @param userId
	 * @param videoId
	 * @return
	 * @throws UserException
	 * @throws SQLException
	 */
	public List<Playlist> getPlaylistForUserWithStatus(long userId,long videoId) throws UserException, SQLException {
		List<Playlist> playlists = new ArrayList<>();
		String sql = "SELECT p.*,SUM(if(v.video_id = ?, 1, 0)) AS video_status "
				+ "FROM playlists AS p LEFT JOIN playlists_has_videos v "
				+ "ON(p.playlist_id=v.playlist_id)  WHERE user_id=? GROUP BY p.playlist_id;";
		try (PreparedStatement ps = con.prepareStatement(sql)) {
			ps.setLong(1, videoId);
			ps.setLong(2, userId);
			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					Playlist playlist = new Playlist(rs.getLong("playlist_id"), rs.getString("playlist_name"), userId);
					playlist.setVideoStatus(rs.getInt("video_status"));
					playlists.add(playlist);
				}
				return playlists;
			}
		}
	}
	/**
	 * @param userId
	 * @param playlistName
	 * @return
	 * @throws PlaylistException
	 * @throws UserException
	 * @throws SQLException
	 */
	public Playlist getPlaylist(long userId, String playlistName) throws PlaylistException, UserException, SQLException{
		String sql="SELECT p.* FROM playlists AS p INNER JOIN users AS u ON(p.user_id=u.user_id) WHERE u.user_id=? AND playlist_name LIKE ?;";
		try (PreparedStatement ps = con.prepareStatement(sql)) {
			ps.setLong(1, userId);
			ps.setString(2, playlistName);
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					return new Playlist(rs.getLong("playlist_id"), rs.getString("playlist_name"), userId);
				}
				throw new PlaylistNotFoundException();
			}
		}
	}
	/**
	 * @param playlist
	 * @throws PlaylistException-if empty playlist 
	 * @throws SQLException
	 */
	public void loadVideosInPlaylist(Playlist playlist) throws PlaylistException, SQLException {
		if (playlist.getPlaylistId() == 0) {
			throw new PlaylistException(PlaylistException.INVALID_ID);
		}
		String sql = "SELECT v.* FROM playlists_has_videos AS pv INNER JOIN videos AS v ON (pv.video_id=v.video_id) WHERE pv.playlist_id=?;";
		List<Video> videos = new ArrayList<>();
		try (PreparedStatement ps = con.prepareStatement(sql)) {
			ps.setLong(1, playlist.getPlaylistId());
			try (ResultSet rs = ps.executeQuery()) {
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
									videoDao.getTags(rs.getLong("video_id"))));
				}
				playlist.setVideos(videos);
			}
		}
	}
	//used in transaction
	private void deleteVideosInPlaylist(long playlistId) throws SQLException {
		String sql = "DELETE FROM playlists_has_videos WHERE playlist_id = ?;";
		try (PreparedStatement ps = con.prepareStatement(sql)) {
			ps.setLong(1, playlistId);
			ps.executeUpdate();
		}
	}
	/**
	 * Add or remove video from playlist
	 * @param playlistId
	 * @param videoId
	 * @throws SQLException
	 */
	public void addVideo(long playlistId, long videoId) throws SQLException {
		String init = "SELECT * FROM playlists_has_videos WHERE playlist_id=? AND video_id=?";
		String insert = "INSERT INTO playlists_has_videos (playlist_id, video_id) VALUES (?,?)";
		String delete = "DELETE FROM playlists_has_videos WHERE playlist_id=? AND video_id=?";
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
	/**
	 * @param searchPlaylistName
	 * @return list 
	 * @throws SQLException
	 */
	public List<Playlist> searchPlaylist(String searchPlaylistName) throws SQLException {
		List<Playlist> playlist = new ArrayList<>();
		String sql = "SELECT * FROM playlists WHERE playlist_name like ?";
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
	/**
	 * Use in transaction
	 * @param userId
	 * @param con
	 * @throws SQLException
	 */
	public void deletePlaylistsForUser(long userId,Connection con) throws SQLException {
		String sql1 = "DELETE pv.* FROM playlists_has_videos AS pv "
				+ "INNER JOIN playlists AS p ON (pv.playlist_id=p.playlist_id) "
				+ "INNER JOIN users AS u ON (p.user_id=u.user_id) "
				+ "WHERE u.user_id=?;";
		String sql2 = "DELETE FROM playlists WHERE user_id=?";
		try (PreparedStatement ps = con.prepareStatement(sql1)) {
			ps.setLong(1, userId);
			ps.executeUpdate();
		}
		try (PreparedStatement ps = con.prepareStatement(sql2)) {
			ps.setLong(1, userId);
			ps.executeUpdate();
		}
	}
}
