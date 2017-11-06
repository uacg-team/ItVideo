package com.itvideo.model.exceptions.playlists;

@SuppressWarnings("serial")
public class PlaylistException extends Exception {
	public static final String INVALID_ID = "playlist id is wrong!";
	public static final String INVALID_NAME = "date for comment is wrong!";
	public static final String INVALID_NAME_LENGTH = "invalid length for playlist name!";
	public static final String INVALID_NAME_SYMBOLS = "dont use symbols: ; # % | \\ \" < or >";
	public static final String PLAYLIST_ALREADY_EXISTS = "playlist name already exist!";
	public static final String PLAYLIST_NOT_FOUND = "Playlist not found!";
	public static final String NOT_SUPORTED_OPERATION = "Cant delete this playlist!";
	public PlaylistException(String message) {
		super(message);
	}
	public PlaylistException() {
	}
}
