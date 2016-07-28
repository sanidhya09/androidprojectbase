package xicom.com.baselibrary.geofencing;

import android.content.Context;
import android.support.annotation.NonNull;

import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

import xicom.com.baselibrary.geofencing.model.GeofenceModel;
import xicom.com.baselibrary.geofencing.providers.GeofencingGooglePlayServicesProvider;
import xicom.com.baselibrary.geofencing.utils.Logger;
import xicom.com.baselibrary.geofencing.utils.LoggerFactory;


/**
 * Managing class for SmartLocation library.
 */
public class SmartLocation {

    private Context context;
    private Logger logger;
    private boolean preInitialize;

    /**
     * Creates the SmartLocation basic instance.
     *
     * @param context       execution context
     * @param logger        logger interface
     * @param preInitialize TRUE (default) if we want to instantiate directly the default providers. FALSE if we want to initialize them ourselves.
     */
    private SmartLocation(Context context, Logger logger, boolean preInitialize) {
        this.context = context;
        this.logger = logger;
        this.preInitialize = preInitialize;
    }

    public static SmartLocation with(Context context) {
        return new Builder(context).logging(true).preInitialize(true).build();
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


    public static class Builder {
        private final Context context;
        private boolean loggingEnabled;
        private boolean preInitialize;

        public Builder(@NonNull Context context) {
            this.context = context;
            this.loggingEnabled = false;
            this.preInitialize = true;
        }

        public Builder logging(boolean enabled) {
            this.loggingEnabled = enabled;
            return this;
        }

        public Builder preInitialize(boolean enabled) {
            this.preInitialize = enabled;
            return this;
        }

        public SmartLocation build() {
            return new SmartLocation(context, LoggerFactory.buildLogger(loggingEnabled), preInitialize);
        }

    }

    public static class GeofencingControl {
        private static final Map<Context, GeofencingProvider> MAPPING = new WeakHashMap<>();

        private final SmartLocation smartLocation;
        private GeofencingProvider provider;

        public GeofencingControl(@NonNull SmartLocation smartLocation, @NonNull GeofencingProvider geofencingProvider) {
            this.smartLocation = smartLocation;

            if (!MAPPING.containsKey(smartLocation.context)) {
                MAPPING.put(smartLocation.context, geofencingProvider);
            }
            provider = MAPPING.get(smartLocation.context);

            if (smartLocation.preInitialize) {
                provider.init(smartLocation.context, smartLocation.logger);
            }
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
