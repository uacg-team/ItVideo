package com.itvideo.model.utils;

import static org.hamcrest.CoreMatchers.nullValue;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.SQLException;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;

import org.jcodec.api.FrameGrab;
import org.jcodec.api.JCodecException;
import org.jcodec.common.model.Picture;
import org.jcodec.scale.AWTUtil;

import com.itvideo.model.User;
import com.itvideo.model.Video;
import com.itvideo.model.exceptions.user.UserException;
import com.itvideo.model.exceptions.user.UserNotFoundException;
import com.itvideo.model.exceptions.video.VideoException;


public abstract class Resources {

	public static final String ROOT = "C:" + File.separator + "res";
	public static final String VIDEO_URL = "videos";
	public static final String IMAGE_URL = "images";
	
	private static final int FRAME_NUMBER = 50;

	
	/**
	 * Example: C:\\Users\\YouTube-PNG-Photos.png
	 * @param absolutePath
	 * @param response
	 * @throws IOException
	 */
	public static void readFromFile(String absolutePath, HttpServletResponse response) throws IOException {
		File myFile = new File(absolutePath);
		try (OutputStream out = response.getOutputStream()) {
			Path path = myFile.toPath();
			Files.copy(path, out);
			out.flush();
		}
	}

	/**
	 * @param absolutePath:
	 *            C:\\Users\\YouTube-PNG-Photos.png
	 * @param inStream
	 * @throws IOException
	 */
	public static void writeFile(String absolutePath, InputStream inStream) throws IOException {
		File myFile = new File(absolutePath);
		if (!myFile.exists()) {
			myFile.createNewFile();
		}
		Files.copy(inStream, myFile.toPath(),StandardCopyOption.REPLACE_EXISTING);
		inStream.close();
	}

	public static void writeAvatar(User u, Part image) throws IOException, SQLException, UserException, UserNotFoundException {
		String fileName = Paths.get(image.getSubmittedFileName()).getFileName().toString();
		String absolutePath = Resources.ROOT + File.separator + u.getUserId()+ File.separator + Resources.IMAGE_URL+ File.separator + fileName;
//		System.out.println("Resources:writeImage:absolutePath:"+absolutePath);
		write(image, absolutePath);
	}
	
	public static void writeVideo(User u, Part file) throws IOException {
		String fileName = Paths.get(file.getSubmittedFileName()).getFileName().toString();
		String absolutePath = Resources.ROOT + File.separator + u.getUserId()+ File.separator + Resources.VIDEO_URL+ File.separator + fileName;
//		System.out.println("Resources:writeVideo:absolutePath:"+absolutePath);
		write(file, absolutePath);
		//String path = Resources.ROOT + File.separator + u.getUserId()+ File.separator + Resources.VIDEO_URL+ File.separator;
		//generateThumbnail(fileName, path);
	}
	
	private static void write(Part file, String absolutePath) throws IOException {
		InputStream inputStream = file.getInputStream();
		File myFile = new File(absolutePath);
		if (!myFile.exists()) {
			myFile.mkdirs();
			myFile.createNewFile();
		}
		Files.copy(inputStream, myFile.toPath(),StandardCopyOption.REPLACE_EXISTING);
		inputStream.close();
		inputStream = null;
		System.gc();
	}
	
	private static void read(String absolutePath, Long userId, HttpServletResponse response) {
		File myFile = new File(absolutePath);
		try (OutputStream out = response.getOutputStream()) {
			Files.copy(myFile.toPath(), out);
			out.flush();
			out.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void readAvatar(String filename, Long userId, HttpServletResponse response) throws IOException {
		String absolutePath = Resources.ROOT + File.separator + userId + File.separator + Resources.IMAGE_URL
				+ File.separator + filename;
//		System.out.println("Resources:readImage:absolutePath:"+absolutePath);
		read(absolutePath, userId, response);
	}

	public static void readVideo(String url, Long userId, HttpServletResponse response) throws IOException {
		String absolutePath = Resources.ROOT + File.separator + userId + File.separator + Resources.VIDEO_URL+ File.separator + url;
//		System.out.println("Resources:readVideo:absolutePath:"+absolutePath);
		read(absolutePath, userId, response);
	}
	
	public static void main(String[] args) throws VideoException {
		Video v = new Video("name", "SampleVideo_1280x720_10mb.mp4", 1, 1, null);
		deleteVideo(v);
	}

	public static void deleteVideo(Video v) {
		String absoluteVideoPath = Resources.ROOT + File.separator +v.getUserId() + File.separator + Resources.VIDEO_URL+ File.separator + v.getLocationUrl();
		String absoluteThumbnailPath = Resources.ROOT + File.separator +v.getUserId() + File.separator + Resources.VIDEO_URL+ File.separator + v.getThumbnailUrl();
		
		File video = new File(absoluteVideoPath);
		File thumbnail = new File(absoluteThumbnailPath);
		
		try {
			video.createNewFile();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		thumbnail.delete();
		video.delete();
	}

	public static void initAvatar(User u, HttpSession session) throws IOException {
		try(InputStream in = session.getServletContext().getResourceAsStream("/static/img/avatar.png")){
			String absolutePath = Resources.ROOT + File.separator + u.getUserId() + File.separator + Resources.IMAGE_URL
					+ File.separator + "avatar.png";
			File myFile = new File(absolutePath);
			
			myFile.getParentFile().mkdirs(); 
			myFile.createNewFile();
			
			Files.copy(in, myFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
		}
	}
}
