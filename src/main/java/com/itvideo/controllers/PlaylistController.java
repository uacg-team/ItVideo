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
import com.itvideo.model.exceptions.user.UserException;

@Controller
@Component
public class PlaylistController {
	@Autowired
	PlaylistDao playlist;
	
	public void loadPlaylistsForUser(Model model, long userId) {
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
	@ResponseBody
	@RequestMapping(value = "player/addToPlaylist", method = RequestMethod.POST)
	public void addToPlaylists(HttpServletRequest req) {
		long videoId=Long.parseLong(req.getParameter("userId"));
		long playlistId=Long.parseLong(req.getParameter("playlistId"));
		try {
			playlist.addVideo(playlistId, videoId);
		} catch (SQLException e) {
			// TODO handle
			e.printStackTrace();
		}
	}
}
