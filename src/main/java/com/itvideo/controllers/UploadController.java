package com.itvideo.controllers;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

import javax.imageio.ImageIO;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpSession;

import org.apache.tika.mime.MimeType;
import org.apache.tika.mime.MimeTypeException;
import org.apache.tika.mime.MimeTypes;
import org.jcodec.api.FrameGrab;
import org.jcodec.api.JCodecException;
import org.jcodec.common.model.Picture;
import org.jcodec.scale.AWTUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.itvideo.WebInitializer;
import com.itvideo.model.Tag;
import com.itvideo.model.User;
import com.itvideo.model.UserDao;
import com.itvideo.model.Video;
import com.itvideo.model.VideoDao;
import com.itvideo.model.exceptions.tags.TagNotFoundException;
import com.itvideo.model.exceptions.user.UserException;
import com.itvideo.model.exceptions.user.UserNotFoundException;
import com.itvideo.model.exceptions.video.VideoException;
import com.itvideo.model.utils.Resources;

@Controller
@MultipartConfig
public class UploadController {

	@Autowired
	VideoDao vd;
	
	@Autowired
	UserDao ud;
	
	
	
	@RequestMapping(value="/uploadAvatar", method = RequestMethod.POST)
	public String uploadAvatar(HttpSession session,Model model,@RequestParam("avatar") MultipartFile avatar) {
		
		User u = (User) session.getAttribute("user");
		try {
			
			if (!avatar.getContentType().equals("image/png")) {
				model.addAttribute("error", "Wrong File Type");
				return "updateUser";
			}
			
			File f = new File(
					WebInitializer.LOCATION + 
					File.separator + 
					u.getUserId() + 
					File.separator + 
					Resources.IMAGE_URL + 
					File.separator +
					avatar.getOriginalFilename());
			
			avatar.transferTo(f);
			
			u.setAvatarUrl(avatar.getOriginalFilename());
			ud.updateUser(u);
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (UserException e) {
			e.printStackTrace();
		} catch (UserNotFoundException e) {
			e.printStackTrace();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "redirect:/updateUser/" + u.getUserId();
	}

	@RequestMapping(value="/uploadVideo", method = RequestMethod.GET)
	public String uploadVideoGet() {
		return "upload";
	}
	
	@RequestMapping(value="/uploadVideo", method = RequestMethod.POST)
	public String uploadVideoPost(
			HttpSession session,
			Model model, 
			@RequestParam("newVideo") MultipartFile file, 
			@RequestParam("name") String name, 
			@RequestParam("tags") String allTags, 
			@RequestParam("privacy") long privacy) {
		
		User u = (User) session.getAttribute("user");
		if (u == null) {
			return "redirect:login";
		}
		try {
			if (!file.getContentType().equals("video/mp4")) {
				model.addAttribute("error", "Wrong File Type");
				return "upload";
			}
			
			String[] inputTags = allTags.split("\\s+");
			Set<Tag> tags = new HashSet<>();
			for (String string : inputTags) {
				tags.add(new Tag(string));
			}
		
			//MimeTypes allTypes = MimeTypes.getDefaultMimeTypes();
			//MimeType type = allTypes.forName(file.getContentType());
			//String ext = type.getExtension(); // .whatever
			
			File f = new File(
					WebInitializer.LOCATION + 
					File.separator + 
					u.getUserId() + 
					File.separator + 
					Resources.VIDEO_URL + 
					File.separator,
					file.getOriginalFilename());

			f.getParentFile().mkdirs(); 
			f.createNewFile();
			file.transferTo(f);
			
			int frameNumber = 40;
			Picture picture = FrameGrab.getFrameFromFile(f, frameNumber);
		
			BufferedImage bufferedImage = AWTUtil.toBufferedImage(picture);
			
			bufferedImage = resize(bufferedImage, 320, 240);
				
			ImageIO.write(bufferedImage, "png", new File(
					WebInitializer.LOCATION + 
					File.separator + 
					u.getUserId() + 
					File.separator + 
					Resources.VIDEO_URL + 
					File.separator +
					file.getOriginalFilename() +
					".png"));
			
			
			Video v = new Video(name, file.getOriginalFilename() , privacy, u.getUserId(), tags);
			v.setThumbnailUrl(file.getOriginalFilename() + ".png");
			
			vd.createVideo(v);
			
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (VideoException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TagNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JCodecException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//was this way but when redirected to main video is not uploaded
//		return "redirect:/main";
		return "redirect:main";
	}
	
	public static BufferedImage resize(BufferedImage img, int newW, int newH) { 
	    Image tmp = img.getScaledInstance(newW, newH, Image.SCALE_SMOOTH);
	    BufferedImage dimg = new BufferedImage(newW, newH, BufferedImage.TYPE_INT_ARGB);

	    Graphics2D g2d = dimg.createGraphics();
	    g2d.drawImage(tmp, 0, 0, null);
	    g2d.dispose();

	    return dimg;
	}  
	
}
