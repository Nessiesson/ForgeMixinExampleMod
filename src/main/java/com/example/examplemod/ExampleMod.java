package com.example.examplemod;

import com.example.examplemod.client.GuiHandler;
import com.example.examplemod.fluid.ModFluids;
import com.example.examplemod.init.ModItems;
import com.example.examplemod.tileentity.TileEntityMuffleFurnace;
import com.example.examplemod.fluid.TileEntityFluidPipe;
import com.example.examplemod.tileentity.TileEntityFluidTank;
import com.example.examplemod.world.CopperOreWorldGenerator;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;
import com.example.examplemod.recipe.MuffleFurnaceRecipes;
import com.example.examplemod.init.ModBlocks;

import org.apache.logging.log4j.Logger;

@Mod(
        modid = ExampleMod.MODID,
        name = ExampleMod.NAME,
        version = ExampleMod.VERSION
)
public class ExampleMod
{
    public static final String MODID = "examplemod";
    public static final String NAME = "Example Mod";
    public static final String VERSION = "1.0";

    public static final int GUI_MUFFLE_FURNACE = 1;

    @Mod.Instance(MODID)
    public static ExampleMod INSTANCE;

    public static final CreativeTabs CREATIVE_TAB =
            new CreativeTabs("examplemod")
            {
                @Override
                public ItemStack createIcon()
                {
                    return new ItemStack(ModItems.RAW_COPPER);
                }
            };

    public static final CreativeTabs MACHINES_TAB =
            new CreativeTabs("examplemod.machines")
            {
                @Override
                public ItemStack createIcon()
                {
                    return new ItemStack(ModItems.FLYWHEEL);
                }
            };

    private static Logger logger;

    /*
     * Forge's Universal Bucket must be enabled before fluid/bucket
     * registration begins. Static initialization guarantees that timing.
     */
    static
    {
        FluidRegistry.enableUniversalBucket();
    }

    @EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        logger = event.getModLog();

        ModFluids.registerFluids();

        GameRegistry.registerTileEntity(
                TileEntityMuffleFurnace.class,
                new ResourceLocation(MODID, "muffle_furnace")
        );

        GameRegistry.registerTileEntity(
                TileEntityFluidPipe.class,
                new ResourceLocation(MODID, "fluid_pipe")
        );

        GameRegistry.registerTileEntity(
                TileEntityFluidTank.class,
                new ResourceLocation(MODID, "fluid_tank")
        );

        GameRegistry.registerWorldGenerator(
                new CopperOreWorldGenerator(),
                0
        );

        NetworkRegistry.INSTANCE.registerGuiHandler(
                this,
                new GuiHandler()
        );
    }

    @EventHandler
    public void init(FMLInitializationEvent event)
    {
        logger.info(
                "DIRT BLOCK >> {}",
                Blocks.DIRT.getRegistryName()
        );

        MuffleFurnaceRecipes.registerRecipes();

        GameRegistry.addShapedRecipe(
                new ResourceLocation(MODID, "muffle_furnace"),
                null,
                new ItemStack(ModBlocks.MUFFLE_FURNACE),
                "KJK",
                "K K",
                "JJJ",
                'K', Blocks.STONE,
                'J', Items.IRON_INGOT
        );

        // Steam engine casing: 2x3 rectangle of copper ingots.
        GameRegistry.addShapedRecipe(
                new ResourceLocation(MODID, "steam_engine_casing_vertical"),
                null,
                new ItemStack(ModBlocks.STEAM_ENGINE_CASING),
                "MM",
                "MM",
                "MM",
                'M', ModItems.COPPER_INGOT
        );

        // Fluid pipe: a simple industrial pipe block.
        GameRegistry.addShapedRecipe(
                new ResourceLocation(MODID, "fluid_pipe"),
                null,
                new ItemStack(ModBlocks.FLUID_PIPE, 8),
                "I I",
                " I ",
                "I I",
                'I', Items.IRON_INGOT
        );

        // Fluid tank: 16,000 mB universal storage endpoint.
        GameRegistry.addShapedRecipe(
                new ResourceLocation(MODID, "fluid_tank"),
                null,
                new ItemStack(ModBlocks.FLUID_TANK),
                "III",
                "I I",
                "III",
                'I', Items.IRON_INGOT
        );

        // Same recipe rotated: 3x2 rectangle of copper ingots.
        GameRegistry.addShapedRecipe(
                new ResourceLocation(MODID, "steam_engine_casing_horizontal"),
                null,
                new ItemStack(ModBlocks.STEAM_ENGINE_CASING),
                "MMM",
                "MMM",
                'M', ModItems.COPPER_INGOT
        );
    }
}
