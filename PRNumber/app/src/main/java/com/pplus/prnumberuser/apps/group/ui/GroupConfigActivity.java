//package com.pplus.prnumberuser.apps.group.ui;
//
//import android.content.Intent;
//import android.os.Bundle;
//import androidx.annotation.NonNull;
//import androidx.recyclerview.widget.LinearLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//import android.view.View;
//
//import com.h6ah4i.android.widget.advrecyclerview.draggable.RecyclerViewDragDropManager;
//import com.pplus.prnumberuser.Const;
//import com.pplus.prnumberuser.R;
//import com.pplus.prnumberuser.apps.common.listener.OnSortChangedListener;
//import com.pplus.prnumberuser.apps.common.toolbar.ImplToolbar;
//import com.pplus.prnumberuser.apps.common.toolbar.OnToolbarListener;
//import com.pplus.prnumberuser.apps.common.toolbar.ToolbarOption;
//import com.pplus.prnumberuser.apps.common.ui.base.BaseActivity;
//import com.pplus.prnumberuser.apps.group.data.GroupConfigAdapter;
//import com.pplus.prnumberuser.core.network.ApiBuilder;
//import com.pplus.prnumberuser.core.network.model.dto.Group;
//import com.pplus.prnumberuser.core.network.model.response.NewResultResponse;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import com.pplus.networks.common.PplusCallback;
//import retrofit2.Call;
//
///**
// * 플러스 - 그룹관리
// */
//public class GroupConfigActivity extends BaseActivity implements ImplToolbar{
//
//    private String SID = null;
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
//        return R.layout.activity_group_config;
//    }
//
//    private View mTextNotExist;
//    private GroupConfigAdapter mGroupConfigAdapter;
//
//    @Override
//    public void initializeView(Bundle savedInstanceState){
//
//        RecyclerView recyclerGroup = (RecyclerView) findViewById(R.id.recycler_group_config);
//        // Setup D&D feature and RecyclerView
//        RecyclerViewDragDropManager dragMgr = new RecyclerViewDragDropManager();
//
//        dragMgr.setInitiateOnMove(false);
//        dragMgr.setInitiateOnLongPress(true);
//
//        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
//        recyclerGroup.setLayoutManager(layoutManager);
//
//        mTextNotExist = findViewById(R.id.text_group_config_notExist);
//        mTextNotExist.setVisibility(View.GONE);
//
//
//        mGroupConfigAdapter = new GroupConfigAdapter(this);
//        recyclerGroup.setAdapter(dragMgr.createWrappedAdapter(mGroupConfigAdapter));
//        dragMgr.attachRecyclerView(recyclerGroup);
//
//        mGroupConfigAdapter.setOnSortChangedListener(new OnSortChangedListener(){
//
//            @Override
//            public void onChange(){
//                setResult(RESULT_OK);
//            }
//        });
//
//        getPlusGroupAll();
//
//    }
//
//    public void getPlusGroupAll(){
//
//        ApiBuilder.create().getPlusGroupAll().setCallback(new PplusCallback<NewResultResponse<Group>>(){
//
//            @Override
//            public void onResponse(Call<NewResultResponse<Group>> call, NewResultResponse<Group> response){
//                List<Group> groupList = response.getDatas();
//                if(groupList != null && groupList.size() > 0){
//                    for(int i = 0; i < groupList.size(); i++) {
//                        if(groupList.get(i).isDefaultGroup()) {
//                            groupList.remove(i);
//                            break;
//                        }
//                    }
//
//                    mGroupConfigAdapter.setDataList(groupList);
//                }else{
//                    mGroupConfigAdapter.clear();
//                }
//            }
//
//            @Override
//            public void onFailure(Call<NewResultResponse<Group>> call, Throwable t, NewResultResponse<Group> response){
//
//            }
//        }).build().call();
//    }
//
//    @NonNull
//    @Override
//    public ToolbarOption getToolbarOption(){
//
//        ToolbarOption toolbarOption = new ToolbarOption(this);
//        toolbarOption.initializeDefaultToolbar(getString(R.string.word_config_group_title), ToolbarOption.ToolbarMenu.LEFT);
//        toolbarOption.setToolbarMenu(ToolbarOption.ToolbarMenu.RIGHT, getString(R.string.word_plus_newGroup));
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
//                            Intent intent = new Intent(GroupConfigActivity.this, GroupAddActivity.class);
//                            if(mGroupConfigAdapter.getItemCount() > 0){
//                                intent.putExtra(Const.PRIORITY, mGroupConfigAdapter.getItem(0).getPriority());
//                                intent.putExtra(Const.GROUP, (ArrayList<Group>)mGroupConfigAdapter.getDataList());
//                            }
//                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
//                            startActivityForResult(intent, Const.REQ_GROUP_CONFIG);
//                        }
//                        break;
//                }
//            }
//        };
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data){
//
//        super.onActivityResult(requestCode, resultCode, data);
//        if(resultCode == RESULT_OK) {
//            switch (requestCode) {
//                case Const.REQ_GROUP_CONFIG:
//                    setResult(RESULT_OK);
//                    getPlusGroupAll();
//
//                    break;
//            }
//        }
//    }
//}
