package com.zgas.tesselar.myzuite.Controller.Adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zgas.tesselar.myzuite.Model.User;
import com.zgas.tesselar.myzuite.R;
import com.zgas.tesselar.myzuite.Utilities.ExtrasHelper;
import com.zgas.tesselar.myzuite.View.Activity.UserSupervisor.DetailActivitySupervisor;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Class that provides access to the User model data items. This class is parameterized with the
 * SupervisorViewHolder, because it does not need a SwiperAdapter like the other adapters.
 *
 * @author jarvizu on 04/09/2017
 * @version 2018.0.9
 * @see User
 * @see ButterKnife
 * @see RecyclerView
 * @see SupervisedViewHolder
 */
public class SupervisorAdapter extends RecyclerView.Adapter<SupervisorAdapter.SupervisedViewHolder> {

    private final String DEBUG_TAG = getClass().getSimpleName();
    private Context context;
    private ArrayList<User> mSupervisedList;

    /**
     * Constructor for the SupervisorAdapter class
     *
     * @param context         Current context of the application.
     * @param mSupervisedList List that contains all the items (users) that will display on the
     *                        RecyclerView.
     */
    public SupervisorAdapter(Context context, ArrayList<User> mSupervisedList) {
        this.context = context;
        this.mSupervisedList = mSupervisedList;
    }

    /**
     * Method for initializing the viewholders, inflates the RowSupervised layout.
     *
     * @param parent   The ViewGroup into which the new View will be added after it is bound to an
     *                 adapter position.
     * @param viewType The type of the new view.
     * @return SupervisorAdapter view
     */
    @Override
    public SupervisedViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater li = LayoutInflater.from(parent.getContext());
        View v = li.inflate(R.layout.row_activity_supervisors_my_supervised, parent, false);
        return new SupervisorAdapter.SupervisedViewHolder(v);
    }

    /**
     * Method that displays the data at an specified position. It Updates the contents of the
     * itemView.
     *
     * @param holder   The ViewHolder which contents should be updated to represent an item
     *                 depending it's position.
     * @param position The position of the item within the data set.
     * @see SupervisedViewHolder
     */
    @Override
    public void onBindViewHolder(SupervisedViewHolder holder, int position) {
        User mUser = mSupervisedList.get(position);
        String id = mUser.getUserId();
        String userName = mUser.getUserName();
        String userStatus = mUser.getUserStatus();
        TextView name = holder.supervisedName;
        TextView status = holder.supervisedStatus;

        if (userName == null) {
            name.setText(context.getString(R.string.no_data));
        } else {
            name.setText(userName);
        }

        if (userStatus.equals(context.getResources().getString(R.string.user_not_active))) {
            status.setTextColor(context.getResources().getColor(R.color.red));
            status.setText(String.valueOf(userStatus));
        } else if (userStatus.equals(context.getResources().getString(R.string.user_active))) {
            status.setTextColor(context.getResources().getColor(R.color.light_green));
            status.setText(String.valueOf(userStatus));
        } else if (userStatus.equals("")) {
            status.setText(context.getString(R.string.no_data));
        }
        holder.itemView.setTag(mSupervisedList.get(position));
    }

    /**
     * Returns the total number of items in the data set held by the adapter.
     */
    @Override
    public int getItemCount() {
        if (mSupervisedList.isEmpty()) {
            return 0;
        } else {
            return mSupervisedList.size();
        }
    }

    /**
     * Class that describes an item view and its data, for its place within the RecyclerView.
     * It maps the components between the layout resource and this adapter.
     * This class manages, as well, the bundle object for the leaks model, and maps the
     * components of the LeaksViewHolder class. Also, it opens a new intent for the user object
     * details.
     *
     * @see android.support.v7.widget.RecyclerView.ViewHolder
     * @see DetailActivitySupervisor
     * @see Bundle
     * @see Intent
     */
    public class SupervisedViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.row_activity_supervisor_tv_supervised_name)
        TextView supervisedName;
        @BindView(R.id.row_activity_supervisor_tv_supervised_status)
        TextView supervisedStatus;

        SupervisedViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int requestCode = getAdapterPosition();
                    User mUser = mSupervisedList.get(requestCode);

                    String id = mUser.getUserId();
                    String name = mUser.getUserName();
                    String email = mUser.getUserEmail();
                    String zone = mUser.getUserZone();
                    String route = mUser.getUserRoute();
                    String type = mUser.getUserType();
                    String status = mUser.getUserStatus();
                    Bundle bundle = new Bundle();
                    bundle.putString(ExtrasHelper.EXTRA_USER_ID, id);
                    bundle.putString(ExtrasHelper.EXTRA_USER_NAME, name);
                    bundle.putString(ExtrasHelper.EXTRA_USER_EMAIL, email);
                    bundle.putString(ExtrasHelper.EXTRA_USER_ZONE, zone);
                    bundle.putString(ExtrasHelper.EXTRA_USER_ROUTE, route);
                    bundle.putString(ExtrasHelper.EXTRA_USER_TYPE, type);
                    bundle.putString(ExtrasHelper.EXTRA_USER_STATUS, status);


                    Intent intent = new Intent(context, DetailActivitySupervisor.class);
                    intent.putExtras(bundle);
                    context.startActivity(intent);
                }
            });
        }
    }
}


