package today.tacademy.com.finalprojecttodayis.common.managers;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by Tacademy on 2017-10-26.
 */

public class MySharedPreferencesManager {
    // singleton class
    private static MySharedPreferencesManager instance;
    public static MySharedPreferencesManager getInstance(){
        if(instance == null){// arrayLists setup
            instance = new MySharedPreferencesManager();
        }
        return instance;
    }

    SharedPreferences mPrefs;
    SharedPreferences.Editor mEditor;

    public static final String KEY_FLAG_LIST = "flaglist";
    public static final String KEY_ITEM_NAMES = "item_names";
    public static final String KEY_ITEM_VALUES = "item_values";
    public static final String KEY_IS_WHO= "is_who";
    public static final String KEY_IS_ALARM_UP= "is_alarm_up";
    public static final String KEY_DATETIME = "datetime";
    public static final String KEY_MY_LOCATION1 = "mylocation1";
    public static final String KEY_LEGAL_LOC1 = "legal_dong1";
    public static final String KEY_SI_DO1 = "sido_name";
    public static final String KEY_GUN_GU1 = "gungu_name";
    public static final String KEY_ADMIN_DONG1 = "admin_dong1";
//    public static final String KEY_MY_LOCATION2 = "mylocation2";
//    public static final String KEY_MY_LOCATION3 = "mylocation3";
    public static final String KEY_LBS_ACCEPTANCE = "is_lbs_allowed";
    public static final String KEY_ITEMS_REFRESH_TIME = "itms_refresh_time";
    public static final String KEY_ITEMS_VALUES = "itms_values";
    public static final String KEY_OBSV_NAME = "obsv_name";
    public static final String KEY_LATITUDE = "latitude";
    public static final String KEY_LONGITUDE = "longitude";


    // 시간
    public static final String KEY_LATEST_APP_RUN_DATETIME = "app_run_datetime";
    public static final String KEY_LATEST_RELEASE_DATETIME = "latest_refresh_datetime";

    // 업뎃시간 및 value 저장용
    public void setItemsRefreshTime(String str){mEditor.putString(KEY_ITEMS_REFRESH_TIME, str); mEditor.commit();}
    public String getItemsRefreshTime(){return mPrefs.getString(KEY_ITEMS_REFRESH_TIME, "");}
    public void setItemsValues(String str){mEditor.putString(KEY_ITEMS_VALUES, str); mEditor.commit();}
    public String getItemsValues(){return mPrefs.getString(KEY_ITEMS_VALUES, "");}
    public void setObsvName(String obsvName){mEditor.putString(KEY_OBSV_NAME, obsvName); mEditor.commit();}
    public String getObsvName(){return mPrefs.getString(KEY_OBSV_NAME, "");}

    // lbs권한체크용
    public void setLBSAcceptance(String s){mEditor.putString(KEY_LBS_ACCEPTANCE, s);mEditor.commit();}
    public String getLBSAcceptance(){return mPrefs.getString(KEY_LBS_ACCEPTANCE, "");}

    // 위치저장용
    public String getLatitude(){return mPrefs.getString(KEY_LATITUDE, "");}
    public void setLatitude(String lat){mEditor.putString(KEY_LATITUDE, lat); mEditor.commit();}
    public String getLongitude(){return mPrefs.getString(KEY_LONGITUDE, "");}
    public void setLongitude(String lon){mEditor.putString(KEY_LONGITUDE, lon); mEditor.commit();}


    public void setLatestAppRunDatetime(String str){mEditor.putString(KEY_LATEST_APP_RUN_DATETIME, str); mEditor.commit();}
    public String getLatestAppRunDatetime(){return mPrefs.getString(KEY_LATEST_APP_RUN_DATETIME, "");}
    public void setLatestReleaseDatetime(String str){mEditor.putString(KEY_LATEST_RELEASE_DATETIME, str); mEditor.commit();}
    public String getLatestReleaseDatetime(){return mPrefs.getString(KEY_LATEST_RELEASE_DATETIME, "");}


    public void setCheckedItemList(Set<String> checkedList){
        mEditor.putStringSet(KEY_FLAG_LIST, checkedList);
        mEditor.commit();
    }
    public Set<String> loadCheckedItemList(){
        return mPrefs.getStringSet(KEY_FLAG_LIST, new HashSet<String>());
    }
    public void setItemNamesKey(String str){mEditor.putString(KEY_ITEM_NAMES, str); mEditor.commit();}
    public String getItemNamesKey(){return mPrefs.getString(KEY_ITEM_NAMES, "");}
    public void setItemValuesKey(String str){mEditor.putString(KEY_ITEM_VALUES, str); mEditor.commit();}
    public String getItemValuesKey(){return mPrefs.getString(KEY_ITEM_VALUES, "");}

    public void setMyLocation1(String str){mEditor.putString(KEY_MY_LOCATION1, str); mEditor.commit();}
    public String getMyLocation1(){return mPrefs.getString(KEY_MY_LOCATION1, "");}
    // 시도
    public void setAdminArea1(String adminArea){mEditor.putString(KEY_SI_DO1, adminArea); mEditor.commit();}
    public String getAdminArea1(){return mPrefs.getString(KEY_SI_DO1, "");}
    // 군구
    public void setLocality1(String gungu){mEditor.putString(KEY_GUN_GU1, gungu); mEditor.commit();}
    public String getLocality1(){return mPrefs.getString(KEY_GUN_GU1, "");}
    // 행정동
    public void setAdminDong1(String legalLoc1){mEditor.putString(KEY_ADMIN_DONG1, legalLoc1); mEditor.commit();}
    public String getAdminDong1(){return mPrefs.getString(KEY_ADMIN_DONG1, "");}
    // 법정동
    public void setLegalLocation1(String legalLoc1){mEditor.putString(KEY_LEGAL_LOC1, legalLoc1); mEditor.commit();}
    public String getLegalLocation1(){return mPrefs.getString(KEY_LEGAL_LOC1, "");}

    public String loadCheckedList(){return mPrefs.getString(KEY_LEGAL_LOC1, "");}
    public void setIsUnitWHO(boolean bool){mEditor.putBoolean(KEY_IS_WHO, bool); mEditor.commit();}
    public boolean getIsUnitWHO(){return mPrefs.getBoolean(KEY_IS_WHO, false);} // 교정이 필요하다!
    public void setIsAlarmUp(boolean bool){mEditor.putBoolean(KEY_IS_ALARM_UP, bool); mEditor.commit();}
    public boolean getIsAlarmUp(){return mPrefs.getBoolean(KEY_IS_ALARM_UP, false);} // 교정이 필요하다!
    public void setDateTime(String str){mEditor.putString(KEY_DATETIME, str); mEditor.commit();}
    public String getDateTime(){return mPrefs.getString(KEY_DATETIME, "");}

    private MySharedPreferencesManager(){
        mPrefs = PreferenceManager.getDefaultSharedPreferences(
                today.tacademy.com.finalprojecttodayis.MyApplication.getContext());
        mEditor = mPrefs.edit();
    }

    public boolean isBackupSync() {
        return mPrefs.getBoolean("perf_sync", false);
    }
}
