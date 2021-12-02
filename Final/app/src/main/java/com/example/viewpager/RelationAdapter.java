package com.example.viewpager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RelationAdapter extends RecyclerView.Adapter<RelationAdapter.CustomViewHolder> {

    private ArrayList<RelationData> relationList;

    public RelationAdapter(ArrayList<RelationData> relationList) {
        this.relationList = relationList;
    }

    @NonNull
    @Override
    public RelationAdapter.CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_protectorlist_item,parent,false);
        CustomViewHolder holder = new CustomViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RelationAdapter.CustomViewHolder holder, int position) {
        holder.tv_name.setText(relationList.get(position).getProtector_Name());
        Log.e("보호자 리스트",holder.tv_name.getText().toString());
        holder.btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //db삭제
                relationList.remove(holder.getAdapterPosition());
            }
        });
    }

    @Override
    public int getItemCount() {
        return (null != relationList ? relationList.size() : 0);
    }

    public void remove(int position){
        try{
            relationList.remove(position);
            notifyItemRemoved(position);
        }catch (IndexOutOfBoundsException e){
            e.printStackTrace();
        }
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {

        protected TextView tv_name;
        protected ImageButton btn_delete;

        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);

            tv_name = itemView.findViewById(R.id.tv_protectorlistitem_name);
            btn_delete = itemView.findViewById(R.id.btn_protectorlistitem_delete);
        }
    }
}
