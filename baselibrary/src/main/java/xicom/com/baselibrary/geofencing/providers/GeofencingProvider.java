package xicom.com.baselibrary.geofencing.providers;

import android.content.Context;

import java.util.List;

import xicom.com.baselibrary.geofencing.GeofenceModel;

/**
 * Created by mrm on 20/12/14.
 */
public interface GeofencingProvider {
    void init(Context context);

    void start(OnGeofencingTransitionListener listener);

    void addGeofence(GeofenceModel geofence);

    void addGeofences(List<GeofenceModel> geofenceList);

    void removeGeofence(String geofenceId);

    void removeGeofences(List<String> geofenceIds);

    void stop();

}