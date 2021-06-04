package com.contineo.inventory.model;

import java.util.Set;

public class Category {
	private String name;
	private Set<String> subCategories;
	
	public Category(String name, Set<String> subCategories) {
		super();
		this.name = name;
		this.subCategories = subCategories;
	}
	
	public boolean hasSubCategory(String category) {
		return subCategories!=null && subCategories.contains(category);
	}

	@Override
	public String toString() {
		return "Category [name=" + name + ", subCategory=" + subCategories + "]";
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Set<String> getSubCategories() {
		return subCategories;
	}

	public void setSubCategories(Set<String> subCategories) {
		this.subCategories = subCategories;
	}
}
