package com.itvideo.controllers;

import java.sql.SQLException;
import java.util.List;
import java.util.Set;

import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.itvideo.model.Playlist;
import com.itvideo.model.PlaylistDao;
import com.itvideo.model.User;
import com.itvideo.model.UserDao;
import com.itvideo.model.Video;
import com.itvideo.model.VideoDao;
import com.itvideo.model.exceptions.user.UserException;
import com.itvideo.model.exceptions.user.UserNotFoundException;
import com.itvideo.model.exceptions.video.VideoException;
import com.itvideo.model.exceptions.video.VideoNotFoundException;
import com.itvideo.model.utils.Hash;

@Controller
public class MainController {
	@Autowired
	VideoDao vd;
	@Autowired
	UserDao ud;
	@Autowired
	CommentController cc;
	@Autowired
	PlaylistController pc;
	@Autowired
	PlaylistDao pd;
	
	@RequestMapping(value="/search", method = RequestMethod.GET)
	public String search(HttpServletRequest request) {
		List<Video> videos = null;
		List<User> users = null;
		List<Playlist> playlists = null;
		String search = request.getParameter("search");
		System.out.println(search);
		try {
			if (search != null) {
				videos = vd.searchVideo(search);
				if (videos.size() == 0) {
					videos = null;
				}
				users = ud.searchUser(search);
				if (users.size() == 0) {
					users = null;
				}
				playlists = pd.searchPlaylist(search);
				if (playlists.size() == 0) {
					playlists = null;
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (VideoException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		request.setAttribute("videos", videos);
		request.setAttribute("users", users);
		request.setAttribute("playlists", playlists);
		return "search";
	}
	
	@RequestMapping(value="/main/sort/{param}", method = RequestMethod.GET)
	public String sort(HttpSession session, @PathVariable("param") String param) {
		try {
			switch (param) {
			case "date":
				session.setAttribute("sort", "date");
				session.setAttribute("videos", vd.getAllVideoOrderByDate());
				return "redirect:/main";
			case "like":
				session.setAttribute("sort", "like");
				session.setAttribute("videos", vd.getAllVideoOrderByLikes());
				return "redirect:/main";
			case "view":
				session.setAttribute("sort", "view");
				session.setAttribute("videos", vd.getAllVideoOrderByViews());
				return "redirect:/main";
			default:
				session.setAttribute("sort", "date");
				session.setAttribute("videos", vd.getAllVideoOrderByDate());
				return "redirect:/main";
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return "redirect:/main";
	}

	@RequestMapping(value = "/main", method = RequestMethod.GET)
	public String main(HttpSession session) {
		try {
			String param = (String) session.getAttribute("sort");
			if (param == null) {
				session.setAttribute("sort", "date");
				session.setAttribute("videos", vd.getAllVideoOrderByDate());
				return "main";
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return "main";
	}
	
	@RequestMapping(value="/player/{number}", method = RequestMethod.GET)
	public String player(Model model, @PathVariable("number") long videoId, HttpSession session) {
		try {
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
			
			cc.loadCommentsForVideo(model,videoId);
			if(session.getAttribute("user")!=null) {
				User user = (User)session.getAttribute("user");
				long userId = user.getUserId();
				pc.loadPlaylistForUser(model, userId);
			}
			return "player";
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (VideoNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UserNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "player";
	}
	
	@RequestMapping(value="/login", method = RequestMethod.GET)
	public String loginForm() {
		return "login";
	}
	
	@RequestMapping(value="/login", method = RequestMethod.POST)
	public String login(HttpServletRequest request, HttpServletResponse response) {
		String username = request.getParameter("username");
		String password = Hash.getHashPass(request.getParameter("password"));
		try {
			User u = ud.getUser(username);
			if (password.equals(u.getPassword())) {
				request.getSession().setMaxInactiveInterval(-1);
				request.getSession().setAttribute("user", u);
				return "redirect:main";
			} else {
				request.setAttribute("passwordError", "Wrong Password");
				return "login";
			}
		} catch (SQLException e) {
			request.setAttribute("error", e.getMessage());
			return "login";
		} catch (UserNotFoundException e) {
			request.setAttribute("usernameError", e.getMessage());
			return "login";
		} catch (UserException e) {
			request.setAttribute("error", e.getMessage());
			return "login";
		} 
	}

	
	@RequestMapping(value="/register", method = RequestMethod.GET)
	public String register() {
		return "register";
	}
	
	
	
	@RequestMapping(value="/logout", method = RequestMethod.GET)
	public String logout(HttpSession session) {
		session.invalidate();
		return "redirect:main";
	}
}
