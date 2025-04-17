//package com.pplus.prnumberbiz.apps.note.ui;
//
//import android.os.Bundle;
//import android.support.annotation.NonNull;
//import android.text.Editable;
//import android.text.TextWatcher;
//import android.view.View;
//import android.widget.EditText;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//import com.bumptech.glide.Glide;
//import com.bumptech.glide.request.RequestOptions;
//import com.pple.pplus.utils.part.utils.StringUtils;
//import com.pplus.prnumberbiz.Const;
//import com.pplus.prnumberbiz.R;
//import com.pplus.prnumberbiz.apps.common.builder.AlertBuilder;
//import com.pplus.prnumberbiz.apps.common.builder.OnAlertResultListener;
//import com.pplus.prnumberbiz.apps.common.builder.data.AlertData;
//import com.pplus.prnumberbiz.apps.common.toolbar.ImplToolbar;
//import com.pplus.prnumberbiz.apps.common.toolbar.OnToolbarListener;
//import com.pplus.prnumberbiz.apps.common.toolbar.ToolbarOption;
//import com.pplus.prnumberbiz.apps.common.ui.base.BaseActivity;
//import com.pplus.prnumberbiz.core.network.ApiBuilder;
//import com.pplus.prnumberbiz.core.network.model.dto.No;
//import com.pplus.prnumberbiz.core.network.model.dto.NoteReceive;
//import com.pplus.prnumberbiz.core.network.model.request.params.ParamsNoteSend;
//import com.pplus.prnumberbiz.core.network.model.response.NewResultResponse;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import network.common.PplusCallback;
//import retrofit2.Call;
//
//public class NoteReplyActivity extends BaseActivity implements ImplToolbar{
//
//    @Override
//    public String getPID(){
//
//        return null;
//    }
//
//    @Override
//    public int getLayoutRes(){
//
//        return R.layout.activity_note_reply;
//    }
//
//    private NoteReceive mNoteReceive;
//
//    @Override
//    public void initializeView(Bundle savedInstanceState){
//
//        mNoteReceive = getIntent().getParcelableExtra(Const.DATA);
//
//        ImageView image = (ImageView)findViewById(R.id.image_note_reply);
//        TextView text_name = (TextView)findViewById(R.id.text_note_reply_name);
//        final EditText edit_contents = (EditText)findViewById(R.id.edit_note_reply_contents);
//        final TextView text_count = (TextView)findViewById(R.id.text_note_reply_text_count);
//
//        text_name.setText(mNoteReceive.getAuthor().getNickname());
//
//        if(mNoteReceive.getAuthor().getProfileImage() != null) {
//            Glide.with(this).load(mNoteReceive.getAuthor().getProfileImage().getUrl()).apply(new RequestOptions().centerCrop().placeholder(R.drawable.ic_gift_profile_default).error(R.drawable.ic_gift_profile_default)).into(image);
//        }
//
//        edit_contents.addTextChangedListener(new TextWatcher(){
//
//            @Override
//            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2){
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2){
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable editable){
//                text_count.setText(getString(R.string.format_count_per, editable.length(), 500));
//            }
//        });
//
//        findViewById(R.id.text_note_reply).setOnClickListener(new View.OnClickListener(){
//
//            @Override
//            public void onClick(View view){
//                final String contents = edit_contents.getText().toString().trim();
//                if(StringUtils.isEmpty(contents)) {
//                    showAlert(R.string.msg_input_note_contents);
//                    return;
//                }
//
//                AlertBuilder.Builder builder = new AlertBuilder.Builder();
//                builder.setTitle(getString(R.string.word_notice_alert));
//                builder.addContents(new AlertData.MessageData(getString(R.string.msg_question_send_note), AlertBuilder.MESSAGE_TYPE.TEXT, 2));
//                builder.setLeftText(getString(R.string.word_cancel)).setRightText(getString(R.string.word_confirm));
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
//                        switch (event_alert) {
//                            case RIGHT:
//                                ParamsNoteSend params = new ParamsNoteSend();
//                                params.setContents(contents);
//                                params.setOrigin(new No(mNoteReceive.getNo()));
//                                List<No> userList = new ArrayList<>();
//                                userList.add(new No(mNoteReceive.getAuthor().getNo()));
//                                params.setReceiverList(userList);
//                                showProgress("");
//                                ApiBuilder.create().replyNote(params).setCallback(new PplusCallback<NewResultResponse<Object>>(){
//
//                                    @Override
//                                    public void onResponse(Call<NewResultResponse<Object>> call, NewResultResponse<Object> response){
//
//                                        hideProgress();
//                                        showAlert(R.string.msg_sent_note);
//                                        setResult(RESULT_OK);
//                                        finish();
//                                    }
//
//                                    @Override
//                                    public void onFailure(Call<NewResultResponse<Object>> call, Throwable t, NewResultResponse<Object> response){
//
//                                        hideProgress();
//                                    }
//                                }).build().call();
//                                break;
//                        }
//                    }
//                }).builder().show(NoteReplyActivity.this);
//            }
//        });
//    }
//
//    @NonNull
//    @Override
//    public ToolbarOption getToolbarOption(){
//
//        ToolbarOption toolbarOption = new ToolbarOption(this);
//        toolbarOption.initializeDefaultToolbar(getString(R.string.msg_reply), ToolbarOption.ToolbarMenu.LEFT);
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
//}
