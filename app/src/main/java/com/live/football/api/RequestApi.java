package com.hoanmy.football.api;

import com.google.gson.JsonElement;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;
import rx.android.BuildConfig;

/**
 * Created by ADMIN on 6/21/2021.
 */

public class RequestApi {
    private static Api api;

    public static Api getInstance() {
        if (api == null) {
            OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.setLevel(BuildConfig.DEBUG ? HttpLoggingInterceptor.Level.BODY : HttpLoggingInterceptor.Level.NONE);
            httpClient.addInterceptor(interceptor);

            try {
                // Create a trust manager that does not validate certificate chains
                final TrustManager[] trustAllCerts = new TrustManager[]{
                        new X509TrustManager() {
                            @Override
                            public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) {
                            }

                            @Override
                            public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) {
                            }

                            @Override
                            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                                return new java.security.cert.X509Certificate[]{};
                            }
                        }
                };
                final SSLContext sslContext = SSLContext.getInstance("SSL");
                sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
                // Create an ssl socket factory with our all-trusting manager
                final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

                httpClient.sslSocketFactory(sslSocketFactory);
                httpClient.hostnameVerifier(new HostnameVerifier() {
                    @Override
                    public boolean verify(String hostname, SSLSession session) {
                        return true;
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }

            api = new Retrofit.Builder()
                    .baseUrl("https://veboapi.90phut.xyz")
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .client(httpClient.build())
                    .build().create(Api.class);
        }

        return api;
    }


    public interface Api {
        @GET("/api/app/match/list?")
        Observable<JsonElement> getMatchList(@Query("date") String date_now);

        @GET("/api/app/match/live")
        Observable<JsonElement> getMatchLive();

        @GET("/api/app/match/links?")
        Observable<JsonElement> getMatchLiveStream(@Query("match_id") String _id);

        @GET("/api/app/match/detail?")
        Observable<JsonElement> getMatchDetails(@Query("match_id") String _id);

        @GET("/api/app/match/facts?")
        Observable<JsonElement> getMatchProgress(@Query("match_id") String _id);

        @GET("/api/config")
        Observable<JsonElement> getConfig();


    }
}
