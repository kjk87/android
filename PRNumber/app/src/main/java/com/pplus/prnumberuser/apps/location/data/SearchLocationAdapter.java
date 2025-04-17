package com.pplus.prnumberuser.apps.location.data;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.pplus.prnumberuser.R;
import com.pplus.prnumberuser.core.network.model.response.result.ResultDaumKeyword;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by 김종경 on 2015-06-17.
 */
public class SearchLocationAdapter extends RecyclerView.Adapter<SearchLocationAdapter.ViewHolder>{

    private Context context;
    private List<ResultDaumKeyword.Document> mDataList;
    private OnItemClickListener listener;

    public interface OnItemClickListener{

        void onItemClick(int position);
    }

    public SearchLocationAdapter(Context context){

        this.context = context;
        this.mDataList = new ArrayList<>();
    }

    public void setOnItemClickListener(OnItemClickListener listener){

        this.listener = listener;
    }

    public ResultDaumKeyword.Document getItem(int position){

        return mDataList.get(position);
    }

    public void add(ResultDaumKeyword.Document data){

        if(mDataList == null) {
            mDataList = new ArrayList<>();
        }
        mDataList.add(data);
        notifyDataSetChanged();
    }

    public void addAll(List<ResultDaumKeyword.Document> dataList){

        if(this.mDataList == null) {
            this.mDataList = new ArrayList<>();
        }

        this.mDataList.addAll(dataList);
        notifyDataSetChanged();
    }

    public void replaceData(int position, ResultDaumKeyword.Document data){

        mDataList.remove(position);
        mDataList.add(position, data);
        notifyDataSetChanged();
    }

    public void clear(){

        mDataList = new ArrayList<>();
        notifyDataSetChanged();
    }

    public ResultDaumKeyword.Document getData(int position){

        return mDataList.get(position);
    }

    public void setDataList(List<ResultDaumKeyword.Document> dataList){

        this.mDataList = dataList;
        notifyDataSetChanged();
    }

    public List<ResultDaumKeyword.Document> getDataList(){

        return mDataList;
    }

    static class ViewHolder extends RecyclerView.ViewHolder{

        public TextView textAddress;

        public ViewHolder(View itemView){

            super(itemView);
            textAddress = (TextView) itemView.findViewById(R.id.text_search_location_address);
            textAddress.setSingleLine();
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_search_location, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position){

        final ResultDaumKeyword.Document item = mDataList.get(position);

        holder.textAddress.setText(item.getPlace_name() + " - " + item.getAddress_name());

        holder.itemView.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v){
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
