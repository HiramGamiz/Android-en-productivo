package com.zgas.tesselar.myzuite.View.Activity.UserLeakage;

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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.shashank.sony.fancydialoglib.Animation;
import com.shashank.sony.fancydialoglib.FancyAlertDialog;
import com.shashank.sony.fancydialoglib.FancyAlertDialogListener;
import com.shashank.sony.fancydialoglib.Icon;
import com.zgas.tesselar.myzuite.Controller.Adapter.NothingSelectedSpinnerAdapter;
import com.zgas.tesselar.myzuite.Model.Leak;
import com.zgas.tesselar.myzuite.Model.User;
import com.zgas.tesselar.myzuite.R;
import com.zgas.tesselar.myzuite.Service.PutStatusLeakTask;
import com.zgas.tesselar.myzuite.Utilities.ExtrasHelper;
import com.zgas.tesselar.myzuite.Utilities.UserPreferences;

import org.json.JSONObject;

import java.text.SimpleDateFormat;

import butterknife.BindColor;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Class that shows the details of the leakages; it's used when the operator is of type 'Leak
 * technician'. In this class we can also modify the leak status - in progress, cancelled, finished -
 * and open the Waze app with the leak address.
 *
 * @author jarvizu
 * @version 2018.0.9
 * @see JSONObject
 * @see User
 * @see UserPreferences
 * @see Bundle
 * @see android.os.AsyncTask
 * @see PutStatusLeakTask
 */
public class DetailActivityLeakage extends AppCompatActivity implements
        PutStatusLeakTask.StatusLeakTaskListener {

    private final String DEBUG_TAG = getClass().getSimpleName();

    private String mStrLeakId;
    private String mStrLeakStatus;
    private String mStrLeakSubject;
    private String mStrLeakAddress;
    private String strCancellationReason;
    private String Ticket;
    private String resolution;
    private String channel;

    @BindView(R.id.activity_detail_leakage_tv_client_name)
    TextView mUserName;
    @BindView(R.id.activity_detail_leakage_tv_case_address)
    TextView mLeakAddress;
    @BindView(R.id.activity_detail_leakage_tv_status)
    TextView mLeakStatus;
    @BindView(R.id.activity_detail_leakage_tv_time_in)
    TextView mLeakTimeIn;
    @BindView(R.id.activity_detail_leakage_tv_time_seen)
    TextView mLeakTimeSeen;
    @BindView(R.id.activity_detail_leakage_tv_time_arrived)
    TextView mLeakTimeArrived;
    @BindView(R.id.activity_detail_leakage_tv_time_programmed)
    TextView mLeakTimeScheduled;
    @BindView(R.id.activity_detail_leakage_tv_capacity)
    TextView mLeakCylinderCapacity;
    @BindView(R.id.activity_detail_leakage_tv_color)
    TextView mLeakCylinderColor;
    @BindView(R.id.activity_detail_leakage_tv_channel)
    TextView mLeakChannel;
    @BindView(R.id.activity_detail_leakage_tv_leak_type)
    TextView mLeakType;
    @Nullable
    @BindView(R.id.activity_detail_leakage_fab_in_progress)
    FloatingActionButton mFabInProgress;
    @Nullable
    @BindView(R.id.activity_detail_leakage_fab_finished)
    FloatingActionButton mFabFinished;
    @Nullable
    @BindView(R.id.activity_detail_leakage_fab_cancel)
    FloatingActionButton mFabFailed;
    @Nullable
    @BindView(R.id.activity_detail_leakage_fab_waze)
    FloatingActionButton mFabWaze;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindColor(R.color.blue)
    int blue;
    @BindColor(R.color.amber)
    int amber;
    @BindColor(R.color.red)
    int red;
    @BindColor(R.color.light_green)
    int light_green;
    @BindColor(R.color.grey_300)
    int grey_300;

    private boolean isClicked = false;
    private JSONObject params;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_leakage);
        ButterKnife.bind(this);
        overridePendingTransition(R.anim.pull_in_right, R.anim.no_change);
        Log.d(DEBUG_TAG, getResources().getString(R.string.on_create));
        UserPreferences mUserPreferences = new UserPreferences(this);
        User mUser = mUserPreferences.getUserObject();
        initUi();
        checkButtons();
    }

    private void checkButtons() {
        if (mStrLeakStatus.equals(this.getResources().getString(R.string.order_status_in_progress))) {
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
        } else if (mStrLeakStatus.equals(this.getResources().getString(R.string.order_status_failed))) {
            mFabFinished.hide();
            mFabFailed.hide();
            mFabWaze.hide();
            mFabInProgress.hide();
        } else if (mStrLeakStatus.equals(this.getResources().getString(R.string.order_status_finished))) {
            mFabFinished.hide();
            mFabFailed.hide();
            mFabWaze.hide();
            mFabInProgress.hide();
        } else if (mStrLeakStatus.equals(this.getResources().getString(R.string.order_status_new))) {
            mFabFinished.hide();
            mFabFailed.hide();
            mFabWaze.hide();
            mFabInProgress.show();
        }
    }

    private void initUi() {
        setSupportActionBar(toolbar);
        SimpleDateFormat dateFormat = new SimpleDateFormat("d/MM/yyyy h:mm a");
        Bundle mBundle = getIntent().getExtras();
        mStrLeakId = mBundle.getString(ExtrasHelper.LEAK_JSON_OBJECT_ID);
        String mStrLeakClientName = mBundle.getString(ExtrasHelper.LEAK_JSON_OBJECT_WHO_REPORTS);
        String mStrLeakSubject = mBundle.getString(ExtrasHelper.LEAK_JSON_OBJECT_SUBJECT);
        String mStrLeakAddress = mBundle.getString(ExtrasHelper.LEAK_JSON_OBJECT_ADDRESS);
        mStrLeakStatus = mBundle.getString(ExtrasHelper.LEAK_JSON_OBJECT_STATUS);
        String mStrLeakType = mBundle.getString(ExtrasHelper.LEAK_JSON_OBJECT_SERVICE_TYPE);
        String mStrLeakPriority = mBundle.getString(ExtrasHelper.LEAK_JSON_OBJECT_PRIORITY);
        String mStrLeakTimeSeen = mBundle.getString(ExtrasHelper.LEAK_JSON_OBJECT_SEEN);
        String mStrLeakTimeArrived = mBundle.getString(ExtrasHelper.LEAK_JSON_OBJECT_DATE_END);
        String mStrLeakTimeScheduled = mBundle.getString(ExtrasHelper.LEAK_JSON_OBJECT_DATE_SCHEDULED);
        String mStrLeakTimeDeparture = mBundle.getString(ExtrasHelper.LEAK_JSON_OBJECT_DATE_DEPARTURE);
        String mStrLeakOperator = mBundle.getString(ExtrasHelper.LEAK_JSON_OBJECT_OPERATOR);
        String mStrLeakFolioSalesNote = mBundle.getString(ExtrasHelper.LEAK_JSON_OBJECT_SALES_NOTE);
        String mStrLeakTimeTechnician = mBundle.getString(ExtrasHelper.LEAK_JSON_OBJECT_DATE_TECHNICIAN);
        String mStrCylinderCapacity = mBundle.getString(ExtrasHelper.LEAK_JSON_OBJECT_CYLINDER_CAPACITY);
        String mStrCylinderColor = mBundle.getString(ExtrasHelper.LEAK_JSON_OBJECT_COLOR);
        String mStrChannel = mBundle.getString(ExtrasHelper.LEAK_JSON_OBJECT_CHANNEL);
        String mStrName = mBundle.getString(ExtrasHelper.LEAK_JSON_OBJECT_NAME);
        if (mStrChannel == null || mStrChannel.equals("") || mStrChannel.equals("null")) {
            mLeakChannel.setText(getResources().getString(R.string.no_data));
        } else {
            mLeakChannel.setText(mStrChannel);
        }


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle(this.getResources().getString(R.string.prompt_detail_leakage) + " " + mStrName);

        if (mStrCylinderCapacity == null || mStrCylinderCapacity.equals("") || mStrCylinderCapacity.equals("null")) {
            mLeakCylinderCapacity.setText(getResources().getString(R.string.no_data));
        } else {
            mLeakCylinderCapacity.setText(mStrCylinderCapacity);
        }

        if (mStrLeakType == null || mStrLeakType.equals("") || mStrLeakType.equals("null")) {
           // mLeakCylinderCapacity.setText(getResources().getString(R.string.no_data)); no debe ir
        } else {
         //   mLeakType.setText(mStrLeakType);
        }

        if (mStrCylinderColor == null || mStrCylinderColor.equals("") || mStrCylinderColor.equals("null")) {
            mLeakCylinderColor.setText(getResources().getString(R.string.no_data));
        } else {
            mLeakCylinderColor.setText(mStrCylinderColor);
        }

        if (mStrLeakAddress == null || mStrLeakAddress.equals("") || mStrLeakAddress.equals("null")) {
            mLeakAddress.setText(getResources().getString(R.string.no_data));
        } else {
            mLeakAddress.setText(mStrLeakAddress);
        }

        if (mStrLeakClientName == null || mStrLeakClientName.equals("") || mStrLeakClientName.equals("null")) {
            mUserName.setText(getResources().getString(R.string.no_data));
        } else {
            mUserName.setText(mStrLeakClientName);
        }

        if (mStrLeakStatus == null || mStrLeakStatus.equals("") || mStrLeakStatus.equals("null")) {
            mLeakStatus.setText(getResources().getString(R.string.no_data));
        } else {
            if (mStrLeakStatus.equals(this.getResources().getString(R.string.order_status_new))) {
                mLeakStatus.setTextColor(getResources().getColor(R.color.blue));
            } else if (mStrLeakStatus.equals(this.getResources().getString(R.string.order_status_in_progress))) {
                mLeakStatus.setTextColor(getResources().getColor(R.color.amber));
            } else if (mStrLeakStatus.equals(this.getResources().getString(R.string.order_status_failed))) {
                mLeakStatus.setTextColor(getResources().getColor(R.color.red));
            } else if (mStrLeakStatus.equals(this.getResources().getString(R.string.order_status_closed))) {
                mLeakStatus.setTextColor(getResources().getColor(R.color.light_green));
            } else {
                mLeakStatus.setTextColor(getResources().getColor(R.color.blue));
            }
            mLeakStatus.setText(mStrLeakStatus);
        }

        if (mStrLeakTimeTechnician == null || mStrLeakTimeTechnician.equals("") || mStrLeakTimeTechnician.equals("null")) {
            mLeakTimeIn.setText(getResources().getString(R.string.no_data));
        } else {
            mLeakTimeIn.setText(mStrLeakTimeTechnician);
        }

        if (mStrLeakTimeSeen == null || mStrLeakTimeSeen.equals("") || mStrLeakTimeSeen.equals("null")) {
            mLeakTimeSeen.setText(getResources().getString(R.string.no_data));
        } else {
            mLeakTimeSeen.setText(mStrLeakTimeSeen);
        }

        if (mStrLeakTimeArrived == null || mStrLeakTimeArrived.equals("") || mStrLeakTimeArrived.equals("null")) {
            mLeakTimeArrived.setText(getResources().getString(R.string.no_data));
        } else {
            mLeakTimeArrived.setText(mStrLeakTimeArrived);
        }

        if (mStrLeakTimeScheduled == null || mStrLeakTimeScheduled.equals("") || mStrLeakTimeScheduled.equals("null")) {
            mLeakTimeScheduled.setText(getResources().getString(R.string.no_data));
        } else {
            mLeakTimeScheduled.setText(mStrLeakTimeScheduled);
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
        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.no_change, R.anim.push_out_right);
    }

    @OnClick(R.id.activity_detail_leakage_fab_finished)
    public void onClickFinished() {
        finishDialog();
    }

    @OnClick(R.id.activity_detail_leakage_fab_in_progress)
    public void onClickProgress() {
        inProgressDialog();
    }

    @OnClick(R.id.activity_detail_leakage_fab_cancel)
    public void onClickCanceled() {
        cancelDialog();
    }

    @OnClick(R.id.activity_detail_leakage_fab_waze)
    public void onClickWaze() {
        wazeIntent(mStrLeakAddress);
    }

    private void callAsyncTaskInProgress() {
        params = new JSONObject();
        try {
            params.put(ExtrasHelper.LEAK_JSON_OBJECT_ID, mStrLeakId);
            params.put(ExtrasHelper.LEAK_JSON_OBJECT_STATUS, this.getResources().getString(R.string.order_status_in_progress));

            Log.d(DEBUG_TAG, "Status: " + params.getString(ExtrasHelper.LEAK_JSON_OBJECT_STATUS)
                    + " ID: " + params.getString(ExtrasHelper.LEAK_JSON_OBJECT_ID));

            PutStatusLeakTask putStatusLeakTask = new PutStatusLeakTask(this, params);
            putStatusLeakTask.setStatusLeakTaskListener(this);
            putStatusLeakTask.execute();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void callAsyncTaskFinished() {
        JSONObject params = new JSONObject();
        try {
            params.put(ExtrasHelper.LEAK_JSON_OBJECT_ID, mStrLeakId);
            params.put(ExtrasHelper.LEAK_JSON_OBJECT_STATUS, this.getResources().getString(R.string.order_status_closed));
            params.put(ExtrasHelper.LEAK_JSON_OBJECT_RESOLUTION_STATUS, resolution);
            params.put(ExtrasHelper.LEAJ_JSON_OBJECT_CHANNEL_STATUS, channel);

            PutStatusLeakTask putStatusLeakTask = new PutStatusLeakTask(this, params);
            putStatusLeakTask.setStatusLeakTaskListener(this);
            putStatusLeakTask.execute();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void callAsyncTaskCancelled() {
        params = new JSONObject();
        try {
            params.put(ExtrasHelper.LEAK_JSON_OBJECT_ID, mStrLeakId);
            params.put(ExtrasHelper.LEAK_JSON_OBJECT_STATUS, getResources().getString(R.string.order_status_failed));
            params.put(ExtrasHelper.ORDER_JSON_OBJECT_FAILURE_REASON, strCancellationReason);
            Log.d(DEBUG_TAG, "Status: " + params.getString(ExtrasHelper.LEAK_JSON_OBJECT_STATUS)
                    + " ID: " + params.getString(ExtrasHelper.LEAK_JSON_OBJECT_ID));

            PutStatusLeakTask putStatusLeakTask = new PutStatusLeakTask(this, params);
            putStatusLeakTask.setStatusLeakTaskListener(this);
            putStatusLeakTask.execute();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void wazeIntent(String address) {
        final String url = "waze://?q=" + address;
        final Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(intent);
    }

    private void inProgressDialog() {
        Log.d(DEBUG_TAG, "In progress dialog " + getResources().getString(R.string.on_create));

        new FancyAlertDialog.Builder(this)
                .setTitle(getResources().getString(R.string.dialog_in_progress_title_leak))
                .setBackgroundColor(amber)
                .setMessage(getResources().getString(R.string.dialog_in_progress_body_leak))
                .setNegativeBtnText(getResources().getString(R.string.cancel))
                .setPositiveBtnBackground(amber)
                .setPositiveBtnText(getResources().getString(R.string.dialog_in_progress_accept))
                .setNegativeBtnBackground(grey_300)
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

    private void finishDialog() {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_finish_case_leakage);
        dialog.getWindow().getAttributes().windowAnimations = R.style.Theme_Dialog_Animation;
        Log.d(DEBUG_TAG, "Finish dialog " + getResources().getString(R.string.on_create));
        dialog.setCancelable(false);

        final Spinner mSpinnerLeakChannel = dialog.findViewById(R.id.dialog_finish_case_leakage_sp_option);
        final Spinner mSpinnerLeakResolution = dialog.findViewById(R.id.dialog_finish_case_leakage_sp_second_option);
        mSpinnerLeakResolution.setVisibility(View.GONE);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.leakage_prompts,
                android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinnerLeakChannel.setAdapter(new NothingSelectedSpinnerAdapter(adapter, R.layout.contact_spinner_row_nothing_selected,
                this));
        mSpinnerLeakChannel.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                ArrayAdapter<CharSequence> innerAdapter;
                switch (i) {
                    case 1: //Cilindro con gas
                        mSpinnerLeakResolution.setVisibility(View.VISIBLE);
                        innerAdapter = ArrayAdapter.createFromResource(getBaseContext(), R.array.leakage_prompts_cilynder_gas,
                                android.R.layout.simple_spinner_item);
                        innerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        mSpinnerLeakResolution.setAdapter(innerAdapter);
                        break;
                    case 2: //Cilindro sin gas
                        mSpinnerLeakResolution.setVisibility(View.VISIBLE);
                        innerAdapter = ArrayAdapter.createFromResource(getBaseContext(), R.array.leakage_prompts_cilynder_no_gas,
                                android.R.layout.simple_spinner_item);
                        innerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        mSpinnerLeakResolution.setAdapter(innerAdapter);
                        break;
                    case 3: //Estacionario con gas
                        mSpinnerLeakResolution.setVisibility(View.VISIBLE);
                        innerAdapter = ArrayAdapter.createFromResource(getBaseContext(), R.array.leakage_prompts_stationary_gas,
                                android.R.layout.simple_spinner_item);
                        innerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        mSpinnerLeakResolution.setAdapter(innerAdapter);
                        break;
                    case 4: //Estacionario sin gas
                        mSpinnerLeakResolution.setVisibility(View.VISIBLE);
                        innerAdapter = ArrayAdapter.createFromResource(getBaseContext(), R.array.leakage_prompts_stationary_no_gas,
                                android.R.layout.simple_spinner_item);
                        innerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        mSpinnerLeakResolution.setAdapter(innerAdapter);
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        Button mBtnAccept = dialog.findViewById(R.id.dialog_finish_case_leakage_btn_accept);
        mBtnAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mSpinnerLeakChannel.getSelectedItem() == null || mSpinnerLeakResolution.getSelectedItem() == null) {
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.leak_finish_incorrect), Toast.LENGTH_LONG).show();
                } else {
                    channel = mSpinnerLeakChannel.getSelectedItem().toString();
                    resolution = mSpinnerLeakResolution.getSelectedItem().toString();
                    Log.d(DEBUG_TAG, resolution);
                    Log.d(DEBUG_TAG, channel);
                    callAsyncTaskFinished();
                    dialog.dismiss();
                }
            }
        });

        Button mBtnCancel = dialog.findViewById(R.id.dialog_finish_case_leakage_btn_cancel);
        mBtnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();

    }

    private void cancelDialog() {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_failure_case_leakage);
        dialog.getWindow().getAttributes().windowAnimations = R.style.Theme_Dialog_Animation;
        Log.d(DEBUG_TAG, "Cancel dialog " + getResources().getString(R.string.on_create));
        dialog.setCancelable(false);

        TextView mTextTitle = dialog.findViewById(R.id.dialog_cancel_case_leakage_title);
        mTextTitle.setText(getResources().getString(R.string.dialog_failure_title_leak));

        TextView mTextBody = dialog.findViewById(R.id.dialog_cancel_case_leakage_body);
        mTextBody.setText(getResources().getString(R.string.dialog_failure_body_leak));

        final Spinner mSpinnerOptions = dialog.findViewById(R.id.dialog_failure_case_leakage_sp_option);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.failure_prompts, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinnerOptions.setAdapter(new NothingSelectedSpinnerAdapter(adapter, R.layout.contact_spinner_row_nothing_selected, this));

        Button mBtnAccept = dialog.findViewById(R.id.dialog_failure_case_leakage_btn_accept);
        mBtnAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mSpinnerOptions.getSelectedItem() == null) {
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.leak_failure_incorrect), Toast.LENGTH_LONG).show();
                } else {
                    strCancellationReason = mSpinnerOptions.getSelectedItem().toString();
                    mSpinnerOptions.setSelection(0);
                    callAsyncTaskCancelled();
                    dialog.dismiss();
                }
            }
        });

        Button mBtnCancel = dialog.findViewById(R.id.dialog_failure_case_leakage_btn_cancel);
        mBtnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    @Override
    public void statusErrorResponse(String error) {
        Log.d(DEBUG_TAG, "Error response: " + error);
        Toast.makeText(this, "Error " + error, Toast.LENGTH_LONG).show();
    }

    @Override
    public void statusSuccessResponse(Leak leak) {
        isClicked = true;
        checkButtons();

        String status = leak.getLeakStatus();

        if (status.equals(this.getResources().getString(R.string.order_status_in_progress))) {
            UserPreferences mUserPreferences = new UserPreferences(this);
            User mUser = mUserPreferences.getUserObject();
            mLeakStatus.setTextColor(getResources().getColor(R.color.amber));
            if(!mUser.getUserType().equals(String.valueOf((R.string.user_type_supervisor)))) {
                mFabInProgress.setVisibility(View.GONE);
                mFabFinished.setVisibility(View.VISIBLE);
                mFabFailed.setVisibility(View.VISIBLE);
            }else{
                mFabInProgress.setVisibility(View.GONE);
                mFabFinished.setVisibility(View.GONE);
                mFabFailed.setVisibility(View.GONE);
            }
            mFabWaze.setVisibility(View.VISIBLE);
            Toast.makeText(this, getResources().getString(R.string.leak_in_progress), Toast.LENGTH_SHORT).show();
        } else if (status.equals(this.getResources().getString(R.string.order_status_closed))) {
            mLeakStatus.setTextColor(getResources().getColor(R.color.light_green));
            mFabInProgress.setVisibility(View.GONE);
            mFabFinished.setVisibility(View.GONE);
            mFabFailed.setVisibility(View.GONE);
            mFabWaze.setVisibility(View.GONE);
            Toast.makeText(this, getResources().getString(R.string.leak_finish_correct), Toast.LENGTH_SHORT).show();
        } else if (status.equals(this.getResources().getString(R.string.order_status_failed))) {
            mLeakStatus.setTextColor(getResources().getColor(R.color.red));
            mFabInProgress.setVisibility(View.GONE);
            mFabFinished.setVisibility(View.GONE);
            mFabFailed.setVisibility(View.GONE);
            mFabWaze.setVisibility(View.GONE);
            Toast.makeText(this, getResources().getString(R.string.leak_failure_correct), Toast.LENGTH_SHORT).show();
        } else if (status.equals(this.getResources().getString(R.string.order_status_reviewing))) {
            mFabInProgress.setVisibility(View.GONE);
            mFabFinished.setVisibility(View.GONE);
            mFabFailed.setVisibility(View.GONE);
            mFabWaze.setVisibility(View.GONE);
        }
        mLeakStatus.setText(leak.getLeakStatus());
        mLeakTimeArrived.setText(leak.getLeakTimeEnd());
    }
}
