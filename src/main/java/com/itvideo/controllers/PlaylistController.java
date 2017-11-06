package com.itvideo.controllers;

import java.sql.SQLException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.itvideo.model.Playlist;
import com.itvideo.model.PlaylistDao;
import com.itvideo.model.User;
import com.itvideo.model.UserDao;
import com.itvideo.model.Video;
import com.itvideo.model.exceptions.playlists.PlaylistException;
import com.itvideo.model.exceptions.user.UserException;
import com.itvideo.model.exceptions.user.UserNotFoundException;

@Controller
@Component
public class PlaylistController {
	@Autowired
	private PlaylistDao pd;
	@Autowired
	private UserDao ud;
	
	@ResponseBody
	@RequestMapping(value = "player/addToPlaylist", method = RequestMethod.POST)
	public void addToPlaylists(HttpServletRequest req) throws SQLException {
		long videoId=Long.parseLong(req.getParameter("videoId"));
		long playlistId=Long.parseLong(req.getParameter("playlistId"));
		try {
			pd.addVideo(playlistId, videoId);
		} catch (SQLException e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	@RequestMapping(value = "/deletePlaylist", method = RequestMethod.POST)
	public String deletePlaylist(HttpServletRequest req,Model model,HttpSession session) {
		long userId=0;
		try {
			userId=((User)session.getAttribute("user")).getUserId();
		}catch(NullPointerException e) {
			model.addAttribute("exception", "MessagingException");
			model.addAttribute("getMessage", "You don't own this playlist");
			e.printStackTrace();
			return "error";
		}
		try {
			long playlistId = Long.parseLong(req.getParameter("playlistId"));
			pd.deletePlaylist(playlistId,userId);
		} catch (PlaylistException e) {
			model.addAttribute("exception", "MessagingException");
			model.addAttribute("getMessage", e.getMessage());
			e.printStackTrace();
			return "error";
		} catch (SQLException e) {
			model.addAttribute("exception", "SQLException");
			model.addAttribute("getMessage", e.getMessage());
			e.printStackTrace();
			return "error";
		}
		return "redirect:/viewProfile/"+userId;
	}
	
	@RequestMapping(value = "/editPlaylist", method = RequestMethod.POST)
	public String editPlaylist(HttpServletRequest req,Model model,HttpSession session) {
		long userId=0;
		try {
			userId=((User)session.getAttribute("user")).getUserId();
		}catch(NullPointerException e) {
			model.addAttribute("exception", "MessagingException");
			model.addAttribute("getMessage", "You don't own this playlist");
			e.printStackTrace();
			return "error";
		}
		try {
			long playlistId = Long.parseLong(req.getParameter("playlistId"));
			String playlistName = req.getParameter("newPlaylistName");
			if(playlistName==null||playlistName.trim().isEmpty()) {
				return "redirect:/viewProfile/"+userId;
			}
			Playlist newPlaylist = new Playlist(playlistName, userId);
			newPlaylist.setPlaylistId(playlistId);
			pd.updatePlaylist(newPlaylist);
		} catch (PlaylistException e) {
			model.addAttribute("exception", "MessagingException");
			model.addAttribute("getMessage", e.getMessage());
			e.printStackTrace();
			return "error";
		} catch (SQLException e) {
			model.addAttribute("exception", "SQLException");
			model.addAttribute("getMessage", e.getMessage());
			e.printStackTrace();
			return "error";
		} catch (UserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "redirect:/viewProfile/"+userId;
	}
	
	@RequestMapping(value = "/createPlaylist", method = RequestMethod.POST)
	public String createPlaylist(HttpServletRequest req,Model model) {
		long userId = Long.parseLong(req.getParameter("userId"));
		String playlistName = req.getParameter("newPlaylist");
		Playlist newPlaylist = null;
		try {
			newPlaylist = new Playlist(playlistName, userId);
		} catch (PlaylistException e) {
			model.addAttribute("exception", "MessagingException");
			model.addAttribute("getMessage", e.getMessage());
			e.printStackTrace();
			return "error";
		}
		try {
			pd.createPlaylist(newPlaylist);
		} catch (PlaylistException e) {
			model.addAttribute("exception", "MessagingException");
			model.addAttribute("getMessage", e.getMessage());
			e.printStackTrace();
			return "error";
			
		} catch (SQLException e) {
			model.addAttribute("exception", "SQLException");
			model.addAttribute("getMessage", e.getMessage());
			e.printStackTrace();
			return "error";
		} catch (UserException e) {
			model.addAttribute("exception", "MessagingException");
			model.addAttribute("getMessage", e.getMessage());
			e.printStackTrace();
			return "error";
		}
		return "redirect:/viewProfile/"+userId;
	}
	@RequestMapping(value = "/showPlaylist", method = RequestMethod.GET)
	public String loadVideosForPlaylist(HttpServletRequest req,Model model) {
		Long userId = Long.valueOf(req.getParameter("userId"));
		String playlistName = req.getParameter("playlistName");
		try {
			Playlist thePlaylist = pd.getPlaylist(userId, playlistName);
			pd.loadVideosInPlaylist(thePlaylist);
			List<Video> videos= thePlaylist.getVideos();
			for(Video v:videos) {
				User videoOwner =  ud.getUser(v.getUserId());
				v.setUserName(videoOwner.getUsername());
			}
			req.setAttribute("videos", videos);
			req.setAttribute("playlistName", playlistName);
			req.setAttribute("userId", thePlaylist.getUserId());
			req.setAttribute("playlistId", thePlaylist.getPlaylistId());
		} catch (UserException e) {
			model.addAttribute("exception", "MessagingException");
			model.addAttribute("getMessage", e.getMessage());
			e.printStackTrace();
			return "error";
		} catch (SQLException e) {
			model.addAttribute("exception", "SQLException");
			model.addAttribute("getMessage", e.getMessage());
			e.printStackTrace();
			return "error";
		} catch (PlaylistException e) {
			model.addAttribute("exception", "MessagingException");
			model.addAttribute("getMessage", e.getMessage());
			e.printStackTrace();
			return "error";
		} catch (UserNotFoundException e) {
			model.addAttribute("exception", "MessagingException");
			model.addAttribute("getMessage", e.getMessage());
			e.printStackTrace();
			return "error";
		}
		return "playlistVideos";
	}
}
