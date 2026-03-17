package com.example.soundstage;

import android.Manifest;
import android.content.pm.PackageManager;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private MediaRecorder recorder;
    private Handler handler = new Handler();

    private LinearLayout visualizer;
    private RecyclerView recycler;
    private TrackAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // ===== RecyclerView =====
        recycler = findViewById(R.id.recyclerTracks);
        recycler.setLayoutManager(new LinearLayoutManager(this));

        ArrayList<String> tracks = new ArrayList<>();
        tracks.add("Track One");
        tracks.add("Track Two");
        tracks.add("Track Three");

        adapter = new TrackAdapter(tracks);
        recycler.setAdapter(adapter);

        // ===== Swipe Gestures =====
        ItemTouchHelper.SimpleCallback swipeCallback =
                new ItemTouchHelper.SimpleCallback(0,
                        ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {

                    @Override
                    public boolean onMove(@NonNull RecyclerView recyclerView,
                                          @NonNull RecyclerView.ViewHolder viewHolder,
                                          @NonNull RecyclerView.ViewHolder target) {
                        return false;
                    }

                    @Override
                    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                        int position = viewHolder.getAdapterPosition();
                        adapter.tracks.remove(position);
                        adapter.notifyItemRemoved(position);
                    }
                };

        new ItemTouchHelper(swipeCallback).attachToRecyclerView(recycler);

        // ===== Dial =====
        View dial = findViewById(R.id.dial);

        dial.setOnTouchListener(new View.OnTouchListener() {
            float centerX, centerY;

            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    centerX = v.getX() + v.getWidth() / 2f;
                    centerY = v.getY() + v.getHeight() / 2f;
                }

                float dx = event.getRawX() - centerX;
                float dy = event.getRawY() - centerY;

                double angle = Math.toDegrees(Math.atan2(dy, dx));
                v.setRotation((float) angle);

                return true;
            }
        });

        // Press animation
        dial.setOnClickListener(v -> {
            v.animate()
                    .scaleX(0.9f)
                    .scaleY(0.9f)
                    .setDuration(100)
                    .withEndAction(() -> v.animate()
                            .scaleX(1f)
                            .scaleY(1f)
                            .setDuration(100));
        });

        // ===== Visualizer =====
        visualizer = findViewById(R.id.visualizerContainer);

        // Permission
        if (checkSelfPermission(Manifest.permission.RECORD_AUDIO)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.RECORD_AUDIO}, 1);
        } else {
            startVisualizer();
        }
    }

    private void startVisualizer() {
        recorder = new MediaRecorder();
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        recorder.setOutputFile("/dev/null");

        try {
            recorder.prepare();
            recorder.start();
        } catch (Exception e) {
            e.printStackTrace();
        }

        handler.post(updateVisualizer);
    }

    private Runnable updateVisualizer = new Runnable() {
        @Override
        public void run() {
            if (recorder != null) {
                int amplitude = recorder.getMaxAmplitude();

                for (int i = 0; i < visualizer.getChildCount(); i++) {
                    View bar = visualizer.getChildAt(i);

                    int height = (amplitude / 1000) + 20;
                    bar.getLayoutParams().height = height;
                    bar.requestLayout();
                }
            }

            handler.postDelayed(this, 100);
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (recorder != null) {
            recorder.release();
            recorder = null;
        }
    }
}
