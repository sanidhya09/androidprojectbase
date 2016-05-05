package com.base.location;

import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.annotation.UiThread;

import com.base.R;

public class LocationUtils {
    public LocationUtils() {
    }

    public static boolean isGpsProviderEnabled(@NonNull Context context) {
        return isProviderEnabled(context, "gps");
    }

    public static boolean isNetworkProviderEnabled(@NonNull Context context) {
        return isProviderEnabled(context, "network");
    }

    public static boolean isPassiveProviderEnabled(@NonNull Context context) {
        return isProviderEnabled(context, "passive");
    }

    @UiThread
    public static void askEnableProviders(@NonNull Context context) {
        askEnableProviders(context, R.string.location_msg);
    }

    @UiThread
    public static void askEnableProviders(@NonNull Context context, @StringRes int messageResource) {
        askEnableProviders(context, messageResource, R.string.location_yes, R.string.location_no);
    }

    @UiThread
    public static void askEnableProviders(@NonNull final Context context, @StringRes int messageResource, @StringRes int positiveLabelResource, @StringRes int negativeLabelResource) {
        (new Builder(context)).setMessage(messageResource).setCancelable(false).setPositiveButton(positiveLabelResource, new OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                context.startActivity(new Intent("android.settings.LOCATION_SOURCE_SETTINGS"));
            }
        }).setNegativeButton(negativeLabelResource, new OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }).show();
    }

    private static boolean isProviderEnabled(@NonNull Context context, @NonNull String provider) {
        LocationManager manager = (LocationManager)context.getSystemService("location");
        return manager.isProviderEnabled(provider);
    }
}
