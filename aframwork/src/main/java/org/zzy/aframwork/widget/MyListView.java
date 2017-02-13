package org.zzy.aframwork.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ListView;

public class MyListView extends ListView {
    private static final String TAG = "MyListView";
    private boolean MMIEnable = true;
    private boolean isOnMeasure;

	public MyListView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public MyListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public MyListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}
	public void setMMIEnable(boolean MMIEnable){
		this.MMIEnable = MMIEnable;
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent event) {
		if(!MMIEnable)//消耗掉事件
			return true;
		return super.dispatchTouchEvent(event);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// 根据模式计算每个child的高度和宽度
        isOnMeasure = true;
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec,  expandSpec);
	}
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        isOnMeasure = false;
        super.onLayout(changed, l, t, r, b);
    }
    public boolean isOnMeasure(){
        return isOnMeasure;
    }

    public boolean isReachBottomEdge() {
        boolean result=false;
        if (getLastVisiblePosition() == (getCount() - 1)) {
            final View bottomChildView = getChildAt(getLastVisiblePosition() - getFirstVisiblePosition());
            result= (getHeight()>=bottomChildView.getBottom());
        };
        return  result;
    }
}