package org.zzy.aframwork.base;

/**
 * Created by admin on 16/11/7.
 */

import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import org.zzy.aframwork.R;


public abstract class BaseFlatActivity extends BaseActivity implements View.OnClickListener {
    /*flat*/
    private RelativeLayout rlDisconnectTipsLayout;
    public abstract void findViews();

    /***************************************************************************************************/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_flat);
        rlDisconnectTipsLayout = (RelativeLayout)findViewById(R.id.rlDisconnectTipsLayout);
        rlDisconnectTipsLayout.setOnClickListener(this);
        super.onCreate(savedInstanceState);
    }

    protected void refreshAllData(){};

    protected void showDisconnect(){
        rlDisconnectTipsLayout.setVisibility(View.VISIBLE);
    }
    protected void showActivity(){
        rlDisconnectTipsLayout.setVisibility(View.GONE);
    }
    @Override
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.rlDisconnectTipsLayout) {
            refreshAllData();

        }
    }
}
