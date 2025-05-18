package net.fabricmc.quickreload.mixin;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.gui.screen.GameMenuScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.world.ClientWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.fabricmc.quickreload.CMRHelper;
import net.fabricmc.quickreload.IGTHelper;

import java.util.Objects;

@Mixin(GameMenuScreen.class)
public class QuickReloadMixin extends Screen{

    @Inject(method = "init", at = @At("TAIL"))
    private void addQuickReloadButton(CallbackInfo ci) {
        this.buttons.add(new ButtonWidget(
                146, //arbitrary button ID
                this.width / 2 - 100,
                this.height / 4 + 160 - 16, //2 rows below S+Q button
                "Quick Reload"
        ));
    }

    @Inject(method = "buttonClicked", at = @At("TAIL"))
    private void quickReload(ButtonWidget button, CallbackInfo ci) {

        // Check for button ID and singleplayer
        if (button.id == 146 && this.client.isIntegratedServerRunning()) {
            // get world name
            String saveName = client.getServer().getLevelName();

            // disconnect
            this.client.world.disconnect();
            this.client.connect((ClientWorld)null);

            // check if speedrunigt is loaded
            boolean igtLoaded = FabricLoader.getInstance().isModLoaded("speedrunigt");

            // if so, wait until the timer is done saving data
            if (igtLoaded) {
                // God will smite me for implementing it this way but if it works I'm fine with that
                while (IGTHelper.getSaveTask());
            }

            // check if custom map resetter is loaded
            boolean cmrLoaded = FabricLoader.getInstance().isModLoaded("custom-map-resetter");

            // if it is, work around custom map resetter's stuff
            if (cmrLoaded) {

                // keep track of whether the player was resetting maps
                boolean wasResetting = CMRHelper.getRunning();

                // turn off the resetter
                CMRHelper.setRunning(false);

                // reconnect
                this.client.startIntegratedServer(saveName, saveName, null);

                // turn the resetter back on if it was on before
                CMRHelper.setRunning(wasResetting);
            }

            // if custom map resetter is NOT loaded just reconnect like normal
            else {

                // reconnect
                this.client.startIntegratedServer(saveName, saveName, null);
            }
        }
    }

}