package com.zgas.tesselar.myzuite.View.Fragment.UserOperator;


import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.zgas.tesselar.myzuite.Controller.Adapter.NothingSelectedSpinnerAdapter;
import com.zgas.tesselar.myzuite.Model.Incidence;
import com.zgas.tesselar.myzuite.Model.User;
import com.zgas.tesselar.myzuite.R;
import com.zgas.tesselar.myzuite.Service.PutIncidenceTask;
import com.zgas.tesselar.myzuite.Utilities.ExtrasHelper;
import com.zgas.tesselar.myzuite.Utilities.UserPreferences;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;


/**
 * This class is for requesting an incidence when the userType is of 'operator'
 *
 * @author jarvizu
 * @version 2018.0.9
 * @see JSONObject
 * @see User
 * @see Incidence
 * @see UserPreferences
 * @see android.os.AsyncTask
 * @see PutIncidenceTask
 */
public class HelpFragmentOperator extends Fragment implements
        PutIncidenceTask.PutIncidenceListener {

    private final String DEBUG_TAG = getClass().getSimpleName();

    @BindView(R.id.fragment_help_operator_sp_options)
    Spinner mSpinnerOptions;
    private String cancelationReason;
    private UserPreferences mUserPreferences;
    private Dialog dialog;
    private Unbinder unbinder;

    public HelpFragmentOperator() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View mRootView = inflater.inflate(R.layout.fragment_help_operator, container, false);
        unbinder = ButterKnife.bind(this, mRootView);
        Log.d(DEBUG_TAG, getResources().getString(R.string.on_create));
        mUserPreferences = new UserPreferences(getContext());
        User mUser = mUserPreferences.getUserObject();
        Log.d(DEBUG_TAG, "Usuario logeado id: " + mUser.getUserId());
        Log.d(DEBUG_TAG, "Usuario logeado nombre: " + mUser.getUserName());
        Log.d(DEBUG_TAG, "Usuario logeado tipo: " + mUser.getUserType());
        initUi();
        return mRootView;
    }

    @OnClick(R.id.fragment_help_operator_btn_send_problem)
    public void onClick() {
        selectOption();
    }

    public void initUi() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(), R.array.help_prompts, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinnerOptions.setAdapter(new NothingSelectedSpinnerAdapter(adapter, R.layout.contact_spinner_row_nothing_selected, getContext()));
    }

    private void selectOption() {
        if (mSpinnerOptions.getSelectedItem() == null) {
            Toast.makeText(getContext(), getResources().getString(R.string.leak_failure_incorrect), Toast.LENGTH_LONG).show();
        } else {
            cancelationReason = mSpinnerOptions.getSelectedItem().toString();
            callAsyncTask();
        }
    }

    private void callAsyncTask() {
        JSONObject params = new JSONObject();
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("d/MM/yyyy h:mm a");
        String date = dateFormat.format(calendar.getTime());

        try {
            params.put(ExtrasHelper.INCIDENCE_JSON_OBJECT_ID, mUserPreferences.getUserObject().getUserId());
            params.put(ExtrasHelper.INCIDENCE_JSON_OBJECT_REASON, cancelationReason);
            params.put(ExtrasHelper.INCIDENCE_JSON_OBJECT_TIME, date);

            Log.d(DEBUG_TAG, "Id: " + params.getString(ExtrasHelper.INCIDENCE_JSON_OBJECT_ID)
                    + " Razón: " + params.getString(ExtrasHelper.INCIDENCE_JSON_OBJECT_REASON)
                    + " Fecha: " + params.getString(ExtrasHelper.INCIDENCE_JSON_OBJECT_TIME));

            PutIncidenceTask putIncidenceTask = new PutIncidenceTask(getContext(), params);
            putIncidenceTask.setPutIncidenceListener(this);
            putIncidenceTask.execute();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void incidenceErrorResponse(String error) {
        Log.d(DEBUG_TAG, error);
        Toast.makeText(getContext(), error, Toast.LENGTH_LONG).show();
    }

    @Override
    public void incidenceSuccessResponse(Incidence incidence) {
        mSpinnerOptions.setSelection(0);
        dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.dialog_report_incidence);
        dialog.getWindow().getAttributes().windowAnimations = R.style.Theme_Dialog_Animation;
        Log.d(DEBUG_TAG, "Dialog " + getResources().getString(R.string.on_create));
        dialog.setCancelable(false);

        final Button mBtnAccept = dialog.findViewById(R.id.dialog_report_incidende_btn_accept);
        mBtnAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
