package com.zgas.tesselar.myzuite.Controller.Adapter;
import com.zgas.tesselar.myzuite.View.Activity.MainActivity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.adapters.RecyclerSwipeAdapter;
import com.zgas.tesselar.myzuite.Model.Leak;
import com.zgas.tesselar.myzuite.Model.Order;
import com.zgas.tesselar.myzuite.R;
import com.zgas.tesselar.myzuite.Service.PutReviewLeakTask;
import com.zgas.tesselar.myzuite.Service.PutSeenTimeTask;
import com.zgas.tesselar.myzuite.Utilities.ExtrasHelper;
import com.zgas.tesselar.myzuite.Utilities.UserPreferences;
import com.zgas.tesselar.myzuite.View.Activity.UserLeakage.DetailActivityLeakage;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Class that provides access to the Leak model data items.
 *
 * @author jarvizu on 24/10/2017
 * @version 2018.0.9
 * @see Leak
 * @see ButterKnife
 * @see RecyclerSwipeAdapter
 */
public class LeaksAdapter extends RecyclerSwipeAdapter<LeaksAdapter.LeaksViewHolder> {

    private final String DEBUG_TAG = getClass().getSimpleName();
    //protected SwipeItemRecyclerMangerImpl mItemManger = new SwipeItemRecyclerMangerImpl(this);

    private Context context;
    private ArrayList<Leak> mLeaksList;
    private Intent intent;
    private JSONObject params;
    private Spinner mSpinnerOptions;
    private Dialog dialog;
    private Leak mLeak;
    private UserPreferences userPreferences;

    private String timeSeen;

    /**
     * Constructor for the LeaksAdapter class.
     *
     * @param context    Current context of the application.
     * @param mLeaksList List that contains all the items(leaks] that will display on the
     *                   RecyclerView.
     */
    public LeaksAdapter(Context context, ArrayList<Leak> mLeaksList) {
        this.context = context;
        this.mLeaksList = mLeaksList;
        userPreferences = new UserPreferences(context);
    }

    /**
     * Method for initializing the viewholders, inflates the RowMainFragment layout.
     *
     * @param parent   The ViewGroup into which the new View will be added after it is bound to an
     *                 adapter position.
     * @param viewType The type of the new view.
     * @return LeaksAdapter view.
     */
    @Override
    public LeaksViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater li = LayoutInflater.from(parent.getContext());
        View v = li.inflate(R.layout.row_main_fragment_operator_my_orders, parent, false);
        return new LeaksAdapter.LeaksViewHolder(v);
    }

    /**
     * Method that displays the data at an specified position. It Updates the contents of the
     * itemView.
     * This method manages, as well, the bundle object for the leaks model, and maps the
     * components of the LeaksViewHolder class. Also, it opens a new intent for the leak object
     * details.
     * The holder uses the SwipeLayout component; this is for independent swiping on each item of
     * the recyclerview, and manages the swipelistener and onClickListener separately.
     *
     * @param viewHolder The ViewHolder which contents should be updated to represent an item
     *                   depending it's position.
     * @param position   The position of the item within the data set.
     * @see LeaksViewHolder
     * @see DetailActivityLeakage
     * @see Intent
     * @see Bundle
     */
    @Override
    public void onBindViewHolder(final LeaksViewHolder viewHolder, final int position) {
        mLeak = mLeaksList.get(position);
        String leakId = mLeak.getLeakId();
        String leakName = mLeak.getLeakName();
        String leakAddress = mLeak.getLeakAddress();
        String leakStatus = mLeak.getLeakStatus();
        String leakType = mLeak.getLeakType();
        String leakHourIn = mLeak.getLeakTimeAssignment();
        String serviceType = mLeak.getLeakServiceType();
        timeSeen = mLeak.getLeakTimeSeen();

        TextView id = viewHolder.mLeakId;
        TextView address = viewHolder.mLeakAddress;
        TextView hourIn = viewHolder.mLeakTimeIn;
        TextView type = viewHolder.mLeakType;
        TextView statusText = viewHolder.mOrderStatusText;
        TextView status = viewHolder.mLeakStatus;
        TextView notice = viewHolder.mLeakNotice;
        ImageView seenDot = viewHolder.mOrderSeenDot;
        //
        TextView OperatorName = viewHolder.mCaseOperatorName;
        OperatorName.setVisibility(View.INVISIBLE);
        //
        if (timeSeen == null || timeSeen.equals("") || timeSeen.equals("null")) {
            seenDot.setVisibility(View.VISIBLE);
            id.setTypeface(null, Typeface.BOLD);
            address.setTypeface(null, Typeface.BOLD);
            hourIn.setTypeface(null, Typeface.BOLD);
            type.setTypeface(null, Typeface.BOLD);
            notice.setTypeface(null, Typeface.BOLD);
            status.setTypeface(null, Typeface.BOLD);
            statusText.setTypeface(null, Typeface.BOLD);
        }else{
            seenDot.setVisibility(View.INVISIBLE);
            id.setTypeface(null, Typeface.NORMAL);
            address.setTypeface(null, Typeface.NORMAL);
            hourIn.setTypeface(null, Typeface.NORMAL);
            type.setTypeface(null, Typeface.NORMAL);
            notice.setTypeface(null, Typeface.NORMAL);
            status.setTypeface(null, Typeface.NORMAL);
            statusText.setTypeface(null, Typeface.NORMAL);
        }

        id.setText("Folio: " + String.valueOf(leakName));
        address.setText("Dirección: " + leakAddress);
        type.setText("Tipo: " + leakType);
        if (leakHourIn == null || leakHourIn.equals("") || leakHourIn.equals("null")) {
            hourIn.setText("Fecha y hora: " + context.getResources().getString(R.string.no_data));
        } else {
            hourIn.setText("Fecha y hora: " + leakHourIn);
        }

        if (leakStatus.equals(context.getResources().getString(R.string.order_status_failed))) {
            status.setTextColor(context.getResources().getColor(R.color.red));
            viewHolder.mSwipeLayout.setSwipeEnabled(false);
        } else if (leakStatus.equals(context.getResources().getString(R.string.order_status_closed))) {
            status.setTextColor(context.getResources().getColor(R.color.light_green));
            viewHolder.mSwipeLayout.setSwipeEnabled(false);
        } else if (leakStatus.equals(context.getResources().getString(R.string.order_status_in_progress))) {
            status.setTextColor(context.getResources().getColor(R.color.amber));
        } else {
            status.setTextColor(context.getResources().getColor(R.color.blue));
        }
        status.setText(leakStatus);

        viewHolder.mSwipeLayout.getSurfaceView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Bundle bundle = new Bundle();
                Calendar calendar = Calendar.getInstance();
                SimpleDateFormat dateFormat = new SimpleDateFormat("d/MM/yyyy h:mm a");
                final String date = dateFormat.format(calendar.getTime());

                final Leak mLeak = mLeaksList.get(position);
                final String id = mLeak.getLeakId();
                final String timeAssignment = mLeak.getLeakTimeAssignment();
                final String timeDeparture = mLeak.getLeakTimeDeparture();
                final String timeScheduled = mLeak.getLeakTimeScheduled();
                final String timeEnd = mLeak.getLeakTimeEnd();
                final String serviceType = mLeak.getLeakServiceType();
                final String userName = mLeak.getLeakAccountName();
                String contactName = mLeak.getLeakContactName();
                final String address = mLeak.getLeakAddress();
                final String subject = mLeak.getLeakSubject();
                final String cylinderCapacity = mLeak.getLeakCylinderCapacity();
                final String cylinderColor = mLeak.getLeakCylinderColor();
                final String leakChannel = mLeak.getLeakChannel();
                final String folioSalesNote = mLeak.getLeakFolioSalesNote();
                final String status = mLeak.getLeakStatus();
                final String priority = mLeak.getLeakPriority();
                final String name = mLeak.getLeakName();
                timeSeen = mLeak.getLeakTimeSeen();

                if (timeSeen == null || timeSeen.equals("") || timeSeen.equals("null")) {
                    params = new JSONObject();
                    userPreferences = new UserPreferences(context);
                    try {
                        PutSeenTimeTask.SeenTimeTaskListener listener = new PutSeenTimeTask.SeenTimeTaskListener() {
                            @Override
                            public void seenTimeErrorResponse(String error) {
                                viewHolder.mSwipeLayout.close(true);
                                Toast.makeText(context, context.getResources().getString(R.string.server_error), Toast.LENGTH_LONG).show();
                            }

                            @Override
                            public void seenTimeSuccessResponse(Order order) {
                                viewHolder.mSwipeLayout.close(true);
                                mLeak.setLeakTimeSeen(date);
                                timeSeen = mLeak.getLeakTimeSeen();
                                bundle.putString(ExtrasHelper.LEAK_JSON_OBJECT_ID, id);
                                bundle.putString(ExtrasHelper.LEAK_JSON_OBJECT_WHO_REPORTS, userName);
                                bundle.putString(ExtrasHelper.LEAK_JSON_OBJECT_SUBJECT, subject);
                                bundle.putString(ExtrasHelper.LEAK_JSON_OBJECT_STATUS, status);
                                bundle.putString(ExtrasHelper.LEAK_JSON_OBJECT_PRIORITY, priority);
                                bundle.putString(ExtrasHelper.LEAK_JSON_OBJECT_SALES_NOTE, folioSalesNote);
                                bundle.putString(ExtrasHelper.LEAK_JSON_OBJECT_DATE_TECHNICIAN, timeAssignment);
                                bundle.putString(ExtrasHelper.LEAK_JSON_OBJECT_DATE_SCHEDULED, timeScheduled);
                                bundle.putString(ExtrasHelper.LEAK_JSON_OBJECT_DATE_END, timeEnd);
                                bundle.putString(ExtrasHelper.LEAK_JSON_OBJECT_DATE_DEPARTURE, timeDeparture);
                                bundle.putString(ExtrasHelper.LEAK_JSON_OBJECT_SEEN, timeSeen);
                                bundle.putString(ExtrasHelper.LEAK_JSON_OBJECT_CYLINDER_CAPACITY, cylinderCapacity);
                                bundle.putString(ExtrasHelper.LEAK_JSON_OBJECT_COLOR, cylinderColor);
                                bundle.putString(ExtrasHelper.LEAK_JSON_OBJECT_CHANNEL, leakChannel);
                                bundle.putString(ExtrasHelper.LEAK_JSON_OBJECT_ADDRESS, address);
                                bundle.putString(ExtrasHelper.LEAK_JSON_OBJECT_NAME, name);

                                intent = new Intent();
                                intent = new Intent(context, DetailActivityLeakage.class);
                                intent.putExtras(bundle);
                                context.startActivity(intent);
                            }
                        };
                        params.put(ExtrasHelper.REVIEW_JSON_OBJECT_OPERATOR_ID, userPreferences.getUserObject().getUserId());
                        params.put(ExtrasHelper.REVIEW_JSON_OBJECT_ORDER_ID, id);
                        params.put(ExtrasHelper.ORDER_JSON_OBJECT_TIME_SEEN, date);

                        PutSeenTimeTask putSeenTimeTask = new PutSeenTimeTask(context, params);
                        putSeenTimeTask.setSeenTimeTaskListener(listener);
                        putSeenTimeTask.execute();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    bundle.putString(ExtrasHelper.LEAK_JSON_OBJECT_ID, id);
                    bundle.putString(ExtrasHelper.LEAK_JSON_OBJECT_WHO_REPORTS, userName);
                    bundle.putString(ExtrasHelper.LEAK_JSON_OBJECT_SUBJECT, subject);
                    bundle.putString(ExtrasHelper.LEAK_JSON_OBJECT_STATUS, status);
                    bundle.putString(ExtrasHelper.LEAK_JSON_OBJECT_PRIORITY, priority);
                    bundle.putString(ExtrasHelper.LEAK_JSON_OBJECT_SALES_NOTE, folioSalesNote);
                    bundle.putString(ExtrasHelper.LEAK_JSON_OBJECT_DATE_TECHNICIAN, timeAssignment);
                    bundle.putString(ExtrasHelper.LEAK_JSON_OBJECT_DATE_SCHEDULED, timeScheduled);
                    bundle.putString(ExtrasHelper.LEAK_JSON_OBJECT_DATE_END, timeEnd);
                    bundle.putString(ExtrasHelper.LEAK_JSON_OBJECT_DATE_DEPARTURE, timeDeparture);
                    bundle.putString(ExtrasHelper.LEAK_JSON_OBJECT_SEEN, timeSeen);
                    bundle.putString(ExtrasHelper.LEAK_JSON_OBJECT_CYLINDER_CAPACITY, cylinderCapacity);
                    bundle.putString(ExtrasHelper.LEAK_JSON_OBJECT_COLOR, cylinderColor);
                    bundle.putString(ExtrasHelper.LEAK_JSON_OBJECT_CHANNEL, leakChannel);
                    bundle.putString(ExtrasHelper.LEAK_JSON_OBJECT_ADDRESS, address);
                    bundle.putString(ExtrasHelper.LEAK_JSON_OBJECT_NAME, name);

                    intent = new Intent();
                    intent = new Intent(context, DetailActivityLeakage.class);
                    intent.putExtras(bundle);
                    context.startActivity(intent);
                }
            }
        });

        viewHolder.mLeakReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog = new Dialog(context);
                dialog.setContentView(R.layout.dialog_review_case);
                dialog.getWindow().getAttributes().windowAnimations = R.style.Theme_Dialog_Animation;

                dialog.setCancelable(false);

                TextView mTitleText = dialog.findViewById(R.id.dialog_review_case_title);
                mTitleText.setText(R.string.dialog_review_order_leak);

                mSpinnerOptions = dialog.findViewById(R.id.dialog_review_case_spinner);
                ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(context, R.array.order_prompts_review,
                        android.R.layout.simple_spinner_item);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                mSpinnerOptions.setAdapter(new NothingSelectedSpinnerAdapter(adapter,
                        R.layout.contact_spinner_row_nothing_selected, context));

                Button mBtnAccept = dialog.findViewById(R.id.dialog_review_case_button_accept);
                mBtnAccept.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        MainActivity aux = new MainActivity();
                        if (mSpinnerOptions.getSelectedItem() == null) {
                            Toast.makeText(context, context.getResources().getString(R.string.order_review_incorrect)
                                    , Toast.LENGTH_LONG).show();
                        } else {
                            params = new JSONObject();
                            userPreferences = new UserPreferences(context);

                            try {
                                params.put(ExtrasHelper.REVIEW_JSON_OBJECT_OPERATOR_ID, userPreferences.getUserObject().getUserId());
                                params.put(ExtrasHelper.REVIEW_JSON_OBJECT_ORDER_ID, mLeak.getLeakId());
                                params.put(ExtrasHelper.REVIEW_JSON_OBJECT_REVIEW, mSpinnerOptions.getSelectedItem().toString());

                                Log.d(DEBUG_TAG, params.get(ExtrasHelper.REVIEW_JSON_OBJECT_OPERATOR_ID).toString());
                                Log.d(DEBUG_TAG, params.get(ExtrasHelper.REVIEW_JSON_OBJECT_ORDER_ID).toString());
                                Log.d(DEBUG_TAG, params.get(ExtrasHelper.REVIEW_JSON_OBJECT_REVIEW).toString());

                                new PutReviewLeakTask(context, params).execute();

                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            mSpinnerOptions.setSelection(0);
                            Toast.makeText(context, context.getResources().getString(R.string.order_review_correct), Toast.LENGTH_LONG).show();
                            dialog.dismiss();
                            callIntent();
                        }
                    }
                });

                Button mBtnCancel = dialog.findViewById(R.id.dialog_review_case_button_cancel);
                mBtnCancel.setOnClickListener(new View.OnClickListener()

                {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });

    }

    /**
     * Returns the total number of items in the data set held by the adapter.
     */
    @Override
    public int getItemCount() {
        if (mLeaksList.isEmpty()) {
            return 0;
        } else {
            return mLeaksList.size();
        }
    }
    private void callIntent(){
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse(ExtrasHelper.CALL));
        context.startActivity(intent);
    }
    @Override
    public int getSwipeLayoutResourceId(int position) {
        return position;
    }

    /**
     * Class that describes an item view and its data, for its place within the RecyclerView.
     * It maps the components between the layout resource and this adapter.
     *
     * @see android.support.v7.widget.RecyclerView.ViewHolder
     */
    public class LeaksViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.row_main_fragment_swipe_orders)
        SwipeLayout mSwipeLayout;
        @BindView(R.id.row_visit_recycler_tv_review_visit)
        TextView mLeakReview;
        @BindView(R.id.row_main_fragment_tv_order_id)
        TextView mLeakId;
        @BindView(R.id.row_main_fragment_tv_order_status)
        TextView mLeakStatus;
        @BindView(R.id.row_main_fragment_prompt_order_status)
        TextView mOrderStatusText;
        @BindView(R.id.row_main_fragment_tv_order_address)
        TextView mLeakAddress;
        @BindView(R.id.row_main_fragment_tv_order_in)
        TextView mLeakTimeIn;
        @BindView(R.id.row_main_fragment_tv_order_type)
        TextView mLeakType;
        @BindView(R.id.row_main_fragment_tv_notice)
        TextView mLeakNotice;
        @BindView(R.id.row_main_fragment_seen_dot)
        ImageView mOrderSeenDot;
        @BindView(R.id.row_main_fragment_tv_order_operator)
        TextView mCaseOperatorName;
        LeaksViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            mLeakNotice.setVisibility(View.GONE);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int requestCode = getAdapterPosition();
                    Log.d(DEBUG_TAG, "OrdersAdapter view listener for adapter position: " + requestCode);
                }
            });
        }
    }
}
