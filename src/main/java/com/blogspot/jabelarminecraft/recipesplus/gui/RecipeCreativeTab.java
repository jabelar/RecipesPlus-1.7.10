package com.blogspot.jabelarminecraft.recipesplus.gui;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

import com.blogspot.jabelarminecraft.recipesplus.RecipePlus;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class RecipeCreativeTab extends CreativeTabs {

public RecipeCreativeTab(String tabLabel)
{
super(tabLabel);
}

@Override
@SideOnly(Side.CLIENT)
public Item getTabIconItem()
{
// return Item.getItemFromBlock(RecipePlus.blockBlueberry);
	return RecipePlus.blueberry;
}

}