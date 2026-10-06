package com.example.examplemod.item;

import com.example.examplemod.ExampleMod;
import com.example.examplemod.init.ModItems;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

public class ItemBase extends Item
{
    public ItemBase(String name, CreativeTabs tab)
    {
        setTranslationKey(ExampleMod.MODID + "." + name);
        setRegistryName(ExampleMod.MODID, name);
        setCreativeTab(tab);
        ModItems.ALL_ITEMS.add(this);
    }
}
