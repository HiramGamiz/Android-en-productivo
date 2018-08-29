package com.zgas.tesselar.myzuite.Service;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.zgas.tesselar.myzuite.Controller.ConnectionController;
import com.zgas.tesselar.myzuite.Model.User;
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
 * Class that communicates with the service and will push the result to the User model list.
 *
 * @author jarvizu on 19/09/2017.
 * @version 2018.0.9
 * @see AsyncTask
 * @see User
 * @see JSONObject
 * @see UserPreferences
 * @see UserInfoListener
 */
public class GetUserInfoTask extends AsyncTask<URL, JSONObject, JSONObject> {

    private final String DEBUG_TAG = getClass().getSimpleName();
    private static final String PARAMS_EMAIL = "email";
    private static final String METHOD = "GET";
    private static final String JSON_OBJECT_ERROR = "errorCode";
    private static final String OPERATORS_ARRAY = "Operators";

    private Context context;
    private JSONObject params;
    private UserInfoListener userInfoListener;
    private UserPreferences userPreferences;
    private ProgressDialog progressDialog;
    private boolean isError = false;

    /**
     * Constructor for the GetUserInfoTask. Additionally, we have an UserPreferences class reference
     * so we can obtain the user data.
     *
     * @param context Current context of the application
     * @param params  Parameters that will be sent to the service.
     */
    public GetUserInfoTask(Context context, JSONObject params) {
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
        //super.doInBackground(new URL(""));
        JSONObject jsonObject = null;

        try {
            Formatter formatter = new Formatter();
            String format = formatter.format(UrlHelper.GET_USER_DATA_URL, params.get(PARAMS_EMAIL)).toString();
            Log.d(DEBUG_TAG, "Url del usuario: " + format);
            String adminToken = userPreferences.getAdminToken();
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
     * If an error occurs, the UserInfoListener will manage it.
     * Else, the json data will be mapped with our User object and it will be shown on the user
     * interface.
     *
     * @param jsonObject The user object that will be received.
     */
    @Override
    protected void onPostExecute(JSONObject jsonObject) {
        super.onPostExecute(jsonObject);
        progressDialog.dismiss();
        User user = null;
        List<User> usersList = new ArrayList<>();

        try {
            try{
                if(jsonObject.get("error").toString()!=null){
                    String Aux=jsonObject.get("error").toString()+" Message: "+jsonObject.get("message").toString();
                    Log.d(DEBUG_TAG, Aux);
                    userInfoListener.userInfoErrorResponse(Aux);
                    isError = true;
                }
            }catch (Exception e ){}
            if (jsonObject == null) {
                userInfoListener.userInfoErrorResponse(jsonObject.getString(JSON_OBJECT_ERROR));
                isError = true;
            } else if (jsonObject.has(JSON_OBJECT_ERROR)) {
                if (jsonObject.get(JSON_OBJECT_ERROR).toString().equals("500")) {
                    userInfoListener.userInfoErrorResponse(context.getResources().getString(R.string.user_data_error));
                    isError = true;
                }
            } else if (jsonObject.has(ExtrasHelper.EXTRA_USER_ID)) {
                Log.d(DEBUG_TAG, "HOLA, SI SIRVE");
                user = new User();
                user.setUserStatus(jsonObject.get(ExtrasHelper.EXTRA_USER_STATUS).toString());
                user.setUserType(jsonObject.get(ExtrasHelper.EXTRA_USER_TYPE).toString());
                user.setUserName(jsonObject.get(ExtrasHelper.EXTRA_USER_NAME).toString());
                user.setUserId(jsonObject.get(ExtrasHelper.EXTRA_USER_ID).toString());
                user.setUserZone(jsonObject.get(ExtrasHelper.EXTRA_USER_ZONE).toString());
                user.setUserEmail(jsonObject.get(ExtrasHelper.EXTRA_USER_EMAIL).toString());
                if (user.getUserType().equals(String.valueOf(R.string.user_type_operator))) {
                    user.setUserType(String.valueOf(R.string.user_type_operator));
                    user.setUserRoute(jsonObject.get(ExtrasHelper.EXTRA_USER_ROUTE).toString());
                } else if (user.getUserType().equals(context.getResources().getString(R.string.user_type_supervisor))) {
                    user.setUserType(String.valueOf(R.string.user_type_supervisor));
                    JSONArray usersArray = jsonObject.getJSONArray(OPERATORS_ARRAY);
                    for (int i = 0; i < usersArray.length(); i++) {
                        JSONObject userObject = usersArray.getJSONObject(i);
                        User supervisedUser = new User();
                        supervisedUser.setUserType(userObject.get(ExtrasHelper.EXTRA_USER_TYPE).toString());
                        supervisedUser.setUserStatus(userObject.get(ExtrasHelper.EXTRA_USER_STATUS).toString());
                        Log.d(DEBUG_TAG, "SupervisedStatus " + supervisedUser.getUserStatus());
                        supervisedUser.setUserId(userObject.getString(ExtrasHelper.EXTRA_USER_ID));
                        Log.d(DEBUG_TAG, "Id del supervisado: " + supervisedUser.getUserId());
                        supervisedUser.setUserName(userObject.getString(ExtrasHelper.EXTRA_USER_NAME));
                        Log.d(DEBUG_TAG, "Nombre de supervisado: " + supervisedUser.getUserName());
                        supervisedUser.setUserEmail(userObject.getString(ExtrasHelper.EXTRA_USER_EMAIL));
                        Log.d(DEBUG_TAG, "Email del supervisado: " + supervisedUser.getUserEmail());
                        supervisedUser.setUserZone(userObject.getString(ExtrasHelper.EXTRA_USER_ZONE));
                        Log.d(DEBUG_TAG, "Zona del supervisado: " + supervisedUser.getUserZone());
                        supervisedUser.setUserRoute(userObject.getString(ExtrasHelper.EXTRA_USER_ROUTE));
                        Log.d(DEBUG_TAG, "Ruta del supervisado: " + supervisedUser.getUserRoute());

                        if (supervisedUser.getUserType().equals(String.valueOf(R.string.user_type_operator))) {
                            supervisedUser.setUserType(String.valueOf(R.string.user_type_operator));
                        } else if (supervisedUser.getUserType().equals(String.valueOf(R.string.user_type_technician))) {
                            supervisedUser.setUserType(String.valueOf(R.string.user_type_technician));
                        } else if (supervisedUser.getUserType().equals(String.valueOf(R.string.user_type_supervisor))) {
                            supervisedUser.setUserType(String.valueOf(R.string.user_type_supervisor));
                        } else if (supervisedUser.getUserType().equals(String.valueOf(R.string.user_type_service))) {
                            supervisedUser.setUserType(String.valueOf(R.string.user_type_service));
                        }
                        Log.d(DEBUG_TAG, "Tipo de supervisado: " + supervisedUser.getUserType());

                        if (supervisedUser.getUserStatus().equals(String.valueOf(R.string.user_active))) {
                            supervisedUser.setUserStatus(String.valueOf(R.string.user_active));
                        } else if (supervisedUser.getUserStatus().equals(String.valueOf(R.string.user_not_active))) {
                            supervisedUser.setUserStatus(String.valueOf(R.string.user_not_active));
                        }
                        Log.d(DEBUG_TAG, "Estatus del supervisado: " + supervisedUser.getUserStatus());

                        usersList.add(supervisedUser);
                    }

                } else if (user.getUserType().equals(String.valueOf(R.string.user_type_service))) {
                    user.setUserType(String.valueOf(R.string.user_type_service));
                } else if (user.getUserType().equals(String.valueOf(R.string.user_type_technician))) {
                    user.setUserType(String.valueOf(R.string.user_type_technician));
                }

                if (user.getUserStatus().equals(String.valueOf(R.string.user_active))) {
                    user.setUserStatus(String.valueOf(R.string.user_active));
                } else if (user.getUserStatus().equals(String.valueOf(R.string.user_not_active))) {
                    user.setUserStatus(String.valueOf(R.string.user_not_active));
                }
                isError = false;
                Log.d(DEBUG_TAG, "Id del usuario: " + user.getUserId());
                Log.d(DEBUG_TAG, "Nombre de usuario: " + user.getUserName());
                Log.d(DEBUG_TAG, "Tipo de usuario: " + user.getUserType());
                Log.d(DEBUG_TAG, "Email del usuario: " + user.getUserEmail());
                Log.d(DEBUG_TAG, "Zona del usuario: " + user.getUserZone());
                Log.d(DEBUG_TAG, "Ruta del usuario: " + user.getUserRoute());
                Log.d(DEBUG_TAG, "Estatus del usuario: " + user.getUserStatus());
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } finally {
            if (userInfoListener != null) {
                userInfoListener.userInfoSuccessResponse(user);
                userInfoListener.userSupervisedSuccessResponse(usersList);
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
        userInfoListener.userInfoErrorResponse(context.getResources().getString(R.string.connection_error));
    }


    public void setUserInfoListener(UserInfoListener userInfoListener) {
        this.userInfoListener = userInfoListener;
    }

    /**
     * Interface for managing the different outputs of the AsyncTask
     */
    public interface UserInfoListener {
        void userInfoErrorResponse(String error);

        void userInfoSuccessResponse(User user);

        void userSupervisedSuccessResponse(List<User> userList);
    }
}
