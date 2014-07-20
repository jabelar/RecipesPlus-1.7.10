package com.blogspot.jabelarminecraft.recipesplus;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.event.world.BlockEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class RecipeEventHandler 
{

	//Only the Event type parameter is what's important (see below for explanations of some types)
	@SubscribeEvent
	public void onEvent(LivingUpdateEvent event)
	{
		//This event has an Entity variable, access it like this: event.entity;
	
	
		//do something to player every update tick:
		if (event.entity instanceof EntityPlayer)
		{
		EntityPlayer player = (EntityPlayer) event.entity;
	//	ItemStack heldItem = player.getHeldItem();
		}
	}
	
	@SubscribeEvent
	public void onEvent(EntityItemPickupEvent event)
	{
		// check if client side before sending message
//		if (event.entity.worldObj.isRemote) 
		{
			System.out.println("Yay loot!");
			Minecraft.getMinecraft().thePlayer.sendChatMessage("Yay loot!");
		}
	}
	
	@SubscribeEvent
	public void onEvent(AttackEntityEvent event)
	{
		// check if client side before sending message
		if (event.entity.worldObj.isRemote) {
			System.out.println("Attack!");
			Minecraft.getMinecraft().thePlayer.sendChatMessage("Attack!");
		}
	}
	
	@SubscribeEvent
	public void onEvent(RenderGameOverlayEvent event)
	{
		
	}
	
	@SubscribeEvent
	public void onEvent(BlockEvent.HarvestDropsEvent event)
	{
	}
}

