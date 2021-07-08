package today.tacademy.com.finalprojecttodayis.common;

import android.content.Context;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import today.tacademy.com.finalprojecttodayis.GalleryTestActivity;
import today.tacademy.com.finalprojecttodayis.R;

/**
 * Created by Tacademy on 2017-11-06.
 */

public class RetrofitOkHttpWarmUp {

    private static RetrofitOkHttpWarmUp retrofitOkHttpWarmUpObj;
    private static final int ALL_TIMEOUT = 15;
    private static final String SKPLANET_LIVING_BASE_URL = "http://apis.skplanetx.com/";
    private static final String SKPLANET_API_KEY;
    static{
        SKPLANET_API_KEY = GalleryTestActivity.getAppContext().getResources().getString(R.string.app_key);
    }

    private OkHttpClient okHttpClient;
    private Retrofit retrofit;

    private RetrofitOkHttpWarmUp(){
        HttpLoggingInterceptor httpLogging = new HttpLoggingInterceptor();

        // logging lvl 설정
        httpLogging.setLevel(HttpLoggingInterceptor.Level.BODY);
        RequestResponseTimeInterceptor totalTime = new RequestResponseTimeInterceptor();
        RequestHeaderInterceptor headerSetInterceptor  = new RequestHeaderInterceptor();

        okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(httpLogging)
                .addInterceptor(totalTime)
                .addInterceptor(headerSetInterceptor)
                .addInterceptor(new RetryInterceptor())
                .connectTimeout(ALL_TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(ALL_TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(ALL_TIMEOUT, TimeUnit.SECONDS)
                .build();

        retrofit = new Retrofit.Builder()
                .baseUrl(SKPLANET_LIVING_BASE_URL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

    }

    // Interceptor calculating overall time taken
    private class RequestResponseTimeInterceptor implements Interceptor{
        @Override
        public Response intercept(Chain chain) throws IOException {
            okhttp3.Request request = chain.request();

            long t1 = System.nanoTime();
            L.Log("chain 요청", String.format(" %s on %s%n%s",
                    request.url(), chain.connection(), request.headers()));

            Response response = chain.proceed(request);
            long t2 = System.nanoTime();
            L.Log("chain 응답",
                    String.format(" %s in %.1fms%n%s",
                            response.request().url(),
                            (t2 - t1) / 1e6d, response.headers()));

            return response;
        }
    }

    // Interceptor setting Header
    private class RequestHeaderInterceptor implements Interceptor{
        @Override
        public Response intercept(Chain chain) throws IOException {
            Request original = chain.request();
            Request.Builder reqBuilder = original.newBuilder()
                    .addHeader("Accept", "application/json")
                    .addHeader("appKey", SKPLANET_API_KEY);

            Request request = reqBuilder.build();
            return chain.proceed(request);
        }
    }

    // Interceptor processing Retry twice(make it 3 times)
    private static class RetryInterceptor implements Interceptor{
        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request();
            Response response = chain.proceed(request);

            int tryCount = 0;
            int maxLimit = 3;

            while(!response.isSuccessful() && tryCount < maxLimit){
                L.Log("interceptor", "Request failed - " + tryCount);
                tryCount++;
                response = chain.proceed(request);
                if(tryCount == maxLimit){
                    Context application = GalleryTestActivity.getAppContext();
                    L.Log("interceptor 문제발생");
                }
            }
            return response;
        }
    }
    private <T> T getRESTService(Class<T> restClass){
        return retrofit.create(restClass);
    }

    // METHOD returns REST Service Object through Singleton
    public static <T> T createWeatherRESTService(Class<T> restInterClass){
        if(retrofitOkHttpWarmUpObj == null){
            retrofitOkHttpWarmUpObj = new RetrofitOkHttpWarmUp();
        }
        return retrofitOkHttpWarmUpObj.getRESTService(restInterClass);
    }


}
