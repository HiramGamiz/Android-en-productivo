package com.zgas.tesselar.myzuite.Service;

import android.annotation.SuppressLint;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Formatter;
import java.util.List;

/**
 * Class that communicates with the service and will push the result to the Leak model list.
 *
 * @author jarvizu on 22/09/2017.
 * @version 2018.0.9
 * @see AsyncTask
 * @see Leak
 * @see JSONObject
 * @see UserPreferences
 * @see LeakagesTaskListener
 */
public class GetLeakagesTask extends AsyncTask<URL, JSONObject, JSONObject> {

    private final String DEBUG_TAG = getClass().getSimpleName();
    private static final String CASES_ARRAY = "Leaks";
    private static final String METHOD = "GET";
    private static final String JSON_OBJECT_ERROR = "error";
    private static final String USER_ID = "Id";

    @SuppressLint("StaticFieldLeak")
    private Context context;
    private JSONObject params;
    private LeakagesTaskListener leakagesTaskListener;
    private UserPreferences userPreferences;
    private ProgressDialog progressDialog;
    private boolean isError = false;

    /**
     * Constructor for the GetLeakagesTask. Additionally, we have an UserPreferences class reference
     * so we can obtain the user data.
     *
     * @param context Current context of the application
     * @param params  Parameters that will be sent to the service.
     */
    public GetLeakagesTask(Context context, JSONObject params) {
        this.context = context;
        this.params = params;
        userPreferences = new UserPreferences(context);
    }

    /**
     * progress dialog to show user that the backup is processing.
     */
    @Override
    protected void onPreExecute() {
        progressDialog = ProgressDialog.show(context, null, context.getResources().getString(R.string.wait_cases_message), false);
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
            Formatter formatter = new Formatter();
            String format = formatter.format(UrlHelper.GET_LEAKS_URL, params.get(USER_ID)).toString();
            String adminToken = userPreferences.getAdminToken();

            URL url = new URL(format);
            ConnectionController connection = new ConnectionController(adminToken, url, METHOD, null, context);
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
     * correct.     *
     * If an error occurs, the LeakagesTaskListener will manage it.
     * Else, the json data will be mapped with our Leak object and it will be shown on the user
     * interface.
     *
     * @param jsonObject The leak object that will be received.
     */
    @Override
    protected void onPostExecute(JSONObject jsonObject) {
        super.onPostExecute(jsonObject);
        progressDialog.dismiss();

        List<Leak> leaksList = new ArrayList<>();
        JSONArray leaksArray;

        try {
            try{
                if(jsonObject.get("error").toString()!=null){
                    String Aux=jsonObject.get("error").toString()+" Message: "+jsonObject.get("message").toString();
                    Log.d(DEBUG_TAG, Aux);
                    leakagesTaskListener.getLeakagesErrorResponse(Aux);
                    isError = true;
                }
            }catch (Exception e ){}
            if (jsonObject == null) {
                leakagesTaskListener.getLeakagesErrorResponse(jsonObject.getString(JSON_OBJECT_ERROR));
                isError = true;
            } else if (jsonObject.has(JSON_OBJECT_ERROR)) {
                if (jsonObject.get(JSON_OBJECT_ERROR).toString().equals("400")) {
                    leakagesTaskListener.getLeakagesErrorResponse(context.getResources().getString(R.string.cases_data_error));
                    isError = true;
                }
            } else if (jsonObject.has(CASES_ARRAY)) {
                leaksArray = jsonObject.getJSONArray(CASES_ARRAY);
                for (int i = 0; i < leaksArray.length(); i++) {
                    JSONObject caseObject = leaksArray.getJSONObject(i);
                    Leak leak = new Leak();
                    leak.setLeakId(caseObject.getString(ExtrasHelper.LEAK_JSON_OBJECT_ID));
                    Log.d(DEBUG_TAG, "Id del caso: " + leak.getLeakId());
                    leak.setLeakTimeDeparture(caseObject.getString(ExtrasHelper.LEAK_JSON_OBJECT_DATE_DEPARTURE));
                    Log.d(DEBUG_TAG, "Llegada del caso: " + leak.getLeakTimeDeparture());
                    leak.setLeakTimeScheduled(caseObject.getString(ExtrasHelper.LEAK_JSON_OBJECT_DATE_SCHEDULED));
                    Log.d(DEBUG_TAG, "Hora programada del caso: " + leak.getLeakTimeScheduled());
                    leak.setLeakTimeSeen(caseObject.getString(ExtrasHelper.LEAK_JSON_OBJECT_SEEN));
                    Log.d(DEBUG_TAG, "Hora de visualización: " + leak.getLeakTimeSeen());
                    leak.setLeakTimeAssignment(caseObject.getString(ExtrasHelper.LEAK_JSON_OBJECT_DATE_TECHNICIAN));
                    Log.d(DEBUG_TAG, "Hora de asignación del caso: " + leak.getLeakTimeAssignment());
                    leak.setLeakTimeEnd(caseObject.getString(ExtrasHelper.LEAK_JSON_OBJECT_DATE_END));
                    Log.d(DEBUG_TAG, "Hora de terminación del caso: " + leak.getLeakTimeEnd());
                    leak.setLeakAccountName(caseObject.getString(ExtrasHelper.LEAK_JSON_OBJECT_WHO_REPORTS));
                    Log.d(DEBUG_TAG, "Cliente del caso: " + leak.getLeakAccountName());
                    leak.setLeakAddress(caseObject.getString(ExtrasHelper.LEAK_JSON_OBJECT_ADDRESS));
                    Log.d(DEBUG_TAG, "Dirección del caso: " + leak.getLeakAddress());
                    leak.setLeakSubject(caseObject.getString(ExtrasHelper.LEAK_JSON_OBJECT_SUBJECT));
                    Log.d(DEBUG_TAG,    "Descripción del caso: " + leak.getLeakSubject());
                    leak.setLeakCylinderCapacity(caseObject.getString(ExtrasHelper.LEAK_JSON_OBJECT_CYLINDER_CAPACITY));
                    Log.d(DEBUG_TAG, "Capacidad: " + leak.getLeakCylinderCapacity());
                    leak.setLeakCylinderColor(caseObject.getString(ExtrasHelper.LEAK_JSON_OBJECT_COLOR));
                    Log.d(DEBUG_TAG, "Color del cilindro: " + leak.getLeakCylinderColor());
                    leak.setLeakChannel(caseObject.getString(ExtrasHelper.LEAK_JSON_OBJECT_CHANNEL));
                    Log.d(DEBUG_TAG, "Chanel: " + leak.getLeakChannel());
                    leak.setLeakFolioSalesNote(caseObject.getString(ExtrasHelper.LEAK_JSON_OBJECT_SALES_NOTE));
                    Log.d(DEBUG_TAG, "Folio: " + leak.getLeakFolioSalesNote());
                    //<-------------------------Agregar tipo de fuga --------------------------------------------->
                    leak.setLeakType(caseObject.get(ExtrasHelper.LEAK_JSON_OBJECT_SERVICE_TYPE).toString());

                    Log.d(DEBUG_TAG, "CaseType " + leak.getLeakType());
                    leak.setLeakStatus(caseObject.get(ExtrasHelper.LEAK_JSON_OBJECT_STATUS).toString());
                    Log.d(DEBUG_TAG, "CaseStatus " + leak.getLeakStatus());
                    leak.setLeakPriority(caseObject.get(ExtrasHelper.LEAK_JSON_OBJECT_PRIORITY).toString());
                    Log.d(DEBUG_TAG, "CasePriority " + leak.getLeakPriority());
                    leak.setLeakName(caseObject.get(ExtrasHelper.LEAK_JSON_OBJECT_NAME).toString());
                    if (leak.getLeakType().equals(context.getResources().getString(R.string.order_type_order))) {
                        leak.setLeakType(context.getResources().getString(R.string.order_type_order));
                    } else if (leak.getLeakType().equals(context.getResources().getString(R.string.order_type_cut))) {
                        leak.setLeakType(context.getResources().getString(R.string.order_type_cut));
                    } else if (leak.getLeakType().equals(context.getResources().getString(R.string.order_type_reconnection))) {
                        leak.setLeakType(context.getResources().getString(R.string.order_type_reconnection));
                    }
                    Log.d(DEBUG_TAG, "Tipo del caso: " + leak.getLeakType());

                    if (leak.getLeakStatus().equals(context.getResources().getString(R.string.order_status_in_progress))) {
                        leak.setLeakStatus(context.getResources().getString(R.string.order_status_in_progress));
                    } else if (leak.getLeakStatus().equals(context.getResources().getString(R.string.order_status_failed))) {
                        leak.setLeakStatus(context.getResources().getString(R.string.order_status_failed));
                    } else if (leak.getLeakStatus().equals(context.getResources().getString(R.string.order_status_closed))) {
                        leak.setLeakStatus(context.getResources().getString(R.string.order_status_closed));
                    } else if (leak.getLeakStatus().equals(context.getResources().getString(R.string.order_status_new))) {
                        leak.setLeakStatus(context.getResources().getString(R.string.order_status_new));
                    } else if (leak.getLeakStatus().equals(context.getResources().getString(R.string.order_status_assigned))) {
                        leak.setLeakStatus(context.getResources().getString(R.string.order_status_assigned));
                    } else if (leak.getLeakStatus().equals(context.getResources().getString(R.string.order_status_accepted))) {
                        leak.setLeakStatus(context.getResources().getString(R.string.order_status_accepted));
                    } else if (leak.getLeakStatus().equals(context.getResources().getString(R.string.order_status_retired))) {
                        leak.setLeakStatus(context.getResources().getString(R.string.order_status_retired));
                    }
                    Log.d(DEBUG_TAG, "Status del caso: " + leak.getLeakStatus());

                    if (leak.getLeakPriority().equals(context.getResources().getString(R.string.order_priority_high))) {
                        leak.setLeakPriority(context.getResources().getString(R.string.order_priority_high));
                    } else if (leak.getLeakPriority().equals(context.getResources().getString(R.string.order_priority_medium))) {
                        leak.setLeakPriority(context.getResources().getString(R.string.order_priority_medium));
                    } else if (leak.getLeakPriority().equals(context.getResources().getString(R.string.order_priority_low))) {
                        leak.setLeakPriority(context.getResources().getString(R.string.order_priority_low));
                    }
                    Log.d(DEBUG_TAG, "Prioridad del caso: " + leak.getLeakPriority());

                    isError = false;
                    leaksList.add(leak);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } finally {
            if (leakagesTaskListener != null) {
                leakagesTaskListener.getLeakagesSuccessResponse(leaksList);
            }
        }
    }

    /**
     * If the AsyncTask is cancelled, it will show an error response.
     */
    @Override
    protected void onCancelled() {
        super.onCancelled();
        progressDialog.dismiss();
        leakagesTaskListener.getLeakagesErrorResponse(context.getResources().getString(R.string.connection_error));
    }

    public void setLeakagesTaskListener(LeakagesTaskListener leakagesTaskListener) {
        this.leakagesTaskListener = leakagesTaskListener;
    }

    /**
     * Interface for managing the different outputs of the AsyncTask
     */
    public interface LeakagesTaskListener {
        void getLeakagesErrorResponse(String error);

        void getLeakagesSuccessResponse(List<Leak> orderList);
    }
}
