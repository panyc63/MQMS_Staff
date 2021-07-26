package com.example.mqms_staff.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mqms_staff.Classes.customer;
import com.example.mqms_staff.R;

import java.util.ArrayList;

public class customerAdapter extends RecyclerView.Adapter<customerAdapter.customerViewHolder> {

    private ArrayList<customer> customerList;
    private OnItemListener onItemListener;

    public customerAdapter(ArrayList<customer> customerList , OnItemListener onItemListener){
        this.customerList = customerList;
        this.onItemListener = onItemListener;
    }



    @NonNull
    @Override
    public customerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_item,parent,false);
        return new customerViewHolder(view,onItemListener);
    }

    @Override
    public void onBindViewHolder(@NonNull customerViewHolder holder, int position) {
        holder.name.setText("Name "+ customerList.get(position).getName());
        holder.nric.setText("NRIC: " + customerList.get(position).getNric());
        holder.queueNo.setText("Queue No: "+ customerList.get(position).getQueueNo());
    }

    @Override
    public int getItemCount() {
        return customerList.size();
    }

    class customerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView name,queueNo,nric;
        OnItemListener onItemListener;

        public customerViewHolder(@NonNull View itemView,OnItemListener onItemListener) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.tvCustName_userItem);
            nric = (TextView) itemView.findViewById(R.id.tvCustNRIC_userItem);
            queueNo = (TextView) itemView.findViewById(R.id.tvQueueNo_userItem);
            this.onItemListener = onItemListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onItemListener.onItemClick(getAdapterPosition());
        }
    }
    public interface OnItemListener{
        void onItemClick(int position);
    }


}
