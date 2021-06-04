package com.contineo.inventory.controller;

import java.util.UUID;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.contineo.inventory.model.Inventory;
import com.contineo.inventory.service.InventoryService;

/**
 * This is controller for managing Category.
 *
 */
@Controller
@RequestMapping("/category")
public class CategoryController {
	private Logger logger = LogManager.getLogger();
	
	@Autowired InventoryService inventoryService;

	@PostMapping(path = "/save", consumes = {MediaType.APPLICATION_JSON_VALUE})
	public ResponseEntity<Inventory> save(@RequestBody Inventory inventory){
		inventoryService.save(inventory);
		logger.info(String.format("Saved inventory. (%s)", inventory));
		
		return ResponseEntity.ok(inventory);
	}
	
	@GetMapping(path = "/echo")
	@ResponseBody
	public String echo(@RequestParam("message") String message) {
		return message;
	}
	
	@GetMapping(path = "/test")
	public ResponseEntity<Inventory> test(){
		Inventory rtnVal = new Inventory(UUID.randomUUID(), "name", "category", "subCategory", 10);
		return ResponseEntity.ok(rtnVal);
	}
}
