package com.example.examplemod.fluid;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidTankProperties;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Small, deterministic fluid network inspired by HBM's provider/receiver
 * model, but built on top of Forge's IFluidHandler capability.
 *
 * A network is a connected component of fluid pipes. Machines/tanks touching
 * any pipe in the component become endpoints of that network.
 */
public final class FluidNetworkManager
{
    private static final int MAX_TRANSFER_PER_ENDPOINT = 200;

    private FluidNetworkManager()
    {
    }

    public static void tickWorld(World world)
    {
        if (world == null || world.isRemote)
        {
            return;
        }

        Set<BlockPos> visitedPipes = new HashSet<BlockPos>();

        for (TileEntity tile : world.loadedTileEntityList)
        {
            if (!(tile instanceof TileEntityFluidPipe))
            {
                continue;
            }

            if (!visitedPipes.add(tile.getPos()))
            {
                continue;
            }

            NetworkComponent component =
                    collectComponent(world, tile.getPos(), visitedPipes);

            if (component.handlers.size() >= 2)
            {
                transfer(component.handlers);
            }
        }
    }

    private static NetworkComponent collectComponent(
            World world,
            BlockPos start,
            Set<BlockPos> visitedPipes)
    {
        NetworkComponent component = new NetworkComponent();
        ArrayDeque<BlockPos> queue = new ArrayDeque<BlockPos>();
        Set<TileEntity> seenHandlers = new HashSet<TileEntity>();

        queue.add(start);

        while (!queue.isEmpty())
        {
            BlockPos current = queue.removeFirst();

            for (EnumFacing facing : EnumFacing.VALUES)
            {
                BlockPos next = current.offset(facing);
                TileEntity tile = world.getTileEntity(next);

                if (tile instanceof TileEntityFluidPipe)
                {
                    if (visitedPipes.add(next))
                    {
                        queue.addLast(next);
                    }
                    continue;
                }

                IFluidHandler handler =
                        getFluidHandler(tile, facing.getOpposite());

                if (handler != null && seenHandlers.add(tile))
                {
                    component.handlers.add(
                            new HandlerEndpoint(
                                    tile,
                                    handler
                            )
                    );
                }
            }
        }

        return component;
    }

    private static IFluidHandler getFluidHandler(
            TileEntity tile,
            EnumFacing side)
    {
        if (tile == null)
        {
            return null;
        }

        Capability<IFluidHandler> capability =
                CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY;

        if (!tile.hasCapability(capability, side))
        {
            return null;
        }

        return tile.getCapability(capability, side);
    }

    private static void transfer(
            List<HandlerEndpoint> handlers)
    {
        for (HandlerEndpoint source : handlers)
        {
            List<FluidStack> candidates =
                    getCandidateFluids(source.handler);

            for (FluidStack candidate : candidates)
            {
                if (candidate == null || candidate.amount <= 0)
                {
                    continue;
                }

                int remaining =
                        Math.min(
                                candidate.amount,
                                getOutputRate(source.tile)
                        );

                if (remaining <= 0)
                {
                    continue;
                }

                for (HandlerEndpoint target : handlers)
                {
                    if (target.tile == source.tile)
                    {
                        continue;
                    }

                    if (!pressureMatches(
                            source.tile,
                            target.tile))
                    {
                        continue;
                    }

                    if (remaining <= 0)
                    {
                        break;
                    }

                    int offerAmount =
                            Math.min(
                                    remaining,
                                    getInputRate(target.tile)
                            );

                    if (offerAmount <= 0)
                    {
                        continue;
                    }

                    FluidStack offer =
                            new FluidStack(
                                    candidate.getFluid(),
                                    offerAmount
                            );

                    FluidStack simulated =
                            source.handler.drain(
                                    offer,
                                    false
                            );

                    if (simulated == null || simulated.amount <= 0)
                    {
                        continue;
                    }

                    int fillable =
                            target.handler.fill(
                                    new FluidStack(
                                            candidate.getFluid(),
                                            simulated.amount
                                    ),
                                    false
                            );

                    if (fillable <= 0)
                    {
                        continue;
                    }

                    int amount =
                            Math.min(
                                    simulated.amount,
                                    fillable
                            );

                    FluidStack toTransfer =
                            new FluidStack(
                                    candidate.getFluid(),
                                    amount
                            );

                    FluidStack drained =
                            source.handler.drain(
                                    toTransfer,
                                    true
                            );

                    if (drained == null || drained.amount <= 0)
                    {
                        continue;
                    }

                    int accepted =
                            target.handler.fill(
                                    new FluidStack(
                                            drained.getFluid(),
                                            drained.amount
                                    ),
                                    true
                            );

                    /*
                     * Forge handlers are expected to obey the simulation
                     * contract. If a foreign handler behaves badly, never
                     * try to manufacture the lost fluid here.
                     */
                    remaining -=
                            Math.min(
                                    drained.amount,
                                    accepted
                            );
                }
            }
        }
    }

    private static List<FluidStack> getCandidateFluids(
            IFluidHandler handler)
    {
        List<FluidStack> fluids = new ArrayList<FluidStack>();

        IFluidTankProperties[] properties =
                handler.getTankProperties();

        if (properties == null)
        {
            return fluids;
        }

        for (IFluidTankProperties property : properties)
        {
            if (property == null)
            {
                continue;
            }

            FluidStack contents =
                    property.getContents();

            if (contents != null
                    && contents.amount > 0)
            {
                fluids.add(contents.copy());
            }
        }

        return fluids;
    }

    private static boolean pressureMatches(
            TileEntity source,
            TileEntity target)
    {
        if (source instanceof IFluidProvider
                && target instanceof IFluidReceiver)
        {
            return ((IFluidProvider) source).getFluidPressure()
                    == ((IFluidReceiver) target).getFluidPressure();
        }

        return true;
    }

    private static int getOutputRate(TileEntity tile)
    {
        if (tile instanceof IFluidProvider)
        {
            return Math.max(
                    0,
                    ((IFluidProvider) tile).getFluidOutputRate()
            );
        }

        return MAX_TRANSFER_PER_ENDPOINT;
    }

    private static int getInputRate(TileEntity tile)
    {
        if (tile instanceof IFluidReceiver)
        {
            return Math.max(
                    0,
                    ((IFluidReceiver) tile).getFluidInputRate()
            );
        }

        return MAX_TRANSFER_PER_ENDPOINT;
    }

    private static final class NetworkComponent
    {
        private final List<HandlerEndpoint> handlers =
                new ArrayList<HandlerEndpoint>();
    }

    private static final class HandlerEndpoint
    {
        private final TileEntity tile;
        private final IFluidHandler handler;

        private HandlerEndpoint(
                TileEntity tile,
                IFluidHandler handler)
        {
            this.tile = tile;
            this.handler = handler;
        }
    }
}
