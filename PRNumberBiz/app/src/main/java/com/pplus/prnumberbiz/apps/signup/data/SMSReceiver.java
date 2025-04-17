package com.pplus.prnumberbiz.apps.signup.data;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;

import com.pple.pplus.utils.part.info.OsUtil;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SMSReceiver extends BroadcastReceiver{

    @Override
    public void onReceive(Context context, Intent intent){

        if(intent.getAction().equals("android.provider.Telephony.SMS_RECEIVED")) {
            StringBuilder sms = new StringBuilder();    // SMS문자를 저장할 곳
            Bundle bundle = intent.getExtras();         // Bundle객체에 문자를 받아온다

            if(bundle != null) {
                // 번들에 포함된 문자 데이터를 객체 배열로 받아온다
                Object[] pdusObj = (Object[]) bundle.get("pdus");

                // SMS를 받아올 SmsMessage 배열을 만든다
                SmsMessage[] messages = new SmsMessage[pdusObj.length];
                for(int i = 0; i < pdusObj.length; i++) {
                    if(OsUtil.isMarshmallow()) {
                        String format = bundle.getString("format");
                        messages[i] = SmsMessage.createFromPdu((byte[]) pdusObj[i], format);
                    } else {
                        messages[i] = SmsMessage.createFromPdu((byte[]) pdusObj[i]);
                    }

                }

                // SmsMessage배열에 담긴 데이터를 append메서드로 sms에 저장
                for(SmsMessage smsMessage : messages) {
                    // getMessageBody메서드는 문자 본문을 받아오는 메서드
                    sms.append(smsMessage.getMessageBody());
                }

                Pattern pattern = Pattern.compile("\\d{6}");

                // matcher에 smsBody와 위에서 만든 Pattern 객체를 매치시킨다
                Matcher matcher = pattern.matcher(sms.toString());

                String authNumber = null;

                // 패턴과 일치하는 문자열이 있으면 그 첫번째 문자열을 authNumber에 담는다
                if(matcher.find()) {
                    authNumber = matcher.group(0);
                }

                Intent in = new Intent(context.getPackageName() + ".sms").putExtra("number", authNumber);
                context.sendBroadcast(in);
                //				this.abortBroadcast();
            }
        }
    }

}
