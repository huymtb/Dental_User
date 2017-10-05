package com.prostage.l_pha.dental_user.common;

/**
 * Created by USER on 30-Mar-17.
 */

public class Constants {

    //firebase
    public static final String FIRE_URL="https://dentalapp-ada99.firebaseio.com";
    public static final String STORAGE_URL="gs://dentalapp-ada99.appspot.com";
    public static final String CHAT_REF = "chat_data";
    public static final String CHAT_IMAGE_REF = "dental-image";
//    public static final String CHAT_AUDIO_REF = "dental-audio";

    //server
    public static final String API_BASE_URL = "http://153.126.150.57:5556";
    public static final String TOKEN = "X-Auth-Token";
    public static final String AUTH = "Authorization";
    public static final String CONTENT_TYPE = "Content-Type";
    public static final String GET_ID = "id";
    public static final String USERNAME = "username";

    //tag
    public static final int TAG_LOGIN = 0;
    public static final int TAG_USER = 1;
    public static final int TAG_ADMIN = 2;
    public static final int TAG_CHANGE_PASS = 3;
    public static final int TAG_CHANGE_NAME = 4;
    public static final int TAG_BACKGROUND_USER = 5;
    public static final int TAG_BACKGROUND_ADMIN = 6;
}
