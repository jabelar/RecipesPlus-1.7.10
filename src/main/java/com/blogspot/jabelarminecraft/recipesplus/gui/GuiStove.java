package com.blogspot.jabelarminecraft.recipesplus.gui;


import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import com.blogspot.jabelarminecraft.recipesplus.containers.ContainerStove;
import com.blogspot.jabelarminecraft.recipesplus.tileentities.TileEntityStove;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiStove extends GuiContainer
{
    private static final ResourceLocation stoveGuiTextures = new ResourceLocation("recipeplus:textures/gui/container/gui_cooking.png");
    private final TileEntityStove tileStove;

    public GuiStove(InventoryPlayer par1InventoryPlayer, TileEntityStove par2TileEntityStove)
    {
    	// instantiate new GUI and pass to super
        super(new ContainerStove(par1InventoryPlayer, par2TileEntityStove));
        this.tileStove = par2TileEntityStove;
        // DEBUG
        System.out.println("GuiStove() constructor");

    }

    /**
     * Draw the foreground layer for the GuiContainer (everything in front of the items)
     */
    @Override
	protected void drawGuiContainerForegroundLayer(int p_146979_1_, int p_146979_2_)
    {
        String s = this.tileStove.hasCustomInventoryName() ? this.tileStove.getInventoryName() : I18n.format(this.tileStove.getInventoryName(), new Object[0]);
        this.fontRendererObj.drawString(s, this.xSize / 2 - this.fontRendererObj.getStringWidth(s) / 2, 6, 4210752);
        this.fontRendererObj.drawString(I18n.format("container.inventory", new Object[0]), 8, this.ySize - 96 + 2, 4210752);
    }

    @Override
	protected void drawGuiContainerBackgroundLayer(float p_146976_1_, int p_146976_2_, int p_146976_3_)
    {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        Minecraft.getMinecraft().getTextureManager().bindTexture(stoveGuiTextures);
        int k = (this.width - this.xSize) / 2;
        int l = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(k, l, 0, 0, this.xSize, this.ySize);
        int i1;

        // draw progress bar
        if (this.tileStove.isBurning())
        {
            i1 = this.tileStove.getBurnTimeRemainingScaled(12);
            this.drawTexturedModalRect(k + 56, l + 36 + 12 - i1, 176, 12 - i1, 14, i1 + 2);
        }

        i1 = this.tileStove.getCookProgressScaled(24);
        this.drawTexturedModalRect(k + 79, l + 34, 176, 14, i1 + 1, 16);
    }
}