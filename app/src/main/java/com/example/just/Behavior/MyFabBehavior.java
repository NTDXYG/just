package com.example.just.Behavior;

import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Administrator on 2018/6/4.
 */

public class MyFabBehavior extends FloatingActionButton.Behavior {

    // 必须重写两个参数的构造方法，否则会报错
    public MyFabBehavior (Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    // 返回true时表示传递滑动参数，同时执行后面的滑动监听，返回false的话后面的onNestedScroll等方法就不会调用了
    // super是直接返回的false

    @Override
    public boolean onStartNestedScroll(CoordinatorLayout coordinatorLayout, FloatingActionButton child, View directTargetChild, View target, int nestedScrollAxes) {
        // 判断如果是垂直滚动则返回true
        return nestedScrollAxes == ViewCompat.SCROLL_AXIS_VERTICAL;
    }

    @Override
    public void onNestedScroll(CoordinatorLayout coordinatorLayout, FloatingActionButton child, View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {
        // 可以直接删掉super 进去之后发现super里面啥也没做
        // super.onNestedScroll(coordinatorLayout, child, target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed);
        System.out.println("dyConsumed: " + dyConsumed);

        // 如果向上滑动 则隐藏
        if (dyConsumed > 0 && child.getVisibility() == View.VISIBLE) {
            child.setVisibility(View.INVISIBLE);
        } else if (dyConsumed < 0  && child.getVisibility() != View.VISIBLE) {
            // 向下则显示
            child.setVisibility(View.VISIBLE);
            child.show();
        }
    }
}
