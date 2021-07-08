package today.tacademy.com.finalprojecttodayis;

import android.Manifest;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.TypedArray;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationAvailability;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ExecutionException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import today.tacademy.com.finalprojecttodayis.common.ItemsCommonModule;
import today.tacademy.com.finalprojecttodayis.common.L;
import today.tacademy.com.finalprojecttodayis.common.PermissionSettingUtils;
import today.tacademy.com.finalprojecttodayis.common.TodayRESTClient;
import today.tacademy.com.finalprojecttodayis.common.asynctasks.AsyncTaskAddressInfo;
import today.tacademy.com.finalprojecttodayis.common.asynctasks.AsyncTaskCarwashInfo;
import today.tacademy.com.finalprojecttodayis.common.asynctasks.AsyncTaskEftcTempInfo;
import today.tacademy.com.finalprojecttodayis.common.asynctasks.AsyncTaskHumidityInfo;
import today.tacademy.com.finalprojecttodayis.common.asynctasks.AsyncTaskItemInfo;
import today.tacademy.com.finalprojecttodayis.common.asynctasks.AsyncTaskLaundryInfo;
import today.tacademy.com.finalprojecttodayis.common.asynctasks.AsyncTaskUVInfo;
import today.tacademy.com.finalprojecttodayis.common.managers.MySharedPreferencesManager;
import today.tacademy.com.finalprojecttodayis.entity.LocationVO;
import today.tacademy.com.finalprojecttodayis.entity.TinyItemVO;

import static android.content.Intent.FLAG_ACTIVITY_REORDER_TO_FRONT;
import static today.tacademy.com.finalprojecttodayis.FirstSettingsActivity.CHECKEDLIST_DONE;
import static today.tacademy.com.finalprojecttodayis.MyApplication.getContext;

/**
 * Created by Tacademy on 2017-10-23.
 */

public class GalleryTestActivity extends AppCompatActivity  {
    GoogleApiClient mClient;

    public static final String DEBUG = "GalleryTestActivity";

    // LBS
    private Intent intentService;
    private BroadcastReceiver receiveFromService;
    private String LBS_INTENT_ACTION =
            "com.tacademy.today.PER_LOCATION";
    /*
    *
    *   GPS
    *
     */
    LocationManager locationManager;
    private SettingsClient mSettingsClient;
    private LocationRequest mLocationRequest;
    private LocationSettingsRequest mLocationSettingsRequest;
    private LocationCallback mLocationCallback;
    private Location mCurrentLocation;
    //구글 로케이션을 연결/연결실패를 처리해주는 클라이언트
    FusedLocationProviderClient mFusedLocationClient;
    private String provider;

    // REST retrofit+okhttp
    private TodayRESTClient restClient;

    public static final int REQUEST_SETTINGS_ACTIVITY = 1;
    public static final int FIRST_SETTINGS_DONE = 2;

    private RelativeLayout currItem;
    private ImageView currBgr;
    private ArrayList<String> tagList;
    public ArrayList<TinyItemVO> tinyItemList;
    private ArrayList<TinyItemVO> itmListAirKorea;
    public HashMap<String, Long> itmListRefreshTime;
    public HashMap<String, Double> itmListValues;

    final public String[] wList = {"좋음", "보통", "나쁨", "보통", "매우나쁨", "심각", "보통", "좋음", "나쁨", "매우나쁨"};
    final public String[] iList = {"pm10", "pm25", "so2", "o3", "no2", "temp", "uv", "humidity", "carwash", "laundry"};
    Gallery mGallery;
    MyGalleryAdapter myGalleryAdapter;
    final Handler handler = new Handler();

    long prevTime;
    View prevView, currView = null;
    boolean isListRefreshed = true;

    TinyItemVO tempItem;

    @BindView(R.id.bgr_img)
    ImageView imgBgr;
    @BindView(R.id.tv_loc)
    TextView tagLocation;
    @BindView(R.id.tv_datetime)
    TextView tagDateTime;
    @BindView(R.id.msg_comparison)
    TextView msgComp;
    @BindView(R.id.msg_advice)
    TextView msgAdv;
    @BindView(R.id.btn_settings)
    ImageButton btnSettings;



    // Context 반환용 temp 메소드
    public static Context getAppContext(){
        return getContext();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gallery_activity);
        ButterKnife.bind(this);

        L.Log("[LAT LON]", MySharedPreferencesManager.getInstance().getLatitude() + ", " +
                MySharedPreferencesManager.getInstance().getLongitude());

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);



        /// 시간저장
        long now = System.currentTimeMillis();
        String latestTime = MySharedPreferencesManager.getInstance().getLatestAppRunDatetime();
        if(latestTime.isEmpty() || "".equals(latestTime)){
            // first timer
            // 현재시간저장하라
            MySharedPreferencesManager.getInstance().setLatestAppRunDatetime(String.valueOf(now));
        }else{  // 오늘 또킨거면 저장하지말고 처음킨거면 저장해라

            MySharedPreferencesManager.getInstance().setLatestAppRunDatetime(now + "");
        }



//        if(MySharedPreferencesManager.getInstance().getLatitude().isEmpty() ||
//                "".equals(MySharedPreferencesManager.getInstance().getLatitude())){
//            if(!locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)){
//                //단말기위치설정
//                PermissionSettingUtils.PermissionDialog.newInstance().show(getSupportFragmentManager(), "dialog");
//            }else{
//                if(Build.VERSION.SDK_INT >= 23){
//                    checkMyPermissionLocation();
//                }else{
//                    initGoogleMapLocation();
//                }
//            }
//        }





//        NetworkConstants.LOC_LATITUDE = MySharedPreferencesManager.getInstance().getLatitude();
//        NetworkConstants.LOC_LONGITUDE = MySharedPreferencesManager.getInstance().getLongitude();

//        L.Log("[LAT LON]", MySharedPreferencesManager.getInstance().getLatitude() + ", " +
//                MySharedPreferencesManager.getInstance().getLongitude());
//
//        L.Log("[LAT LON_networkConstants]", NetworkConstants.LOC_LATITUDE + ", " +
//                NetworkConstants.LOC_LONGITUDE);

        String s = "";
        tinyItemList = new ArrayList<>();
        tagList = new ArrayList<>();

        // [NOT SOLVED] 킬때마다 위치 받게할것인지 새로고침 유저에게 시켜서 위치 확인받을것인지 결정하기
        // 맨처음 시작시 Permission 얻기 꼭처리해!
        if(MySharedPreferencesManager.getInstance().getMyLocation1().isEmpty()){
            // 처음시작시

            // Intent received from first settings activity
//            Set<String> strList = MySharedPreferencesManager.getInstance().loadCheckedItemList();
//            Iterator it = strList.iterator();
//            while(it.hasNext()){
//                tagList.add(it.next().toString());
//            }
            new Thread(new Runnable() {
                @Override
                public void run() {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Intent receivedFirstSettingsList = getIntent();
                            tagList = receivedFirstSettingsList.getStringArrayListExtra(CHECKEDLIST_DONE);

                            L.Log("새로시작하는거에서 받아왔으!", tagList.size() + "");
                            itmListAirKorea = new ArrayList<>();

                            renewAddress();
                        }
                    });
                }
            }).start();

        }else{
            Set<String> myPrefs = MySharedPreferencesManager.getInstance().loadCheckedItemList();

            Iterator<String> it = myPrefs.iterator();
            while(it.hasNext()){
                tagList.add(it.next());
            }
            L.Log(tagList.toString());

            gainParsedItemsInfo();
//            renewTMCoordObsv();
        }

        // TM좌표얻기 및 관측소 얻기
//        LocationVO myLocation = renewTMCoordObsv();

        // 시간 초기화
        tagDateTime.setText(ItemsCommonModule.getCurrentDateTime());
        s = MySharedPreferencesManager.getInstance().getMyLocation1();
        tagLocation.setText(s); // 위치

        LocationVO locVO;



        L.Log("SHARED_PREF_MAIN!!!___", MySharedPreferencesManager.getInstance().loadCheckedItemList().toString());

        mGallery = (Gallery)findViewById(R.id.my_gallery);
        myGalleryAdapter = new MyGalleryAdapter(this, tinyItemList);
        mGallery.setAdapter(myGalleryAdapter);
        mGallery.setOnItemSelectedListener(gallerySelectedListener);
        mGallery.setOnItemClickListener(galleryClickListener);
    }   // onCreate() ends

    public void gainParsedItemsInfo(){
        // 환경공단 5형제 아이템 있을시 한개만 받아온다(asynctask)

        new Thread(new Runnable() {
            @Override
            public void run() {
                // runOnUiThread를 추가하고 그 안에 UI작업을 한다.
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        TinyItemVO itm;
                        if(tagList.contains("pm10") || tagList.contains("pm25")
                                || tagList.contains("o3") || tagList.contains("o3")
                                || tagList.contains("no2")){
                            try{
                                itmListAirKorea = new AsyncTaskItemInfo(GalleryTestActivity.getAppContext()).execute().get();
                                L.Log("taglist에 환경공단 O", itmListAirKorea.size() + "");
                            }catch(ExecutionException e){
                                L.Log("items asynctask: ", "execution error__" + e);
                            }catch(InterruptedException e){
                                L.Log("items asynctask: ", "interrupted error__ " + e);
                            }
                        }

                        // sk용 rest client
//        restClient = TodayContext.getTodayRESTClient();
//        ArrayList<Call<? extends Cloneable>> returnedCalls = new ArrayList<>();
//        L.Log("itmAKList size:", itmListAirKorea.size() + "");
                        Iterator it = tagList.iterator();
                        while(it.hasNext()){
                            String o = it.next().toString();
                            L.Log("////TAGLIST____1/////", o);
                            itm = new TinyItemVO();
                            tempItem = new TinyItemVO();
                            switch(o){
                                // 환경공단꺼면 arrayList값 갖고온다
                                case "pm10":
                                    itm = itmListAirKorea.get(0);
                                    itm.sortItemsByUnitStandard();
                                    tinyItemList.add(itm);
                                    break;
                                case "pm25":
                                    itm = itmListAirKorea.get(1);
                                    itm.sortItemsByUnitStandard();
                                    tinyItemList.add(itm);
                                    break;
                                case "so2":
                                    itm = itmListAirKorea.get(2);
                                    itm.sortItemsByUnitStandard();
                                    tinyItemList.add(itm);
                                    break;
                                case "o3":
                                    itm = itmListAirKorea.get(3);
                                    itm.sortItemsByUnitStandard();
                                    tinyItemList.add(itm);
                                    break;
                                case "no2":
                                    itm = itmListAirKorea.get(4);
                                    itm.sortItemsByUnitStandard();
                                    tinyItemList.add(itm);
                                    break;
                                case "temp":

                                    L.Log("////TAGLIST____2/////", o);
                                    try {
                                        String tVal = new AsyncTaskEftcTempInfo(GalleryTestActivity.getAppContext()).execute().get();
                                        itm.itemTag = "temp";
                                        itm.value = Double.parseDouble(tVal);
                                        L.Log("////TAGLIST____3/////", itm.value + "");
                                    } catch (ExecutionException e) {
                                        L.Log("temp asynctask: ", "execution error__" + e);
                                    } catch (InterruptedException e) {
                                        L.Log("temp asynctask: ", "interrupted error__ " + e);
                                    }

                                    L.Log("////CHECKEDLIST4/////", itm.value + "");
                                    itm.sortItemsByUnitStandard();
                                    tinyItemList.add(itm);

                                    break;
                                case "uv":
                                    try {
                                        String result = new AsyncTaskUVInfo(GalleryTestActivity.getAppContext()).execute().get();
                                        try {
                                            itm.itemTag = "uv";
                                            itm.value = Double.parseDouble(result.trim());
                                        }catch(NullPointerException e){
                                            result = new AsyncTaskUVInfo(GalleryTestActivity.getAppContext()).execute().get();
                                            itm.value = Double.parseDouble(result.trim());
                                        }

                                    } catch (ExecutionException e) {
                                        Log.e("uv asynctask: ", "execution error__" + e);
                                    } catch (InterruptedException e) {
                                        Log.e("uv asynctask: ", "interrupted error__ " + e);
                                    }

                                    itm.sortItemsByUnitStandard();
                                    tinyItemList.add(itm);

                                    L.Log("ccccc", itm.value + ", " + itm.grade);

                                    break;
                                case "humidity":
                                    try {
                                        itm = new AsyncTaskHumidityInfo(GalleryTestActivity.getAppContext()).execute().get();
                                        itm.itemTag = "humidity";
                                    } catch (ExecutionException e) {
                                        L.Log("humidity asynctask: ", "execution error__" + e);
                                    } catch (InterruptedException e) {
                                        L.Log("humidity asynctask: ", "interrupted error__ " + e);
                                    }
                                    itm.sortItemsByUnitStandard();
                                    tinyItemList.add(itm);


                                    break;
                                case "carwash":
                                    try {
                                        itm = new AsyncTaskCarwashInfo(GalleryTestActivity.getAppContext()
                                                , MySharedPreferencesManager.getInstance()
                                                .getAdminArea1()).execute().get();
                                    } catch (ExecutionException e) {
                                        L.Log("carwash asynctask: ", "execution error__" + e);
                                    } catch (InterruptedException e) {
                                        L.Log("carwash asynctask: ", "interrupted error__ " + e);
                                    }
                                    itm.sortItemsByUnitStandard();
                                    tinyItemList.add(itm);

                                    break;
                                case "laundry":
                                    try {
                                        itm = new AsyncTaskLaundryInfo(GalleryTestActivity.getAppContext()).execute().get();
                                    } catch (ExecutionException e) {
                                        L.Log("laundry asynctask: ", "execution error__" + e);
                                    } catch (InterruptedException e) {
                                        L.Log("laundry asynctask: ", "interrupted error__ " + e);
                                    }
                                    itm.sortItemsByUnitStandard();
                                    tinyItemList.add(itm);
                                    break;
                            }
                        }
                        L.Log(tinyItemList.size() + "3   ");
                        MySharedPreferencesManager.getInstance().setCheckedItemList(new HashSet<>(tagList));


                        // 안되면 말구
                        myGalleryAdapter.notifyDataSetChanged();
//        myGalleryAdapter = new MyGalleryAdapter(this, tinyItemList);
                        mGallery.setAdapter(myGalleryAdapter);  // 여기다주는게맞나? (세팅 돌아올시 화면 안바뀌는문제)
                        isListRefreshed = true;
                        currView = null;
                        prevView = null;
                        Log.e("NOTIFYDATASETCHANGED", "came up, " + myGalleryAdapter.tinyItemList.size());
                    }
                });
            }
        }).start();


    }

    private final int  MY_PERMISSION_LOCATION = 100;

    private void checkMyPermissionLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            //Permission Check
            PermissionSettingUtils.requestPermission(this, 10, Manifest.permission.ACCESS_FINE_LOCATION);
        } else {
            //권한을 받았을때 위치찾기 세팅을 시작한다
            initGoogleMapLocation();
        }
    }
    private void initGoogleMapLocation() {
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        mSettingsClient = LocationServices.getSettingsClient(this);
        /*
         * 위치정보 결과를 리턴하는 Callback
         */
        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult result) {
                super.onLocationResult(result);
                //mCurrentLocation = locationResult.getLastLocation();
                mCurrentLocation = result.getLocations().get(0);

                L.T(GalleryTestActivity.getAppContext(), "위경도: " + mCurrentLocation.getLatitude() + ", " + mCurrentLocation.getLongitude());
                MySharedPreferencesManager.getInstance().setLatitude(mCurrentLocation.getLatitude() + "");
                MySharedPreferencesManager.getInstance().setLongitude(mCurrentLocation.getLongitude() + "");
                /**
                 * 지속적으로 위치정보를 받으려면
                 * mLocationRequest.setNumUpdates(1) 주석처리하고
                 * 밑에 코드를 주석을 푼다
                 */
                //mFusedLocationClient.removeLocationUpdates(mLocationCallback);
            }
            //Location관련정보를 모두 사용할 수 있음을 의미
            @Override
            public void onLocationAvailability(LocationAvailability availability) {
                //boolean isLocation = availability.isLocationAvailable();
            }
        };
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(10000);
        mLocationRequest.setFastestInterval(5000);
        //여기선 한번만 위치정보를 가져오기 위함
        mLocationRequest.setNumUpdates(1);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(mLocationRequest);
        mLocationSettingsRequest = builder.build();


        Task<LocationSettingsResponse> locationResponse = mSettingsClient.checkLocationSettings(mLocationSettingsRequest);
        locationResponse.addOnSuccessListener(this, new OnSuccessListener<LocationSettingsResponse>() {
            @Override
            public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                Log.e("onSuccess", "성공적으로 업데이트 됨.");
                //
                if (ActivityCompat.checkSelfPermission(GalleryTestActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
            }
        });
        locationResponse.addOnFailureListener(this, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                int statusCode = ((ApiException) e).getStatusCode();
                switch (statusCode) {
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        Log.e("onFailure", "위치환경체크");
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        String errorMessage = "위치설정체크";
                        Log.e("onFailure", errorMessage);
                }
            }
        });
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        //요청코드가 맞지 않는다면
        if (requestCode != 10) {
            return;
        }
        if (PermissionSettingUtils.isPermissionGranted(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, grantResults,
                Manifest.permission.ACCESS_FINE_LOCATION)) {
            //허락을 받았다면 위치값을 알아오는 코드를 진행
            initGoogleMapLocation();
        }else{
            Toast.makeText(this, "위치정보를 허락하셔야 앱을 사용하실수 있습니다", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 사용자가 위치설정을 하고 Back Key를 눌렀을때 실행
     */
    @Override
    public void onRestart() {
        super.onRestart();
        startActivity(new Intent(this, GalleryTestActivity.class));
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

    private void renewAddress(){

//        getLocation();
//        L.Log();



        new AsyncTaskAddressInfo(this, tagLocation, tagList, tinyItemList, this).execute(1);
        // 주소 새로고침하여 법정동, 주소 얻어서 sharedpref에 저장하기
        // onPostExecute에서 법정동으로 airKorea TM좌표 얻어오기
        // TM좌표로 인근 측정소 받아와서 측정소면 sharedPref에 저장하기
        // 선택한 아이템에따라 api돌리기

        // ---------------------------------------------------------------
        //                      기 존
        // lat, lon 및 측정소명 받아오기
        // 선택한 아이템에 따라 api 돌리기

        String myLoc = MySharedPreferencesManager.getInstance().getMyLocation1();
        tagLocation.setText(myLoc);
        L.Log("(1)address get 성공: ", myLoc);
    }

    private AdapterView.OnItemSelectedListener  gallerySelectedListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long l) {
            currView = view;
            prevTime = 0;
            final long temp = System.currentTimeMillis();
            // find the value of grade from each View in the arrayList and
            // determine which bgr matches each view
//            prevTime = 0;
            // AdapterView객체에서 data를 가져오는 방법
            final TinyItemVO tinyItemVO =
                    (TinyItemVO) parent.getItemAtPosition(position);

            if(isListRefreshed){
                imgBgr.setImageResource(tinyItemVO.getBgrImgId());
                msgComp.setText(tinyItemVO.msgComp);
                msgAdv.setText(tinyItemVO.msgAdv);
                isListRefreshed = false;
            }else{  // 여기갑뭐덜라
                if(currView.getId() != prevView.getId()){
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (System.currentTimeMillis() - temp > 500) {
                                imgBgr.setImageResource(tinyItemVO.getBgrImgId());
                                msgComp.setText(tinyItemVO.msgComp);
                                msgAdv.setText(tinyItemVO.msgAdv);
                            }
                        }
                    }, 100);
                }
            }
            prevView = view;
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {}
    };

    private AdapterView.OnItemClickListener galleryClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long l) {

            TinyItemVO tinyItemVO =
                    (TinyItemVO) parent.getItemAtPosition(position);
            imgBgr.setImageResource(tinyItemVO.getBgrImgId());
            msgComp.setText(tinyItemVO.msgComp);
            msgAdv.setText(tinyItemVO.msgAdv);

            //saving it
        }
    };

    @OnClick({R.id.btn_settings, R.id.btn_settings_layout, R.id.btn_loc})
    public void OnClicks(View view){
        switch(view.getId()){
            case R.id.btn_settings:case R.id.btn_settings_layout:
                Intent intent = new Intent(GalleryTestActivity.this, SettingsActivity.class);
//            startActivityForResult(intent, 100);
                intent.setFlags(FLAG_ACTIVITY_REORDER_TO_FRONT);        // block opening the same activity multiple times
                startActivityForResult(intent, REQUEST_SETTINGS_ACTIVITY);
                break;
            case R.id.btn_loc:
//                getLocation();
//                checkMyPermissionLocation();
//
//                L.Log("[LAT LON]", MySharedPreferencesManager.getInstance().getLatitude() + ", " +
//                        MySharedPreferencesManager.getInstance().getLongitude());
//
//                L.Log("[LAT LON_networkConstants]", NetworkConstants.LOC_LATITUDE + ", " +
//                        NetworkConstants.LOC_LONGITUDE);
//                renewAddress();
                L.T(this, "위치를 재설정합니다.");
                break;
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        ConnectivityManager manager =
                (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        final NetworkInfo info = manager.getActiveNetworkInfo();

        // network 처리
//        if (info == null && !info.isConnectedOrConnecting()) {
//            Intent intent = new Intent(Settings.ACTION_WIRELESS_SETTINGS);
//            startActivity(intent);
//        }


        if(requestCode == REQUEST_SETTINGS_ACTIVITY
                && resultCode == Activity.RESULT_OK){
            ArrayList<String> result = data.getStringArrayListExtra(SettingsActivity.RESULT_FAVOR_LIST);
            Boolean isChosenUnitWho = data.getBooleanExtra(SettingsActivity.RESULT_UNIT_STD, false);
            Boolean isAlarmUp = data.getBooleanExtra(SettingsActivity.RESULT_ALARM_UP, false);
            L.L(MySharedPreferencesManager.getInstance().getIsUnitWHO()+ ", 1");
            MySharedPreferencesManager.getInstance().setIsUnitWHO(isChosenUnitWho);
            MySharedPreferencesManager.getInstance().setIsAlarmUp(isAlarmUp);
            L.L(MySharedPreferencesManager.getInstance().getIsUnitWHO() + ", 2");

            L.Log("////CHECKEDLIST222222/////", result.toString());

            renewItem(result);

//            Toast.makeText(getApplicationContext()
//                    ,"[SYSTEM] " + "선택된 항목: " + makeTextOutOfStringArray(result) + "\n"
//                    + "표시기준: " + (isChosenUnitWho ? "WHO" : "대한민국") + "\n"
//                    + "알람서비스 설정여부: " + (isAlarmUp ? "설정함" : "설정안함")
//                    ,Toast.LENGTH_LONG).show();

        }
    } // onactivityResult ends

    public void renewItem(ArrayList<String> result){

        tinyItemList.clear();
//      tinyItemList = new ArrayList<>();    // DONT DO THIS! ADAPTER LOSES REFERENCE!!!!!!
        tagList.clear();
        L.Log("////RESULT SIZE/////", result.size() + "");
        for(int i = 0 ; i < result.size(); i++) {
            tagList.add(result.get(i));
        }
        L.Log("[아이템 재정렬] tagList ->", tagList.toString());
        L.Log("////CHECKEDLIST33333/////", tagList.toString());
        gainParsedItemsInfo();

        // 정렬@!@!!!!!!!!!!!!!!!!dd
////            L.Log("RESULT: ", result.get(i));
//            for(int j = 0; j < iList.length; j++){
//                if(result.get(i).equals(iList[j])){
//                    L.Log("AAAAAAAAA ", result.get(i) + " AAA " + iList[j]);
//                    tinyItemList.add(new TinyItemVO(result.get(i), wList[i]));
//                }
//            }


//        myGalleryAdapter.notifyDataSetChanged();
////        myGalleryAdapter = new MyGalleryAdapter(this, tinyItemList);
//        mGallery.setAdapter(myGalleryAdapter);  // 여기다주는게맞나? (세팅 돌아올시 화면 안바뀌는문제)
//        isListRefreshed = true;
//        currView = null;
//        prevView = null;
//        Log.e("NOTIFYDATASETCHANGED", "came up, " + myGalleryAdapter.tinyItemList.size());
    }

    // viewholder comes here
    public class MyViewHolder{
        public TextView tvTinyItemName;
        public TextView tvTinyItemGrade;
        public ImageView ivTinyItemGrade;
        public String msgComp;
        public String msgAdv;
        public int imgBgrId;
    }
    public class MyGalleryAdapter extends BaseAdapter {
        private Context context;
        private ArrayList<String> stringList;
        private int galleryBackGroundAttrs;
        private ArrayList<TinyItemVO> tinyItemList;
        private ArrayList<View> vList;
        private int[] imgIds = {
                R.drawable.theme_bgr_good,
                R.drawable.theme_bgr_bad,
                R.drawable.theme_bgr_fatal,
                R.drawable.theme_bgr_mediocre,
                R.drawable.theme_bgr_very_bad,
        };
        private String[] txtStr = {
                "미세먼지", "초미세먼지",
                "오존", "자외선",
                "꽃가루", "세차"
        };

        public MyGalleryAdapter(Context context, ArrayList<TinyItemVO> tinyItemList){
//            super(context, 0); //////////////////////////////////////////////////////////////////////////////////////////////
            this.context = context;
            this.tinyItemList = tinyItemList;
            if(tinyItemList == null){
                tinyItemList = new ArrayList<>();
            }

            TypedArray typeArray = obtainStyledAttributes(R.styleable.GalleryTheme);
            galleryBackGroundAttrs =
                    typeArray.getResourceId(R.styleable.GalleryTheme_android_galleryItemBackground, 0);

            // 여기서해야되나?

        }

        public void addInsert(View view){
//            tinyItemList.add(view);
        }
        public void setListItems(ArrayList<View> list){
            this.tinyItemList = tinyItemList;
        }

        // list의 한 행을 실제 렌더링하는 콜백 메소드

        //inner class
        // viewholder
        public int getCount(){return tinyItemList.size();}
        public Object getItem(int position){return tinyItemList.get(position);}
        public long getItemId(int position){return position;}
        public View getView(int position, View convertView, ViewGroup parent){
            RelativeLayout tinyItemRootLayout = (RelativeLayout) convertView;
            final TinyItemVO tinyItemVO = tinyItemList.get(position);
//            final View view = vList.get(position);
            MyViewHolder holder;

            if(tinyItemRootLayout == null) {
                holder = new MyViewHolder();

                // 현재 item 매핑시킬 레이아웃객체를 inflate한다
                tinyItemRootLayout = (RelativeLayout) LayoutInflater.from(context).inflate(R.layout.item_singular, null);
                holder.tvTinyItemName = tinyItemRootLayout.findViewById(R.id.tv_tag);
                holder.tvTinyItemName.setText(tinyItemVO.getItemName());
                holder.ivTinyItemGrade = tinyItemRootLayout.findViewById(R.id.iv_tiny_grade);
                holder.ivTinyItemGrade.setImageResource(tinyItemVO.getImgId());
                holder.tvTinyItemGrade = tinyItemRootLayout.findViewById(R.id.tv_tiny_status);
                holder.tvTinyItemGrade.setText(tinyItemVO.getGrade());
                holder.imgBgrId = tinyItemVO.getBgrImgId(); //////////////////////
                holder.msgComp = tinyItemVO.msgComp;
                holder.msgAdv = tinyItemVO.msgAdv;
                tinyItemRootLayout.setTag(holder);
            }else{
                holder = (MyViewHolder) tinyItemRootLayout.getTag();
                holder.tvTinyItemName.setText(tinyItemVO.getItemName());
                holder.ivTinyItemGrade.setImageResource(tinyItemVO.getImgId());
                holder.tvTinyItemGrade.setText(tinyItemVO.getGrade());
                holder.imgBgrId = tinyItemVO.getBgrImgId();
                holder.msgComp = tinyItemVO.msgComp;
                holder.msgAdv = tinyItemVO.msgAdv;
            }

            return tinyItemRootLayout;
        }
    }

    private long backKeyPressTime;
    private static final long FINISH_TIME = 1000;

    @Override
    public void onBackPressed() {
        long currentPressedTime = System.currentTimeMillis();
        long delayTime = currentPressedTime - backKeyPressTime;
        if(delayTime >= 0 && delayTime <= FINISH_TIME){
            super.onBackPressed();
        }else {
            backKeyPressTime = currentPressedTime;
            Toast.makeText(this, "한번 더 눌러 종료합니다",
                    Toast.LENGTH_SHORT).show();
        }
    }


    public void refreshMyGalleryAdapter(ArrayList<TinyItemVO> itmVOList){
        myGalleryAdapter = new MyGalleryAdapter(this, itmVOList);
        myGalleryAdapter.notifyDataSetChanged();
        mGallery.setAdapter(myGalleryAdapter);  // 여기다주는게맞나? (세팅 돌아올시 화면 안바뀌는문제)
        isListRefreshed = true;
        currView = null;
        prevView = null;
    }

}
