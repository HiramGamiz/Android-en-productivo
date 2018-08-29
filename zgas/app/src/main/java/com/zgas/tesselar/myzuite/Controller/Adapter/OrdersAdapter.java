package com.zgas.tesselar.myzuite.Controller.Adapter;

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
import com.zgas.tesselar.myzuite.Model.Case;
import com.zgas.tesselar.myzuite.Model.Leak;
import com.zgas.tesselar.myzuite.Model.Order;
import com.zgas.tesselar.myzuite.Model.User;
import com.zgas.tesselar.myzuite.R;
import com.zgas.tesselar.myzuite.Service.PutReviewOrderTask;
import com.zgas.tesselar.myzuite.Service.PutSeenTimeTask;
import com.zgas.tesselar.myzuite.Utilities.ExtrasHelper;
import com.zgas.tesselar.myzuite.Utilities.UserPreferences;
import com.zgas.tesselar.myzuite.View.Activity.MainActivity;
import com.zgas.tesselar.myzuite.View.Activity.UserLeakage.DetailActivityLeakage;
import com.zgas.tesselar.myzuite.View.Activity.UserOperator.DetailActivityOperator;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.security.AccessController;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.zgas.tesselar.myzuite.Utilities.ExtrasHelper.LEAK_JSON_OBJECT_NAME;

/**
 * Class that provides access to the Order model data items; This class works for both orders
 * and service model objects (because they're both 'order' type).
 *
 * @author jarvizu on 24/10/2017
 * @version 2018.0.9
 * @see Order
 * @see ButterKnife
 * @see RecyclerSwipeAdapter
 */
public class OrdersAdapter extends RecyclerSwipeAdapter<OrdersAdapter.OrderViewHolder> {

    private final String DEBUG_TAG = getClass().getSimpleName();
    //protected SwipeItemRecyclerMangerImpl mItemManger = new SwipeItemRecyclerMangerImpl(this);
    private Context context;
    private ArrayList<Case> mCaseList;
    private Intent intent;
    private JSONObject params;
    private UserPreferences userPreferences;
    private Case mCase;
    private Spinner mSpinnerOptions;
    private Dialog dialog;
    private String caseTimeSeen;
    private String caseName;

    /**
     * Constructor for the OrdersAdapter class.
     *
     * @param context    Current context of the application.
     * @param mCaseList List that contains all the items(orders] that will display on the
     *                   RecyclerView.
     */
    public OrdersAdapter(Context context, ArrayList<Case> mCaseList) {
        this.context = context;
        this.mCaseList = mCaseList;
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
    public OrderViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater li = LayoutInflater.from(parent.getContext());
        View v = li.inflate(R.layout.row_main_fragment_operator_my_orders, parent, false);
        return new OrderViewHolder(v);
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
     * @see OrderViewHolder
     * @see DetailActivityOperator
     * @see Intent
     * @see Bundle
     */
    @Override
    public void onBindViewHolder(final OrderViewHolder viewHolder, final int position) {
        mCase = mCaseList.get(position);
        String caseId = mCase.getOrderId();
        String caseAddress = mCase.getOrderAddress();
        String caseNotice = mCase.getOrderNotice();
        String caseStatus = mCase.getOrderStatus();
        String caseType = mCase.getOrderType();
        String caseOrderHourIn = mCase.getOrderTimeAssignment();
        String caseServiceType = mCase.getOrderServiceType();
        caseTimeSeen = mCase.getOrderTimeSeen();
        caseName=mCase.getOrderName();
        TextView id = viewHolder.mCaseId;
        TextView address = viewHolder.mCaseAddress;
        TextView hourIn = viewHolder.mCaseTimeIn;
        TextView type = viewHolder.mCaseType;
        TextView notice = viewHolder.mCaseNotice;
        TextView status = viewHolder.mCaseStatus;
        TextView statusText = viewHolder.mCaseStatusText;
        ImageView seenDot = viewHolder.mCaseSeenDot;
        TextView OperatorName = viewHolder.mCaseOperatorName;

        if (caseTimeSeen == null || caseTimeSeen.equals("") || caseTimeSeen.equals("null")) {
            seenDot.setVisibility(View.VISIBLE);
            id.setTypeface(null, Typeface.BOLD);
            address.setTypeface(null, Typeface.BOLD);
            hourIn.setTypeface(null, Typeface.BOLD);
            type.setTypeface(null, Typeface.BOLD);
            notice.setTypeface(null, Typeface.BOLD);
            status.setTypeface(null, Typeface.BOLD);
            statusText.setTypeface(null, Typeface.BOLD);
        }else{      seenDot.setVisibility(View.INVISIBLE);
            id.setTypeface(null, Typeface.NORMAL);
            address.setTypeface(null, Typeface.NORMAL);
            hourIn.setTypeface(null, Typeface.NORMAL);
            type.setTypeface(null, Typeface.NORMAL);
            notice.setTypeface(null, Typeface.NORMAL);
            status.setTypeface(null, Typeface.NORMAL);
            statusText.setTypeface(null, Typeface.NORMAL);}

        if (caseNotice.equals("Sin aviso") || caseNotice.equals("") || caseNotice.equals(null)) {
            notice.setVisibility(View.GONE);
        } else {
            notice.setText("Avisar al cliente: " + caseNotice);
        }

        id.setText("Folio: " + String.valueOf(caseName));
        address.setText("Dirección: " + caseAddress);
        type.setText("Tipo: " + caseType + " - " + caseServiceType);

        if (caseOrderHourIn == null || caseOrderHourIn.equals("") || caseOrderHourIn.equals("null")) {
            hourIn.setText("Fecha y hora: " + context.getResources().getString(R.string.no_data));
        } else {
            hourIn.setText("Fecha y hora: " + caseOrderHourIn);
        }

        if (caseStatus.equals(context.getResources().getString(R.string.order_status_failed))) {
            status.setTextColor(context.getResources().getColor(R.color.red));
            viewHolder.mSwipeLayout.setSwipeEnabled(false);
        } else if (caseStatus.equals(context.getResources().getString(R.string.order_status_finished))) {
            viewHolder.mSwipeLayout.setSwipeEnabled(false);
            status.setTextColor(context.getResources().getColor(R.color.light_green));
        } else if (caseStatus.equals(context.getResources().getString(R.string.order_status_in_progress))) {
            status.setTextColor(context.getResources().getColor(R.color.amber));
        } else {
            status.setTextColor(context.getResources().getColor(R.color.blue));
        }
        status.setText(caseStatus.toString());

        if (caseStatus.equals(context.getResources().getString(R.string.order_status_reviewing))) {
            viewHolder.mSwipeLayout.setSwipeEnabled(false);
        }
        User AuxUser=userPreferences.getUserObject();
        if(AuxUser.getUserType().equals((String.valueOf(R.string.user_type_supervisor)))){
            if (mCase.getOrderOperatorName() == null || mCase.getOrderOperatorName().equals("") || mCase.getOrderOperatorName().equals("null")) {
                OperatorName.setText("Operador: " + context.getResources().getString(R.string.no_data));
            }else{
                OperatorName.setText("Operador: " + mCase.getOrderOperatorName());
            }
        }else{
            OperatorName.setVisibility(View.INVISIBLE);
        }
        viewHolder.mSwipeLayout.getSurfaceView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Case AuxCase = mCaseList.get(position);
                final String recordTypeAux = AuxCase.getOrderType();
                User AuxUser=userPreferences.getUserObject();
                if (recordTypeAux.equals("Fugas")) {

                    final Bundle bundle = new Bundle();
                    Calendar calendar = Calendar.getInstance();
                    SimpleDateFormat dateFormat = new SimpleDateFormat("d/MM/yyyy h:mm a");
                    final String date = dateFormat.format(calendar.getTime());

                    final Case mLeak = mCaseList.get(position);
                    final String id = mLeak.getOrderId();
                    final String address = mLeak.getOrderAddress();
                    final String status = mLeak.getOrderStatus();
                    final String timeAssignment = mLeak.getOrderTimeAssignment();
                    final String timeArrival = mLeak.getOrderTimeDeparture();
                    final String timeScheduled = mLeak.getOrderTimeScheduled();
                    final String priority = mLeak.getOrderPriority();
                    final String userName = mLeak.getOrderAccountName();
                    final String paymentMethod = mLeak.getOrderPaymentMethod();
                    final String serviceType = mLeak.getOrderServiceType();
                    final String recordType = mLeak.getOrderType();
                    final String treatmentType = mLeak.getOrderTreatment();
                    final String Quantify = mLeak.getorderQuantify();
                    //
                    final String subject = mLeak.getLeakSubject();
                    final String folioSalesNote = mLeak.getLeakFolioSalesNote();
                    final String timeEnd = mLeak.getLeakTimeEnd();
                    final String timeDeparture = mLeak.getLeakTimeDeparture();
                    final String cylinderCapacity = mLeak.getLeakCylinderCapacity();
                    final String cylinderColor = mLeak.getLeakCylinderColor();
                    final String leakChannel = mLeak.getLeakChannel();

                    caseTimeSeen = mLeak.getLeakTimeSeen();

                    if ((caseTimeSeen == null || caseTimeSeen.equals("") || caseTimeSeen.equals("null"))&&!AuxUser.getUserType().equals((String.valueOf(R.string.user_type_supervisor)))) {
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
                                    caseTimeSeen = mLeak.getLeakTimeSeen();
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
                                    bundle.putString(ExtrasHelper.LEAK_JSON_OBJECT_SEEN, caseTimeSeen);



                                    bundle.putString(ExtrasHelper.LEAK_JSON_OBJECT_CYLINDER_CAPACITY, cylinderCapacity);
                                    bundle.putString(ExtrasHelper.LEAK_JSON_OBJECT_COLOR, cylinderColor);
                                    bundle.putString(ExtrasHelper.LEAK_JSON_OBJECT_CHANNEL, leakChannel);
                                    bundle.putString(ExtrasHelper.LEAK_JSON_OBJECT_ADDRESS, address);
                                    bundle.putString(ExtrasHelper.LEAK_JSON_OBJECT_SERVICE_TYPE,recordTypeAux);
                                    bundle.putString(ExtrasHelper.LEAK_JSON_OBJECT_NAME,caseName);

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
                        bundle.putString(ExtrasHelper.LEAK_JSON_OBJECT_SEEN, caseTimeSeen);
                        bundle.putString(ExtrasHelper.LEAK_JSON_OBJECT_CYLINDER_CAPACITY, cylinderCapacity);
                        bundle.putString(ExtrasHelper.LEAK_JSON_OBJECT_COLOR, cylinderColor);
                        bundle.putString(ExtrasHelper.LEAK_JSON_OBJECT_CHANNEL, leakChannel);
                        bundle.putString(ExtrasHelper.LEAK_JSON_OBJECT_ADDRESS, address);
                        bundle.putString(ExtrasHelper.LEAK_JSON_OBJECT_SERVICE_TYPE,recordTypeAux);
                        bundle.putString(ExtrasHelper.LEAK_JSON_OBJECT_NAME,caseName);
                        intent = new Intent();
                        intent = new Intent(context, DetailActivityLeakage.class);
                        intent.putExtras(bundle);
                        context.startActivity(intent);
                    }
                }else{
                final Bundle bundle = new Bundle();
                Calendar calendar = Calendar.getInstance();
                SimpleDateFormat dateFormat = new SimpleDateFormat("d/MM/yyyy h:mm a");
                final String date = dateFormat.format(calendar.getTime());

                Log.d(DEBUG_TAG, "onClick en el pedido: " + mCaseList.get(position).getOrderId());
                mCase = mCaseList.get(position);
                final String id = mCase.getOrderId();
                final String address = mCase.getOrderAddress();
                final String status = mCase.getOrderStatus();
                final String timeAssignment = mCase.getOrderTimeAssignment();
                final String timeArrival = mCase.getOrderTimeDeparture();
                final String timeScheduled = mCase.getOrderTimeScheduled();
                final String priority = mCase.getOrderPriority();
                final String userName = mCase.getOrderAccountName();
                final String paymentMethod = mCase.getOrderPaymentMethod();
                final String serviceType = mCase.getOrderServiceType();
                final String recordType = mCase.getOrderType();
                final String treatmentType = mCase.getOrderTreatment();
                final String Quantify = mCase.getorderQuantify();
                    caseName=mCase.getOrderName();
                caseTimeSeen = mCase.getOrderTimeSeen();

                if (caseTimeSeen == null || caseTimeSeen.equals("") || caseTimeSeen.equals("null")&&!AuxUser.getUserType().equals((String.valueOf(R.string.user_type_supervisor)))) {
                    params = new JSONObject();
                    userPreferences = new UserPreferences(context);
                    try {
                        PutSeenTimeTask.SeenTimeTaskListener listener = new PutSeenTimeTask.SeenTimeTaskListener() {
                            @Override
                            public void seenTimeErrorResponse(String error) {
                                viewHolder.mSwipeLayout.close(true);
                                Toast.makeText(context, error, Toast.LENGTH_LONG).show();
                            }

                            @Override
                            public void seenTimeSuccessResponse(Order order) {
                                viewHolder.mSwipeLayout.close(true);
                                mCase.setOrderTimeSeen(date);
                                caseTimeSeen = mCase.getOrderTimeSeen();
                                bundle.putString(ExtrasHelper.LEAK_JSON_OBJECT_NAME,caseName);
                                bundle.putString(ExtrasHelper.ORDER_JSON_OBJECT_ID, id);
                                bundle.putString(ExtrasHelper.ORDER_JSON_OBJECT_ADDRESS, address);
                                bundle.putString(ExtrasHelper.ORDER_JSON_OBJECT_STATUS, status);
                                bundle.putSerializable(ExtrasHelper.ORDER_JSON_OBJECT_TIME_ASSIGNMENT, timeAssignment);
                                bundle.putSerializable(ExtrasHelper.ORDER_JSON_OBJECT_TIME_SEEN, caseTimeSeen);
                                bundle.putSerializable(ExtrasHelper.ORDER_JSON_OBJECT_TIME_ARRIVAL, timeArrival);
                                bundle.putSerializable(ExtrasHelper.ORDER_JSON_OBJECT_TIME_SCHEDULED, timeScheduled);
                                bundle.putString(ExtrasHelper.ORDER_JSON_OBJECT_PRIORITY, priority);
                                bundle.putString(ExtrasHelper.ORDER_JSON_OBJECT_ACCOUNT_NAME, userName);
                                bundle.putString(ExtrasHelper.ORDER_JSON_OBJECT_SERVICE_TYPE, serviceType);
                                bundle.putString(ExtrasHelper.ORDER_JSON_OBJECT_PAYMENT_METHOD, paymentMethod);
                                bundle.putString(ExtrasHelper.ORDER_JSON_OBJECT_RECORD_TYPE, recordType);
                                bundle.putString(ExtrasHelper.ORDER_JSON_OBJECT_SERVICE_TYPE, serviceType);
                                bundle.putString(ExtrasHelper.ORDER_JSON_TREATMENT, treatmentType);
                                bundle.putString(ExtrasHelper.ORDER_JSON_QUANTIFY, Quantify);


                                intent = new Intent();
                                intent = new Intent(context, DetailActivityOperator.class);
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
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    bundle.putString(ExtrasHelper.LEAK_JSON_OBJECT_NAME,caseName);
                    bundle.putString(ExtrasHelper.ORDER_JSON_OBJECT_ID, id);
                    bundle.putString(ExtrasHelper.ORDER_JSON_OBJECT_ADDRESS, address);
                    bundle.putString(ExtrasHelper.ORDER_JSON_OBJECT_STATUS, status);
                    bundle.putSerializable(ExtrasHelper.ORDER_JSON_OBJECT_TIME_ASSIGNMENT, timeAssignment);
                    bundle.putSerializable(ExtrasHelper.ORDER_JSON_OBJECT_TIME_SEEN, caseTimeSeen);
                    bundle.putSerializable(ExtrasHelper.ORDER_JSON_OBJECT_TIME_ARRIVAL, timeArrival);
                    bundle.putSerializable(ExtrasHelper.ORDER_JSON_OBJECT_TIME_SCHEDULED, timeScheduled);
                    bundle.putString(ExtrasHelper.ORDER_JSON_OBJECT_PRIORITY, priority);
                    bundle.putString(ExtrasHelper.ORDER_JSON_OBJECT_ACCOUNT_NAME, userName);
                    bundle.putString(ExtrasHelper.ORDER_JSON_OBJECT_SERVICE_TYPE, serviceType);
                    bundle.putString(ExtrasHelper.ORDER_JSON_OBJECT_PAYMENT_METHOD, paymentMethod);
                    bundle.putString(ExtrasHelper.ORDER_JSON_OBJECT_RECORD_TYPE, recordType);
                    bundle.putString(ExtrasHelper.ORDER_JSON_OBJECT_SERVICE_TYPE, serviceType);
                    bundle.putString(ExtrasHelper.ORDER_JSON_TREATMENT, treatmentType);
                    bundle.putString(ExtrasHelper.ORDER_JSON_QUANTIFY,  Quantify);
                    intent = new Intent();

                        intent = new Intent(context, DetailActivityOperator.class);
                        intent.putExtras(bundle);
                        context.startActivity(intent);

                }
            }
            }
        });

        viewHolder.mCaseReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog = new Dialog(context);
                dialog.setContentView(R.layout.dialog_review_case);
                dialog.getWindow().getAttributes().windowAnimations = R.style.Theme_Dialog_Animation;

                Log.d(DEBUG_TAG, context.getResources().getString(R.string.on_create));
                dialog.setCancelable(false);

                mSpinnerOptions = dialog.findViewById(R.id.dialog_review_case_spinner);
                ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(context, R.array.order_prompts_review, android.R.layout.simple_spinner_item);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                mSpinnerOptions.setAdapter(new NothingSelectedSpinnerAdapter(adapter, R.layout.contact_spinner_row_nothing_selected, context));

                Button mBtnAccept = dialog.findViewById(R.id.dialog_review_case_button_accept);
                mBtnAccept.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (mSpinnerOptions.getSelectedItem() == null) {
                            Toast.makeText(context, context.getResources().getString(R.string.order_review_incorrect), Toast.LENGTH_LONG).show();
                        } else {

                            params = new JSONObject();
                            userPreferences = new UserPreferences(context);

                            try {
                                params.put(ExtrasHelper.REVIEW_JSON_OBJECT_OPERATOR_ID, userPreferences.getUserObject().getUserId());
                                params.put(ExtrasHelper.REVIEW_JSON_OBJECT_ORDER_ID, mCaseList.get(position).getOrderId());
                                params.put(ExtrasHelper.REVIEW_JSON_OBJECT_REVIEW, mSpinnerOptions.getSelectedItem().toString());

                                Log.d(DEBUG_TAG, params.get(ExtrasHelper.REVIEW_JSON_OBJECT_OPERATOR_ID).toString());
                                Log.d(DEBUG_TAG, params.get(ExtrasHelper.REVIEW_JSON_OBJECT_ORDER_ID).toString());
                                Log.d(DEBUG_TAG, params.get(ExtrasHelper.REVIEW_JSON_OBJECT_REVIEW).toString());

                                PutReviewOrderTask.OrderReviewTaskListener listener = new PutReviewOrderTask.OrderReviewTaskListener() {
                                    @Override
                                    public void reviewOrderErrorResponse(String error) {
                                        viewHolder.mSwipeLayout.close(true);
                                        Toast.makeText(context, context.getResources().getString(R.string.order_review_incorrect), Toast.LENGTH_LONG).show();
                                    }

                                    @Override
                                    public void reviewOrderSuccessResponse(Order order) {
                                        viewHolder.mSwipeLayout.close(true);
                                        Toast.makeText(context, context.getResources().getString(R.string.order_review_correct), Toast.LENGTH_LONG).show();
                                        callIntent();
                                    }
                                };

                                PutReviewOrderTask putReviewOrderTask = new PutReviewOrderTask(context, params);
                                putReviewOrderTask.setOrderReviewTaskListener(listener);
                                putReviewOrderTask.execute();

                                
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            mSpinnerOptions.setSelection(0);
                            dialog.dismiss();
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
    private void callIntent(){
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse(ExtrasHelper.CALL));
        context.startActivity(intent);
    }

    /**
     * Returns the total number of items in the data set held by the adapter.
     */
    @Override
    public int getItemCount() {
        if (mCaseList.isEmpty()) {
            return 0;
        } else {
            return mCaseList.size();
        }
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
    public class OrderViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.row_main_fragment_swipe_orders)
        SwipeLayout mSwipeLayout;
        @BindView(R.id.row_visit_recycler_tv_review_visit)
        TextView mCaseReview;
        @BindView(R.id.row_main_fragment_tv_order_id)
        TextView mCaseId;
        @BindView(R.id.row_main_fragment_tv_order_status)
        TextView mCaseStatus;
        @BindView(R.id.row_main_fragment_prompt_order_status)
        TextView mCaseStatusText;
        @BindView(R.id.row_main_fragment_tv_order_address)
        TextView mCaseAddress;
        @BindView(R.id.row_main_fragment_tv_order_in)
        TextView mCaseTimeIn;
        @BindView(R.id.row_main_fragment_tv_order_type)
        TextView mCaseType;
        @BindView(R.id.row_main_fragment_tv_notice)
        TextView mCaseNotice;
        @BindView(R.id.row_main_fragment_seen_dot)
        ImageView mCaseSeenDot;
        @BindView(R.id.row_main_fragment_tv_order_operator)
        TextView mCaseOperatorName;

        OrderViewHolder(final View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

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

