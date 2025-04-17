//package com.pplus.prnumberbiz.apps.group.ui;
//
//import android.os.Bundle;
//import android.support.annotation.NonNull;
//import android.view.View;
//import android.widget.TextView;
//
//import com.pple.pplus.utils.part.logs.LogUtil;
//import com.pplus.prnumberbiz.Const;
//import com.pplus.prnumberbiz.R;
//import com.pplus.prnumberbiz.apps.common.builder.AlertBuilder;
//import com.pplus.prnumberbiz.apps.common.builder.OnAlertResultListener;
//import com.pplus.prnumberbiz.apps.common.mgmt.LoginInfoManager;
//import com.pplus.prnumberbiz.apps.common.toolbar.ImplToolbar;
//import com.pplus.prnumberbiz.apps.common.toolbar.OnToolbarListener;
//import com.pplus.prnumberbiz.apps.common.toolbar.ToolbarOption;
//import com.pplus.prnumberbiz.apps.common.ui.base.BaseActivity;
//import com.pplus.prnumberbiz.core.network.model.dto.Group;
//import com.pplus.prnumberbiz.core.util.ToastUtil;
//
//import java.util.List;
//
//import network.common.PplusCallback;
//import retrofit2.Call;
//
//public class GroupMoveActivity extends BaseActivity implements ImplToolbar{
//
//    @Override
//    public String getPID(){
//
//        return "";
//    }
//
//    @Override
//    public String getSID(){
//
//        return null;
//    }
//
//    @Override
//    public int getLayoutRes(){
//
//        return R.layout.activity_group_move;
//    }
//
//    private String mKey;
//    private Group mGroup;
//    private List<Group> mGroupList;
//
//    private TextView mTextSelectGroup;
//    private int mSelectValue = 0;
//
//    @Override
//    public void initializeView(Bundle savedInstanceState){
//
//        final TextView textGroupName = (TextView) findViewById(R.id.text_group_move_currentGroup);
//        mTextSelectGroup = (TextView) findViewById(R.id.text_group_move_selectGroup);
//        mKey = getIntent().getStringExtra(Const.KEY);
//        if(mKey.equals(Const.CUSTOMER)) {
//
//        }else{
//
//        }
//
////        if(mKey.equals(Const.PLUS_GROUP) || mKey.equals(Const.PLUS)) {
////
////            iRequest = ApiBuilder.create().plusGroupList().setCallback(new PplusCallback<ResultResponse<ResultPlusGroupList>>(){
////
////                @Override
////                public void onResponse(Call<ResultResponse<ResultPlusGroupList>> call, ResultResponse<ResultPlusGroupList> response){
////
////                    mPlusGroupList = response.getData().getPlusGroupList();
////                    for(PlusGroup group : mPlusGroupList) {
////                        if(group.getSeqNo() == mGroupSeqNo) {
////                            mPlusGroup = group;
////                            break;
////                        }
////                    }
////
////                    if(mGroupSeqNo != 0) {
////                        textGroupName.setText(mPlusGroup.getName());
////                    } else {
////                        textGroupName.setText(R.string.word_plus_en);
////                    }
////
////                }
////
////                @Override
////                public void onFailure(Call<ResultResponse<ResultPlusGroupList>> call, Throwable t, ResultResponse<ResultPlusGroupList> response){
////
////                }
////            });
////        } else if(mKey.equals(Const.FAN_GROUP) || mKey.equals(Const.FAN)) {
////
////            iRequest = ApiBuilder.create().fanGroupList(mPageSeqNo).setCallback(new PplusCallback<ResultResponse<ResultFanGroupList>>(){
////
////                @Override
////                public void onResponse(Call<ResultResponse<ResultFanGroupList>> call, ResultResponse<ResultFanGroupList> response){
////
////                    mGroupList = response.getData().getGroupList();
////                    for(Group group : mGroupList) {
////                        if(group.getSeqNo() == mGroupSeqNo) {
////                            mGroup = group;
////                            break;
////                        }
////                    }
////                    if(mGroupSeqNo != 0) {
////                        if(mGroup != null){
////                            textGroupName.setText(mGroup.getName());
////                        }
////
////                    } else {
////                        textGroupName.setText(R.string.word_fan);
////                    }
////
////                }
////
////                @Override
////                public void onFailure(Call<ResultResponse<ResultFanGroupList>> call, Throwable t, ResultResponse<ResultFanGroupList> response){
////
////                }
////            });
////        }
////        if(iRequest != null)
////            iRequest.build().call();
//
//        mTextSelectGroup.setOnClickListener(new View.OnClickListener(){
//
//            @Override
//            public void onClick(View view){
//                AlertBuilder.Builder builder = new AlertBuilder.Builder();
//                builder.setTitle(getString(R.string.word_select));
//                String[] contents;
//                AlertBuilder.STYLE_ALERT style_alert = AlertBuilder.STYLE_ALERT.LIST_RADIO;
//                int index = 0;
//
//                int defaultValue = mSelectValue;
//
//                if(defaultValue == 0) defaultValue = index + 1;
//                style_alert.setValue(defaultValue);
//                builder.setStyle_alert(style_alert);
//
//                builder.setOnAlertResultListener(new OnAlertResultListener(){
//
//                    @Override
//                    public void onCancel(){
//
//                    }
//
//                    @Override
//                    public void onResult(AlertBuilder.EVENT_ALERT event_alert){
//
//                        String name = null;
//                        switch (event_alert) {
//                            case RADIO:
//                                mSelectValue = event_alert.getValue();
//                                name = mGroupList.get(mSelectValue - 1).getName();
//                                break;
//                        }
//
//                        mTextSelectGroup.setText(name);
//                    }
//                }).builder().show(GroupMoveActivity.this);
//            }
//        });
//
//    }
//
//    @NonNull
//    @Override
//    public ToolbarOption getToolbarOption(){
//
//        ToolbarOption toolbarOption = new ToolbarOption(this);
//        toolbarOption.initializeDefaultToolbar(getString(R.string.word_move_group), ToolbarOption.ToolbarMenu.LEFT);
//        toolbarOption.setToolbarMenu(ToolbarOption.ToolbarMenu.RIGHT, getString(R.string.word_complete));
//        return toolbarOption;
//    }
//
//    @Override
//    public OnToolbarListener getOnToolbarClickListener(){
//
//        return new OnToolbarListener(){
//
//            @Override
//            public void onClick(View v, ToolbarOption.ToolbarMenu toolbarMenu, Object tag){
//                switch (toolbarMenu) {
//                    case LEFT:
//                        if(tag.equals(1)) {
//                            onBackPressed();
//                        }
//                        break;
//                    case RIGHT:
//                        if(tag.equals(1)) {
//
////                            if(mSelectValue == 0) {
////                                showAlert(R.string.msg_select_move_group);
////                                return;
////                            }
////
////                            Map<String, Long> params = new HashMap<>();
////                            IRequest iRequest = null;
////                            if(mKey.equals(Const.PLUS_GROUP)) {
////
////                                PlusGroup selectGroup = mPlusGroupList.get(mSelectValue - 1);
////
////                                if(selectGroup.equals(mPlusGroup)) {
////                                    showAlert(R.string.msg_same_group);
////                                    return;
////                                }
////
////                                params.put("seqNo", mPlusGroup.getSeqNo());
////                                params.put("targetPlusGroupSeqNo", selectGroup.getSeqNo());
////                                iRequest = ApiBuilder.create().plusGroupMove(params);
////
////                            } else if(mKey.equals(Const.FAN_GROUP)) {
////
////                                Group selectGroup = mGroupList.get(mSelectValue - 1);
////
////                                if(selectGroup.equals(mGroup)) {
////                                    showAlert(R.string.msg_same_group);
////                                    return;
////                                }
////
////                                params.put("pageSeqNo", mGroup.getPageSeqNo());
////                                params.put("seqNo", mGroup.getSeqNo());
////                                params.put("targetFanGroupSeqNo", selectGroup.getSeqNo());
////                                iRequest = ApiBuilder.create().fanGroupMove(params);
////                            } else if(mKey.equals(Const.PLUS)) {
////
////                                PlusGroup selectGroup = mPlusGroupList.get(mSelectValue - 1);
////
////                                if(mPlusGroup != null && selectGroup.equals(mPlusGroup)) {
////                                    showAlert(R.string.msg_same_group);
////                                    return;
////                                }
////
////                                params.put("pageSeqNo", mPageSeqNo);
////                                params.put("plusGroupSeqNo", selectGroup.getSeqNo());
////                                iRequest = ApiBuilder.create().plusChangeGroup(params);
////                            } else if(mKey.equals(Const.FAN)) {
////
////                                Group selectGroup = mGroupList.get(mSelectValue - 1);
////
////                                if(mGroup != null && selectGroup.equals(mGroup)) {
////                                    showAlert(R.string.msg_same_group);
////                                    return;
////                                }
////
////                                params.put("pageSeqNo", mPageSeqNo);
////                                params.put("memberSeqNo", mMemberSeqNo);
////                                params.put("fanGroupSeqNo", selectGroup.getSeqNo());
////                                iRequest = ApiBuilder.create().fanChangeGroup(params);
////                            }
////                            if(iRequest != null)
////                                iRequest.setCallback(callback).build().call();
//                        }
//                        break;
//                }
//            }
//        };
//
//    }
//}
