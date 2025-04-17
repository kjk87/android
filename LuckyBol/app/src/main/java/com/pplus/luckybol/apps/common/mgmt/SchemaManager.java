package com.pplus.luckybol.apps.common.mgmt;

import android.content.Context;
import android.content.Intent;

import com.pplus.luckybol.Const;
import com.pplus.luckybol.apps.bol.ui.BolConfigActivity;
import com.pplus.luckybol.apps.common.ui.base.BaseActivity;
import com.pplus.luckybol.apps.event.ui.Event12Activity;
import com.pplus.luckybol.apps.event.ui.EventActivity;
import com.pplus.luckybol.apps.event.ui.EventDetailActivity;
import com.pplus.luckybol.apps.event.ui.EventImpressionActivity;
import com.pplus.luckybol.apps.event.ui.EventMoveDetailActivity;
import com.pplus.luckybol.apps.event.ui.LuckyLottoDetailActivity;
import com.pplus.luckybol.apps.event.ui.NumberEventActivity;
import com.pplus.luckybol.apps.event.ui.PlayActivity;
import com.pplus.luckybol.apps.point.ui.PointHistoryActivity;
import com.pplus.luckybol.apps.setting.ui.AlarmContainerActivity;
import com.pplus.luckybol.apps.setting.ui.NoticeDetailActivity;
import com.pplus.luckybol.core.code.common.EventType;
import com.pplus.luckybol.core.code.common.MoveType1Code;
import com.pplus.luckybol.core.code.common.MoveType2Code;
import com.pplus.luckybol.core.network.ApiBuilder;
import com.pplus.luckybol.core.network.model.dto.Bol;
import com.pplus.luckybol.core.network.model.dto.Event;
import com.pplus.luckybol.core.network.model.dto.Notice;
import com.pplus.luckybol.core.network.model.response.NewResultResponse;
import com.pplus.luckybol.core.util.PplusCommonUtil;
import com.pplus.luckybol.push.PushReceiveData;
import com.pplus.networks.common.PplusCallback;
import com.pplus.utils.part.logs.LogUtil;
import com.pplus.utils.part.utils.StringUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;

/**
 * Created by ksh on 2016-10-18.
 */

public class SchemaManager{

    private static final String TAG = SchemaManager.class.getSimpleName();
    private static SchemaManager mSchemaManager = null;
    private static Context mContext;

    public static final String SCHEMA_PRNUMBER = "prnumber://";
    public static final String SCHEMA_PPLUS = "pplus://qr";
    public static final String SCHEMA_MOVE_TYPE1 = "moveType1";
    public static final String SCHEMA_MOVE_TYPE2 = "moveType2";
    public static final String SCHEMA_MOVE_TARGET = "moveTarget";
    public static final String SCHEMA_MOVE_TARGET_STRING = "moveTargetString";
    public static final String MSG_NO = "msgNo";

    public static SchemaManager getInstance(Context context){

        if(mSchemaManager == null) {
            mSchemaManager = new SchemaManager();
        }
        mContext = context;
        return mSchemaManager;
    }

    public void moveToSchema(String type1, String type2, String moveTarget, String moveTargetString, String msgNo, boolean isPush){

        //null일 경우 crash발생하여 예외처리함.
        if(StringUtils.isEmpty(type1)) {
            return;
        }

        MoveType1Code moveType1Code = MoveType1Code.valueOf(type1);
        MoveType2Code moveType2Code = null;
        if(type2 != null) {
            try {
                moveType2Code = MoveType2Code.valueOf(type2);
            } catch (IllegalArgumentException e) {
                LogUtil.e(TAG, e.toString());
            }

        }
        Intent intent = null;
        if(isPush) {
            callRead(msgNo);
        }
        // 내부로 연결시
        if(moveType1Code == MoveType1Code.inner) {

            if(moveType2Code != null) {
                switch (moveType2Code) {
                    // 특정 PR 페이지 상세
                    case main:
                        break;
                    case pageDetail:
                        break;
                    case postDetail:
                        break;
//                    case noteDetail:
//                        intent = new Intent(mContext, NoteDetailActivity.class);
//                        intent.putExtra(Const.KEY, NoteContainerActivity.NoteType.receive);
//                        intent.putExtra(Const.NO, Long.valueOf(moveTarget));
//                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
//                        mContext.startActivity(intent);
//                        break;
                    case plus:
                        break;
                    case bolHistory:
                        intent = new Intent(mContext, BolConfigActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        mContext.startActivity(intent);
                        break;
                    case pointHistory:
                        intent = new Intent(mContext, PointHistoryActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        mContext.startActivity(intent);
                        break;
                    case msgbox:
                        intent = new Intent(mContext, AlarmContainerActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        mContext.startActivity(intent);
                        break;
                    case offer:
                        break;
                    case eventWin:
                        intent = new Intent(mContext, EventImpressionActivity.class);
                        Event event = new Event();
                        event.setNo(Long.valueOf(moveTarget));
                        intent.putExtra(Const.DATA, event);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        mContext.startActivity(intent);
                        break;
                    case eventDetail:
                        HashMap<String, String> params = new HashMap<>();
                        params.put("no", moveTarget);

                        ApiBuilder.create().getEvent(params).setCallback(new PplusCallback<NewResultResponse<Event>>(){

                            @Override
                            public void onResponse(Call<NewResultResponse<Event>> call, NewResultResponse<Event> response){

                                Event event = response.getData();
                                if(event != null){
                                    if (StringUtils.isNotEmpty(event.getPrimaryType()) && event.getPrimaryType().equals(EventType.PrimaryType.move.name())) {
                                        Intent intent = new Intent(mContext, EventMoveDetailActivity.class);
                                        intent.putExtra(Const.DATA, event);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                        mContext.startActivity(intent);
                                    } else {
                                        Intent intent = new Intent(mContext, EventDetailActivity.class);
                                        intent.putExtra(Const.DATA, event);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                        mContext.startActivity(intent);
                                    }
                                }
                            }

                            @Override
                            public void onFailure(Call<NewResultResponse<Event>> call, Throwable t, NewResultResponse<Event> response){

                            }
                        }).build().call();
                        break;
                    case deliveryDetail:
                        break;
                    case noticeDetail:
                        intent = new Intent(mContext, NoticeDetailActivity.class);
                        Notice notice = new Notice();
                        notice.setNo(Long.valueOf(moveTarget));
                        intent.putExtra(Const.NOTICE, notice);
                        mContext.startActivity(intent);
                        break;
                    case invite:
                        break;
                    case bolDetail:
                        intent = new Intent(mContext, BolConfigActivity.class);
                        Bol bol = new Bol();
                        bol.setNo(Long.valueOf(moveTarget));
                        intent.putExtra(Const.DATA, bol);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        mContext.startActivity(intent);
                        break;

                    case goodsDetail:
                        break;
                    case menu:
                        break;
                    case buyCancel:
                        break;
                    case buyHistory:
                        break;
                    case orderConfirm:
                        break;
                    case orderCancel:
                        break;
                    case eventJoin1:
                        intent = new Intent(mContext, EventActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        ((BaseActivity)mContext).startActivityForResult(intent, Const.REQ_CASH_CHANGE);
                        break;
                    case eventJoin2:
                        intent = new Intent(mContext, Event12Activity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        ((BaseActivity)mContext).startActivityForResult(intent, Const.REQ_CASH_CHANGE);
                        break;
                    case goodluck1:
                        intent = new Intent(mContext, PlayActivity.class);
                        intent.putExtra(Const.KEY, 1);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        ((BaseActivity)mContext).startActivityForResult(intent, Const.REQ_CASH_CHANGE);
                        break;
                    case goodluck4:
                        intent = new Intent(mContext, PlayActivity.class);
                        intent.putExtra(Const.KEY, 4);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        ((BaseActivity)mContext).startActivityForResult(intent, Const.REQ_CASH_CHANGE);
                        break;
                    case eventNumber:
                        intent = new Intent(mContext, NumberEventActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        ((BaseActivity)mContext).startActivityForResult(intent, Const.REQ_CASH_CHANGE);
                        break;
                    case lottoWin:
                        intent = new Intent(mContext, LuckyLottoDetailActivity.class);
                        event = new Event();
                        event.setNo(Long.valueOf(moveTarget));
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        intent.putExtra(Const.DATA, event);
                        mContext.startActivity(intent);
                        break;
                }
            }

        } else {
            if(isPush) {
                callRead(msgNo);
            }
            if(StringUtils.isNotEmpty(moveTargetString)){
                PplusCommonUtil.Companion.openChromeWebView(mContext, moveTargetString);
            }

        }
    }

    private void callRead(String no){

        Map<String, String> params = new HashMap<>();
        params.put("no", no);
        ApiBuilder.create().readComplete(params).setCallback(new PplusCallback<NewResultResponse<Object>>(){

            @Override
            public void onResponse(Call<NewResultResponse<Object>> call, NewResultResponse<Object> response){

            }

            @Override
            public void onFailure(Call<NewResultResponse<Object>> call, Throwable t, NewResultResponse<Object> response){

            }
        }).build().call();
    }

    public void moveToSchema(String url, boolean isPush){

        if(url == null) return;

        LogUtil.d(TAG, "url = " + url);
        Map<String, List<String>> resultData = null;

        if(url.contains(SCHEMA_PRNUMBER)) {
            try {
                resultData = splitQuery(url);
            } catch (UnsupportedEncodingException e) {
                LogUtil.e(TAG, e.toString());
            }

            String type1 = null;
            String type2 = null;
            String moveTarget = null;
            String moveTargetString = null;
            String msgNo = null;

            if(resultData != null && resultData.get(SCHEMA_MOVE_TYPE1) != null && resultData.get(SCHEMA_MOVE_TYPE1).size() > 0) {
                type1 = resultData.get(SCHEMA_MOVE_TYPE1).get(0);
            }

            if(resultData != null && resultData.get(SCHEMA_MOVE_TYPE2) != null && resultData.get(SCHEMA_MOVE_TYPE2).size() > 0) {
                type2 = resultData.get(SCHEMA_MOVE_TYPE2).get(0);
            }

            if(resultData != null && resultData.get(SCHEMA_MOVE_TARGET) != null && resultData.get(SCHEMA_MOVE_TARGET).size() > 0) {
                moveTarget = resultData.get(SCHEMA_MOVE_TARGET).get(0);
            }

            if(resultData != null && resultData.get(SCHEMA_MOVE_TARGET_STRING) != null && resultData.get(SCHEMA_MOVE_TARGET_STRING).size() > 0) {
                moveTargetString = resultData.get(SCHEMA_MOVE_TARGET_STRING).get(0);
            }

            if(resultData != null && resultData.get(MSG_NO) != null && resultData.get(MSG_NO).size() > 0) {
                msgNo = resultData.get(MSG_NO).get(0);
            }

            if(StringUtils.isNotEmpty(type1)){
                moveToSchema(type1, type2, moveTarget, moveTargetString, msgNo, isPush);
            }

        }
    }

    public static Map<String, List<String>> splitQuery(String url) throws UnsupportedEncodingException{

        Map<String, List<String>> params = new HashMap<String, List<String>>();
        String[] urlParts = url.split("\\?");
        String query = null;
        if(urlParts.length > 1) {
            query = urlParts[1];
            for(String param : query.split("&")) {
                String pair[] = param.split("=");
                String key = URLDecoder.decode(pair[0], "UTF-8");
                String value = "";
                if(pair.length > 1) {
                    value = URLDecoder.decode(pair[1], "UTF-8");
                }
                List<String> values = params.get(key);
                if(values == null) {
                    values = new ArrayList<String>();
                    params.put(key, values);
                }
                values.add(value);
            }
        } else {
            if(url.contains(SCHEMA_PRNUMBER)) {
                query = url.replaceAll(SCHEMA_PRNUMBER, "");
            } else {
                query = url;
            }
        }
        for(String param : query.split("&")) {
            String pair[] = param.split("=");
            String key = URLDecoder.decode(pair[0], "UTF-8");
            String value = "";
            if(pair.length > 1) {
                value = URLDecoder.decode(pair[1], "UTF-8");
            }
            List<String> values = params.get(key);
            if(values == null) {
                values = new ArrayList<String>();
                params.put(key, values);
            }
            values.add(value);
        }


        return params;
    }

    public String setSchemaData(PushReceiveData data){

        if(data != null) {
            StringBuilder builder = new StringBuilder();
            builder.append(SCHEMA_PRNUMBER);
            if(data.getMoveType1() != null) {
                builder.append(SCHEMA_MOVE_TYPE1+"=" + data.getMoveType1());
            }
            if(data.getMoveType2() != null) {
                builder.append("&"+SCHEMA_MOVE_TYPE2+"=" + data.getMoveType2());
            }
            if(data.getMoveTarget() != null) {
                builder.append("&"+SCHEMA_MOVE_TARGET+"=" + data.getMoveTarget());
            }
            if(data.getMove_target_string() != null) {
                builder.append("&"+SCHEMA_MOVE_TARGET_STRING+"=" + data.getMove_target_string());
            }

            if(data.getMsgNo() != null) {
                builder.append("&"+MSG_NO+"=" + data.getMsgNo());
            }
            return builder.toString();
        }
        return null;
    }

}

