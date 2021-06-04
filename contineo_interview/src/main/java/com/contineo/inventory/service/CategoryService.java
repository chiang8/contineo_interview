package com.contineo.inventory.service;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.contineo.inventory.model.Category;
import com.google.common.base.Splitter;

/**
 * A service for managing Category. 
 * For convenience sake, it's backed by a properties file. It should be backed by a database instead in real life.
 *
 */
@Component
public class CategoryService {
	private Map<String,Category> map;
	
	public CategoryService() throws IOException {
		map = new ConcurrentHashMap<>();
		refresh();
	}
	
	public Set<Map.Entry<String,Category>> entrySet(){
		return map.entrySet();
	}
	
	public Category get(String key) {
		return map.get(key);
	}
	
	public Collection<Category> getAll(){
		return map.values();
	}
	
	public boolean isCategoryMatchingSubCategory(String categoryName, String subCategoryName) {
		Category category = map.get(categoryName);
		
		return category!=null && category.hasSubCategory(subCategoryName);
	}
	
	private Map<String,Category> loadEntries() throws IOException {
		//load from a properties file. Could have loaded from database instead...
		Properties properties = new Properties();
		try(InputStream in = getClass().getClassLoader().getResourceAsStream("category.properties")){
			properties.load(in);
		}
		
		return properties.entrySet().stream().collect(
				Collectors.toMap(
						e -> e.getKey().toString(),
						e -> new Category(e.getKey().toString(), new HashSet<>(Splitter.on(",").omitEmptyStrings().splitToList(e.getValue().toString())))
						));
	}

	public void save(String key, Category category) {
		map.put(key, category);
	}
	
	public void refresh() throws IOException {		
		map.putAll(loadEntries());
	}
}
