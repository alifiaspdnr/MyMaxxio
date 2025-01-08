package com.alifia.mymaxxio;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.alifia.mymaxxio.model.Event;
import com.bumptech.glide.Glide;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

public class EventDetailDalamReviewActivity extends AppCompatActivity {

    private TextView eventTitle, eventDescription, description;
    private ImageView eventImage;
    private MaterialToolbar eventToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_event_detail_dalam_review);

        eventTitle = findViewById(R.id.tvEventTitle);
        eventDescription = findViewById(R.id.tvEventDescription);
        description = findViewById(R.id.tvDescription);
        eventImage = findViewById(R.id.eventImage);

        eventToolbar = findViewById(R.id.eventToolbar);
        eventToolbar.setNavigationOnClickListener(v -> {
            finish();
        });

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        String bundleEventID = bundle.getString("eventId");
        Log.d("TAG", bundleEventID);

        if (bundleEventID != null) {
            FirebaseFirestore store = FirebaseFirestore.getInstance();

            store.collection("kegiatan")
                    .document(bundleEventID)
                    .get()
                    .addOnSuccessListener(documentSnapshot -> {
                        if (documentSnapshot != null) {
                            Event event = documentSnapshot.toObject(Event.class);
                            eventTitle.setText(event.getNamaKegiatan());
                            eventDescription.setText(event.getDeskripsiSingkat());
                            description.setText(event.getDeskripsi());
                            if (Objects.equals(event.getImageUrl(), "")) {
                                eventImage.setImageResource(R.drawable.img_placeholder);
                            } else {
                                Glide.with(this)
                                        .load(event.getImageUrl())
                                        .into(eventImage);
                            }
                        }
                    });
        }
    }
}