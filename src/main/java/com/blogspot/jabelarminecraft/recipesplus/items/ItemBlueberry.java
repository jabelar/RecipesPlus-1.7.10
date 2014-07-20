package com.blogspot.jabelarminecraft.recipesplus.items;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;

import com.blogspot.jabelarminecraft.recipesplus.RecipePlus;

public class ItemBlueberry extends RecipeItemSeedFood 
{

    public ItemBlueberry() 
    {
        super(1, 0.3F, RecipePlus.blockBlueberry, Blocks.farmland);
        setUnlocalizedName("blueberry");
        setTextureName("recipeplus:blueberry");
        setCreativeTab(CreativeTabs.tabFood);
    }
}
