package com.prostage.l_pha.dental_user.server;

/**
 * Created by don on 11/30/16.
 */

public interface OnLoadResult {

    void onLoadComplete(String response, int idRequest);

    void onLoadError(String response, int idRequest, int errorCode);
}
