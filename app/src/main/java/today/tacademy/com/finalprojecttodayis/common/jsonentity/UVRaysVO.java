package today.tacademy.com.finalprojecttodayis.common.jsonentity;

import java.util.ArrayList;

/**
 * Created by Tacademy on 2017-11-07.
 */

public class UVRaysVO implements Cloneable {
    public Weather weather;
    public class Weather{
        public WIndex wIndex;
        public class WIndex{
            public String timeRelease;
            public ArrayList<UvIndex> uvindex;
            public class UvIndex{
                public Day00 day00;
                public Day01 day01;
                public Day02 day02;
                public class Day00{
                    public String index;
                    public String comment;
                }
                public class Day01{
                    public String index;
                    public String comment;
                }
                public class Day02{
                    public String index;
                    public String comment;
                }
            }
        }
    }
}
