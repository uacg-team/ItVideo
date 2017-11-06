package com.itvideo.model.exceptions.comments;

/**
 * for not valid Comment
 */
@SuppressWarnings("serial")
public class CommentException extends Exception {
	public static final String INVALID_TEXT = "Comment can't contain only whitespaces!";
	public static final String INVALID_TEXT_LENGTH = "Comment can't be more than 1000 symbols!";

	public CommentException(String msg) {
		super(msg);
	}

	public CommentException() {
	}
}
