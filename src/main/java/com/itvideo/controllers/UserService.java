package com.itvideo.controllers;

import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
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
	public static class ReportMsg{
		private String typeError;
		private String msg;
		private String action;
		ReportMsg(String typeError,String report,String action){
			this.msg = report;
			this.typeError = typeError;
			this.action=action;
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
}
