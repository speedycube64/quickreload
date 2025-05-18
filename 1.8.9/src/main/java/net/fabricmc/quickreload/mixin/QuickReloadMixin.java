package net.fabricmc.quickreload.mixin;

<<<<<<< Updated upstream
<<<<<<< Updated upstream
import net.minecraft.client.MinecraftClient;
=======
import net.fabricmc.loader.api.FabricLoader;
>>>>>>> Stashed changes
=======
import net.fabricmc.loader.api.FabricLoader;
>>>>>>> Stashed changes
import net.minecraft.client.gui.screen.GameMenuScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.world.SaveHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

<<<<<<< Updated upstream
<<<<<<< Updated upstream
import java.util.List;
=======
import net.fabricmc.quickreload.CMRHelper;
>>>>>>> Stashed changes
=======
import net.fabricmc.quickreload.CMRHelper;
>>>>>>> Stashed changes

@Mixin(GameMenuScreen.class)
public class QuickReloadMixin extends Screen{

    @Inject(method = "init", at = @At("TAIL"))
    private void addQuickReloadButton(CallbackInfo ci) {
        this.buttons.add(new ButtonWidget(
                146, //arbitrary button ID
                this.width / 2 - 100,
                this.height / 4 + 144 - 16, //right below S+Q button
                "Quick Reload"
        ));
    }

    @Inject(method = "buttonClicked", at = @At("HEAD"))
    private void quickReload(ButtonWidget button, CallbackInfo ci) {

        // Check for button ID and singleplayer
        if (button.id == 146 && this.client.isIntegratedServerRunning()) {
            // get world name
            String saveName = client.getServer().getLevelName();

<<<<<<< Updated upstream
            // reconnect
            this.client.startIntegratedServer(saveName, saveName, null);
=======
            // disconnect
            this.client.world.disconnect();
            this.client.connect((ClientWorld)null);

            // check if custom map resetter is loaded
            boolean cmrLoaded = FabricLoader.getInstance().isModLoaded("custom-map-resetter");

            // if it is, work around custom map resetter's stuff
            if (cmrLoaded) {

                // keep track of whether the player was resetting maps
                boolean wasResetting = CMRHelper.getRunning();

                // turn off the resetter
                CMRHelper.setRunning(false);

                // reload the world
                this.client.startIntegratedServer(saveName, saveName, null);

                // turn the resetter back on if it was on before
                CMRHelper.setRunning(wasResetting);
            }

            // if custom map resetter is NOT loaded just reconnect like normal
            else {
                this.client.startIntegratedServer(saveName, saveName, null);
            }
>>>>>>> Stashed changes
        }
    }

}

//used for debugging to find the pause menu screen to mix into

//@Mixin(MinecraftClient.class)
//public class QuickReloadMixin {
//
//    @Shadow public Screen currentScreen;
//
//    @Inject(method = "setScreen", at = @At("HEAD"))
//    private void onSetScreen(CallbackInfo ci) {
//
//        Screen screen = (Screen)(Object)(this.currentScreen);
//
//        if (screen != null) {
//            System.out.println("Screen class: " + screen.getClass().getName());
//        }
//    }
//}



