package com.itvideo.controllers;

import java.sql.SQLException;

import javax.servlet.ServletContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.itvideo.model.CommentDao;
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
	
	@RequestMapping(value="/alacalcac", method=RequestMethod.GET)
	public String loadComments(Model m) {
		return "home";
	}

	public void loadCommentsForVideo(Model model, long videoId) {
		// TODO Auto-generated method stub
		
	}
	
	
}
