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
 * Created by Tacademy on 2017-11-03.
 */

public class AsyncTaskCarwashInfo extends AsyncTask<Integer, Void, TinyItemVO> {

    private Context context;
    private String sidoName;

    public AsyncTaskCarwashInfo(Context context, String sidoName){
        this.context = context;
        this.sidoName = sidoName;
    }

    @Override
    protected TinyItemVO doInBackground(Integer... integers) {
        Response response = null;
        OkHttpClient toServer; // 연결담당
        TinyItemVO itm = null;
        try{
            toServer = OkHttpInitSingletonManager.getOkHttpClient();
            Request request = new Request.Builder()
                    .url(NetworkConstants.URL_REQUEST_CARWASH)
                    .header("Accept", "application/json")
                    .addHeader("appKey", context.getResources().getString(R.string.app_key))
                    .build();
            response = toServer.newCall(request).execute();
            if(response.isSuccessful()){
                itm = ItemJSONParser.getCarwashParsing(response.body().string(), sidoName);
                L.Log("(9) 세차지수)) ", itm.value + "");
            }else{
                L.Log("(9) 세차지수 요청에러1", " response.isSuccessful이 false라는 뜻");
            }
        }catch(Exception e){

            L.Log("(9) 세차지수 요청에러2", e + "" );
            e.printStackTrace();
        }finally{
            if(response != null){
                response.close();
            }
        }

        return itm;
    }
}
