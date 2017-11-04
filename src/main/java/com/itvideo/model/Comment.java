package com.itvideo.model;

import java.time.LocalDateTime;

import com.itvideo.model.exceptions.comments.CommentException;
import com.itvideo.model.utils.Validator;

public class Comment {
	private long commentId;
	private String text;
	private LocalDateTime date;
	private long userId;
	private long videoId;
	private long replyId;
	// likes/dislikes
	private long likes;
	private long dislikes;
	// userInfo
	private String username;
	private String url;
	// myVoteinfo
	private int vote;
	//number replies
	private long numberReplies;
 
	Comment(long commentId, String text, LocalDateTime date, long userId, long videoId, Long replyId) {
		this.commentId = commentId;
		this.text = text;
		this.date = date;
		this.userId = userId;
		this.videoId = videoId;
		this.replyId = replyId;
	}

	/**
	 * register new comment use repltId=0 to set no reply
	 * @throws CommentException
	 */
	public Comment(String text, LocalDateTime date, long userId, long videoId, long replyId) throws CommentException {
		setText(text);
		setDate(date);
		setUserId(userId);
		setVideoId(videoId);
		setReplyId(replyId);
	}

	public long getCommentId() {
		return commentId;
	}

	public String getText() {
		return text;
	}

	public LocalDateTime getDate() {
		return date;
	}

	public long getUserId() {
		return userId;
	}

	public long getVideoId() {
		return videoId;
	}

	public long getReplyId() {
		return replyId;
	}

	void setId(long id){
		this.commentId = id;
	}

	void setDate(LocalDateTime date){
		this.date = date;
	}

	void setText(String text) throws CommentException {
		if (Validator.isEmpty(text)) {
			throw new CommentException(CommentException.INVALID_TEXT);
		}
		this.text = text;
	}

	void setUserId(long userId) {
		this.userId = userId;
	}

	void setVideoId(long videoId) {
		this.videoId = videoId;
	}

	public void setReplyId(long replayId) {
		this.replyId = replayId;
	}

	public void setLikes(long likes) {
		this.likes = likes;
	}

	public void setDislikes(long dislikes) {
		this.dislikes = dislikes;
	}

	public long getDislikes() {
		return dislikes;
	}

	public long getLikes() {
		return likes;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public int getVote() {
		return vote;
	}

	public void setVote(int vote) {
		this.vote = vote;
	}
	
	public long getNumberReplies() {
		return numberReplies;
	}
	
	public void setNumberReplies(long numberReplies) {
		this.numberReplies = numberReplies;
	}
}
