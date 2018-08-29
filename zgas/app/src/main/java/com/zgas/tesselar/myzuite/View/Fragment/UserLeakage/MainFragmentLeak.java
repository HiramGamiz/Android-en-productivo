package com.zgas.tesselar.myzuite.View.Fragment.UserLeakage;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.dinuscxj.refresh.RecyclerRefreshLayout;
import com.zgas.tesselar.myzuite.Controller.Adapter.LeaksAdapter;
import com.zgas.tesselar.myzuite.Model.Leak;
import com.zgas.tesselar.myzuite.Model.User;
import com.zgas.tesselar.myzuite.R;
import com.zgas.tesselar.myzuite.Service.GetLeakagesTask;
import com.zgas.tesselar.myzuite.Utilities.UserPreferences;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * This class shows the list of all the leakages
 *
 * @author jarvizu
 * @version 2018.0.9
 * @see JSONObject
 * @see User
 * @see Leak
 * @see UserPreferences
 * @see android.os.AsyncTask
 * @see GetLeakagesTask
 * @see RecyclerRefreshLayout
 * @see LeaksAdapter
 * @see ButterKnife
 */
public class MainFragmentLeak extends Fragment implements GetLeakagesTask.LeakagesTaskListener {

    private final String DEBUG_TAG = getClass().getSimpleName();
    private static final int REFRESH_DELAY = 1000;

    @BindView(R.id.fragment_main_leak_recycler_view)
    RecyclerView mRecyclerOrders;
    @BindView(R.id.fragment_main_leakage_refresh_layout)
    RecyclerRefreshLayout mRecyclerRefreshLayout;
    private UserPreferences mUserPreferences;
    private static final String USER_ID = "Id";
    private static final String ADMIN_TOKEN = "access_token";
    private LinearLayoutManager linearLayoutManager;
    private Unbinder unbinder;

    public MainFragmentLeak() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View mRootView = inflater.inflate(R.layout.fragment_main_leak, container, false);
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

    @Override
    public void onStart() {
        super.onStart();
        asyncTask();
    }

    @Override
    public void onResume() {
        super.onResume();
        asyncTask();
    }

    private void asyncTask() {
        try {
            JSONObject params = new JSONObject();
            params.put(USER_ID, mUserPreferences.getUserObject().getUserId());
            params.put(ADMIN_TOKEN, mUserPreferences.getAdminToken());
            Log.d(DEBUG_TAG, "Parámetros: " + params.getString(USER_ID) + " " + params.getString(ADMIN_TOKEN));
            GetLeakagesTask getLeakagesTask = new GetLeakagesTask(getContext(), params);
            getLeakagesTask.setLeakagesTaskListener(this);
            getLeakagesTask.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initUi() {
        linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        mRecyclerRefreshLayout.setOnRefreshListener(new RecyclerRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mRecyclerRefreshLayout.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mRecyclerRefreshLayout.setRefreshing(false);
                        asyncTask();
                    }
                }, REFRESH_DELAY);
            }
        });
    }

    @Override
    public void getLeakagesErrorResponse(String error) {
        Log.d(DEBUG_TAG, error);
        Toast.makeText(getContext(), error, Toast.LENGTH_LONG).show();
    }

    @Override
    public void getLeakagesSuccessResponse(List<Leak> leakList) {
        LeaksAdapter leaksAdapter = new LeaksAdapter(getActivity(), (ArrayList<Leak>) leakList);
        mRecyclerOrders.setHasFixedSize(true);
        mRecyclerOrders.setItemViewCacheSize(20);
        mRecyclerOrders.setDrawingCacheEnabled(true);
        mRecyclerOrders.setLayoutManager(linearLayoutManager);
        mRecyclerOrders.setAdapter(leaksAdapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
