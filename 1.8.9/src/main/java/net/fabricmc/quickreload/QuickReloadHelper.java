package net.fabricmc.quickreload;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.world.ClientWorld;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class QuickReloadHelper{

    private static final Logger LOGGER = LogManager.getLogger();

    // Flag for whether the game has finished saving
    // This is set at the end of stopServer() in MinecraftServerMixin
    // This has to be volatile because the MinecraftServerMixin can change it
    private static volatile boolean saveFinished = false;
    public static void setSaveFinishedFlag(boolean value) { saveFinished = value; }

    // Function to quick reload the current world
    public static void quickReload(MinecraftClient client) {

        LOGGER.info("Quick reloading...");

        // keep track of whether resetter was on (only applies if custom map resetter is loaded)
        boolean wasResetting = false;

        // get world name
        String saveName = client.getServer().getLevelName();

        // check if speedrunigt is loaded
        boolean igtLoaded = FabricLoader.getInstance().isModLoaded("speedrunigt");
        // check if custom map resetter is loaded
        boolean cmrLoaded = FabricLoader.getInstance().isModLoaded("custom-map-resetter");

        // If custom map resetter is loaded, turn off the resetter before quick reloading
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

        // wait for the server to stop saving game data
        LOGGER.info("Waiting for save signal...");
        long ts = System.currentTimeMillis();
        while (true) {
            if (saveFinished)
            {
                LOGGER.info("Save signal received. Reloading...");
                break;
            }
            // 2 s timeout
            if (System.currentTimeMillis() - ts > 2000) {
                LOGGER.warn("Did not receive signal. Reloading anyway...");
                break;
            }
        }
        saveFinished = false;

        // if speedrunigt is loaded wait for it to finish saving timer data
        if (igtLoaded) {
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
