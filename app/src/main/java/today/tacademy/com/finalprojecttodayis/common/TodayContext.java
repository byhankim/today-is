package today.tacademy.com.finalprojecttodayis.common;

import android.app.Application;
import android.content.Context;
import android.os.StrictMode;

/**
 * Created by Tacademy on 2017-11-07.
 */

public class TodayContext extends Application {
    private static TodayRESTClient todayRESTClient;
    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        /*
        *   병목현상 및 network 현상 보기
        *   배포시 주석 처리할 것
         */
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                .detectDiskReads()
                .detectDiskWrites()
                .detectAll()
                .penaltyLog()
                .build());
        StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
                .detectLeakedSqlLiteObjects()
                .detectLeakedClosableObjects()
                .penaltyLog()
                .penaltyDeath()
                .build());

        context = this;
        todayRESTClient = RetrofitOkHttpWarmUp
                .createWeatherRESTService(TodayRESTClient.class);
    }

    public static Context getContext(){return context;}
    public static TodayRESTClient getTodayRESTClient(){
        if(todayRESTClient != null){
            return todayRESTClient;
        }else{
            return todayRESTClient = RetrofitOkHttpWarmUp
                    .createWeatherRESTService(TodayRESTClient.class);
        }
    }
}
