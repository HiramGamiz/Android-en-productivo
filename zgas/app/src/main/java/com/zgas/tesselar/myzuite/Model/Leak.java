package com.zgas.tesselar.myzuite.Model;

/**
 * Leak model
 *
 * @author jarvizu on 28/08/2017
 * @version 2018.0.9
 */
public class Leak {

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

    public Leak() {
    }

    public Leak(String Name,String leakId, String leakUserId, String leakTimeAssignment, String leakTimeSeen, String leakTimeDeparture, String leakTimeEnd, String leakTimeScheduled, String leakServiceType, String leakAccountName, String leakContactName, String leakAddress, String leakSubject, String leakCylinderCapacity, String leakCylinderColor, String leakChannel, String leakFolioSalesNote, String leakStatus, String leakType, String leakPriority) {
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
        this.leakName=Name;
    }

    public String getLeakStatus() {
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
    public String getLeakName() {
        return leakName;
    }

    public void setLeakName(String leakName) {
        this.leakName = leakName;
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


