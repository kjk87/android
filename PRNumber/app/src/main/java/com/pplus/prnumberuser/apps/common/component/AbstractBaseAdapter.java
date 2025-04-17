package com.pplus.prnumberuser.apps.common.component;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by j2n on 2016. 10. 7..
 */

public abstract class AbstractBaseAdapter<E extends Object, VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH>{

    private List<E> dataList;

    public AbstractBaseAdapter(){

        dataList = new ArrayList<E>();
    }

    @Override
    public int getItemCount(){

        return dataList.size();
    }

    public E getItem(int position){

        return dataList.get(position);
    }

    public void add(E data){

        if(dataList == null) {
            dataList = new ArrayList<>();
        }
        dataList.add(data);
        notifyDataSetChanged();
    }

    public void add(int position,E data){

        if(dataList == null) {
            dataList = new ArrayList<>();
        }
        dataList.add(position,data);
        notifyDataSetChanged();
    }

    public void addNotNotifyData(E data){

        if(dataList == null) {
            dataList = new ArrayList<>();
        }
        dataList.add(data);
        notifyDataSetChanged();
    }

    public void addAll(List<E> dataList){

        if(this.dataList == null) {
            this.dataList = new ArrayList<>();
        }

        this.dataList.addAll(dataList);
        notifyDataSetChanged();
    }

    public void replaceData(int position, E data){

        this.dataList.remove(position);
        this.dataList.add(position, data);
        notifyDataSetChanged();
    }

    public void clear(){

        this.dataList = new ArrayList<>();
        notifyDataSetChanged();
    }

    public void setDataList(List<E> dataList){

        this.dataList = dataList;
        notifyDataSetChanged();
    }

    public List<E> getDataList(){

        return dataList;
    }

    public onItemClickListener<E> onItemClickListener;

    public AbstractBaseAdapter.onItemClickListener<E> getOnItemClickListener(){

        return onItemClickListener;
    }

    public void setOnItemClickListener(AbstractBaseAdapter.onItemClickListener<E> onItemClickListener){

        this.onItemClickListener = onItemClickListener;
    }

    public interface onItemClickListener<E>{
        void onItemClick(E result);
        void onProfile(long memberSeq);
        void onPage(long pageSeq);
    }

}
