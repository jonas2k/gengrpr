package com.anconet.gengrpr.model;

import java.util.List;

public class Item {

	String name;
	List<String> properties;

	public Item(String name) {
		super();
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<String> getProperties() {
		return properties;
	}

	public void setProperties(List<String> properties) {
		this.properties = properties;
	}

	public void addProperty(String property) {
		this.properties.add(property);
	}

	public int getPropertiesCount() {
		return this.properties.size();
	}

	@Override
	public String toString() {

		StringBuilder strBuilder = new StringBuilder();
		strBuilder.append(this.getName());

		for (String property : this.properties) {
			strBuilder.append(";" + property);
		}
		return strBuilder.toString();
	}
}