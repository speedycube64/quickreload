package net.fabricmc.quickreload;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.world.ClientWorld;

// This is basically just a function but everything has to be in a class in java because it's such a cool language

public class QuickReloadHelper {

    public static void quickReload(MinecraftClient client) {

        // Check for singleplayer
        if (client.isIntegratedServerRunning()) {

            // check if custom map resetter is loaded
            boolean cmrLoaded = FabricLoader.getInstance().isModLoaded("custom-map-resetter");
            // check if speedrunigt is loaded
            boolean igtLoaded = FabricLoader.getInstance().isModLoaded("speedrunigt");

            // variable for whether the user was in auto reset mode (only applies if custom map resetter is loaded)
            boolean wasResetting = false;

            // get world name
            String saveName = client.getServer().getLevelName();

            // Stop the integrated server (which saves the world)
            client.getServer().stopServer();

            // if custom map resetter is loaded, turn it off
            if (cmrLoaded) {

                // keep track of whether the player was resetting maps
                wasResetting = CMRHelper.getRunning();

                // turn off the resetter
                CMRHelper.setRunning(false);
            }

            // disconnect
            client.world.disconnect();
            client.connect((ClientWorld)null);
            client.setScreen(new TitleScreen());

            // if SpeedRunIGT is loaded, wait until the timer is done saving data
            if (igtLoaded){
                while (IGTHelper.getSaveTask());
            }

            // reconnect
            client.startIntegratedServer(saveName, saveName, null);

            // turn the resetter back on if it was on before
            if (cmrLoaded) {
                CMRHelper.setRunning(wasResetting);
            }
        }
    }

}
