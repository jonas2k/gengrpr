package com.anconet.gengrpr;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.commons.io.FileUtils;

import com.anconet.gengrpr.model.Group;
import com.anconet.gengrpr.model.Item;

public class App {

	private static Collector collector = new Collector();
	private static Grouper grouper = new Grouper();

	public static void main(String[] args) {

		File file = new File(args[0]);
		int groupCount = Integer.parseInt(args[1]);
		int itemsPerGroup = Integer.parseInt(args[2]);

		List<Item> items = collector.collect(file);

		List<Group> groups = grouper.group(items, groupCount, itemsPerGroup);

		File outputFile = new File(System.getProperty("user.home"),
				new SimpleDateFormat("yyyyMMddHHmm'.txt'").format(new Date()));
		for (Group group : groups) {

			System.out.println(group.toString());

			try {
				FileUtils.write(outputFile, group.toString(), "UTF-8", true);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		System.out.println("Wrote output file to \"" + outputFile + "\".");
	}
}