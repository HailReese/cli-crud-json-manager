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
import com.example.model.Status;
import com.example.model.Writer;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class WriterRepositoryImpl implements WriterRepository {

	private static final String FILE_PATH = Paths.get("data", "WritersRepo.json").toString();
	private static final Type LIST_TYPE_TOKEN = new TypeToken<List<Writer>>() {
	}.getType();
	private static final Gson GSON = new Gson();
	private final IdGenerator ID_GENERATOR;

	public WriterRepositoryImpl() {
		this.ID_GENERATOR = new IdGenerator(getMaxId());
	}

	@Override
	public Writer getById(Long id) {
		List<Writer> writers = readFromJson();
		return writers.stream()
				.filter(e -> e.getId() == id)
				.filter(e -> e.getStatus().equals(Status.ACTIVE))
				.findFirst()
				.orElse(null);
	}

	@Override
	public List<Writer> getAll() {
		List<Writer> writers = readFromJson();
		return writers.stream()
				.filter(e -> e.getStatus().equals(Status.ACTIVE))
				.toList();
	}

	@Override
	public Writer save(Writer entity) {
		List<Writer> writers = readFromJson();

		if (entity.getId() == 0)
			entity.setId(ID_GENERATOR.generate());

		writers.add(entity);

		writeToJson(writers);

		return entity;
	}

	@Override
	public Writer update(Writer entity) {
		List<Writer> writers = readFromJson();

		for (int i = 0; i < writers.size(); i++) {
			if (writers.get(i).getId() == entity.getId()) {
				writers.set(i, entity);
				writeToJson(writers);
				return entity;
			}
		}
		return null;
	}

	@Override
	public Writer deleteById(Long id) {
		List<Writer> writers = readFromJson();

		for (int i = 0; i < writers.size(); i++) {
			if (writers.get(i).getId() == id) {
				Writer writer = writers.get(i);
				writer.setStatus(Status.DELETED);
				writers.set(i, writer);
				writeToJson(writers);
				return writer;
			}
		}
		return null;
	}

	private List<Writer> readFromJson() {
		List<Writer> writers;

		// checking an existence of the json data file
		if (!Files.exists(Paths.get(FILE_PATH))) {
			try {
				Files.createFile(Paths.get(FILE_PATH));
			} catch (IOException e) {
				e.printStackTrace();
			}
			writers = new ArrayList<>();
		}

		// reading tasks from data file
		try (Reader reader = new FileReader(FILE_PATH)) {
			writers = GSON.fromJson(reader, LIST_TYPE_TOKEN);
		} catch (IOException e) {
			writers = new ArrayList<>();
		}

		// checking for null
		if (writers == null) {
			writers = new ArrayList<>();
		}
		return writers;
	}

	private void writeToJson(List<Writer> writers) {
		try {
			Files.write(Paths.get(FILE_PATH), GSON.toJson(writers, LIST_TYPE_TOKEN).getBytes());
		} catch (IOException e) {
			throw new DataAccessException("Ошибка записи", e);
		}

	}

	private long getMaxId() {
		List<Writer> writers = readFromJson();
		if (writers.size() > 0) {
			return writers.get(writers.size() - 1).getId();
		}
		return 0;
	}
}
