package com.contineo.inventory.controller;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

import javax.validation.Valid;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.contineo.inventory.model.Inventory;
import com.contineo.inventory.service.InventoryService;
import com.google.common.base.Joiner;

/**
 * This is controller.
 *
 */
@Controller
@RequestMapping("/inventories")
@Validated
public class InventoryController {
	private Logger logger = LogManager.getLogger();
	
	private InventoryService inventoryService;
	
	@Autowired
	public InventoryController(InventoryService inventoryService) {
		this.inventoryService = inventoryService;
	}

	@GetMapping(path = "/{id}")
	public ResponseEntity<JsonMessage> get(@PathVariable UUID id){
		Inventory result = inventoryService.get(id);
		
		if (result==null) {
			return ResponseEntity.notFound().build();
		}else {
			return ResponseEntity.ok(new JsonMessage(result));
		}
	}
	
	@GetMapping
	public ResponseEntity<JsonMessage> getAll(){
		return ResponseEntity.ok(new JsonMessage(inventoryService.getAll()));
	}
	
	@DeleteMapping(path = "/{id}")
	public ResponseEntity<JsonMessage> remove(@PathVariable UUID id){
		Inventory result = inventoryService.remove(id);
		
		if (result==null) {
			return ResponseEntity.notFound().build();
		}else {
			logger.debug(()-> String.format("Removed Inventory. (%s)", result));
			return ResponseEntity.ok(new JsonMessage(result));
		}
	}
	
	@DeleteMapping(params = {"ids"})
	public ResponseEntity<JsonMessage> removeAll(@RequestParam List<UUID> ids){
		Collection<Inventory> result = inventoryService.removeAll(ids);
		
		if (CollectionUtils.isEmpty(result)) {
			return ResponseEntity.notFound().build();
		}else {
			logger.debug(()-> String.format("Removed %s Inventories. (%s)",
					result.size(),
					Joiner.on(", ").join(result.stream().map(Inventory::getId).iterator())));
			return ResponseEntity.ok(new JsonMessage(result));
		}
	}
	
	@PatchMapping(path = "/{id}", params = {"quantity"})
	public ResponseEntity<JsonMessage> updateQuantity(@PathVariable UUID id, @RequestParam int quantity){
		Inventory result = inventoryService.updateQuantity(id, quantity);
		
		if (result==null) {
			return ResponseEntity.notFound().build();
		}else {
			logger.debug(() -> String.format("Updated quantity of inventory(%s) to %s.", id, quantity));
			return ResponseEntity.ok(new JsonMessage(result));
		}
	}
	
	@PostMapping(consumes = {MediaType.APPLICATION_JSON_VALUE})
	public ResponseEntity<JsonMessage> saveAll(@RequestBody @Valid Collection<Inventory> inventories){
		if (CollectionUtils.isEmpty(inventories)) {
			return ResponseEntity.badRequest().build();
		}else {
			Collection<Inventory> result = inventoryService.saveAll(inventories);
			logger.debug(() -> String.format("Saved %s inventories. (%s)", result.size(), result));
			
			return ResponseEntity.ok(new JsonMessage(inventories));
		}
	}
}
