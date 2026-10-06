package com.example.examplemod.block;

import com.example.examplemod.ExampleMod;
import com.example.examplemod.tileentity.TileEntityMuffleFurnace;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockMuffleFurnace extends BlockContainerBase
{
    public static final PropertyDirection FACING =
            PropertyDirection.create(
                    "facing",
                    EnumFacing.Plane.HORIZONTAL
            );

    public static final PropertyBool LIT =
            PropertyBool.create("lit");

    public BlockMuffleFurnace()
    {
        super(
                Material.ROCK,
                "muffle_furnace",
                ExampleMod.CREATIVE_TAB
        );

        setHardness(3.5F);
        setResistance(3.5F);

        setDefaultState(
                blockState.getBaseState()
                        .withProperty(FACING, EnumFacing.NORTH)
                        .withProperty(LIT, false)
        );
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
        if (!world.isRemote)
        {
            player.openGui(
                    ExampleMod.INSTANCE,
                    ExampleMod.GUI_MUFFLE_FURNACE,
                    world,
                    pos.getX(),
                    pos.getY(),
                    pos.getZ()
            );
        }

        return true;
    }

    @Override
    public IBlockState getStateForPlacement(
            World world,
            BlockPos pos,
            EnumFacing facing,
            float hitX,
            float hitY,
            float hitZ,
            int meta,
            EntityLivingBase placer)
    {
        return getDefaultState()
                .withProperty(
                        FACING,
                        placer.getHorizontalFacing().getOpposite()
                );
    }

    @Override
    public int getMetaFromState(IBlockState state)
    {
        return state.getValue(FACING).getHorizontalIndex();
    }

    @Override
    public IBlockState getStateFromMeta(int meta)
    {
        switch (meta)
        {
            case 0:
                return getDefaultState()
                        .withProperty(FACING, EnumFacing.NORTH)
                        .withProperty(LIT, false);
            case 1:
                return getDefaultState()
                        .withProperty(FACING, EnumFacing.SOUTH)
                        .withProperty(LIT, false);
            case 2:
                return getDefaultState()
                        .withProperty(FACING, EnumFacing.WEST)
                        .withProperty(LIT, false);
            case 3:
                return getDefaultState()
                        .withProperty(FACING, EnumFacing.EAST)
                        .withProperty(LIT, false);
            default:
                return getDefaultState()
                        .withProperty(FACING, EnumFacing.NORTH)
                        .withProperty(LIT, false);
        }
    }

    @Override
    protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, FACING, LIT);
    }

    @Override
    public net.minecraft.tileentity.TileEntity createNewTileEntity(
            World world,
            int meta)
    {
        return new TileEntityMuffleFurnace();
    }

    @Override
    public EnumBlockRenderType getRenderType(IBlockState state)
    {
        return EnumBlockRenderType.MODEL;
    }
}
