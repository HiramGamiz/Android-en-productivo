package com.zgas.tesselar.myzuite.Service;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.zgas.tesselar.myzuite.Controller.ConnectionController;
import com.zgas.tesselar.myzuite.Model.Login;
import com.zgas.tesselar.myzuite.R;
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

/**
 * Class that communicates with the service and will push the result to the User model list.
 *
 * @author jarvizu on 19/09/2017.
 * @version 2018.0.9
 * @see AsyncTask
 * @see Login
 * @see JSONObject
 * @see UserPreferences
 * @see LoginTaskListener
 */
public class LoginTask extends AsyncTask<URL, JSONObject, JSONObject> {

    private final String DEBUG_TAG = getClass().getSimpleName();
    private static final String METHOD = "POST";
    private static final String JSON_OBJECT_ERROR = "error";

    private Context context;
    private JSONObject params;
    private LoginTaskListener loginTaskListener;
    private UserPreferences userPreferences;
    private boolean isError = false;
    private ProgressDialog progressDialog;

    /**
     * Constructor for the LoginTask. Additionally, we have an UserPreferences class reference
     * so we can obtain the user data.
     *
     * @param context Current context of the application
     * @param params  Parameters that will be sent to the service.
     */
    public LoginTask(Context context, JSONObject params) {
        this.context = context;
        this.params = params;
        userPreferences = new UserPreferences(context);
    }

    /**
     * progress dialog to show user that the backup is processing.
     */
    @Override
    protected void onPreExecute() {
        progressDialog = ProgressDialog.show(context, null, context.getResources().getString(R.string.wait_message), false);
    }


    /**
     * This methods performs the connection between our URL and our service, passing the method we'll
     * use and the params needed (if needed).
     * In this case, we make 2 different logins: the user login (the user that will be using the app)
     * and the admin login (so we can get the Admin Token needed for all the other requests within
     * the app).
     * This because all the requests made with Salesforce need an access token that we only get after
     * the Salesforce admin logs in. We take this admin token and we store it on the UserPreferences
     * object.
     *
     * @param urls
     * @return JsonObject containing the connection.
     */
    @Override
    protected JSONObject doInBackground(URL... urls) {
        JSONObject jsonObject = null;

        try {
            Formatter formatter_admin = new Formatter();
            String format_admin = formatter_admin.format(UrlHelper.LOGIN_URL, UrlHelper.GRANT_TYPE, UrlHelper.CLIENT_ID, UrlHelper.CLIENT_SECRET, UrlHelper.ADMIN_EMAIL, UrlHelper.ADMIN_PASS).toString();
            Log.d(DEBUG_TAG, format_admin);
            URL url_admin = new URL(format_admin);
            ConnectionController connection_admin = new ConnectionController(null, url_admin, METHOD, null, context);

            JSONObject jsonObjectAdmin = connection_admin.execute();
            String adminToken = jsonObjectAdmin.get(ExtrasHelper.LOGIN_JSON_OBJECT_TOKEN).toString();
            Log.d(DEBUG_TAG, "Token del admin: " + adminToken);
            userPreferences.setAdminToken(adminToken);
            Log.d(DEBUG_TAG, "Token del admin userPreferences: " + userPreferences.getAdminToken());

            Formatter formatter = new Formatter();
            String format = formatter.format(UrlHelper.LOGIN_URL, UrlHelper.GRANT_TYPE, UrlHelper.CLIENT_ID, UrlHelper.CLIENT_SECRET, params.get(ExtrasHelper.LOGIN_JSON_OBJECT_EMAIL), params.get(ExtrasHelper.LOGIN_JSON_OBJECT_PASSWORD)).toString();
            Log.d(DEBUG_TAG, format);

            URL url = new URL(format);
            ConnectionController connection = new ConnectionController(null, url, METHOD, null, context);
            jsonObject = connection.execute();

            if (jsonObject == null) {
                cancel(true);
            }

        } catch (MalformedURLException | SocketTimeoutException e) {
            e.printStackTrace();
            cancel(true);
        } catch (FileNotFoundException e) {
            cancel(true);
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    /**
     * Method that will show the task result on the user interface. It will receive the jsonObject
     * obtained on doInBackground method, and it will check if the jsonObject has an error or is
     * correct.
     * If an error occurs, the LoginTaskListener will manage it.
     * Else, the json data will be mapped with our User object and it will be shown on the user
     * interface.
     *
     * @param jsonObject The user object that will be received.
     */
    @Override
    protected void onPostExecute(JSONObject jsonObject) {
        super.onPostExecute(jsonObject);
        progressDialog.dismiss();
        Login login = null;

        try {
            if (jsonObject == null) {
                Log.d(DEBUG_TAG, "Perro 11111111");
                loginTaskListener.loginErrorResponse(context.getResources().getString(R.string.login_error));
                isError = true;
            } else if (jsonObject.has(JSON_OBJECT_ERROR)) {
                Log.d(DEBUG_TAG, "Perro 22222222222");
                Log.d(DEBUG_TAG, "Error: " + jsonObject.get(JSON_OBJECT_ERROR).toString());
                if (jsonObject.get(JSON_OBJECT_ERROR).toString().equals("400")) {
                    loginTaskListener.loginErrorResponse(context.getResources().getString(R.string.login_error));
                    isError = true;
                }
            } else if (jsonObject.has(ExtrasHelper.LOGIN_JSON_OBJECT_TOKEN)) {
                login = new Login();
                login.setLoginEmail(params.get(ExtrasHelper.LOGIN_JSON_OBJECT_EMAIL).toString());
                login.setLoginAccessToken(jsonObject.get(ExtrasHelper.LOGIN_JSON_OBJECT_TOKEN).toString());
                login.setLoginId(jsonObject.get(ExtrasHelper.LOGIN_JSON_OBJECT_ID).toString());
                login.setLoginInstanceUrl(jsonObject.get(ExtrasHelper.LOGIN_JSON_OBJECT_INSTANCE).toString());
                login.setLoginIssuedAt(jsonObject.get(ExtrasHelper.LOGIN_JSON_OBJECT_ISSUED_AT).toString());
                login.setLoginTokenType(jsonObject.get(ExtrasHelper.LOGIN_JSON_OBJECT_TOKEN_TYPE).toString());
                login.setLoginSignature(jsonObject.get(ExtrasHelper.LOGIN_JSON_OBJECT_SIGNATURE).toString());
                isError = false;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } finally {
            if (loginTaskListener != null) {
                loginTaskListener.loginSuccessResponse(login);
            }
        }


    }

    /**
     * If the AsyncTask is cancelled, it will show an error response.
     */
    @Override
    protected void onCancelled() {
        super.onCancelled();
        loginTaskListener.loginErrorResponse(context.getResources().getString(R.string.connection_error));
    }

    public void setLoginTaskListener(LoginTaskListener loginTaskListener) {
        this.loginTaskListener = loginTaskListener;
    }

    /**
     * Interface for managing the different outputs of the AsyncTask
     */
    public interface LoginTaskListener {
        void loginErrorResponse(String error);

        void loginSuccessResponse(Login login);
    }
}
