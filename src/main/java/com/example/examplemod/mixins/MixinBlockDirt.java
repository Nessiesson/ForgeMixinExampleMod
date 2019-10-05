package com.example.examplemod.mixins;

import net.minecraft.block.BlockDirt;
import net.minecraftforge.fml.common.FMLLog;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BlockDirt.class)
public abstract class MixinBlockDirt
{
    @Inject(method = "<init>", at = @At("RETURN"))
    private void onDirtInit(CallbackInfo ci)
    {
        // you really should not be using FMLLog.
        FMLLog.info("Hello from Mixins!");
    }
}
