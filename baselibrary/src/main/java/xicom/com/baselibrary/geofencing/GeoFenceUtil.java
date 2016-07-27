package xicom.com.baselibrary.geofencing;

import android.content.Context;
import android.support.annotation.NonNull;

import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

import xicom.com.baselibrary.geofencing.providers.GeofencingProvider;
import xicom.com.baselibrary.geofencing.providers.OnGeofencingTransitionListener;

/**
 * Created by sanidhya on 25/7/16.
 */
public class GeoFenceUtil {

    private Context context;

    private GeoFenceUtil(Context context) {
        this.context = context;
    }

    public static GeoFenceUtil with(Context context) {
        return new Builder(context).build();
    }

    public static class Builder {
        private final Context context;

        public Builder(@NonNull Context context) {
            this.context = context;
        }

        public GeoFenceUtil build() {
            return new GeoFenceUtil(context);
        }

    }

    /**
     * @return request handler for geofencing operations
     */
    public GeofencingControl geofencing() {
        return geofencing(new GeofencingGooglePlayServicesProvider());
    }

    /**
     * @param geofencingProvider geofencing provider we want to use
     * @return request handler for geofencing operations
     */
    public GeofencingControl geofencing(GeofencingProvider geofencingProvider) {
        return new GeofencingControl(this, geofencingProvider);
    }


    public static class GeofencingControl {
        private static final Map<Context, GeofencingProvider> MAPPING = new WeakHashMap<>();

        private GeofencingProvider provider;

        public GeofencingControl(@NonNull GeoFenceUtil smartLocation, @NonNull GeofencingProvider geofencingProvider) {

            if (!MAPPING.containsKey(smartLocation.context)) {
                MAPPING.put(smartLocation.context, geofencingProvider);
            }
            provider = MAPPING.get(smartLocation.context);

            provider.init(smartLocation.context);
        }

        public GeofencingControl add(@NonNull GeofenceModel geofenceModel) {
            provider.addGeofence(geofenceModel);
            return this;
        }

        public GeofencingControl remove(@NonNull String geofenceId) {
            provider.removeGeofence(geofenceId);
            return this;
        }

        public GeofencingControl addAll(@NonNull List<GeofenceModel> geofenceModelList) {
            provider.addGeofences(geofenceModelList);
            return this;
        }

        public GeofencingControl removeAll(@NonNull List<String> geofenceIdsList) {
            provider.removeGeofences(geofenceIdsList);
            return this;
        }

        public void start(OnGeofencingTransitionListener listener) {
            if (provider == null) {
                throw new RuntimeException("A provider must be initialized");
            }
            provider.start(listener);
        }

        public void stop() {
            provider.stop();
        }
    }
}
