/**
    Copyright (C) 2014 by jabelar

    This file is part of jabelar's Minecraft Forge modding examples; as such,
    you can redistribute it and/or modify it under the terms of the GNU
    General Public License as published by the Free Software Foundation,
    either version 3 of the License, or (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
    GNU General Public License for more details.

    For a copy of the GNU General Public License see <http://www.gnu.org/licenses/>.
*/

package com.blogspot.jabelarminecraft.recipesplus.gui;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

import com.blogspot.jabelarminecraft.recipesplus.RecipePlus;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class RecipeCreativeTab extends CreativeTabs 
{

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