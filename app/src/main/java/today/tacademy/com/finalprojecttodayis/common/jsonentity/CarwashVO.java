package today.tacademy.com.finalprojecttodayis.common.jsonentity;

import java.util.ArrayList;

/**
 * Created by Tacademy on 2017-11-07.
 */

public class CarwashVO implements Cloneable {
    public Weather weather;
    public class Weather{
        public WIndex wIndex;
        public class WIndex{
            public String timeRelease;
            public ArrayList<Carwash> carWash;
            public class Carwash{
                    public String index;
                    public String locationName;
                    public String comment;
            }
        }
    }
}
