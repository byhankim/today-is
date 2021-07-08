package today.tacademy.com.finalprojecttodayis.common.constants;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.HashMap;

import today.tacademy.com.finalprojecttodayis.common.managers.MySharedPreferencesManager;
import today.tacademy.com.finalprojecttodayis.entity.TinyItemVO;

/**
 * Created by Tacademy on 2017-11-11.
 */

public class ItemConstants {

    public static HashMap<String, Double> valueMap;
    public static HashMap<String, Long> releaseTimeMap;

    public static ArrayList<String> itmSelectList;
    public static ArrayList<TinyItemVO> tinyItmList;
//    public static LinkedHashSet<String> itmList;
//    public static LinkedHashMap<String, Integer> releaseHour;

    static{

        itmSelectList = new ArrayList<>();
        tinyItmList = new ArrayList<>();
//        if(MySharedPreferencesManager.getInstance().loadCheckedItemList().isEmpty()){
//            // 처음 실행
//            itmSelectList = new ArrayList<>();
//            tinyItmList = new ArrayList<>();
//        }else{
//            // 기존 실행
//
//        }

        //처음 실행 및 이전 실행 여부 가르기

        String latestRunTime = MySharedPreferencesManager.getInstance().getLatestAppRunDatetime();
        if(latestRunTime.isEmpty() || "".equals(latestRunTime)){
            // 처음 실행일 경우
            valueMap = new HashMap<>();
            releaseTimeMap = new HashMap<>();
        }else{
            String tempVals = MySharedPreferencesManager.getInstance().getItemsValues();
            valueMap = getValueMapFromString(tempVals);
            String tempTime = MySharedPreferencesManager.getInstance().getItemsRefreshTime();
            releaseTimeMap = getReleaseDateMapFromString(tempTime);
        }



    }

    public static String getStringFromValueMap(){
        Gson gson = new Gson();
        return gson.toJson(valueMap);
    }

    public static String getStringFromReleaseTimeMap(){
        Gson gson = new Gson();
        return gson.toJson(releaseTimeMap);
    }

    public static HashMap<String, Double> getValueMapFromString(String prefStr){
        Gson gson = new Gson();
        java.lang.reflect.Type type = new TypeToken<HashMap<String, Double>>(){}.getType();
        return gson.fromJson(prefStr, type);
    }

    public static HashMap<String, Long> getReleaseDateMapFromString(String prefStr){
        Gson gson = new Gson();
        java.lang.reflect.Type type = new TypeToken<HashMap<String, Long>>(){}.getType();
        return gson.fromJson(prefStr, type);
    }

    public static void saveCurrentValueMapInPref(){
        MySharedPreferencesManager.getInstance()
                .setItemsValues(getStringFromValueMap());
    }

    public static void saveCurrentTimeMapInPref(){
        MySharedPreferencesManager.getInstance()
                .setItemsValues(getStringFromReleaseTimeMap());
    }

    public static void getParsedItemsInfo(){
        // 환경공단 5형제 아이템 있을시 한개만 받아온다(asynctask)

//        TinyItemVO itm;
//        if(tagList.contains("pm10") || tagList.contains("pm25")
//                || tagList.contains("o3") || tagList.contains("o3")
//                || tagList.contains("no2")){
//            try{
//                itmListAirKorea = new AsyncTaskItemInfo(this).execute().get();
//                L.Log("taglist에 환경공단 O", itmListAirKorea.size() + "");
//            }catch(ExecutionException e){
//                L.Log("items asynctask: ", "execution error__" + e);
//            }catch(InterruptedException e){
//                L.Log("items asynctask: ", "interrupted error__ " + e);
//            }
//        }
//
//        // sk용 rest client
////        restClient = TodayContext.getTodayRESTClient();
////        ArrayList<Call<? extends Cloneable>> returnedCalls = new ArrayList<>();
//
//        Iterator it = tagList.iterator();
//        while(it.hasNext()){
//            String o = it.next().toString();
//            itm = new TinyItemVO();
//            tempItem = new TinyItemVO();
//            switch(o){
//                // 환경공단꺼면 arrayList값 갖고온다
//                case "pm10":
//                    itm = itmListAirKorea.get(0);
//                    L.Log(11, ("".equals(itm.itemName))
//                            || itm.itemName.isEmpty() ? "itm 비었다!" : "item 안비었다!");
//                    itm.sortItemsByUnitStandard();
//                    tinyItemList.add(itm);
//                    break;
//                case "pm25":
//                    itm = itmListAirKorea.get(1);
//                    itm.sortItemsByUnitStandard();
//                    tinyItemList.add(itm);
//                    break;
//                case "so2":
//                    itm = itmListAirKorea.get(2);
//                    itm.sortItemsByUnitStandard();
//                    tinyItemList.add(itm);
//                    break;
//                case "o3":
//                    itm = itmListAirKorea.get(3);
//                    itm.sortItemsByUnitStandard();
//                    tinyItemList.add(itm);
//                    break;
//                case "no2":
//                    itm = itmListAirKorea.get(4);
//                    itm.sortItemsByUnitStandard();
//                    tinyItemList.add(itm);
//                    break;
//                case "temp":
//                    try {
//                        String tVal = new AsyncTaskEftcTempInfo(this).execute().get();
//                        itm.itemName = "temp";
//                        itm.value = Double.parseDouble(tVal);
//                    } catch (ExecutionException e) {
//                        L.Log("temp asynctask: ", "execution error__" + e);
//                    } catch (InterruptedException e) {
//                        L.Log("temp asynctask: ", "interrupted error__ " + e);
//                    }
//                    itm.sortItemsByUnitStandard();
//                    tinyItemList.add(itm);
//
//
////                    Call<? extends Cloneable> gsonData = restClient.requestEffectiveTemp(NetworkConstants.LOC_LATITUDE,
////                            NetworkConstants.LOC_LONGITUDE, NetworkConstants.SK_API_VERSION);
////
////                    Call<EffectiveTempVO> effectiveTemp = (Call<EffectiveTempVO>) gsonData;
////
////                    effectiveTemp.enqueue(new Callback<EffectiveTempVO>(){
////                        @Override
////                        public void onResponse(Call<EffectiveTempVO> call, Response<EffectiveTempVO> response) {
////                            if(response.isSuccessful()){
////                                tempItem = setEffectiveTemp(response.body());
////                                L.T(GalleryTestActivity.getAppContext(), "체감온도2: " + tempItem.value);
////                                tempItem.sortItemsByUnitStandard();
////                                tinyItemList.add(tempItem);
//////                                tempItem.sortItemsByUnitStandard();
//////                                tinyItemList.add(tempItem);
////
////                            }
////                        }
////
////                        @Override
////                        public void onFailure(Call<EffectiveTempVO> call, Throwable t) {
////                            L.Log("체감온도 알아노는데 실패 ㅋㅋ");
////                        }
////                    });
//
//                    break;
//                case "uv":
//                    try {
//                        String result = new AsyncTaskUVInfo(this).execute().get();
//                        itm.itemName = "uv";
//                        try {
//                            itm.value = Double.parseDouble(result.trim());
//                        }catch(NullPointerException e){
//                            result = new AsyncTaskUVInfo(this).execute().get();
//                            itm.value = Double.parseDouble(result.trim());
//                        }
//
//                    } catch (ExecutionException e) {
//                        Log.e("uv asynctask: ", "execution error__" + e);
//                    } catch (InterruptedException e) {
//                        Log.e("uv asynctask: ", "interrupted error__ " + e);
//                    }
//
//                    itm.sortItemsByUnitStandard();
//                    tinyItemList.add(itm);
//
//                    L.Log("ccccc", itm.value + ", " + itm.grade);
////                    Call<? extends Cloneable> gsonDataUV = restClient.requestUV(NetworkConstants.LOC_LATITUDE,
////                            NetworkConstants.LOC_LONGITUDE, NetworkConstants.SK_API_VERSION);
////
////                    Call<UVRaysVO> uvRays = (Call<UVRaysVO>) gsonDataUV;
////
////                    uvRays.enqueue(new Callback<UVRaysVO>(){
////                        @Override
////                        public void onResponse(Call<UVRaysVO> call, Response<UVRaysVO> response) {
////                            if(response.isSuccessful()){
////                                tempItem = setUVRays(response.body());
////                                tinyItemList.add(tempItem);
////                                L.Log(tinyItemList.size() + "");
////                            }
////                        }
////
////                        @Override
////                        public void onFailure(Call<UVRaysVO> call, Throwable t) {
////                        }
////                    });
//
////                    tempItem.sortItemsByUnitStandard();
////                                itm = tempItem;
////                    tinyItemList.add(tempItem);
//
//                    break;
//                case "humidity":
//                    try {
//                        itm = new AsyncTaskHumidityInfo(this).execute().get();
//                    } catch (ExecutionException e) {
//                        L.Log("humidity asynctask: ", "execution error__" + e);
//                    } catch (InterruptedException e) {
//                        L.Log("humidity asynctask: ", "interrupted error__ " + e);
//                    }
//                    itm.sortItemsByUnitStandard();
//                    tinyItemList.add(itm);
////                    Call<? extends Cloneable> gsonDataHm = restClient.requestHumidity(NetworkConstants.LOC_LATITUDE,
////                            NetworkConstants.LOC_LONGITUDE, NetworkConstants.SK_API_VERSION);
////
////                    Call<DiscomfortHumidVO> humidity = (Call<DiscomfortHumidVO>) gsonDataHm;
////
////                    humidity.enqueue(new Callback<DiscomfortHumidVO>(){
////                        @Override
////                        public void onResponse(Call<DiscomfortHumidVO> call, Response<DiscomfortHumidVO> response) {
////                            if(response.isSuccessful()){
////                                tempItem = setHumidity(response.body());
////                                tinyItemList.add(tempItem);
////                                L.Log(tinyItemList.size() + "");
////                            }
////                        }
////
////                        @Override
////                        public void onFailure(Call<DiscomfortHumidVO> call, Throwable t) {
////                        }
////                    });
//
//
//                    break;
//                case "carwash":
//                    try {
//                        itm = new AsyncTaskCarwashInfo(this
//                                , MySharedPreferencesManager.getInstance()
//                                .getAdminArea1()).execute().get();
//                    } catch (ExecutionException e) {
//                        L.Log("carwash asynctask: ", "execution error__" + e);
//                    } catch (InterruptedException e) {
//                        L.Log("carwash asynctask: ", "interrupted error__ " + e);
//                    }
//                    itm.sortItemsByUnitStandard();
//                    tinyItemList.add(itm);
////                    Call<? extends Cloneable> gsonDataCw = restClient.requestCarwash(NetworkConstants.SK_API_VERSION);
////
////                    Call<CarwashVO> carwash = (Call<CarwashVO>) gsonDataCw;
////
////                    carwash.enqueue(new Callback<CarwashVO>(){
////                        @Override
////                        public void onResponse(Call<CarwashVO> call, Response<CarwashVO> response) {
////                            if(response.isSuccessful()){
////                                setCarwash(response.body(), tempItem);
////                                tinyItemList.add(tempItem);
////                                L.Log(tinyItemList.size() + "");
////                            }
////                        }
////
////                        @Override
////                        public void onFailure(Call<CarwashVO> call, Throwable t) {
////                        }
////                    });
//
//                    break;
//                case "laundry":
//                    try {
//                        itm = new AsyncTaskLaundryInfo(this).execute().get();
//                    } catch (ExecutionException e) {
//                        L.Log("laundry asynctask: ", "execution error__" + e);
//                    } catch (InterruptedException e) {
//                        L.Log("laundry asynctask: ", "interrupted error__ " + e);
//                    }
//                    itm.sortItemsByUnitStandard();
//                    tinyItemList.add(itm);
////                    Call<? extends Cloneable> gsonDataLD = restClient.requestLaundry(NetworkConstants.LOC_LATITUDE,
////                            NetworkConstants.LOC_LONGITUDE, NetworkConstants.SK_API_VERSION);
////
////                    Call<LaundryVO> laundry = (Call<LaundryVO>) gsonDataLD;
////                    synchronized(tinyItemList) {
////                        laundry.enqueue(new Callback<LaundryVO>() {
////                            @Override
////                            public void onResponse(Call<LaundryVO> call, Response<LaundryVO> response) {
////                                if (response.isSuccessful()) {
////                                    tempItem = setLaundry(response.body());
////                                    L.Log("LAUNDRY____", tempItem.itemName + " " + tempItem.value + " " + tempItem.grade);
////                                    tinyItemList.add(tempItem);
////                                    L.Log(tinyItemList.size() + "1   ");
////                                }
////                            }
////
////                            @Override
////                            public void onFailure(Call<LaundryVO> call, Throwable t) {
////                            }
////                        });
////                    }
////                    // 열루안온다 ㅠㅠ
////                    L.Log(tinyItemList.size() + "2   ");
//                    break;
//            }
//        }
//        L.Log(tinyItemList.size() + "3   ");
//        MySharedPreferencesManager.getInstance().setCheckedItemList(new HashSet<>(tagList));
    }

}
