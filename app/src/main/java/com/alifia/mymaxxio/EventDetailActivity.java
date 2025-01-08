package com.alifia.mymaxxio;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.alifia.mymaxxio.model.Event;
import com.bumptech.glide.Glide;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class EventDetailActivity extends AppCompatActivity {
    private TextView eventTitle, eventDescription, description;
    private ImageView eventImage;
    private Button unggahanBtn, btnHadir;
    private MaterialToolbar eventToolbar;
    private FirebaseFirestore store;
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_event_detail);

        store = FirebaseFirestore.getInstance();
        userId = FirebaseAuth.getInstance().getUid();

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

        btnHadir = findViewById(R.id.btn_hadir);

        unggahanBtn = findViewById(R.id.unggahanBtn);
        unggahanBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bundleEventID != null) {
                    Intent intent = new Intent(EventDetailActivity.this, UnggahanKegiatan.class);
                    intent.putExtra("event", bundleEventID);
                    startActivity(intent);
                }
            }
        });

        // Bundle bundleEventID = getIntent().getExtras();
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

                            cekApakahIniKegiatanSaya(userId, bundleEventID);
                        }
                    });
        }
    }

    private void cekApakahIniKegiatanSaya(String userId, String bundleEventID) {
        store.collection("kegiatan_saya")
                .whereEqualTo("uid", userId)
                .whereEqualTo("eventId", bundleEventID)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && !task.getResult().isEmpty()) {
                        // Event already in 'kegiatan_saya', show 'Remove' button
                        //btnHadir.setText("Hapus dari Kegiatan Saya");
                        setButtonState(btnHadir, "Hapus dari Kegiatan Saya", R.color.merah_danger);
                        btnHadir.setOnClickListener(v -> hapusDariKegiatanSaya(userId, bundleEventID));
                    } else {
                        // Event not in 'kegiatan_saya', show 'Add' button
                        //btnHadir.setText("Tambahkan ke Kegiatan Saya");
                        setButtonState(btnHadir, "Tandai sebagai 'Hadir'", R.color.ijo_sukses);
                        btnHadir.setOnClickListener(v -> tambahKeKegiatanSaya(userId, bundleEventID));
                    }
                });
    }

    private void setButtonState(Button button, String text, int colorResId) {
        button.setText(text);
        button.setBackgroundColor(getResources().getColor(colorResId));
    }

    private void tambahKeKegiatanSaya(String userId, String bundleEventID) {
        Map<String, Object> myActivity = new HashMap<>();
        myActivity.put("uid", userId);
        myActivity.put("eventId", bundleEventID);

        store.collection("kegiatan_saya")
                .add(myActivity)
                .addOnSuccessListener(documentReference -> {
                    setButtonState(btnHadir, "Tandai sebagai 'Hadir'", R.color.ijo_sukses);
                    btnHadir.setOnClickListener(v -> hapusDariKegiatanSaya(userId, bundleEventID));
                    Toast.makeText(this, "Kegiatan ditambahkan", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> Toast.makeText(this, "Gagal menambahkan kegiatan", Toast.LENGTH_SHORT).show());
    }

    private void hapusDariKegiatanSaya(String userId, String bundleEventID) {
        store.collection("kegiatan_saya")
                .whereEqualTo("uid", userId)
                .whereEqualTo("eventId", bundleEventID)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (DocumentSnapshot document : queryDocumentSnapshots) {
                        document.getReference().delete()
                                .addOnSuccessListener(aVoid -> {
                                    setButtonState(btnHadir, "Hapus dari Kegiatan Saya", R.color.merah_danger);
                                    btnHadir.setOnClickListener(v -> tambahKeKegiatanSaya(userId, bundleEventID));
                                    Toast.makeText(this, "Kegiatan dihapus", Toast.LENGTH_SHORT).show();
                                })
                                .addOnFailureListener(e -> Toast.makeText(this, "Gagal menghapus kegiatan", Toast.LENGTH_SHORT).show());
                    }
                });
    }


}