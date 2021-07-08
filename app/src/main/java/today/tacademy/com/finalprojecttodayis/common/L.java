package today.tacademy.com.finalprojecttodayis.common;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by Tacademy on 2017-11-03.
 */

public class L {

    public static final boolean DEBUG = true;
    public static final boolean DEBUG_PREF = true;

    public static void L(String msg){
        if(DEBUG_PREF){
            Log.e("[PREF_TEST] ", msg);
        }
    }

    public static void Log(String msg){
        if(DEBUG) Log.e("[Logger]", msg);
    }

    public static void Log(int index, String msg){
        if(DEBUG) Log.e("[Logger #" + index + "] ", msg);
    }

    public static void Log(String tag, String msg){
        if(DEBUG) Log.e(tag, msg);
    }

    public static void Log(String tag, int index, String msg){
        if(DEBUG) Log.e("[" + tag + " #" + index + "] ", msg);
    }


    public static void T(Context context, String str){
        if(DEBUG) Toast.makeText(context, str, Toast.LENGTH_SHORT).show();
    }

    public static void S(int millis){
        try {
            Thread.sleep(millis);
        }catch(InterruptedException e){
            L.Log("thrad sleep됐따!");
        }
    }
}
