package com.pplus.utils.part.logs.crash;

import android.content.Context;
import android.os.Looper;
import android.util.Log;

import com.pplus.utils.part.logs.LogUtil;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by 안명훈 on 16. 6. 27..
 */
public class CrashException implements Thread.UncaughtExceptionHandler {

    private Context context;

    public String CRASH_FILE_NAME_PATTERN = "crashLog-%s.txt";

    public CrashException(Context applicationContext) {
        context = applicationContext;
    }

    @Override
    public void uncaughtException(Thread thread, Throwable ex) {

        errorWrite(thread , ex);
        new Thread() {
            @Override
            public void run() {
                // UI쓰레드에서 토스트 뿌림
                Looper.prepare();
//                Toast.makeText(getApplicationContext(), "에러메시지", Toast.LENGTH_SHORT)
//                        .show();

//                new android.support.v7.app.AlertDialog.Builder(context).setMessage("test").create().show();

//

                Looper.loop();
            }
        }.start();

        // 쓰레드 잠깐 쉼
//        try {
//            Thread.sleep(2000);
//        } catch (InterruptedException e) {
//        }

        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(10);

    }

    /**
     * 발생한 에러에대해 파일로 저장합니다.
     * */
    private void errorWrite(Thread thread, Throwable ex){

        String fileName = new SimpleDateFormat("yy-MM-dd-hh-mm-ss", Locale.getDefault()).format(new Date()) + "_CrashLog.txt";
        BufferedWriter buf = null;
        try {

            new File(getCrashLoggerPath(context)).mkdir();

            File logFile = new File(getCrashLoggerPath(context) + File.separator + fileName);

            buf = new BufferedWriter(new FileWriter(logFile, true));
            buf.append(thread.getName()).append(File.pathSeparator).append(String.valueOf(thread.getId()));
            buf.append(Log.getStackTraceString(ex));
            buf.newLine();


        } catch (IOException e) {
            LogUtil.e("CrashException", e.toString());
            if(buf != null){
                try {
                    buf.close();
                }catch (IOException e2){
                    LogUtil.e("CrashException", e2.toString());
                }

            }
        }
    }

    public static String getCrashLoggerPath(Context context){
//        return Environment.getExternalStorageDirectory().getPath()+File.separator+"crash";
        return context.getFilesDir().getPath()+ File.separator+"crash";
    }

    public static int getCrashCount(Context context){
        return new File(getCrashLoggerPath(context)).listFiles().length;
    }

    public static File[] getCrashFiles(Context context){
        return new File(getCrashLoggerPath(context)).listFiles();
    }

}
