package com.anconet.gengrpr;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.math3.random.MersenneTwister;

import com.anconet.gengrpr.model.Group;
import com.anconet.gengrpr.model.Item;

public class Grouper {

	private MersenneTwister mt = new MersenneTwister();

	public List<Group> group(List<Item> items, int groupCount, int itemsPerGroup) {

		List<Group> groups = new ArrayList<Group>();
		Set<Item> usedItems = new HashSet<Item>();

		for (int i = 0; i < groupCount; i++) {
			groups.add(new Group("Group" + (i + 1)));
		}

		for (Group group : groups) {

			while (group.getItemsCount() < itemsPerGroup) {

				Item currentItem = items.get(mt.nextInt(items.size()));
				if (!usedItems.contains(currentItem)) {
					group.addItem(currentItem);
					usedItems.add(currentItem);
				}
			}
		}
		return groups;
	}

	public List<Group> group(List<Item> items, int groupCount, int itemsPerGroup, int rounds) {

		List<Group> groups = new ArrayList<Group>();

		for (int i = 0; i < rounds; i++) {
			groups.addAll(this.group(items, groupCount, itemsPerGroup));
		}
		return groups;
	}
}