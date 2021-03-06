package com.itvideo.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import com.itvideo.model.exceptions.video.VideoException;
import com.itvideo.model.interfaces.Searchable;



public class Video implements Serializable, Searchable {

	private static final long serialVersionUID = 1L;
	
	private static final int MIN_NAME_LENGTH = 3;
	private static final int MAX_NAME_LENGTH = 40;
	
	private long videoId;
	private String name;
	private int views;
	private LocalDateTime date;
	private String locationUrl;
	private long userId;
	private String thumbnailUrl;
	private String description;
	private long privacyId;

	private String userName;
	private String privacy;
	
	private int likes;
	private int dislikes;
	private User owner; 
	
	private int vote;

	private Set<Tag> tags = new HashSet<>();

	Video(long videoId, String name, int views, LocalDateTime date, String locationUrl, long userId,
			String thumbnailUrl, String description, long privacyId, Set<Tag> tags) {
		this.videoId = videoId;
		this.name = name;
		this.views = views;
		this.date = date;
		this.locationUrl = locationUrl;
		this.userId = userId;
		this.thumbnailUrl = thumbnailUrl;
		this.description = description;
		this.privacyId = privacyId;
		this.tags = tags;
	}
	
	public Video(String name, String locationUrl, long privacyId, long userId, Set<Tag> tags)
			throws VideoException {
		setName(name);
		setLocationUrl(locationUrl);
		setPrivacyId(privacyId);
		setUserId(userId);
		setTags(tags);

		this.date = LocalDateTime.now();
		this.views = 0;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Video other = (Video) obj;
		if (videoId != other.videoId)
			return false;
		return true;
	}

	public LocalDateTime getDate() {
		return this.date;
	}

	public String getDescription() {
		return this.description;
	}

	public int getDislikes() {
		return dislikes;
	}

	public int getLikes() {
		return likes;
	}

	public String getLocationUrl() {
		return locationUrl;
	}
	
	public String getName() {
		return this.name;
	}
	
	public User getOwner() {
		return owner;
	}
	
	public String getPrivacy() {
		return privacy;
	}
	
	public long getPrivacyId() {
		return this.privacyId;
	}
	
	public Set<Tag> getTags() {
		return this.tags;
	}


	public String getThumbnailUrl() {
		return this.thumbnailUrl;
	}

	public long getUserId() {
		return this.userId;
	}

	public String getUserName() {
		return userName;
	}

	public long getVideoId() {
		return this.videoId;
	}

	public int getViews() {
		return this.views;
	}

	public int getVote() {
		return vote;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (videoId ^ (videoId >>> 32));
		return result;
	}

	public void removeTag(Tag t) {
		this.tags.remove(t);
	}

	public void setDate(LocalDateTime date) {
		this.date = date;
	}

	public void setDescription(String description) throws VideoException {
		this.description = description;
	}

	public void setDislikes(int dislikes) {
		this.dislikes = dislikes;
	}

	public void setLikes(int likes) {
		this.likes = likes;
	}

	public void setLocationUrl(String locationUrl) throws VideoException {
		if (name == null || name.trim().isEmpty()) {
			throw new VideoException(VideoException.INVALID_LOCATION);
		}
		this.locationUrl = locationUrl;
	}

	public void setName(String name) throws VideoException {
		if (name == null || name.trim().isEmpty()) {
			throw new VideoException(VideoException.INVALID_NAME);
		}
		if (name.trim().length() < MIN_NAME_LENGTH && name.trim().length() > MAX_NAME_LENGTH) {
			throw new VideoException(VideoException.INVALID_NAME_LENGTH);
		}
		this.name = name;
	}

	public void setOwner(User owner) {
		this.owner = owner;
	}

	public void setPrivacy(String privacy) {
		this.privacy = privacy;
	}
	
	public void setPrivacyId(long privacyId) throws VideoException {
		this.privacyId = privacyId;
	}
	
	public void setTags(Set<Tag> tags) {
		this.tags = tags;
	}

	public void setThumbnailUrl(String thumbnailUrl) throws VideoException {
		if (thumbnailUrl == null || thumbnailUrl.isEmpty()) {
			throw new VideoException(VideoException.INVALID_THUMBNAIL);
		}
		this.thumbnailUrl = thumbnailUrl;
	}

	public void setUserId(long userId) throws VideoException {
		this.userId = userId;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public void setVideoId(long videoId) {
		this.videoId = videoId;
	}
	
	public void setViews(int views) {
		this.views = views;
	}

	public void setVote(int vote) {
		this.vote = vote;
	}

	@Override
	public String toString() {
		return "Video [video_id=" + videoId + ", name=" + name + ", views=" + views + ", date=" + date
				+ ", location_url=" + locationUrl + ", user_id=" + userId + ", thumbnail_url=" + thumbnailUrl
				+ ", description=" + description + ", privacy_id=" + privacyId + ", tags=" + tags + "]";
	}

}
