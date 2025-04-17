package com.pplus.prnumberuser.apps.search.data;

import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.pplus.prnumberuser.R;
import com.pplus.prnumberuser.apps.common.mgmt.SearchHistoryManager;

import java.util.ArrayList;

/**
 * Created by Windows7-00 on 2016-12-23.
 */

public class SearchWordAdapter extends RecyclerView.Adapter<SearchWordAdapter.ViewHolder>{

    private SearchWordAdapter.OnItemClickListener listener;
    private SearchWordAdapter.DataChangedListener mDataChangedListener;

    private int[] resIds = {R.drawable.border_color_ffedf5, R.drawable.border_color_ffe0e0, R.drawable.border_color_ffeee4,
            R.drawable.border_color_fffbdc, R.drawable.border_color_ecfff7, R.drawable.border_color_e0f4ff, R.drawable.border_color_f1edff};

    public interface OnItemClickListener{

        void onItemClick(int position);
    }

    public interface DataChangedListener{

        void onChanged(int position);
    }

    private ArrayList<String> mDataList;
    private boolean mEnableDelete;
    private boolean isPageConfig = false;

    public void setPageConfig(boolean isPageConfig) {
        this.isPageConfig = isPageConfig;
    }

    public SearchWordAdapter(boolean enableDelete){

        this.mDataList = new ArrayList<>();
        this.mEnableDelete = enableDelete;
    }

    public void setDataChangedListener(SearchWordAdapter.DataChangedListener dataChangedListener){

        this.mDataChangedListener = dataChangedListener;
    }

    public void setOnItemClickListener(SearchWordAdapter.OnItemClickListener listener){

        this.listener = listener;
    }

    public void add(String data){

        if(mDataList == null) {
            mDataList = new ArrayList<>();
        }
        mDataList.add(data);
        notifyDataSetChanged();
    }

    public void addAll(ArrayList<String> dataList){

        if(this.mDataList == null) {
            this.mDataList = new ArrayList<>();
        }

        this.mDataList.addAll(dataList);
        notifyDataSetChanged();
    }

    public void clear(){

        mDataList = new ArrayList<>();
        notifyDataSetChanged();
    }

    public void setDataList(ArrayList<String> dataList){

        if(dataList != null) {
            this.mDataList = dataList;
            notifyDataSetChanged();
        }
    }

    public String getItem(int position){

        return mDataList.get(position);
    }

    static class ViewHolder extends RecyclerView.ViewHolder{

        RelativeLayout container;
        TextView textRecent, textPopular;
        ImageView imageDelete;
        View layoutRecent;

        public ViewHolder(View itemView){

            super(itemView);
            container = (RelativeLayout) itemView.findViewById(R.id.rl_item_container);
            layoutRecent = itemView.findViewById(R.id.layout_search_word_recent);
            textPopular = (TextView) itemView.findViewById(R.id.text_search_word_popular);
            textPopular.setSingleLine();
            textRecent = (TextView) itemView.findViewById(R.id.text_search_word_recent);
            textRecent.setSingleLine();
            imageDelete = (ImageView) itemView.findViewById(R.id.image_search_word_delete);
        }
    }

    @Override
    public SearchWordAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_search_word, parent, false);
        return new SearchWordAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final SearchWordAdapter.ViewHolder holder, int position){

        if (isPageConfig) {
            int colorBg = position % resIds.length;
            holder.container.setBackgroundResource(resIds[colorBg]);
        }

        if(mEnableDelete) {
            holder.layoutRecent.setVisibility(View.VISIBLE);
            holder.textPopular.setVisibility(View.GONE);
            holder.textRecent.setText(mDataList.get(position));
            holder.imageDelete.setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View view){

                    int position = holder.getAdapterPosition();
                    if (!isPageConfig) {
                        SearchHistoryManager.getInstance().delete(mDataList.get(position));
                    }
                    if(mDataChangedListener != null) {
                        mDataChangedListener.onChanged(position);
                    }
                    notifyDataSetChanged();
                }
            });
        } else {
            holder.layoutRecent.setVisibility(View.GONE);
            holder.textPopular.setVisibility(View.VISIBLE);
            holder.textPopular.setText(mDataList.get(position));
        }

        holder.itemView.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view){

                if(listener != null) {
                    listener.onItemClick(holder.getAdapterPosition());
                }
            }
        });

    }

    @Override
    public int getItemCount(){

        return mDataList.size();
    }
}
