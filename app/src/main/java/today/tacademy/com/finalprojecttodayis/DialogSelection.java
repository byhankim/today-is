package today.tacademy.com.finalprojecttodayis;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

import today.tacademy.com.finalprojecttodayis.common.L;

/**
 * Created by Tacademy on 2017-11-14.
 */

public class DialogSelection extends DialogFragment {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final String[] items = {"미세먼지", "초미세먼지", "아황산가스", "오존", "이산화질소",
        "체감온도", "자외선", "불쾌지수", "세차지수", "빨래지수"};
        final String[] itemsTags ={"pm10", "pm25", "so2", "o3", "no2",
        "temp", "uv", "humidity", "carwash", "laundry"};

        AlertDialog.Builder builder =
                new AlertDialog.Builder(getActivity());

        builder.setTitle("원하는 항목을 선택하세요")
            .setMultiChoiceItems(items, null, new DialogInterface.OnMultiChoiceClickListener(){
                @Override
                public void onClick(DialogInterface dialogInterface, int i, boolean b) {
                    L.Log("dialog", "option selected: " + items[i]);
                }
            });

        return builder.create();
    }
}
