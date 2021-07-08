package today.tacademy.com.finalprojecttodayis;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.SettingsClient;

import today.tacademy.com.finalprojecttodayis.common.L;
import today.tacademy.com.finalprojecttodayis.common.managers.MySharedPreferencesManager;

/**
 * Created by Tacademy on 2017-11-13.
 */

public class TodaySplashActivity extends AppCompatActivity{

    private Handler handler = new Handler();
    private NetworkInfo info;
    private final static long DELAY_TIME = 500L;
    private boolean isNetworkLocation, isGPSLocation;

    LocationManager locationManager;
    private SettingsClient mSettingsClient;
    private LocationRequest mLocationRequest;
    private LocationSettingsRequest mLocationSettingsRequest;
    private LocationCallback mLocationCallback;
    private Location mCurrentLocation;
    //구글 로케이션을 연결/연결실패를 처리해주는 클라이언트
    FusedLocationProviderClient mFusedLocationClient;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        ConnectivityManager manager =
                (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        info = manager.getActiveNetworkInfo();

        if (locationManager != null) {
            isGPSLocation = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            isNetworkLocation = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
            L.Log("gps, network", String.valueOf(isGPSLocation + ", " + isNetworkLocation));
        }

    }   // oncreate ends

    @Override
    protected void onStart() {
        super.onStart();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // 인터넷 설정이 되어있을시
                if (info != null && info.isConnectedOrConnecting()) {
                    // 첫실행이면...gps켜졌냐 안켜졌냐 ? 안켜졌으면 꺼버리기
                    //            아니면 킨 상태로 firstSettingsActivity로 이동하기
                    if(MySharedPreferencesManager.getInstance().loadCheckedItemList().isEmpty() ||
                            "".equals(MySharedPreferencesManager.getInstance().loadCheckedItemList())){
                        if (isGPSLocation) {
                            Intent intent = new Intent(TodaySplashActivity.this, FirstSettingsActivity.class);
                            intent.putExtra("provider", LocationManager.GPS_PROVIDER);
                            startActivity(intent);
                            finish();
                        } else if (isNetworkLocation) {
                            Intent intent = new Intent(TodaySplashActivity.this, FirstSettingsActivity.class);
                            intent.putExtra("provider", LocationManager.NETWORK_PROVIDER);
                            startActivity(intent);
                            finish();
                        } else {
                            // 단말기 위치설정이 되어있지 않음
                            AlertDialog.Builder locationDisabledDialog = new AlertDialog.Builder(TodaySplashActivity.this);
                            locationDisabledDialog.setTitle("위치설정이 되어있지 않아 앱을 종료합니다.")
                                    .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            Toast.makeText(TodaySplashActivity.this,
                                                    "위치 설정을 켜신 후 다시 실행해주세요", Toast.LENGTH_LONG).show();
                                            finish();
                                        }
                                    });     // 여기가 안된댜 ㅠㅠs
                            locationDisabledDialog.create();
                            locationDisabledDialog.show();
//                            finish();   // 여다 함 해보자
                        }
                    }else { // 위치 또 받을 필요 없으니 바로 실행한다
                        startActivity(new Intent(TodaySplashActivity.this, GalleryTestActivity.class));
                        finish();   // activity stack에서 pop한다
                    }
                } else {    // 인터넷 설정 안 되어있으면...
                    Intent intent = new Intent(Settings.ACTION_WIRELESS_SETTINGS);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                }

            }
        }, 500);

    }   // onStart() ends


    /**
     * 사용자가 위치설정을 하고 Back Key를 눌렀을때 실행
     */
    @Override
    public void onRestart() {
        super.onRestart();
        startActivity(new Intent(this, TodaySplashActivity.class));
        finish();
    }
    /**
     * 위치정보 제거
     */
    @Override
    public void onStop() {
        super.onStop();
        if (mFusedLocationClient != null) {
            mFusedLocationClient.removeLocationUpdates(mLocationCallback);
        }
    }
}
