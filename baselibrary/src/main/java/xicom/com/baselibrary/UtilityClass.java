package xicom.com.baselibrary;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.squareup.otto.Bus;
import com.squareup.otto.ThreadEnforcer;

import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by sanidhya on 14/1/15.
 */
public class UtilityClass {

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

    public boolean isStringNotNullAndNotEmpty(String str) {
        if (str == null || str.trim().length() <= 0) {
            return false;
        }

        return true;
    }

    public Bus busProvider() {
        return new Bus(ThreadEnforcer.ANY);
    }

    public void decompressZipFile(String appName, String filename, String password) {
        String filePath = Environment.getExternalStorageDirectory() + "/" + appName;

        try {
            File src = new File(filePath, filename);
            ZipFile zipFile = new ZipFile(src);

            if (zipFile.isEncrypted())
                zipFile.setPassword(password);

            zipFile.extractAll(filePath);

        } catch (ZipException e) {
            e.printStackTrace();
        }
    }

    public String getPath(Uri uri, Context context)
    {
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = context.getContentResolver().query(uri, projection, null, null, null);
        if (cursor == null) return null;
        int column_index =             cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String s=cursor.getString(column_index);
        cursor.close();
        return s;
    }
}
