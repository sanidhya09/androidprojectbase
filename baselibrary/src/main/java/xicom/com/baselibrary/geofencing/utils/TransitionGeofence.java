package xicom.com.baselibrary.geofencing.utils;


import xicom.com.baselibrary.geofencing.model.GeofenceModel;

/**
 * Wraps Geofences and Transitions
 */
public class TransitionGeofence {
    private GeofenceModel geofenceModel;
    private int transitionType;

    public TransitionGeofence(GeofenceModel geofence, int transitionType) {
        this.geofenceModel = geofence;
        this.transitionType = transitionType;
    }

    public GeofenceModel getGeofenceModel() {
        return geofenceModel;
    }

    public int getTransitionType() {
        return transitionType;
    }
}
