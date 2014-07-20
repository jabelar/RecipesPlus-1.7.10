package com.blogspot.jabelarminecraft.recipesplus.tileentities;


import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemHoe;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.item.ItemTool;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;

import com.blogspot.jabelarminecraft.recipesplus.blocks.BlockStove;
import com.blogspot.jabelarminecraft.recipesplus.recipes.RecipesStove;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class TileEntityStove extends TileEntity implements ISidedInventory
{
    private static final int[] slotsTop = new int[] {0};
    private static final int[] slotsBottom = new int[] {2, 1};
    private static final int[] slotsSides = new int[] {1};
    /**
     * The ItemStacks that hold the items currently being used in the stove
     */
    // [0] is input itemstack to be cooked
    // [1] is fuel itemstack
    // [2] is output itemstack after cooking
    private ItemStack[] stoveItemStacks = new ItemStack[3];
    /**
     * The number of ticks that the stove will keep burning
     */
    public int stoveBurnTime;
    /**
     * The number of ticks that a fresh copy of the currently-burning item would keep the stove burning for
     */
    public int currentItemBurnTime;
    /**
     * The number of ticks that the current item has been cooking for
     */
    public int stoveCookTime;
    private String inventory_name;

    /**
     * Returns the number of slots in the inventory.
     */
    @Override
	public int getSizeInventory()
    {
        // DEBUG
        System.out.println("TileEntityStove getSizeInventory()");
        return this.stoveItemStacks.length;

    }

    /**
     * Returns the stack in slot i
     */
    @Override
	public ItemStack getStackInSlot(int par1)
    {
        // this is called for each slot every tick while in the gui
        // DEBUG
        System.out.println(par1+" of "+this.stoveItemStacks.length);

        return this.stoveItemStacks[par1];
    }

    /**
     * Removes from an inventory slot (first arg) up to a specified number (second arg) of items and returns them in a
     * new stack.
     */
    @Override
	public ItemStack decrStackSize(int slot_num, int amount_to_take)
    {
        // DEBUG
        System.out.println("TileEntityStove decrStackSize()");
        if (this.stoveItemStacks[slot_num] != null)
        {
            ItemStack itemstack;

            if (this.stoveItemStacks[slot_num].stackSize <= amount_to_take)
            {
                itemstack = this.stoveItemStacks[slot_num];
                this.stoveItemStacks[slot_num] = null;
                return itemstack;
            }
            else
            {
                itemstack = this.stoveItemStacks[slot_num].splitStack(amount_to_take);

                if (this.stoveItemStacks[slot_num].stackSize == 0)
                {
                    this.stoveItemStacks[slot_num] = null;
                }

                return itemstack;
            }
        }
        else
        {
            return null;
        }
    }

    /**
     * When some containers are closed they call this on each slot, then drop whatever it returns as an EntityItem -
     * like when you close a workbench GUI.
     */
    @Override
	public ItemStack getStackInSlotOnClosing(int slot_num)
    {
        // DEBUG
        System.out.println("TileEntityStove getStackinSlotOnClosing()");
        if (this.stoveItemStacks[slot_num] != null)
        {
            ItemStack itemstack = this.stoveItemStacks[slot_num];
            this.stoveItemStacks[slot_num] = null;
            return itemstack;
        }
        else
        {
            return null;
        }
    }

    /**
     * Sets the given item stack to the specified slot in the inventory (can be crafting or armor sections).
     */
    @Override
	public void setInventorySlotContents(int slot_num, ItemStack item_stack)
    {
        // DEBUG
        System.out.println("TileEntityStove setInventorySlotContents()");
        this.stoveItemStacks[slot_num] = item_stack;

        // enforce stack size limit
        if (item_stack != null && item_stack.stackSize > this.getInventoryStackLimit())
        {
            item_stack.stackSize = this.getInventoryStackLimit();
        }
    }

    /**
     * Returns the name of the inventory
     */
    @Override
	public String getInventoryName()
    {
        // this is called every tick in the gui
        return this.hasCustomInventoryName() ? this.inventory_name : "container.stove";
    }

    /**
     * Returns if the inventory is named
     */
    @Override
	public boolean hasCustomInventoryName()
    {
        // this is called every tick when in the gui
        return this.inventory_name != null && this.inventory_name.length() > 0;
    }

    public void setEntityName(String name)
    {
        // DEBUG
        System.out.println("TileEntityStove setEntityName()");
        this.inventory_name = name;
    }

    @Override
	public void readFromNBT(NBTTagCompound tag_compound)
    {
        // DEBUG
        System.out.println("TileEntityStove readFromNBT()");
        super.readFromNBT(tag_compound);
        NBTTagList nbttaglist = tag_compound.getTagList("Items", 10);
        this.stoveItemStacks = new ItemStack[this.getSizeInventory()];

        for (int i = 0; i < nbttaglist.tagCount(); ++i)
        {
            NBTTagCompound nbttagcompound1 = nbttaglist.getCompoundTagAt(i);
            byte slot_num = nbttagcompound1.getByte("Slot");
            
            // DEBUG
            System.out.println("Reading slot #"+slot_num);

            if (slot_num >= 0 && slot_num < this.stoveItemStacks.length)
            {
                this.stoveItemStacks[slot_num] = ItemStack.loadItemStackFromNBT(nbttagcompound1);
            }
        }

        this.stoveBurnTime = tag_compound.getShort("BurnTime");
        this.stoveCookTime = tag_compound.getShort("CookTime");
        this.currentItemBurnTime = getItemBurnTime(this.stoveItemStacks[1]);

        if (tag_compound.hasKey("CustomName", 8))
        {
            this.inventory_name = tag_compound.getString("CustomName");
            // DEBUG
            System.out.println("Inventory name = "+inventory_name);

        }
    }

    @Override
	public void writeToNBT(NBTTagCompound nbt_tag_compound)
    {
        // DEBUG
        System.out.println("TileEntityStove writeToNBT()");
        super.writeToNBT(nbt_tag_compound);
        nbt_tag_compound.setShort("BurnTime", (short)this.stoveBurnTime);
        nbt_tag_compound.setShort("CookTime", (short)this.stoveCookTime);
        NBTTagList nbttaglist = new NBTTagList();

        for (int i = 0; i < this.stoveItemStacks.length; ++i)
        {
            if (this.stoveItemStacks[i] != null)
            {
                NBTTagCompound nbttagcompound1 = new NBTTagCompound();
                nbttagcompound1.setByte("Slot", (byte)i);
                this.stoveItemStacks[i].writeToNBT(nbttagcompound1);
                nbttaglist.appendTag(nbttagcompound1);
            }
        }

        nbt_tag_compound.setTag("Items", nbttaglist);

        if (this.hasCustomInventoryName())
        {
            nbt_tag_compound.setString("CustomName", this.inventory_name);
            // DEBUG
            System.out.println("Inventory name = "+inventory_name);

        }
    }

    /**
     * Returns the maximum stack size for a inventory slot.
     */
    @Override
	public int getInventoryStackLimit()
    {
        // DEBUG
        System.out.println("TileEntityStove getInventoryStackLimit()");
        return 64;
    }

    /**
     * Returns an integer between 0 and the passed value representing how close the current item is to being completely
     * cooked
     */
    @SideOnly(Side.CLIENT)
    public int getCookProgressScaled(int p_145953_1_)
    {
        // this is called every tick when in the gui
        return this.stoveCookTime * p_145953_1_ / 200;
    }

    /**
     * Returns an integer between 0 and the passed value representing how much burn time is left on the current fuel
     * item, where 0 means that the item is exhausted and the passed value means that the item is fresh
     */
    @SideOnly(Side.CLIENT)
    public int getBurnTimeRemainingScaled(int p_145955_1_)
    {
        // DEBUG
        System.out.println("TileEntityStove getBurnTimeRemainingScaled()");

    	if (this.currentItemBurnTime == 0)
        {
            this.currentItemBurnTime = 200;
        }

        return this.stoveBurnTime * p_145955_1_ / this.currentItemBurnTime;
    }

    /**
     * Stove isBurning
     */
    public boolean isBurning()
    {
        return this.stoveBurnTime > 0;
    }

    @Override
	public void updateEntity()
    {
        boolean flag = this.stoveBurnTime > 0;
        boolean still_burning = false;

        if (this.stoveBurnTime > 0)
        {
            --this.stoveBurnTime;
        }

        if (!this.worldObj.isRemote)
        {
            if (this.stoveBurnTime == 0 && this.canCook())
            {
                this.currentItemBurnTime = this.stoveBurnTime = getItemBurnTime(this.stoveItemStacks[1]);

                if (this.stoveBurnTime > 0)
                {
                    still_burning = true;

                    if (this.stoveItemStacks[1] != null)
                    {
                        --this.stoveItemStacks[1].stackSize;

                        if (this.stoveItemStacks[1].stackSize == 0)
                        {
                            this.stoveItemStacks[1] = stoveItemStacks[1].getItem().getContainerItem(stoveItemStacks[1]);
                        }
                    }
                }
            }

            if (this.isBurning() && this.canCook())
            {
                ++this.stoveCookTime;

                if (this.stoveCookTime == 200)
                {
                    this.stoveCookTime = 0;
                    this.cookItem();
                    still_burning = true;
                }
            }
            else
            {
                this.stoveCookTime = 0;
            }

            if (flag != this.stoveBurnTime > 0)
            {
                still_burning = true;
                BlockStove.updateStoveBlockState(this.stoveBurnTime > 0, this.worldObj, this.xCoord, this.yCoord, this.zCoord);
            }
        }

        if (still_burning)
        {
        	// ensure changes to chunk are saved to disk
            this.markDirty();
        }
    }

    /**
     * Returns true if the stove can cook an item, i.e. has a source item, destination stack isn't full, etc.
     */
    private boolean canCook()
    {
        // this is called every tick even when not in the gui
    	
    	// if nothing in input slot then can't cook
        if (this.stoveItemStacks[0] == null)
        {
            return false;
        }
        else
        {
        	// check if the input item to be cooked can be cooked
            ItemStack cook_result = RecipesStove.cooking().getCookingResult(this.stoveItemStacks[0]);
            if (cook_result == null) return false;
            if (this.stoveItemStacks[2] == null) return true;
            if (!this.stoveItemStacks[2].isItemEqual(cook_result)) return false;
            int result = stoveItemStacks[2].stackSize + cook_result.stackSize;
            return result <= getInventoryStackLimit() && result <= this.stoveItemStacks[2].getMaxStackSize(); //Forge BugFix: Make it respect stack sizes properly.
        }
    }

    /**
     * Turn one item from the stove source stack into the appropriate cooked item in the stove result stack
     */
    public void cookItem()
    {
        // DEBUG
        System.out.println("TileEntityStove cookItem()");
        if (this.canCook())
        {
            ItemStack cook_result = RecipesStove.cooking().getCookingResult(this.stoveItemStacks[0]);

            if (this.stoveItemStacks[2] == null)
            {
                this.stoveItemStacks[2] = cook_result.copy();
            }
            else if (this.stoveItemStacks[2].getItem() == cook_result.getItem())
            {
                this.stoveItemStacks[2].stackSize += cook_result.stackSize; // Forge BugFix: Results may have multiple items
            }

            --this.stoveItemStacks[0].stackSize;

            if (this.stoveItemStacks[0].stackSize <= 0)
            {
                this.stoveItemStacks[0] = null;
            }
        }
    }

    /**
     * Returns the number of ticks that the supplied fuel item will keep the stove burning, or 0 if the item isn't
     * fuel
     */
    public static int getItemBurnTime(ItemStack item_stack)
    {
        // DEBUG
        System.out.println("TileEntityStove getItemBurnTime()");
        if (item_stack == null)
        {
            return 0;
        }
        else
        {
            Item item = item_stack.getItem();
            
            // fuels that are blocks
            if (item instanceof ItemBlock && Block.getBlockFromItem(item) != Blocks.air)
            {
                Block block = Block.getBlockFromItem(item);

                if (block == Blocks.wooden_slab)
                {
                    return 150;
                }

                if (block.getMaterial() == Material.wood)
                {
                    return 300;
                }

                if (block == Blocks.coal_block)
                {
                    return 16000;
                }
            }

            // fuels that are items
            if (item instanceof ItemTool && ((ItemTool)item).getToolMaterialName().equals("WOOD")) return 200;
            if (item instanceof ItemSword && ((ItemSword)item).getToolMaterialName().equals("WOOD")) return 200;
            if (item instanceof ItemHoe && ((ItemHoe)item).getToolMaterialName().equals("WOOD")) return 200;
            if (item == Items.stick) return 100;
            if (item == Items.coal) return 1600;
            if (item == Items.lava_bucket) return 20000;
            if (item == Item.getItemFromBlock(Blocks.sapling)) return 100;
            if (item == Items.blaze_rod) return 2400;
            return GameRegistry.getFuelValue(item_stack);
        }
    }

    public static boolean isItemFuel(ItemStack item_stack)
    {
        // DEBUG
        System.out.println("TileEntityStove isItemFuel()");
    	// returns true if it has a burn time greater than 0
        return getItemBurnTime(item_stack) > 0;
    }

    /**
     * Do not make give this method the name canInteractWith because it clashes with Container
     */
    @Override
	public boolean isUseableByPlayer(EntityPlayer par1EntityPlayer)
    {
        // DEBUG
        System.out.println("TileEntityStove isUseableByPlayer()");
    	// usable if player is close enough
        return this.worldObj.getTileEntity(this.xCoord, this.yCoord, this.zCoord) != this ? false : par1EntityPlayer.getDistanceSq(this.xCoord + 0.5D, this.yCoord + 0.5D, this.zCoord + 0.5D) <= 64.0D;
    }

    @Override
	public void openInventory() {}

    @Override
	public void closeInventory() {}

    /**
     * Returns true if automation is allowed to insert the given stack (ignoring stack size) into the given slot.
     */
    @Override
	public boolean isItemValidForSlot(int par1, ItemStack item_stack)
    {
        // DEBUG
        System.out.println("TileEntityStove isItemValidForSlot()");
        return par1 == 2 ? false : (par1 == 1 ? isItemFuel(item_stack) : true);
    }

    /**
     * Returns an array containing the indices of the slots that can be accessed by automation on the given side of this
     * block.
     */
    @Override
	public int[] getAccessibleSlotsFromSide(int par1)
    {
        // DEBUG
        System.out.println("TileEntityStove getAccessibleSlotsFromSide()");
        return par1 == 0 ? slotsBottom : (par1 == 1 ? slotsTop : slotsSides);
    }

    /**
     * Returns true if automation can insert the given item in the given slot from the given side. Args: Slot, item,
     * side
     */
    @Override
	public boolean canInsertItem(int par1, ItemStack item_stack, int par3)
    {
        // DEBUG
        System.out.println("TileEntityStove canInsertItem()");
        return this.isItemValidForSlot(par1, item_stack);
    }

    /**
     * Returns true if automation can extract the given item in the given slot from the given side. Args: Slot, item,
     * side
     */
    @Override
	public boolean canExtractItem(int par1, ItemStack item_stack, int par3)
    {
        // DEBUG
        System.out.println("TileEntityStove canExtractItem()");
       return par3 != 0 || par1 != 1 || item_stack.getItem() == Items.bucket;
    }
}
