package com.itvideo.controllers;

import static org.mockito.Mockito.reset;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.httpclient.HttpClientError;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.itvideo.model.CommentDao;
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
import com.mysql.fabric.Response;
import com.sun.jna.platform.win32.VerRsrc.VS_FIXEDFILEINFO;

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
	public String search(HttpServletRequest request, Model model, HttpServletResponse response) {
		String search = (String) request.getParameter("search");
		List<Video> videos = null;
		List<User> users = null;
		List<Playlist> playlists = null;
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
			model.addAttribute("exception", "SQLException");
			model.addAttribute("getMessage", e.getMessage());
			return "error";
		} catch (VideoException e) {
			model.addAttribute("exception", "VideoException");
			model.addAttribute("getMessage", e.getMessage());
			return "error";
		} catch (UserException e) {
			model.addAttribute("exception", "UserException");
			model.addAttribute("getMessage", e.getMessage());
			return "error";
		}
		model.addAttribute("videos", videos);
		model.addAttribute("users", users);
		model.addAttribute("playlists", playlists);
		return "search";
	}
	
	@RequestMapping(value="/main/sort/{param}", method = RequestMethod.GET)
	public String sort(HttpSession session, Model model, @PathVariable("param") String param) {
		try {
			List<Video> videos = null;
			switch (param) {
			case "date":
				session.setAttribute("sort", "date");
				videos = vd.getAllVideoOrderByDate();
				break;
			case "like":
				session.setAttribute("sort", "like");
				videos = vd.getAllVideoOrderByLikes();
				break;
			case "view":
				session.setAttribute("sort", "view");
				videos = vd.getAllVideoOrderByViews();
				break;
			default:
				session.setAttribute("sort", "date");
				videos = vd.getAllVideoOrderByDate();
				break;
			}
			
			for (Video video : videos) {
				video.setUserName(vd.getUserName(video.getUserId()));
				video.setPrivacy(vd.getPrivacy(video.getPrivacyId()));
			}
			
			session.setAttribute("videos", videos);
			return "redirect:/main";
		} catch (SQLException e) {
			model.addAttribute("exception", "SQLException");
			model.addAttribute("getMessage", e.getMessage());
			return "error";
		}
	}
	
	
	@RequestMapping(value = "/index", method = RequestMethod.GET)
	public String index() {
		return "redirect:/main";
	}

	@RequestMapping(value = "/main", method = RequestMethod.GET)
	public String main(HttpSession session, Model model) {
		try {
			String param = (String) session.getAttribute("sort");
			List<Video> videos = null;
			if (param == null) {
				
				videos = vd.getAllVideoOrderByDate();
				if (videos != null) {
					for (Video video : videos) {
						video.setUserName(vd.getUserName(video.getUserId()));
						video.setPrivacy(vd.getPrivacy(video.getPrivacyId()));
					}
				}
				
				session.setAttribute("sort", "date");
				session.setAttribute("videos", videos);
				return "main";
			}else {
				switch (param) {
				case "date":
					session.setAttribute("sort", "date");
					videos = vd.getAllVideoOrderByDate();
					break;
				case "like":
					session.setAttribute("sort", "like");
					videos = vd.getAllVideoOrderByLikes();
					break;
				case "view":
					session.setAttribute("sort", "view");
					videos = vd.getAllVideoOrderByViews();
					break;
				default:
					session.setAttribute("sort", "date");
					videos = vd.getAllVideoOrderByDate();
					break;
				}
				
				for (Video video : videos) {
					video.setUserName(vd.getUserName(video.getUserId()));
					video.setPrivacy(vd.getPrivacy(video.getPrivacyId()));
				}
				session.setAttribute("videos", videos);
			}
		} catch (SQLException e) {
			model.addAttribute("exception", "SQLException");
			model.addAttribute("getMessage", e.getMessage());
			return "error";
		}
		return "main";
	}
	
	@RequestMapping(value="/player/{videoId}", method = RequestMethod.GET)
	public String player(Model model, @PathVariable("videoId") long videoId, HttpSession session, HttpServletResponse response) {
		try {
			if (!vd.existsVideo(videoId)) {
				model.addAttribute("exception", "VideoNotFoundException");
				model.addAttribute("getMessage", "Video Not Found");
				return "error";
			}
			
			User user = (User) session.getAttribute("user");
			long userId = 0;
			if (user != null) {
				userId = user.getUserId();
			}
			
			
			Video video = vd.getVideoForPlayer(videoId, userId);

			Set<Video> related = vd.getRelatedVideos(videoId);
			
			vd.increaseViews(videoId);
			
			model.addAttribute("mainVideo", video);
			model.addAttribute("related", related);
			
//			cc.loadCommentsForVideo(model,videoId);
			if(session.getAttribute("user")!=null) {
				pc.loadPlaylistsForUser(model, userId);
				cc.loadCommentsWithVotesForVideo(model, videoId, userId, CommentDao.ASC_BY_DATE);
			}else {
				//TODO 
				cc.loadCommentsWithVotesForVideo(model, videoId, 0, CommentDao.ASC_BY_DATE);
			}
			return "player";
		} catch (SQLException e) {
			model.addAttribute("exception", "SQLException");
			model.addAttribute("getMessage", e.getMessage());
			return "error";
		} catch (VideoNotFoundException e) {
			model.addAttribute("exception", "VideoNotFoundException");
			model.addAttribute("getMessage", e.getMessage());
			return "error";
		}
	}
}
