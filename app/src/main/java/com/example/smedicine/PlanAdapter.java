package com.example.smedicine;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.smedicine.bean.PlanShow;
import java.util.List;

public class PlanAdapter extends RecyclerView.Adapter<PlanAdapter.ViewHolder> {
    private Context mContext;

    private List<PlanShow> mplanShow;

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView medicineImg;
        TextView medicineName;
        TextView medicineTime;
        TextView medicneNum;

        public ViewHolder(View view) {
            super(view);
            medicineImg = (ImageView) view.findViewById(R.id.medicine_image_plan_item);
            medicineName = (TextView) view.findViewById(R.id.medicine_name_plan_item);
            medicineTime = (TextView) view.findViewById(R.id.medicine_time_plan_item);
            medicneNum = (TextView) view.findViewById(R.id.medicine_num_plan_item);
        }
    }

    public PlanAdapter(List<PlanShow> planShows) {
        mplanShow = planShows;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        if (mContext == null) {
            mContext = parent.getContext();
        }
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.medicine_item, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        PlanShow planShow = mplanShow.get(position);
        holder.medicineName.setText(planShow.getMedicineName());
        holder.medicineTime.setText("时间："+planShow.getHour() + ":" + planShow.getMinute());
        holder.medicneNum.setText(planShow.getNum()+"(包、片、粒)");
//        //glide添加参数Uri.encode(bean?.taskName, "utf-8")
        String url = "http://39.105.11.138:8881/files/" + planShow.getMedicineName()  + ".jpg";
        Glide.with(mContext).load(url).into(holder.medicineImg);
        //Glide.with(mContext).load("http://39.105.11.138:8881/files/%E8%AF%BA%E6%B0%9F%E6%B2%99%E6%98%9F%E8%83%B6%E5%9B%8A%20(%E4%B8%87%E9%80%9A).jpg").into(holder.medicineImg);
    }

    @Override
    public int getItemCount() {
        return mplanShow.size();
    }



}
