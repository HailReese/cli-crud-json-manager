package com.example.model;

public class Label {
	private long id;
	private String name;
	private Status status;

	public Label(long id, String name) {
		this.id = id;
		this.name = name;
		this.status = Status.ACTIVE;
	}

	public long getId() {
		return this.id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Status getStatus() {
		return this.status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

}
