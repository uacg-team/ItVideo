package com.itvideo.controllers;

import java.sql.SQLException;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.itvideo.model.User;
import com.itvideo.model.UserDao;
import com.itvideo.model.exceptions.user.UserException;
import com.itvideo.model.exceptions.user.UserNotFoundException;
import com.itvideo.model.utils.Hash;

@Controller
@RestController
public class UserService {
	@Autowired
	UserDao ud;
	
	@ResponseBody
	@RequestMapping(value="/logint", method = RequestMethod.POST)
	public void loginPostTest(HttpSession session, HttpServletResponse response,
			@RequestParam("username") String username,
			@RequestParam("password") String password) {
		String hashedPass = Hash.getHashPass(password);
		try {
			User u = ud.getUser(username);
			if (!u.isActivated()) {
				response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
				
				//model.addAttribute("username", username);
			//	model.addAttribute("usernameError", "Please Activate Your Account");
			}
			
			if (hashedPass.equals(u.getPassword())) {
				//session.setMaxInactiveInterval(-1);
				//session.setAttribute("user", u);
			} else {
				response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			//	model.addAttribute("username", username);
			//	model.addAttribute("passwordError", "Wrong Password");
			}
		} catch (SQLException e) {
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			//model.addAttribute("error", e.getMessage());
		} catch (UserNotFoundException e) {
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
		//	model.addAttribute("usernameError", e.getMessage());
		} catch (UserException e) {
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
		//	model.addAttribute("error", e.getMessage());
		} 
	}
}
