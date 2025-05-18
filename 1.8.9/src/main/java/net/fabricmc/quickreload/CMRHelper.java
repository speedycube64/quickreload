// This class hosts the custom map resetter stuff outside of the mixin so that the mixin works even if custom map
// resetter is not loaded

package net.fabricmc.quickreload;
import xyz.tildejustin.custommapresetter.CustomMapResetter;

public class CMRHelper {

    public static void setRunning(boolean value){
        CustomMapResetter.running = value;
    }

    public static boolean getRunning(){
        return CustomMapResetter.running;
    }

}
