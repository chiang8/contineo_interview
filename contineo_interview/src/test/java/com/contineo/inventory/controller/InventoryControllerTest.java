package com.contineo.inventory.controller;

import java.util.Arrays;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.contineo.inventory.model.Inventory;
import com.contineo.inventory.service.InventoryService;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(Lifecycle.PER_CLASS)
@ActiveProfiles("test")
public class InventoryControllerTest {
	@Autowired private MockMvc mvc;
	@Autowired private InventoryService inventoryService;
	private ObjectMapper objectMapper = new ObjectMapper();
	
	private Inventory computerInventory_1;		//readonly
	private Inventory computerInventory_2;		//readonly
	
	@BeforeAll
	public void init() {
		//readonly
		computerInventory_1 = new Inventory(UUID.randomUUID(), "dell_laptop", "computer", "laptop", 5);
		computerInventory_2 = new Inventory(UUID.randomUUID(), "ipad_pro", "computer", "tablet", 10);
		
		//for testing get requests
		inventoryService.saveAll(Arrays.asList(computerInventory_1, computerInventory_2));
	}

	@Test
	public void testGet()throws Exception{
		mvc.perform(MockMvcRequestBuilders.get("/inventories/" + computerInventory_1.getId()))
			.andDo(MockMvcResultHandlers.log())
			.andExpect(MockMvcResultMatchers.status().is(200))
			.andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
			.andExpect(MockMvcResultMatchers.jsonPath("$.data.id").value(computerInventory_1.getId().toString()))
			.andExpect(MockMvcResultMatchers.jsonPath("$.data.name").value(computerInventory_1.getName()))
			.andExpect(MockMvcResultMatchers.jsonPath("$.data.category").value(computerInventory_1.getCategory()))
			.andExpect(MockMvcResultMatchers.jsonPath("$.data.subCategory").value(computerInventory_1.getSubCategory()))
			.andExpect(MockMvcResultMatchers.jsonPath("$.data.quantity").value(computerInventory_1.getQuantity()));
	}
	
	@Test
	public void testGet_invalidArgument()throws Exception{
		mvc.perform(MockMvcRequestBuilders.get("/inventories/1234"))		//1234 is an invalid UUID
			.andDo(MockMvcResultHandlers.log())
			.andExpect(MockMvcResultMatchers.status().is(500))
			.andExpect(MockMvcResultMatchers.jsonPath("$.success").value(false))
			.andExpect(MockMvcResultMatchers.jsonPath("$.messages").value(Matchers.notNullValue()));
	}
	
	@Test
	public void testGet_notFound()throws Exception{
		mvc.perform(MockMvcRequestBuilders.get("/inventories/" + UUID.randomUUID()))	//look up inventory with a random UUID
			.andDo(MockMvcResultHandlers.log())
			.andExpect(MockMvcResultMatchers.status().is(404));
	}

	@Test
	public void testGetAll()throws Exception{
		mvc.perform(MockMvcRequestBuilders.get("/inventories"))
			.andDo(MockMvcResultHandlers.log())
			.andExpect(MockMvcResultMatchers.status().is(200))
			.andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
			.andExpect(MockMvcResultMatchers.jsonPath("$.data.[?(@.name == \"dell_laptop\")].category").value(computerInventory_1.getCategory()))
			.andExpect(MockMvcResultMatchers.jsonPath("$.data.[?(@.name == \"dell_laptop\")].subCategory").value(computerInventory_1.getSubCategory()))
			.andExpect(MockMvcResultMatchers.jsonPath("$.data.[?(@.name == \"dell_laptop\")].quantity").value(computerInventory_1.getQuantity()))
			.andExpect(MockMvcResultMatchers.jsonPath("$.data.[?(@.name == \"ipad_pro\")].category").value(computerInventory_2.getCategory()))
			.andExpect(MockMvcResultMatchers.jsonPath("$.data.[?(@.name == \"ipad_pro\")].subCategory").value(computerInventory_2.getSubCategory()))
			.andExpect(MockMvcResultMatchers.jsonPath("$.data.[?(@.name == \"ipad_pro\")].quantity").value(computerInventory_2.getQuantity()));
	}

	@Test
	public void testInvalidPath()throws Exception{
		mvc.perform(MockMvcRequestBuilders.get("/invalidPath"))
			.andDo(MockMvcResultHandlers.log())
			.andExpect(MockMvcResultMatchers.status().is(HttpStatus.NOT_FOUND.value()));
	}

	@Test
	public void testRemoveInventories()throws Exception{
		//save 2 new entries
		Inventory newInventory_1 = new Inventory(UUID.randomUUID(), "shirt", "fashion", "clothing", 100);
		inventoryService.save(newInventory_1);
		Inventory newInventory_2 = new Inventory(UUID.randomUUID(), "dress", "fashion", "clothing", 200);
		inventoryService.save(newInventory_2);
			
		//remove entries
		mvc.perform(MockMvcRequestBuilders.delete("/inventories?ids=" + StringUtils.join(Arrays.asList(newInventory_1.getId(), newInventory_2.getId()), ","))
				.content(objectMapper.writeValueAsBytes(Arrays.asList(newInventory_1)))
				.contentType(MediaType.APPLICATION_JSON)
				)
			.andDo(MockMvcResultHandlers.log())
			.andExpect(MockMvcResultMatchers.status().is(200))
			.andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
			.andExpect(MockMvcResultMatchers.jsonPath("$.data.[?(@.name == \"shirt\")].category").value(newInventory_1.getCategory()))
			.andExpect(MockMvcResultMatchers.jsonPath("$.data.[?(@.name == \"shirt\")].subCategory").value(newInventory_1.getSubCategory()))
			.andExpect(MockMvcResultMatchers.jsonPath("$.data.[?(@.name == \"shirt\")].quantity").value(newInventory_1.getQuantity()))
			.andExpect(MockMvcResultMatchers.jsonPath("$.data.[?(@.name == \"dress\")].category").value(newInventory_2.getCategory()))
			.andExpect(MockMvcResultMatchers.jsonPath("$.data.[?(@.name == \"dress\")].subCategory").value(newInventory_2.getSubCategory()))
			.andExpect(MockMvcResultMatchers.jsonPath("$.data.[?(@.name == \"dress\")].quantity").value(newInventory_2.getQuantity()));
	}
	
	@Test
	public void testRemoveInventories_onlyOneSucceeded()throws Exception{
		//save 2 new entries
		Inventory newInventory_1 = new Inventory(UUID.randomUUID(), "shirt", "fashion", "clothing", 100);
		inventoryService.save(newInventory_1);
		Inventory newInventory_2 = new Inventory(UUID.randomUUID(), "dress", "fashion", "clothing", 200);
		inventoryService.save(newInventory_2);
			
		//remove entries
		mvc.perform(MockMvcRequestBuilders.delete("/inventories?ids=" + StringUtils.join(Arrays.asList(newInventory_1.getId(), UUID.randomUUID()), ",")))	//one random UUID
			.andDo(MockMvcResultHandlers.log())
			.andExpect(MockMvcResultMatchers.status().is(200))
			.andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
			.andExpect(MockMvcResultMatchers.jsonPath("$.data", Matchers.hasSize(1)))
			.andExpect(MockMvcResultMatchers.jsonPath("$.data.[?(@.name == \"shirt\")].category").value(newInventory_1.getCategory()))
			.andExpect(MockMvcResultMatchers.jsonPath("$.data.[?(@.name == \"shirt\")].subCategory").value(newInventory_1.getSubCategory()))
			.andExpect(MockMvcResultMatchers.jsonPath("$.data.[?(@.name == \"shirt\")].quantity").value(newInventory_1.getQuantity()));
	}
	
	@Test
	public void testRemoveInventory()throws Exception{
		//save a new entry
		Inventory newInventory_1 = new Inventory(UUID.randomUUID(), "shirt", "fashion", "clothing", 100);
		inventoryService.save(newInventory_1);
			
		//remove entry
		mvc.perform(MockMvcRequestBuilders.delete("/inventories/" + newInventory_1.getId()))
			.andDo(MockMvcResultHandlers.log())
			.andExpect(MockMvcResultMatchers.status().is(200))
			.andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
			.andExpect(MockMvcResultMatchers.jsonPath("$.data.id").value(newInventory_1.getId().toString()))
			.andExpect(MockMvcResultMatchers.jsonPath("$.data.name").value(newInventory_1.getName()))
			.andExpect(MockMvcResultMatchers.jsonPath("$.data.category").value(newInventory_1.getCategory()))
			.andExpect(MockMvcResultMatchers.jsonPath("$.data.subCategory").value(newInventory_1.getSubCategory()))
			.andExpect(MockMvcResultMatchers.jsonPath("$.data.quantity").value(newInventory_1.getQuantity()));
	}
	
	@Test
	public void testRemoveInventory_notFound()throws Exception{			
		//remove entry
		mvc.perform(MockMvcRequestBuilders.delete("/inventories/" + UUID.randomUUID()))
			.andDo(MockMvcResultHandlers.log())
			.andExpect(MockMvcResultMatchers.status().is(404));
	}
	
	@Test
	public void testSaveInventories()throws Exception{
		Inventory newInventory_1 = new Inventory(null, "nike_basketball_shoe", "fashion", "shoe", 100);
		Inventory newInventory_2 = new Inventory(null, "pants", "fashion", "clothing", 200);
		
		mvc.perform(MockMvcRequestBuilders.post("/inventories")
				.content(objectMapper.writeValueAsBytes(Arrays.asList(newInventory_1, newInventory_2)))
				.contentType(MediaType.APPLICATION_JSON)
				)
			.andDo(MockMvcResultHandlers.log())
			.andExpect(MockMvcResultMatchers.status().is(200))
			.andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
			.andExpect(MockMvcResultMatchers.jsonPath("$.data[0].id").value(Matchers.notNullValue()))
			.andExpect(MockMvcResultMatchers.jsonPath("$.data[0].name").value(newInventory_1.getName()))
			.andExpect(MockMvcResultMatchers.jsonPath("$.data[0].category").value(newInventory_1.getCategory()))
			.andExpect(MockMvcResultMatchers.jsonPath("$.data[0].subCategory").value(newInventory_1.getSubCategory()))
			.andExpect(MockMvcResultMatchers.jsonPath("$.data[0].quantity").value(newInventory_1.getQuantity()))
			.andExpect(MockMvcResultMatchers.jsonPath("$.data[1].id").value(Matchers.notNullValue()))
			.andExpect(MockMvcResultMatchers.jsonPath("$.data[1].name").value(newInventory_2.getName()))
			.andExpect(MockMvcResultMatchers.jsonPath("$.data[1].category").value(newInventory_2.getCategory()))
			.andExpect(MockMvcResultMatchers.jsonPath("$.data[1].subCategory").value(newInventory_2.getSubCategory()))
			.andExpect(MockMvcResultMatchers.jsonPath("$.data[1].quantity").value(newInventory_2.getQuantity()));
	}
	
	@Test
	public void testSaveInventories_invalid_subCatetory()throws Exception{
		Inventory newInventory_1 = new Inventory(null, "addidas_basketball_shoe", "fashion", "tablet", 100);
		Inventory newInventory_2 = new Inventory(null, "shorts", "fashion", "laptop", 200);
		
		mvc.perform(MockMvcRequestBuilders.post("/inventories")
				.content(objectMapper.writeValueAsBytes(Arrays.asList(newInventory_1, newInventory_2)))
				.contentType(MediaType.APPLICATION_JSON)
				)
			.andDo(MockMvcResultHandlers.log())
			.andExpect(MockMvcResultMatchers.status().is(HttpStatus.BAD_REQUEST.value()))
			.andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
			.andExpect(MockMvcResultMatchers.jsonPath("$.success").value(false))
			.andExpect(MockMvcResultMatchers.jsonPath("$.messages").value(Matchers.notNullValue()));
	}

	@Test
	public void testUpdateInventories()throws Exception{
		//save a new entry
		Inventory newInventory_1 = new Inventory(UUID.randomUUID(), "t-shirt", "fashion", "clothing", 200);
		inventoryService.save(newInventory_1);
		
		//update
		newInventory_1.setQuantity(300);
		
		mvc.perform(MockMvcRequestBuilders.post("/inventories")
				.content(objectMapper.writeValueAsBytes(Arrays.asList(newInventory_1)))
				.contentType(MediaType.APPLICATION_JSON)
				)
			.andDo(MockMvcResultHandlers.log())
			.andExpect(MockMvcResultMatchers.status().is(200))
			.andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
			.andExpect(MockMvcResultMatchers.jsonPath("$.data[0].id").value(newInventory_1.getId().toString()))
			.andExpect(MockMvcResultMatchers.jsonPath("$.data[0].name").value(newInventory_1.getName()))
			.andExpect(MockMvcResultMatchers.jsonPath("$.data[0].category").value(newInventory_1.getCategory()))
			.andExpect(MockMvcResultMatchers.jsonPath("$.data[0].subCategory").value(newInventory_1.getSubCategory()))
			.andExpect(MockMvcResultMatchers.jsonPath("$.data[0].quantity").value(newInventory_1.getQuantity()))
			.andExpect(MockMvcResultMatchers.jsonPath("$.data[0].lastUpdatedDate").value(Matchers.greaterThan(newInventory_1.getLastUpdatedDate().getTime())));
	}

	@Test
	public void testUpdateQuantity()throws Exception{
		//save a new entry 
		Inventory newInventory_1 = new Inventory(null, "cap", "fashion", "clothing", 200);
		inventoryService.save(newInventory_1);
		
		//update quantity
		int newQuantity = 300;
		mvc.perform(MockMvcRequestBuilders.patch("/inventories/" + newInventory_1.getId())
				.param("quantity", Integer.toString(newQuantity))
				.contentType(MediaType.APPLICATION_JSON)
				)
			.andDo(MockMvcResultHandlers.log())
			.andExpect(MockMvcResultMatchers.status().is(200))
			.andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
			.andExpect(MockMvcResultMatchers.jsonPath("$.data.id").value(newInventory_1.getId().toString()))
			.andExpect(MockMvcResultMatchers.jsonPath("$.data.name").value(newInventory_1.getName()))
			.andExpect(MockMvcResultMatchers.jsonPath("$.data.category").value(newInventory_1.getCategory()))
			.andExpect(MockMvcResultMatchers.jsonPath("$.data.subCategory").value(newInventory_1.getSubCategory()))
			.andExpect(MockMvcResultMatchers.jsonPath("$.data.quantity").value(newQuantity));
	}
}
