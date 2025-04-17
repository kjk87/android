package com.pplus.prnumberbiz.apps.customer.data;

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
import com.pplus.prnumberbiz.core.network.model.dto.Customer;

import java.util.ArrayList;


/**
 * Created by j2n on 2016. 12. 22..
 */

public class SelectMyCustomerAdapter extends BaseArrayAdapter<Customer>{

    private ArrayList<Customer> mSelectList;

    public SelectMyCustomerAdapter(Context context){

        super(context, R.layout.item_history);
        mSelectList = new ArrayList<>();
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

        final Customer item = getItem(position);

        holder.text_user_name.setText(item.getName());
        if(item.getTarget() != null && item.getTarget().getProfileImage() != null) {
            Glide.with(getContext()).load(item.getTarget().getProfileImage().getUrl()).apply(new RequestOptions().centerCrop().placeholder(R.drawable.ic_gift_profile_default).error(R.drawable.ic_gift_profile_default)).into(holder.image_user_profileImg);
        } else {
            holder.image_user_profileImg.setImageResource(R.drawable.ic_gift_profile_default);
        }

        holder.text_user_subInfo.setText(item.getMobile());

        holder.image_user_checkbox.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view){
                if(mSelectList.contains(item)){
                    mSelectList.remove(item);
                }else{
                    mSelectList.add(item);
                }

                notifyDataSetChanged();
            }
        });

        holder.image_user_checkbox.setSelected(mSelectList.contains(item));

        return convertView;
    }

    class ViewHolder{

        public ImageView image_user_profileImg, image_user_checkbox;
        public TextView text_user_name, text_user_subInfo;
        public Context context;

        public void initialize(View itemView){

            context = itemView.getContext();
            image_user_profileImg = (ImageView) itemView.findViewById(R.id.image_user_profileImg);
            text_user_name = (TextView) itemView.findViewById(R.id.text_user_name);
            text_user_subInfo = (TextView) itemView.findViewById(R.id.text_user_subInfo);
            image_user_checkbox = (ImageView) itemView.findViewById(R.id.image_user_checkbox);
            image_user_checkbox.setVisibility(View.VISIBLE);
        }
    }
}
