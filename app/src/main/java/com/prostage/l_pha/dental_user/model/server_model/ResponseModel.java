package com.prostage.l_pha.dental_user.model.server_model;

import com.google.gson.JsonElement;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ResponseModel implements Serializable {

        @SerializedName("message")
        @Expose
        private JsonElement message;
        @SerializedName("timestamp")
        @Expose
        private Long timestamp;
        @SerializedName("status")
        @Expose
        private Integer status;

        public JsonElement getMessage() {
            return message;
        }

        public void setMessage(JsonElement message) {
            this.message = message;
        }

        public Long getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(Long timestamp) {
            this.timestamp = timestamp;
        }

        public Integer getStatus() {
            return status;
        }

        public void setStatus(Integer status) {
            this.status = status;
        }

    }