package com.zgas.tesselar.myzuite.View.Fragment.UserOperator;


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
import com.zgas.tesselar.myzuite.Controller.Adapter.OrdersAdapter;
import com.zgas.tesselar.myzuite.Model.Case;
import com.zgas.tesselar.myzuite.Model.Order;
import com.zgas.tesselar.myzuite.Model.User;
import com.zgas.tesselar.myzuite.R;
import com.zgas.tesselar.myzuite.Service.GetOrdersTask;
import com.zgas.tesselar.myzuite.Service.GetOrdersTaskSupervisor;
import com.zgas.tesselar.myzuite.Utilities.UserPreferences;

import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * This class shows the list of all the orders of type 'order'
 *
 * @author jarvizu
 * @version 2018.0.9
 * @see JSONObject
 * @see User
 * @see Order
 * @see UserPreferences
 * @see android.os.AsyncTask
 * @see GetOrdersTask
 * @see GetOrdersTaskSupervisor
 * @see RecyclerRefreshLayout
 * @see OrdersAdapter
 * @see ButterKnife
 */

public class MainFragmentOperator extends Fragment implements GetOrdersTask.OrderTaskListener,GetOrdersTaskSupervisor.OrderTaskListener {/*
        , OrdersAdapter.OrdersAdapterListener {*/

    private final String DEBUG_TAG = getClass().getSimpleName();
    private static final String USER_ID = "Id";
    private static final String ADMIN_TOKEN = "access_token";
    private static final int REFRESH_DELAY = 1000;

    @BindView(R.id.fragment_main_operator_recycler_view)
    RecyclerView mRecyclerOrders;
    @BindView(R.id.fragment_main_operator_refresh_layout)
    RecyclerRefreshLayout mRecyclerRefreshLayout;
    private UserPreferences mUserPreferences;
    LinearLayoutManager layoutManager;
    private Unbinder unbinder;
    private ArrayList<Case> globalOrderList;
    private OrdersAdapter mOrderAdapter;

    public MainFragmentOperator() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(DEBUG_TAG, getResources().getString(R.string.on_create));
        View mRootView = inflater.inflate(R.layout.fragment_main_operator, container, false);
        unbinder = ButterKnife.bind(this, mRootView);
        mUserPreferences = new UserPreferences(getContext());

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
        super.onResume();  // Always call the superclass method first
        asyncTask();
    }

    private void initUi() {
        layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
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

    private void asyncTask() {
        try {
            JSONObject params = new JSONObject();
            params.put(USER_ID, mUserPreferences.getUserObject().getUserId());
            params.put(ADMIN_TOKEN, mUserPreferences.getAdminToken());

            User AuxUser= new User();
            AuxUser=mUserPreferences.getUserObject();
            if(AuxUser.getUserType().equals((String.valueOf(R.string.user_type_supervisor)))){
                GetOrdersTaskSupervisor getOrdersTask = new GetOrdersTaskSupervisor(getContext(), params);
                getOrdersTask.setOrderTaskListener(this);
                getOrdersTask.execute();
            }else{
                GetOrdersTask getOrdersTask = new GetOrdersTask(getContext(), params);
                getOrdersTask.setOrderTaskListener(this);
                getOrdersTask.execute();
            }

        } catch (Exception e) {
           e.printStackTrace();
            //String error=e.getMessage().toString();
        }
    }

    @Override
    public void getCasesErrorResponse(String error) {
        Toast.makeText(getContext(), error, Toast.LENGTH_LONG).show();
    }

    @Override
    public void getCasesSuccessResponse(ArrayList<Case> orderList) {
        this.globalOrderList = orderList;
        mOrderAdapter = new OrdersAdapter(getContext(), globalOrderList);
        mRecyclerOrders.setHasFixedSize(true);
        mRecyclerOrders.setItemViewCacheSize(20);
        mRecyclerOrders.setDrawingCacheEnabled(true);
        mRecyclerOrders.setLayoutManager(layoutManager);
        mRecyclerOrders.setAdapter(mOrderAdapter);
        //mOrderAdapter.setOrdersAdapterListener(this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
/*
    @Override
    public void setSeenTime(Order order) {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("d/MM/yyyy h:mm a");
        String date = dateFormat.format(calendar.getTime());
        order.setOrderTimeSeen(date);
        Log.d(DEBUG_TAG, "PORFAVORFUNCIONA: " + order.getOrderId() + "" + order.getOrderTimeSeen());
    }*/
}
