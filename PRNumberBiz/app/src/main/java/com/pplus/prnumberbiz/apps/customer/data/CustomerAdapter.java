//package com.pplus.prnumberbiz.apps.customer.data;
//
//import android.app.Activity;
//import android.content.Context;
//import android.content.Intent;
//import android.support.v7.widget.RecyclerView;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
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
//import com.pplus.prnumberbiz.apps.common.mgmt.LoginInfoManager;
//import com.pplus.prnumberbiz.apps.common.ui.base.BaseActivity;
//import com.pplus.prnumberbiz.apps.customer.ui.CustomerActivity;
//import com.pplus.prnumberbiz.apps.customer.ui.CustomerDirectRegActivity;
//import com.pplus.prnumberbiz.apps.customer.ui.RecommendActivity;
//import com.pplus.prnumberbiz.core.network.ApiBuilder;
//import com.pplus.prnumberbiz.core.network.model.dto.Customer;
//import com.pplus.prnumberbiz.core.network.model.response.NewResultResponse;
//import com.pplus.prnumberbiz.core.util.ToastUtil;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//import network.common.PplusCallback;
//import retrofit2.Call;
//
//
///**
// * Created by 김종경 on 2015-06-17.
// */
//public class CustomerAdapter extends RecyclerView.Adapter<CustomerAdapter.ViewHolder>{
//
//    private Context mContext;
//    private List<Customer> mDataList;
//    private OnItemClickListener listener;
//
//    public interface OnItemClickListener{
//
//        void onItemClick(int position);
//    }
//
//    public CustomerAdapter(Context context){
//
//        setHasStableIds(true);
//        mContext = context;
//        mDataList = new ArrayList<>();
//    }
//
//    @Override
//    public long getItemId(int position){
//
//        return mDataList.get(position).getNo();
//    }
//
//    public void setOnItemClickListener(OnItemClickListener listener){
//
//        this.listener = listener;
//    }
//
//    public Customer getItem(int position){
//
//        return mDataList.get(position);
//    }
//
//    public List<Customer> getDataList(){
//
//        return mDataList;
//    }
//
//    public void add(Customer data){
//
//        if(mDataList == null) {
//            mDataList = new ArrayList<>();
//        }
//        mDataList.add(data);
//        notifyDataSetChanged();
//    }
//
//    public void addAll(List<Customer> dataList){
//
//        if(this.mDataList == null) {
//            this.mDataList = new ArrayList<>();
//        }
//
//        this.mDataList.addAll(dataList);
//        notifyDataSetChanged();
//    }
//
//    public void replaceData(int position, Customer data){
//
//        mDataList.remove(position);
//        mDataList.add(position, data);
//        notifyDataSetChanged();
//    }
//
//    public void clear(){
//
//        mDataList = new ArrayList<>();
//        notifyDataSetChanged();
//    }
//
//    public void setDataList(List<Customer> dataList){
//
//        this.mDataList = dataList;
//        notifyDataSetChanged();
//    }
//
//    static class ViewHolder extends RecyclerView.ViewHolder{
//
//        public ImageView image_user_profileImg, image_user_checkbox;
//        public TextView text_user_name, text_user_subInfo;
//        public View layout_user_customer, text_user_recommend, image_user_more;
//
//        public ViewHolder(View itemView){
//
//            super(itemView);
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
//        }
//    }
//
//    @Override
//    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
//
//        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user, parent, false);
//        return new ViewHolder(v);
//    }
//
//    @Override
//    public void onBindViewHolder(final ViewHolder holder, int position){
//
//        final Customer item = getItem(position);
//        holder.text_user_name.setText(item.getName());
//        if(item.getTarget() != null && item.getTarget().getProfileImage() != null) {
//            Glide.with(mContext).load(item.getTarget().getProfileImage().getUrl()).apply(new RequestOptions().centerCrop().placeholder(R.drawable.ic_gift_profile_default).error(R.drawable.ic_gift_profile_default)).into(holder.image_user_profileImg);
//        } else {
//            holder.image_user_profileImg.setImageResource(R.drawable.ic_gift_profile_default);
//        }
//        holder.text_user_subInfo.setText(item.getMobile());
//
////        if(item.getTarget() == null) {
////            holder.text_user_recommend.setVisibility(View.VISIBLE);
////        } else {
////            holder.text_user_recommend.setVisibility(View.GONE);
////        }
//
//        holder.text_user_recommend.setOnClickListener(new View.OnClickListener(){
//
//            @Override
//            public void onClick(View view){
//
//                Intent intent = new Intent(mContext, RecommendActivity.class);
//                intent.putExtra(Const.MOBILE_NUMBER, item.getMobile());
//                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                mContext.startActivity(intent);
//            }
//        });
//
//        holder.image_user_more.setOnClickListener(new View.OnClickListener(){
//
//            @Override
//            public void onClick(View view){
//
//                AlertBuilder.Builder builder = new AlertBuilder.Builder();
//                builder.setStyle_alert(AlertBuilder.STYLE_ALERT.LIST_BOTTOM);
//                builder.setContents(new String[]{mContext.getString(R.string.word_modified), mContext.getString(R.string.word_delete)});
//                builder.setLeftText(mContext.getString(R.string.word_cancel));
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
//                                    case 1:
//                                        intent = new Intent(mContext, CustomerDirectRegActivity.class);
//                                        intent.putExtra(Const.DATA, item);
//                                        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                                        ((Activity) mContext).startActivityForResult(intent, Const.REQ_GROUP_CONFIG);
//                                        break;
//                                    case 2:
//                                        AlertBuilder.Builder builder = new AlertBuilder.Builder();
//                                        builder.setTitle(mContext.getString(R.string.word_notice_alert));
//                                        builder.setStyle_alert(AlertBuilder.STYLE_ALERT.MESSAGE);
//                                        builder.addContents(new AlertData.MessageData(mContext.getString(R.string.format_msg_delete_group2, item.getName()), AlertBuilder.MESSAGE_TYPE.TEXT, 1));
//                                        builder.setLeftText(mContext.getString(R.string.word_cancel)).setRightText(mContext.getString(R.string.word_confirm));
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
//                                                        params.put("page.no", "" + LoginInfoManager.getInstance().getUser().getPage().getNo());
//                                                        ((BaseActivity) mContext).showProgress("");
//                                                        ApiBuilder.create().deleteCustomer(params).setCallback(new PplusCallback<NewResultResponse<Object>>(){
//
//                                                            @Override
//                                                            public void onResponse(Call<NewResultResponse<Object>> call, NewResultResponse<Object> response){
//                                                                ((BaseActivity) mContext).hideProgress();
//                                                                ToastUtil.showAlert(mContext, R.string.msg_delete_complete);
//                                                                ((CustomerActivity) mContext).getGroupAll();
//                                                            }
//
//                                                            @Override
//                                                            public void onFailure(Call<NewResultResponse<Object>> call, Throwable t, NewResultResponse<Object> response){
//                                                                ((BaseActivity) mContext).hideProgress();
//                                                            }
//                                                        }).build().call();
//                                                        break;
//                                                }
//                                            }
//                                        }).builder().show(mContext);
//                                        break;
//                                }
//                                break;
//                        }
//                    }
//                }).builder().show(mContext);
//            }
//        });
//
//        holder.itemView.setOnClickListener(new View.OnClickListener(){
//
//            @Override
//            public void onClick(View view){
//
//                if(listener != null) {
//                    listener.onItemClick(holder.getAdapterPosition());
//                }
//            }
//        });
//    }
//
//    @Override
//    public int getItemCount(){
//
//        return mDataList.size();
//    }
//}
