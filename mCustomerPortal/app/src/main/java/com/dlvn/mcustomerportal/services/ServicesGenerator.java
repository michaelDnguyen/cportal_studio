package com.dlvn.mcustomerportal.services;

import java.io.IOException;

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

/**
 * @arthor nn.tai
 * @date Oct 20, 2016
 */
public class ServicesGenerator {
	private static final String APITOKEN_PARAM = "Token";

	private static OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

	private static Retrofit.Builder builder = new Retrofit.Builder().baseUrl(Constant.URL)
			.addConverterFactory(GsonConverterFactory.create(
					new GsonBuilder().registerTypeAdapterFactory(new NullStringToEmptyAdapterFactory()).create()));

	// private static Retrofit.Builder builder = new
	// Retrofit.Builder().baseUrl(Constant.URL)
	// .addConverterFactory(GsonConverterFactory.create(new
	// GsonBuilder().serializeNulls().create()));

	public static <S> S createService(Class<S> serviceClass) {
		return createServiceWithTokenParam(serviceClass, null);
	}

	public static <S> S createService(Context app, Class<S> serviceClass) {
		return createServiceWithTokenParam(app, serviceClass, null);
	}

	public static <S> S createServiceWithTokenParam(Class<S> serviceClass, final String authToken) {
		HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
		logging.setLevel(HttpLoggingInterceptor.Level.BODY);

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

	public static synchronized <S> S createServiceWithTokenParam(final Context app, Class<S> serviceClass,
			final String authToken) {
		HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
		logging.setLevel(HttpLoggingInterceptor.Level.BODY);

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

	// public static <S> S createService(Class<S> serviceClass) {
	// Retrofit retrofit = builder.client(httpClient.build()).build();
	// return retrofit.create(serviceClass);
	// }

	// public static <S> S createServiceWithLog(Class<S> serviceClass) {
	// HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
	// logging.setLevel(HttpLoggingInterceptor.Level.BODY);
	// Retrofit retrofit =
	// builder.client(httpClient.addInterceptor(logging).build()).build();
	//
	// return retrofit.create(serviceClass);
	// }
	//
	// public static <S> S createService(Class<S> serviceClass, final String
	// authToken) {
	// if (authToken != null) {
	// httpClient.addInterceptor(new Interceptor() {
	// @Override
	// public Response intercept(Interceptor.Chain chain) throws IOException {
	// Request original = chain.request();
	//
	// // Request customization: add request headers
	// Request.Builder requestBuilder = original.newBuilder().header("token",
	// authToken)
	// .method(original.method(), original.body());
	//
	// Request request = requestBuilder.build();
	// return chain.proceed(request);
	// }
	// });
	// }
	//
	// OkHttpClient client = httpClient.build();
	// Retrofit retrofit = builder.client(client).build();
	// return retrofit.create(serviceClass);
	// }

}
