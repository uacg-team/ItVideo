package com.itvideo.controllers;

import java.sql.SQLException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

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
	
	@RequestMapping(value="/follow", method = RequestMethod.POST)
	public String followPost(
			HttpSession session,
			Model model, 
			@RequestParam("following") long followingId, 
			@RequestParam("follower") long followerId, 
			@RequestParam("action") String action) {
		
		
		User follower = (User) session.getAttribute("user");
		if (follower == null) {
			return "login";
		}
		
		System.out.println("action = " + action);
		
		try {
			if (action.equals("follow")) {
				ud.followUser(followingId, followerId);

			}
			if (action.equals("unfollow")) {
				ud.unfollowUser(followingId, followerId);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return "redirect:/viewProfile/"+followingId;
	}
	
	@RequestMapping(value = "/updateUser/{userId}", method = RequestMethod.GET)
	public String updateUserGet() {
		return "updateUser";
	}
	
	@RequestMapping(value = "/deleteUser/{userId}", method = RequestMethod.POST)
	public String deleteUserPost(HttpSession session, @PathVariable("userId") long userId) {
		try {
			User user = (User) session.getAttribute("user");
			if (user.getUserId() == userId) {
				ud.delete(user.getUserId());
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "redirect:/main";
	}
	
	@RequestMapping(value = "/updateUser/{userId}", method = RequestMethod.POST)
	public String updateUserPost(
			HttpSession session, 
			Model model, 
			HttpServletRequest request,
			@RequestParam("firstName") String firstName,
			@RequestParam("lastName") String lastName,
			@RequestParam("facebook") String facebook,
			@RequestParam("gender") String gender ) {
		try {
			User u = (User) session.getAttribute("user");
			
			if (!firstName.equals("")) {
				u.setFirstName(firstName);
			}
			if (!lastName.equals("")) {
				u.setLastName(lastName);
			}
			if (!facebook.equals("")) {
				u.setFacebook(facebook);
			}

			if (gender == "null") {
				gender = null;
			}
			
			u.setGender(gender);
			ud.updateUser(u);
		} catch (UserException e) {
			e.printStackTrace();
			model.addAttribute("UserException", e.getMessage());
			return "updateUser";
		} catch (SQLException e) {
			e.printStackTrace();
			model.addAttribute("SQLException", e.getMessage());
			return "updateUser";
		} catch (UserNotFoundException e) {
			e.printStackTrace();
			model.addAttribute("UserNotFoundException", e.getMessage());
			return "updateUser";
		}
		return "updateUser";
	}
	
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
				pc.loadPlaylistsForUser(model, u.getUserId());
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
