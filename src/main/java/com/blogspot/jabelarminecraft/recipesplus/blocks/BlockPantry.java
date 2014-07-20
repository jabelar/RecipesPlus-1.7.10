package com.blogspot.jabelarminecraft.recipesplus.blocks;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

import com.blogspot.jabelarminecraft.recipesplus.gui.GuiPantry;
import com.blogspot.jabelarminecraft.recipesplus.tileentities.TileEntityPantry;

public class BlockPantry extends BlockContainer 
{
	
	TileEntityPantry my_tile_entity;

	public BlockPantry() 
	{
		super(Material.wood);
        // DEBUG
        System.out.println("BlockPantry constructor()"); 

        this.setHardness(3.5F);
        this.setStepSound(soundTypePiston);
        this.setBlockName("pantry");
       	this.setCreativeTab(CreativeTabs.tabDecorations);   	
	}
	
	@Override
	public TileEntityPantry createNewTileEntity(World var1, int var2) 
	{
        // DEBUG
        System.out.println("BlockPantry createNewTileEntity()"); 
       	
        my_tile_entity = new TileEntityPantry();
      
		return my_tile_entity;
	}

	// When block is activated (with right-click) it gets the tile entity at the click position
	// then activates the associated gui for that tile entity
    @Override
    public boolean onBlockActivated(World world, int pos_x, int pos_y, int pos_z,
                    EntityPlayer player, int metadata, float what, float these, float are) 
    {
        // DEBUG
        System.out.println("BlockPantry onBlockActivated(): Client Side = "+world.isRemote); 

        if (world.isRemote)
        {
            return true;
        }
        else // on server side so display the gui for the associated tileentity
        {

            if (my_tile_entity != null)
            {
            	// in BlockFurnace original code was entity_player.func_146101_a(tileentitypantry) probably openGui function
            	// and so I took the code from that function and used it with modification here
                Minecraft.getMinecraft().displayGuiScreen(new GuiPantry(player.inventory, my_tile_entity));

            }

            return true;
        }
    }

    @Override
    public void breakBlock(World world, int x, int y, int z, Block block, int par6) 
    {
        // DEBUG
        System.out.println("BlockPantry breakBlock(): Client Side = "+world.isRemote); 

        dropItems(world, x, y, z);
        super.breakBlock(world, x, y, z, block, par6);
    }

    private void dropItems(World world, int x, int y, int z)
    {
        // DEBUG
        System.out.println("BlockPantry dropItems(): Client Side = "+world.isRemote); 

         Random rand = new Random();
        
        // if it doesn't contain an inventory then nothing to drop
        if (!(my_tile_entity instanceof IInventory)) 
        {
        	return;
        }
        
        // contains items so convert contents to EntityItems
        IInventory inventory = my_tile_entity;
        int num_of_slots = inventory.getSizeInventory();
        // DEBUG
        System.out.println("BlockPantry dropItem(): Number of items to drop = "+num_of_slots); 

        // for each slot in inventory convert it to EntityItem
        for (int i = 0; i < num_of_slots; i++) 
        {
            ItemStack item_stack = inventory.getStackInSlot(i);
            // if there is something in the stack, process it
            if (item_stack != null && item_stack.stackSize > 0) 
            {
                // DEBUG
                System.out.println("BlockPantry dropItem(): Slot = "+i+": Something to drop = "+item_stack.getDisplayName()+" QTY "+item_stack.stackSize); 

            	// randomize the toss spawn point
                float rx = rand.nextFloat() * 0.8F + 0.1F;
                float ry = rand.nextFloat() * 0.8F + 0.1F;
                float rz = rand.nextFloat() * 0.8F + 0.1F;

                EntityItem entity_item = new EntityItem(world,
                        x + rx, y + ry, z + rz,
                new ItemStack(item_stack.getItem(), item_stack.stackSize, item_stack.getItemDamage()));
                // copy tag compound to the EntityItem if item had one
                if (item_stack.hasTagCompound()) 
                {
                    entity_item.getEntityItem().setTagCompound((NBTTagCompound) item_stack.getTagCompound().copy());
                }
                
                // give bit of random motion
                float factor = 0.05F;
                entity_item.motionX = rand.nextGaussian() * factor;
                entity_item.motionY = rand.nextGaussian() * factor + 0.2F;
                entity_item.motionZ = rand.nextGaussian() * factor;
                world.spawnEntityInWorld(entity_item);
                
                // clear item stack
                item_stack.stackSize = 0;
            }
        }
    }
}
