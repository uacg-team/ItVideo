package  com.itvideo.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


import com.itvideo.model.exceptions.playlists.PlaylistException;
import com.itvideo.model.utils.Validator;


public class Playlist {
	public static final int MIN_LENGTH_PLAYLIST_NAME = 3;
	public static final int MAX_LENGTH_PLAYLIST_NAME = 45;
	
	private long playlistId;
	private String playlistName;
	private long userId;
	private boolean isLoaded;

	private List<Video> videos;
	//is video in playlist, when user watch video
	private int videoStatus;
	/**
	 * get from DB , for loading videos use PlaylistDAO -loadVideosInPlaylist
	 */
	Playlist(long playlistId, String playlistName, long user_id) {
		this.playlistId = playlistId;
		this.playlistName = playlistName;
		this.userId = user_id;
		this.videos = new ArrayList<>();
		isLoaded = false;
	}

	/**
	 * @param name
	 *            - playlist name
	 * @param userId
	 * @throws PlaylistException
	 *             -not valid name and user
	 */
	public Playlist(String name, long userId) throws PlaylistException {
		this.playlistId = (long) 0;
		setName(name);
		setUserId(userId);
		this.videos = new ArrayList<>();
		isLoaded = false;
	}

	public long getPlaylistId() {
		return playlistId;
	}

	public String getPlaylistName() {
		return playlistName;
	}

	public long getUserId() {
		return userId;
	}

	void setId(long playlistId) throws PlaylistException {
		this.playlistId = playlistId;
	}

	public void setName(String playlistName) throws PlaylistException {
		if (Validator.isEmpty(playlistName)) {
			throw new PlaylistException(PlaylistException.INVALID_NAME);
		}
		if (Validator.isValidLength(MIN_LENGTH_PLAYLIST_NAME, MAX_LENGTH_PLAYLIST_NAME, playlistName)) {
			throw new PlaylistException(PlaylistException.INVALID_NAME_LENGTH);
		}
		if (Validator.isContaingSpecialChars(playlistName)) {
			throw new PlaylistException(PlaylistException.INVALID_NAME_SYMBOLS);
		}
		this.playlistName = playlistName;
	}

	void setUserId(long userId) throws PlaylistException {
		this.userId = userId;
	}
	public List<Video> getVideos() {
		return Collections.unmodifiableList(videos);
	}

	void setVideos(List<Video> videos) {
		this.videos = videos;
	}

	public boolean isLoaded() {
		return isLoaded;
	}

	void setVideoStatus(int videoStatus) {
		this.videoStatus = videoStatus;
	}
	public int getVideoStatus() {
		return videoStatus;
	}
}
