//package com.pplus.prnumberuser.core.outgoing;
//
//import android.content.BroadcastReceiver;
//import android.content.Context;
//import android.content.Intent;
//
//import com.pplus.utils.part.logs.LogUtil;
//import com.pplus.prnumberuser.Const;
//import com.pplus.prnumberuser.PRNumberApplication;
//import com.pplus.prnumberuser.apps.LauncherScreenActivity;
//
//
////@EReceiver
//public class OutGoingReceiver extends BroadcastReceiver{
//
//    private Context context;
//
//    @Override
//    public void onReceive(Context context, Intent intent){
//
//        LogUtil.e("OutGoingReceiver", "OUT");
//
//        this.context = context;
//        phoneNumberChk(intent.getStringExtra(Intent.EXTRA_PHONE_NUMBER));
//    }
//
//
//    private void phoneNumberChk(String strNumber){
//
//        if(strNumber.contains("#")) {
//            abortBroadcast();
//            try {
//                strNumber = strNumber.replaceAll("[^0-9]+", "");
//                setResultData(null);
//
//                Intent intent = new Intent(PRNumberApplication.getContext(), OutGoingActivity.class);
//                intent.putExtra(Const.NUMBER, strNumber);
//                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                PRNumberApplication.getContext().startActivity(intent);
//
////                if(strNumber.equals("8888")){
////                    Intent intent = new Intent(PRNumberApplication.getContext(), LauncherScreenActivity.class);
////                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
////                    PRNumberApplication.getContext().startActivity(intent);
////                }else{
////                    Intent intent = new Intent(PRNumberApplication.getContext(), OutGoingActivity.class);
////                    intent.putExtra(Const.NUMBER, strNumber);
////                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
////                    PRNumberApplication.getContext().startActivity(intent);
////                }
//
//            } catch (Exception e) {
//                LogUtil.e("OutGoingReceiver", "e : " + e.toString());
//            }
//        }
//
//    }
//
//}
