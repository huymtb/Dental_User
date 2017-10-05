package com.prostage.l_pha.dental_user.model.server_model.admin_model;

import com.google.gson.JsonElement;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by USER on 18-May-17.
 */

public class AdminModel implements Serializable{
    @SerializedName("id")
    @Expose
    private Integer id;

    @SerializedName("address")
    @Expose
    private String address;

    @SerializedName("closeDays")
    @Expose
    private String closeDays;

    @SerializedName("fullName")
    @Expose
    private String fullName;

    @SerializedName("notificationText1")
    @Expose
    private String notificationText1;

    @SerializedName("notificationText2")
    @Expose
    private String notificationText2;

    @SerializedName("notificationTime1")
    @Expose
    private Integer notificationTime1;

    @SerializedName("notificationTime2")
    @Expose
    private Integer notificationTime2;

    @SerializedName("technique")
    @Expose
    private String technique;

    @SerializedName("tel")
    @Expose
    private String tel;

    @SerializedName("numOfDoctors")
    @Expose
    private Integer numOfDoctors;

    @SerializedName("timeForPerPatient")
    @Expose
    private Integer timeForPerPatient;

    @SerializedName("free")
    @Expose
    private Boolean free;

    @SerializedName("practiceRemark")
    @Expose
    private String practiceRemark;

    @SerializedName("remark")
    @Expose
    private String remark;

    @SerializedName("createdDate")
    @Expose
    private String createdDate;

    @SerializedName("updatedDate")
    @Expose
    private String updatedDate;

    @SerializedName("workingSet")
    @Expose
    private JsonElement workingSet;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCloseDays() {
        return closeDays;
    }

    public void setCloseDays(String closeDays) {
        this.closeDays = closeDays;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getNotificationText1() {
        return notificationText1;
    }

    public void setNotificationText1(String notificationText1) {
        this.notificationText1 = notificationText1;
    }

    public String getNotificationText2() {
        return notificationText2;
    }

    public void setNotificationText2(String notificationText2) {
        this.notificationText2 = notificationText2;
    }

    public Integer getNotificationTime1() {
        return notificationTime1;
    }

    public void setNotificationTime1(Integer notificationTime1) {
        this.notificationTime1 = notificationTime1;
    }

    public Integer getNotificationTime2() {
        return notificationTime2;
    }

    public void setNotificationTime2(Integer notificationTime2) {
        this.notificationTime2 = notificationTime2;
    }

    public String getTechnique() {
        return technique;
    }

    public void setTechnique(String technique) {
        this.technique = technique;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public Integer getNumOfDoctors() {
        return numOfDoctors;
    }

    public void setNumOfDoctors(Integer numOfDoctors) {
        this.numOfDoctors = numOfDoctors;
    }

    public Integer getTimeForPerPatient() {
        return timeForPerPatient;
    }

    public void setTimeForPerPatient(Integer timeForPerPatient) {
        this.timeForPerPatient = timeForPerPatient;
    }

    public Boolean getFree() {
        return free;
    }

    public void setFree(Boolean free) {
        this.free = free;
    }

    public String getPracticeRemark() {
        return practiceRemark;
    }

    public void setPracticeRemark(String practiceRemark) {
        this.practiceRemark = practiceRemark;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public String getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(String updatedDate) {
        this.updatedDate = updatedDate;
    }

    public JsonElement getWorkingSet() {
        return workingSet;
    }

    public void setWorkingSet(JsonElement workingSet) {
        this.workingSet = workingSet;
    }

}