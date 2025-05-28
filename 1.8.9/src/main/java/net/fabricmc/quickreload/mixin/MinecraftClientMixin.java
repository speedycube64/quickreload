package net.fabricmc.quickreload.mixin;

import net.fabricmc.quickreload.QuickReloadKeyBindings;
import net.fabricmc.quickreload.QuickReloadHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.GameOptions;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Arrays;

@Mixin(MinecraftClient.class)
public class MinecraftClientMixin {

    private boolean quickReloadKeyRegistered = false;

    // Add the quick reload hotkey as soon as options are done initializing
    @Inject(method="tick", at=@At("TAIL"))
    private void registerNewHotkey(CallbackInfo ci) {
        if (!quickReloadKeyRegistered && MinecraftClient.getInstance().options != null) {
            GameOptions options = MinecraftClient.getInstance().options;

            // Expand the keybinding array and add the quick reload key
            options.allKeys = Arrays.copyOf(options.allKeys, options.allKeys.length + 1);
            options.allKeys[options.allKeys.length - 1] = QuickReloadKeyBindings.quickReloadKey;

            quickReloadKeyRegistered = true;
            System.out.println("Quick Reload keybinding registered.");
        }
    }

    // Quick reload if the hotkey is pressed
    @Inject(method="tick", at=@At("TAIL"))
    private void quickReload(CallbackInfo ci) {
        if (quickReloadKeyRegistered && QuickReloadKeyBindings.quickReloadKey.isPressed()) {
            MinecraftClient client = (MinecraftClient)(Object)(this);
            QuickReloadHelper.quickReload(client);
        }
    }
}
