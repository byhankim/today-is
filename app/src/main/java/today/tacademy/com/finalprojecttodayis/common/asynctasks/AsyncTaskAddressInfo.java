package today.tacademy.com.finalprojecttodayis.common.asynctasks;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.TextView;

import java.util.ArrayList;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import today.tacademy.com.finalprojecttodayis.GalleryTestActivity;
import today.tacademy.com.finalprojecttodayis.R;
import today.tacademy.com.finalprojecttodayis.common.ItemJSONParser;
import today.tacademy.com.finalprojecttodayis.common.L;
import today.tacademy.com.finalprojecttodayis.common.constants.NetworkConstants;
import today.tacademy.com.finalprojecttodayis.common.managers.MySharedPreferencesManager;
import today.tacademy.com.finalprojecttodayis.common.managers.OkHttpInitSingletonManager;
import today.tacademy.com.finalprojecttodayis.entity.AddressVO;
import today.tacademy.com.finalprojecttodayis.entity.LocationVO;
import today.tacademy.com.finalprojecttodayis.entity.TinyItemVO;

/**
 * Created by Tacademy on 2017-11-01.
 */

public class AsyncTaskAddressInfo extends AsyncTask<Integer, Void, Void> {

    private Context context;
    AddressVO addrVO = null;

    ArrayList<String> itmSelectList;
    ArrayList<TinyItemVO> itmVOList;
    TextView tagLocation;
    GalleryTestActivity galleryTestActivity;

    @Override
    protected void onPreExecute() {
        super.onPreExecute();



    }



    public AsyncTaskAddressInfo (Context context, TextView tagLocation, ArrayList<String> itmSelectList, ArrayList<TinyItemVO> itmVOList,
                                 GalleryTestActivity galleryTestActivity){
        this.context = context;
        this.tagLocation = tagLocation;
        this.itmSelectList = itmSelectList;
        this.itmVOList = itmVOList;
        this.galleryTestActivity = galleryTestActivity;
    }

    @Override
    protected Void doInBackground(Integer... integers) {
        Response response = null;
        OkHttpClient toServer; // 연결담당
        try{
            toServer = OkHttpInitSingletonManager.getOkHttpClient();
            Request request = new Request.Builder()
                    .url(String.format(NetworkConstants.URL_REQUEST_REVERSE_GEOCODING))
                    .header("Accept", "application/json")
                    .addHeader("appKey", context.getResources().getString(R.string.app_key))
                    .build();
            response = toServer.newCall(request).execute();
            if(response.isSuccessful()){
                addrVO = ItemJSONParser.getAdressFromCoord(response.body().string());
                // ============================================================================
                //             java.lang.IllegalStateException: closed 에러에 관하여
                //             (https://github.com/square/okhttp/issues/1240) 답변 첨부
                // ----------------------------------------------------------------------------
                // Are you reading the response body 2x? You can only call string() once.

                // Because response body can be huge so OkHttp doesn’t store it in memory,
                // it reads it as a stream from network when you need it.

                // When you read body as a string() OkHttp downloads response body
                // and returns it to you without keeping reference to the string,
                // it can’t be downloaded twice without new request.
                // ============================================================================

//                L.Log("DOINBACKGROUND___", response.body().string());
            }else{
                L.Log("시군구 요청에러1", " " + response.message().toString());
            }

        }catch(Exception e){
            L.Log("시군구 요청에러2", response.message().toString() + "   " + e);
            e.printStackTrace();
        }finally{
            if(response != null){
                response.close();
            }
        }

        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);

        String temp = "";
        temp = addrVO.adminArea + " " + addrVO.locality + " "  + addrVO.thoroughfare;

        // 위치변경시 저장 및 display하기
        MySharedPreferencesManager.getInstance().setMyLocation1(temp);
        MySharedPreferencesManager.getInstance().setLegalLocation1(addrVO.legalFare);
        MySharedPreferencesManager.getInstance().setAdminArea1(addrVO.adminArea);
        MySharedPreferencesManager.getInstance().setLocality1(addrVO.locality);
        NetworkConstants.LEGAL_DONG = addrVO.legalFare;     // constants에서 법정동이름바꿈
        tagLocation.setText(temp);

        LocationVO loc = new LocationVO();
        // tm좌표 및 관측소 이름 불러온다음 pref에 저장(tm은 저장할필요 없음)
        new AsyncTaskTMCoordAKInfo(context, itmSelectList, itmVOList, galleryTestActivity)
                    .execute();
    }
}
