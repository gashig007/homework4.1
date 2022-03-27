package com.example.homework41.adapter;

import android.graphics.Color;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.example.homework41.Model.Model;
import com.example.homework41.OnClick;
import com.example.homework41.databinding.ItemProfileBinding;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.function.Function;

public class ProfileAdapter extends RecyclerView.Adapter<ProfileAdapter.ProfileViewHolder> {
    private List<Model> data;
    private OnClick onClick;

    public ProfileAdapter() {
        this.data = new ArrayList<Model>();
    }

    @NonNull
    @Override
    public ProfileViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemProfileBinding view = ItemProfileBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);;
        return new ProfileViewHolder(view);
    }

    public void setData(List<Model> list) {
        this.data = list;
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(@NonNull ProfileViewHolder holder, int position) {
        holder.bind(data.get(position));
        if(position % 2 == 0){
            holder.binding.getRoot().setBackgroundColor(Color.GRAY);
        }
        else {
            holder.binding.getRoot().setBackgroundColor(Color.CYAN);
        }
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

    public Model getItem(int position) {
        return data.get(position);
    }


    public void updateItem(Model s, int position) {
        data.set(position, s);
        notifyItemChanged(position);
    }
    public void deleteItem(int position){
        data.remove(position);
        notifyItemRemoved(position);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void addList(List<Model> list){
        Comparator<Model> comparator = Comparator.comparing(new Function<Model, Long>() {
            @Override
            public Long apply(Model model) {
                return model.getCreate();
            }
        });
        data = list;
        data.sort(comparator);
        Collections.reverse(data);
        notifyDataSetChanged();
    }

    class ProfileViewHolder extends RecyclerView.ViewHolder {
        private ItemProfileBinding binding;

        public ProfileViewHolder(@NonNull ItemProfileBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;
            binding.getRoot().setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    onClick.onLongClick(getAdapterPosition());
                    return true;
                }
            });
            binding.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onClick.onClick(getAdapterPosition());
                }
            });
        }

        public void bind(Model s) {
            binding.title.setText(s.getTitle());
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss, dd MMM yyyy", Locale.ROOT);
            String date = String.valueOf(simpleDateFormat.format(s.getCreate()));
            binding.tvDate.setText(date);
        }
    }
}
