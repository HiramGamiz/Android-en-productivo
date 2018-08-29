package com.zgas.tesselar.myzuite.Service;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.zgas.tesselar.myzuite.Controller.ConnectionController;
import com.zgas.tesselar.myzuite.Model.Leak;
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
 * @author jarvizu on 27/01/2018.
 * @version 2018.0.9
 * @see AsyncTask
 * @see Leak
 * @see JSONObject
 * @see UserPreferences
 * @see StatusLeakTaskListener
 */
public class PutStatusLeakTask extends AsyncTask<URL, JSONObject, JSONObject> {

    private final String DEBUG_TAG = getClass().getSimpleName();
    private static final String METHOD = "PUT";
    private static final String JSON_OBJECT_ERROR = "StatusCode";

    private Context context;
    private StatusLeakTaskListener statusLeakTaskListener;
    private JSONObject params;
    private UserPreferences userPreferences;
    private boolean isError = false;
    private ProgressDialog progressDialog;

    /**
     * Constructor for the PutStatusLeakTask. Additionally, we have an UserPreferences class reference
     * so we can obtain the user data.
     *
     * @param context Current context of the application
     * @param params  Parameters that will be sent to the service.
     */
    public PutStatusLeakTask(Context context, JSONObject params) {
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
     *
     * @param urls
     * @return JsonObject containing the connection.
     */
    @Override
    protected JSONObject doInBackground(URL... urls) {
        JSONObject jsonObject = null;
        try {
            String adminToken = userPreferences.getAdminToken();
            Formatter formatter = new Formatter();
            String format = formatter.format(UrlHelper.PUT_LEAK_STATUS_URL, params.get(ExtrasHelper.LEAK_JSON_OBJECT_ID)).toString();
            Log.d(DEBUG_TAG, format);
            URL url = new URL(format);
            ConnectionController connectionController = new ConnectionController(adminToken, url, METHOD, params, context);
            Log.d(DEBUG_TAG, METHOD);
            jsonObject = connectionController.execute();

            if (jsonObject == null) {
                cancel(true);
            }

        } catch (MalformedURLException | FileNotFoundException | JSONException | SocketTimeoutException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    /**
     * Method that will show the task result on the user interface. It will receive the jsonObject
     * obtained on doInBackground method, and it will check if the jsonObject has an error or is
     * correct.
     * If an error occurs, the StatusLeakListener will manage it.
     * Else, the json data will be mapped with our Leak object and a pop up will be shown
     * on the user interface.
     *
     * @param jsonObject The user object that will be received.
     */
    @Override
    protected void onPostExecute(JSONObject jsonObject) {
        super.onPostExecute(jsonObject);
        progressDialog.dismiss();
        Log.d(DEBUG_TAG, jsonObject.toString());
        Leak leak = new Leak();
        try {
            try{
                if(jsonObject.get("error").toString()!=null){
                    String Aux=jsonObject.get("error").toString()+" Message: "+jsonObject.get("message").toString();
                    Log.d(DEBUG_TAG, Aux);
                    statusLeakTaskListener.statusErrorResponse(Aux);
                    isError = true;
                }
            }catch (Exception e ){}
            if (jsonObject == null) {
                statusLeakTaskListener.statusErrorResponse(context.getResources().getString(R.string.cases_status_error));
                isError = true;
            } else if (jsonObject.get(ExtrasHelper.LEAK_JSON_OBJECT_ID).toString().equals(null)) {
                statusLeakTaskListener.statusErrorResponse(context.getResources().getString(R.string.cases_status_error));
                isError = true;
            } else if (jsonObject.get(ExtrasHelper.LEAK_JSON_OBJECT_ID).toString() != null) {
                jsonObject.put(ExtrasHelper.LEAK_JSON_OBJECT_STATUS, params.get(ExtrasHelper.LEAK_JSON_OBJECT_STATUS));
                leak.setLeakStatus(jsonObject.get(ExtrasHelper.LEAK_JSON_OBJECT_STATUS).toString());
                Log.d(DEBUG_TAG, jsonObject.get(ExtrasHelper.LEAK_JSON_OBJECT_STATUS_UPDATE).toString());
                Log.d(DEBUG_TAG, leak.getLeakStatus());
                isError = false;
                if (statusLeakTaskListener != null) {
                    statusLeakTaskListener.statusSuccessResponse(leak);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } finally {

        }
    }

    /**
     * If the AsyncTask is cancelled, it will show an error response.
     */
    @Override
    protected void onCancelled() {
        super.onCancelled();
        statusLeakTaskListener.statusErrorResponse(context.getResources().getString(R.string.connection_error));
    }

    public void setStatusLeakTaskListener(StatusLeakTaskListener statusLeakTaskListener) {
        this.statusLeakTaskListener = statusLeakTaskListener;
    }

    /**
     * Interface for managing the different outputs of the AsyncTask
     */
    public interface StatusLeakTaskListener {
        void statusErrorResponse(String error);

        void statusSuccessResponse(Leak leak);
    }
}
