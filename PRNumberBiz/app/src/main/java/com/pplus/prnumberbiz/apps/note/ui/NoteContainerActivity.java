//package com.pplus.prnumberbiz.apps.note.ui;
//
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.support.annotation.NonNull;
//import android.support.v7.widget.LinearLayoutManager;
//import android.support.v7.widget.RecyclerView;
//import android.view.View;
//
//import com.pplus.prnumberbiz.Const;
//import com.pplus.prnumberbiz.R;
//import com.pplus.prnumberbiz.apps.common.mgmt.LoginInfoManager;
//import com.pplus.prnumberbiz.apps.common.toolbar.ImplToolbar;
//import com.pplus.prnumberbiz.apps.common.toolbar.OnToolbarListener;
//import com.pplus.prnumberbiz.apps.common.toolbar.ToolbarOption;
//import com.pplus.prnumberbiz.apps.common.ui.base.BaseActivity;
//import com.pplus.prnumberbiz.apps.note.data.NoteContainerAdapter;
//import com.pplus.prnumberbiz.apps.note.data.NoteReceiveAdapter;
//import com.pplus.prnumberbiz.core.network.ApiBuilder;
//import com.pplus.prnumberbiz.core.network.model.dto.NoteReceive;
//import com.pplus.prnumberbiz.core.network.model.response.NewResultResponse;
//import com.pplus.prnumberbiz.core.util.PplusCommonUtil;
//
//import java.util.HashMap;
//import java.util.Map;
//
//import network.common.PplusCallback;
//import retrofit2.Call;
//
///**
// * 대화방 리스트
// */
//public class NoteContainerActivity extends BaseActivity implements ImplToolbar, View.OnClickListener{
//
//    private LinearLayoutManager mLayoutManager;
//    private NoteReceiveAdapter mAdapter;
//
//    @Override
//    public String getPID(){
//
//        return "";
//    }
//
//    @Override
//    public int getLayoutRes(){
//
//        return R.layout.activity_note_container;
//    }
//
//    private boolean mLockListView = true;
//    private int mTotalCount = 0, mPage = 0;
//
//    @Override
//    public void initializeView(Bundle savedInstanceState){
//
//        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_note_container);
//        mLayoutManager = new LinearLayoutManager(this);
//        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
//        recyclerView.setLayoutManager(mLayoutManager);
//        mAdapter = new NoteReceiveAdapter(this);
//        recyclerView.setAdapter(mAdapter);
//
//        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener(){
//
//            int pastVisibleItems, visibleItemCount, totalItemCount;
//
//            @Override
//            public void onScrolled(RecyclerView recyclerView, int dx, int dy){
//
//                super.onScrolled(recyclerView, dx, dy);
//                visibleItemCount = mLayoutManager.getChildCount();
//                totalItemCount = mLayoutManager.getItemCount();
//                pastVisibleItems = mLayoutManager.findFirstVisibleItemPosition();
//                if(!mLockListView) {
//                    if((totalItemCount < mTotalCount) && (visibleItemCount + pastVisibleItems) >= (totalItemCount * Const.NEXT_RECYCLERVIEW_MARGIN)) {
//                        mPage++;
//                        listCall(mPage);
//                    }
//                }
//            }
//        });
//
//        mAdapter.setOnItemClickListener(new NoteReceiveAdapter.OnItemClickListener(){
//
//            @Override
//            public void onItemClick(int position){
//
//                Intent intent = new Intent(NoteContainerActivity.this, NoteDetailActivity.class);
//                intent.putExtra(Const.DATA, mAdapter.getItem(position));
//                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
//                startActivityForResult(intent, Const.REQ_DETAIL);
//            }
//        });
//
//        getCount();
//    }
//
//    private void getCount(){
//
//        Map<String, String> params = new HashMap<>();
//        ApiBuilder.create().getReceiveNoteCount(params).setCallback(new PplusCallback<NewResultResponse<Integer>>(){
//
//            @Override
//            public void onResponse(Call<NewResultResponse<Integer>> call, NewResultResponse<Integer> response){
//
//                hideProgress();
//                mLockListView = false;
//                mTotalCount = response.getData();
//                mAdapter.clear();
//                mPage = 1;
//                listCall(mPage);
//            }
//
//            @Override
//            public void onFailure(Call<NewResultResponse<Integer>> call, Throwable t, NewResultResponse<Integer> response){
//
//            }
//        }).build().call();
//    }
//
//    private void listCall(int page){
//
//        mLockListView = true;
//        Map<String, String> params = new HashMap<>();
//        params.put("pg", "" + page);
//        showProgress("");
//        ApiBuilder.create().getReceiveNoteList(params).setCallback(new PplusCallback<NewResultResponse<NoteReceive>>(){
//
//            @Override
//            public void onResponse(Call<NewResultResponse<NoteReceive>> call, NewResultResponse<NoteReceive> response){
//
//                if(LoginInfoManager.getInstance().getUser().getProperties() != null && LoginInfoManager.getInstance().getUser().getProperties().isExistsNewNote()){
//                    PplusCommonUtil.Companion.getSession(null);
//                }
//
//                mLockListView = false;
//                hideProgress();
//
//                mAdapter.addAll(response.getDatas());
//            }
//
//            @Override
//            public void onFailure(Call<NewResultResponse<NoteReceive>> call, Throwable t, NewResultResponse<NoteReceive> response){
//
//                hideProgress();
//            }
//        }).build().call();
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data){
//
//        super.onActivityResult(requestCode, resultCode, data);
//        switch (requestCode){
//            case Const.REQ_DETAIL:
//                getCount();
//                break;
//        }
//    }
//
//    @Override
//    public void onClick(View view){
//
//        switch (view.getId()) {
//        }
//    }
//
//    @NonNull
//    @Override
//    public ToolbarOption getToolbarOption(){
//
//        ToolbarOption toolbarOption = new ToolbarOption(this);
//        toolbarOption.initializeDefaultToolbar(getString(R.string.word_message_container), ToolbarOption.ToolbarMenu.LEFT);
//
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
//
//                switch (toolbarMenu) {
//                    case LEFT:
//                        onBackPressed();
//                        break;
//                }
//            }
//        };
//
//    }
//
//    @Override
//    protected void onSaveInstanceState(Bundle outState){
//
//        super.onSaveInstanceState(outState);
//        if(mAdapter != null) {
//            mAdapter.saveStates(outState);
//        }
//    }
//
//    @Override
//    protected void onRestoreInstanceState(Bundle savedInstanceState){
//
//        super.onRestoreInstanceState(savedInstanceState);
//        if(mAdapter != null) {
//            mAdapter.restoreStates(savedInstanceState);
//        }
//    }
//
//}
