package com.itvideo.controllers;

import java.io.IOException;
import java.sql.SQLException;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
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
import com.itvideo.model.utils.PasswordGenerator;
import com.itvideo.model.utils.Resources;
import com.itvideo.model.utils.SendEmail;
import com.itvideo.model.utils.TockenGenerator;

@Controller
@RestController
public class UserService {
	@Autowired
	UserDao ud;
	
	/**
	 * report to js if there is problem,and what action is coming next
	 */
	public static class ReportMsg{
		private String typeError;
		private String msg;
		private String action;

		ReportMsg(String typeError, String report, String action) {
			this.msg = report;
			this.typeError = typeError;
			this.action = action;
		}

		public String getAction() {
			return action;
		}

		public String getMsg() {
			return msg;
		}

		public String getTypeError() {
			return typeError;
		}
	}
	
	@ResponseBody	
	@RequestMapping(value="/forgotPassword", method = RequestMethod.POST)
	public ReportMsg forgotPasswordPost(
			HttpServletResponse response,
			@RequestParam("email") String email) {
		String newPassword = PasswordGenerator.generate();
		try {
			User u =  ud.getUserByEmail(email);
			
			u.setPasswordNoValidation(newPassword);
			SendEmail.forgottenPassword(u);
			u.setPasswordNoValidation(Hash.getHashPass(newPassword));
			ud.updateUser(u);
			response.setStatus(200);
			return new ReportMsg(" ", " ","none");
		} catch (SQLException e) {
			response.setStatus(500);
			return new ReportMsg("sqlError", e.getMessage() ,"none");
		} catch (UserNotFoundException e) {
			response.setStatus(400);
			return new ReportMsg("userError", e.getMessage(),"none");
		} catch (UserException e) {
			response.setStatus(400);
			return new ReportMsg("userError", e.getMessage(),"none");
		} catch (MessagingException e) {
			response.setStatus(400);
			return new ReportMsg("emailError", e.getMessage(),"none");
		}
	}
	
	@ResponseBody
	@RequestMapping(value="/main/login", method = RequestMethod.POST)
	public ReportMsg loginPostTest(HttpSession session, HttpServletResponse response,HttpServletRequest request) {
		String username=request.getParameter("username");
		String password=request.getParameter("password");
		String hashedPass = Hash.getHashPass(password);
		try {
			User u = ud.getUser(username);
			if (!u.isActivated()) {
				response.setStatus(401);
				return new ReportMsg("not-activated", "Please Activate Your Account","none");
			}
			if (hashedPass.equals(u.getPassword())) {
				session.setMaxInactiveInterval(-1);
				session.setAttribute("user", u);
				return new ReportMsg(" ", " ","main");
			} else {
				response.setStatus(401);
				return new ReportMsg("passwordError", "Wrong Password","none");
			}
		} catch (SQLException e) {
			response.setStatus(500);
			return new ReportMsg("sqlError", "Our team has been alerted of the issue, we are looking into it immediately","none");
		} catch (UserNotFoundException e) {
			response.setStatus(401);
			return new ReportMsg("usernameError", e.getMessage(),"none");
		} catch (UserException e) {
			response.setStatus(401);
			return new ReportMsg("usernameError", e.getMessage(),"none");
		} 
	}
	
	@ResponseBody
	@RequestMapping(value="/main/register", method = RequestMethod.POST)
	public ReportMsg registerPost( HttpServletResponse response,HttpServletRequest request,HttpSession session ) {
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		String confirmPassword = request.getParameter("confirmPassword");
		String email = request.getParameter("email");
		User u = null;
		try {
			if (ud.existsUser(username)) {
				response.setStatus(400);
				return new ReportMsg("usernameError", "This username already exist", "none");
			}		
			if (ud.existsEmail(email)) {
				response.setStatus(400);
				return new ReportMsg("emailError", "This email already exist", "none");
			}		
			if (!password.equals(confirmPassword)) {
				response.setStatus(400);
				return new ReportMsg("passwordError", "Password differ", "none");
			}
			u = new User(username, password, email);
			u.setAvatarUrl("avatar.png");
			String token = new TockenGenerator().generate();
			u.setActivationToken(token);
			u.setPasswordNoValidation(Hash.getHashPass(password));
			ud.createUser(u);
			SendEmail.welcome(u);
			Resources.initAvatar(u,session);
			return new ReportMsg("success", "", "none");
		} catch (SQLException e) {
			e.printStackTrace();
			response.setStatus(500);
			return new ReportMsg("sqlError", "DataBase problem.Our team has been alerted of the issue, we are looking into it immediately.","none");
		} catch (UserException e) {
			response.setStatus(400);
			return new ReportMsg("userError",  e.getMessage(), "none");
		} catch (IOException e) {
			e.printStackTrace();
			response.setStatus(500);
			return new ReportMsg("IOError",  "DataBase problem.Our team has been alerted of the issue, we are looking into it immediately.", "none");
		} catch (MessagingException e) {
			response.setStatus(400);
			return new ReportMsg("emailError",  e.getMessage(), "none");
		}
	}
}
