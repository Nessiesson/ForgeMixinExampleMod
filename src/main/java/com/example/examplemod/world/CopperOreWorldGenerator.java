package com.example.examplemod.world;

import com.example.examplemod.init.ModBlocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.feature.WorldGenMinable;
import net.minecraftforge.fml.common.IWorldGenerator;

import java.util.Random;

public class CopperOreWorldGenerator implements IWorldGenerator {
    private static final int VEINS_PER_CHUNK = 12;
    private static final int VEIN_SIZE = 8;
    private static final int MIN_HEIGHT = 4;
    private static final int MAX_HEIGHT = 64;

    @Override
    public void generate(Random random, int chunkX, int chunkZ, World world,
                         IChunkGenerator chunkGenerator, IChunkProvider chunkProvider) {
        if (world.provider.getDimension() != 0) return;

        WorldGenMinable generator = new WorldGenMinable(ModBlocks.COPPER_ORE.getDefaultState(), VEIN_SIZE);
        for (int i = 0; i < VEINS_PER_CHUNK; i++) {
            int x = chunkX * 16 + random.nextInt(16);
            int y = MIN_HEIGHT + random.nextInt(MAX_HEIGHT - MIN_HEIGHT);
            int z = chunkZ * 16 + random.nextInt(16);
            generator.generate(world, random, new BlockPos(x, y, z));
        }
    }
}
