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

@Controller
@RestController
public class CommentsService {
	@Autowired
	CommentDao comment;

	/**
	 * @param compare-String coming from jsp
	 * @return comparator for comments, default comparator is <b>DESC_BY_DATE<b>
	 */
	private Comparator<Comment> getComparator(String compare) {
		if (compare == null || compare.isEmpty()) {
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

	@ResponseBody
	@RequestMapping(value = "player/getCommentsWithVotes/{videoId}/{myUserId}/{compare}/{part}/{date}", method = RequestMethod.GET)
	public List<Comment> getCommentsWithVotesForVideo(
			@PathVariable Long videoId, 
			@PathVariable Long myUserId,
			@PathVariable String compare, 
			@PathVariable Integer part, 
			@PathVariable String date,
			HttpServletResponse resp) {
		String comparator = null;
		if (compare == null || compare.isEmpty()) {
			comparator ="date desc";
		}
		switch (compare) {
		case "newest":
			comparator ="date desc";
			break;
		case "oldest":
			comparator ="date asc";
			break;
		case "mostLiked":
			comparator ="likes desc";
			break;
		case "mostDisliked":
			comparator ="dislikes desc";
			break;
		default:
			comparator ="date desc";
			break;
		}
		List<Comment> comments = null;
		try {
			comments = comment.getAllCommentsWithVotesByVideoWithoutReplies(part,videoId, myUserId, comparator,date);
		} catch (SQLException e) {
			resp.setStatus(500);
			e.printStackTrace();
		}
		return comments;
	}

	@ResponseBody
	@RequestMapping(value = "player/getRepliesWithVotes/{commentId}/{myUserId}/{compare}", method = RequestMethod.GET)
	public List<Comment> getRepliesWithVotesForComment(
			@PathVariable Long commentId, 
			@PathVariable Long myUserId,
			@PathVariable String compare,
			HttpServletResponse resp) {
		Comparator<Comment> comparator = getComparator(compare);
		List<Comment> comments = null;
		try {
			comments = comment.getAllRepliesWithVotesForComment(commentId, myUserId, comparator);
		} catch (SQLException e) {
			resp.setStatus(500);
			e.printStackTrace();
		}
		return comments;
	}

	@ResponseBody
	@RequestMapping(value = "/player/addComment", method = RequestMethod.POST)
	public Comment addComment(HttpServletRequest req, HttpServletResponse resp) throws CommentException {
		String text = req.getParameter("text");
		Long videoId = Long.parseLong(req.getParameter("videoId"));
		Long userId = Long.parseLong(req.getParameter("myUserId"));
		Long replyId = Long.parseLong(req.getParameter("replyId"));
		Comment newComment = null;
		try {
			newComment = new Comment(text, LocalDateTime.now(), userId, videoId, replyId);
			comment.createComment(newComment);
		} catch (CommentException e) {
			resp.setStatus(405);
			e.printStackTrace();
			return new Comment("error", LocalDateTime.now(), 0, 0, 0);
		} catch (SQLException e) {
			resp.setStatus(500);
			e.printStackTrace();
			return new Comment("error", LocalDateTime.now(), 0, 0, 0);
		}
		System.out.println(newComment.getCommentId());
		return newComment;
	}

	@ResponseBody
	@RequestMapping(value = "player/commentLike", method = RequestMethod.POST)
	public void likeCommentTest(HttpServletRequest req,HttpServletResponse response) {
		Long userId = Long.parseLong(req.getParameter("userId"));
		long commentId = Long.parseLong(req.getParameter("commentId"));

		if (userId == 0) {
			response.setStatus(401);
		} else {
			try {
				int like = Integer.parseInt(req.getParameter("like"));
				// 1 for like -1 for dislike come from js;
				like = like == 1 ? 1 : 0;
				comment.likeComment(commentId, userId, like);
			} catch (SQLException e) {
				e.printStackTrace();
				response.setStatus(500);
			}
		}
	}

	@ResponseBody
	@RequestMapping(value = "player/deleteComment", method = RequestMethod.POST)
	public void deleteComment(HttpServletRequest req,HttpServletResponse resp) {
		long commentId = Long.parseLong(req.getParameter("commentId"));
		try {
			comment.deleteComment(commentId);
		} catch (SQLException e) {
			e.printStackTrace();
			resp.setStatus(500);
		}
	}
}
