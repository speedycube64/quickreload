package net.fabricmc.quickreload;

import net.minecraft.client.option.KeyBinding;
import org.lwjgl.input.Keyboard;

public class QuickReloadKeyBindings {
    public static KeyBinding quickReloadKey = new KeyBinding(
            "key.quickreload", // translation key
            Keyboard.KEY_M,    // default key
            "key.categories.quickreload" // category
    );
}
