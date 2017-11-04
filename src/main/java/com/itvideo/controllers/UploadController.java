package com.itvideo.controllers;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

import javax.imageio.ImageIO;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpSession;

import org.jcodec.api.FrameGrab;
import org.jcodec.api.JCodecException;
import org.jcodec.common.DemuxerTrack;
import org.jcodec.common.io.FileChannelWrapper;
import org.jcodec.common.io.NIOUtils;
import org.jcodec.common.model.Picture;
import org.jcodec.containers.mp4.demuxer.MP4Demuxer;
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

	private static final int THUMBNAIL_WIDTH = 800;
	
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
			model.addAttribute("exception", "SQLException");
			model.addAttribute("getMessage", e.getMessage());
			return "error";
		} catch (UserNotFoundException e) {
			model.addAttribute("exception", "UserNotFoundException");
			model.addAttribute("getMessage", e.getMessage());
			return "error";
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
			@RequestParam("description") String description, 
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
				if (string.trim().isEmpty()) {
					continue;
				}
				tags.add(new Tag(string));
			}
			
			Video v = new Video(name, file.getOriginalFilename() , privacy, u.getUserId(), tags);
			v.setDescription(description);
			
			//MimeTypes allTypes = MimeTypes.getDefaultMimeTypes();
			//MimeType type = allTypes.forName(file.getContentType());
			//String ext = type.getExtension(); // .whatever
			
			String absolutePath  = WebInitializer.LOCATION + 
					File.separator + 
					u.getUserId() + 
					File.separator + 
					Resources.VIDEO_URL + 
					File.separator +
					file.getOriginalFilename();
			
			File f = new File(absolutePath);

			f.getParentFile().mkdirs(); 
			f.createNewFile();
			file.transferTo(f);

			
			//using jcodec library to get middle frame from the image
			FileChannelWrapper ch = NIOUtils.readableFileChannel(absolutePath);
			MP4Demuxer demuxer =  MP4Demuxer.createMP4Demuxer(ch);
			DemuxerTrack video_track = demuxer.getVideoTrack();
			int midFrame = (int) (video_track.getMeta().getTotalFrames() / 2);
			Picture picture = FrameGrab.getFrameFromFile(f, midFrame);
			BufferedImage bufferedImage = AWTUtil.toBufferedImage(picture);
			bufferedImage = resize(bufferedImage);
			
			ImageIO.write(bufferedImage, "png", new File(
					WebInitializer.LOCATION + 
					File.separator + 
					u.getUserId() + 
					File.separator + 
					Resources.VIDEO_URL + 
					File.separator +
					file.getOriginalFilename() +
					".png"));
			
			v.setThumbnailUrl(file.getOriginalFilename() + ".png");
			
			vd.createVideo(v);
			
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (VideoException e) {
			model.addAttribute("error", e.getMessage());
			return "upload";
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
	
	public static BufferedImage resize(BufferedImage img) {
		double ratio = (img.getWidth() * 1.0) / (img.getHeight() * 1.0);
		int newW = THUMBNAIL_WIDTH;
		int newH = (int)(newW / ratio);
	    Image tmp = img.getScaledInstance(newW, newH, Image.SCALE_SMOOTH);
	    BufferedImage dimg = new BufferedImage(newW, newH, BufferedImage.TYPE_INT_ARGB);

	    Graphics2D g2d = dimg.createGraphics();
	    g2d.drawImage(tmp, 0, 0, null);
	    g2d.dispose();

	    return dimg;
	}  
	
}
