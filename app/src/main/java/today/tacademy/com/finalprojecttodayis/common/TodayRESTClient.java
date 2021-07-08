package today.tacademy.com.finalprojecttodayis.common;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import today.tacademy.com.finalprojecttodayis.common.jsonentity.CarwashVO;
import today.tacademy.com.finalprojecttodayis.common.jsonentity.DiscomfortHumidVO;
import today.tacademy.com.finalprojecttodayis.common.jsonentity.EffectiveTempVO;
import today.tacademy.com.finalprojecttodayis.common.jsonentity.LaundryVO;
import today.tacademy.com.finalprojecttodayis.common.jsonentity.UVRaysVO;

/**
 * Created by Tacademy on 2017-11-06.
 */

public interface TodayRESTClient {
    @GET("/weather/windex/wctindex")
    Call<EffectiveTempVO> requestEffectiveTemp(
            @Query("lat") String latitude, @Query("lon") String longitude, @Query("version") String apiVersion);
    @GET("/weather/windex/uvindex")
    Call<UVRaysVO> requestUV(
            @Query("lat") String latitude, @Query("lon") String longitude, @Query("version") String apiVersion);
    @GET("/weather/windex/thindex")
    Call<DiscomfortHumidVO> requestHumidity(
            @Query("lat") String latitude, @Query("lon") String longitude, @Query("version") String apiVersion);
    @GET("/weather/windex/laundry")
    Call<LaundryVO> requestLaundry (
            @Query("lat") String latitude, @Query("lon") String longitude, @Query("version") String apiVersion);
    @GET("/weather/windex/carwash")
    Call<CarwashVO> requestCarwash (
            @Query("version") String apiVersion);
}
