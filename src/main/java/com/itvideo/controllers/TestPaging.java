package com.itvideo.controllers;

import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.itvideo.model.Video;
import com.itvideo.model.VideoDao;

@Controller
@Component
public class TestPaging {
	
	private static final int ITEMS_PER_PAGE = 4;
	
	@Autowired
	private VideoDao vd;
	
	@RequestMapping(value = "/main1", method = RequestMethod.POST)
	public String videosPagePost(@RequestParam("pageid") int pageid, Model model) {
		List<Video> list = null;
		try {
			int totalPages = vd.getAllVideos();
			int total = ITEMS_PER_PAGE;
			
			if (pageid == 1) {
			} else {
				pageid = (pageid - 1) * total + 1;
			}
			list = vd.getVideosByPage(pageid, total);
			model.addAttribute("totalPages", totalPages);
			model.addAttribute("total", total);
			model.addAttribute("list", list);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return "main1";
	}

	@RequestMapping(value = "/main1")
	public String main1(Model model) {

		List<Video> list = null;
		try {
			int totalPages = vd.getAllVideos();
			int total = ITEMS_PER_PAGE;
			list = vd.getVideosByPage(1, total);
			model.addAttribute("totalPages", totalPages);
			model.addAttribute("total", total);
			model.addAttribute("list", list);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return "main1";
	}
}
