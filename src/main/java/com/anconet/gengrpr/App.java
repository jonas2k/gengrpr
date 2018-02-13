package com.anconet.gengrpr;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.io.FileUtils;

import com.anconet.gengrpr.model.Group;
import com.anconet.gengrpr.model.Item;

public class App {

	private static Collector collector = new Collector();
	private static Grouper grouper = new Grouper();

	private static Options options = new Options();
	private static CommandLine commandLine;

	public static void main(String[] args) {

		prepareOptions();

		try {
			CommandLineParser parser = new DefaultParser();
			commandLine = parser.parse(options, args);

			if (allOptionsPresent()) {

				File file = new File(commandLine.getOptionValue("f"));
				int groupCount = Integer.parseInt(commandLine.getOptionValue("g"));
				int itemsPerGroup = Integer.parseInt(commandLine.getOptionValue("i"));

				List<Item> items = collector.collect(file);
				List<Group> groups = grouper.group(items, groupCount, itemsPerGroup);

				File outputFile = new File(System.getProperty("user.home"),
						new SimpleDateFormat("yyyyMMddHHmm'.txt'").format(new Date()));

				for (Group group : groups) {
					System.out.println(group.toString());
					FileUtils.write(outputFile, group.toString(), "UTF-8", true);
				}
				System.out.println("Wrote output file to \"" + outputFile + "\".");

			} else {
				printHelp();
			}

		} catch (ParseException | IOException e) {
			e.printStackTrace();
		}
	}

	private static boolean allOptionsPresent() {
		return (commandLine.hasOption("f") && commandLine.hasOption("g") && commandLine.hasOption("i"));
	}

	private static void printHelp() {
		HelpFormatter formatter = new HelpFormatter();
		formatter.printHelp("gengrpr", options);
	}

	private static void prepareOptions() {
		Option help = new Option("h", "help", false, "print this message");

		Option sourceFile = Option.builder("f").longOpt("sourcefile").desc("input file").hasArg().argName("FILE")
				.build();

		Option groupCount = Option.builder("g").longOpt("groupcount").desc("number of groups to create").hasArg()
				.argName("NUMBER").build();

		Option itemsPerGroup = Option.builder("i").longOpt("itemspergroup").desc("number of items per group\"").hasArg()
				.argName("NUMBER").build();

		options.addOption(help);
		options.addOption(sourceFile);
		options.addOption(groupCount);
		options.addOption(itemsPerGroup);
	}
}