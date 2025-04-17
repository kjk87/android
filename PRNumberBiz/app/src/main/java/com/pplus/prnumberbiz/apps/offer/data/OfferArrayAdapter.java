package com.pplus.prnumberbiz.apps.offer.data;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.pplus.prnumberbiz.R;
import com.pplus.prnumberbiz.apps.common.ui.base.BaseArrayAdapter;


/**
 * Created by j2n on 2016. 12. 22..
 */

public class OfferArrayAdapter extends BaseArrayAdapter<Object>{

    public OfferArrayAdapter(Context context){

        super(context, R.layout.item_history);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){

        ViewHolder viewHolder;

        if(convertView == null) {

            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_offer, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.initialize(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        //        Post postData = getItem(position);

        return convertView;
    }

    class ViewHolder{

        ImageView image_profile, image_offer;
        TextView text_name, text_title, text_contents;
        public Context context;

        public void initialize(View itemView){

            context = itemView.getContext();

            image_profile = (ImageView) itemView.findViewById(R.id.image_offer_profile);
            text_name = (TextView) itemView.findViewById(R.id.text_offer_name);
            image_offer = (ImageView) itemView.findViewById(R.id.image_offer);
            text_title = (TextView) itemView.findViewById(R.id.text_offer_title);
            text_contents = (TextView) itemView.findViewById(R.id.text_offer_contents);

        }
    }
}
