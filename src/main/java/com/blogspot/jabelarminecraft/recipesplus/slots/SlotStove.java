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

package com.blogspot.jabelarminecraft.recipesplus.slots;

import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.AchievementList;
import net.minecraft.util.MathHelper;

import com.blogspot.jabelarminecraft.recipesplus.recipes.RecipesStove;

import cpw.mods.fml.common.FMLCommonHandler;

public class SlotStove extends Slot
{
    /**
     * The player that is using the GUI where this slot resides.
     */
    private final EntityPlayer thePlayer;
    private int field_75228_b;

    public SlotStove(EntityPlayer par1EntityPlayer, IInventory par2IInventory, int par3, int par4, int par5)
    {
        super(par2IInventory, par3, par4, par5);
        this.thePlayer = par1EntityPlayer;
        // DEBUG
        System.out.println("SlotStove constructor()"); 

    }

    /**
     * Check if the stack is a valid item for this slot. Always true beside for the armor slots.
     */
    @Override
	public boolean isItemValid(ItemStack par1ItemStack)
    {
        // DEBUG
        System.out.println("SlotStove isItemValid()"); 

        return false;
    }

    /**
     * Decrease the size of the stack in slot (first int arg) by the amount of the second int arg. Returns the new
     * stack.
     */
    @Override
	public ItemStack decrStackSize(int par1)
    {
        // DEBUG
        System.out.println("SlotStove decrStackSize()"); 

        if (this.getHasStack())
        {
            this.field_75228_b += Math.min(par1, this.getStack().stackSize);
        }

        return super.decrStackSize(par1);
    }

    @Override
	public void onPickupFromSlot(EntityPlayer par1EntityPlayer, ItemStack par2ItemStack)
    {
        // DEBUG
        System.out.println("SlotStove onPickupFromSlot()"); 

       this.onCrafting(par2ItemStack);
        super.onPickupFromSlot(par1EntityPlayer, par2ItemStack);
    }

    /**
     * the itemStack passed in is the output - ie, iron ingots, and pickaxes, not ore and wood. Typically increases an
     * internal count then calls onCrafting(item).
     */
    @Override
	protected void onCrafting(ItemStack par1ItemStack, int par2)
    {
        // DEBUG
        System.out.println("SlotStove onCrafting()"); 

        this.field_75228_b += par2;
        this.onCrafting(par1ItemStack);
    }

    /**
     * the itemStack passed in is the output - ie, iron ingots, and pickaxes, not ore and wood.
     */
    @Override
	protected void onCrafting(ItemStack par1ItemStack)
    {
        // DEBUG
        System.out.println("SlotStove onCrafting()"); 

        par1ItemStack.onCrafting(this.thePlayer.worldObj, this.thePlayer, this.field_75228_b);

        if (!this.thePlayer.worldObj.isRemote)
        {
            int i = this.field_75228_b;
            float f = RecipesStove.cooking().func_151398_b(par1ItemStack);
            int j;

            if (f == 0.0F)
            {
                i = 0;
            }
            else if (f < 1.0F)
            {
                j = MathHelper.floor_float(i * f);

                if (j < MathHelper.ceiling_float_int(i * f) && (float)Math.random() < i * f - j)
                {
                    ++j;
                }

                i = j;
            }

            while (i > 0)
            {
                j = EntityXPOrb.getXPSplit(i);
                i -= j;
                this.thePlayer.worldObj.spawnEntityInWorld(new EntityXPOrb(this.thePlayer.worldObj, this.thePlayer.posX, this.thePlayer.posY + 0.5D, this.thePlayer.posZ + 0.5D, j));
            }
        }

        this.field_75228_b = 0;

        FMLCommonHandler.instance().firePlayerSmeltedEvent(thePlayer, par1ItemStack);

        if (par1ItemStack.getItem() == Items.iron_ingot)
        {
            this.thePlayer.addStat(AchievementList.acquireIron, 1);
        }

        if (par1ItemStack.getItem() == Items.cooked_fished)
        {
            this.thePlayer.addStat(AchievementList.cookFish, 1);
        }
    }
}