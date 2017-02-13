package org.zzy.aframwork.assetsDb;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import org.zzy.aframwork.util.TimerUtil;


/**
 * Created by admin on 16/11/21.
 */
public class AutoDbHelper {
    public interface CheckDbUpdateInterface {
        void onCheck(CheckDbUpdateCallback callback);
        void onUpdateDbSuccess();
    }
    public interface CheckDbUpdateCallback{
        void onCheckCallback(boolean b, String url, String checkSum);
    }

    private CheckDbUpdateInterface checkDbUpdateInterface;
    private AssetsDbHelper assetsDbHelper;
    private Context context;
    private String dbName;
    private int checkDuration;
    /*********************************************************************************************************/
    /*
    *
    * app 初始化时候调用
    *
    * */
    public AutoDbHelper(Context context, String dbName, int checkDuration, CheckDbUpdateInterface checkDbUpdateInterface){
        this.context = context;
        this.dbName = dbName;
        this.checkDuration = checkDuration;
        this.checkDbUpdateInterface = checkDbUpdateInterface;
        try {
            init();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void init() throws Exception {
        if(checkDuration<5000){
            throw new Exception("checkDuration must more than 5000 millisecond");
        }
        /*默认版本为1*/
        assetsDbHelper = new AssetsDbHelper(context,dbName);
        this.checkDbUpdateInterface = checkDbUpdateInterface;
        /*start a repeat timer by duration loop*/
        new TimerUtil().startTimerTask(100,checkDuration,new TimerUtil.TimerOutListener() {
            @Override
            public void onTimerOut() {
                /*to check db version*/
                AutoDbHelper.this.checkDbUpdateInterface.onCheck(new CheckDbUpdateCallback() {
                    @Override
                    public void onCheckCallback(boolean b, String url, String checkSum) {
                        if(b){
                            /*need update */
                            new UpdateHelper().downloadAndOverrideFile(AutoDbHelper.this.context,url,
                                AutoDbHelper.this.dbName, new UpdateHelper.FileReadyListener() {
                            @Override
                            public void onReady() {
                                /*Finish download and check and replace*/
                                try{
                                    assetsDbHelper.resetDb();
                                    /*如果没有resetDb成功，会抛异常*/
                                    AutoDbHelper.this.checkDbUpdateInterface.onUpdateDbSuccess();
                                }catch (Exception e){
                                    e.printStackTrace();
                                }
                                }
                            });
                        }
                    }
                });
            }
        });
    }

    public void close(){
        assetsDbHelper.close();
    }
    public SQLiteDatabase getReadableDatabase(){
        return assetsDbHelper.getReadableDatabase();
    }


}
