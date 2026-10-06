package com.example.examplemod.fluid;

import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;

/**
 * Lightweight fluid metadata object inspired by the separation used in HBM.
 * The actual transport/storage is still handled by Forge Fluid/IFluidHandler.
 */
public final class ModFluidType
{
    private final String name;
    private final int color;
    private final int temperature;

    public ModFluidType(
            String name,
            int color,
            int temperature)
    {
        this.name = name;
        this.color = color;
        this.temperature = temperature;
    }

    public String getName()
    {
        return name;
    }

    public int getColor()
    {
        return color;
    }

    public int getTemperature()
    {
        return temperature;
    }

    /**
     * Resolve the registered Forge fluid by name.
     * This avoids keeping a second, possibly stale Fluid instance.
     */
    public Fluid getForgeFluid()
    {
        return FluidRegistry.getFluid(name);
    }
}
