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
import today.tacademy.com.finalprojecttodayis.entity.TinyItemVO;

/**
 * Created by Tacademy on 2017-11-01.
 */

public class AsyncTaskWeatherInfo extends AsyncTask<Integer, Void, TinyItemVO> {

    private Context context;

    public AsyncTaskWeatherInfo(Context context){this.context = context;}

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected TinyItemVO doInBackground(Integer... integers){
//            int page = integers[0];
        Response response = null; //응답 담당
        OkHttpClient toServer; // 연결 담당
        TinyItemVO itemVO = null;
        try{
            toServer = OkHttpInitSingletonManager.getOkHttpClient();
            // GET 방식은 따로 선언하지 않는다
            Request request = new Request.Builder()
                    .url(String.format(NetworkConstants.SERVER_URL_REQUEST_FINE_DUST))
                    .header("Accept", "application/json")
                    .addHeader("appKey", context.getResources().getString(R.string.app_key))
                    .build();

            response = toServer.newCall(request).execute();
            if(response.message() == null){
                L.Log("weather요청 response:" , " response는 null");
            }else{
                L.Log("weather요청 response:" , " response는 not null");
            }
            if(response.isSuccessful()){
                itemVO = ItemJSONParser.getPM10Parsing(response.body().string());
            }else{
                L.Log("웨더플래닛 pm10요청에러1", response.message().toString());
            }
        }catch(Exception e){
            L.Log("웨더플래닛 pm10 요청에러2", response.message().toString() + "  " + e);
        }finally{
            if(response != null){
                response.close();
            }
        }
        return itemVO;
    }
}
