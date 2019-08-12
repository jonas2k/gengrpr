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
import com.anconet.gengrpr.validation.IInputValidator;
import com.anconet.gengrpr.validation.InputValidator;

public class App {

	private static Grouper grouper = new Grouper();

	private static IInputValidator inputValidator = new InputValidator();
	private static CommandLine commandLine;

	public static void main(String[] args) {

		Options options = prepareOptions();

		try {
			CommandLineParser parser = new DefaultParser();
			commandLine = parser.parse(options, args);

			if (inputIsValid()) {

				File inputFile = new File(commandLine.getOptionValue("f"));
				int groupCount = Integer.parseInt(commandLine.getOptionValue("g"));
				int itemsPerGroup = Integer.parseInt(commandLine.getOptionValue("i"));

				List<Item> items = new Collector().collect(inputFile);

				int roundCount = 1;
				if (commandLine.hasOption("r")) {
					roundCount = Integer.parseInt(commandLine.getOptionValue("r"));
				}

				for (int i = 0; i < roundCount; i++) {

					List<Group> groups;

					if (commandLine.hasOption("b")) {
						List<Item> blacklistItems = new Collector().collect(new File(commandLine.getOptionValue("b")));
						groups = grouper.group(items, groupCount, itemsPerGroup, blacklistItems);
					} else {
						groups = grouper.group(items, groupCount, itemsPerGroup);
					}

					File outputFile = new File(System.getProperty("user.home"),
							new SimpleDateFormat("'gengrpr-'yyyyMMddHHmmss'.txt'").format(new Date()));

					for (Group group : groups) {
						System.out.println(group.toString());
						FileUtils.write(outputFile, group.toString(), "UTF-8", true);
					}
					System.out.println("Wrote output file to \"" + outputFile + "\".");
				}

			} else {
				throw new IllegalArgumentException("Invalid input.");
			}

		} catch (ParseException | IllegalArgumentException | IOException e) {
			System.out.println(e.getMessage());
			printHelp(options);
		}
	}

	private static boolean inputIsValid() {
		return inputValidator.validate(commandLine);
	}

	private static void printHelp(Options options) {
		new HelpFormatter().printHelp("gengrpr", options);
	}

	private static Options prepareOptions() {
		Options options = new Options();

		Option help = new Option("h", "help", false, "print this message");
		Option sourceFile = Option.builder("f").longOpt("sourcefile").desc("input file, required").hasArg()
				.argName("FILE").build();
		Option groupCount = Option.builder("g").longOpt("groupcount").desc("number of groups to create, required")
				.hasArg().argName("NUMBER").build();
		Option itemsPerGroup = Option.builder("i").longOpt("itemspergroup").desc("number of items per group, required")
				.hasArg().argName("NUMBER").build();
		Option blacklistFile = Option.builder("b").longOpt("blacklist").desc("blacklist file").hasArg().argName("FILE")
				.build();
		Option roundCount = Option.builder("r").longOpt("rounds").desc("number of rounds").hasArg().argName("NUMBER")
				.build();

		options.addOption(help);
		options.addOption(sourceFile);
		options.addOption(groupCount);
		options.addOption(itemsPerGroup);
		options.addOption(blacklistFile);
		options.addOption(roundCount);

		return options;
	}
}