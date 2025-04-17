//package com.pplus.prnumberbiz.apps.note.ui;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.support.annotation.NonNull;
//import android.view.View;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//import com.bumptech.glide.Glide;
//import com.bumptech.glide.request.RequestOptions;
//import com.pple.pplus.utils.part.utils.time.DateFormatUtils;
//import com.pplus.prnumberbiz.Const;
//import com.pplus.prnumberbiz.R;
//import com.pplus.prnumberbiz.apps.common.toolbar.ImplToolbar;
//import com.pplus.prnumberbiz.apps.common.toolbar.OnToolbarListener;
//import com.pplus.prnumberbiz.apps.common.toolbar.ToolbarOption;
//import com.pplus.prnumberbiz.apps.common.ui.base.BaseActivity;
//import com.pplus.prnumberbiz.core.network.ApiBuilder;
//import com.pplus.prnumberbiz.core.network.model.dto.NoteReceive;
//import com.pplus.prnumberbiz.core.network.model.dto.NoteSend;
//import com.pplus.prnumberbiz.core.network.model.response.NewResultResponse;
//
//import java.text.SimpleDateFormat;
//import java.util.Date;
//import java.util.HashMap;
//import java.util.Map;
//
//import network.common.PplusCallback;
//import retrofit2.Call;
//
//public class NoteDetailActivity extends BaseActivity implements ImplToolbar{
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
//        return R.layout.activity_note_detail;
//    }
//
//    private NoteReceive mNoteReceive;
//    private TextView text_name, text_contents, text_date, text_reply_contents;
//    private ImageView image;
//
//    @Override
//    public void initializeView(Bundle savedInstanceState){
//
//        image = (ImageView) findViewById(R.id.image_note_detail);
//        text_name = (TextView) findViewById(R.id.text_note_detail_name);
//        text_contents = (TextView) findViewById(R.id.text_note_detail_contents);
//        text_date = (TextView) findViewById(R.id.text_note_detail_date);
//        text_reply_contents = (TextView)findViewById(R.id.text_note_detail_reply_contents);
//
//        findViewById(R.id.text_note_detail_reply).setOnClickListener(new View.OnClickListener(){
//
//            @Override
//            public void onClick(View view){
//
//                Intent intent = new Intent(NoteDetailActivity.this, NoteReplyActivity.class);
//                intent.putExtra(Const.DATA, mNoteReceive);
//                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
//                startActivityForResult(intent, Const.REQ_REPLY);
//            }
//        });
//
//        mNoteReceive = getIntent().getParcelableExtra(Const.DATA);
//        if(mNoteReceive != null){
//            init();
//        }else{
//            Long no = getIntent().getLongExtra(Const.NO, 0);
//            getReceiveNote(no);
//        }
//    }
//
//    private void init(){
//        Map<String, String> params = new HashMap<>();
//        params.put("no", "" + mNoteReceive.getNo());
//        ApiBuilder.create().readNote(params).build().call();
//        text_contents.setText(mNoteReceive.getContents());
//
//        Date d = null;
//        try {
//            d = DateFormatUtils.PPLUS_DATE_FORMAT.parse(mNoteReceive.getRegDate());
//        } catch (Exception e) {
//
//        }
//
//        if(mNoteReceive.getAuthor().getProfileImage() != null) {
//            Glide.with(this).load(mNoteReceive.getAuthor().getProfileImage().getUrl()).apply(new RequestOptions().centerCrop().placeholder(R.drawable.ic_gift_profile_default).error(R.drawable.ic_gift_profile_default)).into(image);
//        } else {
//            image.setImageResource(R.drawable.ic_gift_profile_default);
//        }
//
//        text_name.setText(mNoteReceive.getAuthor().getNickname());
//
//        SimpleDateFormat output = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
//        text_date.setText(output.format(d));
//
//        checkReply();
//    }
//
//    private void checkReply(){
//        if(mNoteReceive.getReplyNo() != null) {
//            getSendNote(mNoteReceive.getReplyNo());
//            text_reply_contents.setVisibility(View.VISIBLE);
//            findViewById(R.id.text_note_detail_reply).setVisibility(View.GONE);
//        }else{
//            text_reply_contents.setVisibility(View.GONE);
//        }
//    }
//
//    private void getReceiveNote(Long no){
//        Map<String, String> params = new HashMap<>();
//        params.put("no", ""+no);
//        showProgress("");
//        ApiBuilder.create().getReceiveNote(params).setCallback(new PplusCallback<NewResultResponse<NoteReceive>>(){
//
//            @Override
//            public void onResponse(Call<NewResultResponse<NoteReceive>> call, NewResultResponse<NoteReceive> response){
//                hideProgress();
//                mNoteReceive = response.getData();
//                init();
//            }
//
//            @Override
//            public void onFailure(Call<NewResultResponse<NoteReceive>> call, Throwable t, NewResultResponse<NoteReceive> response){
//                hideProgress();
//            }
//        }).build().call();
//    }
//
//    private void getSendNote(Long no){
//        Map<String, String> params = new HashMap<>();
//        params.put("no", ""+no);
//        showProgress("");
//        ApiBuilder.create().getSendNote(params).setCallback(new PplusCallback<NewResultResponse<NoteSend>>(){
//
//            @Override
//            public void onResponse(Call<NewResultResponse<NoteSend>> call, NewResultResponse<NoteSend> response){
//                hideProgress();
//                text_reply_contents.setText(response.getData().getContents());
//            }
//
//            @Override
//            public void onFailure(Call<NewResultResponse<NoteSend>> call, Throwable t, NewResultResponse<NoteSend> response){
//
//            }
//        }).build().call();
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data){
//
//        super.onActivityResult(requestCode, resultCode, data);
//        switch (requestCode){
//            case Const.REQ_REPLY:
//                if(resultCode == RESULT_OK){
//                    getReceiveNote(mNoteReceive.getNo());
//                }
//                break;
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
//}
