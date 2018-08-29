package com.zgas.tesselar.myzuite.Service;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.zgas.tesselar.myzuite.Controller.ConnectionController;
import com.zgas.tesselar.myzuite.Model.Order;
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

public class PutSeenTimeTask extends AsyncTask<URL, JSONObject, JSONObject> {

    private final String DEBUG_TAG = getClass().getSimpleName();
    private static final String METHOD = "PUT";
    private static final String JSON_OBJECT_ERROR = "StatusCode";

    private PutSeenTimeTask.SeenTimeTaskListener seenTimeTaskListener;
    private Context context;
    private JSONObject params;
    private UserPreferences userPreferences;
    private ProgressDialog progressDialog;
    private boolean isError = false;

    /**
     * Constructor for the PutSeenTimeTask. Additionally, we have an UserPreferences class reference
     * so we can obtain the user data.
     *
     * @param context Current context of the application
     * @param params  Parameters that will be sent to the service.
     */
    public PutSeenTimeTask(Context context, JSONObject params) {
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
            String format = formatter.format(UrlHelper.PUT_TIME_SEEN_URL).toString();
            Log.d(DEBUG_TAG, format);
            URL url = new URL(format);
            ConnectionController connectionController = new ConnectionController(adminToken, url, METHOD, params, context);
            Log.d(DEBUG_TAG, METHOD);
            jsonObject = connectionController.execute();

            if (jsonObject == null) {
                cancel(true);
            }

        } catch (MalformedURLException | FileNotFoundException | SocketTimeoutException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    /**
     * Method that will show the task result on the user interface. It will receive the jsonObject
     * obtained on doInBackground method, and it will check if the jsonObject has an error or is
     * correct.
     * If an error occurs, the OrderReviewTaskListener will manage it.
     * Else, the json data will be mapped with our Order object and a pop up will be shown
     * on the user interface.
     *
     * @param jsonObject The user object that will be received.
     */
    @Override
    protected void onPostExecute(JSONObject jsonObject) {
        super.onPostExecute(jsonObject);
        progressDialog.dismiss();
        Log.d(DEBUG_TAG, jsonObject.toString());
        Order order = null;
        boolean errorApex=false;
        if (seenTimeTaskListener == null) {
            Log.d(DEBUG_TAG, "Listener nulo");
        } else {
            Log.d(DEBUG_TAG, "Listener no nulo");
        }


            try {
                if (jsonObject.get("error").toString() != null) {
                    String Aux = jsonObject.get("error").toString() + " message: " + jsonObject.get("message").toString();
                    Log.d(DEBUG_TAG, Aux);
                    seenTimeTaskListener.seenTimeErrorResponse(Aux);
                    isError = true;
                    errorApex = true;
                }
            } catch (Exception e) {
            }
            if (!errorApex) {
                try {
                if (jsonObject == null) {
                    seenTimeTaskListener.seenTimeErrorResponse(context.getResources().getString(R.string.cases_status_error));
                    isError = true;
                } else if (jsonObject.has(ExtrasHelper.REVIEW_JSON_OBJECT_ORDER_ID)) {
                    order = new Order();
                    jsonObject.put(ExtrasHelper.REVIEW_JSON_OBJECT_ORDER_ID, params.get(ExtrasHelper.REVIEW_JSON_OBJECT_ORDER_ID));
                    jsonObject.put(ExtrasHelper.REVIEW_JSON_OBJECT_OPERATOR_ID, params.get(ExtrasHelper.REVIEW_JSON_OBJECT_OPERATOR_ID));
                    jsonObject.put(ExtrasHelper.ORDER_JSON_OBJECT_TIME_SEEN, params.get(ExtrasHelper.ORDER_JSON_OBJECT_TIME_SEEN));
                    Log.d(DEBUG_TAG, jsonObject.get(ExtrasHelper.REVIEW_JSON_OBJECT_ORDER_ID).toString());
                    Log.d(DEBUG_TAG, jsonObject.get(ExtrasHelper.REVIEW_JSON_OBJECT_OPERATOR_ID).toString());
                    Log.d(DEBUG_TAG, jsonObject.get(ExtrasHelper.ORDER_JSON_OBJECT_TIME_SEEN).toString());

                    isError = false;
                }
            } catch(JSONException e){
                e.printStackTrace();
            } finally{
                if (seenTimeTaskListener != null) {
                    seenTimeTaskListener.seenTimeSuccessResponse(order);
                }
            }
        }
    }

    public void setSeenTimeTaskListener(SeenTimeTaskListener seenTimeTaskListener) {
        this.seenTimeTaskListener = seenTimeTaskListener;
    }

    /**
     * If the AsyncTask is cancelled, it will show an error response.
     */
    @Override
    protected void onCancelled() {
        super.onCancelled();
    }

    /**
     * Interface for managing the different outputs of the AsyncTask
     */
    public interface SeenTimeTaskListener {
        void seenTimeErrorResponse(String error);

        void seenTimeSuccessResponse(Order order);
    }
}

