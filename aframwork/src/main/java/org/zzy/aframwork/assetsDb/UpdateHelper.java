package org.zzy.aframwork.assetsDb;

import android.content.Context;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;


public class UpdateHelper {
    public interface FileReadyListener {
        void onReady();
    }
    private String fileName;
    private FileReadyListener listener;
    private Context context;
/***************************************************************************************************/

    public void downloadAndOverrideFile(Context context, String url, final String saveFileName, FileReadyListener listener) {
        this.context = context;
        this.fileName = saveFileName;
        this.listener = listener;
        new AsyncTask<String, Integer, File>() {
            @Override
            protected File doInBackground(String... path) {
                OutputStream output = null;
                File filecomplete = null;
                try {
                    URL url = new URL(path[0]);
                    HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();
                    InputStream input = urlConn.getInputStream();
                    String tempPath =  getTmpFileName();

                    int total = urlConn.getContentLength();

                    filecomplete = new File(tempPath);
                    output = new FileOutputStream(filecomplete);
                    byte buffer[] = new byte[1024];
                    int length = -1;
                    int hasread = 0;
                    while ((length = input.read(buffer)) != -1) {
                        output.write(buffer, 0, length);
                        hasread = hasread + length;
                        // Thread.sleep(5);//延迟下载，看到下载效果更明显

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
            }

            @Override
            protected void onPreExecute() {
            }

            @Override
            protected void onPostExecute(File fileName) {
                if (fileName != null) {
                    try {
//                        if(!MD5Utils.getMD5Checksum(fileName.getAbsolutePath()).equals(checkSum)){
//                            Toast.makeText(context,"invalid checksum!",Toast.LENGTH_SHORT).show();
//                            return;
//                        }
                        updateDB();
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(UpdateHelper.this.context,e.toString(), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }.execute(url);
    }

    private void updateDB() {
        /*delete old*/
        deleteOldDb();
        /*rename the new db*/
        renameTmpDb();
        /*restart db*/
        if(listener!=null){
            listener.onReady();
        }
    }
    private String getTmpFileName(){
        return context.getApplicationInfo().dataDir+"/databases/" + fileName + ".tmp";
    }
    private String getDbFileName(){
        return context.getApplicationInfo().dataDir+"/databases/" + fileName;
    }

    private void renameTmpDb() {
        String oldPath =  getTmpFileName();
        String newPath = getDbFileName();
        File file = new File(oldPath);
        file.renameTo(new File(newPath));
    }

    private void deleteOldDb(){
        String dbPath = getDbFileName();
        if (!TextUtils.isEmpty(dbPath)) {
            File file = new File(dbPath);
            if (file.exists()){
                file.delete();
            }
        }
    }
}
