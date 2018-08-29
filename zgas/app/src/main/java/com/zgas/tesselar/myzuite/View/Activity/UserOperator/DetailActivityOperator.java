package com.zgas.tesselar.myzuite.View.Activity.UserOperator;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.shashank.sony.fancydialoglib.Animation;
import com.shashank.sony.fancydialoglib.FancyAlertDialog;
import com.shashank.sony.fancydialoglib.FancyAlertDialogListener;
import com.shashank.sony.fancydialoglib.Icon;
import com.zgas.tesselar.myzuite.Controller.Adapter.NothingSelectedSpinnerAdapter;
import com.zgas.tesselar.myzuite.Model.Order;
import com.zgas.tesselar.myzuite.Model.User;
import com.zgas.tesselar.myzuite.R;
import com.zgas.tesselar.myzuite.Service.PutStatusOrderTask;
import com.zgas.tesselar.myzuite.Utilities.ExtrasHelper;
import com.zgas.tesselar.myzuite.Utilities.UserPreferences;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Class that shows the details of the orders; it's used when the operator is of type 'Operator'.
 * In this class we can also modify the order status - in progress, cancelled, finished -
 * and open the Waze app with the order address.
 *
 * @author jarvizu
 * @version 2018.0.9
 * @see JSONObject
 * @see Order
 * @see User
 * @see UserPreferences
 * @see Bundle
 * @see android.os.AsyncTask
 * @see PutStatusOrderTask
 */
public class DetailActivityOperator extends AppCompatActivity implements
        PutStatusOrderTask.StatusOrderTaskListener {

    private final String DEBUG_TAG = getClass().getSimpleName();

    private String mStrCaseId;
    private String mStrCaseAddress;
    private String mStrCaseStatus;
    private String mStrCaseTimeSeen;
    private String mStrCaseServiceType;
    private String mStrTicket;
    private String mStrTicket_Folio;
    private String mStrQuantity;
    private String mStrCylinder10;
    private String mStrCylinder20;
    private String mStrCylinder30;
    private String mStrCylinder45;
    private String mStrFailureReason;
    private String mStrQuantify_product;
    private String mStrCaseName;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.activity_detail_operator_tv_client_name)
    TextView mUserName;
    @BindView(R.id.activity_detail_operator_tv_case_address)
    TextView mCaseAddress;
    @BindView(R.id.activity_detail_operator_tv_status)
    TextView mCaseStatus;
    @BindView(R.id.activity_detail_operator_tv_time_in)
    TextView mCaseTimeIn;
    @BindView(R.id.activity_detail_operator_operator_Quantify_seen)
    TextView mCaseQuantify;
    @BindView(R.id.activity_detail_operator_tv_arrived)
    TextView mCaseTimeArrived;
    @BindView(R.id.activity_detail_operator_tv_time_programmed)
    TextView mCaseTimeProgrammed;
    @BindView(R.id.activity_detal_operator_tv_payment)
    TextView mCasePaymentMethod;
    @BindView(R.id.activity_detail_operator_tv_treatment)
    TextView mCaseTreatment;
    @BindView(R.id.activity_detail_operator_tv_type)
    TextView mCaseType;
    @Nullable
    @BindView(R.id.dialog_finish_case_stationary_tv_quantity)
    EditText etQuantity;
    @Nullable
    @BindView(R.id.dialog_finish_case_stationary_tv_ticket_number)
    EditText etTicket;
    @Nullable
    @BindView(R.id.activity_detail_operator_fab_in_progress)
    FloatingActionButton mFabInProgress;
    @Nullable
    @BindView(R.id.activity_detail_operator_fab_finished)
    FloatingActionButton mFabFinished;
    @Nullable
    @BindView(R.id.activity_detail_operator_fab_failure)
    FloatingActionButton mFabFailed;
    @Nullable
    @BindView(R.id.activity_detail_operator_fab_waze)
    FloatingActionButton mFabWaze;


    private boolean isClicked = false;
    private JSONObject params;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_operator);
        ButterKnife.bind(this);
        overridePendingTransition(R.anim.pull_in_right, R.anim.no_change);
        Log.d(DEBUG_TAG, getResources().getString(R.string.on_create));
        UserPreferences userPreferences = new UserPreferences(this);
        User user = userPreferences.getUserObject();
        initUi();
        checkButtons();
    }

    private void checkButtons() {
        if (mStrCaseStatus.equals(this.getResources().getString(R.string.order_status_in_progress))) {
            UserPreferences userPreferences = new UserPreferences(this);
            User user = userPreferences.getUserObject();
            if(!user.getUserType().equals(String.valueOf((R.string.user_type_supervisor)))){
            mFabFinished.show();
            mFabFailed.show();
            }else{
                mFabFinished.hide();
                mFabFailed.hide();
            }
            mFabWaze.show();
            mFabInProgress.hide();
        } else if (mStrCaseStatus.equals(this.getResources().getString(R.string.order_status_failed))) {
            mFabFinished.hide();
            mFabFailed.hide();
            mFabWaze.hide();
            mFabInProgress.hide();
        } else if (mStrCaseStatus.equals(this.getResources().getString(R.string.order_status_finished))) {
            mFabFinished.hide();
            mFabFailed.hide();
            mFabWaze.hide();
            mFabInProgress.hide();
        } else if (mStrCaseStatus.equals(this.getResources().getString(R.string.order_status_assigned))) {
            mFabFinished.hide();
            mFabFailed.hide();
            mFabWaze.hide();
            mFabInProgress.show();
        }else{
            mFabFinished.hide();
            mFabFailed.hide();
            mFabWaze.hide();
            mFabInProgress.hide();
        }
    }

    @OnClick(R.id.activity_detail_operator_fab_in_progress)
    public void onClickProgress() {
        inProgressDialog();
    }

    @OnClick(R.id.activity_detail_operator_fab_finished)
    public void onClickFinished() {
        finishDialog();
    }

    @OnClick(R.id.activity_detail_operator_fab_failure)
    public void onClickFailed() {
        failureDialog();
    }

    @OnClick(R.id.activity_detail_operator_fab_waze)
    public void onClickWaze() {
        wazeIntent(mStrCaseAddress);
    }

    private void initUi() {
        Bundle bundle = getIntent().getExtras();
        mStrCaseId = bundle.getString(ExtrasHelper.ORDER_JSON_OBJECT_ID);
        String mStrCaseUserName = bundle.getString(ExtrasHelper.ORDER_JSON_OBJECT_ACCOUNT_NAME);
        mStrCaseAddress = bundle.getString(ExtrasHelper.ORDER_JSON_OBJECT_ADDRESS);
        mStrCaseStatus = bundle.getString(ExtrasHelper.ORDER_JSON_OBJECT_STATUS);
        String mStrCasePriority = bundle.getString(ExtrasHelper.ORDER_JSON_OBJECT_PRIORITY);
        String mStrCaseTimeIn = bundle.getString(ExtrasHelper.ORDER_JSON_OBJECT_TIME_ASSIGNMENT);
        String mStrCaseTimeArrived = bundle.getString(ExtrasHelper.ORDER_JSON_OBJECT_TIME_ARRIVAL);
        String mStrCaseTimeProgrammed = bundle.getString(ExtrasHelper.ORDER_JSON_OBJECT_TIME_SCHEDULED);
        String mStrCasePaymentMethod = bundle.getString(ExtrasHelper.ORDER_JSON_OBJECT_PAYMENT_METHOD);
        String mStrCaseRecordType = bundle.getString(ExtrasHelper.ORDER_JSON_OBJECT_RECORD_TYPE);
        mStrCaseServiceType = bundle.getString(ExtrasHelper.ORDER_JSON_OBJECT_SERVICE_TYPE);
        String mStrCaseTimeSeen = bundle.getString(ExtrasHelper.ORDER_JSON_OBJECT_TIME_SEEN);
        String mStrCaseTreatment = bundle.getString(ExtrasHelper.ORDER_JSON_TREATMENT);
        String mStrCaseType = bundle.getString(ExtrasHelper.ORDER_JSON_OBJECT_TYPE);
        mStrQuantify_product = bundle.getString(ExtrasHelper.ORDER_JSON_QUANTIFY);
        mStrCaseName=bundle.getString(ExtrasHelper.ORDER_JSON_NAME);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle(getApplicationContext().getResources().getString(R.string.prompt_detail_operator) + " " + mStrCaseName);

        if (mStrCaseTreatment == null || mStrCaseTreatment.equals("") || mStrCaseTreatment.equals("null")) {
            mCaseTreatment.setText(getResources().getString(R.string.no_data));
        } else {
            mCaseTreatment.setText(mStrCaseTreatment);
        }

        if (mStrCaseType == null || mStrCaseType.equals("") || mStrCaseType.equals("null")) {
            mCaseType.setText(getResources().getString(R.string.no_data));
        } else {
            mCaseType.setText(mStrCaseType);
        }

        if (mStrCaseUserName == null || mStrCaseUserName.equals("") || mStrCaseUserName.equals("null")) {
            mUserName.setText(getResources().getString(R.string.no_data));
        } else {
            mUserName.setText(mStrCaseUserName);
        }

        if (mStrCaseAddress == null || mStrCaseAddress.equals("") || mStrCaseAddress.equals("null")) {
            mCaseAddress.setText(getResources().getString(R.string.no_data));
        } else {
            mCaseAddress.setText(mStrCaseAddress);
        }
        if (mStrQuantify_product == null || mStrQuantify_product.equals("") || mStrQuantify_product.equals("null")) {
            mCaseQuantify.setText(getResources().getString(R.string.no_data));
        } else {
            mCaseQuantify.setText(mStrQuantify_product);
        }

        if (mStrCaseStatus == null || mStrCaseStatus.equals("") || mStrCaseStatus.equals("null")) {
            mCaseStatus.setText(getResources().getString(R.string.no_data));
        } else {
            if (mStrCaseStatus.equals(this.getResources().getString(R.string.order_status_in_progress))) {
                //  getSupportActionBar().setDisplayHomeAsUpEnabled(false);
                //getSupportActionBar().setDisplayShowHomeEnabled(false);
                mCaseStatus.setTextColor(getResources().getColor(R.color.amber));
                mFabWaze.setVisibility(View.VISIBLE);
                mCaseStatus.setText(mStrCaseStatus);
            } else if (mStrCaseStatus.equals(this.getResources().getString(R.string.order_status_failed))) {
                mFabFinished.hide();
                mFabFailed.hide();
                mFabWaze.hide();
                mFabInProgress.hide();
                mCaseStatus.setTextColor(getResources().getColor(R.color.red));
                mCaseStatus.setText(mStrCaseStatus);
            } else if (mStrCaseStatus.equals(this.getResources().getString(R.string.order_status_finished))) {
                mFabFinished.hide();
                mFabFailed.hide();
                mFabWaze.hide();
                mFabInProgress.hide();
                mCaseStatus.setTextColor(getResources().getColor(R.color.light_green));
                mCaseStatus.setText(mStrCaseStatus);
            } else if(mStrCaseStatus.equals(this.getResources().getString(R.string.order_status_assigned))){
                mCaseStatus.setTextColor(getResources().getColor(R.color.blue));
                mCaseStatus.setText(mStrCaseStatus);
            }
        }

        if (mStrCaseTimeIn == null || mStrCaseTimeIn.equals("") || mStrCaseTimeIn.equals("null")) {
            mCaseTimeIn.setText(getResources().getString(R.string.no_data));
        } else {
            mCaseTimeIn.setText(mStrCaseTimeIn);
        }

        if (mStrCaseTimeSeen == null || mStrCaseTimeSeen.equals("") || mStrCaseTimeSeen.equals("null")) {
            //mCaseTimeSeen.setText(getResources().getString(R.string.no_data));
        } else {
            //   mCaseTimeSeen.setText(mStrCaseTimeSeen);
        }

        if (mStrCaseTimeArrived == null || mStrCaseTimeArrived.equals("") || mStrCaseTimeArrived.equals("null")) {
            mCaseTimeArrived.setText(getResources().getString(R.string.no_data));
        } else {
            mCaseTimeArrived.setText(mStrCaseTimeArrived);
        }

        if (mStrCaseTimeProgrammed == null || mStrCaseTimeProgrammed.equals("") || mStrCaseTimeProgrammed.equals("null")) {
            mCaseTimeProgrammed.setText(getResources().getString(R.string.no_data));
        } else {
            mCaseTimeProgrammed.setText(mStrCaseTimeProgrammed);
        }

        if (mStrCasePaymentMethod == null || mStrCasePaymentMethod.equals("") || mStrCasePaymentMethod.equals("null")) {
            mCasePaymentMethod.setText(getResources().getString(R.string.no_data));
        } else {
            mCasePaymentMethod.setText(mStrCasePaymentMethod);
        }
    }

    private void callAsyncTaskInProgress() {
        params = new JSONObject();
        try {
            params.put(ExtrasHelper.ORDER_JSON_OBJECT_ID, mStrCaseId);
            params.put(ExtrasHelper.ORDER_JSON_OBJECT_STATUS, this.getResources().getString(R.string.order_status_in_progress));
            Log.d(DEBUG_TAG, "Status: " + params.getString(ExtrasHelper.ORDER_JSON_OBJECT_STATUS) + " ID: " + params.getString(ExtrasHelper.ORDER_JSON_OBJECT_ID));

            PutStatusOrderTask putStatusOrderTask = new PutStatusOrderTask(this, params);
            putStatusOrderTask.setStatusOrderTaskListener(this);
            putStatusOrderTask.execute();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void callAsyncTaskFinished() {
        JSONObject params = new JSONObject();
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("d/MM/yyyy h:mm a");
        String date = dateFormat.format(calendar.getTime());

        try {
            params.put(ExtrasHelper.ORDER_JSON_OBJECT_ID, mStrCaseId);
            params.put(ExtrasHelper.ORDER_JSON_TIME_FINISHED, date);
            params.put(ExtrasHelper.ORDER_JSON_OBJECT_STATUS, this.getResources().getString((R.string.order_status_finished)));
            if (mStrCaseServiceType.equals(this.getResources().getString(R.string.service_cylinder))) {
                params.put(ExtrasHelper.ORDER_JSON_OBJECT_TICKET_FOLIO, mStrTicket_Folio);
                params.put(ExtrasHelper.ORDER_JSON_OBJECT_CYLINDER_10, mStrCylinder10);
                params.put(ExtrasHelper.ORDER_JSON_OBJECT_CYLINDER_20, mStrCylinder20);
                params.put(ExtrasHelper.ORDER_JSON_OBJECT_CYLINDER_30, mStrCylinder30);
                params.put(ExtrasHelper.ORDER_JSON_OBJECT_CYLINDER_45, mStrCylinder45);
                Log.d(DEBUG_TAG, "Status: " + params.getString(ExtrasHelper.ORDER_JSON_OBJECT_STATUS) +
                        "Id: " + params.getString(ExtrasHelper.ORDER_JSON_OBJECT_ID) +
                        "folio" + params.getString(ExtrasHelper.ORDER_JSON_OBJECT_TICKET_FOLIO) +
                        "Cylinder 10: " + params.getString(ExtrasHelper.ORDER_JSON_OBJECT_CYLINDER_10) +
                        "Cylinder 20: " + params.getString(ExtrasHelper.ORDER_JSON_OBJECT_CYLINDER_20) +
                        "Cylinder 30: " + params.getString(ExtrasHelper.ORDER_JSON_OBJECT_CYLINDER_30) +
                        "Cylinder 45: " + params.getString(ExtrasHelper.ORDER_JSON_OBJECT_CYLINDER_45) +
                        "Date: " + params.getString(ExtrasHelper.ORDER_JSON_TIME_FINISHED));
            } else if (mStrCaseServiceType.equals(this.getResources().getString(R.string.service_static))) {
                params.put(ExtrasHelper.ORDER_JSON_OBJECT_TICKET, mStrTicket);
                params.put(ExtrasHelper.ORDER_JSON_OBJECT_QUANTITY, mStrQuantity);
//                  params.put(ExtrasHelper.ORDER_JSON_OBJECT_ADDRESS, mStrCaseAddress);

                Log.d(DEBUG_TAG, "Status: " + params.getString(ExtrasHelper.ORDER_JSON_OBJECT_STATUS) +
                        "id: " + params.getString(ExtrasHelper.ORDER_JSON_OBJECT_ID) +
                        "Ticket: " + params.getString(ExtrasHelper.ORDER_JSON_OBJECT_TICKET) +
                        "Quantity: " + params.getString(ExtrasHelper.ORDER_JSON_OBJECT_QUANTITY) +
                        //   "Address: " +"'"+ params.getString(ExtrasHelper.ORDER_JSON_OBJECT_ADDRESS)+"'" +
                        "Date: " + params.getString(ExtrasHelper.ORDER_JSON_TIME_FINISHED));
            }
            PutStatusOrderTask putStatusOrderTask = new PutStatusOrderTask(this, params);
            putStatusOrderTask.setStatusOrderTaskListener(this);
            putStatusOrderTask.execute();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void callAsyncTaskFailed() {
        params = new JSONObject();
        try {
            params.put(ExtrasHelper.ORDER_JSON_OBJECT_ID, mStrCaseId);
            params.put(ExtrasHelper.ORDER_JSON_OBJECT_STATUS, getResources().getString(R.string.order_status_failed));
            params.put(ExtrasHelper.ORDER_JSON_OBJECT_FAILURE_REASON, mStrFailureReason);
            Log.d(DEBUG_TAG, "Status: " + params.getString(ExtrasHelper.ORDER_JSON_OBJECT_STATUS) + " ID: " + params.getString(ExtrasHelper.ORDER_JSON_OBJECT_ID));
            PutStatusOrderTask putStatusOrderTask = new PutStatusOrderTask(this, params);
            putStatusOrderTask.setStatusOrderTaskListener(this);
            putStatusOrderTask.execute();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        int id = menuItem.getItemId();
        switch (id) {
            case android.R.id.home:
                finish();
                overridePendingTransition(R.anim.no_change, R.anim.push_out_right);
                break;
        }
        return super.onOptionsItemSelected(menuItem);
    }

    @Override
    public void onBackPressed() {
        //       if(!mStrCaseStatus.equals(this.getResources().getString(R.string.order_status_in_progress))){
        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.no_change, R.anim.push_out_right);
    }
    // }

    private void wazeIntent(String address) {
        final String url = "waze://?q=" + address;
        final Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(intent);
    }

    private void finishDialog() {
        final Dialog dialog = new Dialog(this);
        if (mStrCaseServiceType.equals("Cilindro")) {
            dialog.setContentView(R.layout.dialog_finish_case_operator_cylinder);
            dialog.getWindow().getAttributes().windowAnimations = R.style.Theme_Dialog_Animation;
            Log.d(DEBUG_TAG, getResources().getString(R.string.on_create));
            dialog.setCancelable(false);

            final EditText etTikectFolio = dialog.findViewById(R.id.dialog_finish_case_dialog_finish_cylinder_ticket_edit_text);
            final EditText et10Kilograms = dialog.findViewById(R.id.dialog_finish_case_cylinder_10_edit_text);
            final EditText et20Kilograms = dialog.findViewById(R.id.dialog_finish_case_cylinder_20_edit_text);
            final EditText et30Kilograms = dialog.findViewById(R.id.dialog_finish_case_cylinder_30_edit_text);
            final EditText et45Kilograms = dialog.findViewById(R.id.dialog_finish_case_cylinder_45_edit_text);

            Button mBtnAccept = dialog.findViewById(R.id.dialog_finish_case_cylinder_btn_accept);
            mBtnAccept.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mStrTicket_Folio = etTikectFolio.getText().toString();
                    mStrCylinder10 = et10Kilograms.getText().toString();
                    mStrCylinder20 = et20Kilograms.getText().toString();
                    mStrCylinder30 = et30Kilograms.getText().toString();
                    mStrCylinder45 = et45Kilograms.getText().toString();
                    callAsyncTaskFinished();
                    dialog.dismiss();
                }
            });

            Button mBtnCancel = dialog.findViewById(R.id.dialog_finish_case_cylinder_btn_cancel);
            mBtnCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });

        } else if (mStrCaseServiceType.equals("Estacionario")) {
            dialog.setContentView(R.layout.dialog_finish_case_operator_stationary);
            dialog.getWindow().getAttributes().windowAnimations = R.style.Theme_Dialog_Animation;
            Log.d(DEBUG_TAG, getResources().getString(R.string.on_create));
            dialog.setCancelable(false);

            etQuantity = dialog.findViewById(R.id.dialog_finish_case_stationary_tv_quantity);
            etTicket = dialog.findViewById(R.id.dialog_finish_case_stationary_tv_ticket_number);

            Button mBtnAccept = dialog.findViewById(R.id.dialog_finish_case_stationary_btn_accept);
            mBtnAccept.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (isEmpty(etQuantity) || isEmpty(etTicket)) {
                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.order_finish_incorrect), Toast.LENGTH_LONG).show();
                    } else {
                        mStrQuantity = etQuantity.getText().toString();
                        mStrTicket = etTicket.getText().toString();
                        Log.d(DEBUG_TAG, "Cantidad surtida " + mStrQuantity);
                        Log.d(DEBUG_TAG, "Folio del ticket " + mStrTicket);
                        etQuantity.getText().clear();
                        etTicket.getText().clear();
                        callAsyncTaskFinished();
                        dialog.dismiss();
                    }
                }
            });

            Button mBtnCancel = dialog.findViewById(R.id.dialog_finish_case_stationary_btn_cancel);
            mBtnCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
        }
        dialog.show();
    }

    private void failureDialog() {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_failure_case_operator);
        dialog.getWindow().getAttributes().windowAnimations = R.style.Theme_Dialog_Animation;

        Log.d(DEBUG_TAG, getResources().getString(R.string.on_create));
        dialog.setCancelable(false);

        final Spinner mSpinnerOptions = dialog.findViewById(R.id.dialog_failure_case_sp_option);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.failure_prompts, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinnerOptions.setAdapter(new NothingSelectedSpinnerAdapter(adapter, R.layout.contact_spinner_row_nothing_selected, this));

        Button mBtnAccept = dialog.findViewById(R.id.dialog_failure_case_btn_accept);
        mBtnAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mSpinnerOptions.getSelectedItem() == null) {
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.order_failure_incorrect), Toast.LENGTH_LONG).show();
                } else {
                    mStrFailureReason = mSpinnerOptions.getSelectedItem().toString();
                    mSpinnerOptions.setSelection(0);
                    callAsyncTaskFailed();
                    dialog.dismiss();
                }
            }
        });

        Button mBtnCancel = dialog.findViewById(R.id.dialog_failure_case_btn_cancel);
        mBtnCancel.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void inProgressDialog() {
        Log.d(DEBUG_TAG, getResources().getString(R.string.on_create));

        new FancyAlertDialog.Builder(this)
                .setTitle(getResources().getString(R.string.dialog_in_progress_title))
                .setBackgroundColor(getResources().getColor(R.color.amber))
                .setMessage(getResources().getString(R.string.dialog_in_progress_body))
                .setNegativeBtnText(getResources().getString(R.string.cancel))
                .setPositiveBtnBackground(getResources().getColor(R.color.amber))
                .setPositiveBtnText(getResources().getString(R.string.dialog_in_progress_accept))
                .setNegativeBtnBackground(getResources().getColor(R.color.grey_300))
                .setAnimation(Animation.SIDE)
                .isCancellable(false)
                .setIcon(R.drawable.icon_progress, Icon.Visible)
                .OnPositiveClicked(new FancyAlertDialogListener() {
                    @Override
                    public void OnClick() {
                        callAsyncTaskInProgress();
                    }
                })
                .OnNegativeClicked(new FancyAlertDialogListener() {
                    @Override
                    public void OnClick() {
                        mFabFailed.hide();
                        mFabFinished.hide();
                        mFabInProgress.show();
                    }
                })
                .build();
    }

    private boolean isEmpty(EditText etText) {
        return etText.getText().toString().trim().length() == 0;
    }

    @Override
    public void statusErrorResponse(String error) {
        Log.d(DEBUG_TAG, "Error response: " + error);
        Toast.makeText(this, "Error " + error, Toast.LENGTH_LONG).show();
    }

    @Override
    public void statusSuccessResponse(Order order) {
        UserPreferences userPreferences = new UserPreferences(this);
        User user = userPreferences.getUserObject();
        isClicked = true;
        checkButtons();
        String status = order.getOrderStatus();
        //  mStrCaseStatus= order.getOrderStatus();
        if (status.equals(this.getResources().getString(R.string.order_status_in_progress))) {
            //       getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            //     getSupportActionBar().setDisplayShowHomeEnabled(false);
            mCaseStatus.setTextColor(getResources().getColor(R.color.amber));
            if(!user.getUserType().equals(String.valueOf((R.string.user_type_supervisor)))){
            mFabInProgress.setVisibility(View.GONE);
            mFabFinished.setVisibility(View.VISIBLE);
            mFabFailed.setVisibility(View.VISIBLE);
            }else{
                mFabInProgress.setVisibility(View.GONE);
                mFabFinished.setVisibility(View.GONE);
                mFabFailed.setVisibility(View.GONE);
            }
            mFabWaze.setVisibility(View.VISIBLE);
            Toast.makeText(this, getResources().getString(R.string.order_in_progress), Toast.LENGTH_SHORT).show();
        } else if (status.equals(this.getResources().getString(R.string.order_status_finished))) {
            mCaseStatus.setTextColor(getResources().getColor(R.color.light_green));
            mFabInProgress.setVisibility(View.GONE);
            mFabFinished.setVisibility(View.GONE);
            mFabFailed.setVisibility(View.GONE);
            mFabWaze.setVisibility(View.GONE);
            mFabFinished.hide();
            mFabFailed.hide();
            mFabWaze.hide();
            mFabInProgress.hide();
            Toast.makeText(this, getResources().getString(R.string.order_finish_correct), Toast.LENGTH_SHORT).show();
        } else if (status.equals(this.getResources().getString(R.string.order_status_failed))) {
            mCaseStatus.setTextColor(getResources().getColor(R.color.red));
            mFabInProgress.setVisibility(View.GONE);
            mFabFinished.setVisibility(View.GONE);
            mFabFailed.setVisibility(View.GONE);
            mFabWaze.setVisibility(View.GONE);
            mFabFinished.hide();
            mFabFailed.hide();
            mFabWaze.hide();
            mFabInProgress.hide();
            Toast.makeText(this, getResources().getString(R.string.order_failure_correct), Toast.LENGTH_SHORT).show();
        } else if (status.equals(this.getResources().getString(R.string.order_status_reviewing))) {
            mFabInProgress.setVisibility(View.GONE);
            mFabFinished.setVisibility(View.GONE);
            mFabFailed.setVisibility(View.GONE);
            mFabWaze.setVisibility(View.GONE);

        }
        mCaseStatus.setText(order.getOrderStatus().toString());
    }
}


