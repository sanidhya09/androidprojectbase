package xicom.com.baselibrary.locations;

import android.content.Context;


/**
 * Created by sanidhya on 20/7/16.
 */
public class LocationUtil {
    public Context mContext;

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
        return location(new LocationProvider(this));
    }

    public LocationControl location(LocationProviderInterface provider) {
        return new LocationControl(this, provider);
    }

    public static class LocationControl {

        private final LocationUtil locationUtil;
        private LocationProviderInterface provider;

        public LocationControl(LocationUtil locationUtil, LocationProviderInterface provider) {
            this.locationUtil = locationUtil;
            this.provider = provider;
        }

        public void connect() {
            provider.init(locationUtil.mContext);
        }

        public void start(OnLocationUpdatedListener onLocationUpdatedListener){
            provider.start(onLocationUpdatedListener);
        }

        public void stop(){
            provider.stop();
        }
    }

}
