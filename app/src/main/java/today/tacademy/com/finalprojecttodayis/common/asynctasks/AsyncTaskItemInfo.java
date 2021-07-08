package today.tacademy.com.finalprojecttodayis.common.asynctasks;

import android.content.Context;
import android.os.AsyncTask;

import java.util.ArrayList;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import today.tacademy.com.finalprojecttodayis.common.ItemJSONParser;
import today.tacademy.com.finalprojecttodayis.common.L;
import today.tacademy.com.finalprojecttodayis.common.constants.NetworkConstants;
import today.tacademy.com.finalprojecttodayis.common.managers.OkHttpInitSingletonManager;
import today.tacademy.com.finalprojecttodayis.entity.TinyItemVO;

/**
 * Created by Tacademy on 2017-10-31.
 */

public class AsyncTaskItemInfo extends AsyncTask<Integer, Void, ArrayList<TinyItemVO>> {

    private Context context;

    // 한국 환경공단에서...
    public AsyncTaskItemInfo(Context context){this.context = context;}

    @Override
    protected ArrayList<TinyItemVO> doInBackground(Integer... integers) {
        ArrayList<TinyItemVO> itmList = null;
        Response response = null;
        OkHttpClient toServer; // 연결담당

        try{
            itmList = new ArrayList<>();
            toServer = OkHttpInitSingletonManager.getOkHttpClient();
            Request request = new Request.Builder()
                    .url(NetworkConstants.URL_REQUEST_AIR_QUALITY)
                    .build();
            response = toServer.newCall(request).execute();
            if(response.isSuccessful()){
                String temptemp = response.body().string();
                itmList = ItemJSONParser.getAirItems(temptemp);
            } else {
                L.Log("(1~5) 환경공단 요청에러1", " " + response.message().toString());
            }
        }catch (Exception e){
            L.Log("(1~5) 환경공단 요청에러2", response.message().toString() + "   " + e);
            e.printStackTrace();
        }finally{
            if(response != null){
                response.close();
            }
        }
        return itmList;
    }
}
