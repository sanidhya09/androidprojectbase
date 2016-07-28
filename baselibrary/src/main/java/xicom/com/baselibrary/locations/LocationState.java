package xicom.com.baselibrary.locations;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationManager;
import android.os.Build;
import android.provider.Settings;
import android.text.TextUtils;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 * Created by sanidhya on 24/07/16.
 */
public class LocationState {

    private static LocationState instance;
    private Context context;
    private LocationManager locationManager;

    private LocationState(Context context) {
        this.context = context;
        this.locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
    }

    public static LocationState with(Context context) {
        if (instance == null) {
            instance = new LocationState(context.getApplicationContext());
        }
        return instance;
    }

    public boolean locationServicesEnabled() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            int locationMode = Settings.Secure.LOCATION_MODE_OFF;

            try {
                locationMode = Settings.Secure.getInt(context.getContentResolver(), Settings.Secure.LOCATION_MODE);

            } catch (Settings.SettingNotFoundException ignored) {
                // This is ignored
            }

            return locationMode != Settings.Secure.LOCATION_MODE_OFF;

        } else {
            String locationProviders = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
            return !TextUtils.isEmpty(locationProviders);
        }
    }

    public boolean isAnyProviderAvailable() {
        return isGpsAvailable() || isNetworkAvailable();
    }

    public boolean isGpsAvailable() {
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    public boolean isNetworkAvailable() {
        return locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    public boolean isPassiveAvailable() {
        return locationManager.isProviderEnabled(LocationManager.PASSIVE_PROVIDER);
    }

    public boolean isMockSettingEnabled() {
        return !("0".equals(Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ALLOW_MOCK_LOCATION)));
    }

    public List<Address> getAddress(double latitude, double longitude) {
        Geocoder geocoder;
        geocoder = new Geocoder(context, Locale.getDefault());

        try {
            return geocoder.getFromLocation(latitude, longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}