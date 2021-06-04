package com.contineo.inventory.service;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@TestInstance(Lifecycle.PER_CLASS)
@ActiveProfiles("test")
public class CategoryServiceTest {
	@Autowired CategoryService categoryService;
	
	@Test
	public void testIsCategoryMatchingSubCategory() {
		assertTrue(categoryService.isCategoryMatchingSubCategory("fashion", "shoe"));
		assertFalse(categoryService.isCategoryMatchingSubCategory("fashion", "laptop"));
	}
}
