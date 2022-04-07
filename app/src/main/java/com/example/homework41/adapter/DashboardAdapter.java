package com.example.homework41.adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.homework41.Model.Model;
import com.example.homework41.databinding.ItemBinding;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class DashboardAdapter extends RecyclerView.Adapter<DashboardAdapter.DashboardViewHolder> {
private List<Model> list;

    public void setList(List<Model> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public DashboardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemBinding view = ItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new DashboardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DashboardViewHolder holder, int position) {
holder.bind(list.get(position));
        if(position % 2 == 0){
            holder.binding.getRoot().setBackgroundColor(Color.GRAY);
        }
        else {
            holder.binding.getRoot().setBackgroundColor(Color.CYAN);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    protected static class DashboardViewHolder extends RecyclerView.ViewHolder{
private ItemBinding binding;
        public DashboardViewHolder(@NonNull ItemBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;
        }

        public void bind(Model s) {
            binding.tvTitle.setText(s.getTitle());
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss, dd MMM yyyy", Locale.ROOT);
            String date = String.valueOf(simpleDateFormat.format(s.getCreate()));
            binding.tvDate.setText(date);
        }
    }
}
