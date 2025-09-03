package com.example.model;

import java.util.ArrayList;
import java.util.List;

public class Writer {
	private long id;
	private String firstName;
	private String lastName;
	private List<Long> postIds;
	private Status status;

	public Writer(long id, String firstName, String lastName) {
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.postIds = new ArrayList<>();
		this.status = Status.ACTIVE;
	}

	public long getId() {
		return this.id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getFirstName() {
		return this.firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return this.lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public List<Long> getPostIds() {
		return this.postIds;
	}

	public void setPostIds(List<Long> postIds) {
		this.postIds = postIds;
	}

	public Status getStatus() {
		return this.status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}
}
