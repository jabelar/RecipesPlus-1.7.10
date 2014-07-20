package com.blogspot.jabelarminecraft.recipesplus.containers;


import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

import com.blogspot.jabelarminecraft.recipesplus.recipes.RecipesStove;
import com.blogspot.jabelarminecraft.recipesplus.slots.SlotStove;
import com.blogspot.jabelarminecraft.recipesplus.tileentities.TileEntityStove;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ContainerStove extends Container
{
    private final TileEntityStove tileStove;
    private int lastCookTime;
    private int lastBurnTime;
    private int lastItemBurnTime;
    private static final String __OBFID = "CL_00001748";

    public ContainerStove(InventoryPlayer par1InventoryPlayer, TileEntityStove par2TileEntityStove)
    {
        // DEBUG
        System.out.println("ContainerStove constructor()"); 

        this.tileStove = par2TileEntityStove;
        
        // add slots
        // first add special slots for stove
        // add slot for input item to be cooked
        this.addSlotToContainer(new SlotStove(par1InventoryPlayer.player, par2TileEntityStove, 0, 56, 17));
        // add slot for fuel
        this.addSlotToContainer(new SlotStove(par1InventoryPlayer.player, par2TileEntityStove, 1, 56, 53));
        // add slot for output that has been cooked
        this.addSlotToContainer(new SlotStove(par1InventoryPlayer.player, par2TileEntityStove, 2, 116, 35));
 
        // DEBUG
        System.out.println("Number of slots = "+ this.inventorySlots.size()); 
    
        // add slots for the inventory items
        int i;
        for (i = 0; i < 3; ++i)
        {
            for (int j = 0; j < 9; ++j)
            {
                this.addSlotToContainer(new Slot(par2TileEntityStove, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
                // DEBUG
                System.out.println("Number of slots = "+ this.inventorySlots.size()); 
            
            }
        }

        // add slots for held items
        for (i = 0; i < 9; ++i)
        {
            this.addSlotToContainer(new Slot(par2TileEntityStove, i, 8 + i * 18, 142));
            // DEBUG
            System.out.println("Number of slots = "+ this.inventorySlots.size()); 
        
        }
    }

    @Override
	public void addCraftingToCrafters(ICrafting par1ICrafting)
    {
        // DEBUG
        System.out.println("ContainerStove addCraftingToCrafters()"); 

        super.addCraftingToCrafters(par1ICrafting);
        par1ICrafting.sendProgressBarUpdate(this, 0, this.tileStove.stoveCookTime);
        par1ICrafting.sendProgressBarUpdate(this, 1, this.tileStove.stoveBurnTime);
        par1ICrafting.sendProgressBarUpdate(this, 2, this.tileStove.currentItemBurnTime);
    }

    /**
     * Looks for changes made in the container, sends them to every listener.
     */
    @Override
	public void detectAndSendChanges()
    {
        super.detectAndSendChanges();

        for (int i = 0; i < this.crafters.size(); ++i)
        {
            ICrafting icrafting = (ICrafting)this.crafters.get(i);

            if (this.lastCookTime != this.tileStove.stoveCookTime)
            {
                icrafting.sendProgressBarUpdate(this, 0, this.tileStove.stoveCookTime);
            }

            if (this.lastBurnTime != this.tileStove.stoveBurnTime)
            {
                icrafting.sendProgressBarUpdate(this, 1, this.tileStove.stoveBurnTime);
            }

            if (this.lastItemBurnTime != this.tileStove.currentItemBurnTime)
            {
                icrafting.sendProgressBarUpdate(this, 2, this.tileStove.currentItemBurnTime);
            }
        }

        this.lastCookTime = this.tileStove.stoveCookTime;
        this.lastBurnTime = this.tileStove.stoveBurnTime;
        this.lastItemBurnTime = this.tileStove.currentItemBurnTime;
    }

    @Override
	@SideOnly(Side.CLIENT)
    public void updateProgressBar(int par1, int par2)
    {
        if (par1 == 0)
        {
            this.tileStove.stoveCookTime = par2;
        }

        if (par1 == 1)
        {
            this.tileStove.stoveBurnTime = par2;
        }

        if (par1 == 2)
        {
            this.tileStove.currentItemBurnTime = par2;
        }
    }

    @Override
	public boolean canInteractWith(EntityPlayer par1EntityPlayer)
    {
        return this.tileStove.isUseableByPlayer(par1EntityPlayer);
    }

    /**
     * Called when a player shift-clicks on a slot. You must override this or you will crash when someone does that.
     */
    @Override
	public ItemStack transferStackInSlot(EntityPlayer par1EntityPlayer, int par2)
    {
        // DEBUG
        System.out.println("ContainerStove transferStackInSlot()"); 

        ItemStack itemstack = null;
        SlotStove slot = (SlotStove)this.inventorySlots.get(par2);

        if (slot != null && slot.getHasStack())
        {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();

            if (par2 == 2)
            {
                if (!this.mergeItemStack(itemstack1, 3, 39, true))
                {
                    return null;
                }

                slot.onSlotChange(itemstack1, itemstack);
            }
            else if (par2 != 1 && par2 != 0)
            {
                if (RecipesStove.cooking().getCookingResult(itemstack1) != null)
                {
                    if (!this.mergeItemStack(itemstack1, 0, 1, false))
                    {
                        return null;
                    }
                }
                else if (TileEntityStove.isItemFuel(itemstack1))
                {
                    if (!this.mergeItemStack(itemstack1, 1, 2, false))
                    {
                        return null;
                    }
                }
                else if (par2 >= 3 && par2 < 30)
                {
                    if (!this.mergeItemStack(itemstack1, 30, 39, false))
                    {
                        return null;
                    }
                }
                else if (par2 >= 30 && par2 < 39 && !this.mergeItemStack(itemstack1, 3, 30, false))
                {
                    return null;
                }
            }
            else if (!this.mergeItemStack(itemstack1, 3, 39, false))
            {
                return null;
            }

            if (itemstack1.stackSize == 0)
            {
                slot.putStack((ItemStack)null);
            }
            else
            {
                slot.onSlotChanged();
            }

            if (itemstack1.stackSize == itemstack.stackSize)
            {
                return null;
            }

            slot.onPickupFromSlot(par1EntityPlayer, itemstack1);
        }

        return itemstack;
    }
}