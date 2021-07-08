package today.tacademy.com.finalprojecttodayis.entity;

import today.tacademy.com.finalprojecttodayis.R;
import today.tacademy.com.finalprojecttodayis.common.L;
import today.tacademy.com.finalprojecttodayis.common.managers.MySharedPreferencesManager;

/**
 * Created by Tacademy on 2017-10-26.
 */

public class TinyItemVO {
    public double value;
    public int imgId;
    public String grade;
    public String itemName;
    public String itemTag;
    //임시
    public String releaseTime;
    public long releaseTimeInMillis;


    public String msgComp;
    public String msgAdv;
    public String addrs;
    public String currTime;
//    public int innerIndColor;


    // bgr img
    int bgrImgId;

    public TinyItemVO(){}

    public TinyItemVO(String itemName, float value){
        this.itemName = itemName;
        this.value = value;
    }

    public TinyItemVO(String itemName, String grade){
        this.itemName= itemName;
        this.grade = grade;
        setImgAndBgrImg();
    }

    public TinyItemVO(String itemName, String grade, float value, int imgId){
        this.itemName = itemName;
        this.value = value;
        this.grade = grade;

        setImgAndBgrImg();
    }

    public void sortItemsByUnitStandard(){
        L.Log("////CHECKEDLIST555/////", itemTag + "");
        if(MySharedPreferencesManager.getInstance().getIsUnitWHO()){
            sortItemsByWhoStd();
        }else{
            sortItemsByKRStandard();
        }
    }

    private void sortItemsByKRStandard(){     // 우리나라 기준
        L.Log("아이템태그: ", itemTag);
        switch(itemTag){
//            case "pm10":case "미세먼지":
            case "pm10":
                itemName = "미세먼지";
                if(value < 31) {
                    grade = "좋음";
                    imgId = R.drawable.ic_tiny_heart_freepik_flat;
                    bgrImgId = R.drawable.theme_bgr_good;
                    msgComp = "미세먼지 걱정 없는 맑은 날입니다";
                    msgAdv = "마스크 없이 맑은 공기를 즐기세요";
                }else if(value < 51){
                    grade = "보통";
                    imgId = R.drawable.ic_tiny_half_robinkylander_flat;
                    bgrImgId = R.drawable.theme_bgr_mediocre;
                    msgComp = "미세먼지 상태가 보통입니다";
                    msgAdv = "민감 환자군에게 마스크를 권장합니다";
                }else if(value < 101){
                    grade = "보통";
                    imgId = R.drawable.ic_tiny_half_robinkylander_flat;
                    bgrImgId = R.drawable.theme_bgr_mediocre;
                    msgComp = "WHO 기준으로는 나쁨 상태입니다.";
                    msgAdv = "외출시 꼭 마스크 착용하세요";
                }else if(value < 151){
                    grade = "나쁨";
                    imgId = R.drawable.ic_tiny_exclamation_freepik_flat;
                    bgrImgId = R.drawable.theme_bgr_bad;
                    msgComp = "WHO 기준으로 매우 나쁨 상태입니다";
                    msgAdv = "가급적 외출 자제하시고 창문 닫으세요";
                }else if(value < 201){ // official은 600까지 매우나쁨
                    grade = "매우나쁨";
                    imgId = R.drawable.ic_tiny_radioactive_freepik_flat;
                    bgrImgId = R.drawable.theme_bgr_very_bad;
                    msgComp = "미세먼지가 매우 심각합니다";
                    msgAdv = "외출 자제하시고 창문 꼼꼼히 닫으세요";
                }else{
                    grade = "심각";
                    imgId = R.drawable.ic_tiny_radioactive_freepik_flat;
                    bgrImgId = R.drawable.theme_bgr_fatal;
                    msgComp = "미세먼지가 매우 심각합니다.";
                    msgAdv = "가급적 외출 자제하시고 창문 닫으세요";
                }

//                L.T(GalleryTestActivity.getAppContext(), "item값들: " + itemName + ", " + value + ", " + grade + "!");
                break;
            case "pm25":
                itemName = "초미세먼지";
                if(value < 16) {
                    grade = "좋음";
                    imgId = R.drawable.ic_tiny_heart_freepik_flat;
                    bgrImgId = R.drawable.theme_bgr_good;
                    msgComp = "초미세먼지 걱정 없는 맑은 날입니다";
                    msgAdv = "마스크 없이 맑은 공기를 즐기세요";
                }else if(value < 26){
                    grade = "보통";
                    imgId = R.drawable.ic_tiny_half_robinkylander_flat;
                    bgrImgId = R.drawable.theme_bgr_mediocre;
                    msgComp = "초미세먼지 상태가 보통입니다";
                    msgAdv = "민감 환자군에게 마스크를 권장합니다";
                }else if(value < 51){
                    grade = "보통";
                    imgId = R.drawable.ic_tiny_half_robinkylander_flat;
                    bgrImgId = R.drawable.theme_bgr_mediocre;
                    msgComp = "WHO 기준으로 나쁨 상태입니다.";
                    msgComp = "외출시 마스크를 필히 착용하세요";
                }else if(value < 101){
                    grade = "나쁨";
                    imgId = R.drawable.ic_tiny_exclamation_freepik_flat;
                    bgrImgId = R.drawable.theme_bgr_bad;
                    msgComp = "WHO 기준으로 매우 나쁨 상태입니다";
                    msgAdv = "외출 자제하시고 창문 꼭 닫으세요";
                }else if(value < 151){ // 원래는 500까지 매우나쁨
                    grade = "매우나쁨";
                    imgId = R.drawable.ic_tiny_radioactive_freepik_flat;
                    bgrImgId = R.drawable.theme_bgr_very_bad;
                    msgComp = "초미세먼지가 매우 심각합니다";
                    msgAdv = "외출 자제하시고 창문 닫으세요";
                }else{
                    grade = "심각";
                    imgId = R.drawable.ic_tiny_radioactive_freepik_flat;
                    bgrImgId = R.drawable.theme_bgr_fatal;
                    msgComp = "초미세먼지가 매우 심각합니다";
                    msgAdv = "외출 자제하시고 창문 닫으세요";
                }
                break;
            case "so2": // 아황산가스, sulfur dioxide, so2
                itemName = "아황산가스";
                if(value < 0.021) {
                    grade = "좋음";
                    imgId = R.drawable.ic_tiny_heart_freepik_flat;
                    bgrImgId = R.drawable.theme_bgr_good;
                    msgComp = "아황산가스 걱정 없어요";
                    msgAdv = "다른 항목들도 확인해 보세요";
                }else if(value < 0.031){
                    grade = "보통";
                    imgId = R.drawable.ic_tiny_half_robinkylander_flat;
                    bgrImgId = R.drawable.theme_bgr_mediocre;
                    msgComp = "걱정 안 하셔도 좋습니다";
                    msgAdv = "호흡기 민감군은 신경써주세요";
                }else if(value < 0.051){
                    grade = "보통";
                    imgId = R.drawable.ic_tiny_half_robinkylander_flat;
                    bgrImgId = R.drawable.theme_bgr_mediocre;
                    msgComp = "인체에 약간의 영향이 올 수 있습니다";
                    msgAdv = "호흡기 민감군은 신경써주세요";
                }else if(value < 0.151){
                    grade = "나쁨";
                    imgId = R.drawable.ic_tiny_exclamation_freepik_flat;
                    bgrImgId = R.drawable.theme_bgr_bad;
                    msgComp = "식물에 피해가 올 수 있는 수준입니다";
                    msgAdv = "호흡기 질환자는 특히 유의해 주세요";
                }else if(value < 1){
                    grade = "매우나쁨";
                    imgId = R.drawable.ic_tiny_radioactive_freepik_flat;
                    bgrImgId = R.drawable.theme_bgr_very_bad;
                    msgComp = "걱정 안 하셔도 좋습니다";
                    msgAdv = "다른 항목들도 체크해 보세요";
                }else{
                    grade = "심각";
                    imgId = R.drawable.ic_tiny_radioactive_freepik_flat;
                    bgrImgId = R.drawable.theme_bgr_fatal;
                    msgComp = "인체에 치명적 영향을 미칠 수 있습니다";
                    msgAdv = "화학물질용 마스크를 반드시 착용하세요";
                }
                break;
            case "o3": // 오존, ozone, o3
                itemName = "오존";
                if(value < 0.031) {
                    grade = "좋음";
                    imgId = R.drawable.ic_tiny_heart_freepik_flat;
                    bgrImgId = R.drawable.theme_bgr_good;
                    msgComp = "튼튼한 오존층!";
                    msgAdv = "다른 항목도 확인해 보세요";
                }else if(value < 0.091){
                    grade = "보통";
                    imgId = R.drawable.ic_tiny_half_robinkylander_flat;
                    bgrImgId = R.drawable.theme_bgr_mediocre;
                    msgComp = "기관지 및 인후 관련 환자는 주의하세요";
                    msgAdv = "특별한 작용은 없습니다";
                }else if(value < 0.151){
                    grade = "나쁨";
                    imgId = R.drawable.ic_tiny_exclamation_freepik_flat;
                    bgrImgId = R.drawable.theme_bgr_bad;
                    msgComp = "오존은 피부, 기관지, 폐 모두 영향을";
                    msgAdv = "끼칩니다. 외출은 자제, 마스크는 필수!";
                }else if(value < 6){
                    grade = "매우나쁨";
                    imgId = R.drawable.ic_tiny_radioactive_freepik_flat;
                    bgrImgId = R.drawable.theme_bgr_very_bad;
                    msgComp = "야외활동 절대 하지마세요!";
                    msgAdv = "기관지와 피부 보호는 필수입니다";
                }else{
                    grade = "심각";
                    imgId = R.drawable.ic_tiny_radioactive_freepik_flat;
                    bgrImgId = R.drawable.theme_bgr_fatal;
                    msgComp = "야외활동 절대 하지마세요!";
                    msgAdv = "기관지와 피부 보호는 필수입니다";
                }
                break;
            case "no2": // 이산화질소, nitrogen dioxide, no2
                itemName = "이산화질소";
                if(value < 0.031) {
                    grade = "좋음";
                    imgId = R.drawable.ic_tiny_heart_freepik_flat;
                    bgrImgId = R.drawable.theme_bgr_good;
                    msgComp = "걱정하지 않아도 좋아요";
                    msgAdv = "다른 항목들도 확인해 보세요";
                }else if(value < 0.061){
                    grade = "보통";
                    imgId = R.drawable.ic_tiny_half_robinkylander_flat;
                    bgrImgId = R.drawable.theme_bgr_mediocre;
                    msgComp = "기관계 민감군은 주의하세요";
                    msgAdv = "미세먼지 없다면 환기 한 번 추천합니다";
                }else if(value < 0.201){
                    grade = "나쁨";
                    imgId = R.drawable.ic_tiny_exclamation_freepik_flat;
                    bgrImgId = R.drawable.theme_bgr_bad;
                    msgComp = "기관계 민감군은 주의하세요";
                    msgAdv = "미세먼지 없다면 환기 한 번 추천합니다";
                }else if(value < 2){
                    grade = "매우나쁨";
                    imgId = R.drawable.ic_tiny_radioactive_freepik_flat;
                    bgrImgId = R.drawable.theme_bgr_very_bad;
                    msgComp = "기관계 민감군은 주의하세요";
                    msgAdv = "미세먼지 없다면 환기 한 번 추천합니다";
                }else{
                    grade = "심각";
                    imgId = R.drawable.ic_tiny_radioactive_freepik_flat;
                    bgrImgId = R.drawable.theme_bgr_fatal;
                    msgComp = "기관계 민감군은 주의하세요";
                    msgAdv = "미세먼지 없다면 환기 한 번 추천합니다";
                }
                break;
            case "temp": // 체감온도
                itemName = "체감온도";
                if(value < 25 && value > 20) {
                    grade = "좋음";
                    imgId = R.drawable.ic_tiny_heart_freepik_flat;
                    bgrImgId = R.drawable.theme_bgr_good;
                    msgComp = value + "도! 선선하고 시원합니다";
                    msgAdv = "외출하기 좋은 기온입니다!";
                }else if(value < 29 && value > 18){
                    grade = "보통";
                    imgId = R.drawable.ic_tiny_half_robinkylander_flat;
                    bgrImgId = R.drawable.theme_bgr_mediocre;
                    msgComp = value + "도! 살만한 온도네요";
                    msgAdv = "아직은 외출할 만 해요!";
                }else if(value < 32 && value > 14){
                    grade = "나쁨";
                    imgId = R.drawable.ic_tiny_exclamation_freepik_flat;
                    bgrImgId = R.drawable.theme_bgr_bad;
                    msgComp = value + "도! 실내외 온도차를 고려해서";
                    msgAdv = "옷차림을 신경쓰도록 해요!";
                }else if(value < 37 || value > 5){
                    grade = "매우나쁨";
                    imgId = R.drawable.ic_tiny_radioactive_freepik_flat;
                    bgrImgId = R.drawable.theme_bgr_very_bad;
                    msgComp = value + "도! 방콕! 하고픈 온도입니다";
                    msgAdv = "중간중간 환기 잊지 마세요!";
                }else{
                    grade = "심각";
                    imgId = R.drawable.ic_tiny_radioactive_freepik_flat;
                    bgrImgId = R.drawable.theme_bgr_fatal;
                    msgComp = value + "도! 으윽...도저히 버틸 수 없습니다";
                    msgAdv = "가급적 외출을 자제하세요";
                }
                break;
            case "uv": // 자외선
                itemName = "자외선";
                // 자외선지수는 WMO/WHO 등 국제기구 등에서 제안하는 “Global Solar UV Index”의 가이드라인을 활용함
                if(value < 21.0) {
                    grade = "좋음";
                    imgId = R.drawable.ic_tiny_heart_freepik_flat;
                    bgrImgId = R.drawable.theme_bgr_good;
                    msgComp = "자외선 걱정할 필요 없어요!";
                    msgAdv = "선크림 바르지 않아도 괜찮습니다";
                }else if(value < 51){
                    grade = "보통";
                    imgId = R.drawable.ic_tiny_half_robinkylander_flat;
                    bgrImgId = R.drawable.theme_bgr_mediocre;
                    msgComp = "선크림이 필수는 아니지만";
                    msgAdv = "민감성 피부라면 꼭 선크림 바르세요";
                }else if(value < 71){
                    grade = "나쁨";
                    imgId = R.drawable.ic_tiny_exclamation_freepik_flat;
                    bgrImgId = R.drawable.theme_bgr_bad;
                    msgComp = "자외선 지수가 많이 높습니다";
                    msgAdv = "선크림 필히 바르세요";
                }else if(value < 101){
                    grade = "매우나쁨";
                    imgId = R.drawable.ic_tiny_radioactive_freepik_flat;
                    bgrImgId = R.drawable.theme_bgr_very_bad;
                    msgComp = "가급적 야외활동 자제하시고";
                    msgAdv = "실내에서도 선크림을 발라주세요";
                }else{
                    grade = "심각";
                    imgId = R.drawable.ic_tiny_radioactive_freepik_flat;
                    bgrImgId = R.drawable.theme_bgr_fatal;
                    msgComp = "가급적 야외활동 자제하시고";
                    msgAdv = "실내에서도 선크림을 발라주세요";
                }
                break;
            case "humidity": // 불쾌지수
                itemName = "불쾌지수";

                if(value < 21) {
                    grade = "좋음";
                    imgId = R.drawable.ic_tiny_heart_freepik_flat;
                    bgrImgId = R.drawable.theme_bgr_good;
                    msgComp = "단 1g도 습기차지 않습니다!";
                    msgAdv = "걱정 말고 야외활동을 즐겨요~";
                }else if(value < 51){
                    grade = "보통";
                    imgId = R.drawable.ic_tiny_half_robinkylander_flat;
                    bgrImgId = R.drawable.theme_bgr_mediocre;
                    msgComp = "습기문제 없어요~";
                    msgAdv = "체감온도 한 번 확인하고 외출해요";
                }else if(value < 81){
                    grade = "나쁨";
                    imgId = R.drawable.ic_tiny_exclamation_freepik_flat;
                    bgrImgId = R.drawable.theme_bgr_bad;
                    msgComp = "습기찬 하루";
                    msgAdv = "습기제거제가 필요합니다";
                }else if(value < 101){
                    grade = "매우나쁨";
                    imgId = R.drawable.ic_tiny_radioactive_freepik_flat;
                    bgrImgId = R.drawable.theme_bgr_very_bad;
                    msgComp = "불쾌! 불쾌! 불쾌!";
                    msgAdv = "실내라면 보일러 한 번 틀어주세요";
                }else{
                    grade = "심각";
                    imgId = R.drawable.ic_tiny_radioactive_freepik_flat;
                    bgrImgId = R.drawable.theme_bgr_fatal;
                    msgComp = "불쾌! 불쾌! 불쾌!";
                    msgAdv = "실내라면 보일러 한 번 틀어주세요";
                }
                break;
            case "carwash": // 세차지수
                itemName = "세차지수";
                if(value > 70) {
                    grade = "좋음";
                    imgId = R.drawable.ic_tiny_heart_freepik_flat;
                    bgrImgId = R.drawable.theme_bgr_good;
                    msgComp = "세차하기 정말 좋은 날씨네요";
                    msgAdv = "기다리지 말고 지금 당장 GO! GO!";
                }else if(value > 50){
                    grade = "보통";
                    imgId = R.drawable.ic_tiny_half_robinkylander_flat;
                    bgrImgId = R.drawable.theme_bgr_mediocre;
                    msgComp = "그럭저럭 세차할 만한 날입니다";
                    msgAdv = "어둑해지기 전에 서두릅시다";
                }else if(value > 20){
                    grade = "나쁨";
                    imgId = R.drawable.ic_tiny_exclamation_freepik_flat;
                    bgrImgId = R.drawable.theme_bgr_bad;
                    msgComp = "오늘은 영 날이 아니예요";
                    msgAdv = "급하지 않다면 다음으로 미루세요";
                }else if(value > -1){
                    grade = "매우나쁨";
                    imgId = R.drawable.ic_tiny_radioactive_freepik_flat;
                    bgrImgId = R.drawable.theme_bgr_very_bad;
                    msgComp = "절대! 네버! 오늘은 안돼요";
                    msgAdv = "아무리 급해도 오늘만은 참으세요";
                }else{
                    grade = "심각";
                    imgId = R.drawable.ic_tiny_radioactive_freepik_flat;
                    bgrImgId = R.drawable.theme_bgr_fatal;
                    msgComp = "절대! 네버! 오늘은 안돼요";
                    msgAdv = "아무리 급해도 오늘만은 참으세요";
                }
                break;
            case "laundry": // 빨래지수
                itemName = "빨래지수";
                if(value > 70) {
                    grade = "좋음";
                    imgId = R.drawable.ic_tiny_heart_freepik_flat;
                    bgrImgId = R.drawable.theme_bgr_good;
                    msgComp = "빨래하기 정말 좋은 날씨네요";
                    msgAdv = "뽀송뽀송 빨래하러 먼저 갈게요~";
                }else if(value > 50){
                    grade = "보통";
                    imgId = R.drawable.ic_tiny_half_robinkylander_flat;
                    bgrImgId = R.drawable.theme_bgr_mediocre;
                    msgComp = "그럭저럭 빨래하기 나쁘지 않아요";
                    msgAdv = "어둑해지기 전에 서두릅시다";
                }else if(value > 20){
                    grade = "나쁨";
                    imgId = R.drawable.ic_tiny_exclamation_freepik_flat;
                    bgrImgId = R.drawable.theme_bgr_bad;
                    msgComp = "오늘은 영 날이 아니예요";
                    msgAdv = "급하지 않다면 다음으로 미루세요";
                }else if(value > -1){
                    grade = "매우나쁨";
                    imgId = R.drawable.ic_tiny_radioactive_freepik_flat;
                    bgrImgId = R.drawable.theme_bgr_very_bad;
                    msgComp = "절대! 네버! 오늘은 안돼요";
                    msgAdv = "아무리 급해도 오늘만은 참으세요";
                }else{
                    grade = "심각";
                    imgId = R.drawable.ic_tiny_radioactive_freepik_flat;
                    bgrImgId = R.drawable.theme_bgr_fatal;
                    msgComp = "절대! 네버! 오늘은 안돼요";
                    msgAdv = "아무리 급해도 오늘만은 참으세요";
                }
                break;
            default:
                itemName = "기타";
                if(value < 30) {
                    grade = "좋음";
                    imgId = R.drawable.ic_tiny_heart_freepik_flat;
                    bgrImgId = R.drawable.theme_bgr_good;
                    msgAdv = "kr default good";
                }else if(value < 50){
                    grade = "보통";
                    imgId = R.drawable.ic_tiny_half_robinkylander_flat;
                    bgrImgId = R.drawable.theme_bgr_mediocre;
                    msgAdv = "kr default soso";
                }else if(value < 76){
                    grade = "나쁨";
                    imgId = R.drawable.ic_tiny_exclamation_freepik_flat;
                    bgrImgId = R.drawable.theme_bgr_bad;
                    msgAdv = "kr default bad";
                }else if(value <= 90){
                    grade = "매우나쁨";
                    imgId = R.drawable.ic_tiny_radioactive_freepik_flat;
                    bgrImgId = R.drawable.theme_bgr_very_bad;
                    msgAdv = "kr default very bad";
                }else{
                    grade = "심각";
                    imgId = R.drawable.ic_tiny_radioactive_freepik_flat;
                    bgrImgId = R.drawable.theme_bgr_fatal;
                    msgAdv = "kr default fatal";
                }

//                L.T(GalleryTestActivity.getAppContext(), "item값들: " + itemName + ", " + value + ", " + grade + "!");
                break;
        }
    }

//{"pm10", "pm25", "so2", "o3", "no2", "pollen", "uv", "humidity", "carwash", "laundry"};
    private void sortItemsByWhoStd(){     // WHO 기준
        switch(itemTag){
//            case "pm10":case "미세먼지":
            case "pm10":
                itemName = "미세먼지";
                if(value < 31) {
                    grade = "좋음";
                    imgId = R.drawable.ic_tiny_heart_freepik_flat;
                    bgrImgId = R.drawable.theme_bgr_good;
                    msgComp = "미세먼지 걱정 없는 맑은 날입니다";
                    msgAdv = "마스크 없이 맑은 공기를 즐기세요";
                }else if(value < 51){
                    grade = "보통";
                    imgId = R.drawable.ic_tiny_half_robinkylander_flat;
                    bgrImgId = R.drawable.theme_bgr_mediocre;
                    msgComp = "미세먼지 상태가 보통입니다";
                    msgAdv = "민감 환자군에게 마스크를 권장합니다";
                }else if(value < 101){
                    grade = "나쁨";
                    imgId = R.drawable.ic_tiny_exclamation_freepik_flat;
                    bgrImgId = R.drawable.theme_bgr_bad;
                    msgComp = "미세먼지가 심합니다.";
                    msgAdv = "외출시 마스크를 꼭 착용하세요";
                }else if(value < 151){
                    grade = "매우나쁨";
                    imgId = R.drawable.ic_tiny_radioactive_freepik_flat;
                    bgrImgId = R.drawable.theme_bgr_very_bad;
                    msgComp = "미세먼지가 매우 심각합니다.";
                    msgAdv = "가급적 외출 자제하시고 창문 닫으세요";
                }else{
                    grade = "심각";
                    imgId = R.drawable.ic_tiny_radioactive_freepik_flat;
                    bgrImgId = R.drawable.theme_bgr_fatal;
                    msgComp = "미세먼지가 매우 심각합니다.";
                    msgAdv = "가급적 외출 자제하시고 창문 닫으세요";
                }

//                L.T(GalleryTestActivity.getAppContext(), "item값들: " + itemName + ", " + value + ", " + grade + "!");
                break;
            case "pm25":
                itemName = "초미세먼지";
                if(value < 16) {
                    grade = "좋음";
                    imgId = R.drawable.ic_tiny_heart_freepik_flat;
                    bgrImgId = R.drawable.theme_bgr_good;
                    msgComp = "초미세먼지 걱정 없는 맑은 날입니다";
                    msgAdv = "마스크 없이 맑은 공기를 즐기세요";
                }else if(value < 26){
                    grade = "보통";
                    imgId = R.drawable.ic_tiny_half_robinkylander_flat;
                    bgrImgId = R.drawable.theme_bgr_mediocre;
                    msgComp = "초미세먼지 상태가 보통입니다";
                    msgAdv = "민감 환자군에게 마스크를 권장합니다";
                }else if(value < 51){
                    grade = "나쁨";
                    imgId = R.drawable.ic_tiny_exclamation_freepik_flat;
                    bgrImgId = R.drawable.theme_bgr_bad;
                    msgComp = "외출시 마스크를 필히 착용하세요";
                    msgAdv = "가급적 외출 자제하시고 창문 닫으세요";
                }else if(value < 101){
                    grade = "매우나쁨";
                    imgId = R.drawable.ic_tiny_radioactive_freepik_flat;
                    bgrImgId = R.drawable.theme_bgr_very_bad;
                    msgComp = "초미세먼지 상태가 매우 심각합니다";
                    msgAdv = "외출 자제하시고 창문 닫으세요";
                }else{
                    grade = "심각";
                    imgId = R.drawable.ic_tiny_radioactive_freepik_flat;
                    bgrImgId = R.drawable.theme_bgr_fatal;
                    msgComp = "초미세먼지가 매우 심각합니다";
                    msgAdv = "외출 자제하세요";
                }

//                L.T(GalleryTestActivity.getAppContext(), "item값들: " + itemName + ", " + value + ", " + grade + "!");
                break;

            case "so2": // 아황산가스, sulfur dioxide, so2
                itemName = "아황산가스";
                if(value < 0.021) {
                    grade = "좋음";
                    imgId = R.drawable.ic_tiny_heart_freepik_flat;
                    bgrImgId = R.drawable.theme_bgr_good;
                    msgComp = "아황산가스 걱정 없어요";
                    msgAdv = "다른 항목들도 확인해 보세요";
                }else if(value < 0.031){
                    grade = "보통";
                    imgId = R.drawable.ic_tiny_half_robinkylander_flat;
                    bgrImgId = R.drawable.theme_bgr_mediocre;
                    msgComp = "걱정 안 하셔도 좋습니다";
                    msgAdv = "호흡기 민감군은 신경써주세요";
                }else if(value < 0.051){
                    grade = "보통";
                    imgId = R.drawable.ic_tiny_half_robinkylander_flat;
                    bgrImgId = R.drawable.theme_bgr_mediocre;
                    msgComp = "인체에 약간의 영향이 올 수 있습니다";
                    msgAdv = "호흡기 민감군은 신경써주세요";
                }else if(value < 0.151){
                    grade = "나쁨";
                    imgId = R.drawable.ic_tiny_exclamation_freepik_flat;
                    bgrImgId = R.drawable.theme_bgr_bad;
                    msgComp = "식물에 피해가 올 수 있는 수준입니다";
                    msgAdv = "호흡기 질환자는 특히 유의해 주세요";
                }else if(value < 1){
                    grade = "매우나쁨";
                    imgId = R.drawable.ic_tiny_radioactive_freepik_flat;
                    bgrImgId = R.drawable.theme_bgr_very_bad;
                    msgComp = "호흡기 질환자는 외출을 금합니다.";
                    msgAdv = "유기화학물용 마스크를 착용하세요";
                }else{
                    grade = "심각";
                    imgId = R.drawable.ic_tiny_radioactive_freepik_flat;
                    bgrImgId = R.drawable.theme_bgr_fatal;
                    msgComp = "인체에 치명적 영향을 미칠 수 있습니다";
                    msgAdv = "유기화학물용 마스크를 꼭 착용하세요";
                }
                break;
            case "o3": // 오존, ozone, o3
                itemName = "오존";
                if(value < 0.031) {
                    grade = "좋음";
                    imgId = R.drawable.ic_tiny_heart_freepik_flat;
                    bgrImgId = R.drawable.theme_bgr_good;
                    msgComp = "튼튼한 오존층!";
                    msgAdv = "다른 항목도 확인해 보세요";
                }else if(value < 0.091){
                    grade = "보통";
                    imgId = R.drawable.ic_tiny_half_robinkylander_flat;
                    bgrImgId = R.drawable.theme_bgr_mediocre;
                    msgComp = "기관지 및 인후 관련 환자는 주의하세요";
                    msgAdv = "특별한 작용은 없습니다";
                }else if(value < 0.151){
                    grade = "나쁨";
                    imgId = R.drawable.ic_tiny_exclamation_freepik_flat;
                    bgrImgId = R.drawable.theme_bgr_bad;
                    msgComp = "오존은 피부, 기관지, 폐 모두 영향을";
                    msgAdv = "끼칩니다. 외출은 자제, 마스크는 필수!";
                }else if(value < 6){
                    grade = "매우나쁨";
                    imgId = R.drawable.ic_tiny_radioactive_freepik_flat;
                    bgrImgId = R.drawable.theme_bgr_very_bad;
                    msgComp = "야외활동 절대 하지마세요!";
                    msgAdv = "기관지와 피부 보호는 필수입니다";
                }else{
                    grade = "심각";
                    imgId = R.drawable.ic_tiny_radioactive_freepik_flat;
                    bgrImgId = R.drawable.theme_bgr_fatal;
                    msgComp = "야외활동 절대 하지마세요!";
                    msgAdv = "기관지와 피부 보호는 필수입니다";
                }
                break;
            case "no2": // 이산화질소, nitrogen dioxide, no2
                itemName = "이산화질소";
                if(value < 0.031) {
                    grade = "좋음";
                    imgId = R.drawable.ic_tiny_heart_freepik_flat;
                    bgrImgId = R.drawable.theme_bgr_good;
                    msgComp = "걱정하지 않아도 좋아요";
                    msgAdv = "다른 항목들도 확인해 보세요";
                }else if(value < 0.061){
                    grade = "보통";
                    imgId = R.drawable.ic_tiny_half_robinkylander_flat;
                    bgrImgId = R.drawable.theme_bgr_mediocre;
                    msgComp = "기관계 민감군은 주의하세요";
                    msgAdv = "미세먼지 없다면 환기 한 번 추천합니다";
                }else if(value < 0.201){
                    grade = "나쁨";
                    imgId = R.drawable.ic_tiny_exclamation_freepik_flat;
                    bgrImgId = R.drawable.theme_bgr_bad;
                    msgAdv = "who no2 bad";
                }else if(value < 2){
                    grade = "매우나쁨";
                    imgId = R.drawable.ic_tiny_radioactive_freepik_flat;
                    bgrImgId = R.drawable.theme_bgr_very_bad;
                    msgAdv = "who no2 very bad";
                }else{
                    grade = "심각";
                    imgId = R.drawable.ic_tiny_radioactive_freepik_flat;
                    bgrImgId = R.drawable.theme_bgr_fatal;
                    msgAdv = "who no2 fatal";
                }
                break;
            case "temp": // 체감온도
                itemName = "체감온도";
                if(value < 25 && value > 20) {
                    grade = "좋음";
                    imgId = R.drawable.ic_tiny_heart_freepik_flat;
                    bgrImgId = R.drawable.theme_bgr_good;
                    msgComp = value + "도! 선선하고 시원합니다";
                    msgAdv = "외출하기 좋은 기온입니다!";
                }else if(value < 29 && value > 18){
                    grade = "보통";
                    imgId = R.drawable.ic_tiny_half_robinkylander_flat;
                    bgrImgId = R.drawable.theme_bgr_mediocre;
                    msgComp = value + "도! 살만한 온도네요";
                    msgAdv = "아직은 외출할 만 해요!";
                }else if(value < 32 && value > 14){
                    grade = "나쁨";
                    imgId = R.drawable.ic_tiny_exclamation_freepik_flat;
                    bgrImgId = R.drawable.theme_bgr_bad;
                    msgComp = value + "도! 실내외 온도차를 고려해서";
                    msgAdv = "옷차림을 신경쓰도록 해요!";
                }else if(value < 37 || value > 5){
                    grade = "매우나쁨";
                    imgId = R.drawable.ic_tiny_radioactive_freepik_flat;
                    bgrImgId = R.drawable.theme_bgr_very_bad;
                    msgComp = value + "도! 방콕! 하고픈 온도입니다";
                    msgAdv = "중간중간 환기 잊지 마세요!";
                }else{
                    grade = "심각";
                    imgId = R.drawable.ic_tiny_radioactive_freepik_flat;
                    bgrImgId = R.drawable.theme_bgr_fatal;
                    msgComp = value + "도! 으윽...도저히 버틸 수 없습니다";
                    msgAdv = "가급적 외출을 자제하세요";
                }
                break;
            case "uv": // 자외선
                itemName = "자외선";
                // 자외선지수는 WMO/WHO 등 국제기구 등에서 제안하는 “Global Solar UV Index”의 가이드라인을 활용함
                if(value < 21.0) {
                    grade = "좋음";
                    imgId = R.drawable.ic_tiny_heart_freepik_flat;
                    bgrImgId = R.drawable.theme_bgr_good;
                    msgComp = "자외선 걱정할 필요 없어요!";
                    msgAdv = "선크림 바르지 않아도 괜찮습니다";
                }else if(value < 51){
                    grade = "보통";
                    imgId = R.drawable.ic_tiny_half_robinkylander_flat;
                    bgrImgId = R.drawable.theme_bgr_mediocre;
                    msgComp = "선크림이 필수는 아니지만";
                    msgAdv = "민감성 피부라면 꼭 선크림 바르세요";
                }else if(value < 71){
                    grade = "나쁨";
                    imgId = R.drawable.ic_tiny_exclamation_freepik_flat;
                    bgrImgId = R.drawable.theme_bgr_bad;
                    msgComp = "자외선 지수가 많이 높습니다";
                    msgAdv = "선크림 필히 바르세요";
                }else if(value < 101){
                    grade = "매우나쁨";
                    imgId = R.drawable.ic_tiny_radioactive_freepik_flat;
                    bgrImgId = R.drawable.theme_bgr_very_bad;
                    msgComp = "가급적 야외활동 자제하시고";
                    msgAdv = "실내에서도 선크림을 발라주세요";
                }else{
                    grade = "심각";
                    imgId = R.drawable.ic_tiny_radioactive_freepik_flat;
                    bgrImgId = R.drawable.theme_bgr_fatal;
                    msgComp = "가급적 야외활동 자제하시고";
                    msgAdv = "실내에서도 선크림을 발라주세요";
                }
                break;
            case "humidity": // 불쾌지수
                itemName = "불쾌지수";

                if(value < 21) {
                    grade = "좋음";
                    imgId = R.drawable.ic_tiny_heart_freepik_flat;
                    bgrImgId = R.drawable.theme_bgr_good;
                    msgComp = "단 1g도 습기차지 않습니다!";
                    msgAdv = "걱정 말고 야외활동을 즐겨요~";
                }else if(value < 51){
                    grade = "보통";
                    imgId = R.drawable.ic_tiny_half_robinkylander_flat;
                    bgrImgId = R.drawable.theme_bgr_mediocre;
                    msgComp = "습기문제 없어요~";
                    msgAdv = "체감온도 한 번 확인하고 외출해요";
                }else if(value < 81){
                    grade = "나쁨";
                    imgId = R.drawable.ic_tiny_exclamation_freepik_flat;
                    bgrImgId = R.drawable.theme_bgr_bad;
                    msgComp = "습기찬 하루";
                    msgAdv = "습기제거제가 필요합니다";
                }else if(value < 101){
                    grade = "매우나쁨";
                    imgId = R.drawable.ic_tiny_radioactive_freepik_flat;
                    bgrImgId = R.drawable.theme_bgr_very_bad;
                    msgComp = "불쾌! 불쾌! 불쾌!";
                    msgAdv = "실내라면 보일러 한 번 틀어주세요";
                }else{
                    grade = "심각";
                    imgId = R.drawable.ic_tiny_radioactive_freepik_flat;
                    bgrImgId = R.drawable.theme_bgr_fatal;
                    msgComp = "불쾌! 불쾌! 불쾌!";
                    msgAdv = "실내라면 보일러 한 번 틀어주세요";
                }
                break;
            case "carwash": // 세차지수
                itemName = "세차지수";
                if(value > 70) {
                    grade = "좋음";
                    imgId = R.drawable.ic_tiny_heart_freepik_flat;
                    bgrImgId = R.drawable.theme_bgr_good;
                    msgComp = "세차하기 정말 좋은 날씨네요";
                    msgAdv = "기다리지 말고 지금 당장 GO! GO!";
                }else if(value > 50){
                    grade = "보통";
                    imgId = R.drawable.ic_tiny_half_robinkylander_flat;
                    bgrImgId = R.drawable.theme_bgr_mediocre;
                    msgComp = "그럭저럭 세차할 만한 날입니다";
                    msgAdv = "어둑해지기 전에 서두릅시다";
                }else if(value > 20){
                    grade = "나쁨";
                    imgId = R.drawable.ic_tiny_exclamation_freepik_flat;
                    bgrImgId = R.drawable.theme_bgr_bad;
                    msgComp = "오늘은 영 날이 아니예요";
                    msgAdv = "급하지 않다면 다음으로 미루세요";
                }else if(value > -1){
                    grade = "매우나쁨";
                    imgId = R.drawable.ic_tiny_radioactive_freepik_flat;
                    bgrImgId = R.drawable.theme_bgr_very_bad;
                    msgComp = "절대! 네버! 오늘은 안돼요";
                    msgAdv = "아무리 급해도 오늘만은 참으세요";
                }else{
                    grade = "심각";
                    imgId = R.drawable.ic_tiny_radioactive_freepik_flat;
                    bgrImgId = R.drawable.theme_bgr_fatal;
                    msgComp = "절대! 네버! 오늘은 안돼요";
                    msgAdv = "아무리 급해도 오늘만은 참으세요";
                }
                break;
            case "laundry": // 빨래지수
                itemName = "빨래지수";
                if(value > 70) {
                    grade = "좋음";
                    imgId = R.drawable.ic_tiny_heart_freepik_flat;
                    bgrImgId = R.drawable.theme_bgr_good;
                    msgComp = "빨래하기 정말 좋은 날씨네요";
                    msgAdv = "뽀송뽀송 빨래하러 먼저 갈게요~";
                }else if(value > 50){
                    grade = "보통";
                    imgId = R.drawable.ic_tiny_half_robinkylander_flat;
                    bgrImgId = R.drawable.theme_bgr_mediocre;
                    msgComp = "그럭저럭 빨래하기 나쁘지 않아요";
                    msgAdv = "어둑해지기 전에 서두릅시다";
                }else if(value > 20){
                    grade = "나쁨";
                    imgId = R.drawable.ic_tiny_exclamation_freepik_flat;
                    bgrImgId = R.drawable.theme_bgr_bad;
                    msgComp = "오늘은 영 날이 아니예요";
                    msgAdv = "급하지 않다면 다음으로 미루세요";
                }else if(value > -1){
                    grade = "매우나쁨";
                    imgId = R.drawable.ic_tiny_radioactive_freepik_flat;
                    bgrImgId = R.drawable.theme_bgr_very_bad;
                    msgComp = "절대! 네버! 오늘은 안돼요";
                    msgAdv = "아무리 급해도 오늘만은 참으세요";
                }else{
                    grade = "심각";
                    imgId = R.drawable.ic_tiny_radioactive_freepik_flat;
                    bgrImgId = R.drawable.theme_bgr_fatal;
                    msgComp = "절대! 네버! 오늘은 안돼요";
                    msgAdv = "아무리 급해도 오늘만은 참으세요";
                }
                break;
            default:
                itemName = "기타";
                if(value < 30) {
                    grade = "좋음";
                    imgId = R.drawable.ic_tiny_heart_freepik_flat;
                    bgrImgId = R.drawable.theme_bgr_good;
                    msgAdv = "who default good";
                }else if(value < 50){
                    grade = "보통";
                    imgId = R.drawable.ic_tiny_half_robinkylander_flat;
                    bgrImgId = R.drawable.theme_bgr_mediocre;
                    msgAdv = "who default soso";
                }else if(value < 76){
                    grade = "나쁨";
                    imgId = R.drawable.ic_tiny_exclamation_freepik_flat;
                    bgrImgId = R.drawable.theme_bgr_bad;
                    msgAdv = "who default bad";
                }else if(value <= 90){
                    grade = "매우나쁨";
                    imgId = R.drawable.ic_tiny_radioactive_freepik_flat;
                    bgrImgId = R.drawable.theme_bgr_very_bad;
                    msgAdv = "who default very bad";
                }else{
                    grade = "심각";
                    imgId = R.drawable.ic_tiny_radioactive_freepik_flat;
                    bgrImgId = R.drawable.theme_bgr_fatal;
                    msgAdv = "who default fatal";
                }

//                L.T(GalleryTestActivity.getAppContext(), "item값들: " + itemName + ", " + value + ", " + grade + "!");
                break;
        }
    }

    public void setImgAndBgrImg(){
        switch(grade){
            case "좋음":
                imgId = R.drawable.ic_mood_bad;
                bgrImgId = R.drawable.theme_bgr_good;
//                innerIndColor = R.color.goodIndicator;
                break;
            case "보통":
                imgId = R.drawable.ic_mood_bad;
                bgrImgId = R.drawable.theme_bgr_mediocre;
//                innerIndColor = R.color.mediocreIndicator;
                break;
            case "나쁨":
                imgId = R.drawable.ic_mood_bad;
                bgrImgId = R.drawable.theme_bgr_bad;
//                innerIndColor = R.color.badIndicatorItemColor;
                break;
            case "매우나쁨":
                imgId = R.drawable.ic_mood_bad;
                bgrImgId = R.drawable.theme_bgr_very_bad;
//                innerIndColor = R.color.veryBadIndicator;
                break;
            case "심각":
                imgId = R.drawable.ic_mood_bad;
                bgrImgId = R.drawable.theme_bgr_fatal;
//                innerIndColor = R.color.fatalIndicator;
                break;
        }
    }


    public String getItemName(){return itemName;}
    public void setItemName(String itemName){this.itemName = itemName;}
    public String getGrade(){return grade;}
    public void setGrade(String grade){this.grade = grade;}
    public double getValue(){return value;}
    public void setValue(float value){this.value = value;}
    public int getImgId(){return imgId;}
    public void setImgId(int imgId){this.imgId = imgId;}
    public int getBgrImgId(){return bgrImgId;}
    public void setBgrImgId(int bgrImgId){this.bgrImgId = bgrImgId;}
}
