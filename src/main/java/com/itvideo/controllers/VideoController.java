package com.itvideo.controllers;

import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

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
	
	@ResponseBody
	@RequestMapping(value = "/player/videoAsyncLike", method = RequestMethod.POST)
	public void videoAsyncLike(HttpSession session, HttpServletResponse response,
			@RequestParam("like") int like,
			@RequestParam("videoId") int videoId,
			@RequestParam("userId") int userId) {
		User u = (User) session.getAttribute("user");
		if (u != null) {
			try {
				vd.like(videoId, userId, like);
			} catch (SQLException e) {
				//TODO: return status code and handle it with JS
				e.printStackTrace();
			}
		}
	}

	@RequestMapping(value = "/videoLike", method = RequestMethod.POST)
	@Deprecated
	public String likeVideo(HttpSession session, Model model, HttpServletRequest request) {
		User u = (User) session.getAttribute("user");
		if (u == null) {
			return "login";
		}

		int like = Integer.valueOf(request.getParameter("like"));
		long videoId = Long.valueOf(request.getParameter("videoId"));
		long userId = u.getUserId();
		try {
			vd.like(videoId, userId, like);
		} catch (SQLException e) {
			model.addAttribute("exception", "SQLException");
			model.addAttribute("getMessage", e.getMessage());
			return "error";
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
			model.addAttribute("exception", "VideoNotFoundException");
			model.addAttribute("getMessage", e.getMessage());
			return "error";
		} catch (SQLException e) {
			e.printStackTrace();
			model.addAttribute("exception", "SQLException");
			model.addAttribute("getMessage", e.getMessage());
			return "error";
		}
		return "editVideo";
	}
	
	@RequestMapping(value = "/editVideo/{videoId}", method = RequestMethod.POST)
	public String editVideoPost( Model model,
			@PathVariable("videoId") long videoId,
			@RequestParam("name") String newName, 
			@RequestParam("description") String newDesc, 
			@RequestParam("privacy") long newPrivacy) {
		try {
			Video v = vd.getVideo(videoId);
			
			if (!newName.equals("")) {
				v.setName(newName);
			}
			
			if (!newDesc.equals("")) {
				v.setDescription(newDesc);
			}
			
			v.setPrivacyId(newPrivacy);
			vd.updateVideo(v);
		} catch (VideoNotFoundException e) {
			e.printStackTrace();
			model.addAttribute("exception", "VideoNotFoundException");
			model.addAttribute("getMessage", e.getMessage());
			return "error";
		} catch (SQLException e) {
			e.printStackTrace();
			model.addAttribute("exception", "SQLException");
			model.addAttribute("getMessage", e.getMessage());
			return "error";
		} catch (VideoException e) {
			e.printStackTrace();
			model.addAttribute("exception", "VideoException");
			model.addAttribute("getMessage", e.getMessage());
			return "error";
		}
		return "redirect:/player/"+videoId;
	}
	
	@RequestMapping(value = "/deleteVideo", method = RequestMethod.POST)
	public String deleteVideoPost(@RequestParam("videoId") long videoId, Model model) {
		try {
			Resources.deleteVideo(vd.getVideo(videoId));
			vd.deleteVideo(videoId);
		} catch (SQLException e) {
			e.printStackTrace();
			model.addAttribute("exception", "SQLException");
			model.addAttribute("getMessage", e.getMessage());
			return "error";
		} catch (VideoNotFoundException e) {
			e.printStackTrace();
			model.addAttribute("exception", "VideoNotFoundException");
			model.addAttribute("getMessage", e.getMessage());
			return "error";
		}
		return "redirect:/main";
	}
}
