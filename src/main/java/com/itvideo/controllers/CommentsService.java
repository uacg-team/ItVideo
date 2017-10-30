package com.itvideo.controllers;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
	
	@Deprecated
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
	@Deprecated
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
		System.out.println(compare);
		if(compare==null) {
			compare="";
		}
		Comparator<Comment> comparator = null;
		switch (compare) {
		case "newest":
			comparator = CommentDao.DESC_BY_DATE;
			break;
		case "oldest":
			comparator = CommentDao.ASC_BY_DATE;
			break;
		case "mostLiked":
			comparator = CommentDao.DESC_BY_LIKES;
			break;
		case "mostDisliked":
			comparator = CommentDao.DESC_BY_DISLIKES;
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
		System.out.println(compare);
		if(compare==null) {
			compare="";
		}
		Comparator<Comment> comparator = null;
		switch (compare) {
		case "latest":
			comparator = CommentDao.ASC_BY_DATE;
			break;
		case "oldest":
			comparator = CommentDao.DESC_BY_DATE;
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
	
	@ResponseBody
	@RequestMapping(value = "/player/addComment", method = RequestMethod.POST)
	public Comment addComment(HttpServletRequest req,HttpServletResponse resp) {
		String text =req.getParameter("text");
		Long videoId = Long.parseLong(req.getParameter("videoId"));
		Long userId = Long.parseLong(req.getParameter("myUserId"));
		Long replyId = Long.parseLong(req.getParameter("replyId"));
		System.out.println(text+videoId+userId);
		Comment newComment=null;
		try {
			newComment = new Comment(text, LocalDateTime.now(), userId, videoId, replyId);
			comment.createComment(newComment);
		} catch (CommentException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(newComment.getCommentId());
		return newComment;
	}

}
