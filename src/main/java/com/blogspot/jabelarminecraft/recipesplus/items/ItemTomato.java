package com.blogspot.jabelarminecraft.recipesplus.items;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;

import com.blogspot.jabelarminecraft.recipesplus.RecipePlus;

public class ItemTomato extends RecipeItemSeedFood 
{

	// Constructors
	public ItemTomato() 
	{
		
		super(1, 0.3F, RecipePlus.blockTomato, Blocks.farmland);
		setUnlocalizedName("tomato");
		setTextureName("recipeplus:tomato");
		setCreativeTab(CreativeTabs.tabFood);

	}

}
