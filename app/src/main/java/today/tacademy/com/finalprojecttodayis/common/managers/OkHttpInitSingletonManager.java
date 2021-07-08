package today.tacademy.com.finalprojecttodayis.common.managers;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;

/**
 * Created by Tacademy on 2017-10-30.
 */

public class OkHttpInitSingletonManager {
    private static OkHttpClient okHttpClient;
    private static final int OKHTTP_INIT_VALUE = 15;
    static {
        okHttpClient = new OkHttpClient.Builder()
                .retryOnConnectionFailure(true)
                .connectTimeout(OKHTTP_INIT_VALUE, TimeUnit.SECONDS)
                .readTimeout(OKHTTP_INIT_VALUE, TimeUnit.SECONDS)
                .build();
    }

    public static OkHttpClient getOkHttpClient(){
//        if( okHttpClient != null){
//            return okHttpClient;
//        }else {
//            okHttpClient = new OkHttpClient.Builder()
//                    .retryOnConnectionFailure(true)
//                    .connectTimeout(OKHTTP_INIT_VALUE, TimeUnit.SECONDS)
//                    .readTimeout(OKHTTP_INIT_VALUE, TimeUnit.SECONDS)
//                    .build();
//        }
//        return okHttpClient;

        if(okHttpClient == null){
            okHttpClient = new OkHttpClient.Builder()
                    .retryOnConnectionFailure(true)
                    .connectTimeout(OKHTTP_INIT_VALUE, TimeUnit.SECONDS)
                    .readTimeout(OKHTTP_INIT_VALUE, TimeUnit.SECONDS)
                    .build();
        }
        return okHttpClient;
    }

}
