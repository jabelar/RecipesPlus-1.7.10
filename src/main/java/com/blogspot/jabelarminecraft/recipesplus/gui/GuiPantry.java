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

package com.blogspot.jabelarminecraft.recipesplus.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import com.blogspot.jabelarminecraft.recipesplus.containers.ContainerPantry;
import com.blogspot.jabelarminecraft.recipesplus.tileentities.TileEntityPantry;

public class GuiPantry extends GuiContainer 
{

    private static final ResourceLocation pantryGuiTextures = new ResourceLocation("recipeplus:textures/gui/container/gui_cooking.png");
    private static TileEntityPantry tile_entity_pantry ;
    
	public GuiPantry(InventoryPlayer player, TileEntityPantry tep) 
	{
		// instantiate new gui and pass to super
		super(new ContainerPantry(player, tep));

		tile_entity_pantry = tep;
		
        // DEBUG
        System.out.println("GuiPantry constructor was invoked prior to ContainerPantry constructor(): Client Side = "+tile_entity_pantry.getWorldObj().isRemote); 

	}

    /**
     * Draw the foreground layer for the GuiContainer (everything in front of the items)
     */
    @Override
	protected void drawGuiContainerForegroundLayer(int p_146979_1_, int p_146979_2_)
    {
        String s = this.tile_entity_pantry.hasCustomInventoryName() ? this.tile_entity_pantry.getInventoryName() : I18n.format(this.tile_entity_pantry.getInventoryName(), new Object[0]);
        this.fontRendererObj.drawString(s, this.xSize / 2 - this.fontRendererObj.getStringWidth(s) / 2, 6, 4210752);
        this.fontRendererObj.drawString(I18n.format("container.inventory", new Object[0]), 8, this.ySize - 96 + 2, 4210752);
    }

    @Override
	protected void drawGuiContainerBackgroundLayer(float p_146976_1_, int p_146976_2_, int p_146976_3_)
    {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        Minecraft.getMinecraft().getTextureManager().bindTexture(pantryGuiTextures);
        int k = (this.width - this.xSize) / 2;
        int l = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(k, l, 0, 0, this.xSize, this.ySize);
        int i1;

        // can draw other things like progress bars here 
    }
    
    /**
     * Called when the mouse is clicked.
     */
    @Override
    protected void mouseClicked(int mouseX, int mouseY, int key_code)
    {
        // DEBUG
        System.out.println("GuiPantry mouseClicked(): Client Side = "+tile_entity_pantry.getWorldObj().isRemote+": Slot = "+this.getSlotNumAtPosition(mouseX, mouseY)+": CTRL = "+this.isCtrlKeyDown()+": Shift = "+this.isShiftKeyDown()); 

    	super.mouseClicked(mouseX, mouseY, key_code);
    }
    

    /**
     * Called when a mouse button is pressed and the mouse is moved around. 
     */
    @Override
    protected void mouseClickMove(int mouseX, int mouseY, int lastButtonClicked, long timeSinceMouseClick)
    {
        // DEBUG
        System.out.println("GuiPantry mouseClickMove(): Client Side = "+tile_entity_pantry.getWorldObj().isRemote); 

        super.mouseClickMove(mouseX, mouseY, lastButtonClicked, timeSinceMouseClick);
    }
    
    /**
     * Called when the mouse is moved or a mouse button is released.  which_type==-1 is
     * mouseMove, ==0 is mouseUp left button, or ==1 is mouseUp right button
     */
    @Override
    protected void mouseMovedOrUp(int mouseX, int mouseY, int which_type)
    {
        // DEBUG
        System.out.println("GuiPantry mouseMovedOrUp(): Client Side = "+tile_entity_pantry.getWorldObj().isRemote+": Mouse event type = "+which_type); 
       

    	super.mouseMovedOrUp(mouseX, mouseY, which_type);
    }

    @Override
	protected void handleMouseClick(Slot slot, int p_146984_2_, int p_146984_3_, int p_146984_4_)
    {
        // DEBUG
        System.out.println("GuiPantry handleMouseClick(): Client Side = "+tile_entity_pantry.getWorldObj().isRemote); 

    	super.handleMouseClick(slot, p_146984_2_, p_146984_3_, p_146984_4_);
    }
    
    /**
     * Returns true if this GUI should pause the game when it is displayed in single-player
     */
    @Override
    public boolean doesGuiPauseGame()
    {
        return false;
    }
    
    /**
     * Returns the slot at the given coordinates or null if there is none.
     */
    // this was private in GuiContainer so copied here
    private Slot getSlotAtPosition(int pos_x, int pos_y)
    {
        for (int k = 0; k < this.inventorySlots.inventorySlots.size(); ++k)
        {
            Slot slot = (Slot)this.inventorySlots.inventorySlots.get(k);

            if (this.isMouseOverSlot(slot, pos_x, pos_y))
            {
                return slot;
            }
        }

        return null;
    }
    
    /**
     * Returns the number of the slot at the given coordinates or 999 if there is none.
     */
    private int getSlotNumAtPosition(int pos_x, int pos_y)
    {
        for (int k = 0; k < this.inventorySlots.inventorySlots.size(); ++k)
        {
            Slot slot = (Slot)this.inventorySlots.inventorySlots.get(k);

            if (this.isMouseOverSlot(slot, pos_x, pos_y))
            {
                return k;
            }
        }

        return 999;
    }
   
    /**
     * Returns if the passed mouse position is over the specified slot.
     */
    // this was private in GuiContainer so copied here
   private boolean isMouseOverSlot(Slot slot, int pos_x, int pos_y)
    {
        return this.func_146978_c(slot.xDisplayPosition, slot.yDisplayPosition, 16, 16, pos_x, pos_y);
    }

}

