package com.itvideo.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.itvideo.model.exceptions.tags.TagNotFoundException;
import com.itvideo.model.utils.DBConnection;

@Component
public class TagDao {

	private Connection con;
	
	@Autowired
    private void initField() {
		 con = DBConnection.VIDEOS.getConnection();
    }

	public boolean existTag(String tag) throws SQLException {
		String sql = "SELECT tag FROM tags WHERE tag = ?";
		try (PreparedStatement ps = con.prepareStatement(sql);) {
			ps.setString(1, tag);
			try (ResultSet rs = ps.executeQuery();) {
				if (rs.next()) {
					return true;
				}
				return false;
			}
		}
	}

	public Tag getTag(String tag) throws SQLException, TagNotFoundException {
		String sql = "SELECT * FROM tags WHERE tag = ?";
		try (PreparedStatement ps = con.prepareStatement(sql);) {
			ps.setString(1, tag);
			try (ResultSet rs = ps.executeQuery();) {
				if (rs.next()) {
					return new Tag(
							rs.getLong("tag_id"), 
							rs.getString("tag"));
				}
				throw new TagNotFoundException();	
			}
		}
	}

	public void insertVideoTags(Video v) throws SQLException, TagNotFoundException {
		Set<Tag> tags = v.getTags();
		
		String sql = "INSERT INTO videos_has_tags (video_id, tag_id) VALUES (?, ?)";

		for (Tag tag : tags) {
			insertTag(tag.getTag());
			tag = getTag(tag.getTag());
			
			try (PreparedStatement videos_has_tags = con.prepareStatement(sql);) {
				videos_has_tags.setLong(1, v.getVideoId());
				videos_has_tags.setLong(2, tag.getTag_id());
				videos_has_tags.executeUpdate();
			}
		}
	}

	public void insertTag(String tag) throws SQLException {
		if (!existTag(tag)) {
			String sql = "INSERT INTO tags (tag) VALUES (?)";
			try (PreparedStatement ps = con.prepareStatement(sql);) {
				ps.setString(1, tag);
				ps.executeUpdate();
			}
		}
	}

}
