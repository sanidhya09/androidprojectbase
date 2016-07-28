package xicom.com.baselibrary.geofencing;

import android.content.Context;

import java.util.List;

import xicom.com.baselibrary.geofencing.model.GeofenceModel;
import xicom.com.baselibrary.geofencing.utils.Logger;


/**
 * Created by sanidhya on 20/12/14.
 */
public interface GeofencingProvider {
    void init(Context context, Logger logger);

    void start(OnGeofencingTransitionListener listener);

    void addGeofence(GeofenceModel geofence);

    void addGeofences(List<GeofenceModel> geofenceList);

    void removeGeofence(String geofenceId);

    void removeGeofences(List<String> geofenceIds);

    void stop();

}
