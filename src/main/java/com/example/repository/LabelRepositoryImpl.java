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
import com.example.model.Label;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class LabelRepositoryImpl implements LabelRepository {
	private static final String FILE_PATH = Paths.get("data", "LabelsRepo.json").toString();
	private static final Type LIST_TYPE_TOKEN = new TypeToken<List<Label>>() {
	}.getType();
	private static final Gson GSON = new Gson();
	private final IdGenerator ID_GENERATOR;

	public LabelRepositoryImpl() {
		this.ID_GENERATOR = new IdGenerator(getMaxId());
	}

	@Override
	public Label getById(Long id) {
		List<Label> labels = readFromJson();
		return labels.stream()
				.filter(e -> e.getId() == id)
				.filter(e -> e.getStatus().equals(Status.ACTIVE))
				.findFirst()
				.orElse(null);
	}

	@Override
	public List<Label> getAll() {
		List<Label> labels = readFromJson();
		return labels.stream()
				.filter(e -> e.getStatus().equals(Status.ACTIVE))
				.toList();
	}

	@Override
	public Label save(Label entity) {
		List<Label> labels = readFromJson();

		if (entity.getId() == 0)
			entity.setId(ID_GENERATOR.generate());

		labels.add(entity);

		writeToJson(labels);

		return entity;
	}

	@Override
	public Label update(Label entity) {
		List<Label> labels = readFromJson();

		for (int i = 0; i < labels.size(); i++) {
			if (labels.get(i).getId() == entity.getId()) {
				labels.set(i, entity);
				writeToJson(labels);
				return entity;
			}
		}
		return null;
	}

	@Override
	public Label deleteById(Long id) {
		List<Label> labels = readFromJson();

		for (int i = 0; i < labels.size(); i++) {
			if (labels.get(i).getId() == id) {
				Label label = labels.get(i);
				label.setStatus(Status.DELETED);
				labels.set(i, label);
				writeToJson(labels);
				return label;
			}
		}
		return null;
	}

	private List<Label> readFromJson() {
		List<Label> labels;

		// checking an existence of the json data file
		if (!Files.exists(Paths.get(FILE_PATH))) {
			try {
				Files.createFile(Paths.get(FILE_PATH));
			} catch (IOException e) {
				e.printStackTrace();
			}
			labels = new ArrayList<>();
		}

		// reading tasks from data file
		try (Reader reader = new FileReader(FILE_PATH)) {
			labels = GSON.fromJson(reader, LIST_TYPE_TOKEN);
		} catch (IOException e) {
			labels = new ArrayList<>();
		}

		// checking for null
		if (labels == null) {
			labels = new ArrayList<>();
		}
		return labels;
	}

	private void writeToJson(List<Label> labels) {
		try {
			Files.write(Paths.get(FILE_PATH), GSON.toJson(labels, LIST_TYPE_TOKEN).getBytes());
		} catch (IOException e) {
			throw new DataAccessException("Ошибка записи", e);
		}

	}

	private long getMaxId() {
		List<Label> labels = readFromJson();
		if (labels.size() > 0) {
			return labels.get(labels.size() - 1).getId();
		}
		return 0;
	}
}
