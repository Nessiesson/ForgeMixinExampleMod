package com.example.examplemod.block;

import com.example.examplemod.ExampleMod;
import com.example.examplemod.fluid.IFluidPipe;
import com.example.examplemod.fluid.TileEntityFluidPipe;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockFluidPipe
        extends BlockContainerBase
        implements IFluidPipe
{
    public BlockFluidPipe()
    {
        super(
                Material.IRON,
                "fluid_pipe",
                ExampleMod.MACHINES_TAB
        );

        setHardness(2.0F);
        setResistance(6.0F);
    }

    @Override
    public TileEntity createNewTileEntity(
            World world,
            int meta)
    {
        return new TileEntityFluidPipe();
    }

    @Override
    public boolean isOpaqueCube(IBlockState state)
    {
        return true;
    }
}
