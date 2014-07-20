package com.blogspot.jabelarminecraft.recipesplus.blocks;

import java.util.Random;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.Item;
import net.minecraft.util.IIcon;

import com.blogspot.jabelarminecraft.recipesplus.RecipePlus;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockBlueberry extends RecipeBlockCrops
{

    public BlockBlueberry()
    {
        // Basic block setup
        setBlockName("blueberries");
        setBlockTextureName("recipeplus:blueberries_stage_0");

    }

    /**
     * Returns the quantity of items to drop on block destruction.
     */
    @Override
	public int quantityDropped(int parMetadata, int parFortune, Random parRand)
    {
        return (parMetadata/2);
    }

    @Override
	public Item getItemDropped(int parMetadata, Random parRand, int parFortune)
    {
    	// DEBUG
    	System.out.println("BlockBlueberry getItemDropped()");
        return (RecipePlus.blueberry);
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister parIIconRegister)
    {
          iIcon = new IIcon[8];
          // seems that crops like to have 8 growth icons, but okay to repeat actual texture if you want
          iIcon[0] = parIIconRegister.registerIcon("recipeplus:blueberries_stage_0");
          iIcon[1] = parIIconRegister.registerIcon("recipeplus:blueberries_stage_0");
          iIcon[2] = parIIconRegister.registerIcon("recipeplus:blueberries_stage_1");
          iIcon[3] = parIIconRegister.registerIcon("recipeplus:blueberries_stage_1");
          iIcon[4] = parIIconRegister.registerIcon("recipeplus:blueberries_stage_2");
          iIcon[5] = parIIconRegister.registerIcon("recipeplus:blueberries_stage_2");
          iIcon[6] = parIIconRegister.registerIcon("recipeplus:blueberries_stage_3");
          iIcon[7] = parIIconRegister.registerIcon("recipeplus:blueberries_stage_3");
    }
}