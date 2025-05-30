// This class hosts the custom map resetter stuff outside of the QuickReloadHelper so that quick reload can work with
// custom map resetter loaded or not loaded

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
