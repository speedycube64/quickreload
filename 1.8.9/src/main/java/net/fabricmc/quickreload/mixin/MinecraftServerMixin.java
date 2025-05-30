package net.fabricmc.quickreload.mixin;

import net.fabricmc.quickreload.QuickReloadHelper;
import net.minecraft.server.MinecraftServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftServer.class)
public class MinecraftServerMixin {

    @Inject(method = "stopServer", at = @At("TAIL"))
    private void letQuickReloadHelperKnowTheGameFinishedSaving(CallbackInfo ci){
        QuickReloadHelper.setSaveFinishedFlag(true);
    }
}
