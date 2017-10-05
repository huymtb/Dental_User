package com.prostage.l_pha.dental_user.server;

import android.util.Log;

import com.prostage.l_pha.dental_user.common.Constants;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Credentials;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ServiceGenerator {

    private static final int CONNECT_TIMEOUT = 15;
    private static final int WRITE_TIMEOUT = 60;
    private static final int TIMEOUT = 60;

    private static HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
    //=================================================================================
    private static OkHttpClient.Builder httpClient = new OkHttpClient.Builder()
            .connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)
            .writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS)
            .readTimeout(TIMEOUT, TimeUnit.SECONDS)
            .addInterceptor(loggingInterceptor);
    //=================================================================================
    private static Retrofit.Builder builder = new Retrofit
            .Builder()
            .baseUrl(Constants.API_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create());

    private static Retrofit retrofit = builder.build();
    //=================================================================================
    public static <S> S createServiceLogin(Class<S> serviceClass, String username, String password) {

        final String auth = Credentials.basic(username, password);
        Log.e("login", httpClient.toString());
        httpClient.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request original = chain.request();
                Request.Builder requestBuilder = original
                        .newBuilder()
                        .header(Constants.AUTH, auth);
                requestBuilder.removeHeader(Constants.TOKEN);//remove header
                Request request = requestBuilder.build();
                return chain.proceed(request);
            }
        });
        builder.client(httpClient.build());
        retrofit = builder.build();
        return retrofit.create(serviceClass);
    }
    //=================================================================================
    public static <S> S createServiceMain(Class<S> serviceClass, final String authToken) {
        Log.e("get", httpClient.toString());
        httpClient.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request original = chain.request();
                Request.Builder requestBuilder = original
                        .newBuilder()
                        .header(Constants.TOKEN, authToken)
                        .header(Constants.CONTENT_TYPE, "application/json");
                Request request = requestBuilder.build();
                return chain.proceed(request);
            }
        });
        builder.client(httpClient.build());
        retrofit = builder.build();
        return retrofit.create(serviceClass);
    }
}