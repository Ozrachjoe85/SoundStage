import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Recycler
        RecyclerView recycler = findViewById(R.id.recyclerTracks);
        recycler.setLayoutManager(new LinearLayoutManager(this));
        recycler.setAdapter(new TrackAdapter(Arrays.asList(
                "Neon Skyline",
                "Analog Dreams",
                "Midnight Circuit",
                "Static Pulse",
                "Echo Protocol"
        )));

        // Dial animation
        View dial = findViewById(R.id.dial);
        Animation pressAnim = AnimationUtils.loadAnimation(this, R.anim.scale_press);

        dial.setOnClickListener(v -> {
            v.startAnimation(pressAnim);
        });
    }
}
