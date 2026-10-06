package com.example.examplemod.fluid;

import com.example.examplemod.ExampleMod;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;

/**
 * Central registry for the mod's fluids.
 *
 * Add new ModFluidType entries here first, then register the matching Forge
 * Fluid in registerFluids().
 */
public final class ModFluids
{
    public static final ModFluidType PROCESS_WATER =
            new ModFluidType(
                    "examplemod_process_water",
                    0xFF4A90E2,
                    300
            );

    private ModFluids()
    {
    }

    public static void registerFluids()
    {
        registerProcessWater();
    }

    private static void registerProcessWater()
    {
        if (FluidRegistry.getFluid(PROCESS_WATER.getName()) != null)
        {
            return;
        }

        Fluid fluid = new Fluid(
                PROCESS_WATER.getName(),
                new ResourceLocation(
                        "minecraft",
                        "blocks/water_still"
                ),
                new ResourceLocation(
                        "minecraft",
                        "blocks/water_flow"
                )
        )
                .setUnlocalizedName(
                        ExampleMod.MODID + ".process_water"
                )
                .setColor(PROCESS_WATER.getColor())
                .setTemperature(PROCESS_WATER.getTemperature())
                .setDensity(1000)
                .setViscosity(1000);

        if (FluidRegistry.registerFluid(fluid))
        {
            /*
             * Universal bucket support is enabled from ExampleMod before
             * fluid registration.
             */
            FluidRegistry.addBucketForFluid(fluid);
        }
    }
}
