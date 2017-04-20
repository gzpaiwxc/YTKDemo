package com.colin.tiankong.customview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.colin.tiankong.R;

/**
 * Created by zhuqizhi on 2017/4/19.
 */
public class MyEditText extends EditText {

    // 当前输入的字符数
    private int presentCount;

    public MyEditText(Context context) {
        super(context);
        initListener();
    }
    public MyEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        initListener();
    }
    public MyEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initListener();
    }

    private void initListener() {

        /// 设置edittetx内容改变监听器
        addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                Log.d("lianshou", "beforeTextChanged s==>" + s +"  s.length==>"+s.length()+ "  start==>" + start + "  count==>" + count + "  after==>" + after);
                if (s != null && count != 0) {
                    setBackgroundResource(R.drawable.btn_blue_round_10);
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                Log.d("lianshou", "onTextChanged s==>" + s + "  start==>" + start + "  before==>" + before + "  count==>" + count);
            }

            @Override
            public void afterTextChanged(Editable s) {
                // 当内容改变后，计数、并判断是否超过规定字符长度
                presentCount = s.length();
                // 当内容长度为0并获得焦点时：
                if(presentCount==0&&isFocused()){
                    // 当内容为空时，背景浅蓝色
//                    setBackgroundColor(Color.parseColor("#78DAFF"));
                    setBackgroundResource(R.drawable.btn_aqua_round_10);
                }else if(presentCount!=0&&isFocused()){
                    // 当内容长度不为0时，背景为深蓝色
//                    setBackgroundColor(Color.parseColor("#25AEF2"));
                    setBackgroundResource(R.drawable.btn_blue_round_10);
                }
            }
        });

        // 添加焦点的获取通知
        setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                // 获取焦点时
                if(hasFocus) {
                    if(presentCount!=0){
                        //获取焦点，且内容不为空时背景颜色为深蓝色
                        setBackgroundResource(R.drawable.btn_blue_round_10);
                    }else{
                        //获取焦点，且内容为空时背景为浅蓝色
//                        setBackgroundColor(Color.parseColor("#78DAFF"));
                        setBackgroundResource(R.drawable.btn_aqua_round_10);
                    }
                }else {
                    if(presentCount!=0){
                        //失去焦点，且内容不为空时背景颜色为深蓝色
//                        setBackgroundColor(Color.parseColor("#25AEF2"));
                        setBackgroundResource(R.drawable.btn_blue_round_10);
                    }else{
                        // 没有获取焦点时，背景颜色为白色
//                        setBackgroundColor(Color.parseColor("#ffffff"));
                        setBackgroundResource(R.drawable.btn_white_round_10);
                    }

                }

            }
        });
    }

}
