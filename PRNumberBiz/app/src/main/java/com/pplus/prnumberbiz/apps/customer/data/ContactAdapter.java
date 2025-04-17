package com.pplus.prnumberbiz.apps.customer.data;//package com.pple.prnumber.apps.customer.data;
//
//import android.content.Context;
//import android.content.Intent;
//import android.net.Uri;
//import android.support.v4.content.ContextCompat;
//import android.support.v7.widget.RecyclerView;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//import com.bumptech.glide.Glide;
//import com.pple.pplus.utils.part.apps.permission.Permission;
//import com.pple.pplus.utils.part.apps.permission.PermissionListener;
//import com.pple.pplus.utils.part.pref.PreferenceUtil;
//import com.pple.pplus.utils.part.utils.StringUtils;
//import com.pple.prnumber.Const;
//import com.pple.prnumber.PRNumberBizApplication;
//import com.pple.prnumber.R;
//import com.pple.prnumber.apps.common.builder.AlertBuilder;
//import com.pple.prnumber.apps.common.builder.OnAlertResultListener;
//import com.pple.prnumber.apps.common.builder.PPlusPermission;
//import com.pple.prnumber.apps.common.builder.data.AlertData;
//import com.pple.prnumber.apps.common.mgmt.UserInfoManager;
//import com.pple.prnumber.apps.common.ui.custom.SectionedRecyclerViewAdapter;
//import com.pple.prnumber.apps.customer.ui.ContactInviteActivity;
//import com.pple.prnumber.core.database.entity.Contact;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//
///**
// * Created by 김종경 on 2016-08-24.
// */
//public class ContactAdapter extends SectionedRecyclerViewAdapter<ContactAdapter.ViewHeaderHolder, ContactAdapter.ViewHolder>{
//
//    private Context mContext;
//
//    private List<String> mHeaderList;
//    private HashMap<Integer, List<Contact>> mChildMap;
//    private int[] colorList = {R.color.color_f8cb88, R.color.color_ed9696, R.color.color_b3c6cf, R.color.color_bda0ef};
//
//    public ContactAdapter(Context context){
//
//        super();
//        mContext = context;
//        mHeaderList = new ArrayList<>();
//        mChildMap = new HashMap<>();
//    }
//
//    public void setDataList(List<String> headerList, HashMap<Integer, List<Contact>> childMap){
//
//        this.mHeaderList = headerList;
//        this.mChildMap = childMap;
//        notifyDataSetChanged();
//    }
//
//    public void clear(){
//
//        mHeaderList = new ArrayList<>();
//        mChildMap = new HashMap<>();
//        notifyDataSetChanged();
//    }
//
//    @Override
//    public int getSectionCount(){
//
//        return mHeaderList.size();
//    }
//
//    @Override
//    public int getItemCount(int section){
//
//        return mChildMap.get(section).size();
//    }
//
//    @Override
//    public void onBindHeaderViewHolder(ViewHeaderHolder holder, int section){
//
//        if(holder.textHeaderName != null) {
//            holder.textHeaderName.setText(String.format(mHeaderList.get(section), mChildMap.get(section).size()));
//        }
//
//    }
//
//    @Override
//    public void onBindViewHolder(ViewHolder holder, final int section, int relativePosition, int absolutePosition){
//
//        final Contact item = mChildMap.get(section).get(relativePosition);
//        holder.textName.setText(item.getMemberName());
//
//        if(section == 0) {
//            holder.imageProfile.setVisibility(View.VISIBLE);
//            holder.textFirst.setVisibility(View.GONE);
//            holder.text_prName.setVisibility(View.VISIBLE);
//            if(item.getPageSeqNo() != null && item.getPageSeqNo() > 0) {
//                holder.imagePr.setVisibility(View.VISIBLE);
//                if(StringUtils.isNotEmpty(item.getVirtualNumber())){
//                    holder.text_prnumber.setVisibility(View.VISIBLE);
//                    holder.view_bar.setVisibility(View.VISIBLE);
//                    holder.text_prnumber.setText(item.getVirtualNumber()+"#");
//                }else{
//                    holder.text_prnumber.setVisibility(View.GONE);
//                    holder.view_bar.setVisibility(View.GONE);
//                }
//
//            } else {
//                holder.imagePr.setVisibility(View.GONE);
//                holder.text_prnumber.setVisibility(View.GONE);
//                holder.view_bar.setVisibility(View.GONE);
//
//            }
//            holder.imageInvite.setVisibility(View.GONE);
//            holder.imageCall.setVisibility(View.VISIBLE);
//            holder.text_prName.setText(item.getMemberNickname());
//            Glide.with(mContext).load(item.getImageUrl()).centerCrop().placeholder(R.drawable.ic_gift_profile_default).error(R.drawable.ic_gift_profile_default).into(holder.imageProfile);
//
//        } else {
//            holder.imageProfile.setVisibility(View.GONE);
//            holder.textFirst.setVisibility(View.VISIBLE);
//            holder.imagePr.setVisibility(View.GONE);
//            holder.text_prName.setVisibility(View.GONE);
//            holder.text_prnumber.setVisibility(View.GONE);
//            holder.view_bar.setVisibility(View.GONE);
//            holder.imageCall.setVisibility(View.VISIBLE);
//            holder.imageInvite.setVisibility(View.VISIBLE);
//            holder.textFirst.setText(item.getMemberName().substring(0, 1));
//
//            holder.textFirst.setBackgroundColor(ContextCompat.getColor(mContext, colorList[relativePosition % colorList.length]));
//        }
//
//        holder.imageCall.setOnClickListener(new View.OnClickListener(){
//
//            @Override
//            public void onClick(View v){
//
//                boolean isDirectCall = PreferenceUtil.getDefaultPreference(mContext).get(Const.CALL, false);
//
//                if(isDirectCall) {
//                    PPlusPermission pPlusPermission = new PPlusPermission(PRNumberBizApplication.getCurrentActivity());
//                    pPlusPermission.addPermission(Permission.PERMISSION_KEY.CONTACTS);
//                    pPlusPermission.setPermissionListener(new PermissionListener(){
//
//                        @Override
//                        public void onPermissionGranted(){
//
//                            Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + item.getMobileNumber()));
//                            mContext.startActivity(intent);
//                        }
//
//                        @Override
//                        public void onPermissionDenied(ArrayList<String> deniedPermissions){
//
//                        }
//                    });
//                    pPlusPermission.checkPermission();
//
//                    return;
//                }
//                AlertBuilder.Builder builder = new AlertBuilder.Builder();
//                builder.setTitle(mContext.getString(R.string.word_alim));
//                builder.addContents(new AlertData.MessageData(mContext.getString(R.string.msg_direct_call), AlertBuilder.MESSAGE_TYPE.TEXT, 2));
//                builder.setLeftText(mContext.getString(R.string.msg_do_not_look_again)).setRightText(mContext.getString(R.string.word_confirm));
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
//                            case LEFT:
//                                PreferenceUtil.getDefaultPreference(mContext).put(Const.CALL, true);
//                                break;
//                            case RIGHT:
//                                break;
//                        }
//                        PPlusPermission pPlusPermission = new PPlusPermission(PRNumberBizApplication.getCurrentActivity());
//                        pPlusPermission.addPermission(Permission.PERMISSION_KEY.CONTACTS);
//                        pPlusPermission.setPermissionListener(new PermissionListener(){
//
//                            @Override
//                            public void onPermissionGranted(){
//
//                                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + item.getMobileNumber()));
//                                mContext.startActivity(intent);
//                            }
//
//                            @Override
//                            public void onPermissionDenied(ArrayList<String> deniedPermissions){
//
//                            }
//                        });
//                        pPlusPermission.checkPermission();
//
//                    }
//                }).builder().show(mContext);
//
//            }
//        });
//
//        holder.imageInvite.setOnClickListener(new View.OnClickListener(){
//
//            @Override
//            public void onClick(View v){
//
//                if(UserInfoManager.getInstance().isMember() && UserInfoManager.getInstance().getPage() != null && !UserInfoManager.getInstance().getPage().isEmpty()) {
//                    Intent intent = new Intent(mContext, ContactInviteActivity.class);
//                    intent.putExtra(Const.MOBILE_NUMBER, item.getMobileNumber());
//                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
//                    mContext.startActivity(intent);
//                }
//
//            }
//        });
//
//        holder.itemView.setOnClickListener(new View.OnClickListener(){
//
//            @Override
//            public void onClick(View v){
//
//            }
//        });
//
//    }
//
//    @Override
//    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
//
//        switch (viewType) {
//            case VIEW_TYPE_HEADER:
//                return new ViewHeaderHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_contact_header, parent, false));
//            case VIEW_TYPE_ITEM:
//                return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_contact, parent, false));
//
//        }
//        return null;
//    }
//
//    static class ViewHolder extends RecyclerView.ViewHolder{
//
//        public ImageView imageProfile;
//        public TextView textFirst, textName, text_prnumber, text_prName;
//        public View imageInvite, imageCall, imagePr, view_bar;
//
//        public ViewHolder(View itemView){
//
//            super(itemView);
//            imageProfile = (ImageView) itemView.findViewById(R.id.image_contact_profile);
//            textFirst = (TextView) itemView.findViewById(R.id.text_contact_first);
//            textName = (TextView) itemView.findViewById(R.id.text_contact_name);
//            imagePr = itemView.findViewById(R.id.image_contact_pr);
//            imageInvite = itemView.findViewById(R.id.image_contact_invite);
//            imageCall = itemView.findViewById(R.id.image_contact_call);
//            text_prnumber = (TextView)itemView.findViewById(R.id.text_contact_prnumber);
//            text_prName = (TextView)itemView.findViewById(R.id.text_contact_prName);
//            view_bar = itemView.findViewById(R.id.view_contact_bar);
//        }
//
//    }
//
//    static class ViewHeaderHolder extends RecyclerView.ViewHolder{
//
//        public TextView textHeaderName;
//
//        public ViewHeaderHolder(View itemView){
//
//            super(itemView);
//            textHeaderName = (TextView) itemView.findViewById(R.id.text_contact_headerName);
//
//        }
//    }
//
//
//}
