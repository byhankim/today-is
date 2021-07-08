package today.tacademy.com.finalprojecttodayis.common.asynctasks;

import android.content.Context;
import android.os.AsyncTask;

import java.util.ArrayList;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import today.tacademy.com.finalprojecttodayis.GalleryTestActivity;
import today.tacademy.com.finalprojecttodayis.common.ItemJSONParser;
import today.tacademy.com.finalprojecttodayis.common.L;
import today.tacademy.com.finalprojecttodayis.common.constants.NetworkConstants;
import today.tacademy.com.finalprojecttodayis.common.managers.MySharedPreferencesManager;
import today.tacademy.com.finalprojecttodayis.common.managers.OkHttpInitSingletonManager;
import today.tacademy.com.finalprojecttodayis.entity.LocationVO;
import today.tacademy.com.finalprojecttodayis.entity.TinyItemVO;

import static today.tacademy.com.finalprojecttodayis.common.constants.NetworkConstants.URL_REQUEST_OBTAIN_TM_COORD_AIRKOREA;

/**
 * Created by Tacademy on 2017-11-09.
 */

public class AsyncTaskTMCoordAKInfo extends AsyncTask<Integer, Void, Void> {
    private Context context;
    ArrayList<String> itmSelectList;
    ArrayList<TinyItemVO> itmVOList;
    GalleryTestActivity galleryTestActivity;

    LocationVO mLoc = null;

    public AsyncTaskTMCoordAKInfo(Context context, ArrayList<String> itmSelectList
            , ArrayList<TinyItemVO> itmVOList, GalleryTestActivity galleryTestActivity) {
        this.context = context;
        this.itmSelectList = itmSelectList;
        this.itmVOList = itmVOList;
        this.galleryTestActivity = galleryTestActivity;
    }

    @Override
    protected Void doInBackground(Integer... integers) {
        Response response = null;
        OkHttpClient toServer;

        L.Log("[LAT LON2]", MySharedPreferencesManager.getInstance().getLatitude() + ", " +
                MySharedPreferencesManager.getInstance().getLongitude());

        L.Log("[LAT LON_networkConstants2]", NetworkConstants.LOC_LATITUDE + ", " +
                NetworkConstants.LOC_LONGITUDE);
        L.Log("[TM좌표 얻기]", "환경공단거!!!, " +  URL_REQUEST_OBTAIN_TM_COORD_AIRKOREA);

        try{
            toServer = OkHttpInitSingletonManager.getOkHttpClient();
            Request request = new Request.Builder()
                    .url(URL_REQUEST_OBTAIN_TM_COORD_AIRKOREA)
                    .build();

            response = toServer.newCall(request).execute();
            if(response.isSuccessful()){
                mLoc = ItemJSONParser.getTMCoordAKFromLegalDong(response.body().string());
                NetworkConstants.TM_X = mLoc.tm_x + "";
                NetworkConstants.TM_Y = mLoc.tm_y + "";
            }else{
                L.Log("AK_TM좌표 취득 요청에러1", " " + response.message().toString());
            }

        }catch(Exception e){
            L.Log("AK_TM좌표 취득 요청에러2", response.message().toString() + "   " + e);
            e.printStackTrace();
        }finally{
            if(response != null){
                response.close();
            }
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void v) {
        super.onPostExecute(v);

        LocationVO loc = new LocationVO();
        new AsyncTaskObservatoryInfo(context, itmSelectList, itmVOList, galleryTestActivity).execute(1);
//            NetworkConstants.OBSV_NAME = loc.obsvName;
    }
}
