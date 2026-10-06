package com.example.examplemod.block;

import com.example.examplemod.ExampleMod;
import com.example.examplemod.init.ModBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;

public class BlockBase extends Block implements ICreativeTabBlock
{
    private final CreativeTabs creativeTab;

    public BlockBase(Material material, String name, CreativeTabs tab)
    {
        super(material);
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

    public BlockBase setSoundType(SoundType soundType)
    {
        super.setSoundType(soundType);
        return this;
    }
}
