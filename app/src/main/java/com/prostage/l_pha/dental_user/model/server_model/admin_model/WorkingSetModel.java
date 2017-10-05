package com.prostage.l_pha.dental_user.model.server_model.admin_model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by USER on 18-May-17.
 */

public class WorkingSetModel implements Serializable {
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("idWorkingDay")
    @Expose
    private Integer idWorkingDay;
    @SerializedName("firstShiftFromHour")
    @Expose
    private Integer firstShiftFromHour;
    @SerializedName("firstShiftFromMin")
    @Expose
    private Integer firstShiftFromMin;
    @SerializedName("firstShiftToHour")
    @Expose
    private Integer firstShiftToHour;
    @SerializedName("firstShiftToMin")
    @Expose
    private Integer firstShiftToMin;
    @SerializedName("secondShiftFromHour")
    @Expose
    private Integer secondShiftFromHour;
    @SerializedName("secondShiftFromMin")
    @Expose
    private Integer secondShiftFromMin;
    @SerializedName("secondShiftToHour")
    @Expose
    private Integer secondShiftToHour;
    @SerializedName("secondShiftToMin")
    @Expose
    private Integer secondShiftToMin;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getIdWorkingDay() {
        return idWorkingDay;
    }

    public void setIdWorkingDay(Integer idWorkingDay) {
        this.idWorkingDay = idWorkingDay;
    }

    public Integer getFirstShiftFromHour() {
        return firstShiftFromHour;
    }

    public void setFirstShiftFromHour(Integer firstShiftFromHour) {
        this.firstShiftFromHour = firstShiftFromHour;
    }

    public Integer getFirstShiftFromMin() {
        return firstShiftFromMin;
    }

    public void setFirstShiftFromMin(Integer firstShiftFromMin) {
        this.firstShiftFromMin = firstShiftFromMin;
    }

    public Integer getFirstShiftToHour() {
        return firstShiftToHour;
    }

    public void setFirstShiftToHour(Integer firstShiftToHour) {
        this.firstShiftToHour = firstShiftToHour;
    }

    public Integer getFirstShiftToMin() {
        return firstShiftToMin;
    }

    public void setFirstShiftToMin(Integer firstShiftToMin) {
        this.firstShiftToMin = firstShiftToMin;
    }

    public Integer getSecondShiftFromHour() {
        return secondShiftFromHour;
    }

    public void setSecondShiftFromHour(Integer secondShiftFromHour) {
        this.secondShiftFromHour = secondShiftFromHour;
    }

    public Integer getSecondShiftFromMin() {
        return secondShiftFromMin;
    }

    public void setSecondShiftFromMin(Integer secondShiftFromMin) {
        this.secondShiftFromMin = secondShiftFromMin;
    }

    public Integer getSecondShiftToHour() {
        return secondShiftToHour;
    }

    public void setSecondShiftToHour(Integer secondShiftToHour) {
        this.secondShiftToHour = secondShiftToHour;
    }

    public Integer getSecondShiftToMin() {
        return secondShiftToMin;
    }

    public void setSecondShiftToMin(Integer secondShiftToMin) {
        this.secondShiftToMin = secondShiftToMin;
    }

}