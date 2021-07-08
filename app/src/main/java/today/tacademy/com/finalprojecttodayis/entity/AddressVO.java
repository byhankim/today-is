package today.tacademy.com.finalprojecttodayis.entity;

/**
 * Created by Tacademy on 2017-11-01.
 */

public class AddressVO {
    public String adminArea;    // 시/도
    public String locality;     // 구/군
    public String thoroughfare; // 행정동
    public String legalFare;    // 법정동
    public boolean isUnavailable;   // 에러시 처리용 boolean

    {
        isUnavailable = false;
    }
}
