package com.itvideo.controllers;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.itvideo.model.User;
import com.itvideo.model.UserDao;
import com.itvideo.model.Video;
import com.itvideo.model.VideoDao;
import com.itvideo.model.exceptions.user.UserException;
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
	public void getVideo(@PathVariable("videoId") long videoId, HttpServletResponse response) {
		try {
			Video video = vd.getVideo(videoId);
			Resources.readVideo(video.getLocationUrl(), video.getUserId(), response);
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
	public void getAvatar(@PathVariable("userId") long userId, HttpServletResponse response) {
			try {
				User user = ud.getUser(userId);
				Resources.readAvatar(user.getAvatarUrl(), userId, response);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (UserNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (UserException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		
	}
}
