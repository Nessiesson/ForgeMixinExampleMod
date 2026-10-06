package com.example.examplemod.fluid;

import net.minecraft.tileentity.TileEntity;

/**
 * A network-only pipe node.
 *
 * The pipe itself does not expose a tank: the network moves fluid between
 * connected handlers. This prevents pipe contents from becoming a second
 * source of truth.
 */
public class TileEntityFluidPipe extends TileEntity
{
}
