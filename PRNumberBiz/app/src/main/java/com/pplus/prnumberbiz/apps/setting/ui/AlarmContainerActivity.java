package com.pplus.prnumberbiz.apps.setting.ui;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.CompoundButton;

import com.pple.pplus.utils.part.utils.StringUtils;
import com.pplus.prnumberbiz.Const;
import com.pplus.prnumberbiz.R;
import com.pplus.prnumberbiz.apps.bol.ui.PointConfigActivity;
import com.pplus.prnumberbiz.apps.cash.ui.CashConfigActivity2;
import com.pplus.prnumberbiz.apps.common.builder.AlertBuilder;
import com.pplus.prnumberbiz.apps.common.builder.OnAlertResultListener;
import com.pplus.prnumberbiz.apps.common.builder.data.AlertData;
import com.pplus.prnumberbiz.apps.common.mgmt.LoginInfoManager;
import com.pplus.prnumberbiz.apps.common.toolbar.ImplToolbar;
import com.pplus.prnumberbiz.apps.common.toolbar.OnToolbarListener;
import com.pplus.prnumberbiz.apps.common.toolbar.ToolbarOption;
import com.pplus.prnumberbiz.apps.common.ui.base.BaseActivity;
import com.pplus.prnumberbiz.apps.common.ui.custom.SafeSwitchCompat;
import com.pplus.prnumberbiz.apps.main.ui.BizMainActivity;
import com.pplus.prnumberbiz.apps.post.ui.PostDetailActivity;
import com.pplus.prnumberbiz.apps.setting.data.AlarmAdapter;
import com.pplus.prnumberbiz.core.code.common.MoveType1Code;
import com.pplus.prnumberbiz.core.code.common.MoveType2Code;
import com.pplus.prnumberbiz.core.network.ApiBuilder;
import com.pplus.prnumberbiz.core.network.model.dto.Bol;
import com.pplus.prnumberbiz.core.network.model.dto.Msg;
import com.pplus.prnumberbiz.core.network.model.dto.Notice;
import com.pplus.prnumberbiz.core.network.model.dto.Post;
import com.pplus.prnumberbiz.core.network.model.response.NewResultResponse;
import com.pplus.prnumberbiz.core.util.PplusCommonUtil;

import java.util.HashMap;
import java.util.Map;

import network.common.PplusCallback;
import retrofit2.Call;

public class AlarmContainerActivity extends BaseActivity implements ImplToolbar{

    private LinearLayoutManager mLayoutManager;
    private AlarmAdapter mAdapter;

    private int mPage = 1, mTotalCount = 0;
    private boolean mLockListView = true;

    @Override
    public String getPID(){

        return "";
    }

    @Override
    public int getLayoutRes(){

        return R.layout.activity_alarm_container;
    }

    @Override
    public void initializeView(Bundle savedInstanceState){

        RecyclerView recyclerView = (RecyclerView)findViewById(R.id.recycler_alarm);
        mLayoutManager = new LinearLayoutManager(this);
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new AlarmAdapter(this);
        recyclerView.setAdapter(mAdapter);
        mAdapter.setActivity(this);

        mAdapter.setOnItemClickListener(new AlarmAdapter.OnItemClickListener(){

            @Override
            public void onItemClick(int position){


                final Msg item = mAdapter.getItem(position);

                MoveType1Code moveType1Code = null;
                MoveType2Code moveType2Code = null;
                if(item.getMoveType2() != null) {
                    try {
                        moveType1Code = MoveType1Code.valueOf(item.getMoveType1());
                        moveType2Code = MoveType2Code.valueOf(item.getMoveType2());
                    } catch (Exception e) {

                    }
                }

                if(moveType1Code == null){
                    return;
                }

                if(moveType1Code == MoveType1Code.inner){
                    // 스키마 호출
                    if(moveType2Code == null){
                        return;
                    }
                    // 스키마 호출
                    switch (moveType2Code) {
                        case pageDetail:
                            Intent intent = new Intent(AlarmContainerActivity.this, BizMainActivity.class);
                            //                        Page page = new Page();
                            //                        page.setNo(Long.valueOf(item.getMoveTarget().getNo()));
                            //                        intent.putExtra(Const.PAGE, page);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            callRead(""+item.getNo());
                            break;
                        case postDetail:
                            intent = new Intent(AlarmContainerActivity.this, PostDetailActivity.class);
                            Post post = new Post();
                            post.setNo(item.getMoveTarget().getNo());
                            intent.putExtra(Const.DATA, post);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            callRead(""+item.getNo());
                            break;
//                        case noteDetail:
//                            intent = new Intent(AlarmContainerActivity.this, NoteDetailActivity.class);
//                            intent.putExtra(Const.NO, item.getMoveTarget().getNo());
//                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
//                            startActivity(intent);
//                            callRead(""+item.getNo());
//                            break;
                        case plus:
//                            intent = new Intent(AlarmContainerActivity.this, PlusActivity.class);
//                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
//                            startActivity(intent);
                            break;
                        case bolHistory:
                            intent = new Intent(AlarmContainerActivity.this, PointConfigActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                            startActivity(intent);
                            break;
                        case cashHistory:
                            intent = new Intent(AlarmContainerActivity.this, CashConfigActivity2.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                            startActivity(intent);
                            break;
                        case msgbox:
                            break;
                        case offer:
                            break;
//                        case adCoupon:
//                            intent = new Intent(AlarmContainerActivity.this, RealTimeCouponDetailActivity.class);
//                            Advertise advertise = new Advertise();
//                            advertise.setNo(item.getNo());
//                            intent.putExtra(Const.ADVERTISE, advertise);
//                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP );
//                            startActivity(intent);
//                            break;
//                        case adPost:
//                            intent = new Intent(AlarmContainerActivity.this, RealTimePostDetailActivity.class);
//                            advertise = new Advertise();
//                            advertise.setNo(item.getNo());
//                            intent.putExtra(Const.ADVERTISE, advertise);
//                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP );
//                            startActivity(intent);
//                            break;
                        case eventWin:
                            break;
                        case eventDetail:
                            break;
                        case noticeDetail:
                            intent = new Intent(AlarmContainerActivity.this, NoticeDetailActivity.class);
                            Notice notice = new Notice();
                            notice.setNo(item.getNo());
                            intent.putExtra(Const.NOTICE, notice);
                            startActivity(intent);
                            break;
                        case bolDetail:
                            intent = new Intent(AlarmContainerActivity.this, PointConfigActivity.class);
                            Bol bol = new Bol();
                            bol.setNo(item.getNo());
                            intent.putExtra(Const.DATA, bol);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                            startActivity(intent);
                            break;
                    }
                }else{
                    if(StringUtils.isNotEmpty(item.getMoveTargetString())){
                        PplusCommonUtil.Companion.openChromeWebView(AlarmContainerActivity.this, item.getMoveTargetString());
                    }
                }
            }
        });

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener(){

            int pastVisibleItems, visibleItemCount, totalItemCount;

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy){

                super.onScrolled(recyclerView, dx, dy);
                visibleItemCount = mLayoutManager.getChildCount();
                totalItemCount = mLayoutManager.getItemCount();
                pastVisibleItems = mLayoutManager.findFirstVisibleItemPosition();
                if(!mLockListView) {
                    if((totalItemCount < mTotalCount) && (visibleItemCount + pastVisibleItems) >= (totalItemCount * Const.NEXT_RECYCLERVIEW_MARGIN)) {
                        mPage++;
                        listCall(mPage);
                    }
                }
            }
        });
        getCount();
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

    private void getCount(){

        ApiBuilder.create().getMsgCountInBox().setCallback(new PplusCallback<NewResultResponse<Integer>>(){

            @Override
            public void onResponse(Call<NewResultResponse<Integer>> call, NewResultResponse<Integer> response){
                mTotalCount = response.getData();
                mPage = 1;
                mAdapter.clear();
                listCall(mPage);
            }

            @Override
            public void onFailure(Call<NewResultResponse<Integer>> call, Throwable t, NewResultResponse<Integer> response){

            }
        }).build().call();
    }

    private void listCall(int page){
        Map<String, String> params = new HashMap<>();
        params.put("pg", ""+page);
        showProgress("");
        ApiBuilder.create().getMsgListInBox(params).setCallback(new PplusCallback<NewResultResponse<Msg>>(){

            @Override
            public void onResponse(Call<NewResultResponse<Msg>> call, NewResultResponse<Msg> response){
                hideProgress();
                mAdapter.addAll(response.getDatas());
            }

            @Override
            public void onFailure(Call<NewResultResponse<Msg>> call, Throwable t, NewResultResponse<Msg> response){
                hideProgress();
            }
        }).build().call();
    }

    public void delete(Long no){

        Map<String, String> params = new HashMap<>();
        params.put("no", ""+no);
        showProgress("");
        ApiBuilder.create().deleteMsgInBox(params).setCallback(new PplusCallback<NewResultResponse<Object>>(){

            @Override
            public void onResponse(Call<NewResultResponse<Object>> call, NewResultResponse<Object> response){
                hideProgress();
                showAlert(R.string.msg_delete_complete);
                getCount();
            }

            @Override
            public void onFailure(Call<NewResultResponse<Object>> call, Throwable t, NewResultResponse<Object> response){
                hideProgress();
            }
        }).build().call();
    }


    @NonNull
    @Override
    public ToolbarOption getToolbarOption(){

        ToolbarOption toolbarOption = new ToolbarOption(this);
        toolbarOption.initializeDefaultToolbar(getString(R.string.word_alarm_container), ToolbarOption.ToolbarMenu.LEFT);
        View view = getLayoutInflater().inflate(R.layout.item_top_switch, null);
        final SafeSwitchCompat switchCompat = (SafeSwitchCompat) view.findViewById(R.id.switch_top_right);
        if(LoginInfoManager.getInstance().getUser().getDevice().getInstalledApp() != null){
            switchCompat.setSafeCheck(LoginInfoManager.getInstance().getUser().getDevice().getInstalledApp().isPushActivate(), SafeSwitchCompat.IGNORE);
        }else{
            switchCompat.setSafeCheck(false, SafeSwitchCompat.IGNORE);
        }

        switchCompat.setOnSafeCheckedListener(new SafeSwitchCompat.OnSafeCheckedListener(){

            @Override
            public void onAlwaysCalledListener(CompoundButton buttonView, boolean isChecked){

            }

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked){

                if(isChecked) {
                    String pushMask = "1111111111111111";
                    pushUpdate(pushMask, true);
                } else {
                    AlertBuilder.Builder builder = new AlertBuilder.Builder();
                    builder.setTitle(getString(R.string.word_notice_alert));
                    builder.addContents(new AlertData.MessageData(getString(R.string.msg_alert_alarm_off), AlertBuilder.MESSAGE_TYPE.TEXT, 3));
                    builder.setLeftText(getString(R.string.word_cancel)).setRightText(getString(R.string.word_confirm));
                    builder.setOnAlertResultListener(new OnAlertResultListener(){

                        @Override
                        public void onCancel(){
                            switchCompat.setSafeCheck(true, SafeSwitchCompat.IGNORE);
                        }

                        @Override
                        public void onResult(AlertBuilder.EVENT_ALERT event_alert){

                            switch (event_alert) {
                                case RIGHT:
                                    String pushMask = "0000000000000000";
                                    pushUpdate(pushMask, false);
                                    break;
                                case LEFT:
                                    switchCompat.setSafeCheck(true, SafeSwitchCompat.IGNORE);
                                    break;
                            }

                        }
                    }).builder().show(AlarmContainerActivity.this);
                }
            }
        });
        toolbarOption.setToolbarMenu(ToolbarOption.ToolbarMenu.RIGHT, view, 0);
        return toolbarOption;
    }

    private void pushUpdate(final String pushMask, final boolean pushActivate){

        Map<String, String> params = new HashMap<>();
        params.put("pushActivate", String.valueOf(pushActivate));
        params.put("pushMask", pushMask);

        showProgress("");
        ApiBuilder.create().updatePushConfig(params).setCallback(new PplusCallback<NewResultResponse<Object>>(){

            @Override
            public void onResponse(Call<NewResultResponse<Object>> call, NewResultResponse<Object> response){

                hideProgress();
                LoginInfoManager.getInstance().getUser().getDevice().getInstalledApp().setPushActivate(pushActivate);
                LoginInfoManager.getInstance().getUser().getDevice().getInstalledApp().setPushMask(pushMask);
            }

            @Override
            public void onFailure(Call<NewResultResponse<Object>> call, Throwable t, NewResultResponse<Object> response){

                hideProgress();
            }
        }).build().call();
    }

    @Override
    public OnToolbarListener getOnToolbarClickListener(){

        return new OnToolbarListener(){

            @Override
            public void onClick(View v, ToolbarOption.ToolbarMenu toolbarMenu, Object tag){
                switch (toolbarMenu) {
                    case LEFT:
                        if(tag.equals(1)) {
                            onBackPressed();
                        }
                        break;
                }
            }
        };
    }
}
