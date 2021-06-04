package com.contineo.inventory.repository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Repository;

import com.contineo.inventory.model.Inventory;

/**
 * This is persistence layer. For simplicity sake, it's backed by a Map in memory only.
 *
 */
@Repository
public class InventoryRepository {

	private Map<UUID,Inventory> map;
	
	public InventoryRepository() {
		map = new ConcurrentHashMap<>();
	}
	
	public Inventory get(UUID id) {
		return map.get(id);
	}
	
	public Collection<Inventory> getAll(){
		return map.values();
	}
	
	public Inventory remove(UUID id) {
		return map.remove(id);
	}
	
	public Collection<Inventory> removeAll(Collection<UUID> ids){
		List<Inventory> rtnVal = new ArrayList<>();
		
		for(UUID id:ids) {
			Inventory removed = map.remove(id);
			if (removed!=null) {
				rtnVal.add(removed);
			}
		}
		
		return rtnVal;
	}
	
	public Inventory updateQuantity(UUID id, int quantity) {
		return map.computeIfPresent(id, (k,v) -> {
			v.setQuantity(quantity);
			v.setLastUpdatedDate(new Date());
			return v;
		});
	}
	
	public void save(Inventory inventory) {
		map.put(inventory.getId(), inventory);
	}
	
	public void saveAll(Collection<Inventory> inventories) {
		for(Inventory inventory:inventories) {
			map.put(inventory.getId(), inventory);
		}
	}
}
