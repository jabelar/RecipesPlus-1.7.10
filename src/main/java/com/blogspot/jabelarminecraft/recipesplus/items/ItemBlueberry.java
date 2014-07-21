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
