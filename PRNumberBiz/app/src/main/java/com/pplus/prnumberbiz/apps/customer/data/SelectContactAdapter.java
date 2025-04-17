package com.pplus.prnumberbiz.apps.customer.data;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.pplus.prnumberbiz.R;
import com.pplus.prnumberbiz.apps.common.ui.base.BaseArrayAdapter;
import com.pplus.prnumberbiz.apps.customer.ui.SelectContactActivity;
import com.pplus.prnumberbiz.core.database.entity.Contact;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by j2n on 2016. 12. 22..
 */

public class SelectContactAdapter extends BaseArrayAdapter<Contact>{

    private ArrayList<Contact> mSelectList;

    public SelectContactAdapter(Context context){

        super(context, R.layout.item_history);
        mSelectList = new ArrayList<>();
    }

    public ArrayList<Contact> getSelectList(){

        return mSelectList;
    }

    public void setSelectList(List<Contact> selectList){

        mSelectList = new ArrayList<>();
        mSelectList.addAll(selectList);
        notifyDataSetChanged();
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

        final Contact data = getItem(position);
        holder.text_user_name.setText(""+data.getMemberName());
        holder.text_user_subInfo.setText(""+data.getMobileNumber());

        holder.image_user_checkbox.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view){
                if(mSelectList.contains(data)){
                    mSelectList.remove(data);
                }else{
                    mSelectList.add(data);
                }
                ((SelectContactActivity)getContext()).checkTotal();
                notifyDataSetChanged();
            }
        });

        holder.image_user_checkbox.setSelected(mSelectList.contains(data));

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
