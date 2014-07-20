package com.blogspot.jabelarminecraft.recipesplus.recipes;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFishFood;
import net.minecraft.item.ItemStack;

public class RecipesStove
{
    private static final RecipesStove cookingBase = new RecipesStove();
    /**
     * The list of cooking results.
     */
    private Map cookingList = new HashMap();
    private Map experienceList = new HashMap();
    private static final String __OBFID = "CL_00000085";

    /**
     * Used to call methods addCooking and getCookingResult.
     */
    public static RecipesStove cooking()
    {
        return cookingBase;
    }

    private RecipesStove()
    {
        this.addRecipe(Blocks.iron_ore, new ItemStack(Items.iron_ingot), 0.7F);
        this.addRecipe(Blocks.gold_ore, new ItemStack(Items.gold_ingot), 1.0F);
        this.addRecipe(Blocks.diamond_ore, new ItemStack(Items.diamond), 1.0F);
        this.addRecipe(Blocks.sand, new ItemStack(Blocks.glass), 0.1F);
        this.addItemRecipe(Items.porkchop, new ItemStack(Items.cooked_porkchop), 0.35F);
        this.addItemRecipe(Items.beef, new ItemStack(Items.cooked_beef), 0.35F);
        this.addItemRecipe(Items.chicken, new ItemStack(Items.cooked_chicken), 0.35F);
        this.addRecipe(Blocks.cobblestone, new ItemStack(Blocks.stone), 0.1F);
        this.addItemRecipe(Items.clay_ball, new ItemStack(Items.brick), 0.3F);
        this.addRecipe(Blocks.clay, new ItemStack(Blocks.hardened_clay), 0.35F);
        this.addRecipe(Blocks.cactus, new ItemStack(Items.dye, 1, 2), 0.2F);
        this.addRecipe(Blocks.log, new ItemStack(Items.coal, 1, 1), 0.15F);
        this.addRecipe(Blocks.log2, new ItemStack(Items.coal, 1, 1), 0.15F);
        this.addRecipe(Blocks.emerald_ore, new ItemStack(Items.emerald), 1.0F);
        this.addItemRecipe(Items.potato, new ItemStack(Items.baked_potato), 0.35F);
        this.addRecipe(Blocks.netherrack, new ItemStack(Items.netherbrick), 0.1F);
        ItemFishFood.FishType[] afishtype = ItemFishFood.FishType.values();
        int i = afishtype.length;

        for (int j = 0; j < i; ++j)
        {
            ItemFishFood.FishType fishtype = afishtype[j];

            if (fishtype.func_150973_i())
            {
                this.addToCookingList(new ItemStack(Items.fish, 1, fishtype.func_150976_a()), new ItemStack(Items.cooked_fished, 1, fishtype.func_150976_a()), 0.35F);
            }
        }

        this.addRecipe(Blocks.coal_ore, new ItemStack(Items.coal), 0.1F);
        this.addRecipe(Blocks.redstone_ore, new ItemStack(Items.redstone), 0.7F);
        this.addRecipe(Blocks.lapis_ore, new ItemStack(Items.dye, 1, 4), 0.2F);
        this.addRecipe(Blocks.quartz_ore, new ItemStack(Items.quartz), 0.2F);
    }

    public void addRecipe(Block block_input, ItemStack block_output, float experience_earned)
    {
        this.addItemRecipe(Item.getItemFromBlock(block_input), block_output, experience_earned);
    }

    public void addItemRecipe(Item p_151396_1_, ItemStack p_151396_2_, float experience_earned)
    {
        this.addToCookingList(new ItemStack(p_151396_1_, 1, 32767), p_151396_2_, experience_earned);
    }

    public void addToCookingList(ItemStack p_151394_1_, ItemStack p_151394_2_, float experience_earned)
    {
        this.cookingList.put(p_151394_1_, p_151394_2_);
        this.experienceList.put(p_151394_2_, Float.valueOf(experience_earned));
    }

    /**
     * Returns the cooking result of an item.
     */
    public ItemStack getCookingResult(ItemStack p_151395_1_)
    {
        Iterator iterator = this.cookingList.entrySet().iterator();
        Entry entry;

        do
        {
            if (!iterator.hasNext())
            {
                return null;
            }

            entry = (Entry)iterator.next();
        }
        while (!this.func_151397_a(p_151395_1_, (ItemStack)entry.getKey()));

        return (ItemStack)entry.getValue();
    }

    private boolean func_151397_a(ItemStack p_151397_1_, ItemStack p_151397_2_)
    {
        return p_151397_2_.getItem() == p_151397_1_.getItem() && (p_151397_2_.getItemDamage() == 32767 || p_151397_2_.getItemDamage() == p_151397_1_.getItemDamage());
    }

    public Map getCookingList()
    {
        return this.cookingList;
    }

    public float func_151398_b(ItemStack p_151398_1_)
    {
    	
        float ret = p_151398_1_.getItem().getSmeltingExperience(p_151398_1_);
        if (ret != -1) return ret;

        Iterator iterator = this.experienceList.entrySet().iterator();
        Entry entry;

        do
        {
            if (!iterator.hasNext())
            {
                return 0.0F;
            }

            entry = (Entry)iterator.next();
        }
        while (!this.func_151397_a(p_151398_1_, (ItemStack)entry.getKey()));

        return ((Float)entry.getValue()).floatValue();
    }
}