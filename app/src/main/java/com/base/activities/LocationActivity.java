package com.base.activities;

import android.location.Address;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.base.R;

import xicom.com.baselibrary.locations.LocationUtil;

import com.base.core.BaseActivity;
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
        mLatitudeTextView = (TextView) findViewById(R.id.latitude_text);
        mLongitudeTextView = (TextView) findViewById(R.id.longitude_text);
        mLastUpdateTimeTextView = (TextView) findViewById(R.id.last_update_time_text);

        // Set labels.
        mLatitudeLabel = getResources().getString(R.string.latitude_label);
        mLongitudeLabel = getResources().getString(R.string.longitude_label);
        mLastUpdateTimeLabel = getResources().getString(R.string.last_update_time_label);

        mRequestingLocationUpdates = false;
        mLastUpdateTime = "";


    }

    public void startUpdatesButtonHandler(View view) {
        if (!mRequestingLocationUpdates) {
            mRequestingLocationUpdates = true;
            setButtonsEnabledState();

            LocationUtil.LocationConfig locationConfig = new LocationUtil.LocationConfig();
            locationConfig.setInterval(5000).setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

            LocationUtil.with(context).location().setConfig(locationConfig).startLocationUpdates(new LocationUtil.LocationControl.GetLocationUpdates() {
                @Override
                public void getLocation(Location location) {
                    mLatitudeTextView.setText(String.format("%s: %f", mLatitudeLabel,
                            location.getLatitude()));
                    mLongitudeTextView.setText(String.format("%s: %f", mLongitudeLabel,
                            location.getLongitude()));
                    List<Address> address = LocationUtil.with(context).location().getAddress(location.getLatitude(), location.getLongitude());
                    mLastUpdateTimeTextView.setText(address.get(0).getAddressLine(0));
                }
            });

        }
    }

    public void stopUpdatesButtonHandler(View view) {
        if (mRequestingLocationUpdates) {
            mRequestingLocationUpdates = false;
            setButtonsEnabledState();
            LocationUtil.with(context).location().stopLocationUpdates();
        }
    }


    private void setButtonsEnabledState() {
        if (mRequestingLocationUpdates) {
            mStartUpdatesButton.setEnabled(false);
            mStopUpdatesButton.setEnabled(true);
        } else {
            mStartUpdatesButton.setEnabled(true);
            mStopUpdatesButton.setEnabled(false);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
    }
//
//    @Override
//    public void getLocation(Location location) {
//        mLatitudeTextView.setText(String.format("%s: %f", mLatitudeLabel,
//                location.getLatitude()));
//        mLongitudeTextView.setText(String.format("%s: %f", mLongitudeLabel,
//                location.getLongitude()));
//    }
}