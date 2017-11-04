package com.itvideo.model;

import java.time.LocalDateTime;
import java.util.List;

import com.itvideo.model.exceptions.comments.CommentException;

public class Comment {
	private long commentId;
	private String text;
	private LocalDateTime date;
	private long userId;
	private long videoId;
	private long replyId;

	// replies
	@Deprecated
	private List<Comment> replies;
	private boolean hasReplies;

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
	/**
	 * get all fields default, use only by CommentDAO
	 */
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
	 * 
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

	public void setId(long id) throws CommentException {
		if (id < 1) {
			throw new CommentException(CommentException.INVALID_ID);
		}
		this.commentId = id;
	}

	public void setDate(LocalDateTime date) throws CommentException {
		if (date == null) {
			throw new CommentException(CommentException.INVALID_DATE);
		}
		this.date = date;
	}

	public void setText(String text) throws CommentException {
		if (text == null) {
			throw new CommentException(CommentException.INVALID_TEXT);
		}
		this.text = text;
	}

	public void setUserId(long userId) {
		// user_id validate in other place
		this.userId = userId;
	}

	public void setVideoId(long videoId) {
		// video_id validate in other place
		this.videoId = videoId;
	}

	public void setReplyId(long replayId) {
		this.replyId = replayId;
	}

	public void addReplies(List<Comment> allReplies) {
		this.replies = allReplies;
		this.hasReplies = true;
	}
	@Deprecated
	public List<Comment> getReplies() {
		return replies;
	}

	public boolean getHasReplies() {
		return hasReplies;
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
