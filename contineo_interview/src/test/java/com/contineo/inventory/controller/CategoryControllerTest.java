package com.contineo.inventory.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.contineo.inventory.service.CategoryService;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(Lifecycle.PER_CLASS)
@ActiveProfiles("test")
public class CategoryControllerTest {
	@Autowired private MockMvc mvc;
	@Autowired private CategoryService categoryService;
	private ObjectMapper objectMapper = new ObjectMapper();

	@Test
	public void testGetAll()throws Exception{
		String expected = objectMapper.writeValueAsString(new JsonMessage(categoryService.getAll()));
		
		mvc.perform(MockMvcRequestBuilders.get("/categories/all"))
			.andDo(MockMvcResultHandlers.log())
			.andExpect(MockMvcResultMatchers.status().is(200))
			.andExpect(MockMvcResultMatchers.content().json(expected, false));
	}
}
