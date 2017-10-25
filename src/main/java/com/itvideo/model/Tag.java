package com.itvideo.model;

import java.io.Serializable;

public class Tag implements Serializable{
	private static final long serialVersionUID = 1L;

	private long tag_id;

	private String tag;
	
	public Tag(long tag_id, String tag) {
		this.tag_id = tag_id;
		this.tag = tag;
	}
	
	public Tag(String tag) {
		this.tag = tag;
	}
	
	public String getTag() {
		return tag;
	}
	
	public long getTag_id() {
		return tag_id;
	}

	public void setTag_id(long tag_id) {
		this.tag_id = tag_id;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((tag == null) ? 0 : tag.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Tag other = (Tag) obj;
		if (tag == null) {
			if (other.tag != null)
				return false;
		} else if (!tag.equals(other.tag))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Tag [tag=" + tag + "]";
	}
}
