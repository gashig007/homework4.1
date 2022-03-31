package com.example.homework41.board;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.example.homework41.Model.Board;
import com.example.homework41.R;
import com.example.homework41.databinding.PagerBoardBinding;

import java.util.ArrayList;

public class BoardAdapter extends RecyclerView.Adapter<BoardAdapter.BoardViewHolder> {
    private ArrayList<Board> list;
    private int[] lottie = new int[]{R.raw.city, R.raw.city, R.raw.city};

    public BoardAdapter() {
        list = new ArrayList<>();
        list.add(new Board("Name", "Rustam"));
        list.add(new Board("Name", "Rustam"));
        list.add(new Board("Name", "Rustam"));
    }

    @NonNull
    @Override
    public BoardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        PagerBoardBinding view = PagerBoardBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new BoardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BoardViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class BoardViewHolder extends RecyclerView.ViewHolder {
        private PagerBoardBinding binding;

        public BoardViewHolder(@NonNull PagerBoardBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;
        }

        public void bind(int position) {
            Board board = list.get(position);
            binding.textTitle.setText(board.getTitle());
            binding.description.setText(board.getDesc());
            binding.animationView.setAnimation(lottie[position]);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {

                }

            }, 2000);
            if (position == list.size() - 1) {
                binding.btnStart.setVisibility(View.VISIBLE);
            } else {
                binding.btnStart.setVisibility(View.INVISIBLE);
            }
            binding.btnStart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    NavController navController = Navigation.findNavController((Activity) view.getContext(), R.id.nav_host_fragment_activity_main);
                    navController.navigateUp();
                }
            });
        }
    }
}
