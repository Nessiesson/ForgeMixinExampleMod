package com.example.examplemod.util;

import com.example.examplemod.ExampleMod;
import com.example.examplemod.init.ModBlocks;
import com.example.examplemod.init.ModItems;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber(modid = ExampleMod.MODID)
public final class OreDropHandler {
    private OreDropHandler() {}

    @SubscribeEvent
    public static void onHarvestDrops(BlockEvent.HarvestDropsEvent event) {
        if (event.getState().getBlock() != Blocks.IRON_ORE || event.isSilkTouching()) return;

        event.getDrops().clear();
        int fortune = event.getFortuneLevel();
        int amount = 1 + (fortune > 0 ? event.getWorld().rand.nextInt(fortune + 1) : 0);
        event.getDrops().add(new ItemStack(ModItems.RAW_IRON, amount));
        event.setDropChance(1.0F);

        if (event.getState().getBlock() != ModBlocks.WITHERITE_ORE || event.isSilkTouching()) return;

        event.getDrops().clear();
        event.getDrops().add(new ItemStack(ModItems.WITHERITE, amount));
        event.setDropChance(1.0F);


    }
}
