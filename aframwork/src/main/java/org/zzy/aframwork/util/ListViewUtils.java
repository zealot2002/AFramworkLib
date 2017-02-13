package org.zzy.aframwork.util;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;


public class ListViewUtils {

	//解决listView 只显示一行的问题
	public static void fixListViewHeight(ListView listView) {   
        // 如果没有设置数据适配器，则ListView没有子项，返回。  
        ListAdapter listAdapter = listView.getAdapter();
        int totalHeight = 0;   
        if (listAdapter == null) {   
            return;   
        }   
        for (int i = 0, len = listAdapter.getCount(); i < len; i++) {     
            View listViewItem = listAdapter.getView(i , null, listView);  
            // 计算子项View 的宽高   
            //4.2.2 版本以前，对于item的layout，只能是LinerLayout，否则报空指针异常；4.4.x版本已经修复此问题
            listViewItem.measure(0, 0); 
            // 计算所有子项的高度和
            totalHeight += listViewItem.getMeasuredHeight();    
        }   
   
        ViewGroup.LayoutParams params = listView.getLayoutParams();   
        // listView.getDividerHeight()获取子项间分隔符的高度   
        // params.height设置ListView完全显示需要的高度    
        params.height = totalHeight+ (listView.getDividerHeight() * (listAdapter.getCount() - 1));   
        listView.setLayoutParams(params);   
    }
    public static boolean isBottom(ListView listView){
        boolean result=false;
        if (listView.getLastVisiblePosition() == (listView.getCount() - 1)) {
            final View bottomChildView = listView.getChildAt(listView.getLastVisiblePosition() - listView.getFirstVisiblePosition());
            result= (listView.getHeight()>=bottomChildView.getBottom());
        };
        return  result;
    }
    public static int getScrollY(ListView listView,int headerHeight) {
        View c = listView.getChildAt(0);
        if (c == null) {
            return 0;
        }
        int firstVisiblePosition = listView.getFirstVisiblePosition();
        int top = c.getTop();
        int result = -top + firstVisiblePosition * c.getHeight();
        if(c.getHeight()<headerHeight){  /*c已经不是headerview了*/
            return result+headerHeight;
        }else{
            return result;
        }
    }
}
