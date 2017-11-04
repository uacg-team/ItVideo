package com.itvideo.model.utils;

import java.util.regex.Pattern;

import com.itvideo.model.Playlist;

public final class Validator {
	private Validator() {};
	
	public static boolean isNull(Object o) {
		return o == null;
	}
	
	public static boolean isEmpty(String string) {
		if(isNull(string)) {
			return true;
		}
		//replace all spaces tabs and enters with "";
		string = string.replaceAll("[\\s]", "");
		return string.isEmpty();
	}
	
	public static boolean isContaingSpecialChars(String string) {
		if (!Pattern.matches("[^;#/%|\\\\<>\"]+", string)) {
			return true;
		}
		return false;
	}
	
	public static boolean isValidLength(int min,int max,String name) {
		min = min >= 0 ? min : 0;
		max = max > min ? max : -1;
		return name.length() >= min && name.length() < max;
	}
	
	public static boolean isValidPlaylistName(String playlistName) {
		if(isEmpty(playlistName)) {
			return false;
		}
		if(isValidLength(Playlist.MIN_LENGTH_PLAYLIST_NAME, Playlist.MAX_LENGTH_PLAYLIST_NAME, playlistName)) {
			return false;
		}
		if(isContaingSpecialChars(playlistName)) {
			return false;
		}
		return true;
	}
	
	public static boolean isValidIndex(long index) {
		return index>0;
	}
}
