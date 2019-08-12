package com.anconet.gengrpr;

import java.util.ArrayList;
import java.util.List;
import org.apache.commons.math3.random.MersenneTwister;

import com.anconet.gengrpr.model.Group;
import com.anconet.gengrpr.model.Item;

public class Grouper {

	private MersenneTwister mt = new MersenneTwister();

	public List<Group> group(List<Item> sourceItems, int groupCount, int itemsPerGroup)
			throws IllegalArgumentException {

		List<Item> localSourceItems = new ArrayList<Item>(sourceItems);

		if (localSourceItems.size() < groupCount * itemsPerGroup) {
			throw new IllegalArgumentException("Insufficient number of source items.");
		}

		List<Group> groups = new ArrayList<Group>();

		for (int i = 0; i < groupCount; i++) {
			groups.add(new Group("Group" + (i + 1)));
		}

		for (Group group : groups) {
			while (group.getItemsCount() < itemsPerGroup) {
				group.addItem(localSourceItems.remove(mt.nextInt(localSourceItems.size())));
			}
		}
		return groups;
	}
}