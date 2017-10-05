package com.prostage.l_pha.dental_user.model.server_model.user_model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ReservationModel implements Serializable {

    @SerializedName("id")
    @Expose
    private Integer id;

    @SerializedName("reservationDate")
    @Expose
    private String reservationDate;

    @SerializedName("reservationDay")
    @Expose
    private String reservationDay;

    @SerializedName("reservationHour")
    @Expose
    private Integer reservationHour;

    @SerializedName("reservationMin")
    @Expose
    private Integer reservationMin;

    @SerializedName("reservationSec")
    @Expose
    private Integer reservationSec;

    @SerializedName("status")
    @Expose
    private String status;

    @SerializedName("fcm1")
    @Expose
    private String fcm1;

    @SerializedName("fcm2")
    @Expose
    private String fcm2;

    @SerializedName("createdBy")
    @Expose
    private Integer createdBy;

    @SerializedName("createdDate")
    @Expose
    private Object createdDate;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getReservationDate() {
        return reservationDate;
    }

    public void setReservationDate(String reservationDate) {
        this.reservationDate = reservationDate;
    }

    public String getReservationDay() {
        return reservationDay;
    }

    public void setReservationDay(String reservationDay) {
        this.reservationDay = reservationDay;
    }

    public Integer getReservationHour() {
        return reservationHour;
    }

    public void setReservationHour(Integer reservationHour) {
        this.reservationHour = reservationHour;
    }

    public Integer getReservationMin() {
        return reservationMin;
    }

    public void setReservationMin(Integer reservationMin) {
        this.reservationMin = reservationMin;
    }

    public Integer getReservationSec() {
        return reservationSec;
    }

    public void setReservationSec(Integer reservationSec) {
        this.reservationSec = reservationSec;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getFcm1() {
        return fcm1;
    }

    public void setFcm1(String fcm1) {
        this.fcm1 = fcm1;
    }

    public String getFcm2() {
        return fcm2;
    }

    public void setFcm2(String fcm2) {
        this.fcm2 = fcm2;
    }

    public Integer getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Integer createdBy) {
        this.createdBy = createdBy;
    }

    public Object getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Object createdDate) {
        this.createdDate = createdDate;
    }

}