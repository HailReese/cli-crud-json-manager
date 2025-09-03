package com.example.model;

import java.util.ArrayList;
import java.util.List;

public class Post {
	private long id;
	private String title;
	private String content;
	private List<Long> labelIds;
	private Status status;

	public Post(long id, String title, String content) {
		this.id = id;
		this.title = title;
		this.content = content;
		this.labelIds = new ArrayList<>();
		this.status = Status.ACTIVE;
	}

	public long getId() {
		return this.id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getTitle() {
		return this.title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return this.content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public List<Long> getLabelIds() {
		return this.labelIds;
	}

	public void setLabelIds(List<Long> labelIds) {
		this.labelIds = labelIds;
	}

	public Status getStatus() {
		return this.status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}
}
