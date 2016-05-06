package com.base.utils;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by sanidhya on 14/1/15.
 */
public class UtilitySingleton {

    private Context context;

    public void setContext(Context context) {
        this.context = context;
    }

    /**
     * Description : Check if user is online or not
     *
     * @return
     */
    public boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        }
        //ShowToast(context.getString(R.string.toast_network_not_available));
        return false;
    }

    /**
     * Description : Toast with Message String as input
     */
    public void ShowToast(String msg) {

        if (msg != null && !msg.trim().equalsIgnoreCase("") && !msg.equalsIgnoreCase("")) {
                Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
        }


    }

    /**
     * Description : Toast with resourceID as input
     */
    public void ShowToast(int msgID) {
        ShowToast(context.getString(msgID));
    }

    /**
     * Description : Hide the soft keyboard
     *
     * @param view : Pass the current view
     */
    public void hideSoftKeyboard(View view) {
        if (view != null && view.getWindowToken() != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }

    }

    /**
     * Description : Validates the email
     *
     * @param editText
     * @return true : if email is valid
     */
    public boolean validateEmail(EditText editText) {
        Pattern pattern;
        Matcher matcher;
        String EMAIL_PATTERN = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        pattern = Pattern.compile(EMAIL_PATTERN);
        matcher = pattern.matcher(editText.getText().toString());
        if (matcher.matches()) {
            return true;
        } else {
            ShowToast("Invalid Email");
            return false;
        }
    }
    /**
     * Method to verify google play services on the device
     */
    public boolean checkPlayServices(Activity context, int PLAY_SERVICES_RESOLUTION_REQUEST) {
        GoogleApiAvailability googleAPI = GoogleApiAvailability.getInstance();
        int result = googleAPI.isGooglePlayServicesAvailable(context);
        if (result != ConnectionResult.SUCCESS) {
            if (googleAPI.isUserResolvableError(result)) {
                googleAPI.getErrorDialog(context, result,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            }

            return false;
        }

        return true;
    }
}
