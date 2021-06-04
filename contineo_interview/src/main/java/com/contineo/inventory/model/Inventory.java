package com.contineo.inventory.model;

import java.util.Date;
import java.util.UUID;

import com.contineo.inventory.validator.InventoryConstraint;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;

@InventoryConstraint
public class Inventory {
	private UUID id;
	private String name;
	private String category;
	private String subCategory;
	private int quantity;
	private Date lastUpdatedDate;
	private int version;
	
	public Inventory() {}
	
	public Inventory(UUID id, String name, String category, String subCategory, int quantity) {
		super();
		this.id = id;
		this.name = name;
		this.category = category;
		this.subCategory = subCategory;
		this.quantity = quantity;
	}

	@Override
	public String toString() {
		return "Inventory [id=" + id + ", name=" + name + ", category=" + category + ", subCategory=" + subCategory
				+ ", quantity=" + quantity + "]";
	}

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getCategory() {
		return category;
	}
	
	public void setCategory(String category) {
		this.category = category;
	}
	
	public String getSubCategory() {
		return subCategory;
	}
	
	public void setSubCategory(String subCategory) {
		this.subCategory = subCategory;
	}
	
	public int getQuantity() {
		return quantity;
	}
	
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	@JsonFormat(shape = Shape.NUMBER_INT)
	public Date getLastUpdatedDate() {
		return lastUpdatedDate;
	}

	public void setLastUpdatedDate(Date lastUpdatedDate) {
		this.lastUpdatedDate = lastUpdatedDate;
	}

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}
}
