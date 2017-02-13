package org.zzy.aframwork.util;

import android.os.Handler;
import android.os.Message;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by admin on 16/11/10.
 */
public class TimerUtil {
    public interface TimerOutListener{
        void onTimerOut();
    }
    private Timer mTimer;
    private Handler doActionHandler;
    private TimerOutListener timerOutListener;
    /*************************************************************/
    public TimerUtil(){
        mTimer = new Timer();
        doActionHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                int msgId = msg.what;
                switch (msgId) {
                    case 1:
                        if(timerOutListener!=null){
                            timerOutListener.onTimerOut();
                        }
                        break;
                    default:
                        break;
                }
            }
        };
    }

    public void startTimerTask(int delay,TimerOutListener timerOutListener){
        this.timerOutListener = timerOutListener;
        setTimerTask(delay);
    }

    public void startTimerTask(int delay,int period,TimerOutListener timerOutListener){
        this.timerOutListener = timerOutListener;
        setTimerTask(delay,period);
    }

    private void setTimerTask(int delay) {
        mTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                Message message = new Message();
                message.what = 1;
                doActionHandler.sendMessage(message);
            }
        }, delay);
    }
    private void setTimerTask(final int delay,final int period) {
        mTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                Message message = new Message();
                message.what = 1;
                doActionHandler.sendMessage(message);
            }
        }, delay,period);
    }
}
