package today.tacademy.com.finalprojecttodayis.common.jsonentity;

import java.util.ArrayList;

/**
 * Created by Tacademy on 2017-11-07.
 */

public class LaundryVO implements Cloneable {
    public Weather weather;
    public class Weather{
        public WIndex wIndex;
        public  class WIndex{
            public String timeRelease;
            public ArrayList<Laundry> laundry;
            public class Laundry{
                public Day00 day00;
                public Day01 day01;
                public Day02 day02;
                public class Day00{
                    public String comment;
                    public String index;
                }
                public class Day01{
                    public String comment;
                    public String index;
                }
                public class Day02{
                    public String comment;
                    public String index;
                }
            }
        }
    }
}
