package xicom.com.baselibrary;

import android.app.Activity;
import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
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
public enum LocationUtil implements
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {
    INSTANCE;

    protected static final String TAG = "location-updates-sample";
    GetLocationUpdates getLocationUpdates;
    private int priority = LocationRequest.PRIORITY_HIGH_ACCURACY;
    private long interval = 10000;
    private long fastestUpdateInterval =
            interval / 2;

    protected GoogleApiClient mGoogleApiClient;
    protected LocationRequest mLocationRequest;
    protected Location mCurrentLocation;
    private Context context;

    private LocationUtil() {
    }

    public static class LocationConfig {
        private long interval;
        private int priority;

        public LocationConfig setInterval(long interval) {
            this.interval = interval;
            return this;
        }

        public LocationConfig setPriority(int priority) {
            this.priority = priority;
            return this;
        }
    }

    public LocationUtil setConfig(LocationConfig locationConfig) {
        interval = locationConfig.interval;
        priority = locationConfig.priority;
        return this;
    }

    public LocationUtil init(Context locationActivity) {
        this.context = locationActivity;
        buildGoogleApiClient();
        return this;
    }

    protected synchronized void buildGoogleApiClient() {
        Log.i(TAG, "Building GoogleApiClient");
        mGoogleApiClient = new GoogleApiClient.Builder(context)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        createLocationRequest();
    }

    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(interval);
        mLocationRequest.setFastestInterval(fastestUpdateInterval);
        mLocationRequest.setPriority(priority);
    }


    public LocationUtil startLocationUpdates() {
        if (mGoogleApiClient.isConnected()) {
            createLocationRequest();
            LocationServices.FusedLocationApi.requestLocationUpdates(
                    mGoogleApiClient, mLocationRequest, this);
        } else {
            mGoogleApiClient.connect();
        }
        return this;
    }

    public void stopLocationUpdates() {
        if (mGoogleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
            mGoogleApiClient.disconnect();
        }
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

    @Override
    public void onConnected(Bundle connectionHint) {
        Log.i(TAG, "Connected to GoogleApiClient");

        if (mCurrentLocation == null) {
            mCurrentLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            if (mCurrentLocation != null) {
                Log.i(TAG, "mCurrentLocation::::" + mCurrentLocation.getLatitude());
                getLocationUpdates.getLocation(mCurrentLocation);
            }

        }
        startLocationUpdates();
    }

    /**
     * Callback that fires when the location changes.
     */
    @Override
    public void onLocationChanged(Location location) {
        mCurrentLocation = location;
        if (mCurrentLocation != null) {
            Log.i(TAG, "Location::::" + mCurrentLocation.getLatitude());
            getLocationUpdates.getLocation(mCurrentLocation);
        }
    }

    @Override
    public void onConnectionSuspended(int cause) {
        // The connection to Google Play services was lost for some reason. We call connect() to
        // attempt to re-establish the connection.
        Log.i(TAG, "Connection suspended");
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {
        // Refer to the javadoc for ConnectionResult to see what error codes might be returned in
        // onConnectionFailed.
        Log.i(TAG, "Connection failed: ConnectionResult.getErrorCode() = " + result.getErrorCode());
    }

    public interface GetLocationUpdates {
        void getLocation(Location location);
    }

    public void setOnLocationChangeInterface(GetLocationUpdates getLocationUpdates) {
        this.getLocationUpdates = getLocationUpdates;
    }
}
