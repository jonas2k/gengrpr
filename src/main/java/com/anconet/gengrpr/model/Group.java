package com.anconet.gengrpr.model;

import java.util.HashSet;
import java.util.Set;

public class Group {

	String name;
	Set<Item> items = new HashSet<Item>();

	public Group(String name) {
		super();
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Set<Item> getItems() {
		return items;
	}

	public void setItems(Set<Item> items) {
		this.items = items;
	}

	public void addItem(Item item) {
		this.items.add(item);
	}

	public int getItemsCount() {
		return this.items.size();
	}

	@Override
	public String toString() {

		StringBuilder strBuilder = new StringBuilder();
		strBuilder.append(String.format("**************%n"));
		strBuilder.append(String.format("%s%n", this.getName()));
		strBuilder.append(String.format("**************%n"));

		for (Item i : this.items) {
			strBuilder.append(String.format("%s%n", i.toString()));
		}

		return strBuilder.toString();
	}
}