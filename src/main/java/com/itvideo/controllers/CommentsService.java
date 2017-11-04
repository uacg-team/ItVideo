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

@Controller
@RestController
public class CommentsService {
	@Autowired
	CommentDao comment;
	/**
	 * @param compare-String coming from jsp
	 * @return comparator for comments, default comparator is <b>DESC_BY_DATE<b>
	 */
	private Comparator<Comment> getComparator(String compare){
		if(compare == null ||compare.isEmpty()) {
			return CommentDao.DESC_BY_DATE;
		}
		switch (compare) {
		case "newest":
			return CommentDao.DESC_BY_DATE;
		case "oldest":
			return CommentDao.ASC_BY_DATE;
		case "mostLiked":
			return CommentDao.DESC_BY_LIKES;
		case "mostDisliked":
			return CommentDao.DESC_BY_DISLIKES;
		default:
			return CommentDao.DESC_BY_DATE;
		}
	}
	//refactor js parameters
	@ResponseBody
	@RequestMapping(value = "player/getCommentsWithVotes/{videoId}/{myUserId}/{compare}/{part}/{countReplies}", method = RequestMethod.GET)
	public List<Comment> getCommentsWithVotesForVideo(@PathVariable Long videoId,@PathVariable Long myUserId,@PathVariable String compare,@PathVariable Integer part,@PathVariable Integer countReplies) {
		Comparator<Comment> comparator=getComparator(compare);
		List<Comment> comments = null;
		try {
			comments = comment.getAllCommentWithVotesByVideoWithoutReplies(videoId, myUserId, comparator);
		} catch (SQLException e) {
			// TODO handle
			//response.setStatus(500);
			e.printStackTrace();
		}
		return comments;
	}
	
	//tested
	@ResponseBody
	@RequestMapping(value = "player/getRepliesWithVotes/{commentId}/{myUserId}/{compare}", method = RequestMethod.GET)
	public List<Comment> getRepliesWithVotesForComment(@PathVariable Long commentId,@PathVariable Long myUserId,@PathVariable String compare) {
		Comparator<Comment> comparator=getComparator(compare);
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
		} catch (CommentException e) {
			resp.setStatus(401);
			//TODO js catch errors
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			resp.setStatus(401);
		}
		System.out.println(newComment.getCommentId());
		return newComment;
	}
	
	//TODO rename commentLikeTest, like dislike with one function
	@ResponseBody
	@RequestMapping(value = "player/commentLikeTest", method = RequestMethod.POST)
	public void likeCommentTest(HttpServletRequest req) {
		Long userId = Long.parseLong(req.getParameter("userId"));
		long commentId = Long.parseLong(req.getParameter("commentId"));

		if (userId == 0) {
			//TODO status
		} else {
			try {
				int like = Integer.parseInt(req.getParameter("like"));
				// add like or dislike for comment id
				if (like == 1) {
					comment.likeComment(commentId, userId);
				} else if (like == -1) {
					comment.dislikeComment(commentId, userId);
				}
			} catch (SQLException e) {
				// TODO add status code
				e.printStackTrace();
			} catch (CommentException e) {
				// TODO add statusCode
				e.printStackTrace();
			} catch (UserException e) {
				// TODO add statusCode
				e.printStackTrace();
			}
		}
	}
	@ResponseBody
	@RequestMapping(value = "player/deleteComment", method = RequestMethod.POST)
	public void deleteComment(HttpServletRequest req) {
		//TODO validate again userId
		long commentId = Long.parseLong(req.getParameter("commentId"));
		try {
			comment.deleteComment(commentId);
		} catch (SQLException e) {
			// TODO add status code
			e.printStackTrace();
		} catch (CommentException e) {
			// TODO add statusCode
			e.printStackTrace();
		}
	}
}
