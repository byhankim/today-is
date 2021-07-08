package today.tacademy.com.finalprojecttodayis.common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import today.tacademy.com.finalprojecttodayis.R;

/**
 * Created by Tacademy on 2017-10-31.
 */

public final class CheckBoxFindCommon {
    private static HashMap<String, Integer> checkBoxMap;
    static{
        checkBoxMap = new HashMap<>(10);
        checkBoxMap.put("pm10", R.id.itm1);
        checkBoxMap.put("pm25", R.id.itm2);
        checkBoxMap.put("so2", R.id.itm3);
        checkBoxMap.put("o3", R.id.itm4);
        checkBoxMap.put("no2", R.id.itm5);
        checkBoxMap.put("temp", R.id.itm6);
        checkBoxMap.put("uv", R.id.itm7);
        checkBoxMap.put("humidity", R.id.itm8);
        checkBoxMap.put("carwash", R.id.itm9);
        checkBoxMap.put("laundry", R.id.itm10);
    }
    public static Integer  getItemKey(String key){
        return checkBoxMap.get(key);
    }

    public static ArrayList<Integer> getAllItemKeys(){
        ArrayList<Integer> keyValues = new ArrayList<>();
        Iterator it = checkBoxMap.entrySet().iterator();
        while(it.hasNext()){
            Map.Entry pair = (Map.Entry)it.next();
            keyValues.add((Integer)pair.getValue());
        }
        return keyValues;
    }
}
