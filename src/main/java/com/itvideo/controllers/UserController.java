package com.itvideo.controllers;

import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
import com.itvideo.model.utils.Hash;
import com.itvideo.model.utils.PasswordGenerator;
import com.itvideo.model.utils.Resources;
import com.itvideo.model.utils.SendEmail;

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
			@RequestParam("username") String username,
			@RequestParam("newPassword") String newPassword,
			@RequestParam("newPasswordConfirm") String newPasswordConfirm,
			@RequestParam("oldPassword") String oldPassword,
			@RequestParam("email") String email,
			@RequestParam("firstName") String firstName,
			@RequestParam("lastName") String lastName,
			@RequestParam("facebook") String facebook,
			@RequestParam("gender") String gender ) {
		try {
			User u = (User) session.getAttribute("user");
		
			if (!Hash.getHashPass(oldPassword).equals(u.getPassword())) {
				model.addAttribute("errorPassword", "wrong password");
				return "updateUser";
			}
			
			if (!username.equals("")) {
				if (ud.existsUser(username)) {
					model.addAttribute("errorUsername", username + " is not available");
					return "updateUser";
				} 
			}
			
			if (!email.equals("")) {
				if (ud.existsEmail(email)) {
					model.addAttribute("errorEmail", email + " already exist");
					return "updateUser";
				}
			}
			
			if (!newPassword.equals(newPasswordConfirm)) {
				model.addAttribute("errorNewPassword", "new passwords differs");
				return "updateUser";
			}

			if (!username.equals("")) {
				u.setUsername(username);
			}
			
			if (!email.equals("")) {
				u.setEmail(email);
			}
			
			if (!newPassword.equals("")) {
				u.setPasswordNoValidation(Hash.getHashPass(newPassword));
			}

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
	
	@RequestMapping(value="/login", method = RequestMethod.GET)
	public String loginForm() {
		return "login";
	}
	
	@RequestMapping(value="/login", method = RequestMethod.POST)
	public String login(Model model, HttpSession session, HttpServletResponse response,
			@RequestParam("username") String username,
			@RequestParam("password") String password ) {
		
		String hashedPass = Hash.getHashPass(password);
		try {
			User u = ud.getUser(username);
			if (hashedPass.equals(u.getPassword())) {
				session.setMaxInactiveInterval(-1);
				session.setAttribute("user", u);
				return "redirect:main";
			} else {
				response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
				model.addAttribute("username", username);
				model.addAttribute("passwordError", "Wrong Password");
				return "login";
			}
		} catch (SQLException e) {
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			model.addAttribute("error", e.getMessage());
			return "login";
		} catch (UserNotFoundException e) {
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			model.addAttribute("usernameError", e.getMessage());
			return "login";
		} catch (UserException e) {
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			model.addAttribute("error", e.getMessage());
			return "login";
		} 
	}
	
	@RequestMapping(value="/register", method = RequestMethod.GET)
	public String registerGet() {
		return "register";
	}
	
	
	@RequestMapping(value="/register", method = RequestMethod.POST)
	public String registerPost(Model model, HttpSession session,
			@RequestParam("username") String username,
			@RequestParam("password") String password,
			@RequestParam("confirmPassword") String confirmPassword,
			@RequestParam("email") String email ) {
		User u = null;
		model.addAttribute("username", username);
		model.addAttribute("email", email);
		try {
			if (ud.existsUser(username)) {
				model.addAttribute("usernameError", "This username already exist");
				return "register";
			}		
		
			if (!password.equals(confirmPassword)) {
				model.addAttribute("passError", "Password differ");
				return "register";
			}
			
			u = new User(username, password, email);
			u.setAvatarUrl("avatar.png");
			
			SendEmail.to(u);
			
			u.setPasswordNoValidation(Hash.getHashPass(password));
			ud.createUser(u);
			session.setAttribute("user", u);
			
			InputStream in = session.getServletContext().getResourceAsStream("/static/img/avatar.png");
			Resources.initAvatar(u,in);
			
			return "redirect:/main";
		} catch (SQLException e) {
			model.addAttribute("userError", "SQLException: " + e.getMessage());
			return "register";
		} catch (UserException e) {
			model.addAttribute("userError", "UserException: " + e.getMessage());
			return "register";
		} catch (IOException e) {
			model.addAttribute("userError", "IOException: " + e.getMessage());
			return "register";
		}
	}
	
	
	@RequestMapping(value="/forgotPassword", method = RequestMethod.GET)
	public String forgotPasswordGet(@RequestParam("username") String username,Model model) {
		model.addAttribute("username", username);
		return "forgotPassword";
	}
	
	@RequestMapping(value="/forgotPassword", method = RequestMethod.POST)
	public String forgotPasswordPost(
			Model model,
			@RequestParam("username") String username,
			@RequestParam("email") String email) {
		String newPassword = PasswordGenerator.generate();
		try {
			User u = ud.getUser(username);
			if (!email.equals(u.getEmail())) {
				model.addAttribute("emailError", "Email doesn't match to the user");
				return "forgotPassword";
			}
			u.setPasswordNoValidation(newPassword);
			SendEmail.to(u);
			u.setPasswordNoValidation(Hash.getHashPass(newPassword));
			ud.updateUser(u);
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
		return "redirect:main";
	}
	
	@RequestMapping(value="/logout", method = RequestMethod.GET)
	public String logout(HttpSession session) {
		session.invalidate();
		return "redirect:main";
	}
}
