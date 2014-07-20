package com.blogspot.jabelarminecraft.recipesplus.containers;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;

import com.blogspot.jabelarminecraft.recipesplus.tileentities.TileEntityPantry;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ContainerPantry extends Container
{

	protected TileEntityPantry tile_entity_pantry ;
	protected int num_custom_slots;
	protected int num_hotbar_slots;
	protected int num_player_inventory_slots;
    private int field_94535_f = -1;
    private int item_meta_data;
    private final Set hash_set = new HashSet();
	
	public ContainerPantry(InventoryPlayer player, TileEntityPantry tep)
	{
		tile_entity_pantry = tep ;
		num_custom_slots = 4 ;
		num_hotbar_slots = 9 ;
		num_player_inventory_slots = 3*9 ;

		// DEBUG
        System.out.println("ContainerPantry constructor(): Client side ="+tile_entity_pantry.getWorldObj().isRemote); 
		
		// add pantry slots to match tile entity (2 x 2 matrix)
        for (int i = 0; i < 2 ; i++) 
        {
            for (int j = 0; j < 2; j++) 
            {
            	addSlotToContainer(new Slot(tile_entity_pantry, j + i * 2, 62 + j * 30, 20 + i * 30));
                // DEBUG
                System.out.println("Number of slots = "+ this.inventorySlots.size()); 
            
            }
	    }

        // add player inventory slots (including hotslot items)
        bindPlayerInventory(player);
        
	}
	
	@Override
	public boolean canInteractWith(EntityPlayer player) 
	{
		return tile_entity_pantry.isUseableByPlayer(player);
	}

    protected void bindPlayerInventory(InventoryPlayer inventoryPlayer) 
    {
        // DEBUG
        System.out.println("ContainerPantry bindPlayerInventory(): Client side ="+tile_entity_pantry.getWorldObj().isRemote);
        for (int i = 0; i < 3; i++) 
        {
            for (int j = 0; j < 9; j++) 
            {
                    addSlotToContainer(new Slot(inventoryPlayer, j + i * 9 + num_hotbar_slots,
                                    8 + j * 18, 84 + i * 18));
                    // DEBUG
                    System.out.println("Number of slots = "+ this.inventorySlots.size());                    
            }
	    }
	
	    for (int i = 0; i < num_hotbar_slots; i++) 
	    {
	            addSlotToContainer(new Slot(inventoryPlayer, i, 8 + i * 18, 142));

	            // DEBUG
	            System.out.println("Number of slots = "+ this.inventorySlots.size()); 	            
	    }
    }
    
    /**
     * the slot is assumed empty
     */
    @Override
	protected Slot addSlotToContainer(Slot par1Slot)
    {
        par1Slot.slotNumber = this.inventorySlots.size();
        this.inventorySlots.add(par1Slot);
        this.inventoryItemStacks.add((Object)null);
        return par1Slot;
    }
    
    @Override
    public ItemStack transferStackInSlot(EntityPlayer player, int slot) 
    {
        // DEBUG
        System.out.println("ContainerPantry transferStackInSlot(): Client side ="+tile_entity_pantry.getWorldObj().isRemote+": Slot = "+slot);

            ItemStack stack = null;
            // TODO might have to check whether it is a pantry slot and handle appropriately
            
            Slot slotObject = (Slot) inventorySlots.get(slot);

            // if there is a stack in the slot process the transfer
            if (slotObject != null && slotObject.getHasStack()) 
            {
                    ItemStack stackInSlot = slotObject.getStack();
                    stack = stackInSlot.copy();

                    // merges the item into player inventory
                    // and returns null in the case that no merge was possible (due to all slots being occupied)
                    if (!this.mergeItemStack(stackInSlot, 0, 39, false)) 
                        {
                                return null;
                        }

                    // erase stack if size goes to 0
                    if (stackInSlot.stackSize == 0) 
                    {
                            slotObject.putStack(null);
                    } 
                    else // process change, essentially mark dirty
                    {
                            slotObject.onSlotChanged();
                    }

                    if (stackInSlot.stackSize == stack.stackSize) 
                    {
                            return null;
                    }
                    
                    // basically just calls onSlotChanged which calls markDirty which ensures save data is up to date
                    slotObject.onPickupFromSlot(player, stackInSlot);
            }
            return stack;
    }

    public static int bitShiftRightByTwoThenMaskWith3(int par0)
    {
        return par0 >> 2 & 3;
    }

    public static int bitMaskWith3(int par0)
    {
        return par0 & 3;
    }

    @SideOnly(Side.CLIENT)
    public static int bitMaskWith3ThenOrTogetherThenShiftLeftTwo(int par0, int par1)
    {
        return par0 & 3 | (par1 & 3) << 2;
    }

    public static boolean isEqualTo0Or1(int par0)
    {
        return par0 == 0 || par0 == 1;
    }

    protected void clearSet()
    {
        this.item_meta_data = 0;
        this.hash_set.clear();
    }

    public static boolean func_94527_a(Slot par0Slot, ItemStack par1ItemStack, boolean par2)
    {
        boolean slot_empty = par0Slot == null || !par0Slot.getHasStack();

        // if slot and stack exist and slot is not empty and stack contains same item type
        if (par0Slot != null && par0Slot.getHasStack() && par1ItemStack != null && par1ItemStack.isItemEqual(par0Slot.getStack()) && ItemStack.areItemStackTagsEqual(par0Slot.getStack(), par1ItemStack))
        {
            int i = par2 ? 0 : par1ItemStack.stackSize;
            slot_empty |= par0Slot.getStack().stackSize + i <= par1ItemStack.getMaxStackSize();
        }

        return slot_empty;
    }

    public static void func_94525_a(Set par0Set, int par1, ItemStack par2ItemStack, int par3)
    {
        switch (par1)
        {
            case 0:
                par2ItemStack.stackSize = MathHelper.floor_float((float)par2ItemStack.stackSize / (float)par0Set.size());
                break;
            case 1:
                par2ItemStack.stackSize = 1;
        }

        par2ItemStack.stackSize += par3;
    }

    @Override
	public ItemStack slotClick(int slot_num, int mouse_event_type, int par3, EntityPlayer entity_player)
    {
        ItemStack itemstack = null;
        InventoryPlayer inventory_player = entity_player.inventory;
        int i1;
        ItemStack itemstack3;

        if (par3 == 5)
        {
            int l = this.item_meta_data;
            this.item_meta_data = func_94532_c(mouse_event_type);

            if ((l != 1 || this.item_meta_data != 2) && l != this.item_meta_data)
            {
                this.func_94533_d();
            }
            else if (inventory_player.getItemStack() == null)
            {
                this.func_94533_d();
            }
            else if (this.item_meta_data == 0)
            {
                this.field_94535_f = func_94529_b(mouse_event_type);

                if (func_94528_d(this.field_94535_f))
                {
                    this.item_meta_data = 1;
                    this.hash_set.clear();
                }
                else
                {
                    this.func_94533_d();
                }
            }
          else if (this.item_meta_data == 1)
          {

              Slot slot = (Slot)this.inventorySlots.get(slot_num);

              if (slot != null && func_94527_a(slot, inventory_player.getItemStack(), true) && slot.isItemValid(inventory_player.getItemStack()) && inventory_player.getItemStack().stackSize > this.hash_set.size() && this.canDragIntoSlot(slot))
              {
                  this.hash_set.add(slot);
              }
            }
            else if (this.item_meta_data == 2)
            {
                if (!this.hash_set.isEmpty())
                {
                    itemstack3 = inventory_player.getItemStack().copy();
                    i1 = inventory_player.getItemStack().stackSize;
                    Iterator iterator = this.hash_set.iterator();

                    while (iterator.hasNext())
                    {
                        Slot slot1 = (Slot)iterator.next();

                        if (slot1 != null && func_94527_a(slot1, inventory_player.getItemStack(), true) && slot1.isItemValid(inventory_player.getItemStack()) && inventory_player.getItemStack().stackSize >= this.hash_set.size() && this.canDragIntoSlot(slot1))
                        {
                            ItemStack itemstack1 = itemstack3.copy();
                            int j1 = slot1.getHasStack() ? slot1.getStack().stackSize : 0;
                            func_94525_a(this.hash_set, this.field_94535_f, itemstack1, j1);

                            if (itemstack1.stackSize > itemstack1.getMaxStackSize())
                            {
                                itemstack1.stackSize = itemstack1.getMaxStackSize();
                            }

                            if (itemstack1.stackSize > slot1.getSlotStackLimit())
                            {
                                itemstack1.stackSize = slot1.getSlotStackLimit();
                            }

                            i1 -= itemstack1.stackSize - j1;
                            slot1.putStack(itemstack1);
                        }
                    }

                    itemstack3.stackSize = i1;

                    if (itemstack3.stackSize <= 0)
                    {
                        itemstack3 = null;
                    }

                    inventory_player.setItemStack(itemstack3);
                }

                this.func_94533_d();
            }
            else
            {
                this.func_94533_d();
            }
        }
        else if (this.item_meta_data != 0)
        {
            this.func_94533_d();
        }
        else
        {
            Slot slot2;
            int i2;
            ItemStack itemstack5;

            if ((par3 == 0 || par3 == 1) && (mouse_event_type == 0 || mouse_event_type == 1))
            {
                if (slot_num == -999)
                {
                    if (inventory_player.getItemStack() != null && slot_num == -999)
                    {
                        if (mouse_event_type == 0)
                        {
                            entity_player.dropPlayerItemWithRandomChoice(inventory_player.getItemStack(), true);
                            inventory_player.setItemStack((ItemStack)null);
                        }

                        if (mouse_event_type == 1)
                        {
                            entity_player.dropPlayerItemWithRandomChoice(inventory_player.getItemStack().splitStack(1), true);

                            if (inventory_player.getItemStack().stackSize == 0)
                            {
                                inventory_player.setItemStack((ItemStack)null);
                            }
                        }
                    }
                }
                else if (par3 == 1)
                {
                    if (slot_num < 0)
                    {
                        return null;
                    }

                    slot2 = (Slot)this.inventorySlots.get(slot_num);

                    if (slot2 != null && slot2.canTakeStack(entity_player))
                    {
                        itemstack3 = this.transferStackInSlot(entity_player, slot_num);

                        if (itemstack3 != null)
                        {
                            Item item = itemstack3.getItem();
                            itemstack = itemstack3.copy();

                            if (slot2.getStack() != null && slot2.getStack().getItem() == item)
                            {
                                this.retrySlotClick(slot_num, mouse_event_type, true, entity_player);
                            }
                        }
                    }
                }
                else
                {
                    if (slot_num < 0)
                    {
                        return null;
                    }

                    slot2 = (Slot)this.inventorySlots.get(slot_num);

                    if (slot2 != null)
                    {
                        itemstack3 = slot2.getStack();
                        ItemStack itemstack4 = inventory_player.getItemStack();

                        if (itemstack3 != null)
                        {
                            itemstack = itemstack3.copy();
                        }

                        if (itemstack3 == null)
                        {
                            if (itemstack4 != null && slot2.isItemValid(itemstack4))
                            {
                                i2 = mouse_event_type == 0 ? itemstack4.stackSize : 1;

                                if (i2 > slot2.getSlotStackLimit())
                                {
                                    i2 = slot2.getSlotStackLimit();
                                }

                                if (itemstack4.stackSize >= i2)
                                {
                                    slot2.putStack(itemstack4.splitStack(i2));
                                }

                                if (itemstack4.stackSize == 0)
                                {
                                    inventory_player.setItemStack((ItemStack)null);
                                }
                            }
                        }
                        else if (slot2.canTakeStack(entity_player))
                        {
                            if (itemstack4 == null)
                            {
                                i2 = mouse_event_type == 0 ? itemstack3.stackSize : (itemstack3.stackSize + 1) / 2;
                                itemstack5 = slot2.decrStackSize(i2);
                                inventory_player.setItemStack(itemstack5);

                                if (itemstack3.stackSize == 0)
                                {
                                    slot2.putStack((ItemStack)null);
                                }

                                slot2.onPickupFromSlot(entity_player, inventory_player.getItemStack());
                            }
                            else if (slot2.isItemValid(itemstack4))
                            {
                                if (itemstack3.getItem() == itemstack4.getItem() && itemstack3.getItemDamage() == itemstack4.getItemDamage() && ItemStack.areItemStackTagsEqual(itemstack3, itemstack4))
                                {
                                    i2 = mouse_event_type == 0 ? itemstack4.stackSize : 1;

                                    if (i2 > slot2.getSlotStackLimit() - itemstack3.stackSize)
                                    {
                                        i2 = slot2.getSlotStackLimit() - itemstack3.stackSize;
                                    }

                                    if (i2 > itemstack4.getMaxStackSize() - itemstack3.stackSize)
                                    {
                                        i2 = itemstack4.getMaxStackSize() - itemstack3.stackSize;
                                    }

                                    itemstack4.splitStack(i2);

                                    if (itemstack4.stackSize == 0)
                                    {
                                        inventory_player.setItemStack((ItemStack)null);
                                    }

                                    itemstack3.stackSize += i2;
                                }
                                else if (itemstack4.stackSize <= slot2.getSlotStackLimit())
                                {
                                    slot2.putStack(itemstack4);
                                    inventory_player.setItemStack(itemstack3);
                                }
                            }
                            else if (itemstack3.getItem() == itemstack4.getItem() && itemstack4.getMaxStackSize() > 1 && (!itemstack3.getHasSubtypes() || itemstack3.getItemDamage() == itemstack4.getItemDamage()) && ItemStack.areItemStackTagsEqual(itemstack3, itemstack4))
                            {
                                i2 = itemstack3.stackSize;

                                if (i2 > 0 && i2 + itemstack4.stackSize <= itemstack4.getMaxStackSize())
                                {
                                    itemstack4.stackSize += i2;
                                    itemstack3 = slot2.decrStackSize(i2);

                                    if (itemstack3.stackSize == 0)
                                    {
                                        slot2.putStack((ItemStack)null);
                                    }

                                    slot2.onPickupFromSlot(entity_player, inventory_player.getItemStack());
                                }
                            }
                        }

                        slot2.onSlotChanged();
                    }
                }
            }
            else if (par3 == 2 && mouse_event_type >= 0 && mouse_event_type < 9)
            {
                slot2 = (Slot)this.inventorySlots.get(slot_num);

                if (slot2.canTakeStack(entity_player))
                {
                    itemstack3 = inventory_player.getStackInSlot(mouse_event_type);
                    boolean flag = itemstack3 == null || slot2.inventory == inventory_player && slot2.isItemValid(itemstack3);
                    i2 = -1;

                    if (!flag)
                    {
                        i2 = inventory_player.getFirstEmptyStack();
                        flag |= i2 > -1;
                    }

                    if (slot2.getHasStack() && flag)
                    {
                        itemstack5 = slot2.getStack();
                        inventory_player.setInventorySlotContents(mouse_event_type, itemstack5.copy());

                        if ((slot2.inventory != inventory_player || !slot2.isItemValid(itemstack3)) && itemstack3 != null)
                        {
                            if (i2 > -1)
                            {
                                inventory_player.addItemStackToInventory(itemstack3);
                                slot2.decrStackSize(itemstack5.stackSize);
                                slot2.putStack((ItemStack)null);
                                slot2.onPickupFromSlot(entity_player, itemstack5);
                            }
                        }
                        else
                        {
                            slot2.decrStackSize(itemstack5.stackSize);
                            slot2.putStack(itemstack3);
                            slot2.onPickupFromSlot(entity_player, itemstack5);
                        }
                    }
                    else if (!slot2.getHasStack() && itemstack3 != null && slot2.isItemValid(itemstack3))
                    {
                        inventory_player.setInventorySlotContents(mouse_event_type, (ItemStack)null);
                        slot2.putStack(itemstack3);
                    }
                }
            }
            else if (par3 == 3 && entity_player.capabilities.isCreativeMode && inventory_player.getItemStack() == null && slot_num >= 0)
            {
                slot2 = (Slot)this.inventorySlots.get(slot_num);

                if (slot2 != null && slot2.getHasStack())
                {
                    itemstack3 = slot2.getStack().copy();
                    itemstack3.stackSize = itemstack3.getMaxStackSize();
                    inventory_player.setItemStack(itemstack3);
                }
            }
            else if (par3 == 4 && inventory_player.getItemStack() == null && slot_num >= 0)
            {
                slot2 = (Slot)this.inventorySlots.get(slot_num);

                if (slot2 != null && slot2.getHasStack() && slot2.canTakeStack(entity_player))
                {
                    itemstack3 = slot2.decrStackSize(mouse_event_type == 0 ? 1 : slot2.getStack().stackSize);
                    slot2.onPickupFromSlot(entity_player, itemstack3);
                    entity_player.dropPlayerItemWithRandomChoice(itemstack3, true);
                }
            }
            else if (par3 == 6 && slot_num >= 0)
            {
                slot2 = (Slot)this.inventorySlots.get(slot_num);
                itemstack3 = inventory_player.getItemStack();

                if (itemstack3 != null && (slot2 == null || !slot2.getHasStack() || !slot2.canTakeStack(entity_player)))
                {
                    i1 = mouse_event_type == 0 ? 0 : this.inventorySlots.size() - 1;
                    i2 = mouse_event_type == 0 ? 1 : -1;

                    for (int l1 = 0; l1 < 2; ++l1)
                    {
                        for (int j2 = i1; j2 >= 0 && j2 < this.inventorySlots.size() && itemstack3.stackSize < itemstack3.getMaxStackSize(); j2 += i2)
                        {
                            Slot slot3 = (Slot)this.inventorySlots.get(j2);

                            if (slot3.getHasStack() && func_94527_a(slot3, itemstack3, true) && slot3.canTakeStack(entity_player) && this.func_94530_a(itemstack3, slot3) && (l1 != 0 || slot3.getStack().stackSize != slot3.getStack().getMaxStackSize()))
                            {
                                int k1 = Math.min(itemstack3.getMaxStackSize() - itemstack3.stackSize, slot3.getStack().stackSize);
                                ItemStack itemstack2 = slot3.decrStackSize(k1);
                                itemstack3.stackSize += k1;

                                if (itemstack2.stackSize <= 0)
                                {
                                    slot3.putStack((ItemStack)null);
                                }

                                slot3.onPickupFromSlot(entity_player, itemstack2);
                            }
                        }
                    }
                }

                this.detectAndSendChanges();
            }
        }

        return itemstack;
    }
    
//    public ItemStack slotClick(int slot_num, int mouse_event_type, int par3, EntityPlayer entity_player)
//    {
//        // DEBUG
//        System.out.println("ContainerPantry slotClick(): Client Side = "+tile_entity_pantry.getWorldObj().isRemote+": Slot ="+slot_num+": mouse_event_type ="+mouse_event_type+": par3 ="+par3); 
//
//        ItemStack returned_item_stack = null;
//        InventoryPlayer inventory_player = entity_player.inventory;
//        int i1;
//        ItemStack itemstack3;
//
//        if (par3 == 5)
//        {
//            int l = this.item_meta_data;
//            this.item_meta_data = bitMaskWith3(mouse_event_type);
//
//            if ((l != 1 || this.item_meta_data != 2) && l != this.item_meta_data)
//            {
//                // DEBUG
//                System.out.println("ContainerPantry slotClick(): Path 1"); 
//
//                this.clearSet();
//            }
//            else if (inventory_player.getItemStack() == null)
//            {
//                // DEBUG
//                System.out.println("ContainerPantry slotClick(): Path 2"); 
//
//                this.clearSet();
//            }
//            else if (this.item_meta_data == 0)
//            {
//                // DEBUG
//                System.out.println("ContainerPantry slotClick(): Path 3"); 
//
//                this.field_94535_f = bitShiftRightByTwoThenMaskWith3(mouse_event_type);
//
//                if (isEqualTo0Or1(this.field_94535_f))
//                {
//                    this.item_meta_data = 1;
//                    this.hash_set.clear();
//                }
//                else
//                {
//                    this.clearSet();
//                }
//            }
//            else if (this.item_meta_data == 1)
//            {
//                // DEBUG
//                System.out.println("ContainerPantry slotClick(): Path 4"); 
//
//                Slot slot = (Slot)this.inventorySlots.get(slot_num);
//
//                if (slot != null && func_94527_a(slot, inventory_player.getItemStack(), true) && slot.isItemValid(inventory_player.getItemStack()) && inventory_player.getItemStack().stackSize > this.hash_set.size() && this.canDragIntoSlot(slot))
//                {
//                    this.hash_set.add(slot);
//                }
//            }
//            else if (this.item_meta_data == 2)
//            {
//                // DEBUG
//                System.out.println("ContainerPantry slotClick(): Path 5"); 
//
//                if (!this.hash_set.isEmpty())
//                {
//                    itemstack3 = inventory_player.getItemStack().copy();
//                    i1 = inventory_player.getItemStack().stackSize;
//                    Iterator iterator = this.hash_set.iterator();
//
//                    while (iterator.hasNext())
//                    {
//                        Slot slot1 = (Slot)iterator.next();
//
//                        if (slot1 != null && func_94527_a(slot1, inventory_player.getItemStack(), true) && slot1.isItemValid(inventory_player.getItemStack()) && inventory_player.getItemStack().stackSize >= this.hash_set.size() && this.canDragIntoSlot(slot1))
//                        {
//                            ItemStack itemstack1 = itemstack3.copy();
//                            int j1 = slot1.getHasStack() ? slot1.getStack().stackSize : 0;
//                            func_94525_a(this.hash_set, this.field_94535_f, itemstack1, j1);
//
//                            if (itemstack1.stackSize > itemstack1.getMaxStackSize())
//                            {
//                                itemstack1.stackSize = itemstack1.getMaxStackSize();
//                            }
//
//                            if (itemstack1.stackSize > slot1.getSlotStackLimit())
//                            {
//                                itemstack1.stackSize = slot1.getSlotStackLimit();
//                            }
//
//                            i1 -= itemstack1.stackSize - j1;
//                            slot1.putStack(itemstack1);
//                        }
//                    }
//
//                    itemstack3.stackSize = i1;
//
//                    if (itemstack3.stackSize <= 0)
//                    {
//                        itemstack3 = null;
//                    }
//
//                    inventory_player.setItemStack(itemstack3);
//                }
//
//                this.clearSet();
//            }
//            else
//            {
//                this.clearSet();
//            }
//        }
//        else if (this.item_meta_data != 0)
//        {
//            // DEBUG
//            System.out.println("ContainerPantry slotClick(): Path 6"); 
//
//            this.clearSet();
//        }
//        else  // this seems to be the path most often taken
//        {
//            // DEBUG
//            System.out.println("ContainerPantry slotClick(): Path 7"); 
//
//            Slot slot2;
//            int i2;
//            ItemStack itemstack5;
//
//            // this seems to be the past most often taken
//            if ((par3 == 0 || par3 == 1) && (mouse_event_type == 0 || mouse_event_type == 1))
//            {
//                // DEBUG
//                System.out.println("ContainerPantry slotClick(): Path 7.1"); 
//
//                if (slot_num == -999)
//                {
//                    // DEBUG
//                    System.out.println("ContainerPantry slotClick(): Path 7.1.1"); 
//
//                    if (inventory_player.getItemStack() != null && slot_num == -999)
//                    {
//                        if (mouse_event_type == 0)
//                        {
//                            entity_player.dropPlayerItemWithRandomChoice(inventory_player.getItemStack(), true);
//                            inventory_player.setItemStack((ItemStack)null);
//                        }
//
//                        if (mouse_event_type == 1)
//                        {
//                            entity_player.dropPlayerItemWithRandomChoice(inventory_player.getItemStack().splitStack(1), true);
//
//                            if (inventory_player.getItemStack().stackSize == 0)
//                            {
//                                inventory_player.setItemStack((ItemStack)null);
//                            }
//                        }
//                    }
//                }
//                else if (par3 == 1)
//                {
//                    // DEBUG
//                    System.out.println("ContainerPantry slotClick(): Path 7.1.2"); 
//
//                    if (slot_num < 0)
//                    {
//                        return null;
//                    }
//
//                    slot2 = (Slot)this.inventorySlots.get(slot_num);
//
//                    if (slot2 != null && slot2.canTakeStack(entity_player))
//                    {
//                        itemstack3 = this.transferStackInSlot(entity_player, slot_num);
//
//                        if (itemstack3 != null)
//                        {
//                            Item item = itemstack3.getItem();
//                            returned_item_stack = itemstack3.copy();
//
//                            if (slot2.getStack() != null && slot2.getStack().getItem() == item)
//                            {
//                                this.retrySlotClick(slot_num, mouse_event_type, true, entity_player);
//                            }
//                        }
//                    }
//                }
//                else // this seems to be the path most taken
//                {
//                    // DEBUG
//                    System.out.println("ContainerPantry slotClick(): Path 7.1.3"); 
//
//                    if (slot_num < 0)
//                    {
//                        return null;
//                    }
//
//                    slot2 = (Slot)this.inventorySlots.get(slot_num);
//
//                    if (slot2 != null)
//                    {
//                        itemstack3 = slot2.getStack();
//                        ItemStack item_stack_in_player_inventory = inventory_player.getItemStack();
//
//                        // DEBUG
//                        if (item_stack_in_player_inventory != null)
//                        {
//                        	System.out.println("ContainerPantry slotClick(): PlayerInventory Item ="+item_stack_in_player_inventory.getDisplayName()+": Stack Size ="+item_stack_in_player_inventory.stackSize); 
//                        }
//                        
//                        // if nothing in TileEntity slot
//                        if (itemstack3 == null)
//                        {
//                            // DEBUG
//                            System.out.println("ContainerPantry slotClick(): Path 7.1.3.1"); 
//                            
//                            // if there is something in player inventory and it is valid for TileEntity slot
//                            if (item_stack_in_player_inventory != null && slot2.isItemValid(item_stack_in_player_inventory))
//                            {
//                                // DEBUG
//                                System.out.println("Something is in player inventory and it is valid for the slot"); 
//                            	
//                            	// i2 is how many to drop (all on left-click, one on right-click)
//                                i2 = mouse_event_type == 0 ? item_stack_in_player_inventory.stackSize : 1;
//                                
//                                // bound within stack limit
//                                if (i2 > slot2.getSlotStackLimit())
//                                {
//                                    i2 = slot2.getSlotStackLimit();
//                                }
//                                
//                                // after limiting, might need to retain some in player inventory so split
//                                if (item_stack_in_player_inventory.stackSize >= i2)
//                                {
//                                    slot2.putStack(item_stack_in_player_inventory.splitStack(i2));
//                                }
//
//                                if (item_stack_in_player_inventory.stackSize == 0)
//                                {
//                                    inventory_player.setItemStack((ItemStack)null);
//                                }
//                            }
//                        }
//                        // TileEntity is has something in it
//                        else if (slot2.canTakeStack(entity_player))
//                        {
//                            // DEBUG
//                            System.out.println("ContainerPantry slotClick(): Path 7.1.3.2"); 
//                            System.out.println("Meaning TileEntity stack has something in it and slot can take stack"); 
//                            
//                            returned_item_stack = itemstack3.copy();
//
//                            // if item is valid for slot
//                            if (slot2.isItemValid(item_stack_in_player_inventory))
//                            {
//                                // DEBUG
//                                System.out.println("ContainerPantry slotClick(): Path 7.1.3.2.1"); 
//                                System.out.println("Meaning item is valid for TileEntity"); 
//                                if (item_stack_in_player_inventory == null)
//                                {
//                                    System.out.println("Warning: PlayerInventory is null"); 
//                                }
//                                        
//                                // if items in both stacks are exactly the same (including damage level)
//                                if (itemstack3.getItem() == item_stack_in_player_inventory.getItem() && itemstack3.getItemDamage() == item_stack_in_player_inventory.getItemDamage() && ItemStack.areItemStackTagsEqual(itemstack3, item_stack_in_player_inventory))
//                                {
//                                    // DEBUG
//                                    System.out.println("TileEntity item matches PlayerInventory stack so combine them based on mouse click type"); 
//
//                                    // depending on mouse click (left-click is all, right-click is one)
//                                    i2 = mouse_event_type == 0 ? item_stack_in_player_inventory.stackSize : 1;
//
//                                    if (i2 > slot2.getSlotStackLimit() - itemstack3.stackSize)
//                                    {
//                                        i2 = slot2.getSlotStackLimit() - itemstack3.stackSize;
//                                    }
//
//                                    if (i2 > item_stack_in_player_inventory.getMaxStackSize() - itemstack3.stackSize)
//                                    {
//                                        i2 = item_stack_in_player_inventory.getMaxStackSize() - itemstack3.stackSize;
//                                    }
//
//                                    item_stack_in_player_inventory.splitStack(i2);
//
//                                    if (item_stack_in_player_inventory.stackSize == 0)
//                                    {
//                                        inventory_player.setItemStack((ItemStack)null);
//                                    }
//
//                                    itemstack3.stackSize += i2;
//                                }
//                                // items are not matched so swap them
//                                else if (item_stack_in_player_inventory.stackSize <= slot2.getSlotStackLimit())
//                                {
//                                    // DEBUG
//                                    System.out.println("TileEntity item does not match PlayerInventory stack so swap them"); 
//
//                                    slot2.putStack(item_stack_in_player_inventory);
//                                    inventory_player.setItemStack(itemstack3);
//                                }
//                            }
//                            // item is not valid for slot
//                            // check that items are exactly the same (not subtypes or damaged levels)
//                            else if (itemstack3.getItem() == item_stack_in_player_inventory.getItem() && item_stack_in_player_inventory.getMaxStackSize() > 1 && (!itemstack3.getHasSubtypes() || itemstack3.getItemDamage() == item_stack_in_player_inventory.getItemDamage()) && ItemStack.areItemStackTagsEqual(itemstack3, item_stack_in_player_inventory))
//                            {
//                                // DEBUG
//                                System.out.println("ContainerPantry slotClick(): Path 7.1.3.2.2"); 
//                                System.out.println("Meaning item is not valid for TileEntity slot"); 
//
//                                i2 = itemstack3.stackSize;
//
//                                // if the TileEntity stack can fit within PlayerInventory stack
//                                if (i2 > 0 && i2 + item_stack_in_player_inventory.stackSize <= item_stack_in_player_inventory.getMaxStackSize())
//                                {
//                                    // DEBUG
//                                    System.out.println("TileEntity stack can fit within PlayerInventory stack"); 
//
//                                    item_stack_in_player_inventory.stackSize += i2;
//                                    itemstack3 = slot2.decrStackSize(i2);
//
//                                    if (itemstack3.stackSize == 0)
//                                    {
//                                        // DEBUG
//                                        System.out.println("Used up TileEntity stack completely");
//                                        slot2.putStack((ItemStack)null);
//                                    }
//
//                                    slot2.onPickupFromSlot(entity_player, inventory_player.getItemStack());
//                                }
//                            }
//                        }
//
//                        slot2.onSlotChanged();
//                    }
//                }
//            }
//            else if (par3 == 2 && mouse_event_type >= 0 && mouse_event_type < 9)
//            {
//                // DEBUG
//                System.out.println("ContainerPantry slotClick(): Path 7.2"); 
//
//                slot2 = (Slot)this.inventorySlots.get(slot_num);
//
//                if (slot2.canTakeStack(entity_player))
//                {
//                    itemstack3 = inventory_player.getStackInSlot(mouse_event_type);
//                    boolean flag = itemstack3 == null || slot2.inventory == inventory_player && slot2.isItemValid(itemstack3);
//                    i2 = -1;
//
//                    if (!flag)
//                    {
//                        i2 = inventory_player.getFirstEmptyStack();
//                        flag |= i2 > -1;
//                    }
//
//                    if (slot2.getHasStack() && flag)
//                    {
//                        itemstack5 = slot2.getStack();
//                        inventory_player.setInventorySlotContents(mouse_event_type, itemstack5.copy());
//
//                        if ((slot2.inventory != inventory_player || !slot2.isItemValid(itemstack3)) && itemstack3 != null)
//                        {
//                            if (i2 > -1)
//                            {
//                                inventory_player.addItemStackToInventory(itemstack3);
//                                slot2.decrStackSize(itemstack5.stackSize);
//                                slot2.putStack((ItemStack)null);
//                                slot2.onPickupFromSlot(entity_player, itemstack5);
//                            }
//                        }
//                        else
//                        {
//                            slot2.decrStackSize(itemstack5.stackSize);
//                            slot2.putStack(itemstack3);
//                            slot2.onPickupFromSlot(entity_player, itemstack5);
//                        }
//                    }
//                    else if (!slot2.getHasStack() && itemstack3 != null && slot2.isItemValid(itemstack3))
//                    {
//                        inventory_player.setInventorySlotContents(mouse_event_type, (ItemStack)null);
//                        slot2.putStack(itemstack3);
//                    }
//                }
//            }
//            else if (par3 == 3 && entity_player.capabilities.isCreativeMode && inventory_player.getItemStack() == null && slot_num >= 0)
//            {
//                // DEBUG
//                System.out.println("ContainerPantry slotClick(): Path 7.3"); 
//
//                slot2 = (Slot)this.inventorySlots.get(slot_num);
//
//                if (slot2 != null && slot2.getHasStack())
//                {
//                    itemstack3 = slot2.getStack().copy();
//                    itemstack3.stackSize = itemstack3.getMaxStackSize();
//                    inventory_player.setItemStack(itemstack3);
//                }
//            }
//            else if (par3 == 4 && inventory_player.getItemStack() == null && slot_num >= 0)
//            {
//                // DEBUG
//                System.out.println("ContainerPantry slotClick(): Path 7.4"); 
//
//                slot2 = (Slot)this.inventorySlots.get(slot_num);
//
//                if (slot2 != null && slot2.getHasStack() && slot2.canTakeStack(entity_player))
//                {
//                    itemstack3 = slot2.decrStackSize(mouse_event_type == 0 ? 1 : slot2.getStack().stackSize);
//                    slot2.onPickupFromSlot(entity_player, itemstack3);
//                    entity_player.dropPlayerItemWithRandomChoice(itemstack3, true);
//                }
//            }
//            else if (par3 == 6 && slot_num >= 0)
//            {
//                // DEBUG
//                System.out.println("ContainerPantry slotClick(): Path 7.5"); 
//
//                slot2 = (Slot)this.inventorySlots.get(slot_num);
//                itemstack3 = inventory_player.getItemStack();
//
//                if (itemstack3 != null && (slot2 == null || !slot2.getHasStack() || !slot2.canTakeStack(entity_player)))
//                {
//                    i1 = mouse_event_type == 0 ? 0 : this.inventorySlots.size() - 1;
//                    i2 = mouse_event_type == 0 ? 1 : -1;
//
//                    for (int l1 = 0; l1 < 2; ++l1)
//                    {
//                        for (int j2 = i1; j2 >= 0 && j2 < this.inventorySlots.size() && itemstack3.stackSize < itemstack3.getMaxStackSize(); j2 += i2)
//                        {
//                            Slot slot3 = (Slot)this.inventorySlots.get(j2);
//
//                            if (slot3.getHasStack() && func_94527_a(slot3, itemstack3, true) && slot3.canTakeStack(entity_player) && this.func_94530_a(itemstack3, slot3) && (l1 != 0 || slot3.getStack().stackSize != slot3.getStack().getMaxStackSize()))
//                            {
//                                int k1 = Math.min(itemstack3.getMaxStackSize() - itemstack3.stackSize, slot3.getStack().stackSize);
//                                ItemStack itemstack2 = slot3.decrStackSize(k1);
//                                itemstack3.stackSize += k1;
//
//                                if (itemstack2.stackSize <= 0)
//                                {
//                                    slot3.putStack((ItemStack)null);
//                                }
//
//                                slot3.onPickupFromSlot(entity_player, itemstack2);
//                            }
//                        }
//                    }
//                }
//
//                this.detectAndSendChanges();
//            }
//        }
//
//        return returned_item_stack;
//    }


    @Override
	protected void retrySlotClick(int par1, int par2, boolean par3, EntityPlayer par4EntityPlayer)
    {
        this.slotClick(par1, par2, 1, par4EntityPlayer);
    }

    /**
     * Called when the container is closed.
     */
    @Override
	public void onContainerClosed(EntityPlayer entity_player)
    {
        // DEBUG
        System.out.println("ContainerPantry onContainerClosed(): Client Side = "+tile_entity_pantry.getWorldObj().isRemote); 

       InventoryPlayer inventory_player = entity_player.inventory;

        if (inventory_player.getItemStack() != null)
        {
            entity_player.dropPlayerItemWithRandomChoice(inventory_player.getItemStack(), false);
            inventory_player.setItemStack((ItemStack)null);
        }
    }

    /**
     * Callback for when the crafting matrix is changed.
     */
    @Override
	public void onCraftMatrixChanged(IInventory par1IInventory)
    {
        this.detectAndSendChanges();
    }

    /**
     * args: slotID, itemStack to put in slot
     */
    @Override
    public void putStackInSlot(int slot, ItemStack par2ItemStack)
    {
        // DEBUG
        System.out.println("ContainerPantry putStackInSlot(): Client Side = "+tile_entity_pantry.getWorldObj().isRemote+": Slot = "+slot); 

        this.getSlot(slot).putStack(par2ItemStack);
    }

    /**
     * places itemstacks in first x slots, x being aitemstack.length
     */
    @SideOnly(Side.CLIENT)
    @Override
    public void putStacksInSlots(ItemStack[] par1ArrayOfItemStack)
    {
        // DEBUG
        System.out.println("ContainerPantry putStacksInSlots(): Client Side = "+tile_entity_pantry.getWorldObj().isRemote); 
       
        for (int i = 0; i < par1ArrayOfItemStack.length; ++i)
        {
            this.getSlot(i).putStack(par1ArrayOfItemStack[i]);
        }
    }

    @Override
	@SideOnly(Side.CLIENT)
    public void updateProgressBar(int par1, int par2) {}

 
    /**
     * merges provided ItemStack with the first available one in the container/player inventory
     */
    @Override
    protected boolean mergeItemStack(ItemStack par1ItemStack, int range_start, int range_end_plus_one, boolean start_from_end)
    {
        // DEBUG
        System.out.println("ContainerPantry mergeItemStack(): Client Side = "+tile_entity_pantry.getWorldObj().isRemote); 

        boolean amount_in_slot_changed = false;
        int k = range_start;

        if (start_from_end)
        {
            k = range_end_plus_one - 1;
        }

        Slot slot;
        ItemStack itemstack1;

        if (par1ItemStack.isStackable())
        {
            // DEBUG
            System.out.println("ContainerPantry mergeItemStack(): Client Side = "+tile_entity_pantry.getWorldObj().isRemote+": isStackable = true"); 

            // loop through range using index k in direction determined by start_from_end flag
            // looking for slot containing a like item
            while (par1ItemStack.stackSize > 0 && (!start_from_end && k < range_end_plus_one || start_from_end && k >= range_start))
            {
                slot = (Slot)this.inventorySlots.get(k);
                itemstack1 = slot.getStack();

                // if stack from container slot is not empty and contains same item type then you can add together
                if (itemstack1 != null && itemstack1.getItem() == par1ItemStack.getItem() && (!par1ItemStack.getHasSubtypes() || par1ItemStack.getItemDamage() == itemstack1.getItemDamage()) && ItemStack.areItemStackTagsEqual(par1ItemStack, itemstack1))
                {
                    int l = itemstack1.stackSize + par1ItemStack.stackSize;

                    // DEBUG
                    System.out.println("ContainerPantry mergeItemStack(): Client Side = "+tile_entity_pantry.getWorldObj().isRemote+": Resulting stack size should ="+l); 

                    // if combined amount can fit within max stack size move them all
                    if (l <= par1ItemStack.getMaxStackSize())
                    {
                        par1ItemStack.stackSize = 0;
                        itemstack1.stackSize = l;
                        slot.onSlotChanged();
                        amount_in_slot_changed = true;
                    }
                    // otherwise need to leave some in player inventory stack
                    else if (itemstack1.stackSize < par1ItemStack.getMaxStackSize())
                    {
                        // DEBUG
                        System.out.println("ContainerPantry mergeItemStack(): Client Side = "+tile_entity_pantry.getWorldObj().isRemote+": But truncated to max size"); 

                        par1ItemStack.stackSize -= par1ItemStack.getMaxStackSize() - itemstack1.stackSize;
                        itemstack1.stackSize = par1ItemStack.getMaxStackSize();
                        slot.onSlotChanged();
                        amount_in_slot_changed = true;
                    }
                }

                // update loop index depending on direction
                if (start_from_end)
                {
                    --k;
                }
                else
                {
                    ++k;
                }
            }
        }

        // if there are still items in the player inventory stack
        if (par1ItemStack.stackSize > 0)
        {
            // DEBUG
            System.out.println("ContainerPantry mergeItemStack(): Client Side = "+tile_entity_pantry.getWorldObj().isRemote+": Still items in player inventory stack"); 

            // initialize loop index depending on direction
            if (start_from_end)
            {
                k = range_end_plus_one - 1;
            }
            else
            {
                k = range_start;
            }

            // loop through range depending on direction
            while (!start_from_end && k < range_end_plus_one || start_from_end && k >= range_start)
            {
                slot = (Slot)this.inventorySlots.get(k);
                itemstack1 = slot.getStack();
                
                // if no container inventory stack exists create as copy of player inventory stack
                if (itemstack1 == null)
                {
                    slot.putStack(par1ItemStack.copy());
                    slot.onSlotChanged();
                    par1ItemStack.stackSize = 0;
                    amount_in_slot_changed = true;
                    break;
                }

                // update loop index depending on direction
                if (start_from_end)
                {
                    --k;
                }
                else
                {
                    ++k;
                }
            }
        }

        return amount_in_slot_changed;
    }

    /**
     * Returns true if the player can "drag-spilt" items into this slot,. returns true by default. Called to check if
     * the slot can be added to a list of Slots to split the held ItemStack across.
     */
    @Override
    public boolean canDragIntoSlot(Slot par1Slot)
    {
        // DEBUG
        System.out.println("ContainerPantry canDragIntoSlot(): Client Side = "+tile_entity_pantry.getWorldObj().isRemote); 

        return true;
    }

}
