package com.itvideo.controllers;

import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.itvideo.model.User;
import com.itvideo.model.Video;
import com.itvideo.model.VideoDao;
import com.itvideo.model.exceptions.video.VideoException;
import com.itvideo.model.exceptions.video.VideoNotFoundException;
import com.itvideo.model.utils.Resources;

@Controller
public class VideoController {

	@Autowired
	VideoDao vd;

	@RequestMapping(value = "/videoLike", method = RequestMethod.POST)
	public String likeVideo(HttpSession session, HttpServletRequest request) {
		User u = (User) session.getAttribute("user");
		if (u == null) {
			return "login";
		}

		int like = Integer.valueOf(request.getParameter("like"));
		long videoId = Long.valueOf(request.getParameter("videoId"));
		long userId = u.getUserId();
		try {
			if (like == 1) {
				vd.like(videoId, userId);
			}
			if (like == -1) {
				vd.disLike(videoId, userId);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return "redirect:/player/"+videoId;
		}

		return "redirect:/player/"+videoId;
	}
	
	@RequestMapping(value = "/editVideo/{videoId}", method = RequestMethod.GET)
	public String editVideoGet(HttpSession session, Model model, @PathVariable("videoId") long videoId) {
		if (session.getAttribute("user") == null) {
			return "redirect:login";
		}
		try {
			Video video = vd.getVideo(videoId);
			model.addAttribute("video", video);
			User user = (User) session.getAttribute("user");
			model.addAttribute("username", user.getUsername());
			model.addAttribute("userId", user.getUserId());
		} catch (VideoNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return "editVideo";
	}
	
	@RequestMapping(value = "/editVideo/{videoId}", method = RequestMethod.POST)
	public String editVideoPost( 
			@PathVariable("videoId") long videoId,
			@RequestParam("name") String newName, 
			@RequestParam("description") String newDesc, 
			@RequestParam("privacy") long newPrivacy
			) {
		
		try {
			Video v = vd.getVideo(videoId);
			if (newName != null) {
				v.setName(newName);
			}
			if (newDesc != null) {
				v.setDescription(newDesc);
			}
			v.setPrivacyId(newPrivacy);
			vd.updateVideo(v);
		} catch (VideoNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (VideoException e) {
			e.printStackTrace();
		}
		return "redirect:/player/"+videoId;
	}
	
	@RequestMapping(value = "/deleteVideo", method = RequestMethod.POST)
	public String deleteVideoPost(@RequestParam("videoId") long videoId) {
		try {
			//TODO: delete from file system
			Resources.deleteVideo(vd.getVideo(videoId));
			
			vd.deleteVideo(videoId);
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (VideoNotFoundException e) {
			e.printStackTrace();
		}
		return "redirect:/main";
	}
}
