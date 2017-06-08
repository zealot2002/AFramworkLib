package org.zzy.aframwork.network.util;


import android.text.TextUtils;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class NetUtils {
    public static final MediaType MEDIA_TYPE_JSON
            = MediaType.parse("application/json; charset=utf-8");
/*******************************************************************************************************/

    public static String sendGetRequest(RequestCtx ctx) throws Exception {

        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(ctx.getTimerout(), TimeUnit.SECONDS)
                .readTimeout(ctx.getTimerout(), TimeUnit.SECONDS)
                .writeTimeout(ctx.getTimerout(),TimeUnit.SECONDS)
                .build();

        Request request = new Request.Builder()
                .url(ctx.getUrl())
                .headers(Headers.of(ctx.getHeaderMap()))
                .build();
        Response response = client.newCall(request).execute();

        if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

        return handleServerData(response.body().string(),response.code(),ctx);
    }

    public static String sendPostRequest(RequestCtx ctx) throws Exception {
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(ctx.getTimerout(), TimeUnit.SECONDS)
                .readTimeout(ctx.getTimerout(), TimeUnit.SECONDS)
                .writeTimeout(ctx.getTimerout(),TimeUnit.SECONDS)
                .build();

        Request request = new Request.Builder()
                .url(ctx.getUrl())
                .post(RequestBody.create(MEDIA_TYPE_JSON,ctx.getParams().toString()))
                .build();

        Response response = client.newCall(request).execute();
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
