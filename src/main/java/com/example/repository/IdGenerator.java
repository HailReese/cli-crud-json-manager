package com.example.repository;

public class IdGenerator {
	private long maxId;

	public IdGenerator(Long maxId) {
		this.maxId = maxId;
	}

	public Long generate() {
		return ++maxId;
	}
}
