package com.example.examplemod.block;

import com.example.examplemod.ExampleMod;
import com.example.examplemod.init.ModBlocks;
import net.minecraft.block.BlockOre;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

import java.util.Random;
import java.util.function.Supplier;

public class BlockOreBase extends BlockOre implements ICreativeTabBlock
{
    private final Supplier<Item> drop;
    private final CreativeTabs creativeTab;

    public BlockOreBase(String name, CreativeTabs tab, Supplier<Item> drop)
    {
        super();
        this.drop = drop;
        this.creativeTab = tab;
        setTranslationKey(ExampleMod.MODID + "." + name);
        setRegistryName(ExampleMod.MODID, name);
        setCreativeTab(tab);
        ModBlocks.ALL_BLOCKS.add(this);
    }

    @Override
    public CreativeTabs getBlockCreativeTab()
    {
        return creativeTab;
    }

    public BlockOreBase setSoundType(SoundType soundType)
    {
        super.setSoundType(soundType);
        return this;
    }

    @Override
    public Item getItemDropped(IBlockState state, Random rand, int fortune)
    {
        return drop.get();
    }
}
