package xicom.com.baselibrary.locations;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
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

    protected static GoogleApiClient mGoogleApiClient;
    protected LocationRequest mLocationRequest;
    protected Location mCurrentLocation;

    private final LocationUtil locationUtil;
    private boolean isOneFix;

    public LocationProvider(LocationUtil locationUtil) {
        this.locationUtil = locationUtil;
    }

    protected synchronized void buildGoogleApiClient() {
        Log.i(TAG, "Building GoogleApiClient");
        if (mGoogleApiClient == null)
            mGoogleApiClient = new GoogleApiClient.Builder(locationUtil.mContext)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        createLocationRequest();
        if (mGoogleApiClient != null && !mGoogleApiClient.isConnected())
            mGoogleApiClient.connect();
    }

    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(INTERVAL);
        mLocationRequest.setFastestInterval(FASTESTUPDATEINTERVAL);
        mLocationRequest.setPriority(PRIORITY);
        if (isOneFix)
            mLocationRequest.setNumUpdates(1);
    }

    @Override
    public void onConnected(Bundle bundle) {
        Log.i(TAG, "Connected to GoogleApiClient");
        if ((ContextCompat.checkSelfPermission(locationUtil.mContext,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED)|| ContextCompat.checkSelfPermission(locationUtil.mContext,
                Manifest.permission.ACCESS_COARSE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            PendingResult<Status> statusPendingResult = LocationServices.FusedLocationApi.requestLocationUpdates(
                    mGoogleApiClient, mLocationRequest, this);
            statusPendingResult.setResultCallback(new ResultCallback<Status>() {
                @Override
                public void onResult(@NonNull Status status) {
                    if (status.isSuccess()) {

                    } else {
                        if (mCurrentLocation == null) {
                            mCurrentLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
                            if (mCurrentLocation != null) {
                                Log.i(TAG, "mCurrentLocation::::" + mCurrentLocation.getLatitude());
                                mLocationUpdates.getLocation(mCurrentLocation);
                                if (isOneFix) {
                                    stop();
                                }
                            }
                        }
                    }
                }
            });
        }else{
            Toast.makeText(locationUtil.mContext, "Check for Location based permissions", Toast.LENGTH_SHORT).show();
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
            if (isOneFix) {
                stop();
            }
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
    public void init(Context context, boolean isOneFix, LocationConfig locationConfig) {
        this.isOneFix = isOneFix;
        if (locationConfig != null) {
            INTERVAL = locationConfig.interval;
            PRIORITY = locationConfig.priority;
        }

        buildGoogleApiClient();
    }

    @Override
    public void setOnLocationUpdateListener(OnLocationUpdatedListener listener) {
        this.mLocationUpdates = listener;
//        if (mGoogleApiClient.isConnected()) {
//            LocationServices.FusedLocationApi.requestLocationUpdates(
//                    mGoogleApiClient, mLocationRequest, this);
//        }
    }

    @Override
    public void stop() {
        if (mGoogleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
            mGoogleApiClient.disconnect();
        }
    }
}