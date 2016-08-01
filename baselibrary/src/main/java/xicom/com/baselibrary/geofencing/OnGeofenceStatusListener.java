package xicom.com.baselibrary.geofencing;

import android.location.Location;

/**
 * Created by sanidhya on 1/8/16.
 */
public interface OnGeofenceStatusListener {
    void onStatus(String id, int transitionType, Location location);
}
