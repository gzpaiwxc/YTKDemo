package com.colin.tiankong.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.colin.tiankong.R;
import com.colin.tiankong.adapter.HomePagerAdapter;
import com.colin.tiankong.entity.ChooseItem;
import com.colin.tiankong.utils.event.EventShowResult;
import com.colin.tiankong.utils.event.EventUpdateAnswer;
import com.colin.tiankong.utils.RxBus;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import rx.functions.Action1;
import rx.subscriptions.CompositeSubscription;

public class GSWMXActivity extends AppCompatActivity {
    public String[] bodys = {"Draw your _____ please, Li Jing.",
            "It is a _____. It has long _____.",
            "It is a _____. It has long _____.It is a _____. It has long _____.It is a _____. It has long _____.",
            "（2016南昌一模）万里悲秋常作客，*input。（杜甫《登高》）"
            , "（2016南昌一模）别有幽愁暗恨生，*input。（白居易《琵琶行》）"
            , "（2016南昌一模）想当年，金戈铁马，*input。（辛弃疾《京口贝古亭怀古》）"
            , "（2016南昌一模）*input,往来无白丁。（刘禹锡《》陋室铭）"
            , "（2016南昌一模）*input，而神明自得，圣心备焉（荀子《劝学》）"
            , "（2016南昌一模）《出师表》中，诸葛亮回顾自己当初临危受命的两句是*input，*input。"
    };
    private ViewPager viewPager;
    private HomePagerAdapter adapter;
    private CompositeSubscription subscriptions;
    private Map<Integer, ChooseItem> dataMap;

    public static void actionLaunch(Context context) {
        context.startActivity(new Intent(context, GSWMXActivity.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_gsw);
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        dataMap = new HashMap<>();
        for (int i = 0; i < bodys.length + 1; i++) {
            if (i == bodys.length) {
                dataMap.put(i, null);
                continue;
            }
            ChooseItem ci = new ChooseItem();
            ci.body = bodys[i];
            ci.index = i;
            ci.total = bodys.length;
            dataMap.put(i, ci);
        }
        adapter = new HomePagerAdapter(getSupportFragmentManager(), dataMap);
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(0);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                hideInputMethod();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        registerObserver();
    }

    public void hideInputMethod() {
        View view = getCurrentFocus();
        if (view == null) return;
        ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE))
                .hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

    private void registerObserver() {
        subscriptions = new CompositeSubscription();
        subscriptions.add(RxBus.getDefault().toObserverable(EventUpdateAnswer.class)
                .subscribe(new Action1<EventUpdateAnswer>() {
                    @Override
                    public void call(EventUpdateAnswer eventUpdateAnswer) {
                        dataMap.put(eventUpdateAnswer.chooseItem.index, eventUpdateAnswer.chooseItem);
                    }
                }));
        subscriptions.add(RxBus.getDefault().toObserverable(EventShowResult.class)
                .subscribe(new Action1<EventShowResult>() {
                    @Override
                    public void call(EventShowResult eventUpdateCurrentItem) {
                        viewPager.setCurrentItem(eventUpdateCurrentItem.index);
                    }
                }));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (subscriptions != null && !subscriptions.isUnsubscribed()) {
            subscriptions.unsubscribe();
        }
    }


    public interface MyTouchListener {
        public void onTouchEvent(MotionEvent event);
    }

    /**
     * 保存MyTouchListener接口的列表
     */
    private ArrayList<MyTouchListener> myTouchListeners = new ArrayList<>();

    /**
     * 提供给Fragment通过getActivity()方法来注册自己的触摸事件的方法
     * @param touchListener
     */
    public void registerMyTouchListener(MyTouchListener touchListener) {
        myTouchListeners.add(touchListener);
    }

    /**
     * 提供给Fragment通过getActivity()方法来取消注册自己的触摸事件的方法
     * @param touchListener
     */
    public void unRegisterMyTouchListener(MyTouchListener touchListener) {
        myTouchListeners.remove(touchListener);
    }

    /**
     * 分发触摸事件给所有注册了MyTouchListener的接口
     * @param ev
     * @return
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        for (MyTouchListener listener : myTouchListeners) {
            listener.onTouchEvent(ev);
        }
        return super.dispatchTouchEvent(ev);
    }



}
