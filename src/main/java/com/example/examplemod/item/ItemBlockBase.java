package com.example.examplemod.item;

import com.example.examplemod.block.ICreativeTabBlock;
import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;

public class ItemBlockBase extends ItemBlock
{
    public ItemBlockBase(Block block)
    {
        super(block);
        setRegistryName(block.getRegistryName());

        if (block instanceof ICreativeTabBlock)
        {
            setCreativeTab(
                    ((ICreativeTabBlock) block).getBlockCreativeTab()
            );
        }
    }
}
