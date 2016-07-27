package xicom.com.baselibrary.locations;

import android.content.Context;


/**
 * Created by sanidhya on 20/7/16.
 */
public class LocationUtil {
    public Context mContext;
    private static LocationProviderInterface locationProvider;

    private LocationUtil(Context context) {
        mContext = context;
    }


    public static LocationUtil with(Context context) {
        return new Builder(context).build();
    }

    public static class Builder {
        private final Context context;

        public Builder(Context context) {
            this.context = context;
        }

        public LocationUtil build() {
            return new LocationUtil(context);
        }
    }


    public LocationControl location() {
        if (locationProvider == null)
            locationProvider = new LocationProvider(this);
        return location(locationProvider);
    }

    public LocationControl location(LocationProviderInterface provider) {
        return new LocationControl(this, provider);
    }

    public static class LocationControl {

        private final LocationUtil locationUtil;
        private LocationProviderInterface provider;
        private boolean isOneFix;
        private LocationConfig locationConfig;

        public LocationControl(LocationUtil locationUtil, LocationProviderInterface provider) {
            this.locationUtil = locationUtil;
            this.provider = provider;
        }

        public LocationControl oneFix(){
            isOneFix = true;
            return this;
        }

        public LocationControl setConfig(LocationConfig locationConfig){
            this.locationConfig = locationConfig;
            return this;
        }

        public void start(OnLocationUpdatedListener onLocationUpdatedListener) {
            provider.setOnLocationUpdateListener(onLocationUpdatedListener);
            provider.init(locationUtil.mContext, isOneFix, locationConfig);

        }

        public void stop() {
            provider.stop();
        }
    }

}
