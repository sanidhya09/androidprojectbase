package xicom.com.baselibrary.geofencing;

import android.app.Activity;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingEvent;
import com.google.android.gms.location.LocationServices;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import xicom.com.baselibrary.geofencing.providers.GeofencingProvider;
import xicom.com.baselibrary.geofencing.providers.GeofencingStore;
import xicom.com.baselibrary.geofencing.providers.GooglePlayServicesListener;
import xicom.com.baselibrary.geofencing.providers.OnGeofencingTransitionListener;
import xicom.com.baselibrary.geofencing.providers.TransitionGeofence;


/**
 * Created by sanidhya on 3/1/15.
 */
public class GeofencingGooglePlayServicesProvider implements GeofencingProvider, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, ResultCallback<Status> {

    public static final int RESULT_CODE = 10003;

    public static final String BROADCAST_INTENT_ACTION = GeofencingGooglePlayServicesProvider.class.getCanonicalName() + ".GEOFENCE_TRANSITION";
    public static final String GEOFENCES_EXTRA_ID = "geofences";
    public static final String TRANSITION_EXTRA_ID = "transition";
    public static final String LOCATION_EXTRA_ID = "location";

    private final List<Geofence> geofencesToAdd = Collections.synchronizedList(new ArrayList<Geofence>());
    private final List<String> geofencesToRemove = Collections.synchronizedList(new ArrayList<String>());

    private GoogleApiClient client;
    private OnGeofencingTransitionListener listener;
    private GeofencingStore geofencingStore;
    private Context context;
    private PendingIntent pendingIntent;
    private boolean stopped = false;
    private final GooglePlayServicesListener googlePlayServicesListener;


    public GeofencingGooglePlayServicesProvider() {
        this(null);
    }

    public GeofencingGooglePlayServicesProvider(GooglePlayServicesListener playServicesListener) {
        googlePlayServicesListener = playServicesListener;
    }


    @Override
    public void init(@NonNull Context context) {
        this.context = context;

        geofencingStore = new GeofencingStore(context);

        this.client = new GoogleApiClient.Builder(context)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();

        client.connect();

        pendingIntent = PendingIntent.getService(context, 0, new Intent(context, GeofencingService.class),
                PendingIntent.FLAG_UPDATE_CURRENT);
    }

    @Override
    public void addGeofence(GeofenceModel geofence) {
        List<GeofenceModel> wrapperList = new ArrayList<>();
        wrapperList.add(geofence);
        addGeofences(wrapperList);
    }

    @Override
    public void addGeofences(List<GeofenceModel> geofenceList) {
        List<Geofence> convertedGeofences = new ArrayList<>();
        for (GeofenceModel geofenceModel : geofenceList) {
            geofencingStore.put(geofenceModel.getRequestId(), geofenceModel);
            convertedGeofences.add(geofenceModel.toGeofence());
        }

        if (client.isConnected()) {
            if (geofencesToAdd.size() > 0) {
                convertedGeofences.addAll(geofencesToAdd);
                geofencesToAdd.clear();
            }
            LocationServices.GeofencingApi.addGeofences(client, convertedGeofences, pendingIntent);

        } else {
            for (GeofenceModel geofenceModel : geofenceList) {
                geofencesToAdd.add(geofenceModel.toGeofence());
            }
        }

    }

    @Override
    public void removeGeofence(String geofenceId) {
        List<String> wrapperList = new ArrayList<>();
        wrapperList.add(geofenceId);
        removeGeofences(wrapperList);
    }

    @Override
    public void removeGeofences(List<String> geofenceIds) {
        for (String id : geofenceIds) {
            geofencingStore.remove(id);
        }

        if (client.isConnected()) {
            if (geofencesToRemove.size() > 0) {
                geofenceIds.addAll(geofencesToRemove);
                geofencesToRemove.clear();
            }
            LocationServices.GeofencingApi.removeGeofences(client, geofenceIds);
        } else {
            geofencesToRemove.addAll(geofenceIds);
        }

    }

    @Override
    public void start(OnGeofencingTransitionListener listener) {
        this.listener = listener;

        IntentFilter intentFilter = new IntentFilter(BROADCAST_INTENT_ACTION);
        context.registerReceiver(geofencingReceiver, intentFilter);

        if (!client.isConnected()) {
            Log.d("1", "still not connected - scheduled setOnLocationUpdateListener when connection is ok");
        } else if (stopped) {
            client.connect();
            stopped = false;
        }
    }

    @Override
    public void stop() {
        Log.d("1","stop");
        if (client.isConnected()) {
            client.disconnect();
        }
        try {
            context.unregisterReceiver(geofencingReceiver);
        } catch (IllegalArgumentException e) {
            Log.d("1","Silenced 'receiver not registered' stuff (calling stop more times than necessary did this)");
        }
        stopped = true;
    }

    @Override
    public void onConnected(Bundle bundle) {
        Log.d("1","onConnected");

        // TODO wait until the connection is done and retry
        if (client.isConnected()) {
            if (geofencesToAdd.size() > 0) {
                LocationServices.GeofencingApi.addGeofences(client, geofencesToAdd, pendingIntent);
                geofencesToAdd.clear();
            }

            if (geofencesToRemove.size() > 0) {
                LocationServices.GeofencingApi.removeGeofences(client, geofencesToRemove);
                geofencesToRemove.clear();
            }
        }
        if (googlePlayServicesListener != null) {
            googlePlayServicesListener.onConnected(bundle);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.d("1","onConnectionSuspended " + i);
        if (googlePlayServicesListener != null) {
            googlePlayServicesListener.onConnectionSuspended(i);
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d("1","onConnectionFailed");
        if (googlePlayServicesListener != null) {
            googlePlayServicesListener.onConnectionFailed(connectionResult);
        }

    }

    private BroadcastReceiver geofencingReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (BROADCAST_INTENT_ACTION.equals(intent.getAction()) && intent.hasExtra(GEOFENCES_EXTRA_ID)) {
                Log.d("1","Received geofencing event");
                final int transitionType = intent.getIntExtra(TRANSITION_EXTRA_ID, -1);
                final List<String> geofencingIds = intent.getStringArrayListExtra(GEOFENCES_EXTRA_ID);
                for (final String geofenceId : geofencingIds) {
                    // Get GeofenceModel
                    GeofenceModel geofenceModel = geofencingStore.get(geofenceId);
                    if (geofenceModel != null) {
                        listener.onGeofenceTransition(new TransitionGeofence(geofenceModel, transitionType));
                    } else {
                        Log.w("1","Tried to retrieve geofence " + geofenceId + " but it was not in the store");
                    }

                }
            }
        }
    };

    public static class GeofencingService extends IntentService {

        public GeofencingService() {
            super(GeofencingService.class.getSimpleName());
        }

        @Override
        protected void onHandleIntent(Intent intent) {
            GeofencingEvent geofencingEvent = GeofencingEvent.fromIntent(intent);
            if (geofencingEvent != null && !geofencingEvent.hasError()) {
                int transition = geofencingEvent.getGeofenceTransition();

                // Broadcast an intent containing the geofencing info
                Intent geofenceIntent = new Intent(BROADCAST_INTENT_ACTION);
                geofenceIntent.putExtra(TRANSITION_EXTRA_ID, transition);
                geofenceIntent.putExtra(LOCATION_EXTRA_ID, geofencingEvent.getTriggeringLocation());
                ArrayList<String> geofencingIds = new ArrayList<>();
                for (Geofence geofence : geofencingEvent.getTriggeringGeofences()) {
                    geofencingIds.add(geofence.getRequestId());
                }
                geofenceIntent.putStringArrayListExtra(GEOFENCES_EXTRA_ID, geofencingIds);
                sendBroadcast(geofenceIntent);
            }
        }
    }

    @Override
    public void onResult(@NonNull Status status) {
        if (status.isSuccess()) {
            Log.d("1","Geofencing update request successful");
        } else if (status.hasResolution() && context instanceof Activity) {
            Log.w("1",
                    "Unable to register, but we can solve this - will startActivityForResult expecting result code " + RESULT_CODE + " (if received, please try again)");
            try {
                status.startResolutionForResult((Activity) context, RESULT_CODE);
            } catch (IntentSender.SendIntentException e) {
            }
        } else {
            // No recovery. Weep softly or inform the user.
            Log.e("1","Registering failed: " + status.getStatusMessage());
        }
    }

}