package com.itvideo.controllers;

import java.sql.SQLException;
import java.util.Comparator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.itvideo.model.Comment;
import com.itvideo.model.CommentDao;
import com.itvideo.model.exceptions.comments.CommentException;
import com.itvideo.model.exceptions.user.UserException;
import com.itvideo.model.exceptions.video.VideoException;

@Controller
@RestController
public class CommentsService {
	@Autowired
	CommentDao comment;

	@RequestMapping(value = "/comment/video{videoId}", method = RequestMethod.GET)
	public List<Comment> getCommentsForVideo(@PathVariable long videoId) {
		List<Comment> comments = null;
		try {
			comments = comment.getAllComments(videoId, false);
			for (Comment c : comments) {
				List<Comment> replies = comment.getAllReplies(c.getCommentId());
				c.addReplies(replies);
				// load likes dislikes for comment:
				c.setLikes(comment.getLikes(c.getCommentId()));
				c.setDislikes(comment.getDislikes(c.getCommentId()));
				// load user info for comment:
				comment.loadUserInfo(c);
				// load likes dislikes for reply, and user info:
				for (Comment reply : replies) {
					reply.setLikes(comment.getLikes(reply.getCommentId()));
					reply.setDislikes(comment.getDislikes(reply.getCommentId()));
					comment.loadUserInfo(reply);
				}
			}
			return comments;
		} catch (VideoException e) {
			// TODO
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO
			e.printStackTrace();
		} catch (CommentException e) {
			// TODO
			e.printStackTrace();
		}
		return null;
	}

	@RequestMapping(value = "/comment/user{userId}", method = RequestMethod.GET)
	public List<Comment> getCommentsForUser(@PathVariable long userId) {
		List<Comment> comments = null;
		try {
			comments = comment.getAllCommentsByUser(userId, false);
			for (Comment c : comments) {
				List<Comment> replies = comment.getAllReplies(c.getCommentId());
				c.addReplies(replies);
				// load likes dislikes for comment:
				c.setLikes(comment.getLikes(c.getCommentId()));
				c.setDislikes(comment.getDislikes(c.getCommentId()));
				// load user info for comment:
				comment.loadUserInfo(c);
				// load likes dislikes for reply, and user info:
				for (Comment reply : replies) {
					reply.setLikes(comment.getLikes(reply.getCommentId()));
					reply.setDislikes(comment.getDislikes(reply.getCommentId()));
					comment.loadUserInfo(reply);
				}
			}
			return comments;
		} catch (SQLException e) {
			// TODO
			e.printStackTrace();
		} catch (CommentException e) {
			// TODO
			e.printStackTrace();
		} catch (UserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	@ResponseBody
	@RequestMapping(value = "player/getCommentsWithVotes/{videoId}/{myUserId}/{compare}/{part}/{countReplies}", method = RequestMethod.GET)
	public List<Comment> getCommentsWithVotesForVideo(@PathVariable Long videoId,@PathVariable Long myUserId,@PathVariable String compare,@PathVariable Integer part,@PathVariable Integer countReplies) {
		//TODO work with part,compare,...
		if(compare==null) {
			compare="";
		}
		Comparator<Comment> comparator = null;
		switch (compare) {
		case "date_asc":
			comparator = CommentDao.ASC_BY_DATE;
			break;
		default:
			comparator = CommentDao.DESC_BY_DATE;
		}
		List<Comment> comments = null;
		try {
			//TODO replace with original getAllCommentsWithVotesByVideo(videoId, myUserId, comparator);
			comments = comment.getAllCommentWithVotesByVideoWithoutReplies(videoId, myUserId, comparator);
		} catch (SQLException e) {
			// TODO handle
			e.printStackTrace();
		}
		return comments;
	}
	//tested
	@ResponseBody
	@RequestMapping(value = "player/getRepliesWithVotes/{commentId}/{myUserId}/{compare}", method = RequestMethod.GET)
	public List<Comment> getRepliesWithVotesForComment(@PathVariable Long commentId,@PathVariable Long myUserId,@PathVariable String compare) {

		if(compare==null) {
			compare="";
		}
		Comparator<Comment> comparator = null;
		switch (compare) {
		case "date_asc":
			comparator = CommentDao.ASC_BY_DATE;
			break;
		default:
			comparator = CommentDao.DESC_BY_DATE;
		}
		List<Comment> comments = null;
		try {
			comments = comment.getAllRepliesWithVotesForComment(commentId, myUserId, comparator);
		} catch (SQLException e) {
			// TODO handle
			e.printStackTrace();
		}
		return comments;
	}
}
