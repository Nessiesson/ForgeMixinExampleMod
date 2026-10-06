package com.example.examplemod.client;

import com.example.examplemod.ExampleMod;
import com.example.examplemod.init.ModBlocks;
import com.example.examplemod.init.ModItems;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;

@Mod.EventBusSubscriber(
        modid = ExampleMod.MODID,
        value = Side.CLIENT
)
public final class ClientEventSubscriber
{
    private ClientEventSubscriber()
    {
    }

    @SubscribeEvent
    public static void registerModels(ModelRegistryEvent event)
    {
        for (Item item : ModItems.ALL_ITEMS)
        {
            registerItemModel(item);
        }

        for (Block block : ModBlocks.ALL_BLOCKS)
        {
            Item item = Item.getItemFromBlock(block);

            if (item != null)
            {
                registerItemModel(item);
            }
        }
    }

    private static void registerItemModel(Item item)
    {
        ModelLoader.setCustomModelResourceLocation(
                item,
                0,
                new ModelResourceLocation(
                        item.getRegistryName(),
                        "inventory"
                )
        );
    }
}
