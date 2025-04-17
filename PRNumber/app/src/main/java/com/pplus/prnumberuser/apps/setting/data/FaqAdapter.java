package com.pplus.prnumberuser.apps.setting.data;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.pplus.prnumberuser.R;
import com.pplus.prnumberuser.core.network.model.dto.Faq;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ksh on 2016-09-28.
 */

public class FaqAdapter extends RecyclerView.Adapter<FaqAdapter.ViewHolder>{

    private static final String TAG = FaqAdapter.class.getSimpleName();
    private ArrayList<Faq> mDataList;
    private OnItemClickListener listener;
    private Context mContext;

    public FaqAdapter(Context context){

        mDataList = new ArrayList<>();
        mContext = context;
    }
    public interface OnItemClickListener{

        void onItemClick(int position);
    }

    public Faq getItem(int postion){

        return mDataList != null ? mDataList.get(postion) : null;
    }

    public void setOnItemClickListener(OnItemClickListener listener){

        this.listener = listener;
    }

    public void addAll(List<Faq> data){

        mDataList.addAll(data);
        notifyDataSetChanged();
    }

    public void clear(){

        mDataList.clear();
    }

    @Override
    public int getItemViewType(int position){

        return position;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.common_item_two_line, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position){

        final Faq item = mDataList.get(position);

        holder.itemView.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view){

                if(listener != null) {
                    listener.onItemClick(holder.getAdapterPosition());
                }
            }
        });

        if(item != null) {

            holder.text_subject.setText(item.getSubject()); // 제목
        }
    }

    @Override
    public int getItemCount(){

        return mDataList != null ? mDataList.size() : null;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView text_subject, text_date;

        public ViewHolder(View itemView){

            super(itemView);

            text_subject = (TextView) itemView.findViewById(R.id.text_subject);
            text_subject.setMaxLines(2);
            text_date = (TextView) itemView.findViewById(R.id.text_date);
            text_date.setVisibility(View.GONE);
        }

    }


}
