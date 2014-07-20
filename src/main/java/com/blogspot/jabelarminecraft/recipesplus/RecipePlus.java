package com.blogspot.jabelarminecraft.recipesplus;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraftforge.common.MinecraftForge;

import com.blogspot.jabelarminecraft.recipesplus.blocks.BlockBlueberry;
import com.blogspot.jabelarminecraft.recipesplus.blocks.BlockPantry;
import com.blogspot.jabelarminecraft.recipesplus.blocks.BlockStove;
import com.blogspot.jabelarminecraft.recipesplus.blocks.BlockTomato;
import com.blogspot.jabelarminecraft.recipesplus.gui.RecipeCreativeTab;
import com.blogspot.jabelarminecraft.recipesplus.items.ItemBlueberry;
import com.blogspot.jabelarminecraft.recipesplus.items.ItemTomato;
import com.blogspot.jabelarminecraft.recipesplus.tileentities.TileEntityPantry;
import com.blogspot.jabelarminecraft.recipesplus.tileentities.TileEntityStove;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.registry.GameRegistry;

@Mod(modid = RecipePlus.MODID, name = RecipePlus.MODNAME, version = RecipePlus.VERSION)
public class RecipePlus
{
    public static final String MODID = "recipeplus";
    public static final String MODNAME = "Recipe+";
    public static final String VERSION = "0.0.1";

    // create custom creativetab for mod items
    public static CreativeTabs tabRecipePlus = new RecipeCreativeTab("Recipe+");
    
    // instantiate blocks
    public final static Block blockTomato = new BlockTomato();
	public final static Block blockBlueberry = new BlockBlueberry();
	public final static Block blockStove = new BlockStove(false);
	public final static Block blockStoveLit = new BlockStove(true);
	public final static Block blockPantry = new BlockPantry();

    // instantiate items
    public final static Item tomato = new ItemTomato();
    public final static Item blueberry = new ItemBlueberry();
    
    @EventHandler
    // preInit "Run before anything else. Read your config, create blocks, items, etc, and register them with the GameRegistry."
    public void preInit(FMLPreInitializationEvent event) {
    	
        // DEBUG
        System.out.println("preInit()");
    	
    	// register blocks
    	GameRegistry.registerBlock(blockTomato, "tomatoes");
    	GameRegistry.registerBlock(blockBlueberry, "blueberries");
    	GameRegistry.registerBlock(blockStove, "stove");
    	GameRegistry.registerBlock(blockStoveLit, "stove_lit");
    	GameRegistry.registerBlock(blockPantry, "pantry");
   	
    	// register items
    	GameRegistry.registerItem(tomato, "tomato");
    	GameRegistry.registerItem(blueberry, "blueberry");
    	
    	// register tileentities
    	GameRegistry.registerTileEntity(TileEntityStove.class, "stove_tile_entity");
    	GameRegistry.registerTileEntity(TileEntityPantry.class, "pantry_tile_entity");

    }

    @EventHandler
    // load "Do your mod setup. Build whatever data structures you care about. Register recipes."
    public void load(FMLInitializationEvent event) {
    	
        // DEBUG
        System.out.println("load()");

        // register custom event listeners
    	MinecraftForge.EVENT_BUS.register(new RecipeEventHandler());
    	// FMLCommonHandler.instance().bus().register(new RecipeEventHandler()); 
    }

    @EventHandler
    // postInit "Handle interaction with other mods, complete your setup based on this."
    public void postInit(FMLPostInitializationEvent event) {
        // DEBUG
        System.out.println("postInit()");
    }
}
