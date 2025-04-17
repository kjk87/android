package com.pplus.prnumberbiz.apps.post.ui;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;

import com.pple.pplus.utils.part.logs.LogUtil;
import com.pplus.prnumberbiz.Const;
import com.pplus.prnumberbiz.R;
import com.pplus.prnumberbiz.apps.common.toolbar.ImplToolbar;
import com.pplus.prnumberbiz.apps.common.toolbar.OnToolbarListener;
import com.pplus.prnumberbiz.apps.common.toolbar.ToolbarOption;
import com.pplus.prnumberbiz.apps.common.ui.base.BaseActivity;
import com.pplus.prnumberbiz.apps.post.data.ReplyAdapter;
import com.pplus.prnumberbiz.core.network.ApiBuilder;
import com.pplus.prnumberbiz.core.network.model.dto.Comment;
import com.pplus.prnumberbiz.core.network.model.dto.Post;
import com.pplus.prnumberbiz.core.network.model.response.NewResultResponse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import network.common.PplusCallback;
import retrofit2.Call;

public class ReplyActivity extends BaseActivity implements View.OnClickListener, ImplToolbar{

    private Comment mComment;
    private ArrayList<Comment> mChildList;
    private EditText edit_reply;

    @Override
    public String getPID(){

        return null;
    }

    @Override
    public int getLayoutRes(){

        return R.layout.activity_reply;
    }

    private ReplyAdapter mAdapter;
    private Long mNo;
    View not_exist;

    @Override
    public void initializeView(Bundle savedInstanceState){

        mNo = getIntent().getLongExtra(Const.POST_NO, -1);

        ListView list = (ListView) findViewById(R.id.list_reply);

        mAdapter = new ReplyAdapter(this);
        list.setAdapter(mAdapter);
        edit_reply = (EditText) findViewById(R.id.edit_reply);

        not_exist = findViewById(R.id.text_reply_not_exist);
        not_exist.setVisibility(View.GONE);

        getComment();
    }

    private void getComment(){

        showProgress("");
        ApiBuilder.create().getCommentAll(mNo).setCallback(new PplusCallback<NewResultResponse<Comment>>(){

            @Override
            public void onResponse(Call<NewResultResponse<Comment>> call, NewResultResponse<Comment> response){

                hideProgress();
                if(response.getDatas() != null) {

                    if(response.getDatas().size() > 0) {
                        not_exist.setVisibility(View.GONE);
                    } else {
                        not_exist.setVisibility(View.VISIBLE);
                    }

                    List<Comment> commentList = response.getDatas();
                    List<Comment> pCommentList = new ArrayList<>();
                    Map<Long, ArrayList<Comment>> cCommentList = new HashMap<Long, ArrayList<Comment>>();
                    for(Comment comment : commentList) {
                        Post post = new Post();
                        post.setNo(mNo);
                        comment.setPost(post);
                        if(comment.getDepth() == 1) {
                            pCommentList.add(comment);
                        } else {
                            LogUtil.e(LOG_TAG, "comment : {}", comment.toString());
                            if(cCommentList.get(comment.getParent().getNo()) == null) {
                                ArrayList<Comment> childList = new ArrayList<>();
                                childList.add(comment);
                                cCommentList.put(comment.getParent().getNo(), childList);
                            } else {
                                cCommentList.get(comment.getParent().getNo()).add(comment);
                            }
                        }
                    }


                    mAdapter.clear();
                    mAdapter.setcCommentList(cCommentList);
                    mAdapter.addAll(pCommentList);
                }

            }

            @Override
            public void onFailure(Call<NewResultResponse<Comment>> call, Throwable t, NewResultResponse<Comment> response){

                hideProgress();
            }
        }).build().call();
    }

    public void deleteComment(Long no){

        showProgress("");
        ApiBuilder.create().deleteComment(no).setCallback(new PplusCallback<NewResultResponse<Object>>(){

            @Override
            public void onResponse(Call<NewResultResponse<Object>> call, NewResultResponse<Object> response){

                hideProgress();
                getComment();
            }

            @Override
            public void onFailure(Call<NewResultResponse<Object>> call, Throwable t, NewResultResponse<Object> response){

                hideProgress();
            }
        }).build().call();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){

        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case Const.REQ_REPLY:
                getComment();
                break;
        }
    }

    @Override
    public void onClick(View view){

    }

    @NonNull
    @Override
    public ToolbarOption getToolbarOption(){

        ToolbarOption toolbarOption = new ToolbarOption(this);
        toolbarOption.initializeDefaultToolbar(getString(R.string.word_reply), ToolbarOption.ToolbarMenu.LEFT);
        return toolbarOption;
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
