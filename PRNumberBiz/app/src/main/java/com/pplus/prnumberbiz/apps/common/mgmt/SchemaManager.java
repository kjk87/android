package com.pplus.prnumberbiz.apps.common.mgmt;

import android.content.Context;
import android.content.Intent;

import com.pple.pplus.utils.part.logs.LogUtil;
import com.pple.pplus.utils.part.utils.StringUtils;
import com.pplus.prnumberbiz.Const;
import com.pplus.prnumberbiz.apps.bol.ui.PointConfigActivity;
import com.pplus.prnumberbiz.apps.cash.ui.CashConfigActivity2;
import com.pplus.prnumberbiz.apps.post.ui.PostDetailActivity;
import com.pplus.prnumberbiz.apps.sale.ui.SaleGoodsDetailActivity;
import com.pplus.prnumberbiz.apps.sale.ui.SaleOrderProcessyActivity;
import com.pplus.prnumberbiz.apps.setting.ui.AlarmContainerActivity;
import com.pplus.prnumberbiz.apps.setting.ui.NoticeDetailActivity;
import com.pplus.prnumberbiz.core.code.common.EnumData;
import com.pplus.prnumberbiz.core.code.common.MoveType1Code;
import com.pplus.prnumberbiz.core.code.common.MoveType2Code;
import com.pplus.prnumberbiz.core.network.ApiBuilder;
import com.pplus.prnumberbiz.core.network.model.dto.Bol;
import com.pplus.prnumberbiz.core.network.model.dto.Buy;
import com.pplus.prnumberbiz.core.network.model.dto.BuyGoods;
import com.pplus.prnumberbiz.core.network.model.dto.Notice;
import com.pplus.prnumberbiz.core.network.model.dto.Post;
import com.pplus.prnumberbiz.core.network.model.response.NewResultResponse;
import com.pplus.prnumberbiz.core.util.PplusCommonUtil;
import com.pplus.prnumberbiz.push.PushReceiveData;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import network.common.PplusCallback;
import retrofit2.Call;

/**
 * Created by ksh on 2016-10-18.
 */

public class SchemaManager {

    public static boolean mIsGoodsReg = false;

    private static final String TAG = SchemaManager.class.getSimpleName();
    private static SchemaManager mSchemaManager = null;
    private static Context mContext;

    public static final String SCHEMA_PRNUMBER = "prnumberbiz://";
    public static final String SCHEMA_MOVE_TYPE1 = "moveType1";
    public static final String SCHEMA_MOVE_TYPE2 = "moveType2";
    public static final String SCHEMA_MOVE_TARGET = "moveTarget";
    public static final String SCHEMA_MOVE_TARGET_STRING = "moveTargetString";
    public static final String MSG_NO = "msgNo";
    public static final String SCHEMA_PAGE_DETAIL = "moveType1=inner&moveType2=pageDetail&moveTarget=";
    public static final String SCHEMA_REAL_PROMOTION = "moveType1=inner&moveType2=contactListMain&moveTarget=";

    public static SchemaManager getInstance(Context context) {

        if (mSchemaManager == null) {
            mSchemaManager = new SchemaManager();
        }
        mContext = context;
        return mSchemaManager;
    }

    public void moveToSchema(String type1, String type2, String moveTarget, String moveTargetString, String msgNo, boolean isPush) {

        //null일 경우 crash발생하여 예외처리함.
        if (StringUtils.isEmpty(type1)) {
            return;
        }

        MoveType1Code moveType1Code = MoveType1Code.valueOf(type1);
        MoveType2Code moveType2Code = null;
        if (type2 != null) {
            try {
                moveType2Code = MoveType2Code.valueOf(type2);
            } catch (Exception e) {
                moveType2Code = MoveType2Code.main;
            }
        }
        Intent intent = null;
        if (isPush) {
            callRead(msgNo);
        }
        // 내부로 연결시
        if (moveType1Code == MoveType1Code.inner) {

            if (moveType2Code != null) {
                switch (moveType2Code) {
                    // 특정 PR 페이지 상세
                    case main:
                        break;
                    case pageDetail:
                        //                    intent = new Intent(mContext, ShopActivity.class);
                        //                    Page page = new Page();
                        //                    page.setNo(Long.valueOf(moveTarget));
                        //                    intent.putExtra(Const.PAGE, page);
                        //                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        //                    mContext.startActivity(intent);
                        break;
                    case couponDetail:
//                        intent = new Intent(mContext, CouponWithAdsDetailActivity.class);
//                        CouponTemplate couponTemplate = new CouponTemplate();
//                        couponTemplate.setNo(Long.valueOf(moveTarget));
//                        intent.putExtra(Const.COUPON, couponTemplate);
//                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
//                        mContext.startActivity(intent);
                        break;
                    case postDetail:
                        intent = new Intent(mContext, PostDetailActivity.class);
                        Post post = new Post();
                        post.setNo(Long.valueOf(moveTarget));
                        intent.putExtra(Const.DATA, post);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        mContext.startActivity(intent);
                        break;
//                    case noteDetail:
//                        intent = new Intent(mContext, NoteDetailActivity.class);
//                        intent.putExtra(Const.NO, Long.valueOf(moveTarget));
//                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
//                        mContext.startActivity(intent);
//                        break;
                    case plus:
//                        intent = new Intent(mContext, PlusActivity.class);
//                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
//                        mContext.startActivity(intent);
                        break;
                    case bolHistory:
                        intent = new Intent(mContext, PointConfigActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        mContext.startActivity(intent);
                        break;
                    case cashHistory:
                        intent = new Intent(mContext, CashConfigActivity2.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        mContext.startActivity(intent);
                        break;
                    case msgbox:
                        intent = new Intent(mContext, AlarmContainerActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        mContext.startActivity(intent);
                        break;
                    case offer:
                        break;
//                    case adCoupon:
//                        intent = new Intent(mContext, RealTimeCouponDetailActivity.class);
//                        Advertise advertise = new Advertise();
//                        advertise.setNo(Long.valueOf(moveTarget));
//                        intent.putExtra(Const.ADVERTISE, advertise);
//                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
//                        mContext.startActivity(intent);
//                        break;
//                    case adPost:
//                        intent = new Intent(mContext, RealTimePostDetailActivity.class);
//                        advertise = new Advertise();
//                        advertise.setNo(Long.valueOf(moveTarget));
//                        intent.putExtra(Const.ADVERTISE, advertise);
//                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
//                        mContext.startActivity(intent);
//                        break;
                    case eventWin:
                        break;
                    case eventDetail:
                        break;
                    case noticeDetail:
                        intent = new Intent(mContext, NoticeDetailActivity.class);
                        Notice notice = new Notice();
                        notice.setNo(Long.valueOf(moveTarget));
                        intent.putExtra(Const.NOTICE, notice);
                        mContext.startActivity(intent);
                        break;
                    case bolDetail:
                        intent = new Intent(mContext, PointConfigActivity.class);
                        Bol bol = new Bol();
                        bol.setNo(Long.valueOf(moveTarget));
                        intent.putExtra(Const.DATA, bol);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        mContext.startActivity(intent);
                        break;
                    case buyGoodsUse:
                        intent = new Intent(mContext, SaleGoodsDetailActivity.class);
                        BuyGoods buyGoods = new BuyGoods();
                        buyGoods.setSeqNo(Long.valueOf(moveTarget));
                        intent.putExtra(Const.DATA, buyGoods);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        mContext.startActivity(intent);
                        break;
                    case goodsReg:
                        mIsGoodsReg = true;
                        break;
                    case order:
                        intent = new Intent(mContext, SaleOrderProcessyActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        mContext.startActivity(intent);
                        break;
                    case buyUse:
//                        intent = new Intent(mContext, SaleGoodsDetailActivity.class);
//                        buyGoods = new BuyGoods();
//                        buyGoods.setSeqNo(Long.valueOf(moveTarget));
//                        intent.putExtra(Const.DATA, buyGoods);
//                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
//                        mContext.startActivity(intent);
                        break;
                    case pageSeller:

                        int status = Integer.valueOf(moveTarget);
                        if (status == EnumData.SellerStatus.approvalWait.getStatus()) {

                        } else if (status == EnumData.SellerStatus.approval.getStatus()) {

                        } else if (status == EnumData.SellerStatus.reject.getStatus()) {

                        } else if (status == EnumData.SellerStatus.secondRequest.getStatus()) {

                        } else if (status == EnumData.SellerStatus.stop.getStatus()) {

                        }

                        break;
                }
            }
        } else {
            if (isPush) {
                callRead(msgNo);
            }
            if (StringUtils.isNotEmpty(moveTargetString)) {
                PplusCommonUtil.Companion.openChromeWebView(mContext, moveTargetString);
            }

        }
    }

    private void callRead(String no) {

        Map<String, String> params = new HashMap<>();
        params.put("no", no);
        ApiBuilder.create().readComplete(params).setCallback(new PplusCallback<NewResultResponse<Object>>() {

            @Override
            public void onResponse(Call<NewResultResponse<Object>> call, NewResultResponse<Object> response) {

            }

            @Override
            public void onFailure(Call<NewResultResponse<Object>> call, Throwable t, NewResultResponse<Object> response) {

            }
        }).build().call();
    }

    public void moveToPushData(PushReceiveData data) {
        String type1 = data.getMoveType1();
        String type2 = data.getMoveType2();
        String moveTarget = data.getMoveTarget();
        String moveTargetString = data.getMove_target_string();
        String msgNo = data.getMsgNo();

        if (StringUtils.isNotEmpty(type1))
            moveToSchema(type1, type2, moveTarget, moveTargetString, msgNo, true);
    }

    public void moveToSchema(String url, boolean isPush) {

        if (url == null) return;

        LogUtil.d(TAG, "url = " + url);
        Map<String, List<String>> resultData = null;

        if (url.contains(SCHEMA_PRNUMBER)) {
            try {
                resultData = splitQuery(url);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            String type1 = null;
            String type2 = null;
            String moveTarget = null;
            String moveTargetString = null;
            String msgNo = null;

            if (resultData != null && resultData.get(SCHEMA_MOVE_TYPE1) != null && resultData.get(SCHEMA_MOVE_TYPE1).size() > 0) {
                type1 = resultData.get(SCHEMA_MOVE_TYPE1).get(0);
            }

            if (resultData != null && resultData.get(SCHEMA_MOVE_TYPE2) != null && resultData.get(SCHEMA_MOVE_TYPE2).size() > 0) {
                type2 = resultData.get(SCHEMA_MOVE_TYPE2).get(0);
            }

            if (resultData != null && resultData.get(SCHEMA_MOVE_TARGET) != null && resultData.get(SCHEMA_MOVE_TARGET).size() > 0) {
                moveTarget = resultData.get(SCHEMA_MOVE_TARGET).get(0);
            }

            if (resultData != null && resultData.get(SCHEMA_MOVE_TARGET_STRING) != null && resultData.get(SCHEMA_MOVE_TARGET_STRING).size() > 0) {
                moveTargetString = resultData.get(SCHEMA_MOVE_TARGET_STRING).get(0);
            }

            if (resultData != null && resultData.get(MSG_NO) != null && resultData.get(MSG_NO).size() > 0) {
                msgNo = resultData.get(MSG_NO).get(0);
            }

            if (StringUtils.isNotEmpty(type1))
                moveToSchema(type1, type2, moveTarget, moveTargetString, msgNo, isPush);
        }
    }

    public static Map<String, List<String>> splitQuery(String url) throws UnsupportedEncodingException {

        Map<String, List<String>> params = new HashMap<String, List<String>>();
        String[] urlParts = url.split("\\?");
        String query = null;
        if (urlParts.length > 1) {
            query = urlParts[1];
            for (String param : query.split("&")) {
                String pair[] = param.split("=");
                String key = URLDecoder.decode(pair[0], "UTF-8");
                String value = "";
                if (pair.length > 1) {
                    value = URLDecoder.decode(pair[1], "UTF-8");
                }
                List<String> values = params.get(key);
                if (values == null) {
                    values = new ArrayList<String>();
                    params.put(key, values);
                }
                values.add(value);
            }
        } else {
            if (url.contains(SCHEMA_PRNUMBER)) {
                query = url.replaceAll(SCHEMA_PRNUMBER, "");
            } else {
                query = url;
            }
        }
        for (String param : query.split("&")) {
            String pair[] = param.split("=");
            String key = URLDecoder.decode(pair[0], "UTF-8");
            String value = "";
            if (pair.length > 1) {
                value = URLDecoder.decode(pair[1], "UTF-8");
            }
            List<String> values = params.get(key);
            if (values == null) {
                values = new ArrayList<String>();
                params.put(key, values);
            }
            values.add(value);
        }


        return params;
    }

    public String setSchemaData(PushReceiveData data) {

        if (data != null) {
            StringBuilder builder = new StringBuilder();
            builder.append(SCHEMA_PRNUMBER);
            if (data.getMoveType1() != null) {
                builder.append(SCHEMA_MOVE_TYPE1 + "=" + data.getMoveType1());
            }
            if (data.getMoveType2() != null) {
                builder.append("&" + SCHEMA_MOVE_TYPE2 + "=" + data.getMoveType2());
            }
            if (data.getMoveTarget() != null) {
                builder.append("&" + SCHEMA_MOVE_TARGET + "=" + data.getMoveTarget());
            }
            if (data.getMove_target_string() != null) {
                builder.append("&" + SCHEMA_MOVE_TARGET_STRING + "=" + data.getMove_target_string());
            }

            if (data.getMsgNo() != null) {
                builder.append("&" + MSG_NO + "=" + data.getMsgNo());
            }
            return builder.toString();
        }
        return null;
    }
}

