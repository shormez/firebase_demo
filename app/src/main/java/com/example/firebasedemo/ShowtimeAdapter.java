package com.example.firebasedemo;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.firebasedemo.databinding.ItemShowtimeBinding;
import com.example.firebasedemo.entity.Showtime;

import java.util.List;

public class ShowtimeAdapter extends RecyclerView.Adapter<ShowtimeAdapter.ShowtimeViewHolder> {

    private List<Showtime> showtimeList;
    private int selectedPosition = -1;
    private OnShowtimeClickListener listener;

    public interface OnShowtimeClickListener {
        void onShowtimeClick(Showtime showtime);
    }

    public ShowtimeAdapter(List<Showtime> showtimeList, OnShowtimeClickListener listener) {
        this.showtimeList = showtimeList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ShowtimeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemShowtimeBinding binding = ItemShowtimeBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ShowtimeViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ShowtimeViewHolder holder, int position) {
        Showtime showtime = showtimeList.get(position);
        holder.binding.tvShowTime.setText(showtime.getTime());
        holder.binding.tvTheaterName.setText(showtime.getTheaterName());
        holder.binding.tvPrice.setText(String.format("%,.0fđ", showtime.getPrice()));

        // Highlight selected item
        if (selectedPosition == position) {
            holder.binding.cardShowtime.setCardBackgroundColor(Color.LTGRAY);
        } else {
            holder.binding.cardShowtime.setCardBackgroundColor(Color.WHITE);
        }

        holder.itemView.setOnClickListener(v -> {
            int previousPosition = selectedPosition;
            selectedPosition = holder.getAdapterPosition();
            notifyItemChanged(previousPosition);
            notifyItemChanged(selectedPosition);
            listener.onShowtimeClick(showtime);
        });
    }

    @Override
    public int getItemCount() {
        return showtimeList.size();
    }

    public static class ShowtimeViewHolder extends RecyclerView.ViewHolder {
        ItemShowtimeBinding binding;

        public ShowtimeViewHolder(ItemShowtimeBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
