package com.contineo.inventory.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.contineo.inventory.service.CategoryService;

@SpringBootTest
public class CategoryServiceTest {
	@Autowired CategoryService categoryService;
	
	@Test
	public void test_content() {
		categoryService.entrySet().forEach(e -> System.out.println(e.getKey() + ": " + e.getValue()));
	}
}
