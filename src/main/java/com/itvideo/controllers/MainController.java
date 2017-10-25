package com.itvideo.controllers;

import java.sql.SQLException;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.itvideo.model.User;
import com.itvideo.model.UserDao;
import com.itvideo.model.Video;
import com.itvideo.model.VideoDao;

@Controller
public class MainController {
	@Autowired
	VideoDao vd;
	@Autowired
	UserDao ud;
		
	@RequestMapping(value="/main", method = RequestMethod.GET)
	public String main(Model model) {
		try {
			List<Video> videos = vd.getAllVideoOrderByDate();
			model.addAttribute("videos", videos);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return "main";
	}
	
	@RequestMapping(value="/player/{videoId}", method = RequestMethod.GET)
	public String player(Model model, @PathVariable("videoId") long videoId) {

		vd.increaseViews(videoId);
		Video video = vd.getVideo(videoId);
		User videoOwner =  ud.getUser(video.getUserId());
		int likes = vd.getLikes(videoId);
		int disLikes = vd.getDisLikes(videoId);
		Set<Video> related = vd.getRelatedVideos(videoId);
		
		model.addAttribute("mainVideo", video);
		model.addAttribute("videoOwner", videoOwner);
		model.addAttribute("likes", likes);
		model.addAttribute("disLikes", disLikes);
		model.addAttribute("related", related);
	
		CommentServlet.loadCommentsForVideo(request, video.getVideoId());
		if(request.getSession().getAttribute("user")!=null) {
			User user = (User)request.getSession().getAttribute("user");
			long userId = user.getUserId();
			PlaylistServlet.loadPlaylistForUser(request, userId);
		}
		request.getRequestDispatcher("player.jsp").forward(request, response);
	} catch (VideoNotFoundException e) {
		request.getRequestDispatcher("player.jsp").forward(request, response);
	} catch (SQLException e) {
		request.getRequestDispatcher("player.jsp").forward(request, response);
	} catch (UserNotFoundException e) {
		request.getRequestDispatcher("player.jsp").forward(request, response);
	} catch (UserException e) {
		request.getRequestDispatcher("player.jsp").forward(request, response);
	}
		
		
		
		
		
		
		
		model.addAttribute("videoId", videoId);
		return "player";
	}
	
	@RequestMapping(value="/login", method = RequestMethod.GET)
	public String login() {
		return "login";
	}
	
	@RequestMapping(value="/register", method = RequestMethod.GET)
	public String register() {
		return "register";
	}
	
	@RequestMapping(value="/upload", method = RequestMethod.GET)
	public String upload() {
		return "upload";
	}
}
