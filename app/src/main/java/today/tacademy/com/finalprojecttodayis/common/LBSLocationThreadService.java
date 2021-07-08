package today.tacademy.com.finalprojecttodayis.common;

import android.app.Service;
import android.content.Intent;
import android.location.LocationManager;
import android.os.IBinder;
import android.support.annotation.Nullable;

/**
 * Created by Tacademy on 2017-11-04.
 */

public class LBSLocationThreadService extends Service {
    private String LBS_INTENT_ACTION = "com.tacademy.today.PER_LOCATION";
    private Thread startUpThread;
    private LocationManager manager;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    // startService 호출시 동작
    public void onCreate() {super.onCreate();}

//    LBSLocationListener locationListener;


//
//    public int onStartCommand(Intent intent, int flag, int startID){
//        super.onStartCommand(intent, flag, startID);
//        if(startUpThread == null || !startUpThread.isAlive()){
//            startUpThread = new Thread(new Runnable() {
//                @Override
//                public void run() {
//                    Looper.prepare();
//
//                    //현재시스템의 위치관리자 객체를 획득함
////                    lbsManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
//
//                    // update 빈도
//
//                    //
//                }
//            });
//        }
//        return 11;
//    }
}
