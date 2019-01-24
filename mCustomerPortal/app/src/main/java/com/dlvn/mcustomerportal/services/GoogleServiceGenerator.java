package com.dlvn.mcustomerportal.services;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import com.dlvn.mcustomerportal.common.Constant;
import com.dlvn.mcustomerportal.utils.NullStringToEmptyAdapterFactory;
import com.google.gson.GsonBuilder;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.TextUtils;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class GoogleServiceGenerator {

    private static final String APITOKEN_PARAM = "Token";

    private static OkHttpClient.Builder httpClient = new OkHttpClient.Builder().connectTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS).writeTimeout(60, TimeUnit.SECONDS);

    private Retrofit.Builder builder;

    public <S> S createService(Class<S> serviceClass) {
        builder = new Retrofit.Builder().baseUrl("https://www.google.com/recaptcha/api/")
                .addConverterFactory(GsonConverterFactory.create(
                        new GsonBuilder().registerTypeAdapterFactory(new NullStringToEmptyAdapterFactory()).create()));

        return createServiceWithTokenParam(serviceClass, null);
    }

    public <S> S createService(Context app, Class<S> serviceClass) {

        builder = new Retrofit.Builder().baseUrl("https://www.google.com/recaptcha/api/")
                .addConverterFactory(GsonConverterFactory.create(
                        new GsonBuilder().registerTypeAdapterFactory(new NullStringToEmptyAdapterFactory()).create()));

        return createServiceWithTokenParam(app, serviceClass, null);
    }

    public <S> S createService(Context app, Class<S> serviceClass, String url) {

        builder = new Retrofit.Builder().baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create(
                        new GsonBuilder().registerTypeAdapterFactory(new NullStringToEmptyAdapterFactory()).create()));

        return createServiceWithTokenParam(app, serviceClass, null);
    }


    public <S> S createServiceWithTokenParam(Class<S> serviceClass, final String authToken) {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(Constant.LOG_LEVEL);

        if (authToken != null) {
            httpClient.addInterceptor(logging).addInterceptor(new Interceptor() {
                @Override
                public Response intercept(Interceptor.Chain chain) throws IOException {

                    Request original = chain.request();
                    HttpUrl originalHttpUrl = original.url();
                    HttpUrl url = originalHttpUrl.newBuilder().build();
                    // Request customization: add request headers
                    Request.Builder requestBuilder;

                    if (!TextUtils.isEmpty(authToken)) {
                        requestBuilder = original.newBuilder().addHeader(APITOKEN_PARAM, authToken).url(url);
                    } else
                        requestBuilder = original.newBuilder().addHeader(APITOKEN_PARAM, "").url(url);

                    Request request = requestBuilder.build();
                    return chain.proceed(request);
                }
            });
        } else {
            httpClient.addInterceptor(logging);
        }

        OkHttpClient client = httpClient.build();
        Retrofit retrofit = builder.client(client).build();
        return retrofit.create(serviceClass);
    }

    public synchronized <S> S createServiceWithTokenParam(final Context app, Class<S> serviceClass,
                                                          final String authToken) {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(Constant.LOG_LEVEL);

        httpClient.addInterceptor(logging).addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Interceptor.Chain chain) throws IOException {

                ConnectivityManager cm = (ConnectivityManager) app.getSystemService(Context.CONNECTIVITY_SERVICE);

                NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
                boolean isConnected = (activeNetwork != null && activeNetwork.isConnectedOrConnecting());
                if (!isConnected)
                    throw new IOException("NO_INTERNET");
                else {
                    Request original = chain.request();
                    HttpUrl originalHttpUrl = original.url();
                    HttpUrl url = originalHttpUrl.newBuilder().build();
                    // Request customization: add request headers
                    Request.Builder requestBuilder;

                    if (!TextUtils.isEmpty(authToken)) {
                        requestBuilder = original.newBuilder().addHeader(APITOKEN_PARAM, authToken).url(url);
                    } else
                        requestBuilder = original.newBuilder().addHeader(APITOKEN_PARAM, "").url(url);

                    Request request = requestBuilder.build();
                    return chain.proceed(request);
                }
            }
        });

        OkHttpClient client = httpClient.build();
        Retrofit retrofit = builder.client(client).build();
        return retrofit.create(serviceClass);
    }
}
