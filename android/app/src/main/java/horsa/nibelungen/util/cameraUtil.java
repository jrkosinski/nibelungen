package horsa.nibelungen.util;

import android.content.Context;
import android.content.pm.PackageManager;

/**
 * Created by UserZ150 on 6/10/2560.
 */
public class cameraUtil {
    /** Check if this device has a camera */
    public static boolean checkCameraHardware(Context context) {
        if (context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)){
            // this device has a camera
            return true;
        }

        return false;
    }
}
