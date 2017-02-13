package org.zzy.aframwork.util;

import android.util.Log;


/**
 * Created by admin on 16/12/5.
 */

public class MyLog {
    public static int w(String msg){
        StackTraceElement caller = OtherUtils.getCallerStackTraceElement();
        String tag = "%s.%s(L:%d)";
        String callerClazzName = caller.getClassName();
        callerClazzName = callerClazzName.substring(callerClazzName.lastIndexOf(".") + 1);
        tag = String.format(tag, new Object[]{callerClazzName, caller.getMethodName(), Integer.valueOf(caller.getLineNumber())});
        return Log.w(tag,msg);
    }

    public static int d(String msg){
        StackTraceElement caller = OtherUtils.getCallerStackTraceElement();
        String tag = "%s.%s(L:%d)";
        String callerClazzName = caller.getClassName();
        callerClazzName = callerClazzName.substring(callerClazzName.lastIndexOf(".") + 1);
        tag = String.format(tag, new Object[]{callerClazzName, caller.getMethodName(), Integer.valueOf(caller.getLineNumber())});
        return Log.d(tag,msg);
    }

    public static int e(String msg){
        StackTraceElement caller = OtherUtils.getCallerStackTraceElement();
        String tag = "%s.%s(L:%d)";
        String callerClazzName = caller.getClassName();
        callerClazzName = callerClazzName.substring(callerClazzName.lastIndexOf(".") + 1);
        tag = String.format(tag, new Object[]{callerClazzName, caller.getMethodName(), Integer.valueOf(caller.getLineNumber())});
        return Log.e(tag,msg);
    }

//    private static String generateTag() {
//        StackTraceElement caller = OtherUtils.getCallerStackTraceElement();
//        String tag = "%s.%s(L:%d)";
//        String callerClazzName = caller.getClassName();
//        callerClazzName = callerClazzName.substring(callerClazzName.lastIndexOf(".") + 1);
//        tag = String.format(tag, new Object[]{callerClazzName, caller.getMethodName(), Integer.valueOf(caller.getLineNumber())});
//        return tag;
//    }
}
