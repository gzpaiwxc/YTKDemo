package com.colin.tiankong.fragment;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.colin.tiankong.R;
import com.colin.tiankong.activity.GSWMXActivity;
import com.colin.tiankong.customview.MTextView;
import com.colin.tiankong.customview.MyEditText;
import com.colin.tiankong.entity.ChooseItem;
import com.colin.tiankong.entity.ResultEntity;
import com.colin.tiankong.utils.event.EventUpdateAnswer;
import com.colin.tiankong.utils.event.EventUpdateResult;
import com.colin.tiankong.utils.RxBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Colin.Zhang on 2017/3/29.
 */

public class ChildFragment extends BaseFragment {
    public ChildFragment() {
    }

    public static ChildFragment getInstance(ChooseItem chooseItem) {
        ChildFragment cf = new ChildFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable("item", chooseItem);
        cf.setArguments(bundle);
        return cf;
    }

    private final String TAG = "lianshou";

    private List<EditText> editTexts = new ArrayList<>();
    private List<LinearLayout> linearLayouts = new ArrayList<>();
    private ChooseItem chooseItem;
    private MTextView mTextView;
    private RelativeLayout relativeLayout;
    private View loadingView;
    private TextView tvInd;
    private EditText editText;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.frag_child, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mTextView = (MTextView) view.findViewById(R.id.mtextview);
        relativeLayout = (RelativeLayout) view.findViewById(R.id.rlParent);
        chooseItem = getArguments().getParcelable("item");
        tvInd = (TextView) view.findViewById(R.id.tvInd);
        loadingView = view.findViewById(R.id.loadingView);
        tvInd.setText((chooseItem.index + 1) + "/" + chooseItem.total);
        ((GSWMXActivity) this.getActivity()).registerMyTouchListener(myTouchListener);
    }

    public void hideLoading() {
        loadingView.setVisibility(View.GONE);
    }

    @Override
    public void lazyLoadData() {
        super.lazyLoadData();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                initTextContent();
            }
        }, 50);
    }

    private void initTextContent() {
        String source = chooseItem.body;
        SpannableString ss = new SpannableString(source);

        String str = "Draw your _____ please, Li Jing.";

        List<Integer> iLists = new ArrayList<>();
        for (int j = 0; j < source.length() - 1; j++) {
            if (ss.charAt(j) == '_') {
                iLists.add(j);
            }
        }
        for (int i = 0; i < source.length() - 1; i++) {

            int num = iLists.size()/5;
            if (iLists.size() != 0 && iLists != null) {
                for (int k=1;k<=num;k++) {
                    if (i == iLists.get(k*5-5)) {
                        ImageSpan is = new ImageSpan(getActivity(), R.drawable.testbg_null);
                        ss.setSpan(is, i, i + 5, 0);
                    }
                }
            }

            if (ss.charAt(i) == '*') {
                ImageSpan is = new ImageSpan(getActivity(), R.drawable.testbg_null);
                ss.setSpan(is, i, i + 6, 0);
            }
        }
        iLists.clear();
        mTextView.setMText(ss);
        mTextView.setTextSize(16);
        mTextView.setTextColor(Color.BLACK);
        mTextView.invalidate();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
//                addView();
                addTextView();
                hideLoading();
            }
        }, 200);
    }

    private void addView() {
        Map<Integer, List<Integer>> map = mTextView.getListMap();
        Log.e("lianshou", map.toString());
        for (int i = 0; i < map.size(); i++) {
            List<Integer> list = map.get(i);
            editText = new EditText(getActivity());
            editText.setBackgroundResource(R.drawable.btn_white_round_10);
            editText.setTextColor(getResources().getColor(R.color.flag_color));

            if (chooseItem.answer != null && chooseItem.answer.size() > 0
                    && !TextUtils.isEmpty(chooseItem.answer.get(i))
                    && !TextUtils.isEmpty(chooseItem.answer.get(i).trim())) {
                editText.setText(chooseItem.answer.get(i));
            }
            editText.setSingleLine();
            editText.setGravity(Gravity.CENTER);
            editText.setTag(i);
            editText.setTextSize(16);
            final int finalI = i;
            setTextChangeListener(editText, finalI);
            RelativeLayout.LayoutParams rl = new RelativeLayout.LayoutParams(getResources()
                    .getDimensionPixelSize(R.dimen.box_width), getResources()
                    .getDimensionPixelSize(R.dimen.box_height));
            Log.d("lianshou", "list.get(0)==>"+list.get(0)+"list.get(1)==>"+list.get(1));
            rl.leftMargin = list.get(0);
            rl.topMargin = list.get(1);
            editTexts.add(editText);
            relativeLayout.addView(editText, rl);
        }
    }

    private void addTextView() {
        Map<Integer, List<Integer>> map = mTextView.getListMap();
        for (int i = 0; i < map.size(); i++) {
            List<Integer> list = map.get(i);

            tvOption = addText(getActivity());
            tvOption.setText((i+1)+"");
            myEditText = addEditText(getActivity());
//            myEditText.setTextColor(getResources().getColor(R.color.flag_color));

            if (chooseItem.answer != null && chooseItem.answer.size() > 0
                    && !TextUtils.isEmpty(chooseItem.answer.get(i))
                    && !TextUtils.isEmpty(chooseItem.answer.get(i).trim())) {
                myEditText.setText(chooseItem.answer.get(i));
            }
            myEditText.setSingleLine();
            myEditText.setGravity(Gravity.CENTER);
            myEditText.setTag(i);
            myEditText.setTextSize(16);
            final int finalI = i;
            setTextChangeListener(myEditText, finalI);
            RelativeLayout.LayoutParams rl = new RelativeLayout.LayoutParams(getResources()
                    .getDimensionPixelSize(R.dimen.box_width), getResources()
                    .getDimensionPixelSize(R.dimen.box_height));
            if (map.size() > 1) {
                linearLayout = addLinearLayout(getActivity(), tvOption, myEditText);
            }else{
                linearLayout = addLinearLayout(getActivity(), null, myEditText);
            }
            rl.leftMargin = list.get(0);
            rl.topMargin = list.get(1);
            linearLayouts.add(linearLayout);
            relativeLayout.addView(linearLayout, rl);
        }
    }

    private LinearLayout linearLayout;
    private TextView tvOption;
    private MyEditText myEditText;

    /**构建一个TextView*/
    private TextView addText(Context context) {
        TextView tvOption = new TextView(context);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.gravity = Gravity.CENTER;
        lp.rightMargin = 10;
        tvOption.setText("1");
        tvOption.setTextColor(Color.WHITE);
        tvOption.setWidth((int) getResources().getDimension(R.dimen.tvoption_width));
        tvOption.setHeight((int) getResources().getDimension(R.dimen.tvoption_height));
        tvOption.setBackgroundResource(R.drawable.choosed_bg);
        tvOption.setGravity(Gravity.CENTER);
        tvOption.setLayoutParams(lp);
        return tvOption;
    }

    /**构建一个EditText*/
    private MyEditText addEditText(Context context) {
        MyEditText myEditText = new MyEditText(context);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.gravity = Gravity.CENTER;
        myEditText.setBackgroundResource(R.drawable.btn_white_round_10);
        myEditText.setTextColor(Color.WHITE);
        myEditText.setWidth((int) getResources().getDimension(R.dimen.edittext_width));
        myEditText.setHeight((int) getResources().getDimension(R.dimen.edittext_height));
        myEditText.setGravity(Gravity.CENTER);
        myEditText.setTextSize(16);
        myEditText.setLayoutParams(lp);
        return myEditText;
    }

    private LinearLayout addLinearLayout(Context context,TextView textView,MyEditText myEditText) {
        LinearLayout linearLayout = new LinearLayout(context);
        linearLayout.setOrientation(LinearLayout.HORIZONTAL);
        if (textView == null) {
            linearLayout.addView(myEditText);
            return linearLayout;
        } else {
            linearLayout.addView(textView);
            linearLayout.addView(myEditText);
            return linearLayout;
        }
    }



    private void setTextChangeListener(final EditText editText, final int finalI) {
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (chooseItem.answer == null) {
                    chooseItem.answer = new HashMap<>();
                }
                chooseItem.answer.put(finalI, s.toString());
                RxBus.getDefault().post(new EventUpdateAnswer(chooseItem));
                ResultEntity resultEntity = new ResultEntity();
                resultEntity.index = chooseItem.index;
                resultEntity.isAnswer = s.length() > 0;
                RxBus.getDefault().post(new EventUpdateResult(resultEntity));
            }

            @Override
            public void afterTextChanged(Editable s) {
                editText.setTextColor(getResources().getColor(R.color.white));

            }
        });
    }

    GSWMXActivity.MyTouchListener myTouchListener = new GSWMXActivity.MyTouchListener() {
        @Override
        public void onTouchEvent(MotionEvent event) {
            relativeLayout.setFocusable(true);
            relativeLayout.setFocusableInTouchMode(true);
            relativeLayout.requestFocus();
            hideInputMethod();
            //            if (event.getAction() == MotionEvent.ACTION_DOWN) {
            //                View view = getActivity().getCurrentFocus();
            //                if (view != null && view instanceof EditText) {
            //                    Rect r = new Rect();
            //                    view.getGlobalVisibleRect(r);
            //                    int rawX = (int) event.getRawX();
            //                    int rawY = (int) event.getRawY();
            //
            //                    // 判断点击的点是否落在当前焦点所在的 view 上；
            //                    if (!r.contains(rawX, rawY)) {
            //                        view.clearFocus();
            //                    }
            //                }
            //            }
        }
    };

    public void hideInputMethod() {
        View view = getActivity().getCurrentFocus();
        if (view == null)
            return;
        ((InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE))
                .hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ((GSWMXActivity) this.getActivity()).unRegisterMyTouchListener(myTouchListener);
    }
}
