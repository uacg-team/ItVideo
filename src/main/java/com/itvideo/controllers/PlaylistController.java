package com.itvideo.controllers;

import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

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
			playlist.getPlaylistForUser(userId);
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
	//TODO loadPlaylistForUserByName(Model model,long userId,...)
}
