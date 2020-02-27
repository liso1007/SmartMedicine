package com.example.smedicine;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.smedicine.bean.Medicine;

import java.util.List;

public class MedicineAdapter extends RecyclerView.Adapter<MedicineAdapter.ViewHolder> {
    private Context mContext;
    private List<Medicine> mMedicineList;

    static class ViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        ImageView medicineImage;
        TextView medicineName;
        TextView medicineYuliang;

        public ViewHolder(View view) {
            super(view);
            cardView = (CardView) view;
            medicineImage = (ImageView) view.findViewById(R.id.medicine_image_plan_item);
            medicineName = (TextView) view.findViewById(R.id.medicine_name_plan_item);
            medicineYuliang = (TextView)view.findViewById(R.id.medicine_yuliang_plan_item);
        }
    }

    public MedicineAdapter(List<Medicine> medicineList) {
        mMedicineList = medicineList ;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mContext == null) {
            mContext = parent.getContext();
        }
        View view = LayoutInflater.from(mContext).inflate(R.layout.medicine_index_item, parent, false);
        //设置触发点击事件
        final ViewHolder holder = new ViewHolder(view);
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                Medicine medicine = mMedicineList.get(position);
                //传递medicine对象
                Intent intent = new Intent(mContext,MedicineAcivity.class);
                intent.putExtra(MedicineAcivity.MEDICINE_OBJECT,medicine);
                mContext.startActivity(intent);
            }
        });
      return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Medicine medicine = mMedicineList.get(position);
        holder.medicineName.setText(medicine.getMedicinename());
        holder.medicineYuliang.setText("余量"+medicine.getRest());
        //glide添加参数
        Glide.with(mContext).load(medicine.getImg_url()).into(holder.medicineImage);
    }

    @Override
    public int getItemCount() {
        return mMedicineList.size();
    }
}
