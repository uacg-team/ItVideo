package com.itvideo.model.exceptions.playlists;

@SuppressWarnings("serial")
public class PlaylistException extends Exception {
	public static final String INVALID_ID = "playlist id is wrong!";
	public static final String INVALID_USER_ID = "User id is wrong!";
	public static final String INVALID_NAME = "date for comment is wrong!";
	public static final String CANT_UPDATE = "not updated playlist!";
	public static final String MISSING_ID = "playlist have not id";
	public static final String INVALID_NAME_LENGTH_MIN = "name of playlist is too short!";
	public static final String INVALID_NAME_LENGTH_MAX = "name of playlist is too long!";
	public static final String INVALID_NAME_SYMBOLS = "dont use symbols: ; # % | \\ \" < or >";
	public static final String VIDEO_ALREADY_EXIST = "video exist in this playlist!";
	public static final String PLAYLIST_ALREADY_EXISTS = "playlist name already exist!";
	public static final String VIDEOS_NOT_LOADED = "videos not loaded";
	public static final String CANT_CREATE = "Cant create list!";
	public static final String PLAYLIST_NOT_FOUND = "Playlist not found!";
	public PlaylistException(String message) {
		super(message);
	}
	public PlaylistException() {
	}
}
