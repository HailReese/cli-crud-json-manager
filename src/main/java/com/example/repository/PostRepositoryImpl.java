package com.example.repository;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import com.example.exceptions.DataAccessException;
import com.example.model.Post;
import com.example.model.Status;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class PostRepositoryImpl implements PostRepository {

	private static final String FILE_PATH = Paths.get("data", "PostsRepo.json").toString();
	private static final Type LIST_TYPE_TOKEN = new TypeToken<List<Post>>() {
	}.getType();
	private static final Gson GSON = new Gson();
	private final IdGenerator ID_GENERATOR;

	public PostRepositoryImpl() {
		this.ID_GENERATOR = new IdGenerator(getMaxId());
	}

	@Override
	public Post getById(Long id) {
		List<Post> posts = readFromJson();
		return posts.stream()
				.filter(e -> e.getId() == id)
				.filter(e -> e.getStatus().equals(Status.ACTIVE))
				.findFirst()
				.orElse(null);
	}

	@Override
	public List<Post> getAll() {
		List<Post> posts = readFromJson();
		return posts.stream()
				.filter(e -> e.getStatus().equals(Status.ACTIVE))
				.toList();
	}

	@Override
	public Post save(Post entity) {
		List<Post> posts = readFromJson();

		if (entity.getId() == 0)
			entity.setId(ID_GENERATOR.generate());

		posts.add(entity);

		writeToJson(posts);

		return entity;
	}

	@Override
	public Post update(Post entity) {
		List<Post> posts = readFromJson();

		for (int i = 0; i < posts.size(); i++) {
			if (posts.get(i).getId() == entity.getId()) {
				posts.set(i, entity);
				writeToJson(posts);
				return entity;
			}
		}
		return null;
	}

	@Override
	public Post deleteById(Long id) {
		List<Post> posts = readFromJson();

		for (int i = 0; i < posts.size(); i++) {
			if (posts.get(i).getId() == id) {
				Post post = posts.get(i);
				post.setStatus(Status.DELETED);
				posts.set(i, post);
				writeToJson(posts);
				return post;
			}
		}
		return null;
	}

	private List<Post> readFromJson() {
		List<Post> posts;

		// checking an existence of the json data file
		if (!Files.exists(Paths.get(FILE_PATH))) {
			try {
				Files.createFile(Paths.get(FILE_PATH));
			} catch (IOException e) {
				e.printStackTrace();
			}
			posts = new ArrayList<>();
		}

		// reading tasks from data file
		try (Reader reader = new FileReader(FILE_PATH)) {
			posts = GSON.fromJson(reader, LIST_TYPE_TOKEN);
		} catch (IOException e) {
			posts = new ArrayList<>();
		}

		// checking for null
		if (posts == null) {
			posts = new ArrayList<>();
		}
		return posts;
	}

	private void writeToJson(List<Post> posts) {
		try {
			Files.write(Paths.get(FILE_PATH), GSON.toJson(posts, LIST_TYPE_TOKEN).getBytes());
		} catch (IOException e) {
			throw new DataAccessException("Ошибка записи", e);
		}

	}

	private long getMaxId() {
		List<Post> posts = readFromJson();
		if (posts.size() > 0) {
			return posts.get(posts.size() - 1).getId();
		}
		return 0;
	}

}
