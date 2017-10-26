package com.itvideo.controllers;

import java.sql.SQLException;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.itvideo.model.User;
import com.itvideo.model.UserDao;
import com.itvideo.model.Video;
import com.itvideo.model.VideoDao;
import com.itvideo.model.exceptions.user.UserException;
import com.itvideo.model.exceptions.user.UserNotFoundException;

@Controller
public class UserController {
	
	@Autowired
	UserDao ud;

	@Autowired
	VideoDao vd;
	
	@Autowired
	PlaylistController pc;
	
	@RequestMapping(value = "/viewProfile/{userId}", method = RequestMethod.GET)
	public String getVideo(@PathVariable("userId") long userId, Model model, HttpSession session) {
		User loggedUser = (User) session.getAttribute("user");
		try {
				User u = ud.getUser(userId);
				
				List<User> followers = ud.getFollowers(u.getUserId());
				List<User> following = ud.getFollowing(u.getUserId());
				List<Video> videos = null;
				
				if (loggedUser != null && loggedUser.getUserId() == u.getUserId()) {
					videos = vd.getVideos(u.getUserId());
				} else {
					videos = vd.getPublicVideos(u.getUserId());
				}
				
				for (Video video : videos) {
					video.setUserName(vd.getUserName(video.getUserId()));
					video.setPrivacy(vd.getPrivacy(video.getPrivacyId()));
				}
				
				model.addAttribute("user", u);
				model.addAttribute("followers", followers);
				model.addAttribute("following", following);
				model.addAttribute("videos", videos);
				pc.loadPlaylistForUser(model, u.getUserId());
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (UserNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (UserException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		return "viewProfile";
	}
	
}
