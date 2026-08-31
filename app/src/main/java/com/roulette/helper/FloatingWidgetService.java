package com.roulette.helper;

import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.IBinder;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import java.util.*;

public class FloatingWidgetService extends Service {
    private WindowManager mWindowManager;
    private View mFloatingWidget;

    @Override
    public IBinder onBind(Intent intent) { return null; }

    @Override
    public void onCreate() {
        super.onCreate();
        mFloatingWidget = LayoutInflater.from(this).inflate(R.layout.layout_floating_widget, null);

        int layoutType = (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
                ? WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
                : WindowManager.LayoutParams.TYPE_PHONE;

        final WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                layoutType,
                WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL,
                PixelFormat.TRANSLUCENT);

        params.gravity = Gravity.TOP | Gravity.START;
        params.x = 50;
        params.y = 100;

        mWindowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        mWindowManager.addView(mFloatingWidget, params);

        EditText etBmw = mFloatingWidget.findViewById(R.id.inputBMW);
        EditText etMb = mFloatingWidget.findViewById(R.id.inputMB);
        EditText etJag = mFloatingWidget.findViewById(R.id.inputJag);
        EditText etLr = mFloatingWidget.findViewById(R.id.inputLR);
        Button btnCalc = mFloatingWidget.findViewById(R.id.btnCalculate);
        TextView txtRes = mFloatingWidget.findViewById(R.id.txtResult);

        btnCalc.setOnClickListener(v -> {
            Map<String, Integer> bets = new HashMap<>();
            bets.put("BMW", parseAmount(etBmw.getText().toString()));
            bets.put("Mercedes", parseAmount(etMb.getText().toString()));
            bets.put("Jaguar", parseAmount(etJag.getText().toString()));
            bets.put("Land Rover", parseAmount(etLr.getText().toString()));

            List<Map.Entry<String, Integer>> list = new ArrayList<>(bets.entrySet());
            list.sort(Map.Entry.comparingByValue());

            String res = "Target 2nd: " + list.get(1).getKey() + " (" + list.get(1).getValue() + ")\n"
                       + "Target 3rd: " + list.get(2).getKey() + " (" + list.get(2).getValue() + ")";
            txtRes.setText(res);
        });
    }

    private int parseAmount(String str) {
        try { return Integer.parseInt(str.trim()); } 
        catch (Exception e) { return 0; }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mFloatingWidget != null) mWindowManager.removeView(mFloatingWidget);
    }
                                                   }
