package com.zgas.tesselar.myzuite.Service;

import android.content.Context;
import android.util.Log;

import com.zgas.tesselar.myzuite.Controller.ConnectionController;
import com.zgas.tesselar.myzuite.Utilities.ExtrasHelper;
import com.zgas.tesselar.myzuite.Utilities.UrlHelper;
import com.zgas.tesselar.myzuite.Utilities.UserPreferences;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.Formatter;


public class GetNewTokenTask {

    public String DEBUG_TAG = getClass().getSimpleName();
    private UserPreferences userPreferences;

    public GetNewTokenTask(Context context) {
        userPreferences = new UserPreferences(context);
    }

    public void getNewToken() {
        try {
            Formatter formatter_admin = new Formatter();
            String format_admin = formatter_admin.format(UrlHelper.LOGIN_URL, UrlHelper.GRANT_TYPE, UrlHelper.CLIENT_ID, UrlHelper.CLIENT_SECRET, UrlHelper.ADMIN_EMAIL, UrlHelper.ADMIN_PASS).toString();
            URL url_admin = new URL(format_admin);
            ConnectionController connection_admin = new ConnectionController(null, url_admin, "POST");
            connection_admin.setRefreshing(true);
            JSONObject jsonObjectAdmin = connection_admin.execute();
            String adminToken = jsonObjectAdmin.get(ExtrasHelper.LOGIN_JSON_OBJECT_TOKEN).toString();
            Log.d(DEBUG_TAG, "Token del admin: " + adminToken);
            userPreferences.setAdminToken(adminToken);
            Log.d(DEBUG_TAG, "Token del admin userPreferences: " + userPreferences.getAdminToken());
        } catch (MalformedURLException | JSONException | SocketTimeoutException | FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
