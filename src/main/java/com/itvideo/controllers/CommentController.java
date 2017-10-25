package com.itvideo.controllers;

import java.sql.SQLException;
import java.util.List;

import javax.servlet.ServletContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.itvideo.model.Comment;
import com.itvideo.model.CommentDao;
import com.itvideo.model.exceptions.comments.CommentException;
import com.itvideo.model.exceptions.video.VideoException;

@Controller
@Component
public class CommentController {
	@Autowired
	CommentDao comment;
	@Autowired
	ServletContext context;
	
	@RequestMapping(value="/index", method=RequestMethod.GET)
	public String test(Model m) {
		try {
			m.addAttribute("comments", comment.getAllComments(1, false));
		} catch (VideoException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "home";
	}
	

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
				//load user info for comment:
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
			//TODO
			e.printStackTrace();
		} catch (SQLException e) {
			//TODO
			e.printStackTrace();
		} catch (CommentException e) {
			//TODO
			e.printStackTrace();
		}

	}
	
	
}
