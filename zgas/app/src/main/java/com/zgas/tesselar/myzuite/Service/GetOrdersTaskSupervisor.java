package com.zgas.tesselar.myzuite.Service;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.zgas.tesselar.myzuite.Controller.ConnectionController;
import com.zgas.tesselar.myzuite.Model.Case;
import com.zgas.tesselar.myzuite.Model.Order;
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

/**
 * Class that communicates with the service and will push the result to the Order model list.
 *
 * @author jarvizu on 20/09/2017
 * @version 2018.0.9
 * @see AsyncTask
 * @see Order
 * @see JSONObject
 * @see UserPreferences
 * @see OrderTaskListener
 */
public class GetOrdersTaskSupervisor extends AsyncTask<URL, JSONObject, JSONObject> {

    private final String DEBUG_TAG = getClass().getSimpleName();
    private static final String CASES_ARRAY = "Orders";
    private static final String METHOD = "GET";
    private static final String USER_ID = "Id";
    private static final String JSON_OBJECT_ID = "Id";
    private static final String JSON_OBJECT_ERROR = "errorCode";

    private Context context;
    private JSONObject params;
    private UserPreferences userPreferences;
    private OrderTaskListener orderTaskListener;
    private ProgressDialog progressDialog;
    private boolean isError = false;
    private Case aOrder;

    /**
     * Constructor for the GetOrdersTask. Additionally, we have an UserPreferences class reference
     * so we can obtain the user data.
     *
     * @param context Current context of the application.
     * @param params  Parameters that will be sent to the service.
     */
    public GetOrdersTaskSupervisor(Context context, JSONObject params) {
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
            String format = formatter.format(UrlHelper.GET_CASES_SUPERVISOR_URL, params.get(USER_ID)).toString();
            String adminToken = userPreferences.getAdminToken();
            Log.d(DEBUG_TAG, "Url del usuario: " + format);
            Log.d(DEBUG_TAG, "Token del admin: " + adminToken);

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
     * correct.
     * If an error occurs, the OrderTaskListener will manage it.
     * Else, the json data will be mapped with our Order object and it will be shown on the user
     * interface.
     *
     * @param jsonObject The order object that will be received.
     */
    @Override
    protected void onPostExecute(JSONObject jsonObject) {
        super.onPostExecute(jsonObject);
        progressDialog.dismiss();
        ArrayList<Case> casesList = new ArrayList<>();
        JSONArray casesArray= new JSONArray();

        try {

            try{
                if(jsonObject.get("error").toString()!=null){
                    String Aux=jsonObject.get("error").toString()+" Message: "+jsonObject.get("message").toString();
                    Log.d(DEBUG_TAG, Aux);
                    orderTaskListener.getCasesErrorResponse(Aux);
                    isError = true;
                }
            }catch (Exception e ){}
            if (jsonObject == null) {
                orderTaskListener.getCasesErrorResponse(jsonObject.getString(JSON_OBJECT_ERROR));
                isError = true;
            } else if (jsonObject.has(JSON_OBJECT_ERROR)) {
                if (jsonObject.get(JSON_OBJECT_ERROR).toString().equals("400")) {
                    orderTaskListener.getCasesErrorResponse(context.getResources().getString(R.string.cases_data_error));
                    isError = true;
                }
            } else if (jsonObject.has(CASES_ARRAY)) {
                try{
                casesArray = jsonObject.getJSONArray(CASES_ARRAY);
                }
                catch (Exception e){
    String Aux="No hay pedidos";
    orderTaskListener.getCasesErrorResponse(Aux);
                }
                for (int i = 0; i < casesArray.length(); i++) {
                    JSONObject caseObject = casesArray.getJSONObject(i);
                    aOrder = new Case();
                    aOrder.setOrderOperatorName(caseObject.get(ExtrasHelper.ORDER_JSON_OPERATOR_NAME).toString());
                    aOrder.setOrderName(caseObject.get(ExtrasHelper.ORDER_JSON_NAME).toString());
                    aOrder.setOrderId(caseObject.get(JSON_OBJECT_ID).toString());
                    Log.d(DEBUG_TAG, "Id del caso: " + aOrder.getOrderId());
                    aOrder.setOrderUserId(userPreferences.getUserObject().getUserId());

                    aOrder.setorderQuantify(caseObject.getString(ExtrasHelper.ORDER_JSON_QUANTIFY));
                    aOrder.setLeakChannel(caseObject.getString(ExtrasHelper.LEAK_JSON_OBJECT_CHANNEL));
                    Log.d(DEBUG_TAG, "Id del operador: " + aOrder.getOrderUserId());
                    aOrder.setOrderTimeScheduled(caseObject.getString(ExtrasHelper.ORDER_JSON_OBJECT_TIME_SCHEDULED));
                    Log.d(DEBUG_TAG, "Hora programada: " + aOrder.getOrderTimeScheduled());
                    aOrder.setOrderTimeSeen(caseObject.getString(ExtrasHelper.ORDER_JSON_OBJECT_TIME_SEEN));
                    Log.d(DEBUG_TAG, "Hora de visuailzación: " + aOrder.getOrderTimeSeen());
                    aOrder.setOrderTimeAssignment(caseObject.getString(ExtrasHelper.ORDER_JSON_OBJECT_TIME_ASSIGNMENT));
                    Log.d(DEBUG_TAG, "Hora de asignación: " + caseObject.getString(ExtrasHelper.ORDER_JSON_OBJECT_TIME_ASSIGNMENT));
                    aOrder.setOrderAccountName(caseObject.getString(ExtrasHelper.ORDER_JSON_OBJECT_ACCOUNT_NAME));
                    Log.d(DEBUG_TAG, "Cliente: " + aOrder.getOrderAccountName());
                    aOrder.setOrderAddress(caseObject.getString(ExtrasHelper.ORDER_JSON_OBJECT_ADDRESS));
                    Log.d(DEBUG_TAG, "Dirección: " + aOrder.getOrderAddress());
                    aOrder.setOrderContactName(caseObject.getString(ExtrasHelper.ORDER_JSON_OBJECT_CONTACT_NAME));
                    Log.d(DEBUG_TAG, "Cuenta: " + aOrder.getOrderAccountName());
                    aOrder.setOrderServiceType(caseObject.getString(ExtrasHelper.ORDER_JSON_OBJECT_SERVICE_TYPE));
                    Log.d(DEBUG_TAG, "Tipo de servicio: " + aOrder.getOrderServiceType());
                    aOrder.setOrderPaymentMethod(caseObject.getString(ExtrasHelper.ORDER_JSON_OBJECT_PAYMENT_METHOD));
                    Log.d(DEBUG_TAG, "Tipo de pago: " + aOrder.getOrderPaymentMethod());
                    aOrder.setOrderNotice(caseObject.getString(ExtrasHelper.ORDER_JSON_OBJECT_NOTICE));
                    Log.d(DEBUG_TAG, "Aviso: " + aOrder.getOrderNotice());
                    aOrder.setOrderTreatment(caseObject.getString(ExtrasHelper.ORDER_JSON_TREATMENT));
                    Log.d(DEBUG_TAG, "Tratamiento: " + aOrder.getOrderTreatment());

                    aOrder.setOrderType(caseObject.get(ExtrasHelper.ORDER_JSON_OBJECT_TYPE).toString());
                    aOrder.setOrderStatus(caseObject.get(ExtrasHelper.ORDER_JSON_OBJECT_STATUS).toString());
                    aOrder.setOrderPriority(caseObject.get(ExtrasHelper.ORDER_JSON_OBJECT_PRIORITY).toString());

                    if (aOrder.getOrderType().equals(String.valueOf(R.string.order_type_order))) {
                        aOrder.setOrderType(String.valueOf(R.string.order_type_order));
                    } else if (aOrder.getOrderType().equals(String.valueOf(R.string.order_type_cut))) {
                        aOrder.setOrderType(String.valueOf(R.string.order_type_cut));
                    } else if (aOrder.getOrderType().equals(String.valueOf(R.string.order_type_reconnection))) {
                        aOrder.setOrderType(String.valueOf(R.string.order_type_reconnection));
                    }
                    Log.d(DEBUG_TAG, "Tipo de caso: " + aOrder.getOrderType());

                    if (aOrder.getOrderStatus().equals(String.valueOf(R.string.order_status_in_progress))) {
                        aOrder.setOrderStatus(String.valueOf(R.string.order_status_in_progress));
                    } else if (aOrder.getOrderStatus().equals(String.valueOf(R.string.order_status_failed))) {
                        aOrder.setOrderStatus(String.valueOf(R.string.order_status_failed));
                    } else if (aOrder.getOrderStatus().equals(String.valueOf(R.string.order_status_finished))) {
                        aOrder.setOrderStatus(String.valueOf(R.string.order_status_finished));
                    } else if (aOrder.getOrderStatus().equals(String.valueOf(R.string.order_status_new))) {
                        aOrder.setOrderStatus(String.valueOf(R.string.order_status_new));
                    } else if (aOrder.getOrderStatus().equals(String.valueOf(R.string.order_status_assigned))) {
                        aOrder.setOrderStatus(String.valueOf(R.string.order_status_assigned));
                    } else if (aOrder.getOrderStatus().equals(String.valueOf(R.string.order_status_accepted))) {
                        aOrder.setOrderStatus(String.valueOf(R.string.order_status_accepted));
                    } else if (aOrder.getOrderStatus().equals(String.valueOf(R.string.order_status_retired))) {
                        aOrder.setOrderStatus(String.valueOf(R.string.order_status_retired));
                    }

                    Log.d(DEBUG_TAG, "Status del caso : " + aOrder.getOrderStatus());

                    if (aOrder.getOrderPriority().equals(String.valueOf(R.string.order_priority_high))) {
                        aOrder.setOrderPriority(String.valueOf(R.string.order_priority_high));
                    } else if (aOrder.getOrderPriority().equals(String.valueOf(R.string.order_priority_medium))) {
                        aOrder.setOrderPriority(String.valueOf(R.string.order_priority_medium));
                    } else if (aOrder.getOrderPriority().equals(String.valueOf(R.string.order_priority_low))) {
                        aOrder.setOrderPriority(String.valueOf(R.string.order_priority_low));
                    }
                    Log.d(DEBUG_TAG, "Prioridad del caso: " + aOrder.getOrderPriority());

                    isError = false;
                    casesList.add(aOrder);
                }
            }
        } catch (JSONException e) {
            String aa= e.getMessage();
            e.printStackTrace();
        } finally {
            if (orderTaskListener != null) {
                orderTaskListener.getCasesSuccessResponse(casesList);
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
        orderTaskListener.getCasesErrorResponse(context.getResources().getString(R.string.connection_error));
    }

    public void setOrderTaskListener(OrderTaskListener getOrderTaskListener) {
        this.orderTaskListener = getOrderTaskListener;
    }

    /**
     * Interface for managing the different outputs of the AsyncTask
     */
    public interface OrderTaskListener {
        void getCasesErrorResponse(String error);

        void getCasesSuccessResponse(ArrayList<Case> orderList);
    }
}
