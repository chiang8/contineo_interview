package com.contineo.inventory.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.contineo.inventory.service.CategoryService;

/**
 * This is controller for managing Category.
 *
 */
@Controller
@RequestMapping("/categories")
public class CategoryController {
	private CategoryService categoryService;

	@Autowired
	public CategoryController(CategoryService categoryService) {
		this.categoryService = categoryService;
	}
	
	@GetMapping(path = "/all")
	@ResponseBody
	public ResponseEntity<JsonMessage> getAll(){
		return ResponseEntity.ok(new JsonMessage(categoryService.getAll()));
	}
}
