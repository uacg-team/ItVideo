package com.itvideo.controllers;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

import javax.mail.Multipart;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.itvideo.model.Tag;
import com.itvideo.model.User;
import com.itvideo.model.Video;
import com.itvideo.model.VideoDao;
import com.itvideo.model.exceptions.tags.TagNotFoundException;
import com.itvideo.model.exceptions.video.VideoException;
import com.itvideo.model.utils.Resources;

@Controller
@MultipartConfig
public class UploadController {

	@Autowired
	VideoDao vd;
	
	@RequestMapping(value="/upload", method = RequestMethod.GET)
	public String uploadGet() {
		return "upload";
	}
	
	@RequestMapping(value="/upload", method = RequestMethod.POST)
	public String uploadPost(
			Model model,
			HttpSession session, 
			HttpServletRequest request,
			@RequestParam("file") MultipartFile file) {
		try {
			User u = (User) session.getAttribute("user");
			if (u == null) {
				return "redirect:login";
			}
			
			Part newVideo = request.getPart("newVideo");
			if (!newVideo.getContentType().equals("video/mp4")) {
				model.addAttribute("error", "Wrong File Type");
				return "upload";
			}
			  
			String name = request.getParameter("name");
			Resources.writeVideo(u, newVideo);
			
			String[] inputTags = request.getParameter("tags").split("\\s+");
			Set<Tag> tags = new HashSet<>();
			for (String string : inputTags) {
				tags.add(new Tag(string));
			}
		
			try {
				long privacy = Long.valueOf(request.getParameter("privacy"));
				String fileName = Paths.get(newVideo.getSubmittedFileName()).getFileName().toString();
				Video v = new Video(name, fileName , privacy, u.getUserId(), tags);
				vd.createVideo(v);
			} catch (VideoException e) {
				return "upload";
			} catch (SQLException e) {
				return "upload";
			} catch (TagNotFoundException e) {
				return "upload";
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ServletException e) {
			e.printStackTrace();
		}
		return "redirect:main";
	}
	
}
