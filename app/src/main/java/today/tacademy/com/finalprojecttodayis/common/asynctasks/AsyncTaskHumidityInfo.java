package today.tacademy.com.finalprojecttodayis.common.asynctasks;

import android.content.Context;
import android.os.AsyncTask;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import today.tacademy.com.finalprojecttodayis.R;
import today.tacademy.com.finalprojecttodayis.common.ItemJSONParser;
import today.tacademy.com.finalprojecttodayis.common.L;
import today.tacademy.com.finalprojecttodayis.common.managers.OkHttpInitSingletonManager;
import today.tacademy.com.finalprojecttodayis.entity.TinyItemVO;

import static today.tacademy.com.finalprojecttodayis.common.constants.NetworkConstants.URL_REQUEST_HUMIDITY;

/**
 * Created by Tacademy on 2017-11-03.
 */

public class AsyncTaskHumidityInfo extends AsyncTask<Integer, Void, TinyItemVO> {

    private Context context;

    public AsyncTaskHumidityInfo(Context context) {
        this.context = context;
    }

    @Override
    protected TinyItemVO doInBackground(Integer... integers) {
        Response response = null;
        OkHttpClient toServer; // 연결담당
        TinyItemVO item = null;
        try{
            toServer = OkHttpInitSingletonManager.getOkHttpClient();
            Request request = new Request.Builder()
                    .url(URL_REQUEST_HUMIDITY)
                    .header("Accept", "application/json")
                    .addHeader("appKey", context.getResources().getString(R.string.app_key1))
                    .build();
            response = toServer.newCall(request).execute();
            if(response.isSuccessful()){
                item = ItemJSONParser.getHumidityParsing(response.body().string());
                L.Log("(8) 불쾌지수" + item.value);
            }else{
                L.Log("(8) 불쾌지수 요청에러1", " response.isSuccessful이 false라는 뜻");
            }
        }catch(Exception e){

            L.Log("(8) 불쾌지수 요청에러2", e + "" );
            e.printStackTrace();
        }finally{
            if(response != null){
                response.close();
            }
        }

        return item;
    }
}