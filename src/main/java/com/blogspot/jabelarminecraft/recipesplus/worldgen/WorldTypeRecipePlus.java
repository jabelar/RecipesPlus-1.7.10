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

package com.blogspot.jabelarminecraft.recipesplus.worldgen;

import net.minecraft.world.WorldType;
import net.minecraft.world.gen.layer.GenLayer;
import net.minecraft.world.gen.layer.GenLayerBiomeEdge;
import net.minecraft.world.gen.layer.GenLayerZoom;

/**
 * @author jabelar
 *
 */
public class WorldTypeRecipePlus extends WorldType {

	public WorldTypeRecipePlus(int par1, String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}
	
	   /**
     * Creates the GenLayerBiome used for generating the world
     * 
     * @param worldSeed The world seed
     * @param parentLayer The parent layer to feed into any layer you return
     * @return A GenLayer that will return ints representing the Biomes to be generated, see GenLayerBiome
     */
	@Override
	  public GenLayer getBiomeLayer(long worldSeed, GenLayer parentLayer)
    {
        GenLayer ret = new GenLayerBiomeRecipePlus(200L, parentLayer, this);
        ret = GenLayerZoom.magnify(1000L, ret, 2);
        ret = new GenLayerBiomeEdge(1000L, ret);
        return ret;
    }
}