package today.tacademy.com.finalprojecttodayis;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;

import java.util.ArrayList;
import java.util.HashSet;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import today.tacademy.com.finalprojecttodayis.common.CheckBoxFindCommon;
import today.tacademy.com.finalprojecttodayis.common.L;
import today.tacademy.com.finalprojecttodayis.common.constants.NetworkConstants;
import today.tacademy.com.finalprojecttodayis.common.managers.MySharedPreferencesManager;

/**
 * Created by Tacademy on 2017-11-14.
 */


public class FirstSettingsActivity extends AppCompatActivity {


    public static final String CHECKEDLIST_DONE = "listchecked";
    @BindView(R.id.first_settings_toolbar)
    Toolbar toolbar;

    @BindView(R.id.btn_done)
    ImageButton btnDone;

    final String[] itemsTags = {"pm10", "pm25", "so2", "o3", "no2",
            "temp", "uv", "humidity", "carwash", "laundry"};


    public final String CONNECTION_PROVIDER = "connection_provider";
    HashSet<String> checkedList;

    // network n location
    private LocationRequest mLocationRequest;
    private LocationCallback mLocationCallback;
    private Location mCurrentLocation;
    private FusedLocationProviderClient mFusedLocationClient;
    private String provider;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.first_settings_activity);
        ButterKnife.bind(this);


        MySharedPreferencesManager.getInstance().setLatitude(NetworkConstants.LOC_LATITUDE);
        MySharedPreferencesManager.getInstance().setLongitude(NetworkConstants.LOC_LONGITUDE);

        provider = getIntent().getStringExtra("provider");

        checkedList = new HashSet<>();
        setSupportActionBar(toolbar);

//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setDisplayShowTitleEnabled(false);

        final ArrayList<Integer> fullCbgKeys = CheckBoxFindCommon.getAllItemKeys();
        ArrayList<CheckBox> cbg = new ArrayList<>(fullCbgKeys.size());
        for (int i = 0; i < fullCbgKeys.size(); i++) {
            cbg.add(i, (CheckBox) findViewById(fullCbgKeys.get(i)));
            cbg.get(i).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                    switch (compoundButton.getId()) {
                        case R.id.itm1:
                            if (isChecked) checkedList.add("pm10");
                            else checkedList.remove("pm10");
                            break;
                        case R.id.itm2:
                            if (isChecked) checkedList.add("pm25");
                            else checkedList.remove("pm25");
                            break;
                        case R.id.itm3:
                            if (isChecked) checkedList.add("so2");
                            else checkedList.remove("so2");
                            break;
                        case R.id.itm4:
                            if (isChecked) checkedList.add("o3");
                            else checkedList.remove("o3");
                            break;
                        case R.id.itm5:
                            if (isChecked) checkedList.add("no2");
                            else checkedList.remove("no2");
                            break;
                        case R.id.itm6:
                            if (isChecked) checkedList.add("temp");
                            else checkedList.remove("temp");
                            break;
                        case R.id.itm7:
                            if (isChecked) checkedList.add("uv");
                            else checkedList.remove("uv");
                            break;
                        case R.id.itm8:
                            if (isChecked) checkedList.add("humidity");
                            else checkedList.remove("humidity");
                            break;
                        case R.id.itm9:
                            if (isChecked) checkedList.add("carwash");
                            else checkedList.remove("carwash");
                            break;
                        case R.id.itm10:
                            if (isChecked) checkedList.add("laundry");
                            else checkedList.remove("laundry");
                            break;
                    }
                }


            });
        }

    }


    @OnClick({R.id.btn_done})
    void onBackBtn(View view) {
        L.Log("터치ㅣㅣㅣㅣ", "터치터치야");
        setPrefSetting();
        finish();
    }

    public void setPrefSetting() {
        Intent firstSettingsDatum = new Intent(FirstSettingsActivity.this, GalleryTestActivity.class);

        checkedList.add("pm10");
        checkedList.add("pm25");


        MySharedPreferencesManager.getInstance().setCheckedItemList(checkedList);
        ArrayList<String> setList = new ArrayList<>(checkedList);   // 림ㅈ

        firstSettingsDatum.putStringArrayListExtra(CHECKEDLIST_DONE, setList);
        firstSettingsDatum.putExtra(CONNECTION_PROVIDER, provider);

        setResult(RESULT_OK, firstSettingsDatum);
        startActivity(firstSettingsDatum);
    }
}
