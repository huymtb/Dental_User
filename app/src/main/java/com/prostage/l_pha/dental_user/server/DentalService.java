package com.prostage.l_pha.dental_user.server;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.prostage.l_pha.dental_user.R;
import com.prostage.l_pha.dental_user.common.Constants;
import com.prostage.l_pha.dental_user.model.server_model.ResponseModel;
import com.prostage.l_pha.dental_user.utils.SharedHelper;
import com.prostage.l_pha.dental_user.utils.UtilsHelper;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by USER on 15-May-17.
 */

public class DentalService {

    private DentalAPI dentalAPI;
    private SharedHelper sharedHelper;
    private Context mContext;
    private OnLoadResult onLoadResult;
    private ProgressDialog progressBar;

    public DentalService(Context context, OnLoadResult onLoadResult) {
        this.onLoadResult = onLoadResult;
        this.mContext = context;
        sharedHelper = new SharedHelper(mContext);
    }

    //=============================================================================================
    public void requestApi(final Call<ResponseModel> call, final int idRequest, boolean isShowProgressbar) {
        if (call == null) {
            return;
        }

        if (isShowProgressbar) {
            progressBar = new ProgressDialog(mContext);
            progressBar.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            progressBar.show();
            progressBar.setContentView(R.layout.dialog_progress_bar);
        }

        call.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                if (mContext instanceof Activity) {
                    if (!((Activity) mContext).isFinishing())
                        if (progressBar != null && progressBar.isShowing()) {
                            progressBar.dismiss();
                        }
                }

                ResponseModel responseModel = response.body();
                if (responseModel != null) {
                    if (responseModel.getStatus() == 200) {
                        onLoadResult.onLoadComplete(responseModel.getMessage().toString(), idRequest);
                    } else {
                        Toast.makeText(mContext, responseModel.getStatus(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    onLoadResult.onLoadError(mContext.getString(R.string.unknown_error_network), idRequest, 0);
                }
            }

            @Override
            public void onFailure(Call<ResponseModel> call, Throwable t) {
                if (mContext instanceof Activity) {
                    if (!((Activity) mContext).isFinishing())
                        if ((progressBar != null) && progressBar.isShowing()) {
                            progressBar.dismiss();
                        }
                }
                onLoadResult.onLoadError("", idRequest, 0);
            }
        });
    }

    //=============================================================================================
    public Call<ResponseModel> login(String username, String password) {
        dentalAPI = ServiceGenerator.createServiceLogin(DentalAPI.class, username, password);
        return dentalAPI.getToken();
    }

    //=============================================================================================
    public Call<ResponseModel> getUserInfoById(int id) {
        String authToken = sharedHelper.getString(Constants.TOKEN);
        dentalAPI = ServiceGenerator.createServiceMain(DentalAPI.class, authToken);
        return dentalAPI.getUserInfoById(id);
    }

    //=============================================================================================
    public Call<ResponseModel> getAdminInfoById(int id) {
        String authToken = sharedHelper.getString(Constants.TOKEN);
        dentalAPI = ServiceGenerator.createServiceMain(DentalAPI.class, authToken);
        return dentalAPI.getAdminInfoById(id);
    }

    //=============================================================================================
    public Call<ResponseModel> setPassword(int id, String newPassword, String oldPassword) {
        String authToken = sharedHelper.getString(Constants.TOKEN);
        dentalAPI = ServiceGenerator.createServiceMain(DentalAPI.class, authToken);

        JsonObject property = new JsonObject();
        property.addProperty("oldPassword", oldPassword);
        property.addProperty("newPassword", newPassword);

        JsonObject data = new JsonObject();
        data.add("data", property);
        return dentalAPI.setPassword(id, data);
    }

    //=============================================================================================
    public Call<ResponseModel> setName(int id, String firstName, String firstNickName, String lastName, String lastNickName, String reservationDate) {
        String authToken = sharedHelper.getString(Constants.TOKEN);
        dentalAPI = ServiceGenerator.createServiceMain(DentalAPI.class, authToken);

        JsonObject property = new JsonObject();
        property.addProperty("firstName", firstName);
        property.addProperty("firstNickName", firstNickName);
        property.addProperty("lastName", lastName);
        property.addProperty("lastNickName", lastNickName);
        property.addProperty("reservationDate", reservationDate);

        JsonObject info = new JsonObject();
        info.add("info", property);

        JsonObject data = new JsonObject();
        data.add("data", info);

        return dentalAPI.setName(id, data);
    }
}
