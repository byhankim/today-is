package today.tacademy.com.finalprojecttodayis.common.asynctasks;

import android.content.Context;
import android.os.AsyncTask;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import today.tacademy.com.finalprojecttodayis.R;
import today.tacademy.com.finalprojecttodayis.common.ItemJSONParser;
import today.tacademy.com.finalprojecttodayis.common.L;
import today.tacademy.com.finalprojecttodayis.common.constants.NetworkConstants;
import today.tacademy.com.finalprojecttodayis.common.managers.OkHttpInitSingletonManager;
import today.tacademy.com.finalprojecttodayis.entity.LocationVO;

/**
 * Created by Tacademy on 2017-10-31.
 */

public class AsyncTaskTMCoordInfo extends AsyncTask<Integer, Void, LocationVO> {

    private Context context;

    public AsyncTaskTMCoordInfo(Context context) { this.context = context;}

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected LocationVO doInBackground(Integer... integers) {
        Response response = null;
        OkHttpClient toServer;
        LocationVO mLoc = null;
        try{
            toServer = OkHttpInitSingletonManager.getOkHttpClient();
            Request request = new Request.Builder()
                    .url(String.format(NetworkConstants.URL_REQUEST_OBTAIN_TM_COORD))
                    .header("Accept", "application/json")
                    .addHeader("appKey", context.getResources().getString(R.string.app_key))
                    .build();

            response = toServer.newCall(request).execute();
            if(response.isSuccessful()){
                mLoc = ItemJSONParser.getTMCoordFromLatLon(response.body().string());
            }else{
                L.Log("TM좌표 취득 요청에러1", " " + response.message().toString());
            }

        }catch(Exception e){
            L.Log("TM좌표 취득 요청에러2", response.message().toString() + "   " + e);
            e.printStackTrace();
        }finally{
            if(response != null){
                response.close();
            }
        }
        return mLoc;
    }

    @Override
    protected void onPostExecute(LocationVO locationVO) {
        super.onPostExecute(locationVO);
    }
}
