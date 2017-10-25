package  com.itvideo.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;


import com.itvideo.model.exceptions.playlists.PlaylistException;
import com.itvideo.model.exceptions.user.UserException;
import com.itvideo.model.exceptions.video.VideoException;
import com.itvideo.model.exceptions.video.VideoNotFoundException;


public class Playlist {
	private static final int MIN_LENGTH_PLAYLIST_NAME = 3;
	private static final int MAX_LENGTH_PLAYLIST_NAME = 45;
	private long playlistId;
	private String playlistName;
	private long userId;
	private boolean isLoaded;

	private List<Video> videos;

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

	public void setId(long playlistId) throws PlaylistException {
		if (playlistId < 1) {
			throw new PlaylistException(PlaylistException.INVALID_ID);
		}
		this.playlistId = playlistId;
	}

	public void setName(String playlistName) throws PlaylistException {
		if (playlistName == null || playlistName.isEmpty()) {
			throw new PlaylistException(PlaylistException.INVALID_NAME);
		}
		if (playlistName.length() < MIN_LENGTH_PLAYLIST_NAME) {
			throw new PlaylistException(PlaylistException.INVALID_NAME_LENGTH_MIN);
		}
		if (playlistName.length() >= MAX_LENGTH_PLAYLIST_NAME) {
			throw new PlaylistException(PlaylistException.INVALID_NAME_LENGTH_MAX);
		}
		if (!Pattern.matches("[^;#/%|\\\\\"<>]+", playlistName)) {
			throw new PlaylistException(PlaylistException.INVALID_NAME_SYMBOLS);
		}
		this.playlistName = playlistName;
	}

	public void setUserId(long userId) throws PlaylistException {
		if (userId < 1) {
			throw new PlaylistException(UserException.INVALID_ID);
		}
		this.userId = userId;
	}

	/**
	 * <b>Always use with PlaylistDAO addVideo.Before use check for videos
	 * loaded in playlist</b>
	 * 
	 * @param video
	 * @throws PlaylistException-VIDEOS_NOT_LOADED,VIDEO_AREADY_EXIST
	 * @throws VideoException-INVALID_ID
	 */
	public void addVideo(Video video) throws PlaylistException, VideoException {
		if (video == null || video.getVideoId() == 0) {
			throw new VideoException(VideoException.INVALID_ID);
		}
		if (!isLoaded) {
			throw new PlaylistException(PlaylistException.VIDEOS_NOT_LOADED);
		}
		if (videos.contains(video)) {
			throw new PlaylistException(PlaylistException.VIDEO_ALREADY_EXIST);
		}
		//TODO add only public video
		videos.add(video);
	}

	/**
	 * <b>Always use with PlaylistDAO addVideo.Before use check for videos
	 * loaded in playlist</b>
	 * 
	 * @param video
	 * @throws VideoException-INVALID_ID,
	 * @throws VideoNotFoundException NOT_FOUND
	 * @throws PlaylistException VIDEOS_NOT_LOADED
	 */
	public void removeVideo(Video video) throws VideoException, VideoNotFoundException, PlaylistException {
		if (video == null || video.getVideoId() == 0) {
			throw new VideoException(VideoException.INVALID_ID);
		}
		if (!isLoaded) {
			throw new PlaylistException(PlaylistException.VIDEOS_NOT_LOADED);
		}
		if (videos.contains(video)) {
			throw new VideoNotFoundException(VideoNotFoundException.NOT_FOUND);
		}
		videos.remove(video);
	}

	/**
	 * @return unmodifiableList(videos)
	 */
	public List<Video> getVideos() {
		return Collections.unmodifiableList(videos);
	}

	void setVideos(List<Video> videos) {
		this.videos = videos;
	}

	public boolean isLoaded() {
		return isLoaded;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof Playlist))
			return false;
		Playlist other = (Playlist) obj;
		if (playlistId != other.playlistId)
			return false;
		return true;
	}

}
