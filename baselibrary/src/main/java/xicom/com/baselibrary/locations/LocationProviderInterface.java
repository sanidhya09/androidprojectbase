package xicom.com.baselibrary.locations;

import android.content.Context;

/**
 * Created by sanidhya on 26/7/16.
 */
public interface LocationProviderInterface {
    void init(Context context, boolean isOneFix, LocationConfig locationConfig);

    void setOnLocationUpdateListener(OnLocationUpdatedListener listener);

    void stop();
}
