package com.pplus.prnumberbiz.apps.signup.data;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.pplus.prnumberbiz.R;

import java.util.ArrayList;

/**
 * Created by 김종경 on 2015-06-17.
 */
public class MobileCorpAdapter extends RecyclerView.Adapter<MobileCorpAdapter.ViewHolder>{

    private Context context;
    private ArrayList<MobileCorp> mDataList;
    private int select_postion = 0;

    public interface OnClickListener{

        void onClick(int position);
    }

    public class MobileCorp{
        String value;
        String name;

        public MobileCorp(String value, String name){

            this.value = value;
            this.name = name;
        }

        public String getValue(){

            return value;
        }

        public void setValue(String value){

            this.value = value;
        }

        public String getName(){

            return name;
        }

        public void setName(String name){

            this.name = name;
        }
    }

    public MobileCorpAdapter(Context context){

        this.context = context;
        mDataList = new ArrayList<>();
        mDataList.add(new MobileCorp("SKT", context.getString(R.string.word_skt)));
        mDataList.add(new MobileCorp("KTF", context.getString(R.string.word_kt)));
        mDataList.add(new MobileCorp("LGT", context.getString(R.string.word_lgt)));
        mDataList.add(new MobileCorp("SKM", context.getString(R.string.word_skm)));
        mDataList.add(new MobileCorp("KTM", context.getString(R.string.word_ktm)));
        mDataList.add(new MobileCorp("LGM", context.getString(R.string.word_lgm)));

    }

    public String getMobileCrop(){
        return mDataList.get(select_postion).getValue();
    }

    public void setDataList(ArrayList<MobileCorp> dataList){

        mDataList = new ArrayList<>();
        mDataList.addAll(dataList);
        notifyDataSetChanged();
    }

    public void add(MobileCorp data){

        mDataList.add(0, data);
        notifyDataSetChanged();
    }

    public void clear(){

        mDataList.clear();
        notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{

        public TextView text;

        public ViewHolder(View itemView){

            super(itemView);
            text = (TextView) itemView.findViewById(R.id.text_radio);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_radio, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position){

        final MobileCorp item = mDataList.get(position);
        holder.text.setText(item.getName());
        holder.itemView.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view){
                select_postion = holder.getAdapterPosition();
                notifyDataSetChanged();
            }
        });

        if(position == select_postion){
            holder.itemView.setSelected(true);
        }else{
            holder.itemView.setSelected(false);
        }

    }

    @Override
    public int getItemCount(){

        return mDataList.size();
    }
}
