package today.tacademy.com.finalprojecttodayis.common.jsonentity;

import java.util.ArrayList;

/**
 * Created by Tacademy on 2017-11-07.
 */

public class EffectiveTempVO implements Cloneable {
    public Weather weather;
    public class Weather{
        public WIndex wIndex;
        public class WIndex{
            public ArrayList<WctIndex> wctIndex;
            public class WctIndex{
                public Current current;
//                public Forecast forecast;
                public class Current{
                    public String timeRelease;
                    public String index;
                }
//                public class Forecast {
//                    public String index4hour;
//                    public String index7hour;
//                }
            }
        }
    }
}
