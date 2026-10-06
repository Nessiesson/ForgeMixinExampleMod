package com.example.examplemod.tileentity;

import com.example.examplemod.fluid.IFluidProvider;
import com.example.examplemod.fluid.IFluidReceiver;
import com.example.examplemod.fluid.ModFluidTank;
import com.example.examplemod.fluid.ModFluids;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.FluidActionResult;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;

/**
 * Simple 16000 mB storage tank used as the first real network endpoint.
 *
 * Unlike the furnace's fixed process tank, this tank accepts any Forge fluid.
 */
public class TileEntityFluidTank
        extends TileEntity
        implements IFluidProvider, IFluidReceiver
{
    private final ModFluidTank tank =
            new ModFluidTank(
                    this,
                    16000,
                    null
            );

    @Override
    public IFluidHandler getFluidProvider()
    {
        return tank;
    }

    @Override
    public IFluidHandler getFluidReceiver()
    {
        return tank;
    }

    @Override
    public int getFluidOutputRate()
    {
        return 500;
    }

    @Override
    public int getFluidInputRate()
    {
        return 500;
    }

    public ModFluidTank getTank()
    {
        return tank;
    }

    public void handleContainer(
            EntityPlayer player,
            EnumHand hand)
    {
        ItemStack held =
                player.getHeldItem(hand);

        FluidActionResult result =
                FluidUtil.tryEmptyContainer(
                        held,
                        tank,
                        Integer.MAX_VALUE,
                        player,
                        true
                );

        if (result.isSuccess())
        {
            player.setHeldItem(
                    hand,
                    result.getResult()
            );
            markDirty();
            return;
        }

        result =
                FluidUtil.tryFillContainer(
                        held,
                        tank,
                        Integer.MAX_VALUE,
                        player,
                        true
                );

        if (result.isSuccess())
        {
            player.setHeldItem(
                    hand,
                    result.getResult()
            );
            markDirty();
        }
    }

    @Override
    public boolean hasCapability(
            Capability<?> capability,
            EnumFacing facing)
    {
        if (capability ==
                CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY)
        {
            return true;
        }

        return super.hasCapability(
                capability,
                facing
        );
    }

    @Override
    public <T> T getCapability(
            Capability<T> capability,
            EnumFacing facing)
    {
        if (capability ==
                CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY)
        {
            return CapabilityFluidHandler
                    .FLUID_HANDLER_CAPABILITY
                    .cast(tank);
        }

        return super.getCapability(
                capability,
                facing
        );
    }

    @Override
    public NBTTagCompound writeToNBT(
            NBTTagCompound nbt)
    {
        super.writeToNBT(nbt);

        tank.writeToNBTWithPressure(nbt);

        return nbt;
    }

    @Override
    public void readFromNBT(
            NBTTagCompound nbt)
    {
        super.readFromNBT(nbt);

        tank.readFromNBTWithPressure(nbt);
    }
}
