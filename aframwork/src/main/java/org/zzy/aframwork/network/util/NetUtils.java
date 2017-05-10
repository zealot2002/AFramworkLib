package org.zzy.aframwork.network.util;


import android.text.TextUtils;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class NetUtils {
    public static final MediaType MEDIA_TYPE_JSON
            = MediaType.parse("application/json; charset=utf-8");
    private static final int CONNECT_TIMEOUT = 300;
/*******************************************************************************************************/

    public static String sendGetRequest(RequestCtx ctx) throws Exception {
        OkHttpClient okHttpClient = new OkHttpClient();
        OkHttpClient.Builder builder =  okHttpClient.newBuilder();
        builder.connectTimeout(ctx.getTimerout(),TimeUnit.SECONDS);
        builder.readTimeout(ctx.getTimerout(),TimeUnit.SECONDS);
        builder.writeTimeout(ctx.getTimerout(),TimeUnit.SECONDS);
//        builder.writeTimeout()

        final Request request = new Request.Builder()
                .url(ctx.getUrl())
                .build();
        Response response = okHttpClient.newCall(request).execute();
        if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

        return handleServerData(response.body().string(),response.code(),ctx);
    }

    public static String sendPostRequest(RequestCtx ctx) throws Exception {
        OkHttpClient okHttpClient = new OkHttpClient();
        OkHttpClient.Builder builder =  okHttpClient.newBuilder();
        builder.connectTimeout(ctx.getTimerout(),TimeUnit.SECONDS);
        builder.readTimeout(ctx.getTimerout(),TimeUnit.SECONDS);
        builder.writeTimeout(ctx.getTimerout(),TimeUnit.SECONDS);

        Request request = new Request.Builder()
                .url(ctx.getUrl())
                .post(RequestBody.create(MEDIA_TYPE_JSON,ctx.getParams().toString()))
                .build();

        Response response = okHttpClient.newCall(request).execute();
        if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

        return handleServerData(response.body().string(),response.code(),ctx);
    }

    private static String handleServerData(String strResult, int statusCode, RequestCtx ctx){
        /** 服务器返回数据 **/
        if(strResult.contains(HttpConstant.HTML_DATA_PRE)){
            return HttpConstant.HTML_DATA_ERROR;
        }
        if(TextUtils.isEmpty(strResult)||strResult.equals("null")){
            return HttpConstant.EMPTY_DATA_ERROR;
        }
        // 7:对状态码进行判断
        if (statusCode == 200) {
            /** 解密数据 **/
            if(ctx.getDecrypter()!=null){
                return ctx.getDecrypter().decrypt(strResult);
            }
            return strResult;
        } else {
            return HttpConstant.HTTP_ERROR + statusCode;
        }
    }

}
