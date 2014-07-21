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

package com.blogspot.jabelarminecraft.recipesplus.tileentities;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;

public class TileEntityPantry extends TileEntity implements IInventory
{
	private static ItemStack[] inventory ;
	
	public TileEntityPantry()
	{
		inventory = new ItemStack[40];
        // DEBUG
        System.out.println("TileEntityPantry constructor()"); 

	}
		
	@Override
	public int getSizeInventory() 
	{
		return inventory.length;
	}

	@Override
	public ItemStack getStackInSlot(int slot) 
	{
		// called every tick for every slot
		return inventory[slot];
	}

	@Override
	// Note this actually clears the slot if it goes to zero
	public ItemStack decrStackSize(int slot, int amount) 
	{
        ItemStack item_stack = getStackInSlot(slot);
        
        // DEBUG
        System.out.println("TileEntityPantry decrStackSize(): Client side = "+this.worldObj.isRemote+": Slot = "+slot+": Item ="+item_stack.getDisplayName()+": Initial Size ="+item_stack.stackSize+": Decr by = "+amount); 

        if (item_stack != null) // if something in the slot
        {
                if (item_stack.stackSize <= amount) // if there is less than you want to take
                {
                        setInventorySlotContents(slot, null);
                } 
                else // there is more than you want so split the stack, leaving remainder
                {
                	item_stack = item_stack.splitStack(amount);
                 }
        }
        
        // DEBUG
        System.out.println("TileEntityPantry decrStackSize()"+": Item ="+item_stack.getDisplayName()+": Resulting Size ="+item_stack.stackSize); 

        return item_stack;
	}

	@Override
	public ItemStack getStackInSlotOnClosing(int slot) 
	{
		// get stack and store temporarily while clearing slot
		ItemStack item_stack = getStackInSlot(slot);
        
		// DEBUG
        System.out.println("TileEntityPantry getStackInSlotOnClosing(): Client side = "+this.worldObj.isRemote+": Slot = "+slot+": Item ="+item_stack.getDisplayName()+": Stack Size ="+item_stack.stackSize); 

		setInventorySlotContents(slot, null);
		return item_stack;
	}

	@Override
	public void setInventorySlotContents(int slot, ItemStack item_stack) 
	{
        // DEBUG
        System.out.println("TileEntityPantry setInventorySlotContents(): Client side = "+this.worldObj.isRemote+": Slot = "+slot+": Item ="+item_stack.getDisplayName()+": Stack Size ="+item_stack.stackSize); 

        inventory[slot]=item_stack;
        // enforce stacksize limit
        if (item_stack != null && item_stack.stackSize > getInventoryStackLimit()) 
        {
        	item_stack.stackSize = getInventoryStackLimit();
        }
	}

	@Override
	public String getInventoryName() 
	{
        // called every tick
		return "Pantry";
	}

	@Override
	public boolean hasCustomInventoryName() 
	{
		return this.getInventoryName()!=null;
	}

	@Override
	public int getInventoryStackLimit() 
	{
		return 64;
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer player) 
	{
        return worldObj.getTileEntity(xCoord, yCoord, zCoord) == this &&
        player.getDistanceSq(xCoord + 0.5, yCoord + 0.5, zCoord + 0.5) < 64;
	}

	@Override
	public void openInventory() 
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void closeInventory() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isItemValidForSlot(int slot, ItemStack item_stack) 
	{
        // DEBUG
        System.out.println("TileEntityPantry isItemValidForSlot(): Server side = "+this.worldObj.isRemote+": Slot = "+slot+": Item ="+item_stack.getDisplayName()+": Stack Size ="+item_stack.stackSize); 

		return true;
	}

}
