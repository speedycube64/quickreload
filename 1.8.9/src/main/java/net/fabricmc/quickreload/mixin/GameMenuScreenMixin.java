package net.fabricmc.quickreload.mixin;

import net.fabricmc.quickreload.QuickReloadHelper;
import net.minecraft.client.gui.screen.GameMenuScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameMenuScreen.class)
public class GameMenuScreenMixin extends Screen{

    @Inject(method = "init", at = @At("TAIL"))
    private void addQuickReloadButton(CallbackInfo ci) {
        // Check for singleplayer
        if (this.client.isInSingleplayer()) {
            // Add quick reload button
            this.buttons.add(new ButtonWidget(
                    146, //arbitrary button ID
                    this.width / 2 - 100,
                    this.height / 4 + 160 - 16, //2 rows below S+Q button
                    "Quick Reload"));
        }
    }

    @Inject(method = "buttonClicked", at = @At("TAIL"))
    private void quickReload(ButtonWidget button, CallbackInfo ci) {

        // Check for quick reload button pressed
        if (button.id == 146) {

            // disable the button so my dumb ass can't click it twice on accident
            button.active = false;

            // Quick reload
            QuickReloadHelper.quickReload(this.client);
        }
    }

}