package com.blogspot.jabelarminecraft.recipesplus.blocks;

import java.util.ArrayList;
import java.util.Random;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

import com.blogspot.jabelarminecraft.recipesplus.RecipePlus;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockTomato extends RecipeBlockCrops
{

    public BlockTomato()
    {
    	// Basic block setup
        this.setBlockName("tomatoes");
        this.setBlockTextureName("recipeplus:tomatoes_stage_0");

    }

    /**
     * Returns the quantity of items to drop on block destruction.
     */
    @Override
	public int quantityDropped(int parMetadata, int parFortune, Random parRand)
    {
        return (parMetadata/2);
    }
    
    /**
     * This returns a complete list of items dropped from this block
     *
     * @param world The current world
     * @param x X Position
     * @param y Y Position
     * @param z Z Position
     * @param metadata Current metadata
     * @param fortune Breakers fortune level
     * @return A ArrayList containing all items this block drops
     */
    @Override
	  public ArrayList<ItemStack> getDrops(World world, int x, int y, int z, int metadata, int fortune)
    {
    	// DEBUG
    	System.out.println("BlockTomato getDrops()");
        ArrayList<ItemStack> ret = new ArrayList<ItemStack>();

        int count = quantityDropped(metadata, fortune, world.rand);
        for(int i = 0; i < count; i++)
        {
            Item item = getItemDropped(metadata, world.rand, fortune);
            if (item != null)
            {
                ret.add(new ItemStack(item, 1, damageDropped(metadata)));
            }
        }
        return ret;
    }

    @Override
	public Item getItemDropped(int parMetadata, Random parRand, int parFortune)
    {
    	// DEBUG
    	System.out.println("BlockTomato getItemDropped()");
        return (RecipePlus.tomato);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister parIIconRegister)
    {
	      this.iIcon = new IIcon[8];
	      // seems that crops like to have 8 growth icons, but okay to repeat actual texture if you want
	      this.iIcon[0] = parIIconRegister.registerIcon("recipeplus:tomatoes_stage_0");
	      this.iIcon[1] = parIIconRegister.registerIcon("recipeplus:tomatoes_stage_0");
	      this.iIcon[2] = parIIconRegister.registerIcon("recipeplus:tomatoes_stage_1");
	      this.iIcon[3] = parIIconRegister.registerIcon("recipeplus:tomatoes_stage_1");
	      this.iIcon[4] = parIIconRegister.registerIcon("recipeplus:tomatoes_stage_2");
	      this.iIcon[5] = parIIconRegister.registerIcon("recipeplus:tomatoes_stage_2");
	      this.iIcon[6] = parIIconRegister.registerIcon("recipeplus:tomatoes_stage_3");
	      this.iIcon[7] = parIIconRegister.registerIcon("recipeplus:tomatoes_stage_3");
	  }
	}