package com.example.firebasedemo;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;
import com.example.firebasedemo.databinding.ActivityMovieDetailBinding;
import com.example.firebasedemo.entity.Movie;
import com.example.firebasedemo.entity.Showtime;
import com.example.firebasedemo.entity.Ticket;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MovieDetailActivity extends AppCompatActivity {

    private ActivityMovieDetailBinding binding;
    private Movie movie;
    private FirebaseFirestore db;
    private List<Showtime> showtimeList;
    private ShowtimeAdapter adapter;
    private Showtime selectedShowtime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMovieDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        db = FirebaseFirestore.getInstance();
        movie = (Movie) getIntent().getSerializableExtra("movie");

        if (movie != null) {
            displayMovieDetails();
            setupShowtimesRecyclerView();
            fetchShowtimes();
        }

        binding.btnBookTicket.setOnClickListener(v -> {
            if (selectedShowtime == null) {
                Toast.makeText(this, "Vui lòng chọn suất chiếu", Toast.LENGTH_SHORT).show();
                return;
            }
            bookTicket();
        });
    }

    private void displayMovieDetails() {
        binding.tvDetailTitle.setText(movie.getTitle());
        binding.tvDetailGenre.setText("Thể loại: " + movie.getGenre());
        binding.tvDetailDuration.setText("Thời lượng: " + movie.getDuration() + " phút");
        binding.tvDetailDesc.setText(movie.getDescription());
        Glide.with(this).load(movie.getPosterUrl()).into(binding.imgDetailPoster);
    }

    private void setupShowtimesRecyclerView() {
        showtimeList = new ArrayList<>();
        adapter = new ShowtimeAdapter(showtimeList, showtime -> {
            selectedShowtime = showtime;
        });
        binding.rvShowtimes.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        binding.rvShowtimes.setAdapter(adapter);
    }

    private void fetchShowtimes() {
        db.collection("showtimes")
                .whereEqualTo("movieId", movie.getId())
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        showtimeList.clear();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Showtime showtime = document.toObject(Showtime.class);
                            showtimeList.add(showtime);
                        }
                        adapter.notifyDataSetChanged();
                    }
                });
    }

    private void bookTicket() {
        String userId = FirebaseAuth.getInstance().getUid();
        if (userId == null) return;

        Ticket ticket = new Ticket();
        ticket.setUserId(userId);
        ticket.setMovieId(movie.getId());
        ticket.setMovieTitle(movie.getTitle());
        ticket.setTheaterName(selectedShowtime.getTheaterName());
        ticket.setShowtime(selectedShowtime.getTime() + " " + selectedShowtime.getDate());
        ticket.setTotalPrice(selectedShowtime.getPrice());
        ticket.setTimestamp(Timestamp.now());


        db.collection("tickets")
                .add(ticket)
                .addOnSuccessListener(documentReference -> {
                    Toast.makeText(this, "Đặt vé thành công!", Toast.LENGTH_LONG).show();

                    scheduleNotification(movie.getTitle(), selectedShowtime.getDate(), selectedShowtime.getTime());
                    
                    finish();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Lỗi: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private void scheduleNotification(String movieTitle, String dateStr, String timeStr) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
        try {
            Date showDate = sdf.parse(dateStr + " " + timeStr);
            System.out.println("Show date: " + showDate);
            if (showDate != null) {

                long alarmTime = showDate.getTime() - (30 * 60 * 1000);
                

                if (alarmTime < System.currentTimeMillis()) {
                    alarmTime = System.currentTimeMillis() + 5000;
                }
                System.out.println("Alarm time: " + alarmTime);

                Intent intent = new Intent(this, NotificationReceiver.class);
                intent.putExtra("movieTitle", movieTitle);
                
                PendingIntent pendingIntent = PendingIntent.getBroadcast(
                        this, 
                        (int) System.currentTimeMillis(), 
                        intent, 
                        PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
                );

                AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                if (alarmManager != null) {
                    alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, alarmTime, pendingIntent);
                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}
