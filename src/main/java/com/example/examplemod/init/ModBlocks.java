package com.example.examplemod.init;

import com.example.examplemod.ExampleMod;
import com.example.examplemod.block.BlockBase;
import com.example.examplemod.block.BlockMuffleFurnace;
import com.example.examplemod.block.BlockFluidPipe;
import com.example.examplemod.block.BlockFluidTank;
import com.example.examplemod.block.BlockOreBase;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.ArrayList;
import java.util.List;

@Mod.EventBusSubscriber(modid = ExampleMod.MODID)
public final class ModBlocks
{
    public static final List<Block> ALL_BLOCKS =
            new ArrayList<Block>();

    public static final Block MUFFLE_FURNACE =
            new BlockMuffleFurnace();

    public static final Block FLUID_PIPE =
            new BlockFluidPipe();

    public static final Block FLUID_TANK =
            new BlockFluidTank();

    public static final Block COPPER_ORE =
            new BlockOreBase(
                    "copper_ore",
                    ExampleMod.CREATIVE_TAB,
                    () -> ModItems.RAW_COPPER
            )
                    .setSoundType(SoundType.STONE)
                    .setHardness(3.0F);

    public static final Block STEAM_ENGINE_CASING =
            new BlockBase(
                    Material.IRON,
                    "steam_engine_casing",
                    ExampleMod.MACHINES_TAB
            )
                    .setSoundType(SoundType.METAL)
                    .setHardness(3.0F)
                    .setResistance(6.0F);

    public static final Block RAW_IRON_BLOCK =
            new BlockBase(
                    Material.IRON,
                    "raw_iron_block",
                    ExampleMod.CREATIVE_TAB
            )
                    .setSoundType(SoundType.METAL)
                    .setHardness(5.0F)
                    .setResistance(6.0F);

    public static final Block WITHERITE_ORE =
            new BlockOreBase(
                    "witherite_ore",
                    ExampleMod.CREATIVE_TAB,
                    () -> ModItems.WITHERITE
            )
                    .setSoundType(SoundType.STONE)
                    .setHardness(3.0F)
                    .setResistance(3.0F);

    public static final Block FERRITE_MAGNET_BLOCK =
            new BlockBase(
                    Material.IRON,
                    "ferrite_magnet_block",
                    ExampleMod.CREATIVE_TAB
            )
                    .setSoundType(SoundType.METAL)
                    .setHardness(5.0F)
                    .setResistance(6.0F);

    public static final Block FERRITE_MAGNET_BLANK_BLOCK =
            new BlockBase(
                    Material.IRON,
                    "ferrite_magnet_blank_block",
                    ExampleMod.CREATIVE_TAB
            )
                    .setSoundType(SoundType.METAL)
                    .setHardness(5.0F)
                    .setResistance(6.0F);

    static
    {
        COPPER_ORE.setHarvestLevel("pickaxe", 1);
        STEAM_ENGINE_CASING.setHarvestLevel("pickaxe", 1);
        RAW_IRON_BLOCK.setHarvestLevel("pickaxe", 1);
        WITHERITE_ORE.setHarvestLevel("pickaxe", 1);
        FERRITE_MAGNET_BLANK_BLOCK.setHarvestLevel("pickaxe", 1);
        FERRITE_MAGNET_BLOCK.setHarvestLevel("pickaxe", 1);
    }

    private ModBlocks()
    {
    }

    @SubscribeEvent
    public static void registerBlocks(RegistryEvent.Register<Block> event)
    {
        for (Block block : ALL_BLOCKS)
        {
            event.getRegistry().register(block);
        }
    }

    @SubscribeEvent
    public static void onMissingBlockMappings(
            RegistryEvent.MissingMappings<Block> event)
    {
        for (RegistryEvent.MissingMappings.Mapping<Block> mapping : event.getAllMappings())
        {
            if (mapping.key.getNamespace().equals(ExampleMod.MODID))
            {
                if (mapping.key.getPath().equals("ferrite_magnet"))
                {
                    mapping.remap(FERRITE_MAGNET_BLOCK);
                }
                else if (mapping.key.getPath().equals("ferrite_magnet_blank"))
                {
                    mapping.remap(FERRITE_MAGNET_BLANK_BLOCK);
                }
            }
        }
    }
}
