package com.anconet.gengrpr;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.io.FileUtils;

import com.anconet.gengrpr.model.Item;

public class Collector {

	private List<String> lines;
	private List<Item> items = new ArrayList<Item>();

	public List<Item> collect(File file) {

		try {
			lines = FileUtils.readLines(file, "UTF-8");

		} catch (IOException e) {
			e.printStackTrace();
		}

		for (String s : lines) {

			String[] lineParts = s.split(";");

			Item item = new Item(lineParts[0]);

			item.setProperties(new ArrayList<>(Arrays.asList(lineParts)));
			item.getProperties().remove(0);

			items.add(item);

		}
		return items;
	}
}