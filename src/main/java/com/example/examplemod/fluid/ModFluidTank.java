package com.example.examplemod.fluid;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;

/**
 * Forge FluidTank with two HBM-like additions:
 * 1) a fixed fluid filter;
 * 2) a pressure value reserved for the future fluid-network layer.
 */
public class ModFluidTank extends FluidTank
{
    private final Fluid allowedFluid;
    private int pressure;

    public ModFluidTank(
            TileEntity owner,
            int capacity,
            Fluid allowedFluid)
    {
        super(capacity);

        this.allowedFluid = allowedFluid;
        this.pressure = 0;

        setTileEntity(owner);
    }

    public Fluid getAllowedFluid()
    {
        return allowedFluid;
    }

    public int getPressure()
    {
        return pressure;
    }

    public void setPressure(int pressure)
    {
        this.pressure = Math.max(0, pressure);
    }

    @Override
    public boolean canFillFluidType(FluidStack fluid)
    {
        return fluid != null
                && (allowedFluid == null
                || fluid.getFluid() == allowedFluid);
    }

    @Override
    public boolean canDrainFluidType(FluidStack fluid)
    {
        if (fluid == null)
        {
            return false;
        }

        return this.fluid != null
                && this.fluid.getFluid() == fluid.getFluid();
    }

    @Override
    protected void onContentsChanged()
    {
        super.onContentsChanged();

        if (tile != null)
        {
            tile.markDirty();
        }
    }

    public NBTTagCompound writeToNBTWithPressure(
            NBTTagCompound nbt)
    {
        super.writeToNBT(nbt);

        nbt.setInteger(
                "Pressure",
                pressure
        );

        return nbt;
    }

    public ModFluidTank readFromNBTWithPressure(
            NBTTagCompound nbt)
    {
        super.readFromNBT(nbt);

        pressure = nbt.getInteger("Pressure");

        return this;
    }
}
