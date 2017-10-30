package com.itvideo.controllers;

import java.sql.SQLException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

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
	private PlaylistDao playlist;
	@Autowired
	private UserDao ud;
	
	public void loadPlaylistsForUser(Model model, long userId) {
		//get for this videostatus
		List<Playlist> playlists = null;
		try {
			playlists=playlist.getPlaylistForUser(userId);
			model.addAttribute("myPlaylists", playlists);
		} catch (UserException e) {
			// TODO handle
			e.printStackTrace();
			return;
		} catch (SQLException e) {
			// TODO handle
			e.printStackTrace();
			return;
		}
	}
	public void loadPlaylistsForUserWithStatus(Model model, long userId,long videoId) {
		//get for this videostatus
		List<Playlist> playlists = null;
		try {
			playlists=playlist.getPlaylistForUserWithStatus(userId,videoId);
			model.addAttribute("myPlaylists", playlists);
		} catch (UserException e) {
			// TODO handle
			e.printStackTrace();
			return;
		} catch (SQLException e) {
			// TODO handle
			e.printStackTrace();
			return;
		}
	}
	@ResponseBody
	@RequestMapping(value = "player/addToPlaylist", method = RequestMethod.POST)
	public void addToPlaylists(HttpServletRequest req) {
		long videoId=Long.parseLong(req.getParameter("videoId"));
		long playlistId=Long.parseLong(req.getParameter("playlistId"));
		try {
			playlist.addVideo(playlistId, videoId);
		} catch (SQLException e) {
			// TODO handle
			e.printStackTrace();
		}
	}
	@RequestMapping(value = "/createPlaylist", method = RequestMethod.POST)
	public String createPlaylist(HttpServletRequest req) {
		long userId = Long.parseLong(req.getParameter("userId"));
		String playlistName = req.getParameter("newPlaylist");
		Playlist newPlaylist = null;
		try {
			newPlaylist = new Playlist(playlistName, userId);
		} catch (PlaylistException e) {
			// TODO handle return error to user wrong name, not good name...
			e.printStackTrace();
			return null;
		}
		try {
			playlist.createPlaylist(newPlaylist);
		} catch (PlaylistException e) {
			// TODO handle
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO handle
			e.printStackTrace();
		} catch (UserException e) {
			// TODO handle
			e.printStackTrace();
		}
		return "redirect:/viewProfile/"+userId;
	}
	@RequestMapping(value = "/showPlaylist", method = RequestMethod.GET)
	private String loadVideosForPlaylist(HttpServletRequest req) {
		Long userId = Long.valueOf(req.getParameter("userId"));
		String playlistName = req.getParameter("playlistName");
		try {
			Playlist thePlaylist = playlist.getPlaylist(userId, playlistName);
			playlist.loadVideosInPlaylist(thePlaylist);
			List<Video> videos= thePlaylist.getVideos();
			for(Video v:videos) {
				User videoOwner =  ud.getUser(v.getUserId());
				v.setUserName(videoOwner.getUsername());
			}
			req.setAttribute("videos", videos);
			req.setAttribute("playlistName", playlistName);
		} catch (UserException e) {
			// TODO handle
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO handle
			e.printStackTrace();
		} catch (PlaylistException e) {
			// TODO handle
			e.printStackTrace();
		} catch (UserNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "playlistVideos";
	}
}
