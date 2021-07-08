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

import static today.tacademy.com.finalprojecttodayis.common.constants.NetworkConstants.URL_REQUEST_EFCT_TEMP;

/**
 * Created by Tacademy on 2017-11-02.
 */

public class AsyncTaskEftcTempInfo extends AsyncTask<Integer, Void, String> {

    Context context;

    public AsyncTaskEftcTempInfo(Context context){this.context = context;}

    @Override
    protected String doInBackground(Integer... integers) {
        Response response = null;
        OkHttpClient toServer; // 연결담당
        String tempVal = null;


        try{
            toServer = OkHttpInitSingletonManager.getOkHttpClient();
            Request request = new Request.Builder()
                    .url(URL_REQUEST_EFCT_TEMP)
                    .header("Accept", "application/json")
                    .addHeader("appKey", context.getResources().getString(R.string.app_key))
                    .build();
            response = toServer.newCall(request).execute();
            if(response.isSuccessful()){
                String temptemp = response.body().string();
                tempVal = ItemJSONParser.getEffectiveTempParsing(temptemp);
                L.Log("(6 체감온도)", tempVal);
            }else{
                L.Log("(6 체감온도) 요청에러1", " response.isSuccessful이 false라는 뜻");
            }

        }catch(Exception e){

            L.Log("(6 체감온도) 요청에러2", e + "" );
            e.printStackTrace();
        }finally{
            if(response != null){
                response.close();
            }
        }


        return tempVal;
    }


}
