package com.zgas.tesselar.myzuite.Model;

import android.util.Log;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Order model
 *
 * @author jarvizu on 28/08/2017
 * @version 2018.0.9
 * @see RealmObject
 * @see io.realm.Realm
 */
public class Order extends RealmObject {
    @PrimaryKey
    private String orderId;
    private String orderUserId;
    private String orderTimeAssignment;
    private String orderTimeSeen;
    private String orderTimeDeparture;
    private String oderTimeEnd;
    private String orderTimeScheduled;
    private String orderServiceType;
    private String orderAccountName;
    private String orderContactName;
    private String orderAddress;
    private String orderSubject;
    private String orderNotice;
    private String orderPaymentMethod;
    private String orderClientName;
    private String orderStatus;
    private String orderType;
    private String orderPriority;
    private String orderTreatment;
    private String orderQuantify;
    private String OrderName;

    public Order() {
    }

    public Order(String Name,String Quantify, String orderId, String orderUserId, String orderTimeAssignment, String orderTimeSeen, String orderTimeDeparture, String oderTimeEnd, String orderTimeScheduled, String orderServiceType, String orderAccountName, String orderContactName, String orderAddress, String orderSubject, String orderNotice, String orderPaymentMethod, String orderClientName, String orderStatus, String orderType, String orderPriority, String orderTreatment) {
        this.orderId = orderId;
        this.orderUserId = orderUserId;
        this.orderTimeAssignment = orderTimeAssignment;
        this.orderTimeSeen = orderTimeSeen;
        this.orderTimeDeparture = orderTimeDeparture;
        this.oderTimeEnd = oderTimeEnd;
        this.orderTimeScheduled = orderTimeScheduled;
        this.orderServiceType = orderServiceType;
        this.orderAccountName = orderAccountName;
        this.orderContactName = orderContactName;
        this.orderAddress = orderAddress;
        this.orderSubject = orderSubject;
        this.orderNotice = orderNotice;
        this.orderPaymentMethod = orderPaymentMethod;
        this.orderClientName = orderClientName;
        this.orderStatus = orderStatus;
        this.orderType = orderType;
        this.orderPriority = orderPriority;
        this.orderTreatment = orderTreatment;
        this.orderQuantify=Quantify;
        this.OrderName=Name;
    }

    public void setorderQuantify(String Quantify) {
        this.orderQuantify = Quantify;
    }

    public String getorderQuantify() {
        return orderQuantify;
    }
    public String getOrderName() {
        return OrderName;
    }

    public void setOrderName(String OrderName) {
        this.OrderName = OrderName;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getOrderUserId() {
        return orderUserId;
    }


    public void setOrderUserId(String orderUserId) {
        this.orderUserId = orderUserId;
    }

    public String getOrderTimeAssignment() {
        return orderTimeAssignment;
    }

    public void setOrderTimeAssignment(String orderTimeAssignment) {
        this.orderTimeAssignment = orderTimeAssignment;
    }

    public String getOrderTimeSeen() {
        return orderTimeSeen;
    }

    public void setOrderTimeSeen(String orderTimeSeen) {
        this.orderTimeSeen = orderTimeSeen;
    }

    public String getOrderTimeDeparture() {
        return orderTimeDeparture;
    }

    public void setOrderTimeDeparture(String orderTimeDeparture) {
        this.orderTimeDeparture = orderTimeDeparture;
    }

    public String getOrderAccountName() {
        return orderAccountName;
    }

    public void setOrderAccountName(String orderAccountName) {
        this.orderAccountName = orderAccountName;
    }

    public String getOrderAddress() {
        return orderAddress;
    }

    public void setOrderAddress(String orderAddress) {
        this.orderAddress = orderAddress;
    }

    public String getOrderTimeScheduled() {
        return orderTimeScheduled;
    }

    public void setOrderTimeScheduled(String orderTimeScheduled) {
        this.orderTimeScheduled = orderTimeScheduled;
    }

    public String getOrderServiceType() {
        return orderServiceType;
    }

    public void setOrderServiceType(String orderServiceType) {
        this.orderServiceType = orderServiceType;
    }

    public String getOrderContactName() {
        return orderContactName;
    }

    public void setOrderContactName(String orderContactName) {
        this.orderContactName = orderContactName;
    }

    public String getOrderSubject() {
        return orderSubject;
    }

    public void setOrderSubject(String orderSubject) {
        this.orderSubject = orderSubject;
    }

    public String getOrderPaymentMethod() {
        return orderPaymentMethod;
    }

    public void setOrderPaymentMethod(String orderPaymentMethod) {
        this.orderPaymentMethod = orderPaymentMethod;
    }

    public String getOrderNotice() {
        return orderNotice;
    }

    public void setOrderNotice(String orderNotice) {
        this.orderNotice = orderNotice;
    }

    public String getOderTimeEnd() {
        return oderTimeEnd;
    }

    public void setOderTimeEnd(String oderTimeEnd) {
        this.oderTimeEnd = oderTimeEnd;
    }

    public String getOrderClientName() {
        return orderClientName;
    }

    public void setOrderClientName(String orderClientName) {
        this.orderClientName = orderClientName;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    public String getOrderPriority() {
        return orderPriority;
    }

    public void setOrderPriority(String orderPriority) {
        this.orderPriority = orderPriority;
    }

    public String getOrderTreatment() {
        return orderTreatment;
    }

    public void setOrderTreatment(String orderTreatment) {
        this.orderTreatment = orderTreatment;
    }
}
