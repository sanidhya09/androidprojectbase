package xicom.com.baselibrary.locations;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 * Created by sanidhya on 20/7/16.
 */
public class LocationUtil {
    private Context mContext;

    private LocationUtil(Context context) {
        mContext = context;
    }

    public static LocationUtil with(Context context) {
        return new Builder(context).build();
    }

    public static class Builder {
        private final Context context;

        public Builder(@NonNull Context context) {
            this.context = context;
        }

        public LocationUtil build() {
            return new LocationUtil(context);
        }

    }

    public static class LocationConfig {
        private long interval;
        private int priority;

        public LocationConfig setInterval(@NonNull long interval) {
            this.interval = interval;
            return this;
        }

        public LocationConfig setPriority(@NonNull int priority) {
            this.priority = priority;
            return this;
        }
    }

    public LocationControl location() {
        return new LocationControl(this);
    }

    public static class LocationControl implements
            GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

        protected static final String TAG = "location-updates-sample";
        private GetLocationUpdates mLocationUpdates;
        private int PRIORITY = LocationRequest.PRIORITY_HIGH_ACCURACY;
        private long INTERVAL = 10000;
        private long FASTESTUPDATEINTERVAL =
                INTERVAL / 2;

        protected GoogleApiClient mGoogleApiClient;
        protected LocationRequest mLocationRequest;
        protected Location mCurrentLocation;

        private final LocationUtil locationUtil;

        public LocationControl(@NonNull LocationUtil locationUtil) {
            this.locationUtil = locationUtil;
            init();
        }

        public void init() {
            buildGoogleApiClient();
        }

        public LocationControl setConfig(@NonNull LocationConfig locationConfig) {
            INTERVAL = locationConfig.interval;
            PRIORITY = locationConfig.priority;
            return this;
        }

        protected synchronized void buildGoogleApiClient() {
            Log.i(TAG, "Building GoogleApiClient");
            mGoogleApiClient = new GoogleApiClient.Builder(locationUtil.mContext)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
            createLocationRequest();
        }

        protected void createLocationRequest() {
            mLocationRequest = new LocationRequest();
            mLocationRequest.setInterval(INTERVAL);
            mLocationRequest.setFastestInterval(FASTESTUPDATEINTERVAL);
            mLocationRequest.setPriority(PRIORITY);
        }

        @Override
        public void onConnected(@Nullable Bundle bundle) {
            Log.i(TAG, "Connected to GoogleApiClient");

            if (mCurrentLocation == null) {
                mCurrentLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
                if (mCurrentLocation != null) {
                    Log.i(TAG, "mCurrentLocation::::" + mCurrentLocation.getLatitude());
                    mLocationUpdates.getLocation(mCurrentLocation);
                }
            }
        }

        @Override
        public void onConnectionSuspended(int i) {
            // The connection to Google Play services was lost for some reason. We call connect() to
            // attempt to re-establish the connection.
            Log.i(TAG, "Connection suspended");
            mGoogleApiClient.connect();
        }

        @Override
        public void onLocationChanged(Location location) {
            mCurrentLocation = location;
            if (mCurrentLocation != null) {
                Log.i(TAG, "Location::::" + mCurrentLocation.getLatitude());
                mLocationUpdates.getLocation(mCurrentLocation);
            }
        }

        @Override
        public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
            Log.i(TAG, "Connection failed: ConnectionResult.getErrorCode() = " + connectionResult.getErrorCode());
        }

        public void startLocationUpdates(GetLocationUpdates getLocationUpdates) {
            this.mLocationUpdates = getLocationUpdates;
            if (mGoogleApiClient.isConnected()) {
                createLocationRequest();
                LocationServices.FusedLocationApi.requestLocationUpdates(
                        mGoogleApiClient, mLocationRequest, this);
            } else {
                mGoogleApiClient.connect();
            }
        }

        public void stopLocationUpdates() {
            if (mGoogleApiClient.isConnected()) {
                LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
                mGoogleApiClient.disconnect();
            }
        }

        public List<Address> getAddress(double latitude, double longitude) {
            Geocoder geocoder;
            geocoder = new Geocoder(locationUtil.mContext, Locale.getDefault());

            try {
                return geocoder.getFromLocation(latitude, longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

        public interface GetLocationUpdates {
            void getLocation(Location location);
        }

        public LocationState state() {
            return LocationState.with(locationUtil.mContext);
        }
    }

}
