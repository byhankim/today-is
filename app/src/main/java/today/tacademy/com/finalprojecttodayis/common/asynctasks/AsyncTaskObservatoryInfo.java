package today.tacademy.com.finalprojecttodayis.common.asynctasks;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.concurrent.ExecutionException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import today.tacademy.com.finalprojecttodayis.GalleryTestActivity;
import today.tacademy.com.finalprojecttodayis.common.ItemJSONParser;
import today.tacademy.com.finalprojecttodayis.common.L;
import today.tacademy.com.finalprojecttodayis.common.constants.NetworkConstants;
import today.tacademy.com.finalprojecttodayis.common.managers.MySharedPreferencesManager;
import today.tacademy.com.finalprojecttodayis.common.managers.OkHttpInitSingletonManager;
import today.tacademy.com.finalprojecttodayis.entity.TinyItemVO;

/**
 * Created by Tacademy on 2017-11-01.
 */

public class AsyncTaskObservatoryInfo extends AsyncTask<Integer, Void, ArrayList<TinyItemVO>> {

    private Context context;
    ArrayList<String> itmSelectList;
    ArrayList<TinyItemVO> itmVOList;
    GalleryTestActivity galleryTestActivity;

    public AsyncTaskObservatoryInfo(Context context,
                                    ArrayList<String> itmSelectList,
                                    ArrayList<TinyItemVO> itmVOList,
                                    GalleryTestActivity galleryTestActivity){
        this.context = context;
        this.itmSelectList = itmSelectList;
        this.itmVOList = itmVOList;
        this.galleryTestActivity = galleryTestActivity;
    }

    @Override
    protected ArrayList<TinyItemVO> doInBackground(Integer... integers) {
        Response response = null;
        OkHttpClient toServer; // 연결담당
        String obsvName = "";
        try{
            toServer = OkHttpInitSingletonManager.getOkHttpClient();
            Request request = new Request.Builder()
                  .url(NetworkConstants.URL_REQUEST_OBSERVATORY_NAME)
                  .build();
            L.Log("관측소명url", NetworkConstants.URL_REQUEST_OBSERVATORY_NAME);



            L.Log("[LAT LON]3", MySharedPreferencesManager.getInstance().getLatitude() + ", " +
                    MySharedPreferencesManager.getInstance().getLongitude());

            L.Log("[LAT LON_networkConstants3]", NetworkConstants.LOC_LATITUDE + ", " +
                    NetworkConstants.LOC_LONGITUDE);



            response = toServer.newCall(request).execute();
            if(response.isSuccessful()){
                obsvName = ItemJSONParser.getObservatoryName(response.body().string());
                NetworkConstants.OBSV_NAME = obsvName;
                MySharedPreferencesManager.getInstance().setObsvName(obsvName);
                L.Log("(3)관측소명)) ", obsvName);
            }else{
                L.Log("(3)관측소 요청에러1", " response.isSuccessful이 false라는 뜻");
            }
        }catch(Exception e){
            L.Log("(3)관측소 요청에러2", e + "" );
            e.printStackTrace();
        }finally{
            if(response != null){
                response.close();
            }
        }

//        NetworkConstants.OBSV_NAME = obsvName;
        return null;
    }

    @Override
    protected void onPostExecute(ArrayList<TinyItemVO> s) {
        super.onPostExecute(s);

        //  ak api받아오기// 환경공단 5형제 아이템 있을시 한개만 받아온다(asynctask)
        ArrayList<TinyItemVO> itmListAirKorea = new ArrayList<>();

        TinyItemVO itm;
        if(itmSelectList.contains("pm10") || itmSelectList.contains("pm25")
                || itmSelectList.contains("o3") || itmSelectList.contains("o3")
                || itmSelectList.contains("no2")){
            try{
                itmListAirKorea = new AsyncTaskItemInfo(context).execute().get();
                L.Log("itmSelectList에 환경공단 O", itmListAirKorea.size() + "");
            }catch(ExecutionException e){
                L.Log("items asynctask: ", "execution error__" + e);
            }catch(InterruptedException e){
                L.Log("items asynctask: ", "interrupted error__ " + e);
            }
        }

        Iterator it = itmSelectList.iterator();
        while(it.hasNext()){
            String o = it.next().toString();
            itm = new TinyItemVO();
            switch(o){
                // 환경공단꺼면 arrayList값 갖고온다
                case "pm10":
                    itm = itmListAirKorea.get(0);
                    itm.sortItemsByUnitStandard();
                    itmVOList.add(itm);
                    break;
                case "pm25":
                    itm = itmListAirKorea.get(1);
                    itm.sortItemsByUnitStandard();
                    itmVOList.add(itm);
                    break;
                case "so2":
                    itm = itmListAirKorea.get(2);
                    itm.sortItemsByUnitStandard();
                    itmVOList.add(itm);
                    break;
                case "o3":
                    itm = itmListAirKorea.get(3);
                    itm.sortItemsByUnitStandard();
                    itmVOList.add(itm);
                    break;
                case "no2":
                    itm = itmListAirKorea.get(4);
                    itm.sortItemsByUnitStandard();
                    itmVOList.add(itm);
                    break;
                case "temp":
                    try {
                        String tVal = new AsyncTaskEftcTempInfo(context).execute().get();
                        itm.itemTag = "temp";
                        itm.value = Double.parseDouble(tVal);
                    } catch (ExecutionException e) {
                        L.Log("temp asynctask: ", "execution error__" + e);
                    } catch (InterruptedException e) {
                        L.Log("temp asynctask: ", "interrupted error__ " + e);
                    }
                    itm.sortItemsByUnitStandard();
                    itmVOList.add(itm);


                    break;
                case "uv":
                    try {
                        String result = new AsyncTaskUVInfo(context).execute().get();
                        try {
                            itm.itemTag = "uv";
                            itm.value = Double.parseDouble(result.trim());
                        }catch(NullPointerException e){
                            result = new AsyncTaskUVInfo(context).execute().get();
                            itm.value = Double.parseDouble(result.trim());
                        }

                    } catch (ExecutionException e) {
                        Log.e("uv asynctask: ", "execution error__" + e);
                    } catch (InterruptedException e) {
                        Log.e("uv asynctask: ", "interrupted error__ " + e);
                    }

                    itm.sortItemsByUnitStandard();
                    itmVOList.add(itm);

                    L.Log("ccccc", itm.value + ", " + itm.grade);

                    break;
                case "humidity":
                    try {
                        itm = new AsyncTaskHumidityInfo(context).execute().get();
                    } catch (ExecutionException e) {
                        L.Log("humidity asynctask: ", "execution error__" + e);
                    } catch (InterruptedException e) {
                        L.Log("humidity asynctask: ", "interrupted error__ " + e);
                    }
                    itm.sortItemsByUnitStandard();
                    itmVOList.add(itm);


                    break;
                case "carwash":
                    try {
                        itm = new AsyncTaskCarwashInfo(context
                                , MySharedPreferencesManager.getInstance()
                                .getAdminArea1()).execute().get();
                    } catch (ExecutionException e) {
                        L.Log("carwash asynctask: ", "execution error__" + e);
                    } catch (InterruptedException e) {
                        L.Log("carwash asynctask: ", "interrupted error__ " + e);
                    }
                    itm.sortItemsByUnitStandard();
                    itmVOList.add(itm);

                    break;
                case "laundry":
                    try {
                        itm = new AsyncTaskLaundryInfo(context).execute().get();
                    } catch (ExecutionException e) {
                        L.Log("laundry asynctask: ", "execution error__" + e);
                    } catch (InterruptedException e) {
                        L.Log("laundry asynctask: ", "interrupted error__ " + e);
                    }
                    itm.sortItemsByUnitStandard();
                    itmVOList.add(itm);
                    break;
            }
        }
        L.Log(itmVOList.size() + "   ____Async에서 ivmVOlist size   ");
        MySharedPreferencesManager.getInstance().setCheckedItemList(new HashSet<>(itmSelectList));
//        context.refreshMyGalleryAdapter(itmVOList);
//        GalleryTestActivity.getAppContext().getApplicationContext().getApplicationContext().
        galleryTestActivity.refreshMyGalleryAdapter(itmVOList);

    }
}
