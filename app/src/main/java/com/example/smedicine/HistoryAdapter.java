package com.example.smedicine;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smedicine.bean.History;

import java.util.List;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolder> {

    private Context mContext;

    private List<History> mHistoryList;

    static class ViewHolder extends RecyclerView.ViewHolder {

        TextView history_time;
        TextView history_medicine_name;
        TextView history_times;
        TextView history_num;

        public ViewHolder(View view) {
            super(view);
            history_time = (TextView) view.findViewById(R.id.history_time);
            history_medicine_name = (TextView) view.findViewById(R.id.history_medicine_name);
            history_times = (TextView) view.findViewById(R.id.history_times);
            history_num = (TextView) view.findViewById(R.id.history_num);
        }
    }

    public HistoryAdapter(List<History> histories) {
        mHistoryList = histories;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if( mContext == null){
            mContext = parent.getContext();
        }
        View view = LayoutInflater.from(mContext).inflate(R.layout.medicine_history_item,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        History history = mHistoryList.get(position);
        holder.history_time.setText(history.getTime());
        holder.history_num.setText("用药剂量："+history.getNum()+"(片、包)");
        holder.history_times.setText("服药次数："+history.getTimes());
        holder.history_medicine_name.setText(history.getName());
    }

    @Override
    public int getItemCount() {
        return mHistoryList.size();
    }
}
