package com.itvideo.model.exceptions.comments;

/**
 * for not valid Comment
 */
@SuppressWarnings("serial")
public class CommentException extends Exception {
	public static final String INVALID_ID = "comment id is wrong!";
	public static final String INVALID_COMMENT_REPLAY_ID = "comment replay id is wrong!";
	public static final String INVALID_TEXT = "Comment can't contain only whitespaces!";
	public static final String CANT_UPDATE = "not updated comment!";
	public static final String MISSING_ID = "comment have not id";

	public CommentException(String msg) {
		super(msg);
	}

	public CommentException() {
	}
}
