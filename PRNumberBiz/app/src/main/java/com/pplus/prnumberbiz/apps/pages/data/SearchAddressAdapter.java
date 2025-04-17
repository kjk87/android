package com.pplus.prnumberbiz.apps.pages.data;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.pplus.prnumberbiz.R;
import com.pplus.prnumberbiz.core.network.model.dto.Juso;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by 김종경 on 2015-06-17.
 */
public class SearchAddressAdapter extends RecyclerView.Adapter<SearchAddressAdapter.ViewHolder>{

    private Context mContext;
    private List<Juso> mDataList;
    private OnItemClickListener listener;

    public interface OnItemClickListener{

        void onItemClick(int position);
    }

    public SearchAddressAdapter(Context context){

        setHasStableIds(true);

        this.mContext = context;
        this.mDataList = new ArrayList<>();
    }

    @Override
    public long getItemId(int position){

        return position;
    }

    public void setOnItemClickListener(OnItemClickListener listener){

        this.listener = listener;
    }

    public Juso getItem(int position){

        return mDataList.get(position);
    }

    public List<Juso> getDataList(){

        return mDataList;
    }

    public void add(Juso data){

        if(mDataList == null) {
            mDataList = new ArrayList<>();
        }
        mDataList.add(data);
        notifyDataSetChanged();
    }

    public void addAll(List<Juso> dataList){

        if(this.mDataList == null) {
            this.mDataList = new ArrayList<>();
        }

        this.mDataList.addAll(dataList);
        notifyDataSetChanged();
    }

    public void replaceData(int position, Juso data){

        mDataList.remove(position);
        mDataList.add(position, data);
        notifyDataSetChanged();
    }

    public void clear(){

        mDataList = new ArrayList<>();
        notifyDataSetChanged();
    }

    public void setDataList(List<Juso> dataList){

        this.mDataList = dataList;
        notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{

        private TextView text_roadAddress, text_jibunAddress, text_zipcode;

        public ViewHolder(View itemView){

            super(itemView);
            text_zipcode = (TextView) itemView.findViewById(R.id.text_search_zipcode);
            text_roadAddress = (TextView) itemView.findViewById(R.id.text_search_roadAddress);
            text_jibunAddress = (TextView) itemView.findViewById(R.id.text_search_jibunAddress);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_search_address, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position){

        final Juso item = mDataList.get(position);

        holder.text_zipcode.setText(item.getZipNo());
        holder.text_roadAddress.setText(item.getRoadAddr());
        holder.text_jibunAddress.setText(item.getJibunAddr());

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
