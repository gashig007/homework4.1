package com.example.homework41.adapter;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.homework41.Model;
import com.example.homework41.OnClick;
import com.example.homework41.R;
import com.example.homework41.databinding.ItemProfileBinding;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

public class ProfileAdapter extends RecyclerView.Adapter<ProfileAdapter.ProfileViewHolder> {
    private ArrayList<String> data;
    private OnClick onClick;

    public ProfileAdapter() {
        data = new ArrayList<>();
    }

    @NonNull
    @Override
    public ProfileViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemProfileBinding view = ItemProfileBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);;
        return new ProfileViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProfileViewHolder holder, int position) {
        holder.bind(data.get(position));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void addItem(Model s) {
        data.add(0, s);
        notifyItemInserted(data.indexOf(s));
    }

    public void setOnClickListener(OnClick onClick) {
        this.onClick = onClick;
    }

    public String getItem(int position) {
        return data.get(position);
    }


    public void updateItem(Model s, int position) {
        data.set(position, s);
        notifyItemChanged(position);
    }

    class ProfileViewHolder extends RecyclerView.ViewHolder {
        private ItemProfileBinding binding;

        public ProfileViewHolder(@NonNull ItemProfileBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;
            binding.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onClick.onClick(getAdapterPosition());
                    Bundle bundle = new Bundle();
                    bundle.putString("text", binding.text.getText().toString());
                }
            });
        }

        public void bind(Model s) {
            binding.text.setText(s.getTitle());
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss, dd MMM yyyy", Locale.ROOT);
            String date = String.valueOf(simpleDateFormat.format(s.getCreate()));
            binding.title.setText(date);
        }
    }
}
