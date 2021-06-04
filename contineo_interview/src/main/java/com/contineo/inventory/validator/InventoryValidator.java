package com.contineo.inventory.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import com.contineo.inventory.model.Inventory;
import com.contineo.inventory.service.CategoryService;

public class InventoryValidator implements ConstraintValidator<InventoryConstraint, Inventory>{

	@Autowired CategoryService categoryService;
	
	@Override
	public void initialize(InventoryConstraint constraint) {
	}

	@Override
	public boolean isValid(Inventory inventory, ConstraintValidatorContext ctx) {
		boolean rtnVal = true;
	
		if (!categoryService.isCategoryMatchingSubCategory(inventory.getCategory(), inventory.getSubCategory())) {
			ctx.disableDefaultConstraintViolation();
			ctx.buildConstraintViolationWithTemplate(
					String.format("%s is not a valid sub-category for %s category.", inventory.getSubCategory(), inventory.getCategory()))
				.addConstraintViolation();
			
			rtnVal = false;
		}
		
		
		return rtnVal;
	}

}