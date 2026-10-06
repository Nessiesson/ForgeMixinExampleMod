package com.example.examplemod.fluid;

import com.example.examplemod.ExampleMod;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

/**
 * Runs the network on the server thread.
 */
@Mod.EventBusSubscriber(modid = ExampleMod.MODID)
public final class FluidNetworkTicker
{
    private FluidNetworkTicker()
    {
    }

    @SubscribeEvent
    public static void onWorldTick(
            TickEvent.WorldTickEvent event)
    {
        if (event.phase != TickEvent.Phase.END)
        {
            return;
        }

        if (event.world.isRemote)
        {
            return;
        }

        if ((event.world.getTotalWorldTime() % 5L) != 0L)
        {
            return;
        }

        FluidNetworkManager.tickWorld(event.world);
    }
}
