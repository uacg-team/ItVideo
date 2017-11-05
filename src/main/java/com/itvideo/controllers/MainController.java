package com.itvideo.controllers;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

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

import com.itvideo.model.CommentDao;
import com.itvideo.model.Playlist;
import com.itvideo.model.PlaylistDao;
import com.itvideo.model.User;
import com.itvideo.model.UserDao;
import com.itvideo.model.Video;
import com.itvideo.model.VideoDao;
import com.itvideo.model.exceptions.user.UserException;
import com.itvideo.model.exceptions.user.UserNotFoundException;
import com.itvideo.model.exceptions.video.VideoException;
import com.itvideo.model.exceptions.video.VideoNotFoundException;

@Controller
public class MainController {
	@Autowired
	VideoDao vd;
	
	@Autowired
	UserDao ud;
	
	@Autowired
	CommentDao cd;
	
	@Autowired
	PlaylistDao pd;
	
	private static final int VIDEOS_PER_PAGE = 4;
	
	@RequestMapping(value="/search", method = RequestMethod.GET)
	public String search(
			@RequestParam("search") String search,
			@RequestParam("searchParam") String searchParam,
			HttpServletRequest request, 
			HttpSession session, 
			Model model) {
		session.setAttribute("searchParam", searchParam);
		try {
			switch (searchParam) {
			case "users":
				request.setAttribute("searchResult", ud.searchUser(search));
				return "search";
			case "videos":
				request.setAttribute("videos", vd.searchVideo(search));
				return "search";
			case "playlists":
				request.setAttribute("searchResult", pd.searchPlaylist(search));
				return "search";
			default:
				return "/main";
			}
		} catch (SQLException e) {
			model.addAttribute("exception", "SQLException");
			model.addAttribute("getMessage", e.getMessage());
			return "error";
		} catch (VideoException e) {
			model.addAttribute("exception", "VideoException");
			model.addAttribute("getMessage", e.getMessage());
			return "error";
		} catch (UserException e) {
			model.addAttribute("exception", "UserException");
			model.addAttribute("getMessage", e.getMessage());
			return "error";
		}
	}

	@RequestMapping(value="/search/tag/{tag}", method = RequestMethod.GET)
	public String searchTag(HttpServletRequest request, Model model, @PathVariable("tag") String tag) {
		try {
			List<Video> videos = vd.getVideos(tag);
			for (Video video : videos) {
				video.setUserName(vd.getUserName(video.getUserId()));
				video.setPrivacy(vd.getPrivacy(video.getPrivacyId()));
			}
			request.setAttribute("videos", videos);
			return "main";
		} catch (SQLException e) {
			model.addAttribute("exception", "SQLException");
			model.addAttribute("getMessage", e.getMessage());
			return "error";
		} catch (UserNotFoundException e) {
			model.addAttribute("exception", "UserNotFoundException");
			model.addAttribute("getMessage", e.getMessage());
			return "error";
		}
	}
	
	@RequestMapping(value="/main/sort/{param}", method = RequestMethod.GET)
	public String sort(HttpSession session,
			@PathVariable("param") String param) {
		session.setAttribute("sort",param);
		return "redirect:/main";
	}
	
	
	@RequestMapping(value = "/index", method = RequestMethod.GET)
	public String index() {
		return "redirect:main";
	}

	@RequestMapping(value = "/main", method = RequestMethod.GET)
	public String main(HttpSession session, Model model, HttpServletRequest request, HttpServletResponse response) {
		try {
			int pageNumber = 1;
			response.setHeader("Accept-Ranges", "bytes");
			response.setHeader("Connection", "Keep-Alive");
			
			String param = (String) session.getAttribute("sort");
			List<Video> videos = null;
			if (param == null) {
				
				videos = vd.getAllVideoOrderByDate(pageNumber, VIDEOS_PER_PAGE);
				if (videos != null) {
					for (Video video : videos) {
						video.setUserName(vd.getUserName(video.getUserId()));
						video.setPrivacy(vd.getPrivacy(video.getPrivacyId()));
					}
				}
				session.setAttribute("sort", "date");
				
				/*pagination*/
					int totalPages = (int) Math.ceil(vd.getPublicVideosSize() * 1.0 / VIDEOS_PER_PAGE);
					model.addAttribute("totalPages", totalPages);
					model.addAttribute("pageid", 1);
					model.addAttribute("list", videos);
				/*pagination*/
				
				//request.setAttribute("videos", videos);
				return "main";
			}else {
				switch (param) {
				case "date":
					session.setAttribute("sort", "date");
					videos = vd.getAllVideoOrderByDate(pageNumber, VIDEOS_PER_PAGE);
					break;
				case "like":
					session.setAttribute("sort", "like");
					videos = vd.getAllVideoOrderByLikes(pageNumber, VIDEOS_PER_PAGE);
					break;
				case "view":
					session.setAttribute("sort", "view");
					videos = vd.getAllVideoOrderByViews(pageNumber, VIDEOS_PER_PAGE);
					break;
				default:
					session.setAttribute("sort", "date");
					videos = vd.getAllVideoOrderByDate(pageNumber, VIDEOS_PER_PAGE);
					break;
				}
				
				for (Video video : videos) {
					video.setUserName(vd.getUserName(video.getUserId()));
					video.setPrivacy(vd.getPrivacy(video.getPrivacyId()));
				}

				/*pagination*/
				int totalPages = (int) Math.ceil(vd.getPublicVideosSize() * 1.0 / VIDEOS_PER_PAGE);
				model.addAttribute("totalPages", totalPages);
				model.addAttribute("pageid", 1);
				model.addAttribute("list", videos);
				/*pagination*/
				
				return "main";
			}
		} catch (SQLException e) {
			model.addAttribute("exception", "SQLException");
			model.addAttribute("getMessage", e.getMessage());
			return "error";
		} catch (UserNotFoundException e) {
			model.addAttribute("exception", "UserNotFoundException");
			model.addAttribute("getMessage", e.getMessage());
			return "error";
		}
	}
	
	@RequestMapping(value = "/main/page", method = RequestMethod.GET )
	public String videosPagePost(
			@RequestParam("pageid") int pageid, 
			Model model,
			HttpSession session,
			HttpServletRequest request,
			HttpServletResponse response) {
		
		String param = (String) session.getAttribute("sort");
		List<Video> videos = null;
		try {
			int totalPages = (int) Math.ceil(vd.getPublicVideosSize() * 1.0 / VIDEOS_PER_PAGE);
			
			if (pageid > totalPages) {
				pageid = totalPages;
			}
			
			int offset = 0;
			
			if (pageid == 1) {
				offset = 0;
			} else {
				offset = (pageid - 1) * VIDEOS_PER_PAGE;
			}
			
			switch (param) {
			case "date":
				session.setAttribute("sort", "date");
				videos = vd.getAllVideoOrderByDate(offset, VIDEOS_PER_PAGE);
				break;
			case "like":
				session.setAttribute("sort", "like");
				videos = vd.getAllVideoOrderByLikes(offset, VIDEOS_PER_PAGE);
				break;
			case "view":
				session.setAttribute("sort", "view");
				videos = vd.getAllVideoOrderByViews(offset, VIDEOS_PER_PAGE);
				break;
			default:
				session.setAttribute("sort", "date");
				videos = vd.getAllVideoOrderByDate(offset, VIDEOS_PER_PAGE);
				break;
			}

			for (Video video : videos) {
				video.setUserName(vd.getUserName(video.getUserId()));
				video.setPrivacy(vd.getPrivacy(video.getPrivacyId()));
			}
			
			// for paginations
			model.addAttribute("totalPages", totalPages);
			model.addAttribute("pageid", pageid);
			model.addAttribute("list", videos);
			
			return "main";
		} catch (SQLException e) {
			model.addAttribute("exception", "SQLException");
			model.addAttribute("getMessage", e.getMessage());
			return "error";
		} catch (UserNotFoundException e) {
			model.addAttribute("exception", "UserNotFoundException");
			model.addAttribute("getMessage", e.getMessage());
			return "error";
		}
	}
	
	@RequestMapping(value="/player/{videoId}", method = RequestMethod.GET)
	public String player(Model model, 
			@PathVariable("videoId") long videoId, 
			HttpSession session, 
			HttpServletResponse response) {
		try {
			if (!vd.existsVideo(videoId)) {
				model.addAttribute("exception", "VideoNotFoundException");
				model.addAttribute("getMessage", "Video Not Found");
				return "error";
			}
			
			User user = (User) session.getAttribute("user");
			long userId = 0;
			if (user != null) {
				userId = user.getUserId();
			}
			
			Video video = vd.getVideoForPlayer(videoId, userId);

			//userId - logged user a.k.a. me
			//video.getUserId() - videoOwnerId 
			if (user != null) {
				boolean follow = ud.isFollowing(userId, video.getUserId());
				System.out.println(follow);
				model.addAttribute("follow", follow);
			}
			
			Set<Video> related = vd.getRelatedVideos(videoId);
			
			vd.increaseViews(videoId);
			
			model.addAttribute("mainVideo", video);
			model.addAttribute("videos", related);
			model.addAttribute("countComments",cd.getNumberOfCommentsForVideo(videoId));
			model.addAttribute("date",LocalDateTime.now().toString());
			if (session.getAttribute("user") != null) {
				List<Playlist> playlists = null;
				playlists = pd.getPlaylistForUserWithStatus(userId,videoId);
				model.addAttribute("myPlaylists", playlists);
			}
			return "player";
		} catch (SQLException e) {
			model.addAttribute("exception", "SQLException");
			model.addAttribute("getMessage", e.getMessage());
			return "error";
		} catch (VideoNotFoundException e) {
			model.addAttribute("exception", "VideoNotFoundException");
			model.addAttribute("getMessage", e.getMessage());
			return "error";
		} catch (UserException e) {
			model.addAttribute("exception", "MessagingException");
			model.addAttribute("getMessage", e.getMessage());
			return "error";
		}
	}
}
