package com.colin.tiankong.activity;

import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.colin.tiankong.R;

/**
 * Created by Colin.Zhang on 2017/3/30.
 */

public class MainActivity extends FragmentActivity {
    private EditText editText;
    private LinearLayout layout;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_main);
        layout = (LinearLayout) findViewById(R.id.layout);
        findViewById(R.id.tvReadTest).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ReadTestActivity.actionLaunch(MainActivity.this);
            }
        });
        findViewById(R.id.tvGSWMX).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GSWMXActivity.actionLaunch(MainActivity.this);
            }
        });
        findViewById(R.id.tvWXTK).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WXTKActivity.actionLaunch(MainActivity.this);
            }
        });

        editText = (EditText) findViewById(R.id.edit_text);
        editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    v.setBackgroundResource(R.drawable.btn_aqua_round_10);
                } else {
                    v.setBackgroundResource(R.drawable.btn_white_round_10);
                }
            }
        });

        hideInputMethod();
    }
    //    // 点击屏幕其他地方，使 etName 失去焦点（EditText）
    //    @Override
    //    public boolean dispatchTouchEvent(MotionEvent ev) {
    //        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
    //            // 获取当前焦点所在的控件；
    //            View view = getCurrentFocus();
    //            if (view != null && view instanceof EditText) {
    //                Rect r = new Rect();
    //                view.getGlobalVisibleRect(r);
    //                int rawX = (int) ev.getRawX();
    //                int rawY = (int) ev.getRawY();
    //
    //                // 判断点击的点是否落在当前焦点所在的 view 上；
    //                if (!r.contains(rawX, rawY)) {
    //                    view.clearFocus();
    //                }
    //            }
    //        }
    //        return super.dispatchTouchEvent(ev);
    //    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        layout.setFocusable(true);
        layout.setFocusableInTouchMode(true);
        layout.requestFocus();
        return super.onTouchEvent(event);
    }

    public void hideInputMethod() {
        View view = getCurrentFocus();
        if (view == null)
            return;
        ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE))
                .hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
