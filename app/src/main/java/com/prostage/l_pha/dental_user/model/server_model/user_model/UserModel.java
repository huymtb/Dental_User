package com.prostage.l_pha.dental_user.model.server_model.user_model;

import com.google.gson.JsonElement;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class UserModel implements Serializable {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("adminId")
    @Expose
    private Integer adminId;
    @SerializedName("birthday")
    @Expose
    private String birthday;
    @SerializedName("firstName")
    @Expose
    private String firstName;
    @SerializedName("firstNickName")
    @Expose
    private String firstNickName;
    @SerializedName("lastName")
    @Expose
    private String lastName;
    @SerializedName("lastNickName")
    @Expose
    private String lastNickName;
    @SerializedName("note")
    @Expose
    private String note;
    @SerializedName("free")
    @Expose
    private Boolean free;
    @SerializedName("createdDate")
    @Expose
    private String createdDate;
    @SerializedName("updatedDate")
    @Expose
    private String updatedDate;
    @SerializedName("reservations")//phai cung ten tren api
    @Expose
    private JsonElement reservationModels;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getAdminId() {
        return adminId;
    }

    public void setAdminId(Integer adminId) {
        this.adminId = adminId;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getFirstNickName() {
        return firstNickName;
    }

    public void setFirstNickName(String firstNickName) {
        this.firstNickName = firstNickName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getLastNickName() {
        return lastNickName;
    }

    public void setLastNickName(String lastNickName) {
        this.lastNickName = lastNickName;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Boolean getFree() {
        return free;
    }

    public void setFree(Boolean free) {
        this.free = free;
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

    public JsonElement getReservationModels() {
        return reservationModels;
    }

    public void setReservationModels(JsonElement reservationModels) {
        this.reservationModels = reservationModels;
    }

}