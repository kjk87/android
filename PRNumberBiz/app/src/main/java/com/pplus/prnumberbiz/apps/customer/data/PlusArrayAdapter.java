//package com.pplus.prnumberbiz.apps.customer.data;
//
//import android.app.Activity;
//import android.content.Context;
//import android.content.Intent;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//import com.bumptech.glide.Glide;
//import com.bumptech.glide.request.RequestOptions;
//import com.pplus.prnumberbiz.Const;
//import com.pplus.prnumberbiz.R;
//import com.pplus.prnumberbiz.apps.common.builder.AlertBuilder;
//import com.pplus.prnumberbiz.apps.common.builder.OnAlertResultListener;
//import com.pplus.prnumberbiz.apps.common.builder.data.AlertData;
//import com.pplus.prnumberbiz.apps.common.ui.base.BaseArrayAdapter;
//import com.pplus.prnumberbiz.apps.customer.ui.CustomerDirectRegActivity;
//import com.pplus.prnumberbiz.core.network.ApiBuilder;
//import com.pplus.prnumberbiz.core.network.model.dto.User;
//import com.pplus.prnumberbiz.core.network.model.response.NewResultResponse;
//
//import java.util.HashMap;
//import java.util.Map;
//
//import network.common.PplusCallback;
//import retrofit2.Call;
//
//
///**
// * Created by j2n on 2016. 12. 22..
// */
//
//public class PlusArrayAdapter extends BaseArrayAdapter<User>{
//
//    public PlusArrayAdapter(Context context){
//
//        super(context, R.layout.item_history);
//    }
//
//    @Override
//    public View getView(int position, View convertView, ViewGroup parent){
//
//        ViewHolder holder;
//
//        if(convertView == null) {
//
//            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user, parent, false);
//            holder = new ViewHolder();
//            holder.initialize(convertView);
//            convertView.setTag(holder);
//        } else {
//            holder = (ViewHolder) convertView.getTag();
//        }
//
//        final User item = getItem(position);
//        holder.text_user_name.setText(item.getName());
//        if(item.getProfileImage() != null) {
//            Glide.with(getContext()).load(item.getProfileImage().getUrl()).apply(new RequestOptions().centerCrop().placeholder(R.drawable.ic_gift_profile_default).error(R.drawable.ic_gift_profile_default)).into(holder.image_user_profileImg);
//        } else {
//            holder.image_user_profileImg.setImageResource(R.drawable.ic_gift_profile_default);
//        }
//        if(item.getPage() != null) {
//            holder.text_user_subInfo.setText(item.getPage().getNumberList().get(0).getNumber());
//        }
//
//        holder.image_user_more.setOnClickListener(new View.OnClickListener(){
//
//            @Override
//            public void onClick(View view){
//
//                AlertBuilder.Builder builder = new AlertBuilder.Builder();
//                builder.setStyle_alert(AlertBuilder.STYLE_ALERT.LIST_BOTTOM);
//                builder.setContents(new String[]{getContext().getString(R.string.word_modified), getContext().getString(R.string.word_delete)});
//                builder.setLeftText(getContext().getString(R.string.word_cancel));
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
//                            case LIST:
//                                Intent intent = null;
//                                switch (event_alert.getValue()) {
//                                    case 1://그룹명변경
//                                        intent = new Intent(getContext(), CustomerDirectRegActivity.class);
//                                        intent.putExtra(Const.DATA, item);
//                                        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                                        ((Activity) getContext()).startActivityForResult(intent, Const.REQ_GROUP_CONFIG);
//                                        break;
//                                    case 2://그룹삭제
//                                        AlertBuilder.Builder builder = new AlertBuilder.Builder();
//                                        builder.setTitle(getContext().getString(R.string.word_notice_alert));
//                                        builder.setStyle_alert(AlertBuilder.STYLE_ALERT.MESSAGE);
//                                        builder.addContents(new AlertData.MessageData(getContext().getString(R.string.format_msg_delete_group2, item.getName()), AlertBuilder.MESSAGE_TYPE.TEXT, 1));
//                                        builder.setLeftText(getContext().getString(R.string.word_cancel)).setRightText(getContext().getString(R.string.word_confirm));
//                                        builder.setOnAlertResultListener(new OnAlertResultListener(){
//
//                                            @Override
//                                            public void onCancel(){
//
//                                            }
//
//                                            @Override
//                                            public void onResult(AlertBuilder.EVENT_ALERT event_alert){
//
//                                                switch (event_alert) {
//                                                    case RIGHT://그룹삭제
//                                                        Map<String, String> params = new HashMap<String, String>();
//                                                        params.put("no", "" + item.getNo());
//                                                        ApiBuilder.create().deleteFanGroup(params).setCallback(new PplusCallback<NewResultResponse<Object>>(){
//
//                                                            @Override
//                                                            public void onResponse(Call<NewResultResponse<Object>> call, NewResultResponse<Object> response){
//
//                                                            }
//
//                                                            @Override
//                                                            public void onFailure(Call<NewResultResponse<Object>> call, Throwable t, NewResultResponse<Object> response){
//
//                                                            }
//                                                        }).build().call();
//                                                        break;
//                                                }
//                                            }
//                                        }).builder().show(getContext());
//                                        break;
//                                }
//                                break;
//                        }
//                    }
//                }).builder().show(getContext());
//            }
//        });
//
//        return convertView;
//    }
//
//    class ViewHolder{
//
//        public ImageView image_user_profileImg, image_user_checkbox;
//        public TextView text_user_name, text_user_subInfo;
//        public View layout_user_customer, text_user_recommend, image_user_more;
//        public Context context;
//
//        public void initialize(View itemView){
//
//            context = itemView.getContext();
//            image_user_profileImg = (ImageView) itemView.findViewById(R.id.image_user_profileImg);
//            text_user_name = (TextView) itemView.findViewById(R.id.text_user_name);
//            text_user_subInfo = (TextView) itemView.findViewById(R.id.text_user_subInfo);
//            image_user_checkbox = (ImageView) itemView.findViewById(R.id.image_user_checkbox);
//            image_user_checkbox.setVisibility(View.GONE);
//            layout_user_customer = itemView.findViewById(R.id.layout_user_customer);
//            layout_user_customer.setVisibility(View.VISIBLE);
//            text_user_recommend = itemView.findViewById(R.id.text_user_recommend);
//            text_user_recommend.setVisibility(View.GONE);
//            image_user_more = itemView.findViewById(R.id.image_user_more);
//            text_user_recommend.setVisibility(View.GONE);
//        }
//    }
//}
