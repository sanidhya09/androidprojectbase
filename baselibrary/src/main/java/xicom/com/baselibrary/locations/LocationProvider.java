package xicom.com.baselibrary.locations;

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

public class LocationProvider implements
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener, LocationProviderInterface {

    protected static final String TAG = "location-updates-sample";
    private OnLocationUpdatedListener mLocationUpdates;
    private int PRIORITY = LocationRequest.PRIORITY_HIGH_ACCURACY;
    private long INTERVAL = 10000;
    private long FASTESTUPDATEINTERVAL =
            INTERVAL / 2;

    protected GoogleApiClient mGoogleApiClient;
    protected LocationRequest mLocationRequest;
    protected Location mCurrentLocation;

    private final LocationUtil locationUtil;

    public LocationProvider(LocationUtil locationUtil) {
        this.locationUtil = locationUtil;
    }

    public LocationProvider setConfig(LocationConfig locationConfig) {
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
    public void onConnected(Bundle bundle) {
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
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.i(TAG, "Connection failed: ConnectionResult.getErrorCode() = " + connectionResult.getErrorCode());
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

    @Override
    public void init(Context context) {
        buildGoogleApiClient();
    }

    @Override
    public void start(OnLocationUpdatedListener listener) {
        this.mLocationUpdates = listener;
        if (mGoogleApiClient.isConnected()) {
            createLocationRequest();
            LocationServices.FusedLocationApi.requestLocationUpdates(
                    mGoogleApiClient, mLocationRequest, this);
        } else {
            mGoogleApiClient.connect();
        }
    }

    @Override
    public void stop() {
        if (mGoogleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
            mGoogleApiClient.disconnect();
        }
    }
}