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
    //?????? ??????????????? ??????/??????????????? ??????????????? ???????????????
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

    final public String[] wList = {"??????", "??????", "??????", "??????", "????????????", "??????", "??????", "??????", "??????", "????????????"};
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



    // Context ????????? temp ?????????
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



        /// ????????????
        long now = System.currentTimeMillis();
        String latestTime = MySharedPreferencesManager.getInstance().getLatestAppRunDatetime();
        if(latestTime.isEmpty() || "".equals(latestTime)){
            // first timer
            // ????????????????????????
            MySharedPreferencesManager.getInstance().setLatestAppRunDatetime(String.valueOf(now));
        }else{  // ?????? ???????????? ?????????????????? ??????????????? ????????????

            MySharedPreferencesManager.getInstance().setLatestAppRunDatetime(now + "");
        }



//        if(MySharedPreferencesManager.getInstance().getLatitude().isEmpty() ||
//                "".equals(MySharedPreferencesManager.getInstance().getLatitude())){
//            if(!locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)){
//                //?????????????????????
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

        // [NOT SOLVED] ???????????? ?????? ?????????????????? ???????????? ???????????? ????????? ?????? ????????????????????? ????????????
        // ????????? ????????? Permission ?????? ????????????!
        if(MySharedPreferencesManager.getInstance().getMyLocation1().isEmpty()){
            // ???????????????

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

                            L.Log("??????????????????????????? ????????????!", tagList.size() + "");
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

        // TM???????????? ??? ????????? ??????
//        LocationVO myLocation = renewTMCoordObsv();

        // ?????? ?????????
        tagDateTime.setText(ItemsCommonModule.getCurrentDateTime());
        s = MySharedPreferencesManager.getInstance().getMyLocation1();
        tagLocation.setText(s); // ??????

        LocationVO locVO;



        L.Log("SHARED_PREF_MAIN!!!___", MySharedPreferencesManager.getInstance().loadCheckedItemList().toString());

        mGallery = (Gallery)findViewById(R.id.my_gallery);
        myGalleryAdapter = new MyGalleryAdapter(this, tinyItemList);
        mGallery.setAdapter(myGalleryAdapter);
        mGallery.setOnItemSelectedListener(gallerySelectedListener);
        mGallery.setOnItemClickListener(galleryClickListener);
    }   // onCreate() ends

    public void gainParsedItemsInfo(){
        // ???????????? 5?????? ????????? ????????? ????????? ????????????(asynctask)

        new Thread(new Runnable() {
            @Override
            public void run() {
                // runOnUiThread??? ???????????? ??? ?????? UI????????? ??????.
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        TinyItemVO itm;
                        if(tagList.contains("pm10") || tagList.contains("pm25")
                                || tagList.contains("o3") || tagList.contains("o3")
                                || tagList.contains("no2")){
                            try{
                                itmListAirKorea = new AsyncTaskItemInfo(GalleryTestActivity.getAppContext()).execute().get();
                                L.Log("taglist??? ???????????? O", itmListAirKorea.size() + "");
                            }catch(ExecutionException e){
                                L.Log("items asynctask: ", "execution error__" + e);
                            }catch(InterruptedException e){
                                L.Log("items asynctask: ", "interrupted error__ " + e);
                            }
                        }

                        // sk??? rest client
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
                                // ?????????????????? arrayList??? ????????????
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


                        // ????????? ??????
                        myGalleryAdapter.notifyDataSetChanged();
//        myGalleryAdapter = new MyGalleryAdapter(this, tinyItemList);
                        mGallery.setAdapter(myGalleryAdapter);  // ????????????????????????? (?????? ???????????? ?????? ??????????????????)
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
            //????????? ???????????? ???????????? ????????? ????????????
            initGoogleMapLocation();
        }
    }
    private void initGoogleMapLocation() {
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        mSettingsClient = LocationServices.getSettingsClient(this);
        /*
         * ???????????? ????????? ???????????? Callback
         */
        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult result) {
                super.onLocationResult(result);
                //mCurrentLocation = locationResult.getLastLocation();
                mCurrentLocation = result.getLocations().get(0);

                L.T(GalleryTestActivity.getAppContext(), "?????????: " + mCurrentLocation.getLatitude() + ", " + mCurrentLocation.getLongitude());
                MySharedPreferencesManager.getInstance().setLatitude(mCurrentLocation.getLatitude() + "");
                MySharedPreferencesManager.getInstance().setLongitude(mCurrentLocation.getLongitude() + "");
                /**
                 * ??????????????? ??????????????? ????????????
                 * mLocationRequest.setNumUpdates(1) ??????????????????
                 * ?????? ????????? ????????? ??????
                 */
                //mFusedLocationClient.removeLocationUpdates(mLocationCallback);
            }
            //Location??????????????? ?????? ????????? ??? ????????? ??????
            @Override
            public void onLocationAvailability(LocationAvailability availability) {
                //boolean isLocation = availability.isLocationAvailable();
            }
        };
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(10000);
        mLocationRequest.setFastestInterval(5000);
        //????????? ????????? ??????????????? ???????????? ??????
        mLocationRequest.setNumUpdates(1);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(mLocationRequest);
        mLocationSettingsRequest = builder.build();


        Task<LocationSettingsResponse> locationResponse = mSettingsClient.checkLocationSettings(mLocationSettingsRequest);
        locationResponse.addOnSuccessListener(this, new OnSuccessListener<LocationSettingsResponse>() {
            @Override
            public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                Log.e("onSuccess", "??????????????? ???????????? ???.");
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
                        Log.e("onFailure", "??????????????????");
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        String errorMessage = "??????????????????";
                        Log.e("onFailure", errorMessage);
                }
            }
        });
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        //??????????????? ?????? ????????????
        if (requestCode != 10) {
            return;
        }
        if (PermissionSettingUtils.isPermissionGranted(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, grantResults,
                Manifest.permission.ACCESS_FINE_LOCATION)) {
            //????????? ???????????? ???????????? ???????????? ????????? ??????
            initGoogleMapLocation();
        }else{
            Toast.makeText(this, "??????????????? ??????????????? ?????? ??????????????? ????????????", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * ???????????? ??????????????? ?????? Back Key??? ???????????? ??????
     */
    @Override
    public void onRestart() {
        super.onRestart();
        startActivity(new Intent(this, GalleryTestActivity.class));
        finish();
    }
    /**
     * ???????????? ??????
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
        // ?????? ?????????????????? ?????????, ?????? ????????? sharedpref??? ????????????
        // onPostExecute?????? ??????????????? airKorea TM?????? ????????????
        // TM????????? ?????? ????????? ???????????? ???????????? sharedPref??? ????????????
        // ????????? ?????????????????? api?????????

        // ---------------------------------------------------------------
        //                      ??? ???
        // lat, lon ??? ???????????? ????????????
        // ????????? ???????????? ?????? api ?????????

        String myLoc = MySharedPreferencesManager.getInstance().getMyLocation1();
        tagLocation.setText(myLoc);
        L.Log("(1)address get ??????: ", myLoc);
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
            // AdapterView???????????? data??? ???????????? ??????
            final TinyItemVO tinyItemVO =
                    (TinyItemVO) parent.getItemAtPosition(position);

            if(isListRefreshed){
                imgBgr.setImageResource(tinyItemVO.getBgrImgId());
                msgComp.setText(tinyItemVO.msgComp);
                msgAdv.setText(tinyItemVO.msgAdv);
                isListRefreshed = false;
            }else{  // ??????????????????
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
                L.T(this, "????????? ??????????????????.");
                break;
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        ConnectivityManager manager =
                (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        final NetworkInfo info = manager.getActiveNetworkInfo();

        // network ??????
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
//                    ,"[SYSTEM] " + "????????? ??????: " + makeTextOutOfStringArray(result) + "\n"
//                    + "????????????: " + (isChosenUnitWho ? "WHO" : "????????????") + "\n"
//                    + "??????????????? ????????????: " + (isAlarmUp ? "?????????" : "????????????")
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
        L.Log("[????????? ?????????] tagList ->", tagList.toString());
        L.Log("////CHECKEDLIST33333/////", tagList.toString());
        gainParsedItemsInfo();

        // ??????@!@!!!!!!!!!!!!!!!!dd
////            L.Log("RESULT: ", result.get(i));
//            for(int j = 0; j < iList.length; j++){
//                if(result.get(i).equals(iList[j])){
//                    L.Log("AAAAAAAAA ", result.get(i) + " AAA " + iList[j]);
//                    tinyItemList.add(new TinyItemVO(result.get(i), wList[i]));
//                }
//            }


//        myGalleryAdapter.notifyDataSetChanged();
////        myGalleryAdapter = new MyGalleryAdapter(this, tinyItemList);
//        mGallery.setAdapter(myGalleryAdapter);  // ????????????????????????? (?????? ???????????? ?????? ??????????????????)
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
                "????????????", "???????????????",
                "??????", "?????????",
                "?????????", "??????"
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

            // ??????????????????????

        }

        public void addInsert(View view){
//            tinyItemList.add(view);
        }
        public void setListItems(ArrayList<View> list){
            this.tinyItemList = tinyItemList;
        }

        // list??? ??? ?????? ?????? ??????????????? ?????? ?????????

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

                // ?????? item ???????????? ????????????????????? inflate??????
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
            Toast.makeText(this, "?????? ??? ?????? ???????????????",
                    Toast.LENGTH_SHORT).show();
        }
    }


    public void refreshMyGalleryAdapter(ArrayList<TinyItemVO> itmVOList){
        myGalleryAdapter = new MyGalleryAdapter(this, itmVOList);
        myGalleryAdapter.notifyDataSetChanged();
        mGallery.setAdapter(myGalleryAdapter);  // ????????????????????????? (?????? ???????????? ?????? ??????????????????)
        isListRefreshed = true;
        currView = null;
        prevView = null;
    }

}
