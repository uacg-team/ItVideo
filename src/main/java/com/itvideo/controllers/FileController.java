package com.itvideo.controllers;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.itvideo.model.UserDao;
import com.itvideo.model.VideoDao;
import com.itvideo.model.exceptions.user.UserNotFoundException;
import com.itvideo.model.exceptions.video.VideoNotFoundException;
import com.itvideo.model.utils.Resources;

@Controller
public class FileController {
	
	@Autowired
	VideoDao vd;
	
	@Autowired
	UserDao ud;
	 	
	@RequestMapping(value = "/video/{videoId}", method = RequestMethod.GET)
	public void getVideo(@PathVariable("videoId") Long videoId, HttpServletResponse response) {
		try {
			long userId = vd.getUserId(videoId);
			String location = vd.getlocationUrl(videoId);
			String absolutePath = Resources.ROOT + File.separator + userId + File.separator + Resources.VIDEO_URL
					+ File.separator + location;
			File file = new File(absolutePath);
			try (FileInputStream fis = new FileInputStream(file);
				 BufferedInputStream bis = new BufferedInputStream(fis);
				 BufferedOutputStream output = new BufferedOutputStream(response.getOutputStream());) {
				
				response.setContentType("video/mp4");
				response.setHeader("Accept-Ranges", "bytes");
				response.setHeader("Connection", "Keep-Alive");
				response.setHeader("Content-Disposition", "attachment; filename=\"" + location + "\"");
				response.setContentLength((int) file.length());

				for (int data; (data = bis.read()) > -1;) {
					output.write(data);
				}
			}
		} catch (VideoNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	

	@RequestMapping(value = "/img/{userId}", method = RequestMethod.GET)
	public void getAvatar(@PathVariable("userId") Long userId, HttpServletResponse response) {
		try {
			String avatarUrl = ud.getAvatarUrl(userId);
			Resources.readAvatar(avatarUrl, userId, response);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UserNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@RequestMapping(value = "/thumbnail/{videoId}", method = RequestMethod.GET)
	public void getThumbnail(@PathVariable("videoId") long videoId, HttpServletResponse response) {
		try {
			String thumbnailUrl = vd.getThumbnailUrl(videoId);
			long userId = vd.getUserId(videoId);
			String absolutePath = Resources.ROOT + File.separator + userId + File.separator + Resources.VIDEO_URL
					+ File.separator + thumbnailUrl;
			File image = new File(absolutePath);
			FileInputStream fis = new FileInputStream(image);
			BufferedInputStream bis = new BufferedInputStream(fis);
			response.setContentType("image/png");
			BufferedOutputStream output = new BufferedOutputStream(response.getOutputStream());
			for (int data; (data = bis.read()) > -1;) {
				output.write(data);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (VideoNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
