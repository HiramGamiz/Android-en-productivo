package com.zgas.tesselar.myzuite.View.Activity.UserSupervisor;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import com.zgas.tesselar.myzuite.Model.User;
import com.zgas.tesselar.myzuite.R;
import com.zgas.tesselar.myzuite.Service.GetUserInfoTask;
import com.zgas.tesselar.myzuite.Utilities.ExtrasHelper;
import com.zgas.tesselar.myzuite.Utilities.UserPreferences;

import org.json.JSONObject;

import butterknife.BindColor;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Class that shows the details of all the users that a supervisor has under its care. This will show
 * if the user is of type 'supervisor'
 *
 * @author jarvizu
 * @version 2018.0.9
 * @see JSONObject
 * @see User
 * @see UserPreferences
 * @see Bundle
 * @see android.os.AsyncTask
 * @see GetUserInfoTask
 */
public class DetailActivitySupervisor extends AppCompatActivity {

    private final String DEBUG_TAG = getClass().getSimpleName();

    @BindView(R.id.activity_detail_supervisor_tv_user_id)
    TextView mUserId;
    @BindView(R.id.activity_detail_supervisor_tv_user_name)
    TextView mUserName;
    @BindView(R.id.activity_detail_supervisor_tv_user_email)
    TextView mUserEmail;
    @BindView(R.id.activity_detail_supervisor_tv_user_route)
    TextView mUserRoute;
    @BindView(R.id.activity_detail_supervisor_tv_user_zone)
    TextView mUserZone;
    @BindView(R.id.activity_detail_supervisor_tv_user_status)
    TextView mUserStatus;
    @BindView(R.id.activity_detail_supervisor_tv_user_type)
    TextView mUserType;

    @BindColor(R.color.light_green)
    int light_green;
    @BindColor(R.color.red)
    int red;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_supervisor);
        ButterKnife.bind(this);
        overridePendingTransition(R.anim.pull_in_right, R.anim.no_change);
        Log.d(DEBUG_TAG, getResources().getString(R.string.on_create));
        UserPreferences mUserPreferences = new UserPreferences(this);
        User mUser = mUserPreferences.getUserObject();
        initUi();
    }

    private void initUi() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Bundle mBundle = getIntent().getExtras();
        String mStrUserId = mBundle.getString(ExtrasHelper.EXTRA_USER_ID);
        String mStrUserName = mBundle.getString(ExtrasHelper.EXTRA_USER_NAME);
        String mStrUserEmail = mBundle.getString(ExtrasHelper.EXTRA_USER_EMAIL);
        String mStrUserRoute = mBundle.getString(ExtrasHelper.EXTRA_USER_ROUTE);
        String mStrUserZone = mBundle.getString(ExtrasHelper.EXTRA_USER_ZONE);
        String mStrUserStatus = mBundle.getString(ExtrasHelper.EXTRA_USER_STATUS);
        String mStrUserType = mBundle.getString(ExtrasHelper.EXTRA_USER_TYPE);

        getSupportActionBar().setTitle("Detalles de " + mStrUserName);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        if (mStrUserId != null) {
            mUserId.setText(String.valueOf(mStrUserId));
        } else {
            mUserId.setText(getResources().getString(R.string.no_data));
        }

        if (mStrUserName != null) {
            mUserName.setText(mStrUserName);
        } else {
            mUserName.setText(getResources().getString(R.string.no_data));
        }

        if (mStrUserEmail != null) {
            mUserEmail.setText(mStrUserEmail);
        } else {
            mUserEmail.setText(getResources().getString(R.string.no_data));
        }

        if (mStrUserRoute != null) {
            mUserRoute.setText(mStrUserRoute);
        } else {
            mUserRoute.setText(getResources().getString(R.string.no_data));
        }

        if (mStrUserZone != null) {
            mUserZone.setText(mStrUserZone);
        } else {
            mUserZone.setText(getResources().getString(R.string.no_data));
        }

        if (mStrUserStatus != null) {
            if (mStrUserStatus.equals(this.getResources().getString(R.string.user_active))) {
                mUserStatus.setTextColor(light_green);
                mUserStatus.setText(String.valueOf(mStrUserStatus));
            } else if (mStrUserStatus.equals(this.getResources().getString(R.string.user_not_active))) {
                mUserStatus.setTextColor(red);
                mUserStatus.setText(String.valueOf(mStrUserStatus));
            }
        } else {
            mUserStatus.setText(getResources().getString(R.string.no_data));
        }


        if (mStrUserType != null) {
            mUserType.setText(String.valueOf(mStrUserType));
        } else {
            mUserType.setText(getResources().getString(R.string.no_data));
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
}
