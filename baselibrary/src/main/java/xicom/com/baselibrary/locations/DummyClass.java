package xicom.com.baselibrary.locations;

import android.content.Context;

/**
 * Created by sanidhya on 26/7/16.
 */
public class DummyClass implements LocationProviderInterface {
    @Override
    public void init(Context context, boolean isOneFix, LocationConfig locationConfig) {

    }

    @Override
    public void setOnLocationUpdateListener(OnLocationUpdatedListener listener) {

    }

    @Override
    public void stop() {

    }
}
