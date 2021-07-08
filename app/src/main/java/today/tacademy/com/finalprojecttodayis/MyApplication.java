package today.tacademy.com.finalprojecttodayis;

import android.app.Application;
import android.content.Context;

/**
 * Created by Tacademy on 2017-10-26.
 */

public class MyApplication extends Application {
    private static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;    // Applciation은 context를 상속받는다

    }
    public static Context getContext() {return mContext;}
}
