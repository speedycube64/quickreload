// This class retrieves SpeedRunIGT data so that the mod can wait for timer data to finish saving before it reloads the
// world

package net.fabricmc.quickreload;

import com.redlimerl.speedrunigt.timer.InGameTimer;

import java.lang.reflect.Field;

public class IGTHelper {

    public static boolean getSaveTask(){
        try {
            // Access the private member of the timer class
            InGameTimer timer = InGameTimer.getInstance();
            Field field = timer.getClass().getDeclaredField("waitingSaveTask");
            field.setAccessible(true);
            boolean value = field.getBoolean(timer);
            return value;
        } catch (Exception e)
        {
            System.out.println("Couldn't access waitingSaveTask");
            return false;
        }
    }

}
