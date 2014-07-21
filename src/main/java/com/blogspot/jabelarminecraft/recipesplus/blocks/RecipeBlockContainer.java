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

package com.blogspot.jabelarminecraft.recipesplus.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public abstract class RecipeBlockContainer extends Block implements ITileEntityProvider
{
    protected RecipeBlockContainer(Material material)
    {
        super(material);
        this.isBlockContainer = true;
        // DEBUG
        System.out.println("RecipeBlockContainer() constructor");
    }

    /**
     * Called whenever the block is added into the world. Args: world, x, y, z
     */
    @Override
	public void onBlockAdded(World world, int pos_x, int pos_y, int pos_z)
    {
        // DEBUG
        System.out.println("RecipeBlockContainer onBlockAdded()");
        super.onBlockAdded(world, pos_x, pos_y, pos_z);
    }

    @Override
	public void breakBlock(World world, int pos_x, int pos_y, int pos_z, Block p_149749_5_, int p_149749_6_)
    {
        // DEBUG
        System.out.println("RecipeBlockContainer breakBlock()");
        super.breakBlock(world, pos_x, pos_y, pos_z, p_149749_5_, p_149749_6_);
        world.removeTileEntity(pos_x, pos_y, pos_z);
    }

    @Override
	public boolean onBlockEventReceived(World world, int pos_x, int pos_y, int pos_z, int p_149696_5_, int p_149696_6_)
    {
        // DEBUG
        System.out.println("RecipeBlockContainer onBlockEventReceived()");
        super.onBlockEventReceived(world, pos_x, pos_y, pos_z, p_149696_5_, p_149696_6_);
        TileEntity tileentity = world.getTileEntity(pos_x, pos_y, pos_z);
        return tileentity != null ? tileentity.receiveClientEvent(p_149696_5_, p_149696_6_) : false;
    }
}