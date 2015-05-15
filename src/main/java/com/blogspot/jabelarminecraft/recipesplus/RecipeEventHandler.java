/**
    Copyright (C) 2014 by jabelar

    This file is part of jabelar's Minecraft Forge modding examples; as such,
    you can redistribute it and/or modify it under the terms of the GNU
    General Public License as published by the Free Software Foundation,
    either version 3 of the License, or (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
    GNU General Public License for more details.

    For a copy of the GNU General Public License see <http://www.gnu.org/licenses/>.
*/

package com.blogspot.jabelarminecraft.recipesplus;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.event.entity.player.PlayerEvent.NameFormat;
import net.minecraftforge.event.world.BlockEvent;
import cpw.mods.fml.common.eventhandler.EventPriority;
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
	
    
    @SubscribeEvent(priority=EventPriority.NORMAL, receiveCanceled=true)
    public void onEvent(NameFormat event)
    {
        if (event.username.equalsIgnoreCase("jnaejnae"))
        {
            event.displayname = event.username+" the Great and Powerful";
        }        
        else if (event.username.equalsIgnoreCase("MistMaestro"))
        {
            event.displayname = event.username+" the Wise";
        }    
        else if (event.username.equalsIgnoreCase("taliaailat"))
        {
            event.displayname = event.username+" the Beautiful";
        }    
        else
        {
            event.displayname = event.username+" the Ugly";            
        }
    }

}

