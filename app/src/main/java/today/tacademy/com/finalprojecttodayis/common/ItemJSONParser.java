package today.tacademy.com.finalprojecttodayis.common;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;

import today.tacademy.com.finalprojecttodayis.common.managers.MySharedPreferencesManager;
import today.tacademy.com.finalprojecttodayis.entity.AddressVO;
import today.tacademy.com.finalprojecttodayis.entity.LocationVO;
import today.tacademy.com.finalprojecttodayis.entity.TinyItemVO;

/**
 * Created by Tacademy on 2017-10-31.
 */

public class ItemJSONParser {

    // pm10 파싱(sk planet one)
    public static TinyItemVO getPM10Parsing(String responseJSON) {
        TinyItemVO item = null;
        try {
            // value와 grade받기
            JSONObject dust = new JSONObject(responseJSON);
            dust = dust.getJSONObject("weather");

            JSONArray arr10 = dust.getJSONArray("dust");

            item = new TinyItemVO();

            // pm지수
            JSONObject pm10 = arr10.getJSONObject(0).getJSONObject("pm10");
            item.itemTag = "pm10";
            item.value = Double.parseDouble(pm10.getString("value"));
            item.grade = pm10.getString("grade");


        } catch (JSONException e) {
            L.Log("JSON_PARSER__", "pm10 parse error: " + e);
        }

        return item;
    }

    // sk planet Tmap Geocoding 파서(Reverse Geocoding)
    public static AddressVO getAdressFromCoord(String responseJSON) {
        AddressVO addrVO = null;
        try {
            JSONObject addr = new JSONObject(responseJSON);
            addr = addr.getJSONObject("addressInfo");

            addrVO = new AddressVO();       // 꼭 초기화 해줄것!
            addrVO.adminArea = addr.getString("city_do");
            addrVO.locality = addr.getString("gu_gun");
            addrVO.thoroughfare = addr.getString("adminDong");
            addrVO.legalFare = addr.getString("legalDong");

            L.Log("파싱중 법정동이름:", addrVO.legalFare);

        } catch (JSONException e) {
            L.Log("REVERSE_GEOCODER___", "parse error: " + e);
            addrVO.isUnavailable = true;        // 에러시 true를 반환하여 클라단에서 처리
        }

        return addrVO;
    }

    // sk planet TM coord from WGS84GEO
    public static LocationVO getTMCoordFromLatLon(String responseJSON) {
        L.Log("(2) TMresponseJSON: ", responseJSON);
        LocationVO loc = new LocationVO();      // must init once or NPException awaits you!
        try {
            JSONObject tm = new JSONObject(responseJSON);
            tm = tm.getJSONObject("coordinate");
            loc.tm_x = Double.parseDouble(tm.getString("lat"));
            loc.tm_y = Double.parseDouble(tm.getString("lon"));

        } catch (JSONException e) {
            L.Log("TMCOORD_FROM_WGS84GEO: ", "parse error: " + e);
        }

        return loc;
    }

    public static LocationVO getTMCoordAKFromLegalDong(String responseJSON){
        L.Log("(2) TMresponseJSON AK: ", responseJSON);
        LocationVO loc = new LocationVO();
        String locality = MySharedPreferencesManager.getInstance().getLocality1();  // just in case //sggName
        String legalDong = MySharedPreferencesManager.getInstance().getLegalLocation1();

        try{
            JSONObject tm = new JSONObject(responseJSON);

            int numOfRows = tm.getInt("totalCount");

            JSONArray arr = tm.getJSONArray("list");
            ArrayList<JSONObject> objArr = new ArrayList<>();
            if(numOfRows > 1) { // 결과값이 여러개일때
                for (int i = 0; i < numOfRows; i++) {
                    if (arr.getJSONObject(i).getString("umdName").equals(legalDong)) {
                        objArr.add(arr.getJSONObject(i));
                    }
                }

                for (int i = 0; i < objArr.size(); i++) {
                    if (objArr.get(i).getString("sggName").contains(locality)) {
                        loc.tm_x = Double.parseDouble(objArr.get(i).getString("tmX"));
                        loc.tm_y = Double.parseDouble(objArr.get(i).getString("tmY"));
                    }
                }
            }else{  // 결과값이 한개일때(null처리는 안함!)
                loc.tm_x = Double.parseDouble(arr.getJSONObject(0).getString("tmX"));
                loc.tm_y = Double.parseDouble(arr.getJSONObject(0).getString("tmY"));
            }
            L.Log("TM좌표 에어코리아:", loc.tm_x + ", " + loc.tm_y);

        }catch(JSONException e){
            L.Log("TMCOORD_AK_FROM: ", "parse error: " + e);
        }

        return loc;
    }

    // 한국환경공단 airKorea 관측소 이름 parser
    public static String getObservatoryName(String responseJSON) {
        L.Log("ObservtrJSON: ", responseJSON);
        String obsvName = "";
        try {
            JSONObject obsv = new JSONObject(responseJSON);
            JSONArray jArr = obsv.getJSONArray("list");
            JSONObject obsvInfo = jArr.getJSONObject(0);
            obsvName = obsvInfo.getString("stationName");
            L.Log("관측소명 파싱: ", obsvName);
        } catch (JSONException e) {
            L.Log("OBSV_NAME_JSON_ERROR: ", "parse error: " + e);
        } catch (NullPointerException e){
            L.Log("관측소 파싱 nullpointer e" + e);
        } catch(Exception e) {
            L.Log("관측소명 파싱 Exception:", " " + e);
        }

        return obsvName;
    }

    public static ArrayList<TinyItemVO> getAirItems(String responseJSON) {
        L.Log("환경공단 리턴json: ", responseJSON);
        ArrayList<TinyItemVO> itm = null;
        TinyItemVO it = null;
        String[] itmNames = {"pm10", "pm25", "so2", "o3", "no2"};
        String[] itmValNames = {"pm10Value", "pm25Value", "so2Value", "o3Value", "no2Value"};
        try {
            JSONObject obj = new JSONObject(responseJSON);

            JSONArray arr = obj.getJSONArray("list");
            obj = arr.getJSONObject(0);

            // 환경공단 releaseDate는 여기서!
            String releaseTime = obj.getString("dataTime");
            long dtToStore = convertReleaseTimeToMillis(0, releaseTime);
            L.Log("[환경공단 변환 ms]", dtToStore + "");


            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date dt = new Date(dtToStore);


            L.Log("[환경공단 변환 ms]"
                    , String.format(sdf.format(dt) + ", 지금: "
                            + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                            .format(new Date(System.currentTimeMillis()))));



            itm = new ArrayList<>();

            for (int i = 0; i < itmNames.length; i++) {
                it = new TinyItemVO();
                it.itemTag = itmNames[i];
                double val = 0;
                try {
                    val = Double.parseDouble(obj.getString(itmValNames[i]));
                    it.value =  val;
//                it.setUpAll();        //여서해 딴데서해?
                    itm.add(it);
                } catch (NumberFormatException e) {
                    // value에 "-"이 들어왔을경우 바로 전시간 데이터로 가져온다

                    obj = arr.getJSONObject(1);
                    val = Double.parseDouble(obj.getString(itmValNames[i]));
                    it.value =  val;
                }catch (Exception e) {
                    L.Log("에어코리아 파싱 에러: ", e + "");
                }
                // releaseDate랑 value 각각 저장한다

            }

        } catch (JSONException e) {
            L.Log("ITEMS_JSON_PARSE_ERR1: ", "parse error: " + e);
        } catch (Exception e) {
            L.Log("ITEMS_JSON_PARSE_ERR2: ", " " + e);
        }

        return itm;
    }

    // sk weather planet 체감온도 지수
    public static String getEffectiveTempParsing(String responseJSON) {

        L.Log("[6 체감온도 ", "responsestr]" + responseJSON);
        String tempVal = "";
        try {
            JSONObject obj = new JSONObject(responseJSON);
            obj = obj.getJSONObject("weather").getJSONObject("wIndex");
            JSONArray arr = obj.getJSONArray("wctIndex");
            obj = arr.getJSONObject(0).getJSONObject("current");

            // 체감온도 releaseDate는 여기서!
            String releaseTime = obj.getString("timeRelease");
            long dtToStore = convertReleaseTimeToMillis(1, releaseTime);
            L.Log("[체감온도 변환 ms]", dtToStore + "");

            tempVal = obj.getString("index");
            L.Log("[6 체감온도 parse] ", tempVal);

        } catch (JSONException e) {
            L.Log("eftv temp PARSE ERR: ", "parse error: " + e);
        } catch (Exception e) {
            L.Log("eftv temp PARSE ERR: ", " " + e);
        }
        return tempVal;
    }

    // sk weather planet 자외선 지수
    public static String getUVParsing(String responseJSON) {

        L.Log("[7 uv지수 ", "responsestr]" + responseJSON);

        String uvVal = "";
        try {
            JSONObject obj = new JSONObject(responseJSON);
            obj = obj.getJSONObject("weather").getJSONObject("wIndex");

            // releaseTime 저장
            String releaseTime = obj.getString("timeRelease");
            long dtToStore = convertReleaseTimeToMillis(1, releaseTime);
            L.Log("[자외선지수 변환 ms]", dtToStore + "");

            JSONArray arr = obj.getJSONArray("uvindex");
            obj = arr.getJSONObject(0).getJSONObject("day00");

            if(uvVal.isEmpty() || "".equals(uvVal)) {
                obj = arr.getJSONObject(0).getJSONObject("day01");
                uvVal = obj.getString("index");
            }else{
                uvVal = obj.getString("index");
            }
        } catch (JSONException e) {
            L.Log("uv PARSE ERR: ", "parse error: " + e);
        } catch (Exception e) {
            L.Log("uv PARSE ERR: ", " " + e);
        }

        return uvVal;
    }

    // sk weather planet 불쾌지수 지수
    public static TinyItemVO getHumidityParsing(String responseJSON) {

        L.Log("[8 불쾌지수 파싱" , "responsestr]" + responseJSON);
        TinyItemVO item = null;
        try {
            JSONObject obj = new JSONObject(responseJSON);
            obj = obj.getJSONObject("weather").getJSONObject("wIndex");

            JSONArray arr = obj.getJSONArray("thIndex");
            obj = arr.getJSONObject(0).getJSONObject("current");

            String index = obj.getString("index");

            item = new TinyItemVO();
            item.itemTag = "humidity";
            item.value =  Double.parseDouble(index);
        } catch (JSONException e) {
            L.Log("불쾌지수 PARSE ERR: ", "parse error: " + e);
        } catch (Exception e) {
            L.Log("불쾌지수 PARSE ERR: ", " " + e);
        }
        return item;
    }

    // sk weather planet 세차지수
    public static TinyItemVO getCarwashParsing(String responseJSON, String adminArea) {

        L.Log("[9 세차지수 responseJson] : ", responseJSON + "\n시도명: " + adminArea);
        TinyItemVO item = null;
        String sidoName = "";

        switch (adminArea) {       // 나중에 보정 필요함... 강원영동 강원영서 ㅡㅡ
            case "충청북도":
                sidoName = "충북";
                break;
            case "충청남도":case "대전광역시":
                sidoName = "충남";
                break;
            case "전라북도":
                sidoName = "전북";
                break;
            case "전라남도":case "광주광역시":
                sidoName = "전남";
                break;
            case "경상북도":
                sidoName = "경북";
                break;
            case "경상남도":case "울산광역시":case "부산광역시":
                sidoName = "경남";
                break;
            case "서울특별시": case "경기도":case "인천광역시":
            case "세종특별자치시":case "세종시":
                sidoName = "서울.경기.인천";
                break;
            case "강원도":
                sidoName = "강원영동";
                break;
            case "제주도" :case "제주":
                sidoName = "제주";
                break;
        }

        try {
            JSONObject obj = new JSONObject(responseJSON);
            obj = obj.getJSONObject("weather").getJSONObject("wIndex");

            JSONArray arr = obj.getJSONArray("carWash");
            for (int i = 0; i < arr.length(); i++) {
                if (arr.getJSONObject(i).getString("locationName").contains(sidoName)) {
                    obj = arr.getJSONObject(i);
                }

            }

            // index 100: "세차하기 좋은 날입니다"
            // index 30: "내일 비,눈 예보가 있습니다. 세차 하신다면 실내주차는 필수!"
            // index 20: "세차 다음으로 미루세요!"
            String index = obj.getString("index");

            item = new TinyItemVO();
            item.itemTag = "carwash";
            item.value =  Double.parseDouble(index);
        } catch (JSONException e) {
            L.Log("세차지수 PARSE ERR: ", "parse error: " + e);
        } catch (Exception e) {
            L.Log("세차지수 PARSE ERR: ", " " + e);
        }
        return item;
    }

    // sk weather planet 불쾌지수 지수
    public static TinyItemVO getLaundryParsing(String responseJSON) {

        L.Log("[10 빨래지수 json] : ", responseJSON);
        TinyItemVO item = null;
        try {
            JSONObject obj = new JSONObject(responseJSON);
            obj = obj.getJSONObject("weather").getJSONObject("wIndex");

            JSONArray arr = obj.getJSONArray("laundry");

            obj = arr.getJSONObject(0).getJSONObject("day01"); // 00은 제공되지 않음

            String index = obj.getString("index");

            item = new TinyItemVO();
            item.itemTag = "laundry";
            item.value =  Double.parseDouble(index);
        } catch (JSONException e) {
            L.Log("빨래지수 PARSE ERR: ", "parse error: " + e);
        } catch (Exception e) {
            L.Log("빨래지수 PARSE ERR: ", " " + e);
        }
        return item;
    }
    ///


    public static long convertReleaseTimeToMillis(int apiType, String datetime){
        SimpleDateFormat sdf = null;
        switch(apiType){
            case 0:   // 환경공단
                sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                break;
            case 1:      // sk weather planet
                sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                break;
        }

        sdf.setTimeZone(TimeZone.getTimeZone("UCT"));
        Date date = null;
        try {
//                    cal = skFmt.parse(datetime);
            date = sdf.parse(datetime);
        }catch(ParseException e){
            L.Log("sdf 파스 에러", e + "");
        }

        long millis = date.getTime() - 1000 * 60 * 60 * 9;  // 9시간 빼줘서 보정한다

        return millis;
    }

}
