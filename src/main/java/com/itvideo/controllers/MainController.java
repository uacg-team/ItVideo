package com.itvideo.controllers;

import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.itvideo.model.Video;
import com.itvideo.model.VideoDao;

@Controller
public class MainController {
	@Autowired
	VideoDao vd;
	
	@RequestMapping(value="/index", method = RequestMethod.GET)
	public String index(Model model) {
		try {
			List<Video> videos = vd.getAllVideoOrderByDate();
			model.addAttribute("videos", videos);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return "main";
	}
	
	@RequestMapping(value="/main", method = RequestMethod.GET)
	public String main() {
		return "main";
	}
	
	@RequestMapping(value="/login", method = RequestMethod.GET)
	public String login() {
		return "login";
	}
}
