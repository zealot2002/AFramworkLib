package org.zzy.aframwork.network;

import android.os.Handler;
import android.os.Message;


import org.json.JSONException;
import org.zzy.aframwork.network.util.HttpConstant;
import org.zzy.aframwork.network.util.NetDataInvalidException;
import org.zzy.aframwork.network.util.NetUtils;
import org.zzy.aframwork.network.util.RequestCtx;
import org.zzy.aframwork.network.util.ThreadPoolManager;
import org.zzy.aframwork.util.MyLog;

import java.io.IOException;

public class HttpUtil {
    private ThreadPoolManager threadPoolManager;

    private HttpUtil() {
        threadPoolManager = ThreadPoolManager.getInstance();
    }

    private static HttpUtil instance = null;

    public static HttpUtil getInstance() {
        if (instance == null) {
            instance = new HttpUtil();
        }
        return instance;
    }

    public void request(RequestCtx ctx) throws Exception {
        final BaseHandler handler = new BaseHandler(ctx);// handle类数据得到后使用
        final BaseTask taskThread = new BaseTask(ctx, handler);// 线程类--获取数据
        threadPoolManager.addTask(taskThread);
    }

    static class BaseHandler extends Handler {
        private RequestCtx ctx;
        public BaseHandler(RequestCtx ctx) {
            this.ctx = ctx;
        }

        @SuppressWarnings("unchecked")
        public void handleMessage(Message msg) {
            try {
                Object[] objs = (Object[])msg.obj;
                ctx.getCallback().requestCallback(msg.what,objs[1],objs[2]);
            } catch (Exception e) {
                e.printStackTrace();
                MyLog.e("err:"+e.toString());
            }
        }
    }
    public static String getErrorMsg(String msg, int errCode){
        return msg;
    }

    class BaseTask implements Runnable {
        private RequestCtx ctx;
        private Handler handler;

        public BaseTask(RequestCtx ctx,Handler handler) {
            this.ctx = ctx;
            this.handler = handler;
        }

        public void run() {
            String retString = null;
            boolean hasException = false;
            Object[] objs = new Object[3]; //0:成功还是失败；1:数据；2:请求者携带tag
            objs[2] = ctx.getTagObj();
            try {
                MyLog.e("请求服务 url:"+ctx.getUrl());
                if(ctx.getMethod().equals(HttpConstant.HTTP_METHOD_GET)){
                    retString = NetUtils.sendGetRequest(ctx);
                }else if(ctx.getMethod().equals(HttpConstant.HTTP_METHOD_POST)){
                    retString = NetUtils.sendPostRequest(ctx);
                }/*else if(ctx.getMethod().equals(HttpConstant.HTTP_METHOD_PUT)){
                    retString = NetUtils.sendPutRequest(ctx);
                }else if(ctx.getMethod().equals(HttpConstant.HTTP_METHOD_DEL)){
                    retString = NetUtils.sendDelRequest(ctx);
                }*/
//                Logger.e("服务返回数据:"+retString);
                if(retString.equals(HttpConstant.HTML_DATA_ERROR)
                        ||retString.equals(HttpConstant.EMPTY_DATA_ERROR)
                        ||retString.contains(HttpConstant.HTTP_ERROR)){
                    hasException = true;
                    objs[1] = retString;
                }else{
                    if(ctx.getJsonParser()!=null){
                        Object[] tmpObjs = ctx.getJsonParser().parse(retString);
                        objs[0] = tmpObjs[0];
                        objs[1] = tmpObjs[1];
                        if(objs[0].equals(HttpConstant.SUCCESS)){//正常
                            if(ctx.getValidator()!=null){
                                ctx.getValidator().validate(objs[1]);
                            }
                        }else{
                            hasException = true;
                        }
                    }
                }
            }catch(NetDataInvalidException ne){
                ne.printStackTrace();
                hasException = true;
                objs[1] = ne.toString();
            }catch(JSONException je){
                je.printStackTrace();
                hasException = true;
                objs[1] = je.toString();
            }catch(IOException ioe){
                ioe.printStackTrace();
                hasException = true;
                objs[1] = ioe.toString();
            }catch (Exception e) {
                e.printStackTrace();
                hasException = true;
                objs[1] = e.toString();
            }finally{
                if(ctx.getCallback()!=null){
                    if(hasException){//有异常
                        handler.sendMessage(handler.obtainMessage(HttpConstant.FAIL, objs));
                    }else{
                        handler.sendMessage(handler.obtainMessage(HttpConstant.SUCCESS, objs));
                    }
                }
            }
        }
    }
}
