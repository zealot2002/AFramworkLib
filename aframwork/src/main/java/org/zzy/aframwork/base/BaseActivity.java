package org.zzy.aframwork.base;


import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.devspark.appmsg.AppMsg;

import org.zzy.aframwork.R;
import org.zzy.aframwork.util.SystemBarHelper;

import static android.view.Gravity.BOTTOM;
import static android.widget.Toast.LENGTH_SHORT;


public abstract class BaseActivity extends FragmentActivity {
    private Dialog loadingDialog;
    private AppMsg appMsg;
    public Context mContext;

    public abstract void findViews();

    /***************************************************************************************************/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (SystemBarHelper.isMIUI6Later() || SystemBarHelper.isFlyme4Later() || Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                SystemBarHelper.setStatusBarDarkMode(this);
                SystemBarHelper.tintStatusBar(this, Color.parseColor("#ffffff"), 0);
            } else {
                SystemBarHelper.tintStatusBar(this, Color.parseColor("#bbbbbb"), 0);
            }
        }
    }

    protected void showToast(Context context,String msg){
        try{
            if(appMsg==null){
                appMsg = AppMsg.makeText((Activity)context, msg, AppMsg.STYLE_INFO);
                appMsg.setAnimation(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                appMsg.setLayoutGravity(BOTTOM);
                appMsg.setDuration(AppMsg.LENGTH_SHORT);
            }
            if(appMsg.isShowing()){
                return;
            }
            appMsg.setText(msg);
            appMsg.show();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    protected void showToast(String msg){
        showToast(this,msg);
    }
    protected void showShortToast(String text) {
        Toast.makeText(mContext, text, LENGTH_SHORT).show();
    }

    protected void showLongToast(String text) {
        Toast.makeText(mContext, text, Toast.LENGTH_LONG).show();
    }

    public void showLoading() {
        showLoading(mContext,"加载中...");
    }
    /** 打开进度条 */
    protected void showLoading(Context context, String message) {
//        if(loadingDialog!=null&&loadingDialog.isShowing()){
//            return ;
//        }
//        if (loadingDialog == null) {
//            LayoutInflater inflater = LayoutInflater.from(context);
//            View v = inflater.inflate(R.layout.loading_dialog, null);// 得到加载view
//            LinearLayout layout = (LinearLayout) v.findViewById(R.id.dialog_view);// 加载布局
//            loadingDialog = new Dialog(context, R.style.loading_dialog);// 创建自定义样式dialog
//            loadingDialog.setCancelable(false);// 不可以用“返回键”取消
//            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
//                    LinearLayout.LayoutParams.MATCH_PARENT,
//                    LinearLayout.LayoutParams.MATCH_PARENT);
//            loadingDialog.setContentView(layout,params);// 设置布局
//        }
//        loadingDialog.show();
}

    public void closeLoading() {
//        try{
//            if (loadingDialog != null) {
//                loadingDialog.dismiss();
//                loadingDialog = null;
//            }
//        }catch (Exception e){
//            e.printStackTrace();
//        }
    }

    public void startActivity(Class<?> cls) {
        startActivity(cls, null);
    }

    public void startActivity(Class<?> cls, Bundle bundle) {
        Intent intent = new Intent();
        intent.setClass(this, cls);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivity(intent);
    }

    public void showError(String msg){
        if(mContext!=null)
            showShortToast(msg);
    }
}
