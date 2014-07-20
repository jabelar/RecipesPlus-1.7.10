package com.blogspot.jabelarminecraft.recipesplus.blocks;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

import com.blogspot.jabelarminecraft.recipesplus.RecipePlus;
import com.blogspot.jabelarminecraft.recipesplus.gui.GuiStove;
import com.blogspot.jabelarminecraft.recipesplus.tileentities.TileEntityStove;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockStove extends RecipeBlockContainer
{
    private final Random tick_random = new Random();
    private final boolean stove_on;
    private static boolean field_149934_M;
    @SideOnly(Side.CLIENT)
    private IIcon block_icon_top;
    @SideOnly(Side.CLIENT)
    private IIcon block_icon_front;

    public BlockStove(boolean is_lit)
    {
        super(Material.rock);

        // DEBUG
        System.out.println("BlockStove constructor()"); 

        this.stove_on = is_lit;
        this.setHardness(3.5F);
        this.setStepSound(soundTypePiston);
        this.setBlockName("stove");
        if (this.stove_on)
        {
        	this.setLightLevel(0.875F); // gives off light
        }
        else
        {
        	this.setCreativeTab(CreativeTabs.tabDecorations);
        }
    }

    @Override
	public Item getItemDropped(int p_149650_1_, Random p_149650_2_, int p_149650_3_)
    {
        // DEBUG
        System.out.println("BlockStove getItemDropped()"); 

        return Item.getItemFromBlock(RecipePlus.blockStove);
    }

    /**
     * Called whenever the block is added into the world. Args: world, x, y, z
     */
    @Override
	public void onBlockAdded(World world, int pos_x, int pos_y, int pos_z)
    {
        // DEBUG
        System.out.println("BlockStove onBlockAdded()"); 
        
        super.onBlockAdded(world, pos_x, pos_y, pos_z);
        this.initStove(world, pos_x, pos_y, pos_z);
    }

    private void initStove(World world, int pos_x, int pos_y, int pos_z)
    {
        // DEBUG
        System.out.println("BlockStove initStove()"); 

        if (!world.isRemote) // if on server
        {
        	// check for opaque blocks beside the block
            Block block = world.getBlock(pos_x, pos_y, pos_z - 1);
            Block block1 = world.getBlock(pos_x, pos_y, pos_z + 1);
            Block block2 = world.getBlock(pos_x - 1, pos_y, pos_z);
            Block block3 = world.getBlock(pos_x + 1, pos_y, pos_z);
            byte b0 = 3;
            
            // func_149730_j() means isOpaque()
            if (block.func_149730_j() && !block1.func_149730_j())
            {
                b0 = 3;
            }

            if (block1.func_149730_j() && !block.func_149730_j())
            {
                b0 = 2;
            }

            if (block2.func_149730_j() && !block3.func_149730_j())
            {
                b0 = 5;
            }

            if (block3.func_149730_j() && !block2.func_149730_j())
            {
                b0 = 4;
            }

            world.setBlockMetadataWithNotify(pos_x, pos_y, pos_z, b0, 2);
        }
    }

    /**
     * Gets the block's texture. Args: side, meta
     */
    @Override
	@SideOnly(Side.CLIENT)
    public IIcon getIcon(int side, int meta)
    {
        return side == 1 ? this.block_icon_top : (side == 0 ? this.block_icon_top : (side != meta ? this.blockIcon : this.block_icon_front));
    }

    @Override
	@SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister icon_register)
    {
        // DEBUG
        System.out.println("BlockStove registerBlockIcons()"); 
        
        this.blockIcon = icon_register.registerIcon("recipeplus:stove_side");
        this.block_icon_front = icon_register.registerIcon(this.stove_on ? "recipeplus:stove_front_on" : "recipeplus:stove_front_off");
        this.block_icon_top = icon_register.registerIcon("recipeplus:stove_top");
    }

    /**
     * Called upon block activation (right click on the block.)
     */
    @Override
	public boolean onBlockActivated(World world, int pos_x, int pos_y, int pos_z, EntityPlayer entity_player, int p_149727_6_, float p_149727_7_, float p_149727_8_, float p_149727_9_)
    {
        // DEBUG
        System.out.println("BlockStove onBlockActivated()"); 

        if (world.isRemote)
        {
            return true;
        }
        else // on server side so display the gui for the associated tileentity
        {
            TileEntityStove tileentitystove = (TileEntityStove)world.getTileEntity(pos_x, pos_y, pos_z);

            if (tileentitystove != null)
            {
            	// in BlockStove original code was entity_player.func_146101_a(tileentitystove) probably openGui function
            	// and so I took the code from that function and used it with modification here
                Minecraft.getMinecraft().displayGuiScreen(new GuiStove(entity_player.inventory, tileentitystove));

            }

            return true;
        }
    }

    /**
     * Update which block the stove is using depending on whether or not it is burning
     */
    public static void updateStoveBlockState(boolean stove_lit, World world, int pos_x, int pos_y, int pos_z)
    {
        int b0 = world.getBlockMetadata(pos_x, pos_y, pos_z);
        TileEntity tileentity = world.getTileEntity(pos_x, pos_y, pos_z);
        field_149934_M = true;

        if (stove_lit)
        {
            world.setBlock(pos_x, pos_y, pos_z, RecipePlus.blockStoveLit);
        }
        else
        {
            world.setBlock(pos_x, pos_y, pos_z, RecipePlus.blockStove);
        }

        field_149934_M = false;
        world.setBlockMetadataWithNotify(pos_x, pos_y, pos_z, b0, 2);

        if (tileentity != null)
        {
            tileentity.validate();
            world.setTileEntity(pos_x, pos_y, pos_z, tileentity);
        }
    }

    /**
     * Returns a new instance of a block's tile entity class. Called on placing the block.
     */
    @Override
	public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_)
    {
        // DEBUG
        System.out.println("BlockStove createNewTileEntity()"); 

        return new TileEntityStove();
    }

    /**
     * Called when the block is placed in the world.
     */
    @Override
	public void onBlockPlacedBy(World world, int pos_x, int pos_y, int pos_z, EntityLivingBase entity_living_base, ItemStack stove_itemstack)
    {
        // DEBUG
        System.out.println("BlockStove onPlacedBy()"); 

        int l = MathHelper.floor_double(entity_living_base.rotationYaw * 4.0F / 360.0F + 0.5D) & 3;

        if (l == 0)
        {
            world.setBlockMetadataWithNotify(pos_x, pos_y, pos_z, 2, 2);
        }

        if (l == 1)
        {
            world.setBlockMetadataWithNotify(pos_x, pos_y, pos_z, 5, 2);
        }

        if (l == 2)
        {
            world.setBlockMetadataWithNotify(pos_x, pos_y, pos_z, 3, 2);
        }

        if (l == 3)
        {
            world.setBlockMetadataWithNotify(pos_x, pos_y, pos_z, 4, 2);
        }

        if (stove_itemstack.hasDisplayName())
        {
            ((TileEntityStove)world.getTileEntity(pos_x, pos_y, pos_z)).setEntityName(stove_itemstack.getDisplayName());
        }
    }

    @Override
	public void breakBlock(World world, int pos_x, int pos_y, int pos_z, Block block_being_broken, int p_149749_6_)
    {
        // DEBUG
        System.out.println("BlockStove breakBlock()"); 

        if (!field_149934_M)
        {
            TileEntityStove tileentitystove = (TileEntityStove)world.getTileEntity(pos_x, pos_y, pos_z);

            // return itemstack contents of all slots in stove when block is destroyed
            if (tileentitystove != null)
            {
                for (int i1 = 0; i1 < tileentitystove.getSizeInventory(); ++i1)
                {
                    ItemStack itemstack = tileentitystove.getStackInSlot(i1);

                    if (itemstack != null)
                    {
                        float f = this.tick_random.nextFloat() * 0.8F + 0.1F;
                        float f1 = this.tick_random.nextFloat() * 0.8F + 0.1F;
                        float f2 = this.tick_random.nextFloat() * 0.8F + 0.1F;

                        while (itemstack.stackSize > 0)
                        {
                            int j1 = this.tick_random.nextInt(21) + 10;

                            if (j1 > itemstack.stackSize)
                            {
                                j1 = itemstack.stackSize;
                            }

                            itemstack.stackSize -= j1;
                            EntityItem entityitem = new EntityItem(world, pos_x + f, pos_y + f1, pos_z + f2, new ItemStack(itemstack.getItem(), j1, itemstack.getItemDamage()));

                            if (itemstack.hasTagCompound())
                            {
                                entityitem.getEntityItem().setTagCompound((NBTTagCompound)itemstack.getTagCompound().copy());
                            }
                            
                            // "throw" the released entityitems out in random directions
                            float f3 = 0.05F;
                            entityitem.motionX = (float)this.tick_random.nextGaussian() * f3;
                            entityitem.motionY = (float)this.tick_random.nextGaussian() * f3 + 0.2F;
                            entityitem.motionZ = (float)this.tick_random.nextGaussian() * f3;
                            world.spawnEntityInWorld(entityitem);
                        }
                    }
                }

                world.func_147453_f(pos_x, pos_y, pos_z, block_being_broken);
            }
        }

        super.breakBlock(world, pos_x, pos_y, pos_z, block_being_broken, p_149749_6_);
    }

    /**
     * A randomly called display update to be able to add particles or other items for display
     */
    @Override
	@SideOnly(Side.CLIENT)
    public void randomDisplayTick(World world, int pos_x, int pos_y, int pos_z, Random random_num)
    {
        if (this.stove_on)
        {
            int l = world.getBlockMetadata(pos_x, pos_y, pos_z);
            float f = pos_x + 0.5F;
            float f1 = pos_y + 0.0F + random_num.nextFloat() * 6.0F / 16.0F;
            float f2 = pos_z + 0.5F;
            float f3 = 0.52F;
            float f4 = random_num.nextFloat() * 0.6F - 0.3F;

            if (l == 4)
            {
                world.spawnParticle("smoke", f - f3, f1, f2 + f4, 0.0D, 0.0D, 0.0D);
                world.spawnParticle("flame", f - f3, f1, f2 + f4, 0.0D, 0.0D, 0.0D);
            }
            else if (l == 5)
            {
                world.spawnParticle("smoke", f + f3, f1, f2 + f4, 0.0D, 0.0D, 0.0D);
                world.spawnParticle("flame", f + f3, f1, f2 + f4, 0.0D, 0.0D, 0.0D);
            }
            else if (l == 2)
            {
                world.spawnParticle("smoke", f + f4, f1, f2 - f3, 0.0D, 0.0D, 0.0D);
                world.spawnParticle("flame", f + f4, f1, f2 - f3, 0.0D, 0.0D, 0.0D);
            }
            else if (l == 3)
            {
                world.spawnParticle("smoke", f + f4, f1, f2 + f3, 0.0D, 0.0D, 0.0D);
                world.spawnParticle("flame", f + f4, f1, f2 + f3, 0.0D, 0.0D, 0.0D);
            }
        }
    }

    /**
     * If this returns true, then comparators facing away from this block will use the value from
     * getComparatorInputOverride instead of the actual redstone signal strength.
     */
    @Override
	public boolean hasComparatorInputOverride()
    {
        return true;
    }

    /**
     * If hasComparatorInputOverride returns true, the return value from this is used instead of the redstone signal
     * strength when this block inputs to a comparator.
     */
    @Override
	public int getComparatorInputOverride(World world, int pos_x, int pos_y, int pos_z, int p_149736_5_)
    {
        return Container.calcRedstoneFromInventory((IInventory)world.getTileEntity(pos_x, pos_y, pos_z));
    }

    /**
     * Gets an item for the block being called on. Args: world, x, y, z
     */
    @Override
	@SideOnly(Side.CLIENT)
    public Item getItem(World world, int pos_x, int pos_y, int pos_z)
    {
        // DEBUG
        System.out.println("BlockStove getItem()"); 

        return Item.getItemFromBlock(RecipePlus.blockStove);
    }
}