package com.pplus.prnumberbiz.apps.setting.data;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.pple.pplus.utils.part.utils.time.DateFormatUtils;
import com.pplus.prnumberbiz.R;
import com.pplus.prnumberbiz.core.network.model.dto.Notice;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ksh on 2016-09-28.
 */

public class NoticeAdapter extends RecyclerView.Adapter<NoticeAdapter.ViewHolder>{

    private static final String TAG = NoticeAdapter.class.getSimpleName();
    private ArrayList<Notice> list;
    private OnItemClickListener listener;
    private Context mContext;

    public NoticeAdapter(Context context){

        list = new ArrayList<>();
        mContext = context;
    }

    public NoticeAdapter(ArrayList<Notice> mItemList){

        this.list = mItemList;
    }

    public interface OnItemClickListener{

        void onItemClick(int position);
    }

    public Notice getItem(int postion){

        return list != null ? list.get(postion) : null;
    }

    public void setOnItemClickListener(OnItemClickListener listener){

        this.listener = listener;
    }

    public void addAll(List<Notice> channels){

        list.addAll(channels);
        notifyDataSetChanged();
    }

    public void clear(){

        list.clear();
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

        final Notice item = list.get(position);

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
            String date = DateFormatUtils.convertDateFormat(item.getDuration().getStart(), "yyyy.MM.dd");
            holder.text_date.setText(date); // 날짜 변환
        }
    }

    @Override
    public int getItemCount(){

        return list != null ? list.size() : null;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView text_subject, text_date;

        public ViewHolder(View itemView){

            super(itemView);

            text_subject = (TextView) itemView.findViewById(R.id.text_subject);
            text_subject.setSingleLine();
            text_date = (TextView) itemView.findViewById(R.id.text_date);
        }

    }


}
