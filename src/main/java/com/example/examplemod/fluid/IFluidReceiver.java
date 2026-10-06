package com.example.examplemod.fluid;

import net.minecraftforge.fluids.capability.IFluidHandler;

/**
 * Optional HBM-style receiver marker.
 */
public interface IFluidReceiver
{
    IFluidHandler getFluidReceiver();

    default int getFluidInputRate()
    {
        return 200;
    }

    default int getFluidPriority()
    {
        return 0;
    }

    default int getFluidPressure()
    {
        return 0;
    }
}
