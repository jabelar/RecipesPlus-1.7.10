package com.blogspot.jabelarminecraft.recipesplus.slots;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class SlotPantry extends Slot
{

    public SlotPantry(IInventory i_inventory, int slot, int pos_x, int pos_y) 

 	{
		super(i_inventory, slot, pos_x, pos_y);
		// DEBUG
        System.out.println("SlotPantry constructor()"+": Slot = "+slot); 

	}

    /**
     * if par2 has more items than par1, onCrafting(item,countIncrease) is called
     */
    @Override
    public void onSlotChange(ItemStack par1ItemStack, ItemStack par2ItemStack)
    {
		// DEBUG
        System.out.println("SlotPantry onSlotChange()"+": Slot = "+this.slotNumber); 

        if (par1ItemStack != null && par2ItemStack != null)
        {
            if (par1ItemStack.getItem() == par2ItemStack.getItem())
            {
                int i = par2ItemStack.stackSize - par1ItemStack.stackSize;

                if (i > 0)
                {
                    this.onCrafting(par1ItemStack, i);
                }
            }
        }
    }

    /**
     * the itemStack passed in is the output - ie, iron ingots, and pickaxes, not ore and wood. Typically increases an
     * internal count then calls onCrafting(item).
     */
    @Override
    protected void onCrafting(ItemStack par1ItemStack, int par2) {}

    /**
     * the itemStack passed in is the output - ie, iron ingots, and pickaxes, not ore and wood.
     */
    @Override
    protected void onCrafting(ItemStack par1ItemStack) {}

    @Override
    // Doesn't do much except mark the inventory "dirty" for save management
    public void onPickupFromSlot(EntityPlayer par1EntityPlayer, ItemStack par2ItemStack)
    {
        this.onSlotChanged();
    }

    /**
     * Check if the stack is a valid item for this slot. Always true beside for the armor slots.
     */
    @Override
    public boolean isItemValid(ItemStack par1ItemStack)
    {
		// DEBUG
    	if (par1ItemStack != null)
    	{
    		System.out.println("SlotPantry isItemValid()"+": Slot = "+slotNumber+": Item ="+par1ItemStack.getDisplayName()); 
    	}
        return true;
    }

    /**
     * Helper fnct to get the stack in the slot.
     */
    @Override
    public ItemStack getStack()
    {
       return this.inventory.getStackInSlot(this.slotNumber);
    }

    /**
     * Returns if this slot contains a stack.
     */
    @Override
    public boolean getHasStack()
    {
       return this.getStack() != null;
    }

    /**
     * Helper method to put a stack in the slot.
     */
    @Override
    // Puts stack into slot in TileEntity
    public void putStack(ItemStack par1ItemStack)
    {
		// DEBUG
        System.out.println("SlotPantry putStack()"+": Slot = "+this.slotNumber+": Item ="+par1ItemStack.getDisplayName()+": Stack Size ="+par1ItemStack.stackSize); 

       this.inventory.setInventorySlotContents(this.slotNumber, par1ItemStack);
       this.onSlotChanged();
    }

    /**
     * Called when the stack in a Slot changes
     */
    @Override
    // Doesn't do much except mark the inventory "dirty" for save management
    public void onSlotChanged()
    {
        this.inventory.markDirty();
    }

    /**
     * Returns the maximum stack size for a given slot (usually the same as getInventoryStackLimit(), but 1 in the case
     * of armor slots)
     */
    @Override
    public int getSlotStackLimit()
    {
		// DEBUG
        System.out.println("SlotPantry getSlotStackLimit()"+": Slot = "+slotNumber); 

        return this.inventory.getInventoryStackLimit();
    }

    /**
     * Decrease the size of the stack in slot (first int arg) by the amount of the second int arg. Returns the new
     * stack.
     */
    @Override
    public ItemStack decrStackSize(int par1)
    {
		// DEBUG
        System.out.println("SlotPantry decrStackSize()"+": Slot = "+slotNumber+": Decr by ="+par1); 

        return this.inventory.decrStackSize(this.slotNumber, par1);
    }

    /**
     * returns true if this slot is in par2 of par1
     */
    @Override
    public boolean isSlotInInventory(IInventory par1IInventory, int par2)
    {
		// DEBUG
        System.out.println("SlotPantry isSlotInInventory()"+": Slot = "+slotNumber); 

       return par1IInventory == this.inventory && par2 == this.slotNumber;
    }

    /**
     * Return whether this slot's stack can be taken from this slot.
     */
    @Override
    public boolean canTakeStack(EntityPlayer par1EntityPlayer)
    {
		// DEBUG
        System.out.println("SlotPantry canTakeStack()"+": Slot = "+slotNumber); 

    	return true;
    }

}