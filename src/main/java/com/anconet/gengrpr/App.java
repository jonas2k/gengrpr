package com.anconet.gengrpr;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import org.apache.commons.io.FileUtils;

import com.anconet.gengrpr.helpers.Constants;
import com.anconet.gengrpr.model.Group;
import com.anconet.gengrpr.model.Item;
import com.anconet.gengrpr.workers.Collector;
import com.anconet.gengrpr.workers.Grouper;

import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Model.CommandSpec;
import picocli.CommandLine.Option;
import picocli.CommandLine.Spec;

@Command(name = Constants.APP_NAME, mixinStandardHelpOptions = true, version = Constants.APP_NAME + " "
		+ Constants.APP_VERSION, sortOptions = false, description = "Generic grouping application", footer = { "",
				"Source file has to contain one element per line and may contain multiple semicolon separated attributes, e.g.",
				"", "\"name\";\"attribute1\";\"attribute2\"" }, header = { "   __ _  ___ _ __   __ _ _ __ _ __  _ __ ",
						"  / _` |/ _ \\ '_ \\ / _` | '__| '_ \\| '__|", " | (_| |  __/ | | | (_| | |  | |_) | |   ",
						"  \\__, |\\___|_| |_|\\__, |_|  | .__/|_|   ", "   __/ |            __/ |    | |         ",
						"  |___/            |___/     |_|         " + Constants.APP_VERSION + " by "
								+ Constants.APP_AUTHOR,
						"" })
public class App implements Runnable {

	@Option(names = { "-s",
			"--source-file" }, required = true, description = "source file containing elements to be grouped, required.")
	private File sourceFile;

	@Option(names = { "-b", "--blacklist-file" }, description = "optional blacklist file containing elements to skip.")
	private File blacklistFile;

	@Option(names = { "-g",
			"--group-count" }, required = true, description = "number of groups to be created, required.")
	private int groupCount;

	@Option(names = { "-i", "--items-per-group" }, required = true, description = "number of items per group, required.")
	private int itemsPerGroup;

	@Option(names = { "-r", "--rounds" }, description = "optional number of grouping rounds.")
	private int roundCount = 1;

	@Spec
	CommandSpec spec;

	public static void main(String[] args) {
		new CommandLine(new App()).execute(args);
	}

	@Override
	public void run() {

		Arrays.stream(this.spec.usageMessage().header())
				.forEach(l -> System.out.println(CommandLine.Help.Ansi.AUTO.string(l)));

		try {
			List<Item> sourceItems = new Collector().collect(sourceFile);

			if (blacklistFile != null) {
				List<Item> blacklistItems = new Collector().collect(blacklistFile);
				sourceItems.removeAll(blacklistItems);
			}

			for (int round = 1; round <= roundCount; round++) {
				List<Group> groups = new Grouper().group(sourceItems, groupCount, itemsPerGroup);
				writeGroupsToFile(groups, getOutputFile(round));
			}
		} catch (IllegalArgumentException | IOException e) {
			System.err.println(e.getMessage());
		}
	}

	private void writeGroupsToFile(List<Group> groups, File outputFile) throws IOException {
		for (Group group : groups) {
			System.out.println(group.toString());
			FileUtils.write(outputFile, group.toString(), "UTF-8", true);
		}
		System.out.printf("Wrote output file to \"%s\".%n", outputFile);
	}

	private File getOutputFile(int round) {
		return new File(System.getProperty("user.home"), getOutputFileName(round));
	}

	private String getOutputFileName(int round) {
		return String.format("%s-%s-r%s.txt", Constants.APP_NAME,
				LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss")), round);
	}
}