package com.example.examplemod.fluid;

import net.minecraftforge.fluids.capability.IFluidHandler;

/**
 * Optional HBM-style provider marker.
 *
 * Forge capability remains the real transport API, so machines from other
 * mods can still participate without implementing this interface.
 */
public interface IFluidProvider
{
    IFluidHandler getFluidProvider();

    default int getFluidOutputRate()
    {
        return 200;
    }

    default int getFluidPriority()
    {
        return 0;
    }

    /**
     * HBM-style pressure channel. Forge FluidStack itself does not carry
     * pressure, so the network keeps it as endpoint metadata.
     */
    default int getFluidPressure()
    {
        return 0;
    }
}
