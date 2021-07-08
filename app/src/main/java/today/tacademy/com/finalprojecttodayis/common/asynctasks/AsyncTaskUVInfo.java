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

import static today.tacademy.com.finalprojecttodayis.common.constants.NetworkConstants.URL_REQUEST_UV;

/**
 * Created by Tacademy on 2017-11-03.
 */

public class AsyncTaskUVInfo extends AsyncTask<Integer, Void, String> {
    //    static Class owner;
//
//    {
//        owner = this.getClass();
//    }
    private Context context;

    public AsyncTaskUVInfo(Context context){this.context = context;}

    @Override
    protected String doInBackground(Integer... integers) {
        Response response = null;
        OkHttpClient toServer; // 연결담당
        String uvVal = null;

        try {
            toServer = OkHttpInitSingletonManager.getOkHttpClient();
            Request request = new Request.Builder()
                    .url(URL_REQUEST_UV)
                    .header("Accept", "application/json")
                    .addHeader("appKey", context.getResources().getString(R.string.app_key1))
                    .build();
            response = toServer.newCall(request).execute();

//            if(response.code() == 200) {
                if (response.isSuccessful()) {
                    uvVal = ItemJSONParser.getUVParsing(response.body().string());
                    L.Log("(7) UV지수:  ", uvVal + "");
                } else {
                    L.Log("(7) UV지수 요청에러1", " response.isSuccessful이 false라는 뜻");
                }
//            }else{
                if(response.code() == 502) L.Log("(7) UV지수 502 bad gateway 에러");
//            }
        } catch (Exception e) {

            L.Log("(7) UV지수 요청에러2", e + "");
            e.printStackTrace();
        } finally {
            if (response != null) {
                response.close();
            }
        }

        return uvVal;
    }
}
