package com.pple.pplus.utils.part.logs;

import android.os.Binder;
import android.os.Environment;
import android.util.Log;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.helpers.FormattingTuple;
import org.slf4j.helpers.MessageFormatter;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.logging.FileHandler;
import java.util.logging.Formatter;
import java.util.logging.Level;
import java.util.logging.LogRecord;

import com.pple.pplus.utils.Config;

/**
 * <!-- 필수 사항 --!>
 * Created by 안명훈 on 16. 6. 24..<br><br>
 *
 * File log saved 함수 필수 호출<br>
 * initializeLogSave("저장할 폴더 이름")<br>
 * root/name/yy-mm-dd_LOG_number.txt 저장함
 * <hr></hr>
 *     <h1>로그 사용법</h1>
 *
 * <ul>
 * 1. LogUtil.?(TAG,String) <br>
 * 2. LogUtil.?(TAG,String,Object) <br>
 * ex> 선언 <br>
 * int ObjectA = 1; <br>
 * String ObjectB = "LOG"; <br>
 * boolean ObjectC = true; <br>
 * LogUtil.d(TAG,"objA = {} , objB = {} , objC = {}",ObjectA , ObjectB , ObjectC); <br>
 * result> objA = 1 , objB = LOG , objC = true
 * </ul>
 */
public class LogUtil {

    // 파일저장 관련 변수 선언
    /**
     * 파일 사이즈
     */
    private static final int LOG_FILE_SIZE_LIMIT = 512*1024;
    /**
     * 로그 파일 최대치 갯수
     * */
    private static final int LOG_FILE_MAX_COUNT = 10;
    /**
     * 파일명
     * */
    private static String LOG_FILE_NAME = null;
    /**
     * 로그 표현 시각 포멧
     * */
    private static final SimpleDateFormat formatter =
            new SimpleDateFormat("MM-dd HH:mm:ss.SSS: ", Locale.getDefault());
    /**
     * 시간 객체
     * */
    private static final Date date = new Date();
    /**
     * 로그 저장을 위한 객체
     * */
    private static java.util.logging.Logger logger;
    /**
     * 파일 컨트롤러
     * */
    private static FileHandler fileHandler;
    // 여기까지

    /**
     * * <h><b>LogUtil 을 사용하기위해 필수 퍼미션을 등록해야 합니다.<br>미 등록시 오류 발생하며 해당부분을 수정하세요. </b></h><br>
     * uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE"
     * @param applicationName 저장할 파일의 경로를 설정합니다. <br> local/applicationName/yy-mm-dd_Log_순번.txt
     * */
    public static void initializeLogSave(String applicationName) {

        try {

            LOG_FILE_NAME =
                    new SimpleDateFormat("yy-MM-dd", Locale.getDefault()).format(date) + "_LOG_%g.txt";

            new File(Environment.getExternalStorageDirectory() + File.separator + applicationName).mkdir();

            fileHandler = new FileHandler(Environment.getExternalStorageDirectory() + File.separator + applicationName
                    + File.separator +
                    LOG_FILE_NAME, LOG_FILE_SIZE_LIMIT, LOG_FILE_MAX_COUNT, true);

            fileHandler.setFormatter(new Formatter() {
                @Override
                public String format(LogRecord r) {
                    date.setTime(System.currentTimeMillis());

                    StringBuilder ret = new StringBuilder(80);
                    ret.append(formatter.format(date));
                    ret.append(r.getMessage());
                    return ret.toString();
                }
            });

            logger = java.util.logging.Logger.getLogger(LogUtil.class.getName());
            logger.addHandler(fileHandler);
            logger.setLevel(Level.ALL);
            logger.setUseParentHandlers(false);

            Config.setLogSaveEnable(Config.CONFIG.ON);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private static void logSaveToFile(String level , final String tag, final String format, final Object... arg1){

        logger.log(Level.INFO, String.format("%s/%s(%d): %s\n",level,
                tag, Binder.getCallingPid(), getFormatMessage(format , arg1)));
    }

    private static void logSaveToFile(String level,final String tag, final Throwable tr, final String msg){

        logger.log(Level.INFO, String.format("%s/%s(%d): %s\n",level,
                tag, Binder.getCallingPid(), getThrowableMessage(msg , tr)));
    }

    public static Logger getLogger(final String tag) {
        return LoggerFactory.getLogger(tag);
    }

    public static void v(final String tag, final String format, final Object... arg1) {

        if(Config.isLogSaveEnable()){
            logSaveToFile("V" , tag , format , arg1);
        }

        if(Config.isLogEnable()) {
            if(getLogger(tag).isTraceEnabled()) {
                getLogger(tag).trace(format, arg1);
            }else{
                FormattingTuple ft = MessageFormatter.arrayFormat(format , arg1);
                Log.v(tag,ft.getMessage());
            }
        }

    }

    public static void d(final String tag, final String format, final Object... arg1) {

        if(Config.isLogSaveEnable()){
            logSaveToFile("D" , tag , format , arg1);
        }

        if(Config.isLogEnable()) {
            if(getLogger(tag).isDebugEnabled()) {
                getLogger(tag).debug(format, arg1);
            }else{
                FormattingTuple ft = MessageFormatter.arrayFormat(format , arg1);
                Log.d(tag,ft.getMessage());
            }
        }
    }

    public static void i(final String tag, final String format, final Object... arg1) {

        if(Config.isLogSaveEnable()){
            logSaveToFile("I" , tag , format , arg1);
        }

        if(Config.isLogEnable())
            getLogger(tag).info(format, arg1);
    }

    public static void w(final String tag, final String format, final Object... arg1) {

        if(Config.isLogSaveEnable()){
            logSaveToFile("W" , tag , format , arg1);
        }

        if(Config.isLogEnable())
            getLogger(tag).warn(format, arg1);
    }

    public static void e(final String tag, final String format, final Object... arg1) {

        if(Config.isLogSaveEnable()){
            logSaveToFile("E" , tag , format , arg1);
        }

        if(Config.isLogEnable())
            getLogger(tag).error(format, arg1);
    }

    public static void e(final String tag, final Throwable tr, final String msg) {

        if(Config.isLogSaveEnable()){
            logSaveToFile("E" , tag , tr , msg);
        }

        if(Config.isLogEnable())
            getLogger(tag).error(msg, tr);
    }

    public static void e(final String tag, final Throwable tr) {

        if(Config.isLogSaveEnable()){
            logSaveToFile("E" , tag , tr , "");
        }

        if(Config.isLogEnable())
            getLogger(tag).error(tr.getMessage(), tr);
    }

    public static String getFormatMessage(String format , Object... arg1 ){
        FormattingTuple ft = MessageFormatter.arrayFormat(format , arg1);

        return ft.getMessage();
    }

    public static String getThrowableMessage(String message ,Throwable throwable){

        if (throwable != null) {
            message += '\n' + Log.getStackTraceString(throwable);
        }

        return message;
    }

    public static void entry_log(String id) {
        Throwable stack = new Throwable().fillInStackTrace();
        StackTraceElement[] trace = stack.getStackTrace();

        Log.v("ENTRY",
                id + " - " + trace[1].getClassName() + "."
                        + trace[1].getMethodName() + ":"
                        + trace[1].getLineNumber());
    }

}
