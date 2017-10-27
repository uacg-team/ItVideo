package com.itvideo.controllers;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.itvideo.model.Comment;
import com.itvideo.model.CommentDao;
import com.itvideo.model.User;
import com.itvideo.model.exceptions.comments.CommentException;
import com.itvideo.model.exceptions.user.UserException;
import com.itvideo.model.exceptions.video.VideoException;

//sinhronni zaqvki
@Controller
@Component
public class CommentController {
	@Autowired
	CommentDao comment;
	@Autowired
	ServletContext context;

	// tested
	public void loadCommentsForVideo(Model model, long videoId) {
		// load all info for video comments
		List<Comment> comments = null;
		int countComments = 0;
		try {
			comments = comment.getAllComments(videoId, false);
			for (Comment c : comments) {
				List<Comment> replies = comment.getAllReplies(c.getCommentId());
				c.addReplies(replies);
				countComments += replies.size() + 1;
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
			model.addAttribute("comments", comments);
			model.addAttribute("countComments", countComments);
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
	}

	@RequestMapping(value = "/addComment", method = RequestMethod.POST)
	public String addComment(HttpSession session, HttpServletRequest req) {
		// add new comment or reply
		long videoId = Long.valueOf(req.getParameter("videoId"));
		User u = ((User) session.getAttribute("user"));
		if (u == null) {
			return "redirect:/login";
		} else {
			String text = req.getParameter("newComment");
			long userId = u.getUserId();
			long reply;
			if (req.getParameter("reply") == null) {
				reply = 0;
			} else {
				reply = Long.valueOf(req.getParameter("reply"));
			}
			try {
				Comment addComment = new Comment(text, LocalDateTime.now(), userId, videoId, reply);
				comment.createComment(addComment);
			} catch (CommentException e) {
				// TODO handle
				e.printStackTrace();
			} catch (SQLException e) {
				// TODO handle
				e.printStackTrace();
			}
			return "redirect:/player/" + videoId;
		}
	}

	// TODO make asinch
	@RequestMapping(value = "/commentLike", method = RequestMethod.POST)
	public String likeComment(HttpSession session, HttpServletRequest req) {
		User u = ((User) session.getAttribute("user"));
		long videoId = Long.parseLong(req.getParameter("videoId"));
		if (u == null) {
			// TODO view from Party,test
			return "forward:/login";
		} else {
			try {
				int like = Integer.parseInt(req.getParameter("like"));
				long commentId = Long.parseLong(req.getParameter("commentId"));
				// add like or dislike for comment id
				if (like == 1) {
					comment.likeComment(commentId, u.getUserId());
				} else if (like == -1) {
					comment.dislikeComment(commentId, u.getUserId());
				}
			} catch (SQLException e) {
				// TODO add status code
				e.printStackTrace();
				return "forward:/player";
			} catch (CommentException e) {
				// TODO add statusCode
				e.printStackTrace();
				return "forward:/player";
			} catch (UserException e) {
				// TODO add statusCode
				e.printStackTrace();
				return "forward:/player";
			}
			return "redirect:/player/" + videoId;
		}
	}
	//user id =0 if no user
	public void loadCommentsForVideoAndUser(Model model, long videoId,long userId,Comparator<Comment> comparator) {
		// load all info for video comments
		List<Comment> comments = null;
		int countComments = 0;
		try {
			//TODO sort by date!
			comments = comment.getAllComments(videoId, false);
			for (Comment c : comments) {
				List<Comment> replies = comment.getAllReplies(c.getCommentId());
				c.addReplies(replies);
				countComments += replies.size() + 1;
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
			model.addAttribute("comments", comments);
			model.addAttribute("countComments", countComments);
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
	}

	@ResponseBody
	@RequestMapping(value = "player/commentLikeTest", method = RequestMethod.POST)
	public void likeCommentTest(HttpServletRequest req) {
		System.out.println(req.getParameter("userId"));
		System.out.println(req.getParameter("commentId"));
		Long userId = Long.parseLong(req.getParameter("userId"));
		long commentId = Long.parseLong(req.getParameter("commentId"));

		if (userId == 0) {
			// status
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
}
