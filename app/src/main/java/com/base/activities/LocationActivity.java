package com.base.activities;

import android.location.Address;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.base.R;

import xicom.com.baselibrary.geofencing.Constants;
import xicom.com.baselibrary.geofencing.GeoFenceUtil;
import xicom.com.baselibrary.geofencing.GeofenceModel;
import xicom.com.baselibrary.geofencing.OnGeofenceStatusListener;
import xicom.com.baselibrary.locations.LocationConfig;
import xicom.com.baselibrary.locations.LocationUtil;
import xicom.com.baselibrary.locations.OnLocationUpdatedListener;

import com.base.core.BaseActivity;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.LocationRequest;

import java.util.List;

public class LocationActivity extends BaseActivity {

    protected static final String TAG = "location-updates-sample";

    // UI Widgets.
    protected Button mStartUpdatesButton;
    protected Button mStopUpdatesButton;
    protected TextView mLastUpdateTimeTextView;
    protected TextView mLatitudeTextView;
    protected TextView mLongitudeTextView;

    // Labels.
    protected String mLatitudeLabel;
    protected String mLongitudeLabel;
    protected String mLastUpdateTimeLabel;
    protected Boolean mRequestingLocationUpdates;
    protected String mLastUpdateTime;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.location_activity);

        // Locate the UI widgets.
        mStartUpdatesButton = (Button) findViewById(R.id.start_updates_button);
        mStopUpdatesButton = (Button) findViewById(R.id.stop_updates_button);
        mStopUpdatesButton.setEnabled(true);
        mLatitudeTextView = (TextView) findViewById(R.id.latitude_text);
        mLongitudeTextView = (TextView) findViewById(R.id.longitude_text);
        mLastUpdateTimeTextView = (TextView) findViewById(R.id.last_update_time_text);

        // Set labels.
        mLatitudeLabel = getResources().getString(R.string.latitude_label);
        mLongitudeLabel = getResources().getString(R.string.longitude_label);
        mLastUpdateTimeLabel = getResources().getString(R.string.last_update_time_label);

        mRequestingLocationUpdates = false;
        mLastUpdateTime = "";

        GeofenceModel janakpuri = new GeofenceModel.Builder("janakpuri")
                .setTransition(Geofence.GEOFENCE_TRANSITION_ENTER | Geofence.GEOFENCE_TRANSITION_EXIT)
                .setLatitude(28.621127)
                .setLongitude(77.081824)
                .setRadius(2000)
                .setExpiration(Constants.GEOFENCE_EXPIRATION_IN_MILLISECONDS)
                .build();

        GeoFenceUtil.INSTANCE.add(janakpuri).init(this).setOnGeoFenceStatusListener(new OnGeofenceStatusListener() {
            @Override
            public void onStatus(String id, int transitionType, Location location) {
                Log.i(TAG, id + " " + transitionType);
                mLastUpdateTimeTextView.setText("Geofence = " + transitionType);
            }
        });

        // GeoFenceUtil.INSTANCE.removeGeofence();
    }

    public void startUpdatesButtonHandler(View view) {
        setButtonsEnabledState();

        LocationConfig locationConfig = new LocationConfig();
        locationConfig.setInterval(5000).setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        LocationUtil.with(context).location().setConfig(locationConfig).start(new OnLocationUpdatedListener() {
            @Override
            public void getLocation(Location location) {
                mLatitudeTextView.setText(String.format("%s: %f", mLatitudeLabel,
                        location.getLatitude()));
                mLongitudeTextView.setText(String.format("%s: %f", mLongitudeLabel,
                        location.getLongitude()));
                List<Address> address = LocationUtil.with(context).location().state().getAddress(location.getLatitude(), location.getLongitude());
                mLastUpdateTimeTextView.setText(address.get(0).getAddressLine(0));
            }
        });
    }

    public void stopUpdatesButtonHandler(View view) {
        setButtonsEnabledState();
        LocationUtil.with(context).location().stop();
    }


    private void setButtonsEnabledState() {
//        if (mRequestingLocationUpdates) {
//            mStartUpdatesButton.setEnabled(false);
//            mStopUpdatesButton.setEnabled(true);
//        } else {
//            mStartUpdatesButton.setEnabled(true);
//            mStopUpdatesButton.setEnabled(false);
//        }
    }

//
//    @Override
//    public void getLocation(Location location) {
//        mLatitudeTextView.setText(String.format("%s: %f", mLatitudeLabel,
//                location.getLatitude()));
//        mLongitudeTextView.setText(String.format("%s: %f", mLongitudeLabel,
//                location.getLongitude()));
//    }


    @Override
    protected void onStop() {
        super.onStop();
        GeoFenceUtil.INSTANCE.removeGeofence();
    }
}