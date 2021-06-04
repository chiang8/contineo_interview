package com.contineo.inventory.service;

import java.util.Collection;
import java.util.Date;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.contineo.inventory.model.Inventory;
import com.contineo.inventory.repository.InventoryRepository;

/**
 * This is the service layer.
 * Should have been transactional if it's backed by database.
 */
@Service
public class InventoryService {
	private InventoryRepository repo; 
	
	@Autowired
	public InventoryService(InventoryRepository repo) {
		this.repo = repo;
	}

	public void clear() {
		repo.clear();
	}
	
	public Inventory get(UUID id) {
		return repo.get(id);
	}
	
	public Collection<Inventory> getAll(){
		return repo.getAll();
	}
	
	public Collection<Inventory> getAll(Collection<UUID> ids){
		return repo.getAll(ids);
	}
	
	/**
	 *	To remove an inventory. 
	 */
	public Inventory remove(UUID id) {
		return repo.remove(id);
	}
	
	public Collection<Inventory> removeAll(Collection<UUID> ids){
		return repo.removeAll(ids);
	}
	
	/**
	 *	To save an inventory. 
	 */
	public Inventory save(Inventory inventory) {
		if (inventory.getId()==null) {
			inventory.setId(UUID.randomUUID());
		}
		inventory.setLastUpdatedDate(new Date());
		repo.save(inventory);
		
		return inventory;
	}
	
	/**
	 *	To save multiple inventories. 
	 */
	public Collection<Inventory> saveAll(Collection<Inventory> inventories) {
		Date now = new Date();
		for(Inventory inventory:inventories) {
			if (inventory.getId()==null) {
				inventory.setId(UUID.randomUUID());
			}
			inventory.setLastUpdatedDate(now);
		}
		repo.saveAll(inventories);
		
		return inventories;
	}
	
	/**
	 * To update quantity of an existing Inventory.
	 */
	public Inventory updateQuantity(UUID id, int quantity) {
		return repo.updateQuantity(id, quantity);
	}
}
