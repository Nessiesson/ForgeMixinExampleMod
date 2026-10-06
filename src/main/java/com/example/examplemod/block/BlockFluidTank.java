package com.example.examplemod.block;

import com.example.examplemod.ExampleMod;
import com.example.examplemod.tileentity.TileEntityFluidTank;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockFluidTank
        extends BlockContainerBase
{
    public BlockFluidTank()
    {
        super(
                Material.IRON,
                "fluid_tank",
                ExampleMod.MACHINES_TAB
        );

        setHardness(2.5F);
        setResistance(8.0F);
    }

    @Override
    public TileEntity createNewTileEntity(
            World world,
            int meta)
    {
        return new TileEntityFluidTank();
    }

    @Override
    public boolean onBlockActivated(
            World world,
            BlockPos pos,
            IBlockState state,
            EntityPlayer player,
            EnumHand hand,
            EnumFacing facing,
            float hitX,
            float hitY,
            float hitZ)
    {
        TileEntity tile = world.getTileEntity(pos);

        if (!(tile instanceof TileEntityFluidTank))
        {
            return false;
        }

        TileEntityFluidTank tank =
                (TileEntityFluidTank) tile;

        if (!world.isRemote)
        {
            tank.handleContainer(player, hand);
        }

        return true;
    }
}
