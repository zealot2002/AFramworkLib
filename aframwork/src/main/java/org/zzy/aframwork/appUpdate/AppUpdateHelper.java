package org.zzy.aframwork.appUpdate;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.text.method.ScrollingMovementMethod;
import android.widget.Button;
import android.widget.Scroller;
import android.widget.TextView;
import android.widget.Toast;


import org.zzy.aframwork.network.util.MD5Utils;
import org.zzy.aframwork.util.CommonUtil;
import org.zzy.aframwork.util.TimerUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;


public class AppUpdateHelper {
    private static String appName;
    private static String fileMd5CheckSum;
/***************************************************************************************************/

    /*
    * 服务器版本与本地版本比较，判断是否需要更新
    * */
    public static void checkAndDoUpdate(Context context, final String appName, int remoteVersionCode,
                                        boolean isForced, final String changeLog, final String downloadUrl) {
        int currentVersionCode = CommonUtil.getAppVersionCode(context);
        if (remoteVersionCode > currentVersionCode) {
            try{
                updataDialog(context,appName,isForced,changeLog,downloadUrl,"");
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    private static void download(Context context, String url) {
        Intent intent = new Intent(context, DownAPKService.class);
        intent.putExtra("apk_url", url);
        context.startService(intent);
        Toast.makeText(context, "正在后台进行下载，稍后会自动安装", Toast.LENGTH_SHORT).show();
    }

    /**
     * 更新提示的对话框
     */
    public static void updataDialog(final Context context, final String appName, boolean isForced, final String changeLog, final String downloadUrl, final String fileMd5CheckSum) {
        AppUpdateHelper.appName = appName;
        //服务器大于本地提示更新
        AppUpdateHelper.fileMd5CheckSum = fileMd5CheckSum;

        TextView textView = null;

        if (isForced) {
            final AlertDialog dialog = new AlertDialog.Builder(context).setTitle("更新提示：").setMessage(changeLog)
                    .setPositiveButton("立即更新", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface arg0, int arg1) {
                            downloadApkFromServer(downloadUrl, context);
                        }
                    }).setNegativeButton("下次再说", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface arg0, int arg1) {
                        }
                    }).setCancelable(false).show();
            new TimerUtil().startTimerTask(500, new TimerUtil.TimerOutListener() {
                @Override
                public void onTimerOut() {
                    final Button positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                    if(positiveButton != null)
                        positiveButton.requestFocus();
                }
            });

            dialog.setCanceledOnTouchOutside(false);
            textView = (TextView) dialog.findViewById(android.R.id.message);

        } else {// 强制更新
            final AlertDialog dialog = new AlertDialog.Builder(context).setTitle("更新提示：").setMessage(changeLog)
                    .setPositiveButton("立即更新", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface arg0, int arg1) {
                            downloadApkFromServer(downloadUrl, context);
                        }
                    }).setCancelable(false).show();

            new TimerUtil().startTimerTask(500, new TimerUtil.TimerOutListener() {
                @Override
                public void onTimerOut() {
                    final Button positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                    if(positiveButton != null)
                        positiveButton.requestFocus();
                }
            });

            dialog.setCanceledOnTouchOutside(false);
            textView = (TextView) dialog.findViewById(android.R.id.message);
        }
        /*doesn't work!!*/
//        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
//            @Override
//            public void onShow(DialogInterface dialogInterface) {
//                final Button positiveButton = ((AlertDialog)dialogInterface).getButton(AlertDialog.BUTTON_POSITIVE);
//                if(positiveButton != null)
//                    positiveButton.requestFocus();
//            }
//        });

        textView.setMaxLines(5);
        textView.setScroller(new Scroller(context));
        textView.setVerticalScrollBarEnabled(true);
        textView.setMovementMethod(new ScrollingMovementMethod());
    }

    static ProgressDialog progressDialog = null;

    private static void downloadApkFromServer(String path, final Context context) {
        if(progressDialog == null){
            progressDialog = new ProgressDialog(context);
        }
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setMessage("正在下载更新");
        progressDialog.setCancelable(false);
        progressDialog.show();
        progressDialog.setCanceledOnTouchOutside(false);
        new AsyncTask<String, Integer, File>() {
            @Override
            protected File doInBackground(String... path) {
                OutputStream output = null;
                File filecomplete = null;
                try {
                    URL url = new URL(path[0]);
                    HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();
                    InputStream input = urlConn.getInputStream();
                    String temppath = Environment.getExternalStorageDirectory().getPath() + File.separator + appName;
                    File file = new File(temppath);
                    int total = urlConn.getContentLength();

                    if (!file.exists()) {
                        file.mkdir();
                    }
                    filecomplete = new File(file.getAbsolutePath() + File.separator + appName + ".apk");
                    output = new FileOutputStream(filecomplete);
                    byte buffer[] = new byte[1024];
                    int length = -1;
                    int hasread = 0;
                    while ((length = input.read(buffer)) != -1) {
                        output.write(buffer, 0, length);
                        hasread = hasread + length;
                        // Thread.sleep(5);//延迟下载，看到下载效果更明显

                        progressDialog.setProgress((Integer) (hasread * 100) / total);

                        publishProgress((Integer) (hasread * 100) / total);
                    }
                    output.flush();
                } catch (Exception e) {
                    e.printStackTrace();
                    return filecomplete;
                } finally {
                    try {
                        if(output!=null){
                            output.close();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                return filecomplete;
            }

            @Override
            protected void onProgressUpdate(Integer[] values) {
                super.onProgressUpdate(values);
                progressDialog.setProgress(values[0]);
            }

            @Override
            protected void onPreExecute() {
                progressDialog.setMax(100);
            }

            @Override
            protected void onPostExecute(File fileName) {
                if (fileName != null) {
                    progressDialog.dismiss();

                    try {
                        if(!MD5Utils.getMD5Checksum(fileName.getAbsolutePath()).equals(fileMd5CheckSum)){
                            Toast.makeText(context,"invalid checksum!", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(context,e.toString(), Toast.LENGTH_SHORT).show();
                    }

                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setDataAndType(Uri.fromFile(fileName), "application/vnd.android.package-archive");
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                    //震动提示
//                    Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
//                    vibrator.vibrate(1000L);//参数是震动时间(long类型)
                    context.startActivity(intent);// 下载完成之后自动弹出安装界面
                    System.exit(0);
                }
            }
        }.execute(path);
    }
}
