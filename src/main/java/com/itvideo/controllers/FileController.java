package com.itvideo.controllers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.SQLException;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

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
	
	//start streaming
	
	@RequestMapping(value = "/videoStream/{videoId}", method = RequestMethod.GET)
	public StreamingResponseBody getStreamingVideo(@PathVariable("videoId") Long videoId, HttpServletResponse response) {
		try {
			Video video = vd.getVideo(videoId);
			File videoFile = new File(
					Resources.ROOT + 
					File.separator + 
					video.getUserId() + 
					File.separator + 
					Resources.VIDEO_URL + 
					File.separator + 
					video.getLocationUrl());
			final InputStream videoFileStream = new FileInputStream(videoFile);
			return (os) -> {
				readAndWrite(videoFileStream, os);
			};
		} catch (VideoNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	private void readAndWrite(final InputStream is, OutputStream os)
			throws IOException {
		byte[] data = new byte[2048];
		int read = 0;
		while ((read = is.read(data)) > 0) {
			os.write(data, 0, read);
		}
		os.flush();
	}
	
	//end streaming
	
	@RequestMapping(value = "/video/{videoId}", method = RequestMethod.GET)
	public void getVideo(@PathVariable("videoId") Long videoId, HttpServletResponse response) {
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
	public void getAvatar(@PathVariable("userId") Long userId, HttpServletResponse response) {
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
	
	@RequestMapping(value = "/thumbnail/{videoId}", method = RequestMethod.GET)
	public void getThumbnail(@PathVariable("videoId") long videoId, HttpServletResponse response) {
		try {
			Video video = vd.getVideo(videoId);
			Resources.readVideo(video.getThumbnailUrl(), video.getUserId(), response);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (VideoNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
