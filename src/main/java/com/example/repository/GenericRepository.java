package com.example.repository;

import java.util.List;

public interface GenericRepository<T, ID> {
	T getById(ID id);

	List<T> getAll();

	T save(T entity);

	T update(T entity);

	T deleteById(ID id);
}
