package com.itvideo.model.exceptions.video;

@SuppressWarnings("serial")
public class VideoException extends Exception {

	public static final String INVALID_DESCRIPTION = "Invalid video description";
	public static final String INVALID_PRIVACY = "Invalid privacy settings";
	public static final String INVALID_LOCATION = "Invalid location URL";
	public static final String INVALID_THUMBNAIL = "Invalid thumbnail";
	public static final String NOT_FOUND = "Video not found";
	public static final String INVALID_ID = "Invalid user id";
	public static final String INVALID_NAME = "Invalid name";
	public static final String INVALID_NAME_LENGTH = "Invalid name length";
	
	
	public VideoException(String message) {
		super(message);
	}
}
