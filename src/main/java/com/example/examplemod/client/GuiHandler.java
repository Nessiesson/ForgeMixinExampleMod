package com.example.examplemod.client;

import com.example.examplemod.ExampleMod;
import com.example.examplemod.container.ContainerMuffleFurnace;
import com.example.examplemod.tileentity.TileEntityMuffleFurnace;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

public class GuiHandler implements IGuiHandler
{
    @Override
    public Object getServerGuiElement(
            int id,
            EntityPlayer player,
            World world,
            int x,
            int y,
            int z)
    {
        if (id == ExampleMod.GUI_MUFFLE_FURNACE)
        {
            TileEntityMuffleFurnace furnace =
                    (TileEntityMuffleFurnace)
                            world.getTileEntity(
                                    new BlockPos(x, y, z));

            if (furnace != null)
            {
                return new ContainerMuffleFurnace(
                        player.inventory,
                        furnace
                );
            }
        }

        return null;
    }

    @Override
    public Object getClientGuiElement(
            int id,
            EntityPlayer player,
            World world,
            int x,
            int y,
            int z)
    {
        if (id == ExampleMod.GUI_MUFFLE_FURNACE)
        {
            TileEntityMuffleFurnace furnace =
                    (TileEntityMuffleFurnace)
                            world.getTileEntity(
                                    new BlockPos(x, y, z));

            if (furnace != null)
            {
                return new GuiMuffleFurnace(
                        player.inventory,
                        furnace
                );
            }
        }

        return null;
    }
}