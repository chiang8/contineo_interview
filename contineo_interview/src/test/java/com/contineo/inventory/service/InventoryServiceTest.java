package com.contineo.inventory.service;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.Collection;
import java.util.UUID;

import org.apache.commons.collections4.CollectionUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.contineo.inventory.model.Inventory;

@SpringBootTest
@TestInstance(Lifecycle.PER_CLASS)
@ActiveProfiles("test")
public class InventoryServiceTest {

	@Autowired InventoryService inventoryService;
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
	public void testGet() {
		Inventory result = inventoryService.get(computerInventory_1.getId());
		
		assertEquals(computerInventory_1, result);
	}
	
	@Test
	public void testGetAll() {
		inventoryService.clear();
		init();
		
		Collection<Inventory> result = inventoryService.getAll();
		Collection<Inventory> expected = Arrays.asList(computerInventory_1, computerInventory_2);
		
		assertTrue(CollectionUtils.isEqualCollection(expected, result));
	}
	
	@Test
	public void testRemoveAll()throws Exception{
		//save 2 new entries
		Inventory newInventory_1 = new Inventory(UUID.randomUUID(), "shirt", "fashion", "clothing", 100);
		inventoryService.save(newInventory_1);
		Inventory newInventory_2 = new Inventory(UUID.randomUUID(), "dress", "fashion", "clothing", 200);
		inventoryService.save(newInventory_2);
			
		//remove entries
		inventoryService.removeAll(Arrays.asList(newInventory_1.getId(), newInventory_2.getId()));
		
		//assert
		assertNull(inventoryService.get(newInventory_1.getId()));
		assertNull(inventoryService.get(newInventory_2.getId()));
	}
	
	@Test
	public void testRemoveAll_notFound()throws Exception{
		Collection<Inventory> result = inventoryService.removeAll(Arrays.asList(UUID.randomUUID(), UUID.randomUUID()));
		
		assertTrue(CollectionUtils.isEmpty(result));
	}
	
	@Test
	public void testRemoveAll_onlyOneSucceeded()throws Exception{
		//save 2 new entries
		Inventory newInventory_1 = new Inventory(UUID.randomUUID(), "shirt", "fashion", "clothing", 100);
		inventoryService.save(newInventory_1);
		Inventory newInventory_2 = new Inventory(UUID.randomUUID(), "dress", "fashion", "clothing", 200);
		inventoryService.save(newInventory_2);
			
		//remove entries
		UUID randomId = UUID.randomUUID();
		inventoryService.removeAll(Arrays.asList(newInventory_1.getId(), randomId));
		
		//assert
		assertNull(inventoryService.get(newInventory_1.getId()));
		assertNotNull(inventoryService.get(newInventory_2.getId()));
	}
	
	@Test
	public void testRemove()throws Exception{
		//save a new entry
		Inventory newInventory_1 = new Inventory(UUID.randomUUID(), "shirt", "fashion", "clothing", 100);
		inventoryService.save(newInventory_1);
			
		//remove entry
		inventoryService.remove(newInventory_1.getId());
		
		//assert
		assertNull(inventoryService.get(newInventory_1.getId()));
	}
	
	@Test
	public void testRemove_notFound()throws Exception{			
		//remove entry
		Inventory result = inventoryService.remove(UUID.randomUUID());
				
		//assert
		assertNull(result);
	}
	
	@Test
	public void testSaveInventories()throws Exception{
		//save new entries
		Inventory newInventory_1 = new Inventory(null, "nike_basketball_shoe", "fashion", "shoe", 100);
		Inventory newInventory_2 = new Inventory(null, "pants", "fashion", "clothing", 200);
		Collection<Inventory> result = inventoryService.saveAll(Arrays.asList(newInventory_1, newInventory_2));
		
		//assert
		for(Inventory inventory:result) {
			assertEquals(inventoryService.get(inventory.getId()), inventory);
		}
	}
	
	@Test
	public void testSaveInventories_existingEntries()throws Exception{
		//save a new entry
		Inventory newInventory_1 = new Inventory(UUID.randomUUID(), "t-shirt", "fashion", "clothing", 200);
		inventoryService.save(newInventory_1);
		
		//update
		newInventory_1.setQuantity(300);
		Inventory result = inventoryService.save(newInventory_1);
		
		//assert
		Inventory expected = inventoryService.get(newInventory_1.getId());
		assertEquals(expected, result);
	}

	@Test
	public void testUpdateQuantity()throws Exception{
		//save a new entry 
		Inventory newInventory_1 = new Inventory(null, "cap", "fashion", "clothing", 200);
		inventoryService.save(newInventory_1);
		
		//update quantity
		int newQuantity = 300;
		Inventory result = inventoryService.updateQuantity(newInventory_1.getId(), newQuantity);
		
		//assert
		Inventory expected = inventoryService.get(newInventory_1.getId());
		assertEquals(expected, result);
	}
	
	@Test
	public void testUpdateQuantity_notFound()throws Exception{		
		//update quantity
		assertNull(inventoryService.updateQuantity(UUID.randomUUID(), 300));
	}
	
}
