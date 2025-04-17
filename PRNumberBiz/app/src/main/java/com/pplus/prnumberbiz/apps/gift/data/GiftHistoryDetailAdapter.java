package com.pplus.prnumberbiz.apps.gift.data;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.pple.pplus.utils.part.utils.StringUtils;
import com.pplus.prnumberbiz.R;
import com.pplus.prnumberbiz.apps.common.ui.base.BaseArrayAdapter;
import com.pplus.prnumberbiz.core.network.model.dto.User;


/**
 * Created by j2n on 2016. 12. 22..
 */

public class GiftHistoryDetailAdapter extends BaseArrayAdapter<User>{

    public GiftHistoryDetailAdapter(Context context){

        super(context, R.layout.item_history);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){

        ViewHolder holder;

        if(convertView == null) {

            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user, parent, false);
            holder = new ViewHolder();
            holder.initialize(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final User item = getItem(position);
        if(item.getProfileImage() != null) {
            Glide.with(getContext()).load(item.getProfileImage().getUrl()).apply(new RequestOptions().centerCrop().placeholder(R.drawable.ic_gift_profile_default).error(R.drawable.ic_gift_profile_default)).into(holder.image_user_profileImg);
        } else {
            holder.image_user_profileImg.setImageResource(R.drawable.ic_gift_profile_default);
        }

        if(StringUtils.isNotEmpty(item.getNickname())) {
            holder.text_user_name.setText(item.getNickname());
            holder.text_user_name.setVisibility(View.VISIBLE);
        } else {
            holder.text_user_name.setVisibility(View.GONE);
        }

        holder.text_user_subInfo.setVisibility(View.GONE);
        return convertView;
    }

    class ViewHolder{

        public ImageView image_user_profileImg;
        public TextView text_user_name, text_user_subInfo;
        public Context context;

        public void initialize(View itemView){

            context = itemView.getContext();
            image_user_profileImg = (ImageView) itemView.findViewById(R.id.image_user_profileImg);
            text_user_name = (TextView) itemView.findViewById(R.id.text_user_name);
            text_user_subInfo = (TextView) itemView.findViewById(R.id.text_user_subInfo);
        }
    }
}
