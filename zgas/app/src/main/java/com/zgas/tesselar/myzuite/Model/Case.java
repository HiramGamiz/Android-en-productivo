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
public class Case extends RealmObject {
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
    private String leakId;
    private String leakUserId;
    private String leakTimeAssignment; //When the leak arrives to sf
    private String leakTimeSeen; //seen
    private String leakTimeDeparture; //when it leaves
    private String leakTimeEnd; //when ended
    private String leakTimeScheduled; //programmed
    private String leakServiceType;
    private String leakAccountName;
    private String leakContactName;
    private String leakAddress;
    private String leakSubject;
    private String leakCylinderCapacity;
    private String leakCylinderColor;
    private String leakChannel;
    private String leakFolioSalesNote;
    private String leakStatus;
    private String leakType;
    private String leakPriority;
    private String leakName;
    private String orderName;
    private String orderOperatorName;

    public Case() {
    }

    public Case(String orderOperatorName,String leakName,String orderName,String leakId, String leakUserId, String leakTimeAssignment, String leakTimeSeen, String leakTimeDeparture, String leakTimeEnd, String leakTimeScheduled, String leakServiceType, String leakAccountName, String leakContactName, String leakAddress, String leakSubject, String leakCylinderCapacity, String leakCylinderColor, String leakChannel, String leakFolioSalesNote, String leakStatus, String leakType, String leakPriority, String Quantify, String orderId, String orderUserId, String orderTimeAssignment, String orderTimeSeen, String orderTimeDeparture, String oderTimeEnd, String orderTimeScheduled, String orderServiceType, String orderAccountName, String orderContactName, String orderAddress, String orderSubject, String orderNotice, String orderPaymentMethod, String orderClientName, String orderStatus, String orderType, String orderPriority, String orderTreatment) {
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
        this.leakId = leakId;
        this.leakUserId = leakUserId;
        this.leakTimeAssignment = leakTimeAssignment;
        this.leakTimeSeen = leakTimeSeen;
        this.leakTimeDeparture = leakTimeDeparture;
        this.leakTimeEnd = leakTimeEnd;
        this.leakTimeScheduled = leakTimeScheduled;
        this.leakServiceType = leakServiceType;
        this.leakAccountName = leakAccountName;
        this.leakContactName = leakContactName;
        this.leakAddress = leakAddress;
        this.leakSubject = leakSubject;
        this.leakCylinderCapacity = leakCylinderCapacity;
        this.leakCylinderColor = leakCylinderColor;
        this.leakChannel = leakChannel;
        this.leakFolioSalesNote = leakFolioSalesNote;
        this.leakStatus = leakStatus;
        this.leakType = leakType;
        this.leakPriority = leakPriority;
        this.leakName= leakName;
        this.orderName= orderName;
        this.orderOperatorName=orderOperatorName;
    }

    public String getOrderName() {
        return orderName;
    }

    public void setOrderName(String orderName) {
        this.orderName = orderName;
    }
    public void setorderQuantify(String Quantify) {
        this.orderQuantify = Quantify;
    }

    public String getorderQuantify() {
        return orderQuantify;
    }

    public String getOrderOperatorName() {
        return orderOperatorName;
    }

    public void setOrderOperatorName(String orderOperatorName) {
        this.orderOperatorName = orderOperatorName;
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
    }    public String getLeakStatus() {
        return leakStatus;
    }

    public void setLeakStatus(String leakStatus) {
        this.leakStatus = leakStatus;
    }

    public String getLeakType() {
        return leakType;
    }

    public void setLeakType(String leakType) {
        this.leakType = leakType;
    }

    public String getLeakPriority() {
        return leakPriority;
    }

    public void setLeakPriority(String leakPriority) {
        this.leakPriority = leakPriority;
    }

    public String getLeakId() {
        return leakId;
    }

    public void setLeakId(String leakId) {
        this.leakId = leakId;
    }

    public String getLeakUserId() {
        return leakUserId;
    }

    public void setLeakUserId(String leakUserId) {
        this.leakUserId = leakUserId;
    }

    public String getLeakTimeAssignment() {
        return leakTimeAssignment;
    }

    public void setLeakTimeAssignment(String leakTimeAssignment) {
        this.leakTimeAssignment = leakTimeAssignment;
    }

    public String getLeakTimeSeen() {
        return leakTimeSeen;
    }

    public void setLeakTimeSeen(String leakTimeSeen) {
        this.leakTimeSeen = leakTimeSeen;
    }

    public String getLeakTimeDeparture() {
        return leakTimeDeparture;
    }

    public void setLeakTimeDeparture(String leakTimeDeparture) {
        this.leakTimeDeparture = leakTimeDeparture;
    }

    public String getLeakTimeScheduled() {
        return leakTimeScheduled;
    }

    public void setLeakTimeScheduled(String leakTimeScheduled) {
        this.leakTimeScheduled = leakTimeScheduled;
    }

    public String getLeakServiceType() {
        return leakServiceType;
    }

    public void setLeakServiceType(String leakServiceType) {
        this.leakServiceType = leakServiceType;
    }

    public String getLeakAccountName() {
        return leakAccountName;
    }

    public void setLeakAccountName(String leakAccountName) {
        this.leakAccountName = leakAccountName;
    }

    public String getLeakContactName() {
        return leakContactName;
    }

    public void setLeakContactName(String leakContactName) {
        this.leakContactName = leakContactName;
    }

    public String getLeakAddress() {
        return leakAddress;
    }

    public void setLeakAddress(String leakAddress) {
        this.leakAddress = leakAddress;
    }

    public String getLeakSubject() {
        return leakSubject;
    }

    public void setLeakSubject(String leakSubject) {
        this.leakSubject = leakSubject;
    }

    public String getLeakCylinderCapacity() {
        return leakCylinderCapacity;
    }

    public void setLeakCylinderCapacity(String leakCylinderCapacity) {
        this.leakCylinderCapacity = leakCylinderCapacity;
    }

    public String getLeakCylinderColor() {
        return leakCylinderColor;
    }

    public void setLeakCylinderColor(String leakCylinderColor) {
        this.leakCylinderColor = leakCylinderColor;
    }

    public String getLeakChannel() {
        return leakChannel;
    }

    public void setLeakChannel(String leakChannel) {
        this.leakChannel = leakChannel;
    }

    public String getLeakFolioSalesNote() {
        return leakFolioSalesNote;
    }

    public void setLeakFolioSalesNote(String leakFolioSalesNote) {
        this.leakFolioSalesNote = leakFolioSalesNote;
    }

    public String getLeakTimeEnd() {
        return leakTimeEnd;
    }

    public void setLeakTimeEnd(String leakTimeEnd) {
        this.leakTimeEnd = leakTimeEnd;
    }
}


