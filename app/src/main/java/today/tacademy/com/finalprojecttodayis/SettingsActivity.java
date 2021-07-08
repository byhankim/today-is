package today.tacademy.com.finalprojecttodayis;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import today.tacademy.com.finalprojecttodayis.common.CheckBoxFindCommon;
import today.tacademy.com.finalprojecttodayis.common.L;
import today.tacademy.com.finalprojecttodayis.common.managers.MySharedPreferencesManager;

/**
 * Created by Tacademy on 2017-10-20.
 */

public class SettingsActivity extends AppCompatActivity{

    //sharedPreferences
    SharedPreferences mPrefs;
    SharedPreferences.Editor mEditor;
    public static final String PREF_NAME = "prefs";
    public static final String KEY_FLAG_LIST = "flaglist";
    public static final String KEY_ITEM_NAMES = "item_names";
    public static final String KEY_ITEM_VALUES = "item_values";

    HashMap<CheckBox, String> checkBoxMap;
    HashSet<String> checkedList;

    public static final String RESULT_FAVOR_LIST = "favorList";
    public static final String RESULT_UNIT_STD = "chosenUnit";
    public static final String RESULT_ALARM_UP = "isAlarmUp";

    String[] items = {"미세먼지", "초미세먼지", "아황산가스", "오존", "이산화질소"
                    , "체감온도", "자외선", "불쾌지수", "세차지수", "빨래지수"};
    String[] times = {"없음", "오전 6:00", "오전 6:30", "오전 7:00", "오전 7:30"
                                , "오후 6시", "오후 7시", "오후 8시"};
    String[] grades = {"좋음", "보통", "나쁨", "매우나쁨", "심각"};
    ArrayList<String> itemList;
    // Butterknife
    /*@BindView(R.id.btn_backwards)
    ImageButton btnBackArw;*/
    @BindView(R.id.settings_activity_toolbar)
    Toolbar toolbar;
    @BindView(R.id.dp_switch)
    SwitchCompat dpSwitch;
    @BindView(R.id.alr_switch)
    SwitchCompat alrSwitch;
    @BindView(R.id.spn_alr_time)
    Spinner timeSpn;
    @BindView(R.id.spn_alr_item)
    Spinner itemSpn;
    @BindView(R.id.spn_alr_value)
    Spinner valueSpn;
    @BindView(R.id.spn_layout)
    RelativeLayout spnLayout;
    @BindView(R.id.nsv)
    NestedScrollView mNsv;
    @BindView(R.id.txtalarm)
    TextView tvAlarm;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity_layout);
        ButterKnife.bind(this);

        L.L(MySharedPreferencesManager.getInstance().getIsUnitWHO() + "");

        spnLayout.setVisibility(RelativeLayout.GONE);
        mPrefs = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        mEditor = mPrefs.edit();

        itemList = new ArrayList<>();
        checkedList = new HashSet<>();
        setSupportActionBar(toolbar);

        alrSwitch.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if(motionEvent.getAction() == motionEvent.ACTION_UP){
                       detectIfAlarmChecked();
                }
                return false;
            }
        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        final ArrayList<Integer> fullCbgKeys = CheckBoxFindCommon.getAllItemKeys();
        ArrayList<CheckBox> cbg = new ArrayList<>(fullCbgKeys.size());
        for(int i = 0; i < fullCbgKeys.size(); i++){
            cbg.add(i, (CheckBox)findViewById(fullCbgKeys.get(i)));
            cbg.get(i).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                    switch(compoundButton.getId()){
                        case R.id.itm1:
                            if(isChecked) checkedList.add("pm10");
                            else checkedList.remove("pm10");
                            break;
                        case R.id.itm2:
                            if(isChecked) checkedList.add("pm25");
                            else checkedList.remove("pm25");
                            break;
                        case R.id.itm3:
                            if(isChecked) checkedList.add("so2");
                            else checkedList.remove("so2");
                            break;
                        case R.id.itm4:
                            if(isChecked) checkedList.add("o3");
                            else checkedList.remove("o3");
                            break;
                        case R.id.itm5:
                            if(isChecked) checkedList.add("no2");
                            else checkedList.remove("no2");
                            break;
                        case R.id.itm6:
                            if(isChecked) checkedList.add("temp");
                            else checkedList.remove("temp");
                            break;
                        case R.id.itm7:
                            if(isChecked) checkedList.add("uv");
                            else checkedList.remove("uv");
                            break;
                        case R.id.itm8:
                            if(isChecked) checkedList.add("humidity");
                            else checkedList.remove("humidity");
                            break;
                        case R.id.itm9:
                            if(isChecked) checkedList.add("carwash");
                            else checkedList.remove("carwash");
                            break;
                        case R.id.itm10:
                            if(isChecked) checkedList.add("laundry");
                            else checkedList.remove("laundry");
                            break;
                    }
                }
            });
        }
        checkCBFromSharedPref();

//        CheckBoxGroup<String> checkBoxGroup = new CheckBoxGroup<>(checkBoxMap,
//                new CheckBoxGroup.CheckedChangeListener<String>(){
//                    @Override
//                    public void onCheckedChange(ArrayList<String> values) {
////                        Toast.makeText(getApplicationContext()
////                                ,values.toString()
////                                ,Toast.LENGTH_SHORT).show();
//                        checkedList = new HashSet<>(values);
//                    }
//                });


        timeSpn.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

//        alrSwitch.setOnScrollChangeListener();

        ArrayAdapter aa1 = new ArrayAdapter(SettingsActivity.this, android.R.layout.simple_spinner_item, times);
        aa1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        timeSpn.setAdapter(aa1);

        ArrayAdapter aa2 = new ArrayAdapter(SettingsActivity.this, android.R.layout.simple_spinner_item, times);
        aa2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        itemSpn.setAdapter(aa2);

        ArrayAdapter aa3 = new ArrayAdapter(SettingsActivity.this, android.R.layout.simple_spinner_item, grades);
        aa3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        valueSpn.setAdapter(aa3);

    }

    void checkCBFromSharedPref(){
        // SharedPreferences 존재할때 처리부분 (setChecked)
        if(!MySharedPreferencesManager.getInstance().loadCheckedItemList().isEmpty()){
            Set<String> myPrefSet =  MySharedPreferencesManager.getInstance().loadCheckedItemList();
            L.Log("settings_프리퍼런스: ", myPrefSet.toString());

            ArrayList<String> keyNames = new ArrayList(myPrefSet);
            L.Log("dsfasd", keyNames.toString());
            int size = keyNames.size();
            ArrayList<Integer> checkBoxRes = new ArrayList<>(size);

            for(int i=0 ;i < size;i++){
                checkBoxRes.add(CheckBoxFindCommon.getItemKey(keyNames.get(i)));
            }

            CheckBox cb;
            Iterator<String> it = myPrefSet.iterator();
            while(it.hasNext()){
                cb = (CheckBox) findViewById(CheckBoxFindCommon.getItemKey(it.next()));
                cb.setChecked(true);
            }
        }

        // unit display standard 및 alarm up 여부 체크
        dpSwitch.setChecked(MySharedPreferencesManager.getInstance().getIsUnitWHO());
//        alrSwitch.setChecked(MySharedPreferencesManager.getInstance().getIsAlarmUp());
        L.L(MySharedPreferencesManager.getInstance().getIsUnitWHO() + ", settings1");
    }

    //menuitem
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        L.Log("HomePRESSED", "!");
        // handle arrow click here
        if(item.getItemId() == android.R.id.home){
//            checkSelectedItems();
            setPrefSetting();
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    // on or setResult()


    @OnClick({R.id.txtalarm})
    public void OnClicks(View view){
        switch(view.getId()){
//            case R.id.btn_backwards:
//                checkSelectedItems();
//                // dynamically form an array of items here //////////////
//                finish();
//                break;
//            case R.id.spn_alr_time:
//                break;
//            case R.id.spn_alr_item:
//                break;
//            case R.id.spn_alr_value:
//                break;
            case R.id.txtalarm:
//                detectIfAlarmChecked();
                Toast.makeText(this, "알람 설정입니다. 추후 구현 예정입니다",
                        Toast.LENGTH_SHORT).show();
                break;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        setPrefSetting();
        finish();
    }

    public void setPrefSetting(){
        Intent data = new Intent();

        if(checkedList.isEmpty()){
            Toast.makeText(getApplicationContext()
                    , "설정한 항목이 없어 미세먼지, 초미세먼지를 강제선택합니다."
                    , Toast.LENGTH_SHORT).show();
            checkedList.add("pm10");
            checkedList.add("pm25");
        }

        // hashset 저장
        MySharedPreferencesManager.getInstance().setCheckedItemList(checkedList);
        ArrayList<String> checkedListToArrayList = new ArrayList<>(checkedList);
        data.putStringArrayListExtra(RESULT_FAVOR_LIST, checkedListToArrayList);
        data.putExtra(RESULT_UNIT_STD, dpSwitch.isChecked());
//        data.putExtra(RESULT_ALARM_UP, alrSwitch.isChecked());

        setResult(RESULT_OK, data);
//        finish();
    }

    public void checkSelectedItems(){
        StringBuilder strBuf = new StringBuilder();
        int itemLength = itemList.size();
        if(itemLength > 0){
            for(int i = 0 ; i < itemLength; i++){
                strBuf.append(itemList.get(i));
                strBuf.append(",");
            }
        }
//        Toast.makeText(getApplicationContext()
//                , "선택한 아이템: " + strBuf.toString()
//                + "\n표시기준: " + (dpSwitch.isChecked() ? "WHO" : "한국") + "\n"
//                + "알람서비스: " + (alrSwitch.isChecked() ? "설정함" : "설정안함") + "\n"
//                , Toast.LENGTH_SHORT).show();

/*        int selectedCount = 0;
        StringBuilder strBuf = new StringBuilder();
        // check된 item의 id값(array in this case)을 리턴한다
        //long ids[] = memberList.getcheckedItemIds();
        // pos의 각 값이 boolean의 어떤 값을 갖고있는지 map구조로 나타내어 리턴해줌
        SparseBooleanArray sparseMap =
                favorList.getCheckedItemPositions();
        int itemLength = sparseMap.size();
        if(itemLength > 0){
            for(int itemPosition = 0; itemPosition < itemLength; itemPosition++){
                if(sparseMap.valueAt(itemPosition)){
                    selectedCount++;
                    strBuf.append((String)
                            favorList.getItemAtPosition(
                                    itemPosition));
                    strBuf.append(",");
                }
            }
            Toast.makeText(getApplicationContext(),
                    "selecteditems: " + strBuf.toString(),
                    Toast.LENGTH_SHORT).show();
        }*/
    }

    public String[] getItemList(ArrayList<String> itemList){
        String[] items = new String[itemList.size()];

        for(int i = 0; i < itemList.size(); i++){
            items[i] = itemList.get(i);
        }

//        items = itemList.toArray(items);
        return items;
    }

    public void detectIfAlarmChecked(){

        ///// BUG ALERT ///// ==============================================
        ///
        ///  ain't functioning when scrolled to on/off! even with the added
        ///  onSelectScrollChangeListener(?????) ---> does seem weird heh...
        ///
        ///// ==============================================================

//        if(alrSwitch.isChecked()){
//            spnLayout.setVisibility(RelativeLayout.VISIBLE);
//            mNsv.post(new Runnable(){
//                @Override
//                public void run() {
//                    mNsv.fullScroll(ScrollView.FOCUS_DOWN);
//                }
//            });
//        }else{
//            spnLayout.setVisibility(RelativeLayout.GONE);
//        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(checkedList.isEmpty()){
            Toast.makeText(getApplicationContext()
                    , "설정한 항목이 없어 미세먼지, 초미세먼지를 강제선택합니다."
                    , Toast.LENGTH_SHORT).show();
            checkedList.add("pm10");
            checkedList.add("pm25");
        }


        // hashset 저장
//        MySharedPreferencesManager.getInstance().setFlagKey(checkedList.toString());

//        mEditor.putStringSet(KEY_FLAG_LIST, checkedList);
        MySharedPreferencesManager.getInstance().setCheckedItemList(checkedList);
        MySharedPreferencesManager.getInstance().setIsUnitWHO(dpSwitch.isChecked());
        MySharedPreferencesManager.getInstance().setIsAlarmUp(alrSwitch.isChecked());
        // 기준표시와 알림서비스 정리

    }

    @Override
    protected void onStop() {
        super.onStop();
        if(checkedList.isEmpty()){
            Toast.makeText(getApplicationContext()
                    , "설정한 항목이 없어 미세먼지, 초미세먼지를 강제선택합니다."
                    , Toast.LENGTH_SHORT).show();
            checkedList.add("pm10");
            checkedList.add("pm25");
        }


        L.Log("CHECKEDLIST", checkedList.toString());
        // hashset 저장
//        MySharedPreferencesManager.getInstance().setFlagKey(checkedList.toString());

//        mEditor.putStringSet(KEY_FLAG_LIST, checkedList);
        MySharedPreferencesManager.getInstance().setCheckedItemList(checkedList);
        L.Log("SHARED_PREF1!!!___", MySharedPreferencesManager.getInstance().loadCheckedItemList().toString());

    }

    @Override
    protected void onResume() {
        super.onResume();
        checkCBFromSharedPref();
        dpSwitch.setChecked(MySharedPreferencesManager.getInstance().getIsUnitWHO());
        alrSwitch.setChecked(MySharedPreferencesManager.getInstance().getIsAlarmUp());
    }
}
