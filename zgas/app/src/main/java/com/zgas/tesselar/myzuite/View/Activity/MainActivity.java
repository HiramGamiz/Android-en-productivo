package com.zgas.tesselar.myzuite.View.Activity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;
import com.zgas.tesselar.myzuite.Controller.Adapter.PagerAdapter;
import com.zgas.tesselar.myzuite.Controller.Adapter.SupervisorAdapter;
import com.zgas.tesselar.myzuite.Model.User;
import com.zgas.tesselar.myzuite.R;
import com.zgas.tesselar.myzuite.Service.GetUserInfoTask;
import com.zgas.tesselar.myzuite.Utilities.CustomViewPager;
import com.zgas.tesselar.myzuite.Utilities.ExtrasHelper;
import com.zgas.tesselar.myzuite.Utilities.UserPreferences;
import com.zgas.tesselar.myzuite.View.Fragment.UserLeakage.HelpFragmentLeak;
import com.zgas.tesselar.myzuite.View.Fragment.UserLeakage.MainFragmentLeak;
import com.zgas.tesselar.myzuite.View.Fragment.UserOperator.ExtraOrderFragmentOperator;
import com.zgas.tesselar.myzuite.View.Fragment.UserOperator.HelpFragmentOperator;
import com.zgas.tesselar.myzuite.View.Fragment.UserOperator.MainFragmentOperator;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindColor;
import butterknife.BindDrawable;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Optional;

/**
 * Class that manages all the main fragments to be used by the application, it also separates them
 * according to the userType of the user that's logged in.
 *
 * @author jarvizu
 * @version 2018.0.9
 * @see JSONObject
 * @see User
 * @see UserPreferences
 * @see Bundle
 * @see ButterKnife
 * @see android.os.AsyncTask
 * @see GetUserInfoTask
 * @see MainFragmentLeak
 * @see HelpFragmentLeak
 * @see MainFragmentOperator
 * @see HelpFragmentOperator
 * @see ExtraOrderFragmentOperator
 * @see CustomViewPager
 * @see PagerAdapter
 * @see SupervisorAdapter
 * @see RecyclerView
 * @see LinearLayoutManager
 * @see AHBottomNavigation
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener, GetUserInfoTask.UserInfoListener {

    private final String DEBUG_TAG = getClass().getSimpleName();

    @Nullable
    @BindView(R.id.activity_main_cv_bottom_navigation)
    AHBottomNavigation ahBottomNavigation;
    @Nullable
    @BindView(R.id.activity_main_cv_view_pager)
    CustomViewPager customViewPager;
    @Nullable
    @BindView(R.id.activity_main_fab_call)
    FloatingActionButton mFabCall;
    @Nullable
    @BindView(R.id.ativity_supervisor_fab_call)
    FloatingActionButton mFabCallSupervisor;
    @Nullable
    @BindView(R.id.activity_supervisor_recycler_view)
    RecyclerView mRecyclerViewSupervised;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    private PagerAdapter mPagerAdapter;
    private UserPreferences userPreferences;
    private LinearLayoutManager linearLayoutManager;

    @BindColor(R.color.pink_500)
    int pink_500;
    @BindColor(R.color.pink_50)
    int pink_50;
    @BindColor(R.color.white)
    int white;

    @BindDrawable(R.drawable.icon_check)
    Drawable icon_check;
    @BindDrawable(R.drawable.icon_help)
    Drawable icon_help;
    @BindDrawable(R.drawable.icon_gas_cylinder_menu)
    Drawable icon_gas_cylinder_menu;
    @BindDrawable(R.drawable.icon_truck_fast)
    Drawable icon_truck_fast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userPreferences = new UserPreferences(this);
        User user = userPreferences.getUserObject();
        /*If a user is logged in, it will check it's type so the app will show the correct
        user interface.*/
        if (userPreferences.isLoggedIn()) {
            try{
            if (user.getUserType().equals(this.getResources().getString(R.string.user_type_operator))) {
                Log.d(DEBUG_TAG, "User operator " + getResources().getString(R.string.on_create));
                setContentView(R.layout.activity_main);
                ButterKnife.bind(this);
                initUiOperator();
            } else if (user.getUserType().equals(this.getResources().getString(R.string.user_type_supervisor))) {
                Log.d(DEBUG_TAG, "User supervisor " + getResources().getString(R.string.on_create));
                setContentView(R.layout.activity_supervisor);
                ButterKnife.bind(this);
                getSupervisedCallAsyncTask();
                initUiSupervisor();
            } else if (user.getUserType().equals(this.getResources().getString(R.string.user_type_technician))) {
                Log.d(DEBUG_TAG, "User leakages " + getResources().getString(R.string.on_create));
                setContentView(R.layout.activity_main);
                ButterKnife.bind(this);
                initUiLeakage();
            } else {
                Log.d(DEBUG_TAG, "Default " + getResources().getString(R.string.on_create));
                setContentView(R.layout.activity_main);
                ButterKnife.bind(this);
                initUiOperator();
            }
        }catch(Exception  e){
            userPreferences.logoutUser();
        }
    }
    }

    @Optional
    @OnClick({R.id.activity_main_fab_call, R.id.ativity_supervisor_fab_call})
    public void onClick(View view) {
        Log.d(DEBUG_TAG, "Butterknife onClick");
        callIntent();
    }

    private void initUiOperator() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.prompt_main_fragment);
        customViewPager.setPagingEnabled(false);
        HelpFragmentOperator mHelpFragmentOperator = new HelpFragmentOperator();
        MainFragmentOperator mMainFragmentOperator = new MainFragmentOperator();
        ExtraOrderFragmentOperator mExtraOrderFragmentOperator = new ExtraOrderFragmentOperator();
        mPagerAdapter = new PagerAdapter(getSupportFragmentManager());
        mPagerAdapter.addFragment(mExtraOrderFragmentOperator);
        mPagerAdapter.addFragment(mMainFragmentOperator);
        mPagerAdapter.addFragment(mHelpFragmentOperator);
        customViewPager.setAdapter(mPagerAdapter);
        initBottomNavigationOperator();
    }

    private void initUiSupervisor() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.prompt_supervised);
        linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
    }

    private void initUiLeakage() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.prompt_order_leak);
        customViewPager.setPagingEnabled(false);
        MainFragmentLeak mMainFragmentLeak = new MainFragmentLeak();
        HelpFragmentLeak mHelpFragmentLeak = new HelpFragmentLeak();
        mPagerAdapter = new PagerAdapter(getSupportFragmentManager());
        mPagerAdapter.addFragment(mMainFragmentLeak);
        mPagerAdapter.addFragment(mHelpFragmentLeak);
        customViewPager.setAdapter(mPagerAdapter);
        initBottomNavigationLeakage();
    }

    private void initBottomNavigationOperator() {
        AHBottomNavigationItem item1 = new AHBottomNavigationItem(getResources().getString(R.string.prompt_order_fragment), icon_check, pink_500);
        AHBottomNavigationItem item2 = new AHBottomNavigationItem(getResources().getString(R.string.prompt_main_fragment), icon_gas_cylinder_menu, pink_500);
        AHBottomNavigationItem item3 = new AHBottomNavigationItem(getResources().getString(R.string.prompt_help_fragment), icon_help, pink_500);

        ahBottomNavigation.addItem(item1);
        ahBottomNavigation.addItem(item2);
        ahBottomNavigation.addItem(item3);

        ahBottomNavigation.setDefaultBackgroundColor(white);
        ahBottomNavigation.setBehaviorTranslationEnabled(false);
        ahBottomNavigation.setBehaviorTranslationEnabled(false);
        ahBottomNavigation.setAccentColor(pink_500);
        ahBottomNavigation.setInactiveColor(pink_50);
        ahBottomNavigation.setForceTint(true);
        ahBottomNavigation.setTranslucentNavigationEnabled(true);
        ahBottomNavigation.setTitleState(AHBottomNavigation.TitleState.SHOW_WHEN_ACTIVE);
        ahBottomNavigation.setCurrentItem(1);
        customViewPager.setCurrentItem(1);
        customViewPager.addOnPageChangeListener(new CustomViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                animateFabOperator(position);
                switch (position) {
                    case 0:
                        getSupportActionBar().setTitle(R.string.prompt_order_fragment);
                        break;
                    case 1:
                        getSupportActionBar().setTitle(R.string.prompt_main_fragment);
                        break;
                    case 2:
                        getSupportActionBar().setTitle(R.string.prompt_help_fragment);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        ahBottomNavigation.setOnTabSelectedListener(new AHBottomNavigation.OnTabSelectedListener() {
            @Override
            public boolean onTabSelected(int position, boolean wasSelected) {
                if (position == 0) {
                    customViewPager.setCurrentItem(0);
                } else if (position == 1) {
                    customViewPager.setCurrentItem(1);
                } else if (position == 2) {
                    customViewPager.setCurrentItem(2);
                }
                return true;
            }
        });
        ahBottomNavigation.setOnNavigationPositionListener(new AHBottomNavigation.OnNavigationPositionListener() {
            @Override
            public void onPositionChange(int y) {
                Log.d(DEBUG_TAG, "BottomNavigation Position: " + y);
            }
        });
    }

    private void initBottomNavigationLeakage() {
        AHBottomNavigationItem item1 = new AHBottomNavigationItem(getResources().getString(R.string.prompt_order_leak), icon_truck_fast, pink_500);
        AHBottomNavigationItem item2 = new AHBottomNavigationItem(getResources().getString(R.string.prompt_help_fragment), icon_help, pink_500);

        ahBottomNavigation.addItem(item1);
        ahBottomNavigation.addItem(item2);

        ahBottomNavigation.setDefaultBackgroundColor(white);
        ahBottomNavigation.setBehaviorTranslationEnabled(false);
        ahBottomNavigation.setBehaviorTranslationEnabled(false);
        ahBottomNavigation.setAccentColor(pink_500);
        ahBottomNavigation.setInactiveColor(pink_50);
        ahBottomNavigation.setForceTint(true);
        ahBottomNavigation.setTranslucentNavigationEnabled(true);
        ahBottomNavigation.setTitleState(AHBottomNavigation.TitleState.ALWAYS_SHOW);
        ahBottomNavigation.setCurrentItem(0);
        customViewPager.setCurrentItem(0);
        customViewPager.addOnPageChangeListener(new CustomViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                animateFab(position);
                switch (position) {
                    case 0:
                        getSupportActionBar().setTitle(R.string.prompt_order_leak);
                        break;
                    case 1:
                        getSupportActionBar().setTitle(R.string.prompt_help_fragment);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        ahBottomNavigation.setOnTabSelectedListener(new AHBottomNavigation.OnTabSelectedListener() {
            @Override
            public boolean onTabSelected(int position, boolean wasSelected) {
                if (position == 0) {
                    customViewPager.setCurrentItem(0);
                } else if (position == 1) {
                    customViewPager.setCurrentItem(1);
                }
                return true;
            }
        });
        ahBottomNavigation.setOnNavigationPositionListener(new AHBottomNavigation.OnNavigationPositionListener() {
            @Override
            public void onPositionChange(int y) {
                Log.d(DEBUG_TAG, "BottomNavigation Position: " + y);
            }
        });
    }

    private void animateFab(int position) {
        switch (position) {
            case 0:
                mFabCall.show();
                break;
            case 1:
                mFabCall.hide();
                break;
            default:
                mFabCall.hide();
                break;
        }
    }

    private void animateFabOperator(int position) {
        switch (position) {
            case 0:
                mFabCall.hide();
                break;
            case 1:
                mFabCall.show();
                break;
            default:
                mFabCall.hide();
                break;
        }
    }

    private void callIntent() {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse(ExtrasHelper.CALL));
        startActivity(intent);
    }

    private void getSupervisedAsyncTask() {
        try {
            JSONObject params = new JSONObject();
            params.put(ExtrasHelper.EMAIL_TAG, userPreferences.getLoginObject().getLoginEmail());
            params.put(ExtrasHelper.ADMIN_TOKEN, userPreferences.getAdminToken());
            Log.d(DEBUG_TAG, "Parámetros: " + params.getString(ExtrasHelper.EMAIL_TAG) + " " + params.getString(ExtrasHelper.ADMIN_TOKEN));
            GetUserInfoTask getUserInfoTask = new GetUserInfoTask(this, params);
            getUserInfoTask.setUserInfoListener(this);
            getUserInfoTask.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getSupervisedCallAsyncTask() {
        try {
            getSupervisedAsyncTask();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.logout_menu:
                userPreferences.logoutUser();
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void userInfoErrorResponse(String error) {
        Log.d(DEBUG_TAG, "Error response: " + error);
        Toast.makeText(this, "Error " + error, Toast.LENGTH_LONG).show();
    }

    @Override
    public void userInfoSuccessResponse(User user) {
        Log.d(DEBUG_TAG, "User preference id: " + userPreferences.getUserObject().getUserId());
        Log.d(DEBUG_TAG, "User preference name: " + userPreferences.getUserObject().getUserName());
        Log.d(DEBUG_TAG, "User preference type: " + userPreferences.getUserObject().getUserType());
        Log.d(DEBUG_TAG, "User preference email: " + userPreferences.getUserObject().getUserEmail());
        Log.d(DEBUG_TAG, "User preference zone: " + userPreferences.getUserObject().getUserZone());
        Log.d(DEBUG_TAG, "User preference route: " + userPreferences.getUserObject().getUserRoute());
        Log.d(DEBUG_TAG, "User preference status: " + userPreferences.getUserObject().getUserStatus());
    }

    @Override
    public void userSupervisedSuccessResponse(List<User> userList) {
        SupervisorAdapter mSupervisorAdapter = new SupervisorAdapter(this, (ArrayList<User>) userList);
        mRecyclerViewSupervised.setLayoutManager(linearLayoutManager);
        mRecyclerViewSupervised.setAdapter(mSupervisorAdapter);
        mRecyclerViewSupervised.setHasFixedSize(true);
        mRecyclerViewSupervised.setItemViewCacheSize(20);
        mRecyclerViewSupervised.setDrawingCacheEnabled(true);
    }
}
